package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/**
 * Controlador de prueba para verificar la configuración de seguridad y autenticación.
 *
 * Define endpoints básicos (GET, POST, PUT, DELETE, PATCH) bajo la ruta base "/auth".
 * Cada endpoint puede requerir permisos o roles específicos según las reglas de seguridad
 * definidas mediante la anotación @PreAuthorize.
 *
 * La anotación @PreAuthorize("denyAll()") aplicada a nivel de clase indica que,
 * por defecto, todos los endpoints están denegados, a menos que se indique
 * lo contrario en cada mét0do.
 */
@RestController
@RequestMapping("/auth")
// Los endpoints que no tengan reglas específicas serán denegados por defecto.
@PreAuthorize("denyAll()")
public class TestAuthController {
    /**
     * Endpoint de prueba para solicitudes GET.
     * Requiere el permiso "READ".
     *
     */
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('READ')")
    public String helloGet() {
        return "HelloWorld - GET";
    }

    /**
     * Endpoint de prueba para solicitudes POST.
     * No tiene restricción específica, por lo que hereda la política denyAll()
     * y no será accesible a menos que se configure una excepción global.
     *
     */
    @PostMapping("/post")
    public String helloPost() {
        return "HelloWorld - POST";
    }

    /**
     * Endpoint de prueba para solicitudes PUT.
     * Actualmente no tiene restricción de acceso declarada,
     * por lo tanto está denegado por la política general.
     *
     */
    @PutMapping("/put")
    public String helloPut() {
        return "HelloWorld - PUT";
    }

    /**
     * Endpoint de prueba para solicitudes DELETE.
     * Sin reglas de acceso específicas, por lo tanto denegado por defecto.
     *
     */
    @DeleteMapping("/delete")
    public String helloDelete() {
        return "HelloWorld - DELETE";
    }

    /**
     * Endpoint de prueba para solicitudes PATCH.
     * Requiere el permiso "REFACTOR" para poder acceder.
     *
     */
    @PatchMapping("/patch")
    @PreAuthorize("hasAuthority('REFACTOR')")
    public String helloPatch() {
        return "HelloWorld - PATCH";
    }
}
