package Lambdas.Consumer;

import java.util.function.Consumer;

public class ConsumerEjemplo {
    public static void main(String[] args) {
        /*
         * Consumer:
         * Recibe un valor  y no retorna nada.
         */

        /*
         *   Consumer <String> consumer= (param) -> {
         *  System.out.println(param);
         *   };
        */

        /* En caso que la expresion solo tenga una linea podemos evitarnos las llaves
        *  Consumer <String> consumer= (param) -> System.out.println(param);
        */

        /* Si el mismo parametro que se envia se esta utilizando dentro el metodo podemos acortarlo mas.
         * Para es eliminamos las flechas, el parentesis del parametro de entrada y acortamos el sout con ::
         */

        Consumer <String> consumer= System.out::println;
        consumer.accept("Duzz");
    }
}
