# 🔐 Spring Security Demo

Este proyecto es una práctica donde aprendí a configurar **Spring Security 6 con Spring Boot 3**, usando **roles, permisos y usuarios desde base de datos**.

Implementa autenticación básica con `UserDetailsService`, `DaoAuthenticationProvider` y contraseñas encriptadas con **BCrypt**.  
También usa anotaciones como `@PreAuthorize` para controlar el acceso a los endpoints según los permisos del usuario.

Aprendí todo siguiendo este video de **Un Programador Nace**:
👉 [https://www.youtube.com/watch?v=IPWBQDMIYkc&t=6556s](https://www.youtube.com/watch?v=IPWBQDMIYkc&t=6556s)

Incluye:
- Creación automática de usuarios, roles y permisos.
- Configuración de seguridad con `SecurityFilterChain`.
- Controlador de prueba (`TestAuthController`) con distintos niveles de acceso.
