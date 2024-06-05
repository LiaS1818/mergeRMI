
import javax.swing.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class clienteRMI {
    public static void main(String[] args) {
        try {
            String nombre = JOptionPane.showInputDialog("Ingresa Nombre");
            String nom = nombre;

            Registry rmii = LocateRegistry.getRegistry("localhost", 1005);
            Menu menu = new Menu();
            menu.setVisible(true);

            // Esperar hasta que el usuario haya interactuado con el menú para obtener el tamaño del arreglo y la opción
            while (menu.getOpcion() == 0) {
                Thread.sleep(100); // Esperar a que el usuario seleccione una opción
            }

            int[] arregloAux = menu.getArreglo();
            int opcion = menu.getOpcion();
            chatServidor servidor = (chatServidor) rmii.lookup("Chat");

            // Enviar la solicitud al servidor y recibir la respuesta

            new Thread(new implementacionClienteChat(nom, servidor, arregloAux, opcion, menu)).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
