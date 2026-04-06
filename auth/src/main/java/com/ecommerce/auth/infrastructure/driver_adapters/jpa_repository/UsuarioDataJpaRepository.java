package com.ecommerce.auth.infrastructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UsuarioDataJpaRepository extends JpaRepository<UsuarioData, String> {

}
