package com.app;
import com.app.persistence.Entity.PermissionEntity;
import com.app.persistence.Entity.RoleEntity;
import com.app.persistence.Entity.RoleEnum;
import com.app.persistence.Entity.UserEntity;
import com.app.persistence.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;
@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}
	// Inicializa datos de ejemplo (permisos, roles y usuarios) al iniciar la aplicación.
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            // --- Creación de permisos ---
            // Los permisos se crean primero, ya que los roles dependen de ellos.
            PermissionEntity createPermission = PermissionEntity.builder().name("CREATE").build();
            PermissionEntity readPermission   = PermissionEntity.builder().name("READ").build();
            PermissionEntity updatePermission = PermissionEntity.builder().name("UPDATE").build();
            PermissionEntity deletePermission = PermissionEntity.builder().name("DELETE").build();
            PermissionEntity refactorPermission = PermissionEntity.builder().name("REFACTOR").build(); // Permiso adicional de prueba

            // --- Creación de roles ---
            // Cada rol agrupa uno o varios permisos según su nivel de acceso.
            RoleEntity roleAdmin = RoleEntity.builder()
                    .roleEnum(RoleEnum.ADMIN)
                    .permisos(Set.of(createPermission, readPermission, updatePermission, deletePermission))
                    .build();

            RoleEntity roleUser = RoleEntity.builder()
                    .roleEnum(RoleEnum.USER)
                    .permisos(Set.of(readPermission))
                    .build();

            RoleEntity roleDeveloper = RoleEntity.builder()
                    .roleEnum(RoleEnum.DEVELOPER)
                    .permisos(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
                    .build();

            RoleEntity roleInvited = RoleEntity.builder()
                    .roleEnum(RoleEnum.INVITED)
                    .permisos(Set.of(readPermission))
                    .build();

            // --- Creación de usuarios ---
            // Se definen los usuarios iniciales con sus respectivas configuraciones y roles.
            // Las contraseñas están encriptadas con BCrypt.
            // Las propiedades booleanas son las que utiliza Spring Security para validar el estado de la cuenta.
            UserEntity userDuzz = UserEntity.builder()
                    .username("Duzz")
                    .password("$2a$10$fcLYlZomll20nn15VRh4VuYuH9V/TKoUw9CbodWWVEzHMzY91sDx2")
                    .isEnabled(true)           // La cuenta está habilitada
                    .accountNoExpired(true)    // La cuenta no ha expirado
                    .accountNoLocked(true)     // La cuenta no está bloqueada
                    .credentialNoExpired(true) // Las credenciales son válidas
                    .roles(Set.of(roleDeveloper))
                    .build();

            UserEntity userNoe = UserEntity.builder()
                    .username("Noe")
                    .password("$2a$10$fcLYlZomll20nn15VRh4VuYuH9V/TKoUw9CbodWWVEzHMzY91sDx2")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(roleAdmin))
                    .build();

            UserEntity userPaul = UserEntity.builder()
                    .username("Paul")
                    .password("$2a$10$fcLYlZomll20nn15VRh4VuYuH9V/TKoUw9CbodWWVEzHMzY91sDx2")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(roleUser))
                    .build();

            UserEntity userSoplaGaita = UserEntity.builder()
                    .username("SoplaGaitas")
                    .password("$2a$10$fcLYlZomll20nn15VRh4VuYuH9V/TKoUw9CbodWWVEzHMzY91sDx2")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(roleInvited))
                    .build();

            // --- Persistencia de datos ---
            // Se guardan todos los usuarios (junto con sus roles y permisos asociados) en la base de datos.
            userRepository.saveAll(List.of(userDuzz, userPaul, userNoe, userSoplaGaita));
        };
    }
}
