package com.app.config;

import com.app.Service.UserDetailServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
/**
 * Configuración principal de seguridad para la aplicación.
 *
 * <p>Esta clase define la configuración global de Spring Security, incluyendo:</p>
 * <ul>
 *     <li>Política de sesiones.</li>
 *     <li>Gestión de autenticación y proveedores.</li>
 *     <li>Codificación de contraseñas.</li>
 *     <li>Uso de anotaciones {@code @PreAuthorize} para control granular de acceso.</li>
 * </ul>
 *
 * <p>Gracias a {@link EnableMethodSecurity}, se habilita la protección a nivel de método,
 * permitiendo anotar endpoints con reglas como {@code @PreAuthorize("hasAuthority('READ')")}.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar anotaciones de seguridad como @PreAuthorize en controladores y servicios.
public class SecurityConfig {
    // ======================================================================
    // EJEMPLO: CONFIGURACIÓN DE SEGURIDAD DETALLADA (COMENTADA)
    // ======================================================================

    /*
    // Esta configuración muestra cómo definir manualmente las condiciones de seguridad
    // y los endpoints permitidos o restringidos.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Deshabilita CSRF: útil solo cuando no se usan formularios web.
                .csrf(csrf -> csrf.disable())

                // Habilita autenticación básica HTTP (solo para pruebas, no para producción).
                .httpBasic(Customizer.withDefaults())

                // Configura la política de sesiones.
                // Tipos posibles:
                //   ALWAYS       → Crea una sesión siempre (si no existe, la genera).
                //   IF_REQUIRED  → Crea una sesión solo si es necesaria.
                //   NEVER        → No crea nuevas sesiones, pero usa una existente si la hay.
                //   STATELESS    → No crea ni mantiene sesiones (ideal para APIs REST).
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configura los endpoints y sus permisos de acceso.
                .authorizeHttpRequests(http -> {
                    // --- Endpoints públicos ---
                    // Por defecto ninguno, pero puedes permitir alguno si lo deseas.
                    // http.requestMatchers(HttpMethod.GET, "/auth/public").permitAll();

                    // --- Endpoints protegidos ---
                    http.requestMatchers(HttpMethod.GET, "/auth/get").hasAuthority("READ");
                    http.requestMatchers(HttpMethod.POST, "/auth/post").denyAll(); // Denegado por política general
                    http.requestMatchers(HttpMethod.PUT, "/auth/put").denyAll();   // Denegado por política general
                    http.requestMatchers(HttpMethod.DELETE, "/auth/delete").denyAll(); // Denegado por política general
                    http.requestMatchers(HttpMethod.PATCH, "/auth/patch").hasAuthority("REFACTOR");

                    // --- Endpoints no especificados ---
                    // Todo lo demás estará denegado para todos los usuarios.
                    http.anyRequest().denyAll();
                    // Alternativamente:
                    // http.anyRequest().authenticated(); // Solo usuarios autenticados pueden acceder.
                })
                .build();
    }
    */

    // ======================================================================
    // CONFIGURACIÓN ACTIVA (USANDO ANOTACIONES @PreAuthorize)
    // ======================================================================

    /**
     * Configuración principal del filtro de seguridad activo.
     *
     * <p>Esta versión delega el control de permisos a las anotaciones de seguridad
     * declaradas directamente en los endpoints o servicios.</p>
     *
     * <p>Características:</p>
     * <ul>
     *     <li>Deshabilita CSRF (solo necesario si no se usan formularios HTML).</li>
     *     <li>Activa autenticación básica HTTP para pruebas.</li>
     *     <li>Configura sesiones como <b>STATELESS</b> (sin almacenamiento de sesión).</li>
     * </ul>
     *
     * @param httpSecurity objeto de configuración principal de seguridad HTTP.
     * @return una instancia configurada de {@link SecurityFilterChain}.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF (solo para APIs REST sin formularios).
                .httpBasic(Customizer.withDefaults()) // Autenticación básica HTTP (solo para pruebas).
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Política sin estado.
                .build();
    }

    // ======================================================================
    // CONFIGURACIÓN DE AUTENTICACIÓN
    // ======================================================================

    /**
     * Proporciona el {@link AuthenticationManager} encargado de procesar
     * la autenticación de usuarios.
     *
     * @param authenticationConfiguration configuración de autenticación de Spring.
     * @return el {@link AuthenticationManager} configurado.
     * @throws Exception en caso de error al obtener el administrador.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura el proveedor de autenticación basado en acceso a datos (DAO).
     *
     * <p>Este provider conecta con la base de datos mediante un servicio personalizado
     * que implementa {@link org.springframework.security.core.userdetails.UserDetailsService}.</p>
     *
     * @param userDetailServiceimpl implementación del servicio de usuarios.
     * @return un {@link AuthenticationProvider} configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceimpl userDetailServiceimpl) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // Codificador de contraseñas obligatorio.
        provider.setUserDetailsService(userDetailServiceimpl); // Servicio personalizado de usuarios.
        return provider;
    }

    // ======================================================================
    // CONFIGURACIÓN DE PASSWORD ENCODER
    // ======================================================================

    /**
     * Define el codificador de contraseñas.
     *
     * <p>Para pruebas, puede usarse {@code NoOpPasswordEncoder}, aunque se recomienda
     * siempre {@link BCryptPasswordEncoder} en entornos reales.</p>
     *
     * @return una instancia de {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // ⚠️ Solo para pruebas:
        // return NoOpPasswordEncoder.getInstance();

        // ✅ Uso recomendado: BCrypt.
        return new BCryptPasswordEncoder();
    }

    // ======================================================================
    // EJEMPLO OPCIONAL: USUARIOS EN MEMORIA (COMENTADO)
    // ======================================================================

    /*
    @Bean
    public UserDetailsService userDetailsService() {
        // Ejemplo de configuración en memoria.
        // En producción se recomienda cargar usuarios desde una base de datos.

        List<UserDetails> userDetailsList = new ArrayList<>();

        userDetailsList.add(User.withUsername("Duzz")
                .password("1234")
                .roles("ADMIN")
                .authorities("READ", "CREATE")
                .build());

        userDetailsList.add(User.withUsername("ElBarto")
                .password("1234")
                .roles("USER")
                .authorities("READ")
                .build());

        return new InMemoryUserDetailsManager(userDetailsList);
    }
    */


}
