package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest
{
    @TempDir
    Path directorioTemporal;

    private Pedido pedido;

    @BeforeEach
    void setUp( )
    {
        pedido = new Pedido( "Juan", "Calle 123" );
    }

    @Test
    void identificadoresSonDistintosYConsecutivos( )
    {
        Pedido siguiente = new Pedido( "Ana", "Calle 456" );
        assertNotEquals( pedido.getIdPedido( ), siguiente.getIdPedido( ) );
        assertEquals( pedido.getIdPedido( ) + 1, siguiente.getIdPedido( ) );
    }

    @Test
    void conservaElNombreDelCliente( )
    {
        assertEquals( "Juan", pedido.getNombreCliente( ) );
    }

    @Test
    void pedidoVacioTieneTotalCero( )
    {
        assertEquals( 0, pedido.getPrecioTotalPedido( ) );
        String factura = pedido.generarTextoFactura( );
        assertAll(
                ( ) -> assertTrue( factura.contains( "Subtotal $0\n" ) ),
                ( ) -> assertTrue( factura.contains( "IVA del 19 por ciento $0\n" ) ),
                ( ) -> assertTrue( factura.contains( "Total a pagar $0\n" ) ) );
    }

    @Test
    void totalSumaTodosLosProductosYElIVADel19PorCiento( )
    {
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 15000 ) );
        pedido.agregarProducto( new ProductoMenu( "papas", 5000 ) );
        assertEquals( 23800, pedido.getPrecioTotalPedido( ) );
    }

    @Test
    void incluyeProductosBasicosAjustadosYCombos( )
    {
        ProductoMenu hamburguesa = new ProductoMenu( "hamburguesa", 15000 );
        ProductoAjustado ajustado = new ProductoAjustado( hamburguesa );
        ajustado.agregarIngrediente( new Ingrediente( "queso", 2000 ) );
        Combo combo = new Combo( "acompañamientos", 0.1, new ArrayList<>( List.of(
                new ProductoMenu( "papas", 5000 ), new ProductoMenu( "gaseosa", 5000 ) ) ) );
        pedido.agregarProducto( hamburguesa );
        pedido.agregarProducto( ajustado );
        pedido.agregarProducto( combo );

        assertEquals( 48790, pedido.getPrecioTotalPedido( ) );
        String factura = pedido.generarTextoFactura( );
        assertAll(
                ( ) -> assertTrue( factura.contains( "hamburguesa" ) ),
                ( ) -> assertTrue( factura.contains( "Con queso" ) ),
                ( ) -> assertTrue( factura.contains( "Combo acompañamientos" ) ),
                ( ) -> assertTrue( factura.contains( "Subtotal $41000\n" ) ),
                ( ) -> assertTrue( factura.contains( "IVA del 19 por ciento $7790\n" ) ),
                ( ) -> assertTrue( factura.contains( "Total a pagar $48790\n" ) ) );
    }

    @Test
    void facturaIncluyeClienteDireccionProductosYTotalesCorrectos( )
    {
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 15000 ) );
        pedido.agregarProducto( new ProductoMenu( "papas", 5000 ) );
        String esperado = "La Esquina\nFactura del pedido " + pedido.getIdPedido( ) + "\n\n"
                + "Cliente Juan\nDirección Calle 123\n\nTu pedido\n\n"
                + "hamburguesa\nPrecio $15000\n\n"
                + "papas\nPrecio $5000\n\n"
                + "Subtotal $20000\nIVA del 19 por ciento $3800\nTotal a pagar $23800\n\nGracias por tu visita\n";
        assertEquals( esperado, pedido.generarTextoFactura( ) );
    }

    @Test
    void IVARetornaUnValorEnteroSinRedondearHaciaArriba( )
    {
        pedido.agregarProducto( new ProductoMenu( "producto", 1001 ) );
        assertEquals( 1191, pedido.getPrecioTotalPedido( ) );
        assertTrue( pedido.generarTextoFactura( ).contains( "IVA del 19 por ciento $190\n" ) );
    }

    @Test
    void guardaElContenidoCompletoDeLaFactura( ) throws Exception
    {
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 15000 ) );
        Path archivo = directorioTemporal.resolve( "factura.txt" );
        pedido.guardarFactura( archivo.toFile( ) );

        assertEquals( pedido.generarTextoFactura( ), Files.readString( archivo ) );
        assertTrue( Files.readString( archivo ).contains( "Total a pagar $17850\n" ) );
    }

    @Test
    void guardarReemplazaElContenidoAnteriorDelArchivo( ) throws Exception
    {
        Path archivo = Files.writeString( directorioTemporal.resolve( "factura.txt" ), "contenido anterior" );
        pedido.agregarProducto( new ProductoMenu( "papas", 5000 ) );
        pedido.guardarFactura( archivo.toFile( ) );

        assertEquals( pedido.generarTextoFactura( ), Files.readString( archivo ) );
        assertFalse( Files.readString( archivo ).contains( "contenido anterior" ) );
    }

    @Test
    void guardarEnUnDirectorioInexistenteLanzaExcepcion( )
    {
        Path archivo = directorioTemporal.resolve( "inexistente/factura.txt" );
        assertThrows( FileNotFoundException.class, ( ) -> pedido.guardarFactura( archivo.toFile( ) ) );
    }

    @Test
    void facturaNoIncluyeGuionesPuntosNiComas( )
    {
        ProductoAjustado ajustado = new ProductoAjustado( new ProductoMenu( "Hamburguesa de la casa", 18500 ) );
        ajustado.agregarIngrediente( new Ingrediente( "queso", 2200 ) );
        ajustado.eliminarIngrediente( new Ingrediente( "tomate", 900 ) );
        pedido.agregarProducto( ajustado );
        pedido.agregarProducto( new Combo( "Para compartir", 0.075,
                new ArrayList<>( List.of( new ProductoMenu( "Papas rústicas", 6500 ) ) ) ) );

        String factura = pedido.generarTextoFactura( );
        assertFalse( factura.matches( "(?s).*[-;.,].*" ) );
        assertTrue( factura.contains( "Con queso por $2200" ) );
        assertTrue( factura.contains( "Sin tomate" ) );
    }
}
