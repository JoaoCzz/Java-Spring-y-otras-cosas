package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
//Los endpoints que no estan especificados va a rechazar a todos
@PreAuthorize("denyAll()")
public class TestAuthController {
        @GetMapping("/get")
        @PreAuthorize("hasAuthority('READ')")
    public String helloGet(){
        return "HelloWorld -GET";
    }
    @PostMapping("/post")
    public String helloPost() {
        return "HelloWorld - POST";
    }

    @PutMapping("/put")
    public String helloPut() {
        return "HelloWorld - PUT";
    }

    @DeleteMapping("/delete")
    public String helloDelete() {
        return "HelloWorld - DELETE";
    }

    @PatchMapping("/patch")
    @PreAuthorize("hasAuthority('REFACTOR')")
    public String helloPatch() {
        return "HelloWorld - PATCH";
    }
}
