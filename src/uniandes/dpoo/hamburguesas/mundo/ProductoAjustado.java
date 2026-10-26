package uniandes.dpoo.hamburguesas.mundo;

import java.util.ArrayList;

public class ProductoAjustado implements Producto {
    private final ProductoMenu productoBase;
    private final ArrayList<Ingrediente> agregados;
    private final ArrayList<Ingrediente> eliminados;

    public ProductoAjustado(ProductoMenu productoBase) {
        this.productoBase = productoBase;
        agregados = new ArrayList<>();
        eliminados = new ArrayList<>();
    }

    @Override
    public String getNombre() {
        return productoBase.getNombre();
    }

    @Override
    public int getPrecio() {
        int total = productoBase.getPrecio();
        for (Ingrediente ingrediente : agregados) {
            total += ingrediente.getCostoAdicional();
        }
        return total;
    }

    public void agregarIngrediente(Ingrediente ingrediente) {
        agregados.add(ingrediente);
    }

    public void eliminarIngrediente(Ingrediente ingrediente) {
        eliminados.add(ingrediente);
    }

    @Override
    public String generarTextoFactura() {
        StringBuilder detalle = new StringBuilder();
        detalle.append(productoBase.getNombre()).append("\n");
        detalle.append("Precio base $").append(productoBase.getPrecio()).append("\n");
        for (Ingrediente ingrediente : agregados) {
            detalle.append("Con ").append(ingrediente.getNombre())
                    .append(" por $").append(ingrediente.getCostoAdicional()).append("\n");
        }
        for (Ingrediente ingrediente : eliminados) {
            detalle.append("Sin ").append(ingrediente.getNombre()).append("\n");
        }
        detalle.append("Valor del producto $").append(getPrecio()).append("\n\n");
        return detalle.toString();
    }
}
