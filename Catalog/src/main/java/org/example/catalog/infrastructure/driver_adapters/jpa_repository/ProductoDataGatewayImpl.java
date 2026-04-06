package org.example.catalog.infrastructure.driver_adapters.jpa_repository;

import lombok.RequiredArgsConstructor;
import org.example.catalog.domain.model.Producto;
import org.example.catalog.domain.model.gateway.ProductoGateWay;
import org.example.catalog.infrastructure.mapper.ProductoMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductoDataGatewayImpl implements ProductoGateWay {

    private final ProductoDataJpaRepository repository;
    private final ProductoMapper mapper;

    @Override
    public Producto guardarProducto(Producto producto) {
        ProductoData productoData = mapper.toProductoData(producto);
        productoData.setProductoId(null);
        return mapper.toProducto(repository.save(productoData));
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return repository.findAll()
                .stream()
                .map(mapper::toProducto)
                .collect(Collectors.toList());
    }

    @Override
    public Producto buscarProductoPorId(String productoId) {
        return repository.findById(productoId)
                .map(mapper::toProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId));
    }

    @Override
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(mapper::toProducto)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarProductoPorId(String productoId) {
        if (!repository.existsById(productoId)) {
            throw new RuntimeException("No se puede eliminar: producto no encontrado con id: " + productoId);
        }
        repository.deleteById(productoId);
    }

    @Override
    public Producto actualizarProducto(String productoId, Producto producto) {
        if (!repository.existsById(productoId)) {
            throw new RuntimeException("No se puede actualizar: producto no encontrado con id: " + productoId);
        }
        ProductoData productoData = mapper.toProductoData(producto);
        productoData.setProductoId(productoId);
        return mapper.toProducto(repository.save(productoData));
    }
}
