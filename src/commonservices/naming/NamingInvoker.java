package commonservices.naming;

import infrastructure.ServerRequestHandler;

import java.io.IOException;

import distribution.*;

public class NamingInvoker {

	public void invoke(int portNamingServer) throws IOException, Throwable {
		ServerRequestHandler srh = new ServerRequestHandler(portNamingServer);
		byte [] msgToBeUnmarshalled;
		byte [] msgMarshalled;
		Message msgUnmarshalled = new Message();
		NamingImpl rObj = new NamingImpl();
		Marshaller mrsh = new Marshaller();
		Termination ter = new Termination();

		while (true) {

			// @ Receive Message	
			msgToBeUnmarshalled = srh.receive();
									
			// @ Unmarshall received message
			msgUnmarshalled = mrsh.unmarshall(msgToBeUnmarshalled);

			switch (msgUnmarshalled.getBody().getRequestHeader().getOperation()
					.trim()) {

			case "bind":

				// @ Invokes the remote object
				String register_p1 = msgUnmarshalled.getBody().getRequestBody()
						.getParameters().get(0).toString();
				ClientProxy register_p2 = (ClientProxy) msgUnmarshalled
						.getBody().getRequestBody().getParameters().get(1);
				rObj.bind(register_p1, register_p2);
				ter.setResult("");

				Message msgToBeMarshalled_register = new Message(
						new MessageHeader("protocolo", 0, false, 0, 0),
						new MessageBody(null, null, new ReplyHeader("", 0, 0),
								new ReplyBody(ter.getResult())));

				// @ Marshall the response
				msgMarshalled = mrsh.marshall(msgToBeMarshalled_register);

				// @ Send response
				srh.send(msgMarshalled);
				break;

			case "lookup":

				// @ Invokes the remote object
				String bind_p1 = msgUnmarshalled.getBody().getRequestBody()
						.getParameters().get(0).toString();
				ter.setResult(rObj.lookup(bind_p1));

				// @ Prepare message to be sent back to client
				Message msgToBeMarshalled_bind = new Message(new MessageHeader(
						"protocolo", 0, false, 0, 0), new MessageBody(null,
						null, new ReplyHeader("", 0, 0), new ReplyBody(
								ter.getResult())));

				// @ Marshall the response
				msgMarshalled = mrsh.marshall(msgToBeMarshalled_bind);

				// @ Send response
				srh.send(msgMarshalled);
				break;
				
			case "list":

				// @ Invokes the remote object
				ter.setResult(rObj.list());

				// @ Prepare message to be sent back to client
				Message msgToBeMarshalled_list = new Message(new MessageHeader(
						"protocolo", 0, false, 0, 0), new MessageBody(null,
						null, new ReplyHeader("", 0, 0), new ReplyBody(
								ter.getResult())));

				// @ Marshall the response
				msgMarshalled = mrsh.marshall(msgToBeMarshalled_list);

				// @ Send response
				srh.send(msgMarshalled);
				break;
			}
		}
	}
}
