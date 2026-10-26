package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;

public class IngredienteTest {
    @Test
    void conservaElNombreDelIngrediente() {
        Ingrediente ingrediente = new Ingrediente("Queso cheddar", 2200);
        assertEquals("Queso cheddar", ingrediente.getNombre());
    }

    @Test
    void conservaElCostoDeLaAdicion() {
        Ingrediente ingrediente = new Ingrediente("Tocineta ahumada", 3500);
        assertEquals(3500, ingrediente.getCostoAdicional());
    }
}
