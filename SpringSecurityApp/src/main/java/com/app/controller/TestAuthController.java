package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/**
 * Controlador de prueba para verificar la configuración de seguridad y autenticación.
 *
 * <p>Define endpoints básicos (GET, POST, PUT, DELETE, PATCH) bajo la ruta base <b>/auth</b>.
 * Cada endpoint puede requerir permisos o roles específicos según las reglas de
 * {@link org.springframework.security.access.prepost.PreAuthorize}.</p>
 *
 * <p>La anotación {@code @PreAuthorize("denyAll()")} aplicada a nivel de clase
 * indica que, por defecto, todos los endpoints están denegados,
 * a menos que se indique lo contrario a nivel de método.</p>
 */
@RestController
@RequestMapping("/auth")
// Los endpoints que no tengan reglas específicas serán denegados por defecto.
@PreAuthorize("denyAll()")
public class TestAuthController {
    /**
     * Endpoint de prueba para solicitudes GET.
     * <p>Requiere el permiso <b>READ</b>.</p>
     *
     * @return un mensaje de texto simple.
     */
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('READ')")
    public String helloGet() {
        return "HelloWorld - GET";
    }

    /**
     * Endpoint de prueba para solicitudes POST.
     * <p>No tiene restricción específica, pero hereda la política {@code denyAll()} de la clase,
     * por lo que no será accesible a menos que se configure una excepción global.</p>
     *
     * @return un mensaje de texto simple.
     */
    @PostMapping("/post")
    public String helloPost() {
        return "HelloWorld - POST";
    }

    /**
     * Endpoint de prueba para solicitudes PUT.
     * <p>Actualmente no tiene restricción de acceso declarada, por lo que
     * también se encuentra denegado por la política general {@code denyAll()}.</p>
     *
     * @return un mensaje de texto simple.
     */
    @PutMapping("/put")
    public String helloPut() {
        return "HelloWorld - PUT";
    }

    /**
     * Endpoint de prueba para solicitudes DELETE.
     * <p>Sin reglas de acceso específicas, por lo tanto denegado por defecto.</p>
     *
     * @return un mensaje de texto simple.
     */
    @DeleteMapping("/delete")
    public String helloDelete() {
        return "HelloWorld - DELETE";
    }

    /**
     * Endpoint de prueba para solicitudes PATCH.
     * <p>Requiere el permiso <b>REFACTOR</b> para poder acceder.</p>
     *
     * @return un mensaje de texto simple.
     */
    @PatchMapping("/patch")
    @PreAuthorize("hasAuthority('REFACTOR')")
    public String helloPatch() {
        return "HelloWorld - PATCH";
    }
}
