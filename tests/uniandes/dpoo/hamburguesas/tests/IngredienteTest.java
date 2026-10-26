package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;

public class IngredienteTest
{
    private Ingrediente ingrediente1;
    private Ingrediente ingrediente2;
    private Ingrediente ingrediente3;
    private Ingrediente ingrediente4;

    @BeforeEach
    void setUp( ) throws Exception
    {
        ingrediente1 = new Ingrediente( "tomate", 1000 );
        ingrediente2 = new Ingrediente("queso", 2000);
        ingrediente3 = new Ingrediente("pan", 2000);
        ingrediente4 = new Ingrediente("carne", 6000);
        
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "tomate", ingrediente1.getNombre( ), "El nombre del ingrediente no es el esperado." );
    }

    @Test
    void testGetCostoAdicional( )
    {
        assertEquals( 1000, ingrediente1.getCostoAdicional( ), "El costo adicional del ingrediente no es el esperado." );
    }
	
}
	