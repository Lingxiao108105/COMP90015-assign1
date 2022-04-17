package common.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author lingxiao li 1031146
 *
 * basic thread pool which will auto-reduce the thread pool when throughput is low
 *
 */
public class CustomThreadPool {

    private final BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();
    private final ConcurrentHashMap<Integer,Worker> workers = new ConcurrentHashMap<>();
    private Integer workerId = 1;
    private final static int maxWorkerCount = 100;

    public void execute(Runnable task) {
        if(queue.size() > 0 && (workers.size() ==0 || workers.size() < maxWorkerCount)){
            Worker worker = new Worker(workerId);
            worker.start();
            workers.put(workerId,worker);
            workerId ++;

        }
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            System.out.println("Fail to add task" + task.toString());
        }
    }

    public void shutDown() throws InterruptedException {
        for(Worker worker: workers.values()){
            worker.interrupt();
        }

        for(Worker worker: workers.values()){
            worker.join();
        }

        System.out.println("All the workers are shut down!");
    }

    private class Worker extends Thread{

        public Worker(int id) {
            //set id
            Thread.currentThread().setName(Integer.toString(id));
        }

        @Override
        public void run() {
            try{
                while(!Thread.currentThread().isInterrupted()){
                    //wait 10 second
                    Runnable task = queue.poll(10, TimeUnit.SECONDS);
                    //not enough task to run
                    if(task == null){
                        System.out.println(Thread.currentThread().getName() + "is interrupted!");
                        workers.remove(Integer.parseInt(Thread.currentThread().getName()));
                        this.interrupt();
                        break;
                    }
                    task.run();
                }
            }
            catch (InterruptedException e){
                System.out.println(Thread.currentThread().getName() + "is interrupted!");
                //remove it from the thread pool
                workers.remove(Integer.parseInt(Thread.currentThread().getName()));
            }
        }
    }



}
