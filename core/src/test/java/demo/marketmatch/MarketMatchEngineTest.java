package demo.marketmatch;

import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.util.RandomOrderGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchEngineTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketMatchEngineTest.class);

    private static final MarketMatchEngine ENGINE = MarketMatchEngine.getInstance();

    private static final int LOOP_TIMES = 10;
    private static final int WARM_COUNT = 1000;
    private static final int TEST_COUNT = 1000000;
    private static final int MILLIS_IN_ONE_SEC = 1000;

    @Before
    public void setUp() {
        ENGINE.startLoop();
        //warm it
        LOGGER.info("warm JVM");
        timeAndComputeTPS(WARM_COUNT);
        LOGGER.info("warm JVM done");
    }

    @After
    public void tearDown() {
        ENGINE.stopLoop();
    }

    @Test
    public void testReceiveAndMatch() {
        ENGINE.matchOrder(RandomOrderGenerator.createRandomOrder("Au(T+D)"));
    }

    @Test
    public void testRandomReceiveAndMatch() {
        double totalTPS = 0.0D;
        for (int i = 0; i < LOOP_TIMES; i++) {
            totalTPS += timeAndComputeTPS(TEST_COUNT);
        }
        LOGGER.info("avg TPS={}", totalTPS / LOOP_TIMES);
    }

    private double timeAndComputeTPS(int count) {
        List<MarketMatchOrder> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(RandomOrderGenerator.createRandomOrder("Au(T+D)"));
        }
        long start = System.currentTimeMillis();
        ENGINE.matchOrderList(list);
        long end = System.currentTimeMillis();
        double tps = 1.0D * count / ((end - start) / MILLIS_IN_ONE_SEC);
        LOGGER.info("match TPS={}", tps);
        return tps;
    }

}