
import java.io.*;

public class EstructuraFicheros implements Serializable {

    private String name;
    private String path;
    private boolean isDir;
    private int numeFich;
    private EstructuraFicheros[] lista;

    public EstructuraFicheros(String name) throws FileNotFoundException {
        File file = new File(name);
        this.name = file.getName();
        this.path = file.getPath();
        this.isDir = file.isDirectory();
        this.lista = getListaFiles();
        if (file.isDirectory()) {
            File[] ficheros = file.listFiles();
            this.numeFich = 0;
            if (!(ficheros == null)) {
                this.numeFich = ficheros.length;
            }
        }
    }

    public EstructuraFicheros(String name, String path, boolean isDir, int numF) {
        this.name = name;
        this.path = path;
        this.isDir = isDir;
        this.numeFich = numF;
    }

    public String getName() {
        String name_dir = name;
        if (isDir) {
            int l = path.lastIndexOf(File.separator);
            name_dir = path.substring(1 + 1, path.length());
        }
        return name_dir;
    }

    public String getPath() {
        return path;
    }

    public boolean isDir() {
        return isDir;
    }

    public int getNumeFich() {
        return numeFich;
    }

    public EstructuraFicheros[] getLista() {
        return lista;
    }

    @Override
    public String toString() {
        String nom = this.name;
        if (this.isDir) {
            nom = "(DIR) " + name;
        }
        return nom;
    }

    public EstructuraFicheros[] getListaFiles() {

        EstructuraFicheros[] lista = null;
        String sDirectorio = this.path;
        File f = new File(sDirectorio);
        File[] ficheros = f.listFiles();
        int longitud = ficheros.length;

        if (longitud > 0) {
            lista = new EstructuraFicheros[longitud];
            for (int x = 0; x < ficheros.length; x++) {
                EstructuraFicheros elemento;
                String nombre = ficheros[x].getName();
                String path = ficheros[x].getPath();
                boolean isDir = ficheros[x].isDirectory();
                int num = 0;
                if (isDir) {
                    File[] fic = ficheros[x].listFiles();
                    if (!(fic == null)) {
                        num = fic.length;
                    }
                }
                elemento = new EstructuraFicheros(nombre, path, isDir, num);
                lista[x] = elemento;
            }
        }
        return lista;
    }
}
