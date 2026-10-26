package uniandes.dpoo.hamburguesas.mundo;

public class ProductoMenu implements Producto {
    private final String nombre;
    private final int precioBase;

    public ProductoMenu(String nombre, int precioBase) {
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getPrecio() {
        return precioBase;
    }

    @Override
    public String generarTextoFactura() {
        return nombre + "\nPrecio $" + precioBase + "\n\n";
    }
}
