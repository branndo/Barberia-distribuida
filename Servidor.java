import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;

public class Servidor {
    // --- ZONA DE RECURSOS COMPARTIDOS ---
    public static Semaphore sem_clientes = new Semaphore(0);
    public static Semaphore sem_barbero = new Semaphore(0);
    public static Semaphore mutex = new Semaphore(1);

    public static int sillasOcupadas = 0;
    public static final int MAX_SILLAS = 5;

    public static void main(String[] args) {
        int puerto = 12345;
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Barberia abierta en puerto " + puerto);
            System.out.println("Sillas disponibles: " + MAX_SILLAS);

            // Iniciamos el hilo del barbero
            new Thread(new Barbero()).start();

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nueva conexion entrante");
                new Thread(new ManejadorCliente(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Barbero implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("Barbero dice: Zzz... durmiendo al calor de las brazas...");

                    sem_clientes.acquire(); // se bloquea hasta que llegue cliente

                    mutex.acquire();
                    sillasOcupadas--;
                    mutex.release();

                    sem_barbero.release(); // avisa al manejador que esta listo

                    System.out.println("Barbero dice: Cortando cabello...");
                    Thread.sleep(3000); // simula el corte
                    System.out.println("Barbero dice: Corte realizado.");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
