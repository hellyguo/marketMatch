package demo.marketmatch.store;

import demo.marketmatch.util.MarketMatchThreadFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchStoreTest {
    @Test
    public void testGetQueue() throws Exception {
        final MarketMatchStore store = new MarketMatchStore();
        ExecutorService pool = Executors.newFixedThreadPool(20, new MarketMatchThreadFactory("store-test"));
        List<Callable<Integer>> targets = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            final int idx = i;
            targets.add(() -> {
                store.getQueue("p" + idx % 3).match(null);
                return 0;
            });
        }
        pool.invokeAll(targets);
        pool.shutdown();
        pool.awaitTermination(10000, TimeUnit.MINUTES);
    }

}