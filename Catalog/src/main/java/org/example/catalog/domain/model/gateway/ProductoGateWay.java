package org.example.catalog.domain.model.gateway;

import org.example.catalog.domain.model.Producto;

import java.util.List;

public interface ProductoGateWay {
    Producto guardarProducto(Producto producto);
    List<Producto> obtenerTodosLosProductos();
    Producto buscarProductoPorId(String productoId);
    List<Producto> buscarProductosPorNombre(String nombre);
    void eliminarProductoPorId(String productoId);
    Producto actualizarProducto(String productoId, Producto producto);
}
