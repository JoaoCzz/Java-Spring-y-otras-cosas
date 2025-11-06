package Lambdas.Function;

import java.util.function.Function;

public class FunctionEjemplo {
    public static void main(String[] args) {

        /*
         * Function:
         * Recibe un valor y retorna un resultado
         */

        //En las <> ponemos el tipo de valor de entrada y el tipo de valor que vamos a retonar

       /*
        * Function <Integer,String> function = (num) -> {
        *    return "El numero es: " + num;
        * };
        */

        Function <Integer,String> function = (num) ->"El numero es: " + num;

        String resultado=function.apply(5);
        System.out.println(resultado);

    }
}
