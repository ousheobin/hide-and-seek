package ac.kcl.inf.has.simulation.execution;

import ac.kcl.inf.has.agent.player.Agent;

import java.util.concurrent.*;

public class ThreadPool {

    private static ThreadPool instance;

    private ExecutorService threadPool;

    private ThreadPool(){
        int poolSize = Runtime.getRuntime().availableProcessors() * 4;
        threadPool = new ThreadPoolExecutor(poolSize, Integer.MAX_VALUE,
                10L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public static ThreadPool getThreadPool(){
        if(instance == null){
            instance = new ThreadPool();
        }
        return instance;
    }

    public Future submitTask(Runnable runnable){
        return threadPool.submit(runnable);
    }

    public void addNewAgent(Agent agent){
        threadPool.submit(agent);
    }

    public void terminate(){
        instance = null;
        threadPool.shutdown();
    }

}
