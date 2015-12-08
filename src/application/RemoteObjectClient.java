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

		// invoke calculator
		//calculatorProxy.add(1, 3);

		System.out.println("Resultado da operação: "+remoteObjectProxy.doSomeThing());
	}
}
