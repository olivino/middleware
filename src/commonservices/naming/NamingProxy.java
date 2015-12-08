package commonservices.naming;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import distribution.ClientProxy;
import distribution.Invocation;
import distribution.Requestor;
import distribution.Termination;

public class NamingProxy extends ClientProxy implements INaming {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NamingProxy(String h, int p){
		this.host = h;
		this.port = p;	
	}

	public void bind(String serviceName, ClientProxy clientProxy) throws UnknownHostException, IOException, Throwable {
		Invocation inv = new Invocation();
		new Termination();
		ArrayList<Object> parameters = new ArrayList<Object>();
		class Local {
		}
		;
		String methodName;
		Requestor requestor = new Requestor();

		// information received from Client
		methodName = Local.class.getEnclosingMethod().getName();
		parameters.add(serviceName);
		parameters.add(clientProxy);

		// information sent to Requestor
		inv.setClientProxy(this); 
		inv.setOperationName(methodName);
		inv.setParameters(parameters);

		requestor.invoke(inv);

		// @ Result sent back to Client
		return;
	}

	public ClientProxy lookup(String serviceName) throws UnknownHostException, IOException, Throwable {
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
		parameters.add(serviceName);

		// information sent to Requestor
		inv.setClientProxy(this); 
		inv.setOperationName(methodName);
		inv.setParameters(parameters);

		// invoke Requestor
		ter = requestor.invoke(inv);
		
		// @ Result sent back to Client
		return (ClientProxy) ter.getResult();
	}

	@Override
	public ArrayList<String> list() throws UnknownHostException, IOException, Throwable {
		Invocation inv = new Invocation();
		Termination ter = new Termination();
		ArrayList<Object> parameters = new ArrayList<Object>(0);
		class Local {
		}
		;
		String methodName;
		Requestor requestor = new Requestor();

		// information received from Client
		methodName = Local.class.getEnclosingMethod().getName();

		// information sent to Requestor
		inv.setClientProxy(this); 
		inv.setOperationName(methodName);
		inv.setParameters(parameters);

		// invoke Requestor
		ter = requestor.invoke(inv);

		// result sent back to Client
		@SuppressWarnings("unchecked")
		ArrayList<String> result = (ArrayList<String>) ter.getResult();
		
		return result;
	}
}
