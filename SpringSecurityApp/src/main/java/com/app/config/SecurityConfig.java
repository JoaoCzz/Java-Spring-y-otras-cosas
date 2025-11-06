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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //Nos permite usar anotaciones de spring para hacer configs
public class SecurityConfig {

    //Para una buena configuracion necesitamos:

//    //Esta es para configurar las condiciones de la seguridad
//    @Bean
//    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .csrf(csrf -> csrf.disable()) //Seguridad basada en los tokens que se guardan en las cookies que intercepta la comunicacion del navegador a traves del formulario para protegerlo, deshabilitar solo si no trabajas con formularios en el navegador
//                .httpBasic(Customizer.withDefaults()) //Sirve solo cuando te vas a logear con usuario y contraseña (No usar normalmente) proximamente jwt
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Nos sirve para configurar el comportamiento de las sesiones
//                                                                                                                                                 //ALWAYS - IF_REQUERED - NEVER -STATELESS
//                                                                                                                                                 //ALWAYS SIGNIFICA CREA UNA SESION SIEMPRE Y CUANDO EXISTA UNA SINO LA VA A REUTILIZAR
//                                                                                                                                                 //IF_REQUERED CREA UNA NUEVA SOLO SI ES NECESARIA (ES MAS ESTRICTO QUE ALWAYS)
//                                                                                                                                                 //NEVER NO CREA NINGUNA SESION PERO SI YA EXISTE UNA LA VA A UTILIZAR
//                                                                                                                                                 //NO CREA NINGUNA SESSION, NO VA A GUARDAR NINGUN DATO
//                   //Configuramos los endpoints que queremos que este protegidos y no                                                                                                                              //LA VENTAJA DE USAR ESTO ES TENER INFORMACION GUARDADA DEL USUARIO DENTRO DE SU SESION
//                .authorizeHttpRequests(http -> {
//                    //Configurar los endpoints publicos
//                    http.requestMatchers(HttpMethod.GET,"/auth/hello").permitAll();//Ponemos el tipo de metodo que es, el link y el tipo de autorizacion que tiene que tener para acceder pero en este caso es todos
//                    //Configurar los endpoints privados
//                    http.requestMatchers(HttpMethod.GET,"auth/hello-secured").hasAuthority("CREATE"); //Ponemos el tipo de metodo que es, el link y el tipo de autorizacion que tiene que tener para acceder
//                    //Configurar el resto de enpodint - NO ESPECIFICADO
//                    http.anyRequest().denyAll(); //Cualquier otro link esta denegado para todos
//                  //  http.anyRequest().authenticated(); //Cualquier otro link solo esta disponible para usuarios autotenficados
//                })
//                .build();
//    }

    //Esta es para configurar las condiciones de la seguridad
    //Haremos uso de las anotaciones para permitir y denegar endpoints
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) //Seguridad basada en los tokens que se guardan en las cookies que intercepta la comunicacion del navegador a traves del formulario para protegerlo, deshabilitar solo si no trabajas con formularios en el navegador
                .httpBasic(Customizer.withDefaults()) //Sirve solo cuando te vas a logear con usuario y contraseña (No usar normalmente) proximamente jwt
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Nos sirve para configurar el comportamiento de las sesiones
                                                                                //ALWAYS - IF_REQUERED - NEVER -STATELESS
                                                                                //ALWAYS SIGNIFICA CREA UNA SESION SIEMPRE Y CUANDO EXISTA UNA SINO LA VA A REUTILIZAR
                                                                                //IF_REQUERED CREA UNA NUEVA SOLO SI ES NECESARIA (ES MAS ESTRICTO QUE ALWAYS)
                                                                                //NEVER NO CREA NINGUNA SESION PERO SI YA EXISTE UNA LA VA A UTILIZAR
                                                                                //NO CREA NINGUNA SESSION, NO VA A GUARDAR NINGUN DATO
                                                                                //Configuramos los endpoints que queremos que este protegidos y no                                                                                                                              //LA VENTAJA DE USAR ESTO ES TENER INFORMACION GUARDADA DEL USUARIO DENTRO DE SU SESION

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceimpl userDetailServiceimpl){
        //Utilizamos este porque el DAO es el que se conecta a la base de datos
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //Este provider necesita codificar contraseñas por obligacion
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailServiceimpl);
        return provider;
    }

    //Deberia el userDetailAService conectarse a memoria pero por ahora solo guardaremos un usuario en memoria
//    @Bean
//    public UserDetailsService userDetailsService(){
////        //Normalmente traemos los usuarios de la database y lo convertimos en userDetails
////        // En el caso de guardar solo un usuario
//////        UserDetails userDetails= User.withUsername("Duzz")
//////                .password("1234")
//////                .roles("ADMIN")   //ROL Y AUTORIZACIONES NO SON LO MISMO
//////                .authorities("READ","CREATE")
//////                .build();
////        //Utilizo listas para devolver mas usuarios en memoria
////        List<UserDetails> userDetailsList= new ArrayList<>();
////        userDetailsList.add(User.withUsername("Duzz")
////            .password("1234")
////               .roles("ADMIN")   //ROL Y AUTORIZACIONES NO SON LO MISMO
////               .authorities("READ","CREATE")
////               .build());
////        userDetailsList.add(User.withUsername("ElBarto")
////                .password("1234")
////                .roles("USER")   //ROL Y AUTORIZACIONES NO SON LO MISMO
////                .authorities("READ")
////                .build());
////        return new InMemoryUserDetailsManager(userDetailsList);
//
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //Para que no codifique contraseñas SOLO UTILIZAR PARA PRUEBAS
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder(); //Esta seria la forma correcta
    }


}
