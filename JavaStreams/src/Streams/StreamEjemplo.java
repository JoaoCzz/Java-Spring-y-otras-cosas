package Streams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamEjemplo {
   static List<String> nombres = Arrays.asList("Duzz","Paul","Homer","Bob","Goku","Naruto","Satoru");
    public static void main(String[] args) {
        /*
         * Un Stream es una secuencia (flujo) de datos que se procesan de forma continua.
         * Podemos imaginarlo como una línea de ensamblaje: los datos entran por un extremo (entrada)
         * y, mientras avanzan por la cadena, pueden transformarse, filtrarse o combinarse,
         * hasta producir un resultado final (salida).
         *
         * Los Streams permiten trabajar con colecciones de datos de manera declarativa,
         * aplicando operaciones como map(), filter(), reduce(), entre otras.
         */

        // Los Streams procesan los datos de forma secuencial, en orden de aparición.
        // Los operadores de Stream se clasifican en:
        //  - Intermedios: No terminan el flujo, permiten continuar el procesamiento (por ejemplo: filter, map, sorted).
        //  - Finales: Cierran el flujo y devuelven un resultado o ejecutan una acción (por ejemplo: forEach, collect, reduce).

        StreamMap(); // Llamada de ejemplo
    }

    public static void StreamForEach() {
        // forEach(): Recorre cada elemento del Stream y ejecuta una acción sobre él.
        // Es un operador FINAL, recibe un Consumer.
        nombres.stream().forEach(n -> System.out.println(n));

        // Forma abreviada usando referencia a método:
        // nombres.stream().forEach(System.out::println);

        // También se pueden realizar acciones más complejas:
        nombres.stream().forEach(n -> {
            // Ejemplo: realizar operaciones o consultas
            // Este bloque se ejecuta por cada elemento y no retorna nada.
        });
    }

    public static void StreamFilter() {
        // filter(): Filtra los elementos que cumplen una condición.
        // Es un operador INTERMEDIO y recibe un Predicate.
        // En este caso, mostramos los nombres con más de 4 letras.
        nombres.stream()
                .filter(n -> n.length() > 4)
                .forEach(System.out::println);
    }

    public static void StreamMap() {
        // map(): Transforma cada elemento aplicando una función.
        // Es un operador INTERMEDIO que recibe una Function.
        // En este caso, convertimos todos los nombres a mayúsculas.
        nombres.stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);

        // También podemos encadenar map() y filter():
        // Ejemplo: convertir a mayúsculas y mostrar solo los que empiezan con 'D'.
        nombres.stream()
                .map(String::toUpperCase)
                .filter(n -> n.startsWith("D"))
                .forEach(System.out::println);
    }

    public static void StreamSorted() {
        // sorted(): Ordena los elementos del Stream.
        // Por defecto, los ordena alfabéticamente (orden natural).
        nombres.stream()
                .sorted()
                .forEach(System.out::println);

        // También se pueden definir reglas personalizadas de ordenamiento
        // usando un Comparator.
    }

    public static void StreamReduce() {
        // reduce(): Combina todos los elementos en un solo valor.
        // Es un operador FINAL.
        // El primer parámetro (identity) es el valor inicial.
        String resultado = nombres.stream()
                .reduce("Resultado:", (a, b) -> a + " " + b);
        System.out.println(resultado);
    }

    public static void StreamCollect() {
        // collect(): Recoge los elementos del Stream en una colección o estructura final.
        // Aquí convertimos todos los nombres a mayúsculas y los guardamos en una nueva lista.
        List<String> resultado = nombres.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        // Mostramos la nueva lista:
        resultado.forEach(System.out::println);
    }

    public static void StreamDistinct() {
        // distinct(): Elimina los elementos duplicados.
        List<String> nombres2 = Arrays.asList("Duzz", "Duzz", "Paul", "Homer", "Bob", "Goku", "Naruto", "Satoru");

        nombres2.stream()
                .distinct()
                .forEach(System.out::println);
    }

    public static void StreamLimit() {
        // limit(): Limita el número de elementos procesados.
        // En este caso, mostramos solo los primeros 3 elementos.
        nombres.stream()
                .limit(3)
                .forEach(System.out::println);
    }

    public static void StreamSkip() {
        // skip(): Omite un número específico de elementos.
        // Por ejemplo, omitimos los 3 primeros y procesamos el resto.
        nombres.stream()
                .skip(3)
                .forEach(System.out::println);
    }

    public static void StreamAnyMatch() {
        // anyMatch(): Verifica si algún elemento cumple una condición.
        // Retorna true si al menos uno cumple.
        boolean resultado = nombres.stream()
                .anyMatch(n -> n.startsWith("D"));
        System.out.println(resultado);
    }

    public static void StreamAllMatch() {
        // allMatch(): Verifica si todos los elementos cumplen una condición.
        boolean resultado = nombres.stream()
                .allMatch(n -> n.startsWith("D"));
        System.out.println(resultado);
    }

    public static void StreamNoneMatch() {
        // noneMatch(): Verifica si ningún elemento cumple una condición.
        // Retorna true solo si ninguno cumple.
        boolean resultado = nombres.stream()
                .noneMatch(n -> n.length() == 10);
        System.out.println(resultado);
    }
    public static void StreamCount() {
        // count(): Cuenta el número de elementos en el Stream
        // Es un operador final
        long cantidad = nombres.stream().count();
        System.out.println("Cantidad de nombres: " + cantidad);
    }

    public static void StreamFindFirst() {
        // findFirst(): Devuelve el primer elemento del Stream
        // Es un operador final que retorna un Optional
        Optional<String> primero = nombres.stream().findFirst();
        primero.ifPresent(n -> System.out.println("Primer nombre: " + n));
    }

    public static void StreamPeek() {
        // peek(): Permite observar los elementos del Stream mientras pasan por el flujo
        // Es un operador no final, muy útil para depurar o registrar
        nombres.stream()
                .peek(n -> System.out.println("Procesando: " + n))
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    public static void StreamFlatMap() {
        // flatMap(): Convierte cada elemento en un Stream y los combina (aplana)
        // Es un operador no final
        // En este ejemplo, cada nombre se separa en letras individuales
        List<String> resultado = nombres.stream()
                .flatMap(n -> Arrays.stream(n.split("")))
                .collect(Collectors.toList());

        resultado.stream().forEach(System.out::println);
    }

    public static void StreamGroupingBy() {
        // groupingBy(): Agrupa los elementos según una clave
        // Se usa dentro de collect() con Collectors
        // En este caso agrupamos los nombres por su longitud
        Map<Integer, List<String>> agrupado =
                nombres.stream().collect(Collectors.groupingBy(String::length));

        agrupado.forEach((longitud, lista) -> {
            System.out.println("Longitud " + longitud + ": " + lista);
        });
    }

    public static void StreamPartitioningBy() {
        // partitioningBy(): Divide los elementos en dos grupos (true / false)
        // según una condición
        // En este caso, separamos los nombres que tienen más de 4 letras
        Map<Boolean, List<String>> particion =
                nombres.stream().collect(Collectors.partitioningBy(n -> n.length() > 4));

        System.out.println("Nombres con más de 4 letras: " + particion.get(true));
        System.out.println("Nombres con 4 o menos letras: " + particion.get(false));
    }
}
