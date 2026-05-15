package com.ecommerce.auth.infrastructure.entity_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.useCase.UsuarioUseCase;
import com.ecommerce.auth.infrastructure.driver_adapters.jpa_repository.UsuarioData;
import com.ecommerce.auth.infrastructure.mapper.UsuarioMapper;
import com.ecommerce.auth.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioUseCase usuarioUseCase;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setCedula("123456");
        usuario.setNombre("Mateo");
        usuario.setEmail("test@test.com");
        usuario.setPassword("hashedPass");
        usuario.setRole("USER");
    }

    @Test
    void guardarUsuario_exito() {
        when(usuarioUseCase.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<Usuario> response = usuarioController.guardarUsuario(usuario);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("123456", response.getBody().getCedula());
        verify(usuarioUseCase, times(1)).guardarUsuario(usuario);
    }

    @Test
    void guardarUsuario_error() {
        when(usuarioUseCase.guardarUsuario(any(Usuario.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Usuario> response = usuarioController.guardarUsuario(usuario);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void buscarUsuario_exito() {
        when(usuarioUseCase.buscarUsuarioPorCedula("123456")).thenReturn(usuario);

        ResponseEntity<Usuario> response = usuarioController.buscarUsuario("123456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("123456", response.getBody().getCedula());
    }

    @Test
    void buscarUsuario_noEncontrado() {
        Usuario emptyUser = new Usuario(); // cedula is null
        when(usuarioUseCase.buscarUsuarioPorCedula("123456")).thenReturn(emptyUser);

        ResponseEntity<Usuario> response = usuarioController.buscarUsuario("123456");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarUsuario_errorInterno() {
        when(usuarioUseCase.buscarUsuarioPorCedula("123456")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Usuario> response = usuarioController.buscarUsuario("123456");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void eliminarUsuario_exito() {
        doNothing().when(usuarioUseCase).eliminarUsuarioPorCedula("123456");

        ResponseEntity<Void> response = usuarioController.eliminarUsuario("123456");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioUseCase, times(1)).eliminarUsuarioPorCedula("123456");
    }

    @Test
    void eliminarUsuario_error() {
        doThrow(new RuntimeException("Error")).when(usuarioUseCase).eliminarUsuarioPorCedula("123456");

        ResponseEntity<Void> response = usuarioController.eliminarUsuario("123456");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void actualizarUsuario_exito() {
        UsuarioData data = new UsuarioData();
        data.setCedula("123456");
        
        Usuario usuarioMapped = new Usuario();
        usuarioMapped.setCedula("123456");
        usuarioMapped.setNombre("Mateo Actualizado");
        
        when(usuarioMapper.toUsuario(data)).thenReturn(usuarioMapped);
        when(usuarioUseCase.actualizarUsuario(usuarioMapped)).thenReturn(usuarioMapped);

        ResponseEntity<Usuario> response = usuarioController.actualizarUsuario("123456", data);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Mateo Actualizado", response.getBody().getNombre());
    }

    @Test
    void actualizarUsuario_error() {
        UsuarioData data = new UsuarioData();
        Usuario usuarioMapped = new Usuario();
        when(usuarioMapper.toUsuario(data)).thenReturn(usuarioMapped);
        when(usuarioUseCase.actualizarUsuario(usuarioMapped)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Usuario> response = usuarioController.actualizarUsuario("123456", data);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void login_exito() {
        LoginRequest loginRequest = new LoginRequest("test@test.com", "rawPass");

        when(usuarioUseCase.login("test@test.com", "rawPass")).thenReturn(usuario);
        when(jwtService.generateToken("test@test.com", "USER")).thenReturn("mockedToken");

        ResponseEntity<AuthResponse> response = usuarioController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mockedToken", response.getBody().getToken());
        assertEquals("123456", response.getBody().getCedula());
        assertEquals("USER", response.getBody().getRole());
    }

    @Test
    void login_error() {
        LoginRequest loginRequest = new LoginRequest("test@test.com", "wrongPass");

        when(usuarioUseCase.login("test@test.com", "wrongPass")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<AuthResponse> response = usuarioController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
