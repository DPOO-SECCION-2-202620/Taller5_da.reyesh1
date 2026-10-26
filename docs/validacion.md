# Resultados de validación

Validación realizada el 25 de octubre de 2026 con JUnit Jupiter 5.14.4, JaCoCo 0.8.15 y OpenJDK 26.0.2. Se recompilaron las fuentes con `javac --release 17 -encoding UTF-8` y se actualizaron los archivos de `bin/`.

## Pruebas

| Clase de pruebas | Ejecutadas | Fallidas |
| --- | ---: | ---: |
| ProductoMenuTest | 3 | 0 |
| ProductoAjustadoTest | 10 | 0 |
| ComboTest | 8 | 0 |
| PedidoTest | 10 | 0 |
| RestauranteTest | 20 | 0 |
| IngredienteTest | 2 | 0 |
| **Total** | **53** | **0** |

## Cobertura de las clases del modelo

| Clase | Líneas cubiertas | Instrucciones cubiertas | Ramas cubiertas | Métodos cubiertos |
| --- | ---: | ---: | ---: | ---: |
| ProductoMenu | 10/10 | 34/34 | No aplica | 4/4 |
| ProductoAjustado | 26/26 | 116/116 | 6/6 | 6/6 |
| Combo | 16/16 | 71/71 | 2/2 | 4/4 |
| Pedido | 35/35 | 149/149 | 4/4 | 9/9 |
| Restaurante | 97/97 | 389/389 | 38/38 | 13/13 |
| Ingrediente | 6/6 | 15/15 | No aplica | 3/3 |

Todas las clases exigidas alcanzan el 100 %. La interfaz Producto no contiene código ejecutable. El reporte se limita a las clases del modelo; no representa cobertura de la interfaz de consola.

- [Reporte HTML de JaCoCo](cobertura/index.html).
- [Datos XML de JaCoCo](cobertura.xml).

## Verificación de consola

Se verificaron la carga automática de datos, la visualización del menú, la apertura del pedido, los ajustes, el cierre, el archivo de factura y la consulta por identificador. En el caso de regresión se agregó y se eliminó queso mozzarella de una hamburguesa corral:

- Base: $14.000.
- Adición: $2.500.
- Eliminación: no descuenta.
- Neto: $16.500; IVA: $3.135; total: $19.635.

El flujo y las pruebas utilizaron archivos temporales y conservaron intactas las facturas de ejemplo.
