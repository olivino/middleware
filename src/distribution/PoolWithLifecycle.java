package distribution;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PoolWithLifecycle
{
    private final int nThreads;
    private final ArrayList<PoolWorker> threads;
    private final LinkedList queue;
    private final long lifeCycleTime;
    
    private FileWriter log;
    private FileWriter logThreadtask;
    private int createdTotal;
    
    public PoolWithLifecycle(int nThreads, long lifeCycleTime) throws IOException
    {
    	this.lifeCycleTime = lifeCycleTime;
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new ArrayList<PoolWorker>();
        this.log = new FileWriter("logServer.txt",true);
        log.write("poolSize#queueSizer#createdNow#createdTotal\n");
        log.close();
        
        this.logThreadtask = new FileWriter("logThreadTask.txt",true);
        logThreadtask.write("threadWhoTakeTask\n");
        logThreadtask.close();
        
        this.createdTotal = nThreads;
        
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

    public void execute(Runnable r) throws IOException {
    	int createdNow = 0;

        synchronized(queue) {
        	int poolSize = threads.size();         	
        	if( poolSize == 0){
        		this.addMoreThreads(nThreads);
        		createdNow = nThreads;
        	}
        	else if (poolSize < queue.size() + poolSize*0.9){
        		this.addMoreThreads(nThreads);
        		createdNow = nThreads;
        	}
            queue.addLast(r);
            queue.notify();
            if(createdNow > 0){
            	createdTotal += nThreads;
            }
            this.log = new FileWriter("logServer.txt",true);
            log.write(threads.size()+"#"+queue.size()+"#"+createdNow+"#"+createdTotal+"\n");
            log.close();
            
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
            FileWriter logThreadtask;
            
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
                    
                    try {
						logThreadtask = new FileWriter("logThreadTask.txt",true);
						logThreadtask.write(this.getId()+"\n");
	                    logThreadtask.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
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
