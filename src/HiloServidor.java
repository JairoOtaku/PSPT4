
import java.io.*;
import java.net.*;

public class HiloServidor extends Thread {

    Socket socket;
    ObjectOutputStream outObjeto;
    ObjectInputStream inObjeto;
    EstructuraFicheros NF;

    public HiloServidor(Socket s, EstructuraFicheros nF) throws IOException {
        socket = s;
        NF = nF;
        inObjeto = new ObjectInputStream(socket.getInputStream());
        outObjeto = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run() {
        try {
            outObjeto.writeObject(NF);
            while (true) {
                Object peticion = inObjeto.readObject();
                if (peticion instanceof PideFichero) {
                    PideFichero fichero = (PideFichero) peticion;
                    EnviarFichero(fichero);
                }
                if (peticion instanceof EnviaFichero) {
                    EnviaFichero fic = (EnviaFichero) peticion;
                    File d = new File(fic.getDirectorio());
                    File f1 = new File(d, fic.getNombre());
                    FileOutputStream fos = new FileOutputStream(f1);
                    fos.write(fic.getContenidoFichero());
                    fos.close();
                    EstructuraFicheros n = new EstructuraFicheros(fic.getDirectorio());
                    outObjeto.writeObject(n);
                }
            }
        } catch (IOException e) {
            try {
                inObjeto.close();
                outObjeto.close();
                socket.close();
                System.out.println("Cerrando cliente");
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void EnviarFichero(PideFichero fich) {
        File fichero = new File(fich.getNombreFichero());
        FileInputStream filein = null;
        try {
            filein = new FileInputStream(fichero);
            long bytes = fichero.length();
            byte[] buff = new byte[(int) bytes];
            int i, j = 0;
            while ((i = filein.read()) != -1) {
                buff[j] = (byte) i;
                j++;
            }
            filein.close();
            Object ff = new ObtieneFichero(buff);
            outObjeto.writeObject(ff);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
