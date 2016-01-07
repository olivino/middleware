package application;

import java.io.FileWriter;
import java.io.IOException;

import commonservices.naming.NamingProxy;
import distribution.RemoteObjectProxy;

public class RemoteObjectClientThread implements Runnable {
	
	private FileWriter fw;
	private int index;
	private int poissonNumber;
	
	
	public RemoteObjectClientThread(FileWriter fw,int index, int poissonNumber) {
		// TODO Auto-generated constructor stub
		this.fw = fw;
		this.index = index;
		this.poissonNumber = poissonNumber;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String result = "";
		RemoteObjectProxy remoteObjectProxy;
		long startTime,totalTime;
		
		NamingProxy namingService = new NamingProxy("localhost", 1313);

		
		
		startTime = System.nanoTime();
		
		try {
			// look for Calculator in Naming service
			remoteObjectProxy = (RemoteObjectProxy) namingService.lookup("RemoteObject");
			//call remote doSomeThing
			result = remoteObjectProxy.doSomeThing();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		totalTime = System.nanoTime() - startTime;
		totalTime /= 10E6; 
		
		synchronized (fw) {
			try {
				fw.write(index+"#"+poissonNumber+"#"+totalTime+"#"+result+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}