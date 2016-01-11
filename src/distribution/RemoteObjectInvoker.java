package distribution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import infrastructure.ServerRequestHandler;

public class RemoteObjectInvoker {
	
	public void invoke(ClientProxy clientProxy) throws IOException, Throwable {
		ServerRequestHandler srh = new ServerRequestHandler(
				clientProxy.getPort());
		byte[] msgToBeUnmarshalled = null;

		// Get configuration from file
		File configFile = new File("server.conf");
		FileReader fileReader = new FileReader(configFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int processingTime = Integer.parseInt(bufferedReader.readLine());
		int poolStartSize = Integer.parseInt(bufferedReader.readLine());
		int lifeCycleTime = Integer.parseInt(bufferedReader.readLine());
		fileReader.close();
		// create a pool with lifecycle  (initial thread number, life time of a thread)
		PoolWithLifecycle poolWithLifecycle = new PoolWithLifecycle(poolStartSize,lifeCycleTime*1000000);
		
		
		//set time
		RemoteObject Robj = new RemoteObject();
		Robj.setWait_time(processingTime);
		
		System.out.println("start Loop");
		
		// inversion loop
		while (true) {

			// @ Receive Message
			msgToBeUnmarshalled = srh.receive();
			//System.out.println("recebida solicitacao para objetoRemoto");
			
			ThreadedRemoteObjectInvolker threadedRemoteObjectInvolker = new ThreadedRemoteObjectInvolker(srh, msgToBeUnmarshalled);
			
			poolWithLifecycle.execute(threadedRemoteObjectInvolker);
			
			srh = new ServerRequestHandler(clientProxy.getPort());
			
		}
	}

}
