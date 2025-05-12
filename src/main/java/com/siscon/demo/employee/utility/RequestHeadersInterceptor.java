package com.siscon.demo.employee.utility;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

/**
 * Interceptor que registra todos los headers de las peticiones HTTP recibidas.
 * Útil para depuración, auditoría y monitoreo del tráfico de la aplicación.
 */
@Log4j2
@Component
public class RequestHeadersInterceptor implements HandlerInterceptor {

    /**
     * Se ejecuta antes de que el controlador procese la petición.
     * Captura y registra todos los headers de la petición entrante.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = getClientIp(request);
        
        String fullUrl = uri + (queryString != null ? "?" + queryString : "");
        
        log.info("Incoming request: {} {} from IP: {}", method, fullUrl, clientIp);
        
        Map<String, String> headers = getRequestHeaders(request);
        log.info("Request headers for {} {}: {}", method, uri, formatHeaders(headers));
        
        return true;
    }
    
    /**
     * Obtiene todos los headers de la petición y los devuelve como un mapa.
     * @param request La petición HTTP
     * @return Mapa de nombres de headers y sus valores
     */
    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                
                if ("authorization".equalsIgnoreCase(headerName) && headerValue != null) {
                    headerValue = "********";
                }
                
                headersMap.put(headerName, headerValue);
            }
        }
        
        return headersMap;
    }
    
    /**
     * Formatea los headers para una mejor presentación en los logs.
     * @param headers Mapa de headers
     * @return String formateado con los headers
     */
    private String formatHeaders(Map<String, String> headers) {
        if (headers.isEmpty()) {
            return "No headers";
        }
        
        StringBuilder builder = new StringBuilder("\n");
        headers.forEach((name, value) -> builder.append(String.format("    %s: %s\n", name, value)));
        return builder.toString();
    }
    
    /**
     * Obtiene la dirección IP real del cliente, considerando proxies y balanceadores.
     * @param request La petición HTTP
     * @return La dirección IP del cliente
     */
    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        
        // Si hay múltiples IPs (caso de múltiples proxies), tomar la primera
        if (clientIp != null && clientIp.contains(",")) {
            clientIp = clientIp.split(",")[0].trim();
        }
        
        return clientIp;
    }
}