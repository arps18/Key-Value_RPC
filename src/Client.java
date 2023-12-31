import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * The Client class interacts with the server by sending requests and receiving responses.
 */
public class Client {

  /**
   * Private constructor to prevent instantiation of the Client class.
   */
  private Client() {}

  /**
   * The main method that starts the client.
   * Connects to the server using RMI registry and processes user commands.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    try {

      Scanner scanner = new Scanner(System.in);
      System.out.print("Enter the host address: ");
      String host = scanner.nextLine();
      System.out.print("Enter the port number: ");
      int port = scanner.nextInt();
      scanner.nextLine();

      /**
       * Setting up the RMI registry.
       */
      Registry registry = LocateRegistry.getRegistry(host, port);

      /**
       * Looking up for the remote object from the registry
       */
      RemoteInterface stub = (RemoteInterface) registry.lookup("KeyValueServer");

      System.out.println("Connected to server at: " + host + ":" + port);
      System.out.println("---------------------------------------");

      System.out.println("Pre-populating with the static key-value store...");
      System.out.println(" ");

      stub.request("PUT", "key1", "Arpan");
      stub.request("PUT", "key2", "Patel");
      stub.request("PUT", "key3", "United States");
      stub.request("PUT", "key4", "Boston");
      stub.request("PUT", "key5", "iPhone");
      stub.request("PUT", "key6", "Apple");
      printOperationLog("PUT ", "key1 ", "Arpan");
      printOperationLog("PUT ", "key2 ", "Patel");
      printOperationLog("PUT ", "key3 ", "United States");
      printOperationLog("PUT ", "key4 ", "Boston");
      printOperationLog("PUT ", "key5 ", "iPhone");
      printOperationLog("PUT ", "key6 ", "Apple");

      System.out.println(" ");
      System.out.println("Key-value store populated.");

      System.out.println("---------------------------------------");


      System.out.println("Performing operations...");

      System.out.println("---------------------------------------");

      /**
       * Performing the GET operations
       */
      for (int i = 1; i <= 5; i++) {
        String key = "key" + i;
        String response = stub.request("GET", key, "");
        printOperationLog("GET", key, response);
      }

      System.out.println("---------------------------------------");

      /**
       * Performing the DELETE operations
       */
      for (int i = 1; i <= 5; i++) {
        String key = "key" + i;
        String response = stub.request("DELETE", key, "");
        printOperationLog("DELETE", key, response);
      }

      System.out.println("---------------------------------------");

      /**
       * Performing the GET operations
       */
      for (int i = 1; i <= 5; i++) {
        String key = "key" + i;
        String response = stub.request("GET", key, "");
        printOperationLog("GET", key, response);
      }

      System.out.println(" ");
      System.out.println("Operations completed.");
      System.out.println("---------------------------------------");

      System.out.println("Enter commands in the format: operation key [value]");
      System.out.println("Supported operations: PUT, GET, DELETE");
      System.out.println("Enter 'quit' to exit.");

      boolean connected = true;
      while (connected) {
        System.out.println("---------------------------------------");
        try {
          String input = scanner.nextLine();
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
            System.out.println("Invalid command format!");
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
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    String timestamp = "[Time: " + simpleDateFormat.format(new Date()) + "]";
    System.out.println(timestamp + " | Operation: " + operation + " | Key: " + key + " | Response: " + response);
  }
}

