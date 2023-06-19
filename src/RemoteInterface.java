import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {

  String request(String operation,String key, String value) throws RemoteException;
}