package uniandes.dpoo.hamburguesas.mundo;

public class Ingrediente {
    private final String nombre;
    private final int costoAdicional;

    public Ingrediente(String nombre, int costoAdicional) {
        this.nombre = nombre;
        this.costoAdicional = costoAdicional;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCostoAdicional() {
        return costoAdicional;
    }
}
