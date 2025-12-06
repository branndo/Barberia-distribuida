import java.io.*;
import java.net.*;

public class ManejadorCliente implements Runnable {
    private Socket socket;

    public ManejadorCliente(Socket socket) { this.socket = socket; }

    @Override
    public void run() {
        try (
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String mensaje = entrada.readLine();
            System.out.println("Cliente remoto dice: " + mensaje);

            if ("SOLICITO_CORTE".equalsIgnoreCase(mensaje)) {
                
                Servidor.mutex.acquire();

                if (Servidor.sillasOcupadas < Servidor.MAX_SILLAS) {
                    // hay lugar disponible
                Servidor.sillasOcupadas++;
                    System.out.println("Sillas ocupadas: " + Servidor.sillasOcupadas);
                Servidor.mutex.release();
                    
                    Servidor.sem_clientes.release(); // despierta al barbero
                    salida.println("HAY_LUGAR. Esperando turno...");
                    
                Servidor.sem_barbero.acquire(); // espera que el barbero este listo
                    
                    salida.println("Barbero listo. Recibiendo corte...");
                    Thread.sleep(3000); // tiempo del corte
                salida.println("Corte terminado. Adios!");
                    
                } else {
                    // barberia llena
                    Servidor.mutex.release();
                salida.println("BARBERIA_LLENA. Intente mas tarde.");
                    System.out.println("Cliente rechazado - barberia llena");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException e) {}
        }
    }
}
