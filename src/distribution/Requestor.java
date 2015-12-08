package distribution;


import java.io.IOException;
import java.net.UnknownHostException;

import infrastructure.ClientRequestHandler;

public class Requestor {

	public Termination invoke(Invocation inv) throws UnknownHostException,
			IOException, Throwable {
		ClientRequestHandler crh = new ClientRequestHandler(inv
				.getClientProxy().getHost(), inv.getClientProxy().getPort());
		Marshaller marshaller = new Marshaller();
		Termination termination = new Termination();
		byte [] msgMarshalled = new byte [1000];
		byte [] msgToBeUnmarshalled = new byte [1000];
		Message msgUnmarshalled = new Message(); // TODO actual marshalling

		// map Invocation into a Message
		RequestHeader requestHeader = new RequestHeader("", 0, true, 0,
				inv.getOperationName());
		RequestBody requestBody = new RequestBody(inv.getParameters());
		MessageHeader messageHeader = new MessageHeader("MIOP", 0, false, 0, 0);
		MessageBody messageBody = new MessageBody(requestHeader, requestBody,
				null, null);
		Message msgToBeMarshalled = new Message(messageHeader, messageBody);

		// marshall request message
		msgMarshalled = marshaller.marshall(msgToBeMarshalled);

		// send marshalled message
		crh.send(msgMarshalled);

		// receive reply message
		msgToBeUnmarshalled = crh.receive();

		// unmarshall reply message
		msgUnmarshalled = (Message) marshaller.unmarshall(msgToBeUnmarshalled);

		// return result to Client Proxy
		termination.setResult(msgUnmarshalled.getBody().getReplyBody()
				.getOperationResult());

		return termination;
	}
}
