import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        String servidor = "127.0.0.1"; // localhost
        int puerto = 12345;

        try (Socket socket = new Socket(servidor, puerto);
             BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Conectado a la Barberia...");
            salida.println("SOLICITO_CORTE");

            String respuesta;
            while ((respuesta = entrada.readLine()) != null) {
            System.out.println("Barberia dice: " + respuesta);
                if (respuesta.contains("terminado") || respuesta.contains("LLENA")) {
                    break;
                }
            }
            System.out.println("Conexion cerrada.");
            
        } catch (IOException e) {
            System.err.println("No se pudo conectar al servidor.");
        }
    }
}
