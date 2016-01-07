package distribution;

import java.io.IOException;

import infrastructure.ServerRequestHandler;

public class RemoteObjectInvoker {
	
	public void invoke(ClientProxy clientProxy) throws IOException, Throwable {
		ServerRequestHandler srh = new ServerRequestHandler(
				clientProxy.getPort());
		byte[] msgToBeUnmarshalled = null;

		// create a pool with lifecycle  (initial thread number, life time of a thread)
		PoolWithLifecycle poolWithLifecycle = new PoolWithLifecycle(2,1000000);
		
		System.out.println("start Loop");
		
		//set time
		RemoteObject Robj = new RemoteObject();
		Robj.setWait_time(500);
		
		// inversion loop
		while (true) {

			// @ Receive Message
			msgToBeUnmarshalled = srh.receive();
			//System.out.println("recebida solicitacao para objetoRemoto");
			
			ThreadedRemoteObjectInvolker threadedRemoteObjectInvolker = new ThreadedRemoteObjectInvolker(srh, msgToBeUnmarshalled);
			
			poolWithLifecycle.execute(threadedRemoteObjectInvolker);
			
		}
	}

}
