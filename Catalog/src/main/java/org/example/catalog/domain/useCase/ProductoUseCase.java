package org.example.catalog.domain.useCase;

import lombok.RequiredArgsConstructor;
import org.example.catalog.domain.model.Producto;
import org.example.catalog.domain.model.gateway.ProductoGateWay;

import java.util.List;

@RequiredArgsConstructor
public class ProductoUseCase {

    private final ProductoGateWay productoGateWay;

    public Producto guardarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getDescripcion() == null) {
            throw new NullPointerException("Se requiere el nombre y descripción del producto para poder guardarlo");
        }
        Producto productoGuardado = productoGateWay.guardarProducto(producto);
        return productoGuardado;
    }

    public List<Producto> obtenerTodosLosProductos() {
        try {
            return productoGateWay.obtenerTodosLosProductos();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Producto buscarProductoPorId(String productoId) {
        try {
            productoGateWay.buscarProductoPorId(productoId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Producto productoVacio = new Producto();
            return productoVacio;
        }
        return productoGateWay.buscarProductoPorId(productoId);
    }

    public List<Producto> buscarProductosPorNombre(String nombre) {
        try {
            return productoGateWay.buscarProductosPorNombre(nombre);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void eliminarProductoPorId(String productoId) {
        try {
            productoGateWay.eliminarProductoPorId(productoId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Producto actualizarProducto(String productoId, Producto producto) {
        try {
            return productoGateWay.actualizarProducto(productoId, producto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
