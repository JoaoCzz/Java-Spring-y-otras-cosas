package Lambdas.Callable;

import java.util.concurrent.Callable;

public class CallableEjemplo {
    public static void main(String[] args) {
         /*
          *Callable:
          * No recibe valores, pero retoruna un resultado y puede lanazar una excepcion
          * Es casi lo mismo que el function con la diferencia que puede lanzar excepciones (try/catch)
          */
            //Sus usos son para programacion asincrona como puede ser:
            // Concurrencia , Hilos , Promesas -future

        /* Callable<String> callable = () -> {
         *  return "Resultado de la tarea";
         *  };
         */
        Callable<String> callable = () -> "Resultado de la tarea";
        try {
           String resultado= callable.call();
            System.out.println(resultado);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
