package bytebrewers.bitpod.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import bytebrewers.bitpod.service.UserService;
import bytebrewers.bitpod.utils.dto.response.user.JwtClaim;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtUtils jwtUtils;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String token = parseJwt(request);
            if(token != null && jwtUtils.verifyJwtToken(token)) {
                JwtClaim userInfo = jwtUtils.getUserInfoByToken(token);
                UserDetails userDetails = userService.loadByUserId(userInfo.getUserId());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } 
        } catch (Exception e) {
           log.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt (HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        } 

        return null;
    }
}
