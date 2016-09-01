package api.common.net.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/1/14.
 */
public class PriorityThreadFactory implements ThreadFactory {

    private final int mPriority;
    private final AtomicInteger mNumber = new AtomicInteger();
    private final String mName;

    /***
     * 线程名称和线程优先级
     */
    public PriorityThreadFactory(String name, int priority) {
        mName = name;
        mPriority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, mName + '-' + mNumber.getAndIncrement()) {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(mPriority);
                super.run();
            }
        };
    }
}
