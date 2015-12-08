package distribution;

import java.util.ArrayList;

public class Invocation {
	private String operationName;
	private ArrayList<Object> parameters = new ArrayList<Object>();
	private ClientProxy clientProxy;

	public Invocation() {
	};

	public Invocation(String o, ArrayList<Object> p, ClientProxy c) {
		this.operationName = o;
		this.parameters = p;
		this.setClientProxy(c);
	};

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String serviceName) {
		this.operationName = serviceName;
	}

	public ArrayList<Object> getParameters() {
		return this.parameters;
	}

	public void setParameters(ArrayList<Object> p) {
		for (Object m : p) {
			this.parameters.add(m);
		}
	}

	public ClientProxy getClientProxy() {
		return clientProxy;
	}

	public void setClientProxy(ClientProxy clientProxy) {
		this.clientProxy = clientProxy;
	}

}