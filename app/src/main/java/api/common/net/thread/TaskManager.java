package api.common.net.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/1/14.
 */
public class TaskManager {
    private ExecutorService executor;
    //能同时运行的最大任务数
    private final int MAX_NUM_POOL_SIZE = 3;

    private static TaskManager instance;

    private TaskManager() {
        PriorityThreadFactory threadFactory = new PriorityThreadFactory("http-data",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        executor = Executors.newFixedThreadPool(MAX_NUM_POOL_SIZE, threadFactory);
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    /**
     * 提交任务
     */
    public void execute(Runnable task) {
        if (executor != null && !executor.isShutdown()) {
            executor.execute(task);
        }
    }
    public Future<?> submit(Runnable task) {
        Future<?> future=executor.submit(task);
        return future;
    }
    /**
     * 不在接受新任务
     */
    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }

    /**
     * 不在接受新任务,并且试图结束正在进行的任务
     */
    public void shutdownNow() {
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }
}
