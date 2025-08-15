package com.eam.auth.annotation;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HasPermissionAspect {

    @Around("@annotation(hasPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, HasPermission hasPermission) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(
                a -> a.getAuthority().equals(hasPermission.value()))) {
            throw new org.springframework.security.access.AccessDeniedException("Forbidden");
        }
        return joinPoint.proceed();
    }
}
