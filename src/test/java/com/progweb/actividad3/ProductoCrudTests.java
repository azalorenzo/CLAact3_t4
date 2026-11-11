package com.progweb.actividad3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.progweb.actividad3.model.Categoria;
import com.progweb.actividad3.model.Producto;
import com.progweb.actividad3.repositorio.CategoriaRepositorio;
import com.progweb.actividad3.repositorio.ProductoRepositorio;
import jakarta.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductoCrudTests {

    @Autowired private MockMvc navegador;
    @Autowired private CategoriaRepositorio categoriaRepositorio;
    @Autowired private ProductoRepositorio productoRepositorio;
    @Autowired private EntityManager entidad;

    @Test
    void crearConsultarActualizarYEliminar() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNombre("Categoria de prueba");
        categoriaRepositorio.saveAndFlush(categoria);

        navegador.perform(get("/productos/nuevo"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Categoria de prueba")));

        navegador.perform(post("/productos/insertar")
                .param("nombre", "Producto de prueba CRUD")
                .param("precio", "25.50")
                .param("categoria.id", categoria.getId().toString()))
                .andExpect(redirectedUrl("/productos"));

        entidad.flush();
        entidad.clear();
        Producto producto = productoRepositorio.findAll().stream()
                .filter(elemento -> elemento.getNombre().equals("Producto de prueba CRUD"))
                .findFirst().orElseThrow();
        Long id = producto.getId();
        assertEquals(categoria.getId(), producto.getCategoria().getId());

        navegador.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producto de prueba CRUD")))
                .andExpect(content().string(containsString("Categoria de prueba")));

        navegador.perform(get("/productos/editar/" + id))
                .andExpect(status().isOk());
        navegador.perform(put("/productos/actualizar/" + id)
                .param("id", id.toString())
                .param("nombre", "Producto actualizado CRUD")
                .param("precio", "30.00")
                .param("categoria.id", categoria.getId().toString()))
                .andExpect(redirectedUrl("/productos"));
        entidad.flush();
        entidad.clear();
        assertEquals("Producto actualizado CRUD", productoRepositorio.findById(id).orElseThrow().getNombre());

        navegador.perform(post("/productos/insertar")
                .param("nombre", "Producto invalido")
                .param("precio", "-1")
                .param("categoria.id", categoria.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("producto", "precio"));

        navegador.perform(delete("/productos/eliminar/" + id))
                .andExpect(redirectedUrl("/productos"));
        entidad.flush();
        entidad.clear();
        assertFalse(productoRepositorio.existsById(id));
    }
}
