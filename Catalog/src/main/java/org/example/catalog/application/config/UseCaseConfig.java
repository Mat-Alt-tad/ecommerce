package org.example.catalog.application.config;

import org.example.catalog.domain.model.gateway.ProductoGateWay;
import org.example.catalog.domain.useCase.ProductoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProductoUseCase productoUseCase(ProductoGateWay productoGateWay) {
        return new ProductoUseCase(productoGateWay);
    }
}
