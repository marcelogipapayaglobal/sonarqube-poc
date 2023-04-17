package service.apis;

import com.papaya.cycleactivitylog.service.EnvironmentVariables;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;


@Slf4j
@Component
@WebFilter
public class OpenApiRequestFilter extends OncePerRequestFilter {

    private static String getRequestUri(HttpServletRequest request) {
        String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
        if (uri == null) {
            uri = request.getRequestURI();
        }
        return uri;
    }

    private static Boolean isOpenApiUri(String uri) {
        return uri.startsWith("/cycle-activity-log/v3/api-docs") || uri.startsWith("/cycle-activity-log/swagger-ui");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = getRequestUri(request);
        if (isOpenApiUri(uri) && !EnvironmentVariables.CYCLE_ACTIVITY_OPENAPI_ENABLED.getBooleanValue()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        filterChain.doFilter(request, response);
    }
}
