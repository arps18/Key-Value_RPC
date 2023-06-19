import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server implements RemoteInterface {

  private Map<String, String> keyValueStore;

  public Server() {
    keyValueStore = new HashMap<>();
  }

  @Override
  public synchronized String request(String operation, String key, String value) throws RemoteException {
    String response = "";

    try {
      // Perform the requested operation
      switch (operation.toLowerCase()) {
        case "put":
          keyValueStore.put(key, value);
          response = "PUT operation successful";
          break;
        case "get":
          response = keyValueStore.getOrDefault(key, "Key not found");
          break;
        case "delete":
          keyValueStore.remove(key);
          response = "DELETE operation successful";
          break;
        default:
          response = "Invalid operation";
          break;
      }
    } catch (Exception e) {
      response = "Error occurred: " + e.getMessage();
    }

    // Log the operation and response with timestamp
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String timestamp = dateFormat.format(new Date());
    System.out.println(timestamp + " | Operation: " + operation + " | Key: " + key + " | Response: " + response);

    return response;
  }

  public static void main(String[] args) {
    try {
      Server server = new Server();
      RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(server, 0);

      // Set up the RMI registry
      Registry registry = LocateRegistry.createRegistry(1099);
      registry.rebind("KeyValueServer", stub);

      System.out.println("Server is running...");
    } catch (Exception e) {
      System.err.println("Server exception: " + e.toString());
      e.printStackTrace();
    }
  }
}
