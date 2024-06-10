import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class implementacionClienteChat extends UnicastRemoteObject implements chatCliente, Runnable {
    private chatServidor servidor;
    private String nombre;
    private int[] arreglo;
    private int opcion;
    private Menu menu;

    public implementacionClienteChat(String nombre, chatServidor servidor, int[] arreglo, int opcion, Menu menu) throws RemoteException {
        this.nombre = nombre;
        this.servidor = servidor;
        this.opcion = opcion;
        this.arreglo = arreglo;
        this.menu = menu;
        servidor.registro(this);
    }

    @Override
    public void mensajeCliente(String mensaje) throws RemoteException {
        System.err.println(mensaje);
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (menu.getOpcion() == 0) {
                    Thread.sleep(100);
                }

                this.arreglo = menu.getArreglo();
                this.opcion = menu.getOpcion();

                String resultado = servidor.procesarSolicitud(arreglo, opcion, nombre);
                menu.mostrarArregloOrdenado(resultado);
                servidor.mostrarArregloOrginal(arreglo, nombre);
                menu.setTiempo(resultado);

                menu.setOpcion(0);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
