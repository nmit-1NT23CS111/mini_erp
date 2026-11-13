package com.minierp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Intercepts every request (except /login) and redirects unauthenticated
 * users to the login page.
 */
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("loggedInUser") != null;

        if (!loggedIn) {
            // For AJAX / REST calls return 401 instead of redirect
            String acceptHeader = request.getHeader("Accept");
            if (acceptHeader != null && acceptHeader.contains("application/json")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired. Please log in.");
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return false;
        }
        return true;
    }
}
