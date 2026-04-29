package com.ecommerce.auth.infrastructure.entity_points;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String cedula;
    private String nombre;
    private String email;
    private String role;
}
