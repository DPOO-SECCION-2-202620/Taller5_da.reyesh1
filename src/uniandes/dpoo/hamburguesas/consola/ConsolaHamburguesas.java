package uniandes.dpoo.hamburguesas.consola;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import uniandes.dpoo.hamburguesas.excepciones.HamburguesaException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class ConsolaHamburguesas {
    private static final Restaurante restaurante = new Restaurante();
    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bienvenido a La Esquina");
        try {
            restaurante.cargarInformacionRestaurante(new File("data/ingredientes.txt"),
                    new File("data/menu.txt"), new File("data/combos.txt"));
        } catch (HamburguesaException | IOException | NumberFormatException error) {
            System.out.println("No pudimos abrir el menú " + error.getMessage());
            entrada.close();
            return;
        }

        boolean continuar = true;
        while (continuar) {
            int opcion = mostrarOpciones();
            try {
                switch (opcion) {
                    case 1 -> mostrarCarta();
                    case 2 -> abrirPedido();
                    case 3 -> agregarProducto();
                    case 4 -> finalizarPedido();
                    case 5 -> buscarPedido();
                    case 6 -> continuar = false;
                    default -> System.out.println("Elige una opción entre 1 y 6");
                }
            } catch (NumberFormatException error) {
                System.out.println("Escribe el número de la opción que quieres elegir");
            } catch (HamburguesaException | IOException error) {
                System.out.println(error.getMessage());
            }
        }
        System.out.println("Gracias por visitar La Esquina");
        entrada.close();
    }

    private static int mostrarOpciones() {
        System.out.println("\nQué quieres hacer\n");
        System.out.println("1 Ver la carta");
        System.out.println("2 Abrir un pedido");
        System.out.println("3 Elegir algo para comer");
        System.out.println("4 Terminar y guardar la cuenta");
        System.out.println("5 Buscar un pedido");
        System.out.println("6 Salir");
        if (!entrada.hasNextLine()) {
            return 6;
        }
        try {
            return Integer.parseInt(entrada.nextLine());
        } catch (NumberFormatException error) {
            return 0;
        }
    }

    private static void mostrarCarta() {
        int numero = 1;
        System.out.println("\nNuestra carta\n");
        for (ProductoMenu producto : restaurante.getMenuBase()) {
            System.out.println(numero++ + " " + producto.getNombre() + " $" + producto.getPrecio());
        }
        System.out.println("\nNuestros combos\n");
        for (Combo combo : restaurante.getMenuCombos()) {
            System.out.println(numero++ + " " + combo.getNombre() + " $" + combo.getPrecio());
        }
        System.out.println();
    }

    private static void abrirPedido() throws YaHayUnPedidoEnCursoException {
        if (restaurante.getPedidoEnCurso() != null) {
            System.out.println("Todavía hay un pedido abierto");
            return;
        }
        System.out.println("A nombre de quién dejamos el pedido");
        String nombre = entrada.nextLine();
        System.out.println("Cuál es la dirección de entrega");
        String direccion = entrada.nextLine();
        restaurante.iniciarPedido(nombre, direccion);
        System.out.println("Tu pedido quedó abierto");
    }

    private static void agregarProducto() {
        Pedido pedido = restaurante.getPedidoEnCurso();
        if (pedido == null) {
            System.out.println("Abre un pedido antes de elegir los productos");
            return;
        }
        mostrarCarta();
        System.out.println("Escribe el número del producto o combo");
        int seleccion = Integer.parseInt(entrada.nextLine());
        int productos = restaurante.getMenuBase().size();
        if (seleccion >= 1 && seleccion <= productos) {
            ProductoMenu producto = restaurante.getMenuBase().get(seleccion - 1);
            System.out.println("Quieres cambiar algún ingrediente s o n");
            if (entrada.nextLine().equalsIgnoreCase("s")) {
                pedido.agregarProducto(ajustarProducto(producto));
            } else {
                pedido.agregarProducto(producto);
            }
        } else if (seleccion > productos && seleccion <= productos + restaurante.getMenuCombos().size()) {
            pedido.agregarProducto(restaurante.getMenuCombos().get(seleccion - productos - 1));
        } else {
            System.out.println("Ese número no aparece en la carta");
            return;
        }
        System.out.println("Lo agregamos a tu pedido");
    }

    private static ProductoAjustado ajustarProducto(ProductoMenu producto) {
        ProductoAjustado ajustado = new ProductoAjustado(producto);
        while (true) {
            System.out.println("\n1 Añadir un ingrediente\n2 Quitar un ingrediente\n3 Dejarlo así");
            int opcion = Integer.parseInt(entrada.nextLine());
            if (opcion == 3) {
                return ajustado;
            }
            if (opcion != 1 && opcion != 2) {
                System.out.println("Elige una opción entre 1 y 3");
                continue;
            }
            int numero = 1;
            for (Ingrediente ingrediente : restaurante.getIngredientes()) {
                System.out.println(numero++ + " " + ingrediente.getNombre() + " $" + ingrediente.getCostoAdicional());
            }
            System.out.println("Cuál ingrediente quieres elegir");
            int seleccion = Integer.parseInt(entrada.nextLine());
            if (seleccion < 1 || seleccion > restaurante.getIngredientes().size()) {
                System.out.println("Ese ingrediente no aparece en la lista");
                continue;
            }
            Ingrediente ingrediente = restaurante.getIngredientes().get(seleccion - 1);
            if (opcion == 1) {
                ajustado.agregarIngrediente(ingrediente);
                System.out.println("Lo añadimos");
            } else {
                ajustado.eliminarIngrediente(ingrediente);
                System.out.println("Lo preparamos sin ese ingrediente");
            }
        }
    }

    private static void finalizarPedido() throws NoHayPedidoEnCursoException, IOException {
        Pedido pedido = restaurante.getPedidoEnCurso();
        if (pedido == null) {
            System.out.println("No hay una cuenta pendiente");
            return;
        }
        restaurante.cerrarYGuardarPedido();
        System.out.println(pedido.generarTextoFactura());
        System.out.println("Guardamos la factura de tu pedido");
    }

    private static void buscarPedido() {
        System.out.println("Qué número de pedido quieres consultar");
        int numero = Integer.parseInt(entrada.nextLine());
        Pedido actual = restaurante.getPedidoEnCurso();
        if (actual != null && actual.getIdPedido() == numero) {
            System.out.println(actual.generarTextoFactura());
            return;
        }
        for (Pedido pedido : restaurante.getPedidos()) {
            if (pedido.getIdPedido() == numero) {
                System.out.println(pedido.generarTextoFactura());
                return;
            }
        }
        System.out.println("No encontramos un pedido con ese número");
    }
}
