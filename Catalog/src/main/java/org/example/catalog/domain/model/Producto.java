package org.example.catalog.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private String productoId;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
}
