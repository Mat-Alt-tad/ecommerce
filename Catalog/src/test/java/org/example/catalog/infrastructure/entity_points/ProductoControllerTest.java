package org.example.catalog.infrastructure.entity_points;

import org.example.catalog.domain.model.Producto;
import org.example.catalog.domain.useCase.ProductoUseCase;
import org.example.catalog.infrastructure.driver_adapters.jpa_repository.ProductoData;
import org.example.catalog.infrastructure.mapper.ProductoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoUseCase productoUseCase;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setProductoId("prod-001");
        producto.setNombre("Tomate Cherry");
        producto.setDescripcion("Tomate orgánico de temporada");
        producto.setPrecio(3500.0);
        producto.setStock(100);
    }

    // -------------------------------------------------------------------------
    // guardarProducto
    // -------------------------------------------------------------------------

    @Test
    void guardarProducto_exito() {
        ProductoData data = new ProductoData();
        when(productoMapper.toProducto(data)).thenReturn(producto);
        when(productoUseCase.guardarProducto(producto)).thenReturn(producto);

        ResponseEntity<Producto> response = productoController.guardarProducto(data);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("prod-001", response.getBody().getProductoId());
        verify(productoUseCase, times(1)).guardarProducto(producto);
    }

    @Test
    void guardarProducto_error() {
        ProductoData data = new ProductoData();
        when(productoMapper.toProducto(data)).thenReturn(producto);
        when(productoUseCase.guardarProducto(any(Producto.class)))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<Producto> response = productoController.guardarProducto(data);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // -------------------------------------------------------------------------
    // obtenerTodos
    // -------------------------------------------------------------------------

    @Test
    void obtenerTodos_exito() {
        List<Producto> lista = List.of(producto);
        when(productoUseCase.obtenerTodosLosProductos()).thenReturn(lista);

        ResponseEntity<List<Producto>> response = productoController.obtenerTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(productoUseCase, times(1)).obtenerTodosLosProductos();
    }

    @Test
    void obtenerTodos_error() {
        when(productoUseCase.obtenerTodosLosProductos())
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Producto>> response = productoController.obtenerTodos();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // -------------------------------------------------------------------------
    // buscarPorId
    // -------------------------------------------------------------------------

    @Test
    void buscarPorId_exito() {
        when(productoUseCase.buscarProductoPorId("prod-001")).thenReturn(producto);

        ResponseEntity<Producto> response = productoController.buscarPorId("prod-001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("prod-001", response.getBody().getProductoId());
    }

    @Test
    void buscarPorId_noEncontrado() {
        Producto productoVacio = new Producto(); // productoId is null
        when(productoUseCase.buscarProductoPorId("prod-001")).thenReturn(productoVacio);

        ResponseEntity<Producto> response = productoController.buscarPorId("prod-001");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarPorId_errorInterno() {
        when(productoUseCase.buscarProductoPorId("prod-001"))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<Producto> response = productoController.buscarPorId("prod-001");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // -------------------------------------------------------------------------
    // buscarPorNombre
    // -------------------------------------------------------------------------

    @Test
    void buscarPorNombre_exito() {
        List<Producto> lista = List.of(producto);
        when(productoUseCase.buscarProductosPorNombre("Tomate")).thenReturn(lista);

        ResponseEntity<List<Producto>> response = productoController.buscarPorNombre("Tomate");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(productoUseCase, times(1)).buscarProductosPorNombre("Tomate");
    }

    @Test
    void buscarPorNombre_error() {
        when(productoUseCase.buscarProductosPorNombre("Tomate"))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Producto>> response = productoController.buscarPorNombre("Tomate");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // -------------------------------------------------------------------------
    // eliminarProducto
    // -------------------------------------------------------------------------

    @Test
    void eliminarProducto_exito() {
        doNothing().when(productoUseCase).eliminarProductoPorId("prod-001");

        ResponseEntity<Void> response = productoController.eliminarProducto("prod-001");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productoUseCase, times(1)).eliminarProductoPorId("prod-001");
    }

    @Test
    void eliminarProducto_error() {
        doThrow(new RuntimeException("Error"))
                .when(productoUseCase).eliminarProductoPorId("prod-001");

        ResponseEntity<Void> response = productoController.eliminarProducto("prod-001");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // -------------------------------------------------------------------------
    // actualizarProducto
    // -------------------------------------------------------------------------

    @Test
    void actualizarProducto_exito() {
        ProductoData data = new ProductoData();
        data.setProductoId("prod-001");

        Producto productoMapped = new Producto();
        productoMapped.setProductoId("prod-001");
        productoMapped.setNombre("Tomate Cherry Actualizado");
        productoMapped.setPrecio(4000.0);

        when(productoMapper.toProducto(data)).thenReturn(productoMapped);
        when(productoUseCase.actualizarProducto("prod-001", productoMapped))
                .thenReturn(productoMapped);

        ResponseEntity<Producto> response = productoController.actualizarProducto("prod-001", data);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Tomate Cherry Actualizado", response.getBody().getNombre());
    }

    @Test
    void actualizarProducto_error() {
        ProductoData data = new ProductoData();
        Producto productoMapped = new Producto();
        when(productoMapper.toProducto(data)).thenReturn(productoMapped);
        when(productoUseCase.actualizarProducto(eq("prod-001"), any(Producto.class)))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<Producto> response = productoController.actualizarProducto("prod-001", data);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}