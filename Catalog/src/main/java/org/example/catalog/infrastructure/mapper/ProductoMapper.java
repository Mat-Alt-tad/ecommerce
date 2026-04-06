package org.example.catalog.infrastructure.mapper;

import org.example.catalog.domain.model.Producto;
import org.example.catalog.infrastructure.driver_adapters.jpa_repository.ProductoData;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toProducto(ProductoData productoData) {
        return new Producto(
                productoData.getProductoId(),
                productoData.getNombre(),
                productoData.getDescripcion(),
                productoData.getPrecio(),
                productoData.getStock()
        );
    }

    public ProductoData toProductoData(Producto producto) {
        return new ProductoData(
                producto.getProductoId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}
