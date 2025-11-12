package com.app.config;

import com.app.Service.UserDetailServiceimpl;
import com.app.config.Filter.JwtTokenValidator;
import com.app.util.JWTUtils;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar anotaciones de seguridad como @PreAuthorize en controladores y servicios.
public class SecurityConfig {
    /*
     * =============================================================
     * EJEMPLO COMENTADO DE CONFIGURACIÓN DETALLADA DE SEGURIDAD
     * =============================================================
     *
     * Esta configuración muestra cómo definir manualmente los endpoints
     * protegidos, los públicos y la política de sesión.
     * Actualmente está comentada porque se está utilizando el control
     * de acceso mediante anotaciones (@PreAuthorize).
     */

    //Traemos el jwtUtils
    @Autowired
    private JWTUtils jwtUtils;
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

                    // --- Endpoints publicos ---
                    http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    // --- Endpoints protegidos ---
                   http.requestMatchers(HttpMethod.POST, "/method/post").hasAnyRole("ADMIN","DEVELOPER");
                    http.requestMatchers(HttpMethod.PATCH, "/method/patch").hasAuthority("REFACTOR");


                    // --- Endpoints no especificados ---
                    // Cualquier otro endpoint queda denegado
                    http.anyRequest().denyAll();
                    // Alternativamente:
                     //http.anyRequest().authenticated(); // Solo usuarios autenticados pueden acceder.
                })
                //Añadimos el filtro antes que el filtro de autotentifcacion
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class) //Ponemos para que se ejecue antes del basic
                .build();
    }


    /*
     * =============================================================
     * CONFIGURACIÓN ACTIVA DE SEGURIDAD (USANDO ANOTACIONES)
     * =============================================================
     *
     * Esta versión delega el control de acceso a las anotaciones
     * declaradas en controladores y servicios.
     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF (solo para APIs REST sin formularios).
//                .httpBasic(Customizer.withDefaults()) // Autenticación básica HTTP (solo para pruebas).
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Política sin estado.
//                .build();
//    }

    /*
     * =============================================================
     * GESTOR DE AUTENTICACIÓN
     * =============================================================
     *
     * Encargado de manejar el proceso de autenticación de usuarios.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
     * =============================================================
     * PROVEEDOR DE AUTENTICACIÓN (DAO)
     * =============================================================
     *
     * Conecta el servicio de usuarios con la lógica de autenticación
     * y valida credenciales desde la base de datos.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceimpl userDetailServiceimpl) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // Codificador de contraseñas obligatorio.
        provider.setUserDetailsService(userDetailServiceimpl); // Servicio personalizado de usuarios.
        return provider;
    }

    /*
     * =============================================================
     * CODIFICADOR DE CONTRASEÑAS
     * =============================================================
     *
     * Define el algoritmo para codificar contraseñas.
     * BCrypt es el recomendado por Spring Security.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // ⚠️ Solo para pruebas:
        // return NoOpPasswordEncoder.getInstance();

        // ✅ Uso recomendado: BCrypt.
        return new BCryptPasswordEncoder();
    }

    /*
     * =============================================================
     * USUARIOS EN MEMORIA (EJEMPLO OPCIONAL)
     * =============================================================
     *
     * Ejemplo de cómo definir usuarios directamente en memoria.
     * Se recomienda solo para pruebas locales.
     * En producción se recomienda cargar usuarios desde una base de datos.
     */
    /*
    @Bean
    public UserDetailsService userDetailsService() {

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
