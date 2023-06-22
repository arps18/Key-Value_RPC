import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RemoteInterface interface defines methods that can be invoked remotely in a distributed Java application.
 * It extends the Remote interface from the Java RMI (Remote Method Invocation) package.
 */
public interface RemoteInterface extends Remote {

  /**
   * Executes a remote request operation.
   *
   * @param operation the operation to be performed remotely
   * @param key the key associated with the request
   * @param value the value associated with the request
   * @return a response message from the remote operation
   * @throws RemoteException if a remote communication error occurs
   */
  String request(String operation, String key, String value) throws RemoteException;
}
