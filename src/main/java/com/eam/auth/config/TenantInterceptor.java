package com.eam.auth.config;

import com.eam.i18n.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to handle tenant context for i18n functionality.
 * Extracts X-Tenant-ID header and sets it in TenantContextHolder.
 */
@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_TENANT = "default";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_HEADER);
        
        if (tenantId == null || tenantId.trim().isEmpty()) {
            tenantId = DEFAULT_TENANT;
        }
        
        TenantContextHolder.setTenantId(tenantId);
        log.debug("Set tenant context: {} for request: {}", tenantId, request.getRequestURI());
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContextHolder.clear();
        log.debug("Cleared tenant context for request: {}", request.getRequestURI());
    }
}
