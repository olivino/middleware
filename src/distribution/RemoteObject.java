package distribution;

import java.rmi.RemoteException;
import java.util.Random;

import cern.jet.random.Poisson;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

public class RemoteObject implements IRemoteObject{
	
	private static final long serialVersionUID = 1L;
	private static long wait_time = 10;
	
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
		int poissonValue = 0;
		try {
			/*Variaveis do Calculo estatistico - Poisson */
			Random rand = new Random(System.nanoTime());
			RandomEngine engine = new DRand(rand.nextInt());
			int lambda = (int)wait_time;
			Poisson poisson = new Poisson(lambda, engine);
			poissonValue = poisson.nextInt();
			Thread.sleep(poissonValue);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "processing not completed";
		}
		
		return "processing completed using#"+ poissonValue;


	}

}
