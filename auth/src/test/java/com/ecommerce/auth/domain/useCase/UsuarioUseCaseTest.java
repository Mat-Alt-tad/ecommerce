package com.ecommerce.auth.domain.useCase;
import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateWay;
import com.ecommerce.auth.domain.model.gateway.EncrypterGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioGateWay usuarioGateWay;

    @Mock
    private EncrypterGateway encrypterGateway;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setCedula("123456");
        usuario.setNombre("Mateo");
        usuario.setEmail("test@test.com");
        usuario.setPassword("hashedPass");
    }

    @Test
    void actualizarUsuarioTest() {
        Usuario usuarioUpd = new Usuario();
        usuarioUpd.setCedula("123456");
        usuarioUpd.setNombre("Mateo Actualizado");
        usuarioUpd.setEmail("Matlag@test.com");
        usuarioUpd.setPassword("hashedPass");

        when(usuarioGateWay.actualizarUsuario(usuarioUpd)).thenReturn(usuarioUpd);

        Usuario resultado = usuarioUseCase.actualizarUsuario(usuarioUpd);

        assertNotNull(resultado);
        assertEquals("123456", resultado.getCedula());
        assertEquals("Mateo Actualizado", resultado.getNombre());
        assertEquals("Matlag@test.com", resultado.getEmail());

        assertNotEquals(usuario.getNombre(), resultado.getNombre());
        assertNotEquals(usuario.getEmail(), resultado.getEmail());

        verify(usuarioGateWay, times(1)).actualizarUsuario(usuarioUpd);
    }

    @Test
    void actualizarUsuario_cuandoNoExiste_lanzaRuntimeException() {
        when(usuarioGateWay.actualizarUsuario(usuario))
                .thenThrow(new RuntimeException("No se puede Encontrar el usuario: 123456"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioUseCase.actualizarUsuario(usuario));

        assertEquals("No se puede Encontrar el usuario: 123456", ex.getMessage());
        verify(usuarioGateWay, times(1)).actualizarUsuario(usuario);
    }
}