package com.progweb.actividad3.servicio;

import java.util.List;
import org.springframework.stereotype.Service;
import com.progweb.actividad3.model.Categoria;
import com.progweb.actividad3.repositorio.CategoriaRepositorio;

@Service
public class CategoriaServicio {

    private final CategoriaRepositorio categoriaRepositorio;

    public CategoriaServicio(CategoriaRepositorio categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepositorio.findAll();
    }

    public Categoria buscarCategoria(Long id) {
        return categoriaRepositorio.findById(id).orElse(null);
    }
}
