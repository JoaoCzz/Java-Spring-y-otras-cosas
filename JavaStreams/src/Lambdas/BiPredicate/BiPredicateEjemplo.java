package Lambdas.BiPredicate;

import java.util.function.BiPredicate;

public class BiPredicateEjemplo {
    public static void main(String[] args) {
        /*
         * BiPredicate:
         * Recibe dos valores y devuelve un booleano (True o false=
         */

        //Solo hice un ejemplo si a es mayor que b

       /* BiPredicate<Integer,Integer> biPredicate = (a,b) ->{
        *    return a>b;
        *    };
        */

        BiPredicate<Integer,Integer> biPredicate = (a,b) -> a>b;
        boolean resultado= biPredicate.test(6,4);
        System.out.println(resultado);

    }
}
