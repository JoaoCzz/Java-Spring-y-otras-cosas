package Lambdas.Supplier;

import java.util.function.Supplier;

public class SupplierEjemplo {
    public static void main(String[] args) {
        /*
         * Supplier
         * No recibe ningun valor, pero retorna un resultado
         * Pd: No existe Bisupplier porque en java no existe doble retorno xd
         */

        /*  Ponemos en el <> el tipo de dato que vamos a retornar
         *   Supplier<String> supplier= () -> {
         *   return "Hola , Soy un Supplier hecho por Duzz";
         *   };
         */

        //Como sigue siendo una linea podemos resumirlo eliminando las llaves
        Supplier<String> supplier= () -> "Hola,Soy un Supplier hecho por Duzz";
        System.out.println(supplier.get());
    }
}
