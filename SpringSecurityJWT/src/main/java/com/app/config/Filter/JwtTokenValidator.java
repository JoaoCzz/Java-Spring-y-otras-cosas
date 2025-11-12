package com.app.config.Filter;

import com.app.util.JWTUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

/**
 * ================================================================
 * JwtTokenValidator
 * ================================================================
 *
 * Filtro de Spring Security que intercepta cada petición HTTP
 * para validar el token JWT y establecer la autenticación en el contexto de seguridad.
 *
 * Flujo general:
 * 1. Extraer el token JWT del header de la petición (Authorization o Proxy-Authorization).
 * 2. Quitar el prefijo "Bearer ".
 * 3. Validar el token usando JWTUtils.validateToken().
 * 4. Extraer username y roles del token.
 * 5. Convertir roles a GrantedAuthority para Spring Security.
 * 6. Crear Authentication y asignarlo al SecurityContext.
 * 7. Continuar con la cadena de filtros.
 *
 * Nota:
 * - Se ejecuta antes de los controladores para que cualquier endpoint protegido
 *   ya tenga al usuario autenticado en el SecurityContext.
 * - Si el token es inválido o nulo, la petición seguirá el flujo de filtros
 *   y será rechazada por Spring Security si el endpoint requiere autenticación.
 */
public class JwtTokenValidator extends OncePerRequestFilter {

    // JWTUtils se encarga de validar y extraer datos del token
    private JWTUtils jwtUtils;

    /**
     * Constructor
     * JWTUtils no es un Bean de Spring, por eso se inyecta manualmente.
     *
     * @param jwtUtils instancia de JWTUtils
     */
    public JwtTokenValidator(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * ================================================================
     * doFilterInternal
     * ================================================================
     *
     * Método principal que se ejecuta en cada petición HTTP.
     * Valida el token JWT y, si es correcto, establece la autenticación.
     *
     * @param request  petición HTTP entrante
     * @param response respuesta HTTP
     * @param filterChain cadena de filtros de Spring Security
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // --- 1. Extraer token del header ---
        // El token JWT normalmente se envía en Proxy-Authorization o Authorization
        // con formato: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        String jwtToken = request.getHeader(HttpHeaders.PROXY_AUTHORIZATION);

        if (jwtToken != null) { // Solo procesamos si existe un token
            // --- 2. Quitar prefijo "Bearer " ---
            jwtToken = jwtToken.substring(7); // Los primeros 7 caracteres son "Bearer "

            // --- 3. Validar el token ---
            // Verifica firma, expiración y emisor
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

            // --- 4. Extraer información del token ---
            String username = jwtUtils.extractUsername(decodedJWT); // obtener username
            String stringAuthorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString(); // obtener roles/permissions

            // --- 5. Convertir roles a GrantedAuthority ---
            // Spring Security requiere que los roles y permisos sean objetos GrantedAuthority
            // La función commaSeparatedStringToAuthorityList convierte un string "ROLE_ADMIN,READ" en Collection<GrantedAuthority>
            Collection<? extends GrantedAuthority> authorities =
                    AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            // --- 6. Crear objeto Authentication ---
            // Creamos un UsernamePasswordAuthenticationToken con:
            // - username
            // - password null (por seguridad, no se almacena la contraseña)
            // - authorities (roles y permisos)
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // --- 7. Asignar Authentication al SecurityContext ---
            // Esto permite que Spring Security considere al usuario como autenticado
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);

            // Reasignamos el SecurityContext por claridad
            SecurityContextHolder.setContext(context);
        }

        // --- 8. Continuar con la cadena de filtros ---
        // Si el token era nulo o inválido, la petición seguirá y será rechazada
        // automáticamente si el endpoint requiere autenticación
        filterChain.doFilter(request, response);
    }
}
