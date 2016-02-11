public class EnviaFichero {
    byte[] contenidoFichero;
    String nombre;
    String directorio;
    public EnviaFichero(byte[] contenidoFichero, String nombre, String directorio){
        this.contenidoFichero = contenidoFichero;
        this.nombre = nombre;
        this.directorio = directorio;
    }
    public String getNombre(){
        return nombre;
    }
    public String getDirectorio(){
        return directorio;
    }
    public byte[] getContenidoFichero(){
        return contenidoFichero;
    }
}