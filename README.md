# Multi-threaded Key-Value Store Server with RPC

This project implements a multi-threaded key-value store server using Remote Procedure Calls (RPC) for communication with clients. The server allows concurrent operations on the key-value store and ensures thread safety using mutual exclusion.

## Prerequisites

- Java Development Kit (JDK) 8 or later
- IDE or Command Line Interface (CLI) for compiling and running Java code

## Usage

1. Clone the repository or download the source code files.

2. Compile the code using the following commands:

   ```
   javac Server.java
   javac Client.java
   ```

3. Start the server by running the `Server` class:

   ```
   java Server
   ```

   The server prompts you to enter the host address and port number. Provide the appropriate values to configure the server.

4. Start the client by running the `Client` class:

   ```
   java Client
   ```

   The client prompts you to enter the host address and port number. Enter the same values you provided for the server configuration.

5. The client is now connected to the server, and you can perform key-value store operations.

    - Pre-populated Data: The client will automatically populate the key-value store with some sample data.
    - Command Format: Enter commands in the format: `operation key [value]`.
    - Supported Operations: PUT, GET, DELETE.
    - Example Commands:
        - `PUT key1 value1`
        - `GET key2`
        - `DELETE key3`
      
6. To connect with another client and check concurrent operations, one can run `Client_B` and repeat similar steps from Step 2. 

7. To quit the client, enter `quit`.

