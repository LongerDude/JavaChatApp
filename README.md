# JavaChatApp

A simple Java-based chat application that enables real-time communication between multiple clients and a server using socket programming. This project demonstrates basic networking and GUI principles in Java.

## Features

- Client-server architecture using TCP sockets
- Real-time text messaging
- Swing-based GUI for client interface
- Multi-threaded server to handle multiple clients

## Technologies Used

- Java SE
- Swing (GUI)
- Socket programming
- Threading

## Project Structure

```
JavaChatApp/
├── src/
│   ├── Server.java
│   ├── Client.java
│   └── ChatWindow.java
├── .idea/
├── JavaChatApp.iml
└── .gitignore
```

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/LongerDude/JavaChatApp.git
   cd JavaChatApp
   ```

2. Compile the source files:
   ```bash
   javac src/*.java
   ```

3. Start the server:
   ```bash
   java src.Server
   ```

4. Launch clients (in separate terminals or machines):
   ```bash
   java src.Client
   ```

## Notes

- Clients must connect to the correct IP and port specified in `Client.java`.
- The server must be running before any clients are launched.

## Future Improvements

- Add user authentication
- Enhance GUI design
- Implement message history
- Support file sharing

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
