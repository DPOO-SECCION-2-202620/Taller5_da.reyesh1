package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest
{
    private ProductoMenu base;
    private ProductoAjustado ajustado;
    private Ingrediente queso;

    @BeforeEach
    void setUp( )
    {
        base = new ProductoMenu( "hamburguesa", 15000 );
        ajustado = new ProductoAjustado( base );
        queso = new Ingrediente( "queso", 2000 );
    }

    @Test
    void sinAjustesConservaNombreYPrecioBase( )
    {
        assertAll(
                ( ) -> assertEquals( "hamburguesa", ajustado.getNombre( ) ),
                ( ) -> assertEquals( 15000, ajustado.getPrecio( ) ) );
    }

    @Test
    void cobraUnIngredienteAdicional( )
    {
        ajustado.agregarIngrediente( queso );
        assertEquals( 17000, ajustado.getPrecio( ) );
        assertEquals( 15000, base.getPrecio( ), "El ajuste no debe modificar el producto del menú" );
    }

    @Test
    void cobraVariosIngredientesAdicionales( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( new Ingrediente( "tocineta", 3000 ) );
        assertEquals( 20000, ajustado.getPrecio( ) );
    }

    @Test
    void cobraCadaRepeticionDelMismoIngrediente( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( queso );
        assertEquals( 19000, ajustado.getPrecio( ) );
    }

    @Test
    void eliminarIngredientesNoReduceElPrecioBase( )
    {
        ajustado.eliminarIngrediente( queso );
        ajustado.eliminarIngrediente( new Ingrediente( "tomate", 1000 ) );
        assertEquals( 15000, ajustado.getPrecio( ) );
    }

    @Test
    void eliminarElMismoIngredienteNoDescuentaSuAdicion( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( queso );
        assertEquals( 17000, ajustado.getPrecio( ), "Eliminar ingredientes no reduce el precio de las adiciones" );
    }

    @Test
    void eliminarNoDescuentaNingunaAdicionRepetida( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( queso );
        assertEquals( 19000, ajustado.getPrecio( ) );
    }

    @Test
    void eliminarUnObjetoConIgualNombreTampocoDescuentaLaAdicion( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( new Ingrediente( "queso", 2000 ) );
        assertEquals( 17000, ajustado.getPrecio( ) );
    }

    @Test
    void facturaDiscriminaBaseAdicionesEliminacionesYPrecioFinal( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( new Ingrediente( "tomate", 1000 ) );
        String esperado = "hamburguesa\nPrecio base $15000\n"
                + "Con queso por $2000\n"
                + "Con queso por $2000\n"
                + "Sin tomate\nValor del producto $19000\n\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ) );
    }

    @Test
    void facturaSinAjustesIncluyeElProductoYElPrecioFinal( )
    {
        assertEquals( "hamburguesa\nPrecio base $15000\nValor del producto $15000\n\n", ajustado.generarTextoFactura( ) );
    }
}
