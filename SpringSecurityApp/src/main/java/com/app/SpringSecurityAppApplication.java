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
public class SpringSecurityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAppApplication.class, args);
	}
    //Para cada vez darle play
    @Bean
    CommandLineRunner init(UserRepository userRepository){
        return args -> {
            //Creamos los permisos antes que el rol y usuarios
            PermissionEntity createPermission= PermissionEntity.builder().name("CREATE").build();
            PermissionEntity readPermission= PermissionEntity.builder().name("READ").build();
            PermissionEntity updatePermission= PermissionEntity.builder().name("UPDATE").build();
            PermissionEntity deletePermission= PermissionEntity.builder().name("DELETE").build();
            //No se para que pero cree otro por probar
            PermissionEntity refactorPermission= PermissionEntity.builder().name("REFACTOR").build();
            //Creamos ahora los roles
            RoleEntity roleAdmin= RoleEntity.builder().roleEnum(RoleEnum.ADMIN)
                    .permisos(Set.of(createPermission,readPermission,updatePermission,deletePermission)).build();
            RoleEntity roleUser= RoleEntity.builder().roleEnum(RoleEnum.USER)
                    .permisos(Set.of(readPermission)).build();
            RoleEntity roleDeveloper= RoleEntity.builder().roleEnum(RoleEnum.DEVELOPER)
                    .permisos(Set.of(createPermission,readPermission,updatePermission,deletePermission,refactorPermission)).build();
            RoleEntity roleInvited= RoleEntity.builder().roleEnum(RoleEnum.INVITED)
                    .permisos(Set.of(readPermission)).build();

            //Creamos los usuarios
            UserEntity userDuzz = UserEntity.builder()
                    .username("Duzz")
                    .password("$2a$10$fcLYlZomll20nn15VRh4VuYuH9V/TKoUw9CbodWWVEzHMzY91sDx2")
                    //Estos datos maneja spring security
                    .isEnabled(true) //La cuenta esta activa
                    .accountNoExpired(true) //La no ha expirado porque esta activa
                    .accountNoLocked(true) //La cuenta no esta bloqueada
                    .credentialNoExpired(true) //Las creedenciales no estan expiradas
                    .roles(Set.of(roleDeveloper))
                    .build();

            UserEntity userNoe = UserEntity.builder()
                    .username("Noe")
                    .password("$2a$10$fcLYlZomll20nn15VRh4VuYuH9V/TKoUw9CbodWWVEzHMzY91sDx2")
                    //Estos datos maneja spring security
                    .isEnabled(true) //La cuenta esta activa
                    .accountNoExpired(true) //La no ha expirado porque esta activa
                    .accountNoLocked(true) //La cuenta no esta bloqueada
                    .credentialNoExpired(true) //Las creedenciales no estan expiradas
                    .roles(Set.of(roleAdmin))
                    .build();

            UserEntity userPaul = UserEntity.builder()
                    .username("Paul")
                    .password("$2a$10$fcLYlZomll20nn15VRh4VuYuH9V/TKoUw9CbodWWVEzHMzY91sDx2")
                    //Estos datos maneja spring security
                    .isEnabled(true) //La cuenta esta activa
                    .accountNoExpired(true) //La no ha expirado porque esta activa
                    .accountNoLocked(true) //La cuenta no esta bloqueada
                    .credentialNoExpired(true) //Las creedenciales no estan expiradas
                    .roles(Set.of(roleUser))
                    .build();

            UserEntity userSoplaGaita = UserEntity.builder()
                    .username("SoplaGaitas")
                    .password("$2a$10$fcLYlZomll20nn15VRh4VuYuH9V/TKoUw9CbodWWVEzHMzY91sDx2")
                    //Estos datos maneja spring security
                    .isEnabled(true) //La cuenta esta activa
                    .accountNoExpired(true) //La no ha expirado porque esta activa
                    .accountNoLocked(true) //La cuenta no esta bloqueada
                    .credentialNoExpired(true) //Las creedenciales no estan expiradas
                    .roles(Set.of(roleInvited))
                    .build();


            //Para guardar todos de golpe
            userRepository.saveAll(List.of(userDuzz,userPaul,userNoe,userSoplaGaita));

        };
    }

}
