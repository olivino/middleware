package rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import distribution.RemoteObject;

public class RemoteObjectServer {
	
	public RemoteObjectServer() {
		super();
	}

	public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException{
		
		RemoteObject remoteObject = new RemoteObject();
		remoteObject.setWait_time(1000);
		
		Registry registry = LocateRegistry.getRegistry("localhost",2001);
		
		registry.bind("RemoteObject", remoteObject);
		
		System.out.println("Remote Object is ready ...");
		
		while(true) Thread.sleep(2000);
	}

}
