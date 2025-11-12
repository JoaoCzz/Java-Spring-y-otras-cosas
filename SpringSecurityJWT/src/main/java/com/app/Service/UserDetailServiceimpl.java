package com.app.Service;

import com.app.controller.DTO.AuthCreateUserRequest;
import com.app.controller.DTO.AuthLoginRequest;
import com.app.controller.DTO.AuthResponse;
import com.app.persistence.Entity.RoleEntity;
import com.app.persistence.Entity.UserEntity;
import com.app.persistence.Repository.RoleRespository;
import com.app.persistence.Repository.UserRepository;
import com.app.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * ================================================
 * UserDetailServiceImpl
 * ================================================
 *
 * Servicio principal que maneja usuarios, login, creación de usuarios y generación de tokens JWT.
 * Integra la lógica de negocio con Spring Security.
 *
 * Flujo general:
 * 1. Cargar usuarios desde DB → loadUserByUsername
 * 2. Autenticar usuario → authenticate
 * 3. Login y generación de token → loginUser
 * 4. Crear usuario y generar token → createUser
 *
 * Cada mét0do interactúa con:
 * - UserRepository: para acceder a la base de datos
 * - RoleRepository: para validar roles
 * - PasswordEncoder: para encriptar/validar contraseñas
 * - JWTUtils: para generar y validar tokens JWT
 */

@Service
public class UserDetailServiceimpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Repositorio para acceder a los usuarios en DB

    @Autowired
    private PasswordEncoder passwordEncoder; // Para encriptar y validar contraseñas

    @Autowired
    private JWTUtils jwtUtils; // Utilidad para crear y validar tokens JWT

    @Autowired
    private RoleRespository roleRespository; // Repositorio para roles

    /**
     * =====================================================
     * loadUserByUsername
     * =====================================================
     *
     * Carga un usuario de la base de datos y lo adapta al modelo de Spring Security.
     *
     * Flujo:
     * 1. Buscar el usuario en DB
     * 2. Convertir roles y permisos en GrantedAuthority
     * 3. Devolver UserDetails
     *
     * Spring Security llama automáticamente este método durante la autenticación.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // --- Búsqueda del usuario ---
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe!"));

        // --- Convertir roles y permisos a GrantedAuthority ---
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Convertir roles a "ROLE_X" (convención de Spring Security)
        userEntity.getRoles().forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()))
        );

        // Convertir permisos de cada rol a GrantedAuthority
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermisos().stream())
                .forEach(permission ->
                        authorityList.add(new SimpleGrantedAuthority(permission.getName()))
                );

        // --- Retornar UserDetails ---
        return new User(
                userEntity.getUsername(),         // username
                userEntity.getPassword(),         // password encriptado
                userEntity.isEnabled(),           // cuenta habilitada
                userEntity.isAccountNoExpired(),  // cuenta no expirada
                userEntity.isCredentialNoExpired(), // credenciales no expiradas
                userEntity.isAccountNoLocked(),   // cuenta no bloqueada
                authorityList                     // roles y permisos convertidos a GrantedAuthority
        );
    }

    /**
     * =====================================================
     * loginUser
     * =====================================================
     *
     * Autentica un usuario y genera un token JWT.
     *
     * Flujo:
     * 1. Llama a authenticate() para validar credenciales
     * 2. Coloca la autenticación en el SecurityContext
     * 3. Genera token JWT con JWTUtils.createToken()
     * 4. Devuelve AuthResponse con username, token y mensaje
     */
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username(); // obtener username del request
        String password = authLoginRequest.password(); // obtener password del request

        // Autenticar usuario (verifica contraseña y username)
        Authentication authentication = this.authenticate(username, password);

        // Guardar autenticación en Spring Security
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generar token JWT
        String accessToken = jwtUtils.createToken(authentication);

        // Retornar respuesta
        return new AuthResponse(username, "Usuario autenticado correctamente", accessToken, true);
    }

    /**
     * =====================================================
     * authenticate
     * =====================================================
     *
     * Verifica las credenciales del usuario.
     * Se llama desde loginUser().
     *
     * Flujo:
     * 1. Carga usuario desde DB
     * 2. Valida que la contraseña coincida
     * 3. Devuelve Authentication para usar en SecurityContext
     */
    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username); // buscar usuario

        // Validación de username y password
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña inválidos");
        }

        // Crear objeto Authentication para Spring Security
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    /**
     * =====================================================
     * createUser
     * =====================================================
     *
     * Crea un nuevo usuario y genera un token JWT.
     *
     * Flujo:
     * 1. Validar roles existentes en la DB
     * 2. Crear usuario con roles y configuración de seguridad
     * 3. Guardar usuario en DB
     * 4. Convertir roles y permisos a GrantedAuthority
     * 5. Generar token JWT
     * 6. Retornar AuthResponse
     */
    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();
        List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();

        // Validar roles existentes
        Set<RoleEntity> roleEntitySet = roleRespository
                .findRoleEntItiesByRoleEnumIn(roleRequest)
                .stream()
                .collect(Collectors.toSet());

        if (roleEntitySet.isEmpty()) {
            throw new IllegalArgumentException("Los roles especificados no existen");
        }

        // Crear usuario
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        // Guardar en DB
        UserEntity userCreated = userRepository.save(userEntity);

        // Convertir roles y permisos a GrantedAuthority
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userCreated.getRoles().forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()))
        );
        userCreated.getRoles().stream()
                .flatMap(role -> role.getPermisos().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        // Generar token JWT
        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(), userCreated.getPassword(), authorityList);
        String accessToken = jwtUtils.createToken(authentication);

        // Retornar respuesta
        return new AuthResponse(userCreated.getUsername(), "Usuario creado correctamente", accessToken, true);

    }
    }
