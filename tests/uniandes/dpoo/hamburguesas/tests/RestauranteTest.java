package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest
{
    @TempDir
    Path directorioTemporal;

    private Path carpetaFacturas;
    private Restaurante restaurante;

    @BeforeEach
    void setUp( ) throws IOException
    {
        carpetaFacturas = Files.createDirectory( directorioTemporal.resolve( "facturas" ) );
        restaurante = new Restaurante( carpetaFacturas.toFile( ) );
    }

    private File archivo( String nombre, String contenido ) throws IOException
    {
        return Files.writeString( directorioTemporal.resolve( nombre ), contenido ).toFile( );
    }

    private void cargar( String ingredientes, String menu, String combos ) throws Exception
    {
        restaurante.cargarInformacionRestaurante(
                archivo( "ingredientes.txt", ingredientes ),
                archivo( "menu.txt", menu ),
                archivo( "combos.txt", combos ) );
    }

    @Test
    void constructorOriginalCreaUnRestauranteVacio( )
    {
        Restaurante vacio = new Restaurante( );
        assertAll(
                ( ) -> assertNull( vacio.getPedidoEnCurso( ) ),
                ( ) -> assertTrue( vacio.getPedidos( ).isEmpty( ) ),
                ( ) -> assertTrue( vacio.getIngredientes( ).isEmpty( ) ),
                ( ) -> assertTrue( vacio.getMenuBase( ).isEmpty( ) ),
                ( ) -> assertTrue( vacio.getMenuCombos( ).isEmpty( ) ) );
    }

    @Test
    void iniciarPedidoConservaLosDatosDelCliente( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Calle 123" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        assertEquals( "Juan", pedido.getNombreCliente( ) );
        assertTrue( pedido.generarTextoFactura( ).contains( "Dirección Calle 123\n" ) );
        assertTrue( restaurante.getPedidos( ).isEmpty( ) );
    }

    @Test
    void iniciarUnSegundoPedidoLanzaExcepcionYConservaElOriginal( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Calle 123" );
        Pedido original = restaurante.getPedidoEnCurso( );
        YaHayUnPedidoEnCursoException error = assertThrows( YaHayUnPedidoEnCursoException.class,
                ( ) -> restaurante.iniciarPedido( "Ana", "Calle 456" ) );

        assertSame( original, restaurante.getPedidoEnCurso( ) );
        assertTrue( error.getMessage( ).contains( "Juan" ) );
        assertTrue( error.getMessage( ).contains( "Ana" ) );
    }

    @Test
    void cerrarSinPedidoLanzaExcepcion( )
    {
        NoHayPedidoEnCursoException error = assertThrows( NoHayPedidoEnCursoException.class,
                ( ) -> restaurante.cerrarYGuardarPedido( ) );
        assertEquals( "Actualmente no hay un pedido en curso", error.getMessage( ) );
        assertTrue( restaurante.getPedidos( ).isEmpty( ) );
    }

    @Test
    void cerrarGuardaFacturaCompletaConIdYAgregaAlHistorico( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Calle 123" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 15000 ) );
        pedido.agregarProducto( new ProductoMenu( "papas", 5000 ) );
        restaurante.cerrarYGuardarPedido( );

        Path factura = carpetaFacturas.resolve( "factura_" + pedido.getIdPedido( ) + ".txt" );
        assertAll(
                ( ) -> assertNull( restaurante.getPedidoEnCurso( ) ),
                ( ) -> assertEquals( List.of( pedido ), restaurante.getPedidos( ) ),
                ( ) -> assertEquals( pedido.generarTextoFactura( ), Files.readString( factura ) ),
                ( ) -> assertTrue( Files.readString( factura ).contains( "Total a pagar $23800\n" ) ) );
    }

    @Test
    void variosPedidosConservanHistoricoYFacturasSeparadas( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Calle 123" );
        Pedido primero = restaurante.getPedidoEnCurso( );
        restaurante.cerrarYGuardarPedido( );
        restaurante.iniciarPedido( "Ana", "Calle 456" );
        Pedido segundo = restaurante.getPedidoEnCurso( );
        segundo.agregarProducto( new ProductoMenu( "papas", 5000 ) );
        restaurante.cerrarYGuardarPedido( );

        assertEquals( List.of( primero, segundo ), restaurante.getPedidos( ) );
        assertNotEquals( primero.getIdPedido( ), segundo.getIdPedido( ) );
        assertEquals( primero.generarTextoFactura( ), Files.readString(
                carpetaFacturas.resolve( "factura_" + primero.getIdPedido( ) + ".txt" ) ) );
        assertEquals( segundo.generarTextoFactura( ), Files.readString(
                carpetaFacturas.resolve( "factura_" + segundo.getIdPedido( ) + ".txt" ) ) );
    }

    @Test
    void falloAlGuardarConservaElPedidoYPermiteReintentar( ) throws Exception
    {
        Path carpetaInexistente = directorioTemporal.resolve( "pendiente" );
        Restaurante otro = new Restaurante( carpetaInexistente.toFile( ) );
        otro.iniciarPedido( "Juan", "Calle 123" );
        Pedido pedido = otro.getPedidoEnCurso( );

        assertThrows( FileNotFoundException.class, ( ) -> otro.cerrarYGuardarPedido( ) );
        assertSame( pedido, otro.getPedidoEnCurso( ) );
        assertTrue( otro.getPedidos( ).isEmpty( ) );

        Files.createDirectory( carpetaInexistente );
        otro.cerrarYGuardarPedido( );
        assertNull( otro.getPedidoEnCurso( ) );
        assertEquals( List.of( pedido ), otro.getPedidos( ) );
        assertEquals( pedido.generarTextoFactura( ), Files.readString(
                carpetaInexistente.resolve( "factura_" + pedido.getIdPedido( ) + ".txt" ) ) );
    }

    @Test
    void cargaArchivosValidosIgnorandoLineasVacias( ) throws Exception
    {
        cargar( "queso;2000\n\ntomate;1000\n",
                "hamburguesa;15000\n\npapas;5000\ngaseosa;5000\n",
                "familiar;10%;hamburguesa;papas;gaseosa\n\npequeño;0%;papas;gaseosa\n" );

        assertAll(
                ( ) -> assertEquals( 2, restaurante.getIngredientes( ).size( ) ),
                ( ) -> assertEquals( "queso", restaurante.getIngredientes( ).get( 0 ).getNombre( ) ),
                ( ) -> assertEquals( 1000, restaurante.getIngredientes( ).get( 1 ).getCostoAdicional( ) ),
                ( ) -> assertEquals( 3, restaurante.getMenuBase( ).size( ) ),
                ( ) -> assertEquals( "hamburguesa", restaurante.getMenuBase( ).get( 0 ).getNombre( ) ),
                ( ) -> assertEquals( 5000, restaurante.getMenuBase( ).get( 2 ).getPrecio( ) ),
                ( ) -> assertEquals( 2, restaurante.getMenuCombos( ).size( ) ),
                ( ) -> assertEquals( "familiar", restaurante.getMenuCombos( ).get( 0 ).getNombre( ) ),
                ( ) -> assertEquals( 22500, restaurante.getMenuCombos( ).get( 0 ).getPrecio( ) ),
                ( ) -> assertEquals( 10000, restaurante.getMenuCombos( ).get( 1 ).getPrecio( ) ) );
    }

    @Test
    void cargaLosArchivosRealesDelRestaurante( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( new File( "data/ingredientes.txt" ),
                new File( "data/menu.txt" ), new File( "data/combos.txt" ) );

        assertAll(
                ( ) -> assertEquals( 10, restaurante.getIngredientes( ).size( ) ),
                ( ) -> assertEquals( "Queso cheddar", restaurante.getIngredientes( ).get( 0 ).getNombre( ) ),
                ( ) -> assertEquals( 2200, restaurante.getIngredientes( ).get( 0 ).getCostoAdicional( ) ),
                ( ) -> assertEquals( 12, restaurante.getMenuBase( ).size( ) ),
                ( ) -> assertEquals( "Hamburguesa de la casa", restaurante.getMenuBase( ).get( 0 ).getNombre( ) ),
                ( ) -> assertEquals( 18500, restaurante.getMenuBase( ).get( 0 ).getPrecio( ) ),
                ( ) -> assertEquals( 5, restaurante.getMenuCombos( ).size( ) ),
                ( ) -> assertEquals( "Almuerzo de la casa", restaurante.getMenuCombos( ).get( 0 ).getNombre( ) ),
                ( ) -> assertEquals( 27280, restaurante.getMenuCombos( ).get( 0 ).getPrecio( ) ),
                ( ) -> assertEquals( 40940, restaurante.getMenuCombos( ).get( 1 ).getPrecio( ) ),
                ( ) -> assertEquals( 28800, restaurante.getMenuCombos( ).get( 2 ).getPrecio( ) ),
                ( ) -> assertEquals( 29600, restaurante.getMenuCombos( ).get( 3 ).getPrecio( ) ),
                ( ) -> assertEquals( 33725, restaurante.getMenuCombos( ).get( 4 ).getPrecio( ) ) );
    }

    @Test
    void cargaArchivosVaciosSinCrearElementos( ) throws Exception
    {
        cargar( "", "", "" );
        assertTrue( restaurante.getIngredientes( ).isEmpty( ) );
        assertTrue( restaurante.getMenuBase( ).isEmpty( ) );
        assertTrue( restaurante.getMenuCombos( ).isEmpty( ) );
    }

    @Test
    void ingredienteRepetidoLanzaExcepcion( )
    {
        IngredienteRepetidoException error = assertThrows( IngredienteRepetidoException.class,
                ( ) -> cargar( "queso;2000\nqueso;3000\n", "", "" ) );
        assertTrue( error.getMessage( ).contains( "queso" ) );
    }

    @Test
    void productoRepetidoLanzaExcepcion( )
    {
        ProductoRepetidoException error = assertThrows( ProductoRepetidoException.class,
                ( ) -> cargar( "", "hamburguesa;15000\nhamburguesa;16000\n", "" ) );
        assertTrue( error.getMessage( ).contains( "hamburguesa" ) );
    }

    @Test
    void comboRepetidoLanzaExcepcion( )
    {
        ProductoRepetidoException error = assertThrows( ProductoRepetidoException.class,
                ( ) -> cargar( "", "hamburguesa;15000\n",
                        "familiar;10%;hamburguesa\nfamiliar;5%;hamburguesa\n" ) );
        assertTrue( error.getMessage( ).contains( "familiar" ) );
    }

    @Test
    void productoAusenteDeUnComboLanzaExcepcion( )
    {
        ProductoFaltanteException error = assertThrows( ProductoFaltanteException.class,
                ( ) -> cargar( "", "hamburguesa;15000\n", "familiar;10%;hamburguesa;papas\n" ) );
        assertTrue( error.getMessage( ).contains( "papas" ) );
        assertTrue( restaurante.getMenuCombos( ).isEmpty( ) );
    }

    @Test
    void costoDeIngredienteNoNumericoLanzaExcepcion( )
    {
        assertThrows( NumberFormatException.class, ( ) -> cargar( "queso;abc\n", "", "" ) );
    }

    @Test
    void precioDeProductoNoNumericoLanzaExcepcion( )
    {
        assertThrows( NumberFormatException.class, ( ) -> cargar( "", "hamburguesa;abc\n", "" ) );
    }

    @Test
    void descuentoNoNumericoLanzaExcepcion( )
    {
        assertThrows( NumberFormatException.class,
                ( ) -> cargar( "", "hamburguesa;15000\n", "familiar;abc%;hamburguesa\n" ) );
    }

    @Test
    void archivoDeIngredientesInexistenteLanzaIOException( ) throws IOException
    {
        File menu = archivo( "menu.txt", "" );
        File combos = archivo( "combos.txt", "" );
        assertThrows( IOException.class, ( ) -> restaurante.cargarInformacionRestaurante(
                directorioTemporal.resolve( "ausente.txt" ).toFile( ), menu, combos ) );
    }

    @Test
    void archivoDeMenuInexistenteLanzaIOException( ) throws IOException
    {
        File ingredientes = archivo( "ingredientes.txt", "" );
        File combos = archivo( "combos.txt", "" );
        assertThrows( IOException.class, ( ) -> restaurante.cargarInformacionRestaurante(
                ingredientes, directorioTemporal.resolve( "ausente.txt" ).toFile( ), combos ) );
    }

    @Test
    void archivoDeCombosInexistenteLanzaIOException( ) throws IOException
    {
        File ingredientes = archivo( "ingredientes.txt", "" );
        File menu = archivo( "menu.txt", "" );
        assertThrows( IOException.class, ( ) -> restaurante.cargarInformacionRestaurante(
                ingredientes, menu, directorioTemporal.resolve( "ausente.txt" ).toFile( ) ) );
    }
}
