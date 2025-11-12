package com.app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ================================================================
 * JWTUtils
 * ================================================================
 *
 * Clase encargada de crear y validar tokens JWT para la aplicación.
 *
 * Responsabilidades principales:
 * 1. Crear un JWT con username, roles y permisos, expiración y firma segura.
 * 2. Validar un JWT (firma, expiración, issuer).
 * 3. Extraer información del token (username, claims específicos, todos los claims).
 *
 * JWT = JSON Web Token
 * - Está compuesto por header, payload y firma.
 * - Header: define el algoritmo de firma.
 * - Payload: contiene la información (claims) del usuario.
 * - Firma: asegura que el token no fue manipulado.
 *
 * Flujo típico:
 * 1. Usuario se autentica → loginUser() en UserDetailServiceImpl
 * 2. JWTUtils.createToken() genera token con roles/permisos.
 * 3. El token se envía al cliente.
 * 4. Cada petición posterior es filtrada por JwtTokenValidator.
 * 5. JWTUtils.validateToken() verifica que el token sea válido.
 */
@Component
public class JWTUtils {
    // Clave privada utilizada para firmar y verificar tokens
    // Debe ser segura y se recomienda mantenerla en application.properties
    @Value("${security.jwt.key.private}")
    private String privateKey;

    // Identificador del generador del token (issuer)
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    /**
     * ================================================================
     * createToken
     * ================================================================
     *
     * Crea un JWT a partir de la información de autenticación.
     *
     * @param authentication objeto Authentication de Spring Security
     *                       que contiene username y roles/permisos
     * @return String token JWT firmado
     *
     * Flujo:
     * 1. Obtener username del Authentication.
     * 2. Obtener roles y permisos y unirlos en un string separado por comas.
     * 3. Configurar claims, fechas de creación y expiración, ID único.
     * 4. Firmar token con algoritmo HMAC256.
     */
    public String createToken(Authentication authentication) {

        // Definir algoritmo de firma HMAC256 con la clave privada
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        // Extraer username del Authentication
        String username = authentication.getPrincipal().toString();

        // Extraer roles/permisos y convertirlos en un string separado por comas
        // Ej: "ROLE_ADMIN,READ,WRITE"
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Crear el token JWT con claims y fechas importantes
        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)                // Emisor del token (issuer)
                .withSubject(username)                          // Sujeto del token (username)
                .withClaim("authorities", authorities)         // Claims personalizados (roles/permisos)
                .withIssuedAt(new Date())                       // Fecha de emisión del token
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) // Expiración en 30 minutos
                .withJWTId(UUID.randomUUID().toString())       // ID único del token
                .withNotBefore(new Date(System.currentTimeMillis())) // Token válido desde ahora
                .sign(algorithm);                              // Firma del token con HMAC256

        return jwtToken;
    }

    /**
     * ================================================================
     * validateToken
     * ================================================================
     *
     * Valida un token JWT y devuelve el token decodificado.
     *
     * @param token JWT a validar
     * @return DecodedJWT token decodificado si es válido
     * @throws JWTVerificationException si el token es inválido o expirado
     *
     * Flujo:
     * 1. Definir algoritmo HMAC256 con la misma clave privada usada para crear el token.
     * 2. Configurar un JWTVerifier con el issuer esperado.
     * 3. Llamar a verify() para validar firma, expiración y emisor.
     */
    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            // Configurar verificador de token
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator) // solo aceptamos tokens generados por nuestro issuer
                    .build();

            // Validar token y devolverlo decodificado
            return verifier.verify(token);

        } catch (JWTVerificationException JWTe) {
            // Si falla la verificación, lanzar excepción indicando que el token no es válido
            throw new JWTVerificationException("Token inválido, no autorizado");
        }
    }

    /**
     * ================================================================
     * extractUsername
     * ================================================================
     *
     * Extrae el username del token JWT decodificado.
     *
     * @param decodedJWT token ya validado
     * @return String username contenido en el token (claim "sub")
     */
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject(); // el subject del JWT siempre es el username
    }

    /**
     * ================================================================
     * getSpecificClaim
     * ================================================================
     *
     * Obtiene un claim específico del token JWT.
     *
     * @param decodedJWT token ya validado
     * @param claimName nombre del claim a obtener (ej: "authorities")
     * @return Claim valor del claim solicitado
     */
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    /**
     * ================================================================
     * returnAllClaims
     * ================================================================
     *
     * Devuelve todos los claims del token JWT.
     *
     * @param decodedJWT token ya validado
     * @return Map<String, Claim> todos los claims contenidos en el token
     */
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

}
