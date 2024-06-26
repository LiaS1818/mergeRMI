import calculos.MergeSortExecutor;
import calculos.MergeSortTask;
import calculos.SequentialMergeSort;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class implementacionChat extends UnicastRemoteObject implements chatServidor {
    private ArrayList<chatCliente> clientes;
    private ArrayList<int[]> resultadosClientes;
    private final int LIMITE_SOLICITUDES = 2; // Establece el límite de solicitudes necesarias para combinar y ordenar

    protected implementacionChat() throws RemoteException {
        clientes = new ArrayList<>();
        resultadosClientes = new ArrayList<>();
    }

    @Override
    public void registro(chatCliente cliente) throws RemoteException {
        this.clientes.add(cliente);
    }

    @Override
    public void mensaje(String mensaje) throws RemoteException {
        for (chatCliente cliente : clientes) {
            cliente.mensajeCliente(mensaje);
        }
    }

    @Override
    public synchronized String procesarSolicitud(int[] arreglo, int opcion, String nombre) throws RemoteException {
        resultadosClientes.add(arreglo);
        String resultado = "";

        if (resultadosClientes.size() >= LIMITE_SOLICITUDES) {
            int[] arregloCombinado = combinarResultadosClientes();
            resultado = ordenarArreglo(arregloCombinado, opcion);

            mensaje("Resultado combinado ordenado: " + resultado);

        }
        return "Solicitud recibida de " + nombre + ". Resulado final..." + resultado;
    }

    private String ordenarArreglo(int[] arreglo, int opcion) throws RemoteException {
        mensaje(String.valueOf(opcion));
        int[] arregloAux = Arrays.copyOf(arreglo, arreglo.length);
        double duracionMilisegundos;
        StringBuilder sb = new StringBuilder();
        long tiempoInicio;
        long tiempoFinal;
        String tiempo = null;
        switch (opcion) {
            case 1:
                tiempoInicio = System.nanoTime();
                SequentialMergeSort mergeSort = new SequentialMergeSort();
                mergeSort.ordena(arregloAux, 0, arregloAux.length - 1);
                tiempoFinal = System.nanoTime();
                duracionMilisegundos = (tiempoFinal - tiempoInicio) / 1e6;
                tiempo = String.format("Tiempo: %.2f ms", duracionMilisegundos);
                break;
            case 2:
                tiempoInicio = System.nanoTime();
                ForkJoinPool pool = new ForkJoinPool();
                MergeSortTask task = new MergeSortTask(arregloAux, 0, arregloAux.length);
                arregloAux = pool.invoke(task);
                tiempoFinal = System.nanoTime();
                duracionMilisegundos = (tiempoFinal - tiempoInicio) / 1e6;
                tiempo = String.format("Tiempo: %.2f ms", duracionMilisegundos);
                break;
            case 3:
                ExecutorService executor = Executors.newCachedThreadPool();
                MergeSortExecutor task2 = new MergeSortExecutor(arregloAux, 0, arregloAux.length);
                tiempoInicio = System.nanoTime();
                Future<int[]> futureResult = executor.submit(task2);

                try {
                    arregloAux = futureResult.get();
                    tiempoFinal = System.nanoTime();
                    duracionMilisegundos = (tiempoFinal - tiempoInicio) / 1e6;
                    tiempo = String.format("Tiempo: %.2f ms", duracionMilisegundos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    executor.shutdown();
                }
                break;
            default:
                System.out.println( "Opción no válida");
        }


        for (int num : arregloAux) {
            sb.append(num).append(" ");
        }
        sb.append("|").append(tiempo);

        // Enviar el resultado a todos los clientes
        return sb.toString();
    }

    @Override
    public String mostrarArregloOrginal(int[] arreglo, String nombre) throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (int num : arreglo) {
            sb.append(num).append(" ");
        }
        String arregloCliente = sb.toString();
        System.out.println("Arreglo Creado por: " + nombre + ": " + arregloCliente);
        return arregloCliente;
    }

    @Override
    public int[] combinarResultadosClientes() throws RemoteException {
        ArrayList<int[]> todosLosArreglos = new ArrayList<>(resultadosClientes);
        return Arrays.stream(todosLosArreglos.toArray(new int[0][]))
                .flatMapToInt(Arrays::stream)
                .toArray();
    }

    public ArrayList<int[]> getResultados() {
        return this.resultadosClientes;
    }

    public void limpiarArregloDeArreglos(){
        resultadosClientes.clear(); // Limpiar la lista para futuras solicitudes

    }
}
