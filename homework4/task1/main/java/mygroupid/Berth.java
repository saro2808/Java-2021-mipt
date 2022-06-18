package mygroupid;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Berth {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private final Goods goods;

    public Berth(Goods goods) {
        this.goods = goods;
    }

    /**
     * Submits task in the executor
     * @param task the task to be submitted
     * @return executorService.submit(task)
     */
    public Future<?> executorSubmit(Runnable task) {
        return executorService.submit(task);
    }

    /**
     * Shuts the executor down
     */
    public void executorShutdown() {
        executorService.shutdown();
    }
}

