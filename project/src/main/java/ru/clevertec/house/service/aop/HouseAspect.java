package ru.clevertec.house.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.house.entity.dto.response.ResponseHouse;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Aspect
@Component
public class HouseAspect {

    private final CacheFactory<UUID, ResponseHouse> cacheFactory;
    private final Cache<UUID, ResponseHouse> cache;
    private final Lock lock;

    public HouseAspect(CacheFactory<UUID, ResponseHouse> cacheFactory) {
        this.cacheFactory = cacheFactory;
        cache = cacheFactory.createCache();
        lock = new ReentrantLock();
    }

    @Around("ru.clevertec.house.service.aop.HousePointcut.pointcutGetMethod()")
    public ResponseHouse get(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            UUID uuid = (UUID) joinPoint.getArgs()[0];
            ResponseHouse responseHouse = cache.get(uuid);
            if (responseHouse == null) {
                responseHouse = (ResponseHouse) joinPoint.proceed();
            }
            cache.put(responseHouse.uuid(), responseHouse);
            return responseHouse;
        } finally {
            lock.unlock();
        }
    }


    @Around("ru.clevertec.house.service.aop.HousePointcut.pointcutCreateMethod()")
    public ResponseHouse create(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            ResponseHouse response = (ResponseHouse) joinPoint.proceed();
            UUID uuid = response.uuid();
            cache.put(uuid, response);
            return response;
        } finally {
            lock.unlock();
        }
    }

    @Around("ru.clevertec.house.service.aop.HousePointcut.pointcutDeleteMethod()")
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

    @Around("ru.clevertec.house.service.aop.HousePointcut.pointcutPatchMethod() ||" +
            "ru.clevertec.house.service.aop.HousePointcut.pointcutAddOwnerMethod()||" +
            "ru.clevertec.house.service.aop.HousePointcut.pointcutUpdateMethod()")
    public ResponseHouse patch(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            UUID uuid = (UUID) joinPoint.getArgs()[0];
            ResponseHouse response = (ResponseHouse) joinPoint.proceed();
            cache.removeByKey(uuid);
            cache.put(uuid, response);
            return response;
        } finally {
            lock.unlock();
        }
    }
}
