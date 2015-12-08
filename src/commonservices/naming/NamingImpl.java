package commonservices.naming;

import java.util.ArrayList;

import distribution.ClientProxy;

public class NamingImpl implements INaming {

	private String host;
	private int port;
	
	private ArrayList<NamingRecord> namingRepository = new ArrayList<NamingRecord>();

	public NamingImpl() {
	}

	public void bind(String serviceName, ClientProxy clientProxy) {
		NamingRecord namingRecord = new NamingRecord(serviceName, 1313,clientProxy); // TODO 1313

		this.namingRepository.add(namingRecord);

		return;
	}

	public ClientProxy lookup(String serviceName) {
		ClientProxy clientProxy = new ClientProxy();

		for (NamingRecord r : this.namingRepository) {
			if (r.getServiceName().contains(serviceName)) {
				clientProxy = r.getObj();
				break;
			}
		}

		return clientProxy;
	}

	@Override
	public ArrayList<String> list() {
		ArrayList<String> r = new ArrayList<String>(0);
		
		for(NamingRecord nr : this.namingRepository)
		   r.add(nr.getServiceName());
		   
		return r;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
