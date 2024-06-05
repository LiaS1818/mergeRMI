import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class servidorRMI {
    public static void main(String[] args) {
        try {
            Registry rmi = LocateRegistry.createRegistry(1005);
            rmi.rebind("Chat", (Remote) new implementacionChat());
            System.out.println("SErvidor Activo");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
