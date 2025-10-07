import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ChatClientGUI provides a Swing-based graphical user interface for the chat client.
 * It handles user input, message display, event listeners, and client-server communication.
 */
public class ChatClientGUI extends JFrame {
    /** Displays chat messages from all users. */
    private JTextArea messageArea;
    /** Field for user to type messages. */
    private JTextField textField;
    /** Button to exit the chat and close the application. */
    private JButton exitButton;
    /** Handles networking and communication with the chat server. */
    private ChatClient client;

    /**
     * Constructs the chat GUI, sets up all components, and connects to the server.
     */
    public ChatClientGUI() {
        super("Chat Application");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // UI color and font settings for consistent look
        Color backgroundColor = new Color(240, 240, 240);
        Color buttonColor = new Color(75, 75, 75);
        Color textColor = new Color(50, 50, 50);
        Font textFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 12);

        // Message area setup (read-only)
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(textColor);
        messageArea.setFont(textFont);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // Prompt user for their name when launching the app
        String name = JOptionPane.showInputDialog(this, "Enter your name:", "Name Entry", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat Application - " + name);

        // Text field for user input
        textField = new JTextField();
        textField.setFont(textFont);
        textField.setForeground(textColor);
        textField.setBackground(backgroundColor);

        // When Enter is pressed, send the message with a timestamp
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + name + ": "
                        + textField.getText();
                client.sendMessage(message); // Send to server
                textField.setText(""); // Clear input field
            }
        });

        // Exit button to notify the server and exit gracefully
        exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.setBackground(buttonColor);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> {
            // Notify server about leaving
            String departureMessage = name + " has left the chat.";
            client.sendMessage(departureMessage);
            try {
                Thread.sleep(1000); // Allow server to process message
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            System.exit(0);
        });

        // Bottom panel holds text field and exit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        try {
            // Connect to chat server at localhost:5000
            this.client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);
            client.startClient(); // Begin listening for incoming messages
        } catch (IOException e) {
            // Display error if unable to connect
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the server", "Connection error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Appends a received message to the chat display area.
     * @param message the message received from the server
     */
    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    /**
     * Main entry point. Launches the chat GUI on the Swing event thread.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}