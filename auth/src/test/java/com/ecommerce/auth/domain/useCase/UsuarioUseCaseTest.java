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

    @Test
    void guardarUsuarioTest() {
        Usuario usuarioGuardar = new Usuario();
        usuarioGuardar.setEmail("test@test.com");
        usuarioGuardar.setPassword("password123");
        
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setEmail("test@test.com");
        usuarioGuardado.setPassword("hashedPass123");
        
        when(encrypterGateway.encrypt("password123")).thenReturn("hashedPass123");
        when(usuarioGateWay.guardarUsuario(any(Usuario.class))).thenReturn(usuarioGuardado);
        
        Usuario resultado = usuarioUseCase.guardarUsuario(usuarioGuardar);
        
        assertNotNull(resultado);
        assertEquals("hashedPass123", resultado.getPassword());
        verify(encrypterGateway, times(1)).encrypt("password123");
        verify(usuarioGateWay, times(1)).guardarUsuario(usuarioGuardar);
    }

    @Test
    void guardarUsuario_emailOPasswordNulo_lanzaNullPointerException() {
        Usuario usuarioInvalido = new Usuario();
        
        NullPointerException ex = assertThrows(NullPointerException.class, () -> usuarioUseCase.guardarUsuario(usuarioInvalido));
        assertEquals("El email o password no puede ser nulo - guardarUsuario", ex.getMessage());
    }

    @Test
    void buscarUsuarioPorCedulaTest() {
        when(usuarioGateWay.buscarUsuarioPorCedula("123456")).thenReturn(usuario);
        
        Usuario resultado = usuarioUseCase.buscarUsuarioPorCedula("123456");
        
        assertNotNull(resultado);
        assertEquals("123456", resultado.getCedula());
        verify(usuarioGateWay, times(1)).buscarUsuarioPorCedula("123456");
    }

    @Test
    void buscarUsuarioPorCedula_excepcion_retornaUsuarioVacio() {
        when(usuarioGateWay.buscarUsuarioPorCedula("123456")).thenThrow(new RuntimeException("Error db"));
        
        Usuario resultado = usuarioUseCase.buscarUsuarioPorCedula("123456");
        
        assertNotNull(resultado);
        assertNull(resultado.getCedula());
    }

    @Test
    void eliminarUsuarioPorCedulaTest() {
        doNothing().when(usuarioGateWay).eliminarUsuarioPorCedula("123456");
        
        assertDoesNotThrow(() -> usuarioUseCase.eliminarUsuarioPorCedula("123456"));
        verify(usuarioGateWay, times(1)).eliminarUsuarioPorCedula("123456");
    }

    @Test
    void eliminarUsuarioPorCedula_excepcion_lanzaRuntimeException() {
        doThrow(new RuntimeException("Error db")).when(usuarioGateWay).eliminarUsuarioPorCedula("123456");
        
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioUseCase.eliminarUsuarioPorCedula("123456"));
        assertEquals("Error db", ex.getMessage());
    }

    @Test
    void loginTest() {
        when(usuarioGateWay.buscarUsuarioPorEmail("test@test.com")).thenReturn(usuario);
        when(encrypterGateway.matches("rawPassword", "hashedPass")).thenReturn(true);
        
        Usuario resultado = usuarioUseCase.login("test@test.com", "rawPassword");
        
        assertNotNull(resultado);
        assertEquals("test@test.com", resultado.getEmail());
    }

    @Test
    void login_credencialesIncorrectas_lanzaRuntimeException() {
        when(usuarioGateWay.buscarUsuarioPorEmail("test@test.com")).thenReturn(usuario);
        when(encrypterGateway.matches("wrongPass", "hashedPass")).thenReturn(false);
        
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioUseCase.login("test@test.com", "wrongPass"));
        assertEquals("Credenciales incorrectas para el email: test@test.com", ex.getMessage());
    }

    @Test
    void login_emailOPasswordNulo_lanzaNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> usuarioUseCase.login(null, "pass"));
        assertEquals("el correo o password no puede ser nulo - login", ex.getMessage());
    }
}