package org.example.catalog.domain.useCase;

import org.example.catalog.domain.model.Producto;
import org.example.catalog.domain.model.gateway.ProductoGateWay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoUseCaseTest {

    @Mock
    private ProductoGateWay productoGateWay;

    @InjectMocks
    private ProductoUseCase productoUseCase;

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
    void guardarProductoTest() {
        Producto productoGuardar = new Producto();
        productoGuardar.setNombre("Lechuga Romana");
        productoGuardar.setDescripcion("Lechuga fresca del campo");
        productoGuardar.setPrecio(1500.0);
        productoGuardar.setStock(50);

        Producto productoGuardado = new Producto();
        productoGuardado.setProductoId("prod-002");
        productoGuardado.setNombre("Lechuga Romana");
        productoGuardado.setDescripcion("Lechuga fresca del campo");
        productoGuardado.setPrecio(1500.0);
        productoGuardado.setStock(50);

        when(productoGateWay.guardarProducto(productoGuardar)).thenReturn(productoGuardado);

        Producto resultado = productoUseCase.guardarProducto(productoGuardar);

        assertNotNull(resultado);
        assertEquals("prod-002", resultado.getProductoId());
        assertEquals("Lechuga Romana", resultado.getNombre());
        verify(productoGateWay, times(1)).guardarProducto(productoGuardar);
    }

    @Test
    void guardarProducto_nombreODescripcionNulo_lanzaNullPointerException() {
        Producto productoInvalido = new Producto();
        // nombre y descripcion son null

        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> productoUseCase.guardarProducto(productoInvalido));

        assertEquals("Se requiere el nombre y descripción del producto para poder guardarlo", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // obtenerTodosLosProductos
    // -------------------------------------------------------------------------

    @Test
    void obtenerTodosLosProductosTest() {
        List<Producto> lista = List.of(producto);
        when(productoGateWay.obtenerTodosLosProductos()).thenReturn(lista);

        List<Producto> resultado = productoUseCase.obtenerTodosLosProductos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tomate Cherry", resultado.get(0).getNombre());
        verify(productoGateWay, times(1)).obtenerTodosLosProductos();
    }

    @Test
    void obtenerTodosLosProductos_excepcion_lanzaRuntimeException() {
        when(productoGateWay.obtenerTodosLosProductos())
                .thenThrow(new RuntimeException("Error db"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoUseCase.obtenerTodosLosProductos());

        assertEquals("Error db", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // buscarProductoPorId
    // -------------------------------------------------------------------------

    @Test
    void buscarProductoPorIdTest() {
        when(productoGateWay.buscarProductoPorId("prod-001")).thenReturn(producto);

        Producto resultado = productoUseCase.buscarProductoPorId("prod-001");

        assertNotNull(resultado);
        assertEquals("prod-001", resultado.getProductoId());
        verify(productoGateWay, times(2)).buscarProductoPorId("prod-001");
    }

    @Test
    void buscarProductoPorId_excepcion_retornaProductoVacio() {
        when(productoGateWay.buscarProductoPorId("prod-001"))
                .thenThrow(new RuntimeException("Error db"));

        Producto resultado = productoUseCase.buscarProductoPorId("prod-001");

        assertNotNull(resultado);
        assertNull(resultado.getProductoId());
    }


    @Test
    void buscarProductosPorNombreTest() {
        List<Producto> lista = List.of(producto);
        when(productoGateWay.buscarProductosPorNombre("Tomate")).thenReturn(lista);

        List<Producto> resultado = productoUseCase.buscarProductosPorNombre("Tomate");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Tomate Cherry", resultado.get(0).getNombre());
        verify(productoGateWay, times(1)).buscarProductosPorNombre("Tomate");
    }

    @Test
    void buscarProductosPorNombre_excepcion_lanzaRuntimeException() {
        when(productoGateWay.buscarProductosPorNombre("Tomate"))
                .thenThrow(new RuntimeException("Error db"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoUseCase.buscarProductosPorNombre("Tomate"));

        assertEquals("Error db", ex.getMessage());
    }

    @Test
    void eliminarProductoPorIdTest() {
        doNothing().when(productoGateWay).eliminarProductoPorId("prod-001");

        assertDoesNotThrow(() -> productoUseCase.eliminarProductoPorId("prod-001"));
        verify(productoGateWay, times(1)).eliminarProductoPorId("prod-001");
    }

    @Test
    void eliminarProductoPorId_excepcion_lanzaRuntimeException() {
        doThrow(new RuntimeException("Error db"))
                .when(productoGateWay).eliminarProductoPorId("prod-001");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoUseCase.eliminarProductoPorId("prod-001"));

        assertEquals("Error db", ex.getMessage());
    }


    @Test
    void actualizarProductoTest() {
        Producto productoUpd = new Producto();
        productoUpd.setProductoId("prod-001");
        productoUpd.setNombre("Tomate Cherry Actualizado");
        productoUpd.setDescripcion("Tomate orgánico premium");
        productoUpd.setPrecio(4000.0);
        productoUpd.setStock(80);

        when(productoGateWay.actualizarProducto("prod-001", productoUpd)).thenReturn(productoUpd);

        Producto resultado = productoUseCase.actualizarProducto("prod-001", productoUpd);

        assertNotNull(resultado);
        assertEquals("prod-001", resultado.getProductoId());
        assertEquals("Tomate Cherry Actualizado", resultado.getNombre());
        assertEquals(4000.0, resultado.getPrecio());

        assertNotEquals(producto.getNombre(), resultado.getNombre());
        assertNotEquals(producto.getPrecio(), resultado.getPrecio());

        verify(productoGateWay, times(1)).actualizarProducto("prod-001", productoUpd);
    }

    @Test
    void actualizarProducto_cuandoNoExiste_lanzaRuntimeException() {
        when(productoGateWay.actualizarProducto("prod-001", producto))
                .thenThrow(new RuntimeException("No se puede encontrar el producto: prod-001"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productoUseCase.actualizarProducto("prod-001", producto));

        assertEquals("No se puede encontrar el producto: prod-001", ex.getMessage());
        verify(productoGateWay, times(1)).actualizarProducto("prod-001", producto);
    }
}