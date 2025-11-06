package Lambdas.Predicate;

import java.util.function.Predicate;

public class PredicateEjemplo {
    public static void main(String[] args) {
        /*
         * Predicate:
         * Recibe un valor y devuelve un booleano
         */

        /*
         * Predicate<String> predicate= (a) -> {
         *   return a.equals("a");
         *   } ;
        */

        Predicate<String> predicate= (a) -> a.equals("a");
       boolean resultado= predicate.test("a");
        System.out.println(resultado);
    }
}
