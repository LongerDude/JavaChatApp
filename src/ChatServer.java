import java.io.*;
import java.net.*;
import java.util.*;

/**
 * ChatServer listens for incoming client connections and broadcasts received messages
 * to all connected clients. Each client runs in its own thread.
 */
public class ChatServer {
    /** List of all connected client handlers. */
    private static List<ClientHandler> clients = new ArrayList<>();

    /**
     * Main method to start the chat server.
     * @param args command line arguments (unused)
     * @throws IOException if server socket fails to start
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for clients...");

        // Accept clients in an infinite loop
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            // Create new handler for each client
            ClientHandler clientThread = new ClientHandler(clientSocket, clients);
            clients.add(clientThread);
            new Thread(clientThread).start();
        }
    }
}

/**
 * Handles communication with a single client. Receives messages and broadcasts them to all clients.
 */
class ClientHandler implements Runnable {
    private Socket clientSocket;
    // Shared list of all client handlers
    private List<ClientHandler> clients;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Constructs a handler for a connected client.
     * @param socket the client's socket
     * @param clients reference to the shared client handler list
     * @throws IOException if streams cannot be created
     */
    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
        this.clientSocket = socket;
        this.clients = clients;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     * Listens for messages from this client and forwards them to all clients.
     */
    public void run() {
        try {
            String inputLine;
            // Read messages until client disconnects
            while ((inputLine = in.readLine()) != null) {
                // Broadcast to all clients
                for (ClientHandler aClient : clients) {
                    aClient.out.println(inputLine);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // Clean up resources when client disconnects
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}