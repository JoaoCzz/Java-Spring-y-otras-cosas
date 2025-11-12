package com.app.controller.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//Estos es para solo darle el orden del json
@JsonPropertyOrder({"username","message","jwt","status"})
public record AuthResponse(String username, String message, String jwt, boolean status){

}

