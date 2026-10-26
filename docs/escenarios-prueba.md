# Escenarios de prueba del Taller 5

Estos escenarios definen las acciones y los resultados esperados de las pruebas. Los valores esperados se calculan con las reglas del enunciado: las adiciones se cobran, eliminar ingredientes no reduce el precio, los combos aplican su descuento y el pedido suma el IVA del 19 %.

| Clase | Escenario y acciones | Resultado que se verifica |
| --- | --- | --- |
| ProductoMenu | Crear un producto con nombre y precio conocidos. | Los getters conservan ambos valores. |
| ProductoMenu | Generar la factura de un producto, incluso de precio cero. | Aparecen su nombre y su precio. |
| ProductoAjustado | Crear un producto sin ajustes. | Conserva el nombre y el precio base. |
| ProductoAjustado | Agregar uno, varios y el mismo ingrediente varias veces. | Se cobra cada adición y se conserva el precio del producto base. |
| ProductoAjustado | Eliminar ingredientes, incluyendo uno que también se agregó. | La eliminación no descuenta el precio base ni las adiciones. |
| ProductoAjustado | Facturar un producto con adiciones y eliminaciones. | Se identifican el producto, cada ajuste, sus costos y el precio final. |
| Combo | Crear un combo de varios productos con descuento. | El nombre y el precio corresponden a los datos proporcionados. |
| Combo | Probar descuentos de 0 %, 100 % y 9,5 %, y un combo vacío. | Se calculan los precios esperados; el resultado entero trunca la parte decimal. |
| Combo | Modificar la lista original después de construir el combo. | El combo conserva los productos con los que se construyó. |
| Combo | Generar la factura. | Contiene nombre, descuento y precio del combo. |
| Pedido | Crear dos pedidos consecutivos. | Sus identificadores son distintos y consecutivos. |
| Pedido | Crear un pedido vacío y agregar varios productos. | El total vacío es cero; los productos se suman y se agrega IVA del 19 %. |
| Pedido | Agregar productos básicos, ajustados y combos. | Todos participan en el total y aparecen en la factura. |
| Pedido | Generar factura con cliente y dirección conocidos. | Incluye identificación, cliente, dirección, productos, subtotal, IVA y total correctos. |
| Pedido | Facturar ajustes y un combo con descuento fraccionario. | Los ajustes aparecen como Con y Sin y el texto no usa guiones ni puntos ni comas. |
| Pedido | Guardar una factura en un directorio temporal y reemplazar un archivo existente. | El contenido persistido coincide con la factura completa del pedido. |
| Pedido | Guardar en una ruta cuyo directorio padre no existe. | Se lanza FileNotFoundException. |
| Restaurante | Crear un restaurante e iniciar un pedido. | Las colecciones comienzan vacías y el pedido conserva los datos del cliente. |
| Restaurante | Iniciar otro pedido mientras existe uno activo. | Se lanza YaHayUnPedidoEnCursoException y se conserva el pedido original. |
| Restaurante | Cerrar sin pedido activo. | Se lanza NoHayPedidoEnCursoException. |
| Restaurante | Cerrar uno o varios pedidos. | Las facturas se guardan con el ID, los pedidos pasan al histórico y se libera el pedido en curso. |
| Restaurante | Fallar al guardar una factura. | Se informa el error de archivo y el pedido continúa activo, sin añadirse al histórico. |
| Restaurante | Cargar archivos válidos, vacíos y con líneas vacías. | Se cargan los ingredientes, productos y combos correctos; las líneas vacías se ignoran. |
| Restaurante | Cargar los archivos reales de data. | Se integran los tres archivos y se obtienen los precios esperados de los combos. |
| Restaurante | Repetir un ingrediente, un producto o un combo en los archivos. | Se lanza la excepción de repetición correspondiente, indicando el nombre. |
| Restaurante | Referenciar en un combo un producto ausente del menú. | Se lanza ProductoFaltanteException, indicando el producto. |
| Restaurante | Usar un costo o un descuento no numérico. | Se lanza NumberFormatException. |
| Restaurante | Omitir cualquiera de los tres archivos. | Se lanza IOException. |

## Aislamiento y cobertura

Los archivos de prueba y las facturas se crean con `@TempDir`. El constructor adicional de Restaurante permite elegir el directorio de facturas; el constructor original conserva la ruta `./facturas/`. Las pruebas no sobrescriben las facturas de ejemplo ni dependen del orden de ejecución.

Objetivo: 100 % de cobertura en ProductoMenu, ProductoAjustado, Combo y Pedido, y más del 95 % en Restaurante. Además de ejecutar los métodos, las pruebas verifican resultados y excepciones concretos.
