import java.io.*;
import java.net.*;
import javax.swing.*;

public class Servidor {

    static Integer PUERTO = 44441;
    static public EstructuraFicheros NF;
    static ServerSocket servidor;

    public static void main(String[] args) throws IOException {
        String Directorio = "";
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.setDialogTitle("SELECCIONA EL DIRECTORIO DONDE ESTAN LOS FICHEROS");
        int returnVal = f.showDialog(f, "Seleccionar");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = f.getSelectedFile();
            Directorio = file.getAbsolutePath();
        }
        //si no se selecciona nada salir
        if (Directorio.equals("")) {
            System.out.println("Debe seleccionar un directorio.");
            System.exit(0);
        }
        servidor = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado en el puerto: " + PUERTO);
        while (true) {
            try {
                Socket cliente = servidor.accept();
                System.out.println("Bienvenido al cliente");
                NF = new EstructuraFicheros(Directorio);
                HiloServidor hilo = new HiloServidor(cliente, NF);
                hilo.start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }

    }
}