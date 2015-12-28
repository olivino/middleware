package distribution;

import java.io.IOException;
import java.rmi.RemoteException;

import infrastructure.ServerRequestHandler;

public class ThreadedRemoteObjectInvolker implements Runnable{
	
	ServerRequestHandler srh;
	byte[] msgToBeUnmarshalled = null;
	byte[] msgMarshalled = null;
	Message msgUnmarshalled = new Message();
	Marshaller mrsh = new Marshaller();
	Termination ter = new Termination();
	RemoteObject rObj = null;


	public ThreadedRemoteObjectInvolker( ServerRequestHandler srh,byte[] msgToBeUnmarshalled ) throws RemoteException {
		// TODO Auto-generated constructor stub
		this.srh = srh;
		this.msgToBeUnmarshalled = msgToBeUnmarshalled;
		this.rObj = new RemoteObject();
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		// @ Unmarshall received message
		msgUnmarshalled = mrsh.unmarshall(msgToBeUnmarshalled);

		switch (msgUnmarshalled.getBody().getRequestHeader().getOperation()) {
		case "doSomeThing":
			ter.setResult(rObj.doSomeThing());

			Message _add_msgToBeMarshalled = new Message(new MessageHeader(
					"protocolo", 0, false, 0, 0), new MessageBody(null,
							null, new ReplyHeader("", 0, 0), new ReplyBody(
									ter.getResult())));

			// @ Marshall the response
			try {
				msgMarshalled = mrsh.marshall(_add_msgToBeMarshalled);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// @ Send response
			try {
				srh.send(msgMarshalled);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
	}
	
}