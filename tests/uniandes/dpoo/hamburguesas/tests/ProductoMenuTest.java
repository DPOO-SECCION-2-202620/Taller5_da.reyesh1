package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest
{
    @Test
    void conservaNombreYPrecio( )
    {
        ProductoMenu producto = new ProductoMenu( "hamburguesa", 15000 );
        assertAll(
                ( ) -> assertEquals( "hamburguesa", producto.getNombre( ) ),
                ( ) -> assertEquals( 15000, producto.getPrecio( ) ) );
    }

    @Test
    void facturaIncluyeNombreYPrecio( )
    {
        ProductoMenu producto = new ProductoMenu( "papas", 5000 );
        assertEquals( "papas\n            5000\n", producto.generarTextoFactura( ) );
    }

    @Test
    void permiteUnProductoDePrecioCero( )
    {
        ProductoMenu producto = new ProductoMenu( "cortesía", 0 );
        assertEquals( 0, producto.getPrecio( ) );
        assertEquals( "cortesía\n            0\n", producto.generarTextoFactura( ) );
    }
}
