package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientRequestHandler {
	private String host;
	private int port;
	private int sentMessageSize;
	private int receiveMessageSize;

	private Socket clientSocket = null;
	private DataOutputStream outToServer = null;
	private DataInputStream inFromServer = null;

	public ClientRequestHandler(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void send(byte [] msg) throws IOException, InterruptedException {

		clientSocket = new Socket(this.host, this.port);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new DataInputStream(clientSocket.getInputStream());

		sentMessageSize = msg.length;
		outToServer.writeInt(sentMessageSize);
		outToServer.write(msg,0,sentMessageSize);
		outToServer.flush();

		return;
	}

	public byte [] receive() throws IOException, InterruptedException,
			ClassNotFoundException {

		byte[] msg = null;
		
		receiveMessageSize = inFromServer.readInt();
		msg = new byte[receiveMessageSize];
		inFromServer.read(msg, 0, receiveMessageSize);
				
		clientSocket.close();
		outToServer.close();
		inFromServer.close();

		return msg;
	}

	public int getSentMessageSize() {
		return sentMessageSize;
	}

	public void setSentMessageSize(int sentMessageSize) {
		this.sentMessageSize = sentMessageSize;
	}

	public int getReceiveMessageSize() {
		return receiveMessageSize;
	}

	public void setReceiveMessageSize(int receiveMessageSize) {
		this.receiveMessageSize = receiveMessageSize;
	}

}