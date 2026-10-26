package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest
{
    private ArrayList<ProductoMenu> productos;

    @BeforeEach
    void setUp( )
    {
        productos = new ArrayList<>( List.of(
                new ProductoMenu( "hamburguesa", 15000 ),
                new ProductoMenu( "papas", 5000 ),
                new ProductoMenu( "gaseosa", 5000 ) ) );
    }

    @Test
    void conservaElNombre( )
    {
        assertEquals( "combo familiar", new Combo( "combo familiar", 0.1, productos ).getNombre( ) );
    }

    @Test
    void aplicaDescuentoALaSumaDeProductos( )
    {
        assertEquals( 22500, new Combo( "combo", 0.1, productos ).getPrecio( ) );
    }

    @Test
    void sinDescuentoCobraLaSumaCompleta( )
    {
        assertEquals( 25000, new Combo( "combo", 0, productos ).getPrecio( ) );
    }

    @Test
    void descuentoCompletoProducePrecioCero( )
    {
        assertEquals( 0, new Combo( "combo", 1, productos ).getPrecio( ) );
    }

    @Test
    void comboVacioCuestaCero( )
    {
        assertEquals( 0, new Combo( "vacío", 0.1, new ArrayList<>( ) ).getPrecio( ) );
    }

    @Test
    void calculaDescuentoFraccionarioYRetornaPrecioEntero( )
    {
        ArrayList<ProductoMenu> items = new ArrayList<>( List.of(
                new ProductoMenu( "especial", 24000 ),
                new ProductoMenu( "papas medianas", 5500 ),
                new ProductoMenu( "gaseosa", 5000 ) ) );
        assertEquals( 31222, new Combo( "especial", 0.095, items ).getPrecio( ) );
    }

    @Test
    void conservaUnaCopiaDeLaListaOriginal( )
    {
        Combo combo = new Combo( "combo", 0.1, productos );
        productos.clear( );
        assertEquals( 22500, combo.getPrecio( ) );
    }

    @Test
    void facturaIncluyeNombreDescuentoYPrecio( )
    {
        Combo combo = new Combo( "familiar", 0.1, productos );
        assertEquals( "Combo familiar\nIncluye hamburguesa y papas y gaseosa\nDescuento $2500\nValor del combo $22500\n\n", combo.generarTextoFactura( ) );
    }
}
