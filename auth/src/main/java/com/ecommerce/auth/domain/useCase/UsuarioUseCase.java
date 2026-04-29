package com.ecommerce.auth.domain.useCase;

import com.ecommerce.auth.domain.model.gateway.EncrypterGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateWay;
import com.ecommerce.auth.domain.model.Usuario;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final UsuarioGateWay usuarioGateWay;
    private final EncrypterGateway encrypterGateway;

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getPassword() == null) {
            throw new NullPointerException("El email o password no puede ser nulo - guardarUsuario");
        }
        String passEncrypt = encrypterGateway.encrypt(usuario.getPassword());
        usuario.setPassword(passEncrypt);
        return usuarioGateWay.guardarUsuario(usuario);
    }

    public Usuario buscarUsuarioPorCedula(String cedula) {
        try {
            usuarioGateWay.buscarUsuarioPorCedula(cedula);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Usuario();
        }
        return usuarioGateWay.buscarUsuarioPorCedula(cedula);
    }

    public void eliminarUsuarioPorCedula(String cedula) {
        try {
            usuarioGateWay.eliminarUsuarioPorCedula(cedula);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        try {
            return usuarioGateWay.actualizarUsuario(usuario);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Usuario login(String email, String password) {
        if (email == null || password == null) {
            throw new NullPointerException("el correo o password no puede ser nulo - login");
        }
        try {
            return usuarioGateWay.login(email, password, encrypterGateway);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
