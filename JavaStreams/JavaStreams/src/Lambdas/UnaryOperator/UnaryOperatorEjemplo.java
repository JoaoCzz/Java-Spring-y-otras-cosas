package Lambdas.UnaryOperator;

import java.util.function.UnaryOperator;

public class UnaryOperatorEjemplo {
    public static void main(String[] args) {
        /*
         *UnaryOperator:
         * Recibe un valor, lo procesa y devuelve un resultado del mismo tipo
         */
         //Para calcular el iva por ejemplo
        /*
         *UnaryOperator<Double> unaryOperator = (a) -> {
         *   return (Double) (a*0.12);
         * };
         */
        UnaryOperator<Double> unaryOperator = (a) -> (Double) (a*0.12);
        double resultado = unaryOperator.apply(50.0);
        System.out.println(resultado);

    }
}
