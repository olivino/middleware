package commonservices.naming;

import distribution.ClientProxy;

public class NamingRecord {
	private String serviceName;
	private ClientProxy clientProxy;

	public NamingRecord(final String s, final int oId, final  ClientProxy clientProxy){
		this.clientProxy = clientProxy;
		this.serviceName = s;
	}
	public ClientProxy getObj() {
		return clientProxy;
	}

	public void setObj(ClientProxy obj) {
		this.clientProxy = obj;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
