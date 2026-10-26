package uniandes.dpoo.hamburguesas.mundo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Pedido {
    private static final double IVA = 0.19;
    private static int numeroPedidos;

    private final int idPedido;
    private final String nombreCliente;
    private final String direccionCliente;
    private final ArrayList<Producto> productos;

    public Pedido(String nombreCliente, String direccionCliente) {
        idPedido = numeroPedidos++;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        productos = new ArrayList<>();
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void agregarProducto(Producto nuevoProducto) {
        productos.add(nuevoProducto);
    }

    private int getPrecioNetoPedido() {
        int subtotal = 0;
        for (Producto producto : productos) {
            subtotal += producto.getPrecio();
        }
        return subtotal;
    }

    private int getPrecioIVAPedido() {
        return (int) (getPrecioNetoPedido() * IVA);
    }

    public int getPrecioTotalPedido() {
        return getPrecioNetoPedido() + getPrecioIVAPedido();
    }

    public String generarTextoFactura() {
        StringBuilder factura = new StringBuilder();
        factura.append("La Esquina\nFactura del pedido ").append(idPedido).append("\n\n");
        factura.append("Cliente ").append(nombreCliente).append("\n");
        factura.append("Dirección ").append(direccionCliente).append("\n\nTu pedido\n\n");
        for (Producto producto : productos) {
            factura.append(producto.generarTextoFactura());
        }
        factura.append("Subtotal $").append(getPrecioNetoPedido()).append("\n");
        factura.append("IVA del 19 por ciento $").append(getPrecioIVAPedido()).append("\n");
        factura.append("Total a pagar $").append(getPrecioTotalPedido()).append("\n\n");
        factura.append("Gracias por tu visita\n");
        return factura.toString();
    }

    public void guardarFactura(File archivo) throws FileNotFoundException {
        try (PrintWriter salida = new PrintWriter(archivo)) {
            salida.print(generarTextoFactura());
        }
    }
}
