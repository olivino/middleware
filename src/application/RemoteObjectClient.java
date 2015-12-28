package application;

import java.io.IOException;
import java.net.UnknownHostException;

import commonservices.naming.NamingProxy;
import distribution.RemoteObjectProxy;

public class RemoteObjectClient {

	public static void main(String[] args)throws UnknownHostException,
	IOException, Throwable {

		// create an instance of Naming Service
		NamingProxy namingService = new NamingProxy("localhost", 1313);

		// check registered services
		System.out.println(namingService.list());

		// look for Calculator in Naming service
		RemoteObjectProxy remoteObjectProxy = (RemoteObjectProxy) namingService
				.lookup("RemoteObject");

		System.out.println("objeto encontrado no servidor de nomes na porta "+ remoteObjectProxy.getPort() +" e host: " + remoteObjectProxy.getHost());
		
		// invoke calculator
		//calculatorProxy.add(1, 3);
		System.out.println("Esperando resultado...");
		System.out.println("Resultado da operação: "+remoteObjectProxy.doSomeThing());
	}
}
