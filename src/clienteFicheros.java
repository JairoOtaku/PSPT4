import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class clienteFicheros extends JFrame implements Runnable{
    static Socket socket;
    EstructuraFicheros nodo = null;
    ObjectInputStream inObjeto;
    ObjectOutputStream outObjeto;
    EstructuraFicheros Raiz;
    static JList listaDirec = new JList();
    static String direcSelec = "";
    static String ficheroSelec = "";
    static String ficherocompleto = "";
    
    public clienteFicheros(Socket s) throws IOException{
        super("SERVIDOR DE FICHEROS BÁSICO");
        socket = s;
        try{
            outObjeto = new ObjectOutputStream(socket.getOutputStream());
            inObjeto = new ObjectInputStream(socket.getInputStream());
        }catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        }
        listaDirec.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if(lse.getValueIsAdjusting()){
                    ficheroSelec = "";
                    ficherocompleto = "";
                    nodo = (EstructuraFicheros) listaDirec.getSelectedValue();
                    if (nodo.isDir()){
                        campo.setText("FUNCIÓN NO IMPLEMENTADA...");
                    }else{
                        ficheroSelec = nodo.getName();
                        ficherocompleto = nodo.getPath();
                        campo.setText("FICHERO seleccionado: " + ficheroSelec);
                    }
                }
            }
        });
        botonSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try{
                    socket.close();
                    System.exit(0);
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        botonDescargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(ficherocompleto.equals("")){
                    return;
                }
                PideFichero pido = new PideFichero(ficherocompleto);
                try{
                    outObjeto.writeObject(pido);
                    FileOutputStream fos = new FileOutputStream(ficheroSelec);
                    Object obtengo = inObjeto.readObject();
                    if(obtengo instanceof ObtieneFichero){
                        ObtieneFichero fic = (ObtieneFichero) obtengo;
                        fos.write(fic.getContenidoFichero());
                        fos.close();
                        JOptionPane.showMessageDialog(null, "FICHERO DESCARGADO");
                    }
                }catch(IOException e1){
                    e1.printStackTrace();
                }catch(ClassNotFoundException e1){
                    e1.printStackTrace();
                }
            }
        });
        botonCargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JFileChooser f;
                File file;
                f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.FILES_ONLY);
                f.setDialogTitle("Selecciona el Fichero a SUBIR AL SERVIDOR DE FICHEROS");
                int returnVal = f.showDialog(f, "Cargar");
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    file = f.getSelectedFile();
                    String archivo = file.getAbsolutePath();
                    String nombreArchivo = file.getName();
                    BufferedInputStream in;
                    try{
                        in = new BufferedInputStream(new FileInputStream(archivo));
                        long bytes = file.length();
                        byte[] buff = new byte[(int) bytes];
                        int i, j = 0;
                        while((i = in.read()) != -1){
                            buff[j] = (byte) i;
                            j++;
                        }
                        in.close();
                        Object ff = EnviaFichero(buff, nombreArchivo, direcSelec);
                        outObjeto.writeObject(ff);
                        JOptionPane.showMessageDialog(null, "FICHERO CARGADO");
                        nodo = (EstructuraFicheros) inObjeto.readObject();
                        EstructuraFicheros[] lista = nodo.getLista();
                        direcSelec = nodo.getPath();
                        llenarLista(lista, nodo.getNumeFich());
                        campo2.setText("Número de ficheros en el directorio: " + lista.length);
                    }catch(FileNotFoundException e1){
                        e1.printStackTrace();
                    }catch(IOException ee){
                        ee.printStackTrace();
                    }catch(ClassNotFoundException e2){
                        e2.printStackTrace();
                    }
                }
            }
        });
    }
    
    public static void main(String[] args) throws IOException{
        int puerto = 44441;
        Socket s = new Socket("localhost", puerto);
        clienteFicheros hiloC = new clienteFicheros(s);
        hiloC.setBounds(0, 0, 540, 500);
        hiloC.setVisible(true);
        new Thread(hiloC).start();
    }

    @Override
    public void run() {
        try{
            cab.setText("Conectando con el servidor...");
            Raiz = (EstructuraFicheros) inObjeto.readObject();
            EstructuraFicheros[] nodos = Raiz.getLista();
            direcSelec = Raiz.getPath();
            llenarLista(nodos, Raiz.getNumeFich());
            cab3.setText("RAIZ: " + direcSelec);
            cab.setText("CONECTADO AL SERVIDOR DE FICHEROS");
            campo2.setText("Número de ficheros en el directorio: " + Raiz.getNumeFich());
        }catch(IOException e1){
            e1.printStackTrace();
            System.exit(1);
        }catch(ClassNotFoundException e1){
            e1.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void llenarLista(EstructuraFicheros[] files, int numero){
        if(numero == 0){
            return;
        }
        DefaultListModel modeloLista = new DefaultListModel();
        modeloLista = new DefaultListModel();
        listaDirec.setForeground(Color.blue);
        Font fuente = new Font("Courier", Font.PLAIN, 12);
        listaDirec.setFont(fuente);
        listaDirec.removeAll();
        for(int i = 0; i < files.length; i++){
            modeloLista.addElement(files[i]);
        }
        try{
            listaDirec.setModel(modeloLista);
        }catch(NullPointerException n){
            
        }
    }
}