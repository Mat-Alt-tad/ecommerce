package org.example.catalog.infrastructure.entity_points;

import lombok.RequiredArgsConstructor;
import org.example.catalog.domain.model.Producto;
import org.example.catalog.domain.useCase.ProductoUseCase;
import org.example.catalog.infrastructure.driver_adapters.jpa_repository.ProductoData;
import org.example.catalog.infrastructure.mapper.ProductoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ecommerce/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;
    private final ProductoMapper productoMapper;

    @PostMapping("/guardar")
    public ResponseEntity<Producto> guardarProducto(@RequestBody ProductoData productoData) {
        try {
            Producto productoGuardado = productoUseCase.guardarProducto(productoMapper.toProducto(productoData));
            return new ResponseEntity<>(productoGuardado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Producto>> obtenerTodos() {
        try {
            List<Producto> productos = productoUseCase.obtenerTodosLosProductos();
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/{productoId}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable String productoId) {
        try {
            Producto producto = productoUseCase.buscarProductoPorId(productoId);
            if (producto.getProductoId() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(producto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Producto>> buscarPorNombre(@PathVariable String nombre) {
        try {
            List<Producto> productos = productoUseCase.buscarProductosPorNombre(nombre);
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eliminar/{productoId}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String productoId) {
        try {
            productoUseCase.eliminarProductoPorId(productoId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/actualizar/{productoId}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable String productoId, @RequestBody ProductoData productoData) {
        try {
            productoData.setProductoId(productoId);
            Producto productoActualizado = productoUseCase.actualizarProducto(productoId, productoMapper.toProducto(productoData));
            return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
