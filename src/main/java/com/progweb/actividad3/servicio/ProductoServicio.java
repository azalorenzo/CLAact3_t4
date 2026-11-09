package com.progweb.actividad3.servicio;

import java.util.List;
import org.springframework.stereotype.Service;
import com.progweb.actividad3.model.Categoria;
import com.progweb.actividad3.model.Producto;
import com.progweb.actividad3.repositorio.ProductoRepositorio;

@Service
public class ProductoServicio {

    private final ProductoRepositorio productoRepositorio;
    private final CategoriaServicio categoriaServicio;

    public ProductoServicio(ProductoRepositorio productoRepositorio,
                            CategoriaServicio categoriaServicio) {
        this.productoRepositorio = productoRepositorio;
        this.categoriaServicio = categoriaServicio;
    }

    public List<Producto> listarProductos() {
        return productoRepositorio.findAll();
    }

    public Producto buscarProducto(Long id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    // Método  para CREAR
    public Producto crearProducto(Producto producto) {
        if (producto.getId() != null) {
            throw new IllegalArgumentException("El producto ya existe, usa actualizar");
        }

        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Selecciona una categoria");
        }

        Categoria categoria = categoriaServicio.buscarCategoria(producto.getCategoria().getId());
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria no existe");
        }

        producto.setCategoria(categoria);
        return productoRepositorio.save(producto);
    }

    // Método para ACTUALIZAR
    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        if (!productoRepositorio.existsById(id)) {
            throw new IllegalArgumentException("El producto no existe");
        }

        if (productoDetalles.getCategoria() == null || productoDetalles.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Selecciona una categoria");
        }

        Categoria categoria = categoriaServicio.buscarCategoria(productoDetalles.getCategoria().getId());
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria no existe");
        }

        productoDetalles.setId(id);
        productoDetalles.setCategoria(categoria);
        return productoRepositorio.save(productoDetalles);
    }

    public void eliminarProducto(Long id) {
        if (!productoRepositorio.existsById(id)) {
            throw new IllegalArgumentException("El producto no existe");
        }
        productoRepositorio.deleteById(id);
    }
}