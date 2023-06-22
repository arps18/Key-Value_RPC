import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Server class implements the RemoteInterface and provides remote methods for
 * performing key-value store operations.
 */
public class Server implements RemoteInterface {

  private Map<String, String> keyValueStore;

  /**
   * Constructs a new Server instance with an empty key-value store.
   */
  public Server() {
    keyValueStore = new HashMap<>();
  }

  /**
   * Processes the requested operation on the key-value store and returns the response.
   *
   * @param operation the operation to perform (PUT, GET, DELETE)
   * @param key       the key involved in the operation
   * @param value     the value associated with the key (for PUT operation)
   * @return the response message from the operation
   * @throws RemoteException if a remote error occurs during the operation
   */
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
          if (keyValueStore.containsKey(key)) {
            keyValueStore.remove(key);
            response = "DELETE operation successful";
          } else {
            response = "Key not found for DELETE operation";
          }
          break;
        default:
          response = "Invalid operation";
          break;
      }
    } catch (IllegalArgumentException e) {
      response = "Invalid arguments: " + e.getMessage();
    } catch (Exception e) {
      response = "Error occurred: " + e.getMessage();
    }

    // Log the operation and response with timestamp
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    String timestamp = "<Time: " + dateFormat.format(new Date()) + ">";
    System.out.println(timestamp + " | Operation: " + operation + " | Key: " + key + " | Response: " + response);

    return response;
  }

  /**
   * The main method that starts the server by creating an instance of Server,
   * exporting it as a remote object, and binding it to the RMI registry.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    try {
      Server server = new Server();
      RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(server, 0);

      // Get host and port number from user
      Scanner scanner = new Scanner(System.in);
      System.out.print("Enter the host address: ");
      String host = scanner.nextLine();
      System.out.print("Enter the port number: ");
      int port = scanner.nextInt();

      // Set up the RMI registry
      Registry registry = LocateRegistry.createRegistry(port);
      registry.rebind("KeyValueServer", stub);

      System.out.println("Server is running...");
    } catch (Exception e) {
      System.err.println("Server exception: " + e.toString());
      e.printStackTrace();
    }
  }
}
