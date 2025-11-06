package com.app.Service;

import com.app.persistence.Entity.UserEntity;
import com.app.persistence.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceimpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Carga un usuario desde la base de datos a partir de su nombre de usuario
     * y lo adapta al modelo de seguridad de Spring Security.
     *
     * Este método se ejecuta automáticamente durante el proceso de autenticación.
     * Se encarga de:
     *  - Buscar el usuario en la base de datos.
     *  - Convertir sus roles y permisos en autoridades reconocidas por Spring Security.
     *  - Devolver un objeto UserDetails que Spring usa para validar credenciales y gestionar la sesión.
     *
     * @param username nombre de usuario a buscar.
     * @return un objeto UserDetails con los datos y autoridades del usuario.
     * @throws UsernameNotFoundException si el usuario no existe en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // --- Búsqueda del usuario ---
        // Obtiene el usuario desde la base de datos según su nombre de usuario.
        // Si no se encuentra, lanza una excepción manejada por Spring Security.
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe!"));

        // --- Conversión de roles y permisos a autoridades ---
        // Spring Security utiliza GrantedAuthority para manejar roles y permisos.
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Agrega los roles del usuario con el prefijo "ROLE_",
        // requerido por convención en Spring Security.
        userEntity.getRoles().forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))
        );

        // Agrega los permisos asociados a cada rol.
        // Se usa programación funcional para recorrer todos los permisos de todos los roles,
        // y transformarlos en instancias de SimpleGrantedAuthority.
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermisos().stream())
                .forEach(permission ->
                        authorityList.add(new SimpleGrantedAuthority(permission.getName()))
                );

        // --- Mapeo al modelo de seguridad de Spring ---
        // Devuelve un objeto User con toda la información necesaria para la autenticación y autorización.
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),             // Cuenta habilitada
                userEntity.isAccountNoExpired(),    // Cuenta no expirada
                userEntity.isCredentialNoExpired(), // Credenciales válidas
                userEntity.isAccountNoLocked(),     // Cuenta no bloqueada
                authorityList                       // Lista de roles y permisos convertidos
        );
    }
}
