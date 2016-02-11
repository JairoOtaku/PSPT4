
import java.io.Serializable;

public class PideDirectorio implements Serializable {

    String nombreDirectorio;

    public PideDirectorio(String nombreDirectorio) {
        this.nombreDirectorio = nombreDirectorio;
    }

    public String getNombreDirectorio() {
        return nombreDirectorio;
    }
}
