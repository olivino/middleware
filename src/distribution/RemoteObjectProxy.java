package distribution;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import utilsconf.UtilsConf;

public class RemoteObjectProxy extends ClientProxy implements IRemoteObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public RemoteObjectProxy() throws UnknownHostException {
		// TODO Auto-generated constructor stub
		super();
		this.host = InetAddress.getLocalHost().getHostName();
		this.port = UtilsConf.nextPortAvailable();
	}
	
	public RemoteObjectProxy (String h, int p) {
		this.host = h;
		this.port = p;
	}
	

	@Override
	public String doSomeThing() throws Throwable{
		// TODO Auto-generated method stub
		Invocation inv = new Invocation();
		Termination ter = new Termination();
		ArrayList<Object> parameters = new ArrayList<Object>();
		class Local {
		}
		;
		String methodName;
		Requestor requestor = new Requestor();

		// information received from Client
		methodName = Local.class.getEnclosingMethod().getName();
		//parameters.add(x);
		//parameters.add(y);

		// information sent to Requestor
		inv.setClientProxy(this);
		inv.setOperationName(methodName);
		inv.setParameters(parameters);

		// invoke Requestor
		ter = requestor.invoke(inv);

		// @ Result sent back to Client
		return (String) ter.getResult();

	}

	@Override
	public void setWait_time(long wait_time) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getWait_time() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
