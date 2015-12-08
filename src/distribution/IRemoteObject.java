package distribution;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteObject extends Remote, Serializable{
	
	String doSomeThing() throws RemoteException, Throwable;
	void setWait_time(long wait_time) throws RemoteException;
	long getWait_time() throws RemoteException;
}
