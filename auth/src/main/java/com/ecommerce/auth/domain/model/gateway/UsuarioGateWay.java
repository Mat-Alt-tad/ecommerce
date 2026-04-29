package com.ecommerce.auth.domain.model.gateway;

import com.ecommerce.auth.domain.model.Usuario;

public interface UsuarioGateWay {
    Usuario guardarUsuario(Usuario usuario);
    Usuario buscarUsuarioPorCedula(String cedula);
    void eliminarUsuarioPorCedula(String cedula);
    Usuario actualizarUsuario(Usuario usuario);
    Usuario login(String email, String password, EncrypterGateway encrypterGateway);
}
