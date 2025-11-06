package Lambdas.BiConsumer;

import java.util.function.BiConsumer;

public class BiConsumerEjemplo {
    public static void main(String[] args) {
      /*
       * BiConsumer:
       * Recibe dos valores y no retorna nada
       */

       /*
        *   BiConsumer<String,String> biConsumer = (a,b) -> {
        *        System.out.println(a + " "+ b);
        *    };
        */

        //Podemos acortarlo porque es una sola linea
        BiConsumer<String,String> biConsumer = (a,b) -> System.out.println(a + " "+ b);
        //Ojo: No podemos usar la forma corta del sout porque estamos recibiendo dos parametros
        biConsumer.accept("Duzz1","Duzz2");

    }
}
