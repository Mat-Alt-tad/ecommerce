package org.example.catalog.infrastructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoDataJpaRepository extends JpaRepository<ProductoData, String> {

    List<ProductoData> findByNombreContainingIgnoreCase(String nombre);
}
