package com.progweb.actividad3.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.progweb.actividad3.model.Categoria;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
}
