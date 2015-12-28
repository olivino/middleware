package application;

import java.io.IOException;

import commonservices.naming.NamingProxy;
import distribution.RemoteObjectInvoker;
import distribution.RemoteObjectProxy;


public class RemoteObjectServer {
	
	public static void main(String[] args) throws IOException, Throwable {
		RemoteObjectInvoker invoker = new RemoteObjectInvoker();

		// remote object
		RemoteObjectProxy remoteObject = new RemoteObjectProxy();

		// obtain instance of Naming Service
		NamingProxy namingService = new NamingProxy("localhost", 1313);

		// register calculator in Naming service
		namingService.bind("RemoteObject", remoteObject);
		
		System.out.println("RemoteObjectServer ready...");
		
		// invoke Invoker
		invoker.invoke(remoteObject);
		
	}
}

