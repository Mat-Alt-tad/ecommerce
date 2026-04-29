package com.ecommerce.auth.infrastructure.entity_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.useCase.UsuarioUseCase;
import com.ecommerce.auth.infrastructure.driver_adapters.jpa_repository.UsuarioData;
import com.ecommerce.auth.infrastructure.mapper.UsuarioMapper;
import com.ecommerce.auth.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/eccomerce/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final JwtService jwtService;

    @PostMapping("/guardar")
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioValidadoGuardado = usuarioUseCase.guardarUsuario(usuario);
            return new ResponseEntity<>(usuarioValidadoGuardado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/buscar/{cedula}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable String cedula) {
        try {
            Usuario usuario = usuarioUseCase.buscarUsuarioPorCedula(cedula);
            if (usuario.getCedula() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminar/{cedula}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String cedula) {
        try {
            usuarioUseCase.eliminarUsuarioPorCedula(cedula);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/actualizar/{cedula}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String cedula, @RequestBody UsuarioData usuarioData) {
        try {
            usuarioData.setCedula(cedula);
            Usuario usuarioActualizado = usuarioUseCase.actualizarUsuario(usuarioMapper.toUsuario(usuarioData));
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Usuario usuario = usuarioUseCase.login(loginRequest.getEmail(), loginRequest.getPassword());
            String token = jwtService.generateToken(usuario.getEmail(), usuario.getRole());
            AuthResponse response = new AuthResponse(
                    token,
                    usuario.getCedula(),
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getRole()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
