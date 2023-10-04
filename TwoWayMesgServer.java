/*
 * Implementation of a one-way message server in java
 */

// Package for I/O related stuff
import java.io.*;

// Package for socket related stuff
import java.net.*;

/*
 * This class does all the server's job
 * It receives the connection from client
 * and prints messages sent from the client
 */

// Server sock can deal w/ multiple clients
//Client initiaties contact with server
public class TwoWayMesgServer {
	/*
	 * The server program starts from here
	 */
	public static void main(String args[]) {
		// Server needs the port number to listen on
		if (args.length != 1) {
			System.out.println("usage: java OneWayMesgServer <port>");
			System.exit(1);
		}

		// Get the port on which server should listen */
		int serverPort = Integer.parseInt(args[0]);

		// Be prepared to catch socket related exceptions
		try {
			// Create welcoming socket at given port
			ServerSocket serverSock = new ServerSocket(serverPort);
			System.out.println("Waiting for a client ...");

			// Wait to receive a connection request
			Socket clientSock = serverSock.accept();
			System.out.println("Connected to a client at ('" +
												((InetSocketAddress) clientSock.getRemoteSocketAddress()).getAddress().getHostAddress()
												+ "', '" +
												((InetSocketAddress) clientSock.getRemoteSocketAddress()).getPort()
												+ "')"
												);
			
			// Prepare to write to client with auto flush on
			PrintWriter toClientWriter =
				new PrintWriter(clientSock.getOutputStream(), true);
			
			// Prepare to read from keyboard
			BufferedReader fromUserReader = new BufferedReader(
				new InputStreamReader(System.in));

			// No other clients, close the server socket
			serverSock.close();

			// Prepare to read from client
			BufferedReader fromClientReader = new BufferedReader(
					new InputStreamReader(clientSock.getInputStream()));

			// Keep serving the client
			while (true) {
				// Read a message from the client
				String message = fromClientReader.readLine();

				// If we get null, it means client sent EOF
				if (message == null) {
					System.out.println("Client closed connection");
					clientSock.close();
					break;
				}

				// Display the message
				System.out.println("Client: " + message);

				// Read a line from the keyboard
				String line = fromUserReader.readLine();

				// Send the line to the client
				toClientWriter.println(line);

				// We must receive from client BEFORE we send to client
			}
		}
		catch(Exception e) {
			// Print the exception message
			System.out.println(e);
		}
	}
}
