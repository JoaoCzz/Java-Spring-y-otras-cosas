package Lambdas.BinaryOperator;

import java.util.function.BinaryOperator;

public class BinaryOperatorEjemplo {
    public static void main(String[] args) {
       /*
        *BinaryOperator:
        * Recibe dos valores del mismo tipo y retorna un valor del mismo tipo
        */

        //Solo se pone un vez porque los dos valores seran del mismo tipo

        /*
         * BinaryOperator<Integer> binaryOperator= (a,b) -> {
         * return a+b;
         *  };
        */
        BinaryOperator<Integer> binaryOperator= (a,b) -> a + b;
         int resultado =binaryOperator.apply(4,5);
        System.out.println(resultado);
    }
}
