# Epoch

La clase `Epoch` proporciona una forma de acceder y iterar a través de líneas de tiempo almacenadas en un archivo.

## Constructores

### `Epoch(File file) throws IOException`

Este constructor permite crear una nueva instancia de `Epoch` dado un archivo. La modalidad se establece en `Mode.Memory`.

### `Epoch(File file, Mode mode) throws IOException`

Este constructor permite crear una nueva instancia de `Epoch` dado un archivo y una modalidad. La modalidad puede ser `Mode.Memory` o `Mode.Disk`.

## Métodos

### `get(long id)`

Este método permite obtener una línea de tiempo específica a partir de su identificador. Devuelve una instancia de `Timeline`.

### `contains(long id)`

Este método permite verificar si una línea de tiempo específica con un identificador dado está presente en el índice de la cadena. Devuelve `true` si está presente y `false` en caso contrario.

### `iterator()`

Este método permite obtener un iterador para iterar a través de todas las líneas de tiempo disponibles. Devuelve una instancia de `Iterator<Timeline>`.

## `Mode`

El enumerado `Mode` proporciona dos opciones para el modo de almacenamiento: `Memory` y `Disk`.


# Ejemplo de uso de la clase Epoch

Supongamos que tenemos un archivo `timelines.dat` que contiene varias líneas de tiempo y queremos acceder a ellas usando la clase `Epoch`.

// Crear una nueva instancia de Epoch con modo de almacenamiento en memoria
File file = new File("timelines.dat");
Epoch epoch = new Epoch(file);

// Acceder a una línea de tiempo específica con identificador 123
Timeline timeline = epoch.get(123);

// Verificar si una línea de tiempo con identificador 123 está presente en el índice de la cadena
boolean contains = epoch.contains(123);

// Iterar a través de todas las líneas de tiempo
for (Timeline t : epoch) {
// hacer algo con la línea de tiempo
}