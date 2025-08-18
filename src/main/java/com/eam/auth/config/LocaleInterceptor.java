package com.eam.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * Interceptor to handle locale context for i18n functionality.
 * Parses Accept-Language header and sets it in LocaleContextHolder.
 */
@Slf4j
@Component
public class LocaleInterceptor implements HandlerInterceptor {

    private final LocaleResolver localeResolver;

    public LocaleInterceptor() {
        this.localeResolver = new AcceptHeaderLocaleResolver();
        ((AcceptHeaderLocaleResolver) this.localeResolver).setDefaultLocale(Locale.ENGLISH);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Locale locale = localeResolver.resolveLocale(request);
            LocaleContextHolder.setLocale(locale);
            log.debug("Set locale context: {} for request: {}", locale.getLanguage(), request.getRequestURI());
        } catch (Exception e) {
            log.warn("Failed to resolve locale, using default: {}", e.getMessage());
            LocaleContextHolder.setLocale(Locale.ENGLISH);
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LocaleContextHolder.resetLocaleContext();
        log.debug("Cleared locale context for request: {}", request.getRequestURI());
    }
}
