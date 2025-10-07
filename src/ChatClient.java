import java.io.*;
import java.net.*;
import java.util.function.Consumer;

/**
 * The ChatClient class encapsulates client-side networking logic for connecting to the server,
 * sending messages, and receiving updates in a separate thread.
 */
public class ChatClient {
    /** The socket for connecting to the chat server. */
    private Socket socket;
    /** BufferedReader for incoming messages from the server. */
    private BufferedReader in;
    /** PrintWriter for sending messages to the server. */
    private PrintWriter out;
    /** Callback for when a message is received from the server. */
    private Consumer<String> onMessageReceived;

    /**
     * Constructs a ChatClient.
     * @param serverAddress the IP address of the chat server
     * @param serverPort the port number of the chat server
     * @param onMessageReceived a callback to process received messages
     * @throws IOException if connection fails
     */
    public ChatClient(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.onMessageReceived = onMessageReceived;
    }

    /**
     * Sends a message to the server.
     * @param msg the message to send
     */
    public void sendMessage(String msg) {
        out.println(msg);
    }

    /**
     * Starts a background thread to listen for incoming messages from the server.
     * Calls the callback for every received message.
     */
    public void startClient() {
        new Thread(() -> {
            try {
                String line;
                // Continuously read messages from server until connection is closed
                while ((line = in.readLine()) != null) {
                    onMessageReceived.accept(line);
                }
            } catch (IOException e) {
                // Log errors to standard error output
                e.printStackTrace();
            }
        }).start();
    }
}