package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRequestHandler {
	private int portNumber;
	private ServerSocket welcomeSocket = null;
	private Socket connectionSocket = null;

	private int sentMessageSize;
	private int receivedMessageSize;
	private DataOutputStream outToClient = null;
	private DataInputStream inFromClient = null;

	public ServerRequestHandler(int port) {
		this.portNumber = port;
	}

	public byte [] receive() throws IOException, Throwable {

		byte [] rcvMsg = null;
		
		welcomeSocket = new ServerSocket(portNumber);
		//System.out.println("Criado novo socket de boas vindas");
		connectionSocket = welcomeSocket.accept();
		//System.out.println("Cliente conectado");
		welcomeSocket.close();
		
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		inFromClient = new DataInputStream(connectionSocket.getInputStream());

		receivedMessageSize = inFromClient.readInt();
		rcvMsg = new byte[receivedMessageSize];
		
		inFromClient.read(rcvMsg, 0, receivedMessageSize);
				
		return rcvMsg;
	}

	public void send(byte [] msg) throws IOException, InterruptedException {
		
		sentMessageSize = msg.length;
		outToClient.writeInt(sentMessageSize);
		outToClient.write(msg);
		outToClient.flush();

		connectionSocket.close();
		//welcomeSocket.close();
		outToClient.close();
		inFromClient.close();

		return;
	}

	public int getSentMessageSize() {
		return sentMessageSize;
	}

	public void setSentMessageSize(int sentMessageSize) {
		this.sentMessageSize = sentMessageSize;
	}

	public int getReceivedMessageSize() {
		return receivedMessageSize;
	}

	public void setReceivedMessageSize(int receivedMessageSize) {
		this.receivedMessageSize = receivedMessageSize;
	}

}
