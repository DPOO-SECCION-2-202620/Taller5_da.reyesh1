# Validación

Se ejecutaron 54 pruebas con JUnit 5 y todas pasaron. Las fuentes se compilaron con Java 17 como versión de destino.

ProductoMenuTest tiene 3 pruebas, ProductoAjustadoTest 10, ComboTest 8, PedidoTest 11, RestauranteTest 20 e IngredienteTest 2.

## Cobertura

| Clase | Líneas | Instrucciones | Ramas | Métodos |
| --- | ---: | ---: | ---: | ---: |
| ProductoMenu | 7/7 | 21/21 | No aplica | 4/4 |
| ProductoAjustado | 27/27 | 136/136 | 6/6 | 6/6 |
| Combo | 21/21 | 87/87 | 4/4 | 5/5 |
| Pedido | 32/32 | 159/159 | 4/4 | 9/9 |
| Restaurante | 97/97 | 389/389 | 38/38 | 13/13 |
| Ingrediente | 6/6 | 15/15 | No aplica | 3/3 |

Las cinco clases exigidas por el taller conservan el 100 % de cobertura. El reporte corresponde al modelo de la aplicación.

[Reporte HTML](cobertura/index.html)

[Reporte XML](cobertura.xml)

## Factura de ejemplo

La factura se generó desde la consola con el catálogo nuevo. Camila Rojas pidió una hamburguesa de la casa con queso cheddar y sin tomate, unas papas rústicas y una limonada natural.

El subtotal es $33200, el IVA es $6308 y el total es $39508. La factura usa saltos de línea y palabras para describir los ajustes, sin guiones ni signos de puntuación como separadores.

También se verificaron pedidos con combos, la consulta por número de pedido, las opciones inválidas y el cierre de la entrada de consola.
