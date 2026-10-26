
package uniandes.dpoo.hamburguesas.consola;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

import uniandes.dpoo.hamburguesas.excepciones.HamburguesaException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class ConsolaHamburguesas
{
    private static Restaurante restaurante;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args)
    {
        restaurante = new Restaurante();

        System.out.println("Bienvenido al restaurante\n");

        try
        {
            cargarDatos();
            System.out.println("Datos cargados correctamente.");
        }
        catch (HamburguesaException | IOException e)
        {
            System.out.println("Error al cargar los datos: " + e.getMessage());
            scanner.close();
            return;
        }

        boolean continuar = true;

        while (continuar)
        {
            int opcion = mostrarMenu(
                "MENU PRINCIPAL",
                new String[] {
                    "Mostrar menu del restaurante",
                    "Iniciar nuevo pedido",
                    "Agregar elemento al pedido",
                    "Cerrar pedido y guardar factura",
                    "Consultar pedido por ID",
                    "Salir"
                }
            );

            try
            {
                if (opcion == 1)
                {
                    consultarMenu();
                }
                else if (opcion == 2)
                {
                    iniciarPedido();
                }
                else if (opcion == 3)
                {
                    agregarElemento();
                }
                else if (opcion == 4)
                {
                    cerrarPedido();
                }
                else if (opcion == 5)
                {
                    consultarPedido();
                }
                else if (opcion == 6)
                {
                    continuar = false;
                }
                else
                {
                    System.out.println("Opcion no valida.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Debes ingresar un numero valido.");
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Aplicacion terminada.");
        scanner.close();
    }

    private static int mostrarMenu(String titulo, String[] opciones)
    {
        System.out.println("\n--- " + titulo + " ---");

        for (int i = 0; i < opciones.length; i++)
        {
            System.out.println((i + 1) + ". " + opciones[i]);
        }

        System.out.print("Seleccione una opcion: ");
        System.out.print("\n");

        try
        {
            int opcion = Integer.parseInt(scanner.nextLine());
            return opcion;
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    private static void cargarDatos()
        throws NumberFormatException, HamburguesaException, IOException
    {
        File ingredientes = new File("data/ingredientes.txt");
        File menu = new File("data/menu.txt");
        File combos = new File("data/combos.txt");

        restaurante.cargarInformacionRestaurante(
            ingredientes, menu, combos
        );
    }

    private static void consultarMenu()
    {
        int w = 1;
        System.out.println("Mostrando Productos");
        for (ProductoMenu i : restaurante.getMenuBase())
        {
            System.out.println(w + ". ");
            System.out.println(i.getNombre() + "\n");
            System.out.println("Precio: " + i.getPrecio() + "\n");
            System.out.println("-".repeat(60));
            w += 1;
        }
        
        System.out.println("Mostrando Combos");
        for (Combo i : restaurante.getMenuCombos())
        {
            System.out.println(w + ". ");
            System.out.println(i.getNombre() + "\n");
            System.out.println("Precio: " + i.getPrecio() + "\n");
            System.out.println("-".repeat(60));
            w += 1;
        }
    }

    private static void iniciarPedido() throws Exception
    {
        if (restaurante.getPedidoEnCurso() != null)
        {
            System.out.println("Ya hay un pedido en curso.");
            return;
        }

        System.out.println("Nombre del cliente:");
        String nombre = scanner.nextLine();

        System.out.println("Direccion:");
        String direccion = scanner.nextLine();

        restaurante.iniciarPedido(nombre, direccion);

        System.out.println("Pedido iniciado correctamente.");
    }

    private static void agregarElemento()
    {
        if (restaurante.getPedidoEnCurso() == null)
        {
            System.out.println("Primero debes iniciar un pedido.");
            return;
        }

        consultarMenu();

        System.out.println("Selecciona el numero del producto:");
        int opcion = Integer.parseInt(scanner.nextLine());

        int cantidad = restaurante.getMenuBase().size();

        if (opcion <= cantidad && opcion >= 1)
        {
            ProductoMenu producto =
                restaurante.getMenuBase().get(opcion - 1);

            System.out.println("Quieres modificar el producto? (s/n)");
            String respuesta = scanner.nextLine();

            if (respuesta.equalsIgnoreCase("s"))
            {
                ProductoAjustado ajustado = ajustarProducto(producto);

                restaurante.getPedidoEnCurso().agregarProducto(ajustado);
            }
            else
            {
                restaurante.getPedidoEnCurso().agregarProducto(producto);
            }

            System.out.println("Producto agregado al pedido.");
        }
        else if (opcion > cantidad &&
                 opcion <= cantidad + restaurante.getMenuCombos().size())
        {
            Combo combo = restaurante.getMenuCombos().get(opcion - cantidad - 1);

            restaurante.getPedidoEnCurso().agregarProducto(combo);

            System.out.println("Combo agregado al pedido.");
        }
        else
        {
            System.out.println("Opcion invalida.");
        }
    }

    private static ProductoAjustado ajustarProducto(ProductoMenu producto)
    {
        ProductoAjustado ajustado = new ProductoAjustado(producto);

        boolean continuar = true;

        while (continuar)
        {
            System.out.println("\n1. Agregar ingrediente");
            System.out.println("2. Eliminar ingrediente");
            System.out.println("3. Terminar");

            int opcion = Integer.parseInt(scanner.nextLine());

            if (opcion == 3)
            {
                continuar = false;
            }
            else if (opcion == 1 || opcion == 2)
            {
                int numero = 1;

                for (Ingrediente i : restaurante.getIngredientes())
                {
                    System.out.println(numero + ". " + i.getNombre()+ " - $" + i.getCostoAdicional());
                    numero++;
                }

                System.out.println("Selecciona un ingrediente:");
                int seleccion = Integer.parseInt(scanner.nextLine());

                if (seleccion >= 1 &&
                    seleccion <= restaurante.getIngredientes().size())
                {
                    Ingrediente ingrediente =
                        restaurante.getIngredientes().get(seleccion - 1);

                    if (opcion == 1)
                    {
                        ajustado.agregarIngrediente(ingrediente);
                        System.out.println("Ingrediente agregado.");
                    }
                    else
                    {
                        ajustado.eliminarIngrediente(ingrediente);
                        System.out.println("Ingrediente eliminado.");
                    }
                }
                else
                {
                    System.out.println("Ingrediente no valido.");
                }
            }
            else
            {
                System.out.println("Opcion no valida.");
            }
        }

        return ajustado;
    }

    private static void cerrarPedido() throws IOException, NoHayPedidoEnCursoException
    {
        Pedido pedido = restaurante.getPedidoEnCurso();

        if (pedido == null)
        {
            System.out.println("No hay ningun pedido en curso.");
            return;
        }

        System.out.println(pedido.generarTextoFactura());

        restaurante.cerrarYGuardarPedido();

        System.out.println("Factura guardada correctamente.");
    }

    private static void consultarPedido()
    {
        System.out.println("Ingresa el ID del pedido:");
        int id = Integer.parseInt(scanner.nextLine());

        if (restaurante.getPedidoEnCurso() != null &&
            restaurante.getPedidoEnCurso().getIdPedido() == id)
        {
            System.out.println(restaurante.getPedidoEnCurso().generarTextoFactura());
            return;
        }

        for (Pedido i : restaurante.getPedidos())
        {
            if (i.getIdPedido() == id)
            {
                System.out.println(i.generarTextoFactura());
                return;
            }
        }

        System.out.println("No se encontro un pedido con ese ID.");
    }
}
