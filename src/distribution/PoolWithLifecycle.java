package distribution;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PoolWithLifecycle
{
    private final int nThreads;
    private final ArrayList<PoolWorker> threads;
    private final LinkedList queue;
    private final long lifeCycleTime;

    public PoolWithLifecycle(int nThreads, long lifeCycleTime)
    {
    	this.lifeCycleTime = lifeCycleTime;
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new ArrayList<PoolWorker>();

        for (int i=0; i< nThreads; i++) {
        	PoolWorker onePoolWorker = new PoolWorker();
            threads.add(onePoolWorker);
            threads.get(i).start();
        }
    }
    
    public void addMoreThreads (int nThreads){
    	
    	for (int i = 0; i < nThreads; i++){
    		PoolWorker onePoolWorker = new PoolWorker();
            onePoolWorker.start();
    		threads.add(onePoolWorker);
    	}
    }
    
    public int getPoolSize(){
    	synchronized(queue) {
    		return threads.size();
    	}
    }

    public void execute(Runnable r) {

        synchronized(queue) {
        	int poolSize = threads.size(); 
        	if( poolSize == 0){
        		this.addMoreThreads(nThreads);
        	}
        	else if (poolSize < queue.size() + poolSize*0.9){
        		this.addMoreThreads(poolSize);
        	}
            queue.addLast(r);
            queue.notify();
        }
    }

    private class PoolWorker extends Thread {
    	
    	private long elapsedTime;
    	private long nanoTime;
    	
    	public PoolWorker ( ){
    		super();
    		this.elapsedTime = 0;
    		this.nanoTime = System.nanoTime();
    	}
    	
        public void run() {
            Runnable r;

            while (true) {
                synchronized(queue) {
                	elapsedTime = System.nanoTime() - nanoTime;
                	//System.out.println("Pool size: "+ threads.size()+ " elapsed Time: "+elapsedTime);
                    while (queue.isEmpty()) {
                    	if (elapsedTime > lifeCycleTime){
                    		queue.notify();
                    		threads.remove(this);
                    		return;
                    	}
                        try
                        {
                            queue.wait(lifeCycleTime/10);
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }
                    
                    r = (Runnable) queue.removeFirst();
                }

                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                    r.run();
                    this.nanoTime = System.nanoTime();
                }
                catch (RuntimeException e) {
                    // You might want to log something here
                }
            }
        }
    }
}
