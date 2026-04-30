package com.ecommerce.auth.infrastructure.driver_adapters.jpa_repository;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateWay;
import com.ecommerce.auth.infrastructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsuarioDataGatewayImpl implements UsuarioGateWay {

    private final UsuarioDataJpaRepository repository;
    private final UsuarioMapper mapper;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        if (repository.existsById(usuario.getCedula())) {
            throw new RuntimeException("Ya existe un usuario con cedula: " + usuario.getCedula());
        }
        UsuarioData usuarioData = mapper.toUsuarioData(usuario);
        return mapper.toUsuario(repository.save(usuarioData));
    }

    @Override
    public Usuario buscarUsuarioPorCedula(String cedula) {
        return repository.findById(cedula)
                .map(mapper::toUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con cédula: " + cedula));
    }

    @Override
    public void eliminarUsuarioPorCedula(String cedula) {
        if (!repository.existsById(cedula)) {
            throw new RuntimeException("No se puede eliminar: usuario no encontrado con cédula: " + cedula);
        }
        repository.deleteById(cedula);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        if (!repository.existsById(usuario.getCedula())) {
            throw new RuntimeException("No se puede Encontrar el usuario: " + usuario.getCedula());
        }
        UsuarioData usuarioData = mapper.toUsuarioData(usuario);
        return mapper.toUsuario(repository.save(usuarioData));
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }
}
