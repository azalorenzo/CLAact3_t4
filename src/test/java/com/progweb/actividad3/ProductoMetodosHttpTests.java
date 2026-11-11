package com.progweb.actividad3;

import com.progweb.actividad3.controlador.ProductoControlador;
import com.progweb.actividad3.model.Producto;
import com.progweb.actividad3.servicio.CategoriaServicio;
import com.progweb.actividad3.servicio.ProductoServicio;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.FormContentFilter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductoMetodosHttpTests {
    private final ProductoServicio servicio = mock(ProductoServicio.class);
    private final MockMvc navegador = MockMvcBuilders.standaloneSetup(
            new ProductoControlador(servicio, mock(CategoriaServicio.class)))
            .addFilters(new FormContentFilter()).build();

    @Test
    void actualizarConIdEnElBody() throws Exception {
        navegador.perform(put("/productos/actualizar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("id=5&nombre=Teclado&precio=350.00&categoria.id=2"))
                .andExpect(redirectedUrl("/productos"));
        verify(servicio).actualizarProducto(eq(5L), argThat(p ->
                p.getId().equals(5L) && p.getNombre().equals("Teclado")));
    }

    @Test
    void rechazarActualizacionSinId() throws Exception {
        navegador.perform(put("/productos/actualizar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("nombre=Teclado&precio=350.00&categoria.id=2"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(servicio);
    }

    @Test
    void conservarActualizacionConIdEnLaRuta() throws Exception {
        navegador.perform(put("/productos/actualizar/5")
                .param("nombre", "Teclado").param("precio", "350.00")
                .param("categoria.id", "2"))
                .andExpect(redirectedUrl("/productos"));
        verify(servicio).actualizarProducto(eq(5L), any(Producto.class));
    }
}
