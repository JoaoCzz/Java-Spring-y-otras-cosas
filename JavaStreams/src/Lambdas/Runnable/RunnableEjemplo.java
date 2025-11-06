package Lambdas.Runnable;

public class RunnableEjemplo {
    /*
     *Runnable:
     *  No recibe valores y no retorna nada, solo ejecuta una tarea
     *  Su uso principal esta en Hilos (Threads)
     */
    public static void main(String[] args) {
       /*
        *Runnable runnable = () ->{
        * System.out.println("Ejecutando tarea ...");
        *  };
        */
        Runnable runnable = () -> System.out.println("Ejecutando tarea ...");
        runnable.run();
    }
}
