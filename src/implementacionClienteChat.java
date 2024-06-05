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
                // Esperar a que el usuario presione el botón para enviar la solicitud
                while (menu.getOpcion() == 0) {
                    Thread.sleep(100);
                }

                // Obtener el tamaño del arreglo, la opción y el arreglo desde la interfaz gráfica
                int[] arregloAux = menu.getArreglo();
                int opcion = menu.getOpcion();

                // Procesar la solicitud y mostrar el resultado

                String resultado =  servidor.procesarSolicitud(arregloAux, opcion, nombre);
                menu.mostrarArregloOrdenado(resultado);
                servidor.mostrarArregloOrginal(arregloAux, nombre);
                menu.setTiempo(resultado);
                String arregloCombinado = Arrays.toString(servidor.combinarResultadosClientes());
                arreglo = servidor.combinarResultadosClientes();
                resultado = servidor.procesarSolicitud(arreglo, opcion, nombre);
                menu.setTiempo(resultado);

                // Limpiar la opción en la interfaz gráfica para permitir enviar nuevas solicitudes
                menu.setOpcion(0);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}