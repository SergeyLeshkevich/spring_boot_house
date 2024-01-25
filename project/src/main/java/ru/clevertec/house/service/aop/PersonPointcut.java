package ru.clevertec.house.service.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PersonPointcut {

    @Pointcut("execution(* ru.clevertec.house.service.impl.PersonServiceImpl.get(..))")
    public void pointcutGetMethod() {
    }

    @Pointcut("execution(* ru.clevertec.house.service.impl.PersonServiceImpl.create(..))")
    public void pointcutCreateMethod() {
    }

    @Pointcut("execution(* ru.clevertec.house.service.impl.PersonServiceImpl.update(..))")
    public void pointcutUpdateMethod() {
    }

    @Pointcut("execution(* ru.clevertec.house.service.impl.PersonServiceImpl.delete(..))")
    public void pointcutDeleteMethod() {
    }

    @Pointcut("execution(* ru.clevertec.house.service.impl.PersonServiceImpl.patch(..))")
    public void pointcutPatchMethod() {
    }
}
