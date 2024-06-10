import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class clienteRMI {
    public static void main(String[] args) {
        try {
            String nombre = JOptionPane.showInputDialog("Ingresa Nombre");
            Registry rmii = LocateRegistry.getRegistry("localhost", 1005);
            Menu menu = new Menu();
            menu.setVisible(true);

            while (menu.getOpcion() == 0) {
                Thread.sleep(100);
            }

            int[] arregloAux = menu.getArreglo();
            int opcion = menu.getOpcion();
            chatServidor servidor = (chatServidor) rmii.lookup("Chat");

            new Thread(new implementacionClienteChat(nombre, servidor, arregloAux, opcion, menu)).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
