package com.progweb.actividad3.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.progweb.actividad3.model.Producto;

public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
}
