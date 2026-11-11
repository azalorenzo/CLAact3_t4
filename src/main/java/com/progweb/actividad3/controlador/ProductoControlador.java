package com.progweb.actividad3.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.progweb.actividad3.model.Producto;
import com.progweb.actividad3.servicio.CategoriaServicio;
import com.progweb.actividad3.servicio.ProductoServicio;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductoControlador {

    private final ProductoServicio productoServicio;
    private final CategoriaServicio categoriaServicio;

    public ProductoControlador(ProductoServicio productoServicio, CategoriaServicio categoriaServicio) {
        this.productoServicio = productoServicio;
        this.categoriaServicio = categoriaServicio;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/productos";
    }

    @GetMapping("/productos")
    public String listar(Model modelo) {
        modelo.addAttribute("productos", productoServicio.listarProductos());
        return "productos";
    }

    @GetMapping("/productos/nuevo")
    public String nuevo(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        modelo.addAttribute("categorias", categoriaServicio.listarCategorias());
        return "formulario";
    }

    @GetMapping("/productos/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model modelo) {
        Producto producto = productoServicio.buscarProducto(id);
        if (producto == null) {
            return "redirect:/productos";
        }
        modelo.addAttribute("producto", producto);
        modelo.addAttribute("categorias", categoriaServicio.listarCategorias());
        return "formulario";
    }

    // Endpoint para INSERTAR (Crear)
    @PostMapping("/productos/insertar")
    public String insertar(@Valid @ModelAttribute("producto") Producto producto,
                           BindingResult resultado, Model modelo,
                           RedirectAttributes redirectAttributes) { // 1. Añadir RedirectAttributes
        if (!resultado.hasErrors()) {
            try {
                productoServicio.crearProducto(producto);
                redirectAttributes.addFlashAttribute("exito", "¡Producto agregado exitosamente!"); // 2. Mensaje de éxito
                return "redirect:/productos";
            } catch (IllegalArgumentException error) {
                resultado.reject("producto", error.getMessage());
            }
        }
        modelo.addAttribute("categorias", categoriaServicio.listarCategorias());
        return "formulario";
    }

    @PutMapping("/productos/actualizar")
    public String actualizarDesdeBody(@Valid @ModelAttribute("producto") Producto producto,
                                      BindingResult resultado, Model modelo,
                                      RedirectAttributes redirectAttributes) {
        if (producto.getId() == null || producto.getId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envía un id válido en el body");
        }
        return actualizar(producto.getId(), producto, resultado, modelo, redirectAttributes);
    }

    @PutMapping("/productos/actualizar/{id}")
    public String actualizar(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("producto") Producto producto,
                             BindingResult resultado, Model modelo,
                             RedirectAttributes redirectAttributes) { // 1. Añadir RedirectAttributes
        producto.setId(id);
        if (!resultado.hasErrors()) {
            try {
                productoServicio.actualizarProducto(id, producto);
                redirectAttributes.addFlashAttribute("exito", "¡Producto actualizado correctamente!"); // 2. Mensaje de éxito
                return "redirect:/productos";
            } catch (IllegalArgumentException error) {
                resultado.reject("producto", error.getMessage());
            }
        }
        modelo.addAttribute("categorias", categoriaServicio.listarCategorias());
        return "formulario";
    }

    @DeleteMapping("/productos/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id,
                           RedirectAttributes redirectAttributes) { // 1. Añadir RedirectAttributes
        if (productoServicio.buscarProducto(id) != null) {
            productoServicio.eliminarProducto(id);
            redirectAttributes.addFlashAttribute("exito", "¡Producto eliminado correctamente!"); // 2. Mensaje de éxito
        }
        return "redirect:/productos";
    }
}
