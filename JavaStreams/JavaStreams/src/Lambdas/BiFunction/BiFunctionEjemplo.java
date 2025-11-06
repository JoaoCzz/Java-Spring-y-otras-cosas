package Lambdas.BiFunction;

import java.util.function.BiFunction;

public class BiFunctionEjemplo {
    public static void main(String[] args) {
        /*
         * BiFunction:
         * Recibe dos valores y retorna un resultado.
         */

        //<valor1,valor2,valorRetorno>
        /*
         *   BiFunction<Integer,Integer,Integer> function= (a,b) -> {
         *   return (a+b);
         *    };
         */

        BiFunction<Integer,Integer,Integer> function= (a,b) -> a+b;
        int resultado= function.apply(4,5);
        System.out.println(resultado);
    }


}
