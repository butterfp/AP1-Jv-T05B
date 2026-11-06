package security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwt;

    public AuthFilter(JwtProvider jwt) { this.jwt = jwt; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7).trim();
            try {
                if (jwt.validateAccessToken(token)) {
                    var context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(JwtUtil.createAuthentication(jwt.getClaims(token)));
                    SecurityContextHolder.setContext(context);
                }
            } catch (RuntimeException e) { SecurityContextHolder.clearContext(); }
        }
        chain.doFilter(request, response);
    }
}
