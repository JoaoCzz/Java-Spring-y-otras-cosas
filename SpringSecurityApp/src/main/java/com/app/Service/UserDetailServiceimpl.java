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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(("El usuario "+username+" no existe!")));
        //Spring maneja los permisos con esta clase por eso debemos mapear el user
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        //Para enviar los roles del usuario al simpleGrantedAuthority
        userEntity.getRoles().forEach(role ->authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        //Usamos programacion funcional para convertir el rol en un stream para recorrer dentro los permisos y añadirlo al simpleGranted
        userEntity.getRoles().stream()
                .flatMap(role ->role.getPermisos().stream())
                .forEach(permission ->authorityList.add(new SimpleGrantedAuthority(permission.getName())));
        //Mapeamos todo a un User de Spring
        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }
}
