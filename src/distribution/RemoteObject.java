package distribution;

import java.rmi.RemoteException;

public class RemoteObject implements IRemoteObject{
	
	private static final long serialVersionUID = 1L;
	private static long wait_time;
	
	public RemoteObject () throws RemoteException  {
		super();
		
	}	
	
	public long getWait_time() {
		return wait_time;
	}

	public void setWait_time(long wait_time) {
		RemoteObject.wait_time = wait_time;
	}



	public String doSomeThing(){
		
		try {
			Thread.sleep(wait_time);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "processing not completed";
		}
		
		return "processing completed";


	}

}
