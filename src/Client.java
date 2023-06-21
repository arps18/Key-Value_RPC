import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Client class interacts with the server by sending requests and receiving responses.
 */
public class Client {
  private Client() {}

  /**
   * The main method that starts the client.
   * Connects to the server using RMI registry and processes user commands.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    try {
      // Get the RMI registry
      Registry registry = LocateRegistry.getRegistry("localhost", 1099);

      // Look up the remote object from the registry
      RemoteInterface stub = (RemoteInterface) registry.lookup("KeyValueServer");

      // Pre-populate key-value store with data
      System.out.println("Pre-populating the key-value store...");
      stub.request("PUT", "firstName", "John");
      stub.request("PUT", "lastName", "Doe");
      stub.request("PUT", "country", "United States");
      stub.request("PUT", "state", "California");
      stub.request("PUT", "device", "Phone");
      stub.request("PUT", "brand", "Apple");
      System.out.println("Key-value store populated.");

      // Perform PUT, GET, and DELETE operations
      System.out.println("Connected to server.");
      System.out.println("Performing operations...");

      // Perform 10 PUT operations
      for (int i = 1; i <= 10; i++) {
        String key = "key" + i;
        String value = "value" + i;
        String response = stub.request("PUT", key, value);
        printOperationLog("PUT", key, response);
      }

      // Perform 5 GET operations
      for (int i = 1; i <= 5; i++) {
        String key = "key" + i;
        String response = stub.request("GET", key, "");
        printOperationLog("GET", key, response);
      }

      // Perform 5 DELETE operations
      for (int i = 1; i <= 5; i++) {
        String key = "key" + i;
        String response = stub.request("DELETE", key, "");
        printOperationLog("DELETE", key, response);
      }

      System.out.println("Operations completed.");
      System.out.println("Enter 'quit' to exit.");

      boolean connected = true;
      while (connected) {
        try {
          String input = System.console().readLine();
          String[] parts = input.split(" ");

          if (parts[0].equalsIgnoreCase("quit")) {
            connected = false;
            System.out.println("Connection closed.");
          } else if (parts.length >= 2) {
            String operation = parts[0];
            String key = parts[1];
            String value = "";

            if (operation.equalsIgnoreCase("put") && parts.length >= 3) {
              value = parts[2];
            }

            String response = "";
            switch (operation.toLowerCase()) {
              case "put":
                response = stub.request("PUT", key, value);
                break;
              case "get":
                response = stub.request("GET", key, "");
                break;
              case "delete":
                response = stub.request("DELETE", key, "");
                break;
              default:
                System.out.println("Invalid operation");
                continue;
            }

            printOperationLog(operation, key, response);
          } else {
            System.out.println("Invalid command format.");
          }
        } catch (IllegalArgumentException e) {
          System.err.println("Invalid arguments: " + e.getMessage());
        } catch (Exception e) {
          System.err.println("Error occurred: " + e.toString());
        }
      }
    } catch (Exception e) {
      System.err.println("Client exception: " + e.toString());
      e.printStackTrace();
    }
  }

  /**
   * Prints the operation log with timestamp.
   *
   * @param operation the operation performed
   * @param key       the key involved in the operation
   * @param response  the response from the server
   */
  private static void printOperationLog(String operation, String key, String response) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String timestamp = dateFormat.format(new Date());
    System.out.println(timestamp + " | Operation: " + operation + " | Key: " + key + " | Response: " + response);
  }
}
