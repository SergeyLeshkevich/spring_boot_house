package ru.clevertec.house.service.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class HousePointcut {

    @Pointcut("execution(* ru.clevertec.house.service.impl.HouseServiceImpl.get(..))")
    public void pointcutGetMethod(){
    }


    @Pointcut("execution(* ru.clevertec.house.service.impl.HouseServiceImpl.create(..))")
    public void pointcutCreateMethod() {
    }

    @Pointcut("execution(* ru.clevertec.house.service.impl.HouseServiceImpl.update(..))")
    public void pointcutUpdateMethod(){
    }

    @Pointcut("execution(* ru.clevertec.house.service.impl.HouseServiceImpl.delete(..))")
    public void pointcutDeleteMethod() {
    }

    @Pointcut(value = "execution(* ru.clevertec.house.service.impl.HouseServiceImpl.addOwner(..))")
    public void pointcutAddOwnerMethod() {
    }

    @Pointcut("execution(* ru.clevertec.house.service.impl.HouseServiceImpl.patch(..))")
    public void pointcutPatchMethod() {
    }
}
