package com.ecommerce.auth.domain.useCase;

import com.ecommerce.auth.domain.model.gateway.UsuarioGateWay;
import com.ecommerce.auth.domain.model.Usuario;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final UsuarioGateWay usuarioGateWay;

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getPassword() == null) {
            throw new NullPointerException("El email o password no puede ser nulo - guardarUsuario");
        }
        Usuario usuarioGuardado = usuarioGateWay.guardarUsuario(usuario);

        return usuarioGuardado;
    }

    public Usuario buscarUsuarioPorCedula(String cedula) {
        try {
            usuarioGateWay.buscarUsuarioPorCedula(cedula);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Usuario usuarioVacio = new Usuario();
            return usuarioVacio;
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

    public Usuario actualizarUsuario(Usuario usuario){
        try{
            return usuarioGateWay.actualizarUsuario(usuario);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
