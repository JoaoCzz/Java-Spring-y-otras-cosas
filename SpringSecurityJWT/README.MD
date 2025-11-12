🔐 Spring Security + JWT Demo

Este proyecto es una práctica donde aprendí a configurar Spring Security 6 con Spring Boot 3, implementando autenticación y autorización mediante JWT, usando roles y permisos almacenados en base de datos.

Implementa autenticación basada en UserDetailsService, generación de tokens JWT firmados con HMAC256, y control de acceso a endpoints según roles y permisos. Las contraseñas se encriptan con BCrypt.

Además, incluye un filtro personalizado (JwtTokenValidator) que valida el token en cada petición y establece la autenticación en el SecurityContext de Spring.

📚 Aprendizaje

Aprendí a manejar:

Spring Security con autenticación basada en base de datos.

Creación de roles y permisos dinámicos para cada usuario.

Generación y validación de tokens JWT.

Uso de SecurityContext para manejar la sesión del usuario.

Control de acceso a endpoints mediante roles y permisos.

Encriptación de contraseñas con BCrypt.

🔗 Referencias

Este proyecto se desarrolló siguiendo el tutorial de Un Programador Nace:

👉 https://www.youtube.com/watch?v=wzGCBZCknUs Video completo sobre Spring Security y JWT