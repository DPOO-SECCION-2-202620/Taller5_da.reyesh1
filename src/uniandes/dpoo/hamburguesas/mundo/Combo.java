package uniandes.dpoo.hamburguesas.mundo;

import java.util.ArrayList;

public class Combo implements Producto {
    private final ArrayList<ProductoMenu> itemsCombo;
    private final double descuento;
    private final String nombreCombo;

    public Combo(String nombre, double descuento, ArrayList<ProductoMenu> items) {
        nombreCombo = nombre;
        this.descuento = descuento;
        itemsCombo = new ArrayList<>(items);
    }

    @Override
    public String getNombre() {
        return nombreCombo;
    }

    private int getPrecioSinDescuento() {
        int total = 0;
        for (ProductoMenu producto : itemsCombo) {
            total += producto.getPrecio();
        }
        return total;
    }

    @Override
    public int getPrecio() {
        int precioNormal = getPrecioSinDescuento();
        return (int) (precioNormal - precioNormal * descuento);
    }

    @Override
    public String generarTextoFactura() {
        ArrayList<String> nombres = new ArrayList<>();
        for (ProductoMenu producto : itemsCombo) {
            nombres.add(producto.getNombre());
        }
        return "Combo " + nombreCombo + "\n"
                + "Incluye " + String.join(" y ", nombres) + "\n"
                + "Descuento $" + (getPrecioSinDescuento() - getPrecio()) + "\n"
                + "Valor del combo $" + getPrecio() + "\n\n";
    }
}
