package ru.clevertec.house.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Aspect
@Component
public class PersonAspect {

    private final CacheFactory<UUID, ResponsePerson> cacheFactory;
    private final Cache<UUID, ResponsePerson> cache;
    private final Lock lock;

    public PersonAspect(CacheFactory<UUID, ResponsePerson> cacheFactory) {
        this.cacheFactory = cacheFactory;
        this.cache = cacheFactory.createCache();
        lock = new ReentrantLock();
    }

    @Around("ru.clevertec.house.service.aop.PersonPointcut.pointcutGetMethod()")
    public ResponsePerson get(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            UUID uuid = (UUID) joinPoint.getArgs()[0];
            ResponsePerson response = cache.get(uuid);
            if (response == null) {
                response = (ResponsePerson) joinPoint.proceed();
            }
            cache.put(response.uuid(), response);
            return response;
        } finally {
            lock.unlock();
        }
    }

    @Around("ru.clevertec.house.service.aop.PersonPointcut.pointcutCreateMethod()")
    public ResponsePerson create(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            ResponsePerson response = (ResponsePerson) joinPoint.proceed();
            UUID uuid = response.uuid();
            cache.put(uuid, response);
            return response;
        } finally {
            lock.unlock();
        }
    }

    @Around("ru.clevertec.house.service.aop.PersonPointcut.pointcutUpdateMethod()||" +
            "ru.clevertec.house.service.aop.PersonPointcut.pointcutPatchMethod()")
    public ResponsePerson update(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            ResponsePerson response = (ResponsePerson) joinPoint.proceed();
            UUID personUuid = response.uuid();
            cache.removeByKey(personUuid);
            cache.put(personUuid, response);
            return response;
        } finally {
            lock.unlock();
        }

    }

    @Around("ru.clevertec.house.service.aop.PersonPointcut.pointcutDeleteMethod()")
    public void delete(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            UUID uuid = (UUID) joinPoint.getArgs()[0];
            joinPoint.proceed();
            cache.removeByKey(uuid);
        } finally {
            lock.unlock();
        }
    }
}
