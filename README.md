# Taller 5 — Hamburguesas

Aplicación de consola y pruebas unitarias y de integración con JUnit 5.

## Correcciones

- Eliminar un ingrediente no descuenta el precio base ni las adiciones, incluso cuando se agrega y se elimina el mismo ingrediente.
- Las pruebas verifican precios, contenido completo de facturas, archivos guardados, identificadores consecutivos, carga de datos y excepciones.
- Las facturas y los archivos de prueba se crean en directorios temporales mediante `@TempDir`.
- `Restaurante(File carpetaFacturas)` permite seleccionar un directorio existente para las facturas. `Restaurante()` conserva la ruta habitual `./facturas/`.

Los escenarios están descritos en [docs/escenarios-prueba.md](docs/escenarios-prueba.md).

## Ejecutar

Usar Java 17 o superior y ejecutar desde la raíz del proyecto para que se encuentren `data/` y `facturas/`:

```sh
java -cp bin uniandes.dpoo.hamburguesas.consola.ConsolaHamburguesas
```

Para trabajar en Eclipse, configurar `src` y `tests` como carpetas de código fuente, añadir la biblioteca JUnit 5 al Build Path y ejecutar el paquete `uniandes.dpoo.hamburguesas.tests` con **Run As → JUnit Test**. Para medir cobertura con EclEmma, usar **Coverage As → JUnit Test** sobre el mismo paquete.

## Validación

Las 53 pruebas pasan. Las cinco clases exigidas alcanzan el 100 % de cobertura de líneas, instrucciones y métodos, y todas sus ramas existentes están cubiertas. Se verificó también el flujo de consola con una adición y una eliminación del mismo ingrediente.

Consultar los [resultados detallados](docs/validacion.md) y el [reporte HTML de JaCoCo](docs/cobertura/index.html).
# Taller5_da.reyesh1
