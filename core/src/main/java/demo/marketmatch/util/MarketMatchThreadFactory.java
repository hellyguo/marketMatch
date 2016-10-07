package demo.marketmatch.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchThreadFactory implements ThreadFactory {
    private AtomicLong index = new AtomicLong(0);
    private String prefix = "MarketMatch";

    public MarketMatchThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread th = new Thread(r, prefix + '-' + index.getAndIncrement());
        th.setDaemon(true);
        return th;
    }
}
