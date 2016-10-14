package demo.marketmatch;

import com.alibaba.fastjson.JSONObject;
import demo.marketmatch.constants.MarketMatchType;
import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.util.RandomPriceGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static demo.marketmatch.constants.MarketMatchDirect.BUY;
import static demo.marketmatch.constants.MarketMatchDirect.SELL;
import static demo.marketmatch.constants.MarketMatchJsonKey.*;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchEngineTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketMatchEngineTest.class);

    private static final MarketMatchEngine ENGINE = MarketMatchEngine.getInstance();

    private static final int LOOP_TIMES = 10;
    private static final int WARM_COUNT = 1000;
    private static final int TEST_COUNT = 1000000;
    private static final int USER_SIZE = 300;
    private static final Random RANDOM = new Random();
    private static final int BASIC_LIMIT_PRICE = 23003;
    private static final int BASIC_VOLUME = 100;
    private static final int VOLUME_RANGE = 100;
    private static final int MILLIS_IN_ONE_SEC = 1000;

    private RandomPriceGenerator generator = new RandomPriceGenerator(BASIC_LIMIT_PRICE, RANDOM);

    @Before
    public void setUp() {
        ENGINE.startLoop();
        //warm it
        LOGGER.info("warm JVM");
        timeAndComputeTPS(generator, WARM_COUNT);
        LOGGER.info("warm JVM done");
    }

    @After
    public void tearDown() {
        ENGINE.stopLoop();
    }

    @Test
    public void testReceiveAndMatch() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PID.jsonKeyName(), "Au(T+D)");
        jsonObject.put(CID.jsonKeyName(), "demo1");
        jsonObject.put(DIRECT.jsonKeyName(), BUY.name());
        jsonObject.put(ORDER_TYPE.jsonKeyName(), MarketMatchType.LIMIT.name());
        jsonObject.put(LIMIT_PRICE.jsonKeyName(), BASIC_LIMIT_PRICE);
        jsonObject.put(VOLUME.jsonKeyName(), BASIC_VOLUME);
        jsonObject.put(TIMESTAMP.jsonKeyName(), System.currentTimeMillis());
        String json = jsonObject.toJSONString();
        LOGGER.info(json);
        MarketMatchOrder order = JSONObject.parseObject(json, MarketMatchOrder.class);
        ENGINE.matchOrder(order);
    }

    @Test
    public void testRandomReceiveAndMatch() {
        double totalTPS = 0.0D;
        for (int i = 0; i < LOOP_TIMES; i++) {
            totalTPS += timeAndComputeTPS(generator, TEST_COUNT);
        }
        LOGGER.info("avg TPS={}", totalTPS / LOOP_TIMES);
    }

    private double timeAndComputeTPS(RandomPriceGenerator generator, int count) {
        List<MarketMatchOrder> list = new ArrayList<>();
        JSONObject jsonObject;
        MarketMatchOrder order;
        for (int i = 0; i < count; i++) {
            jsonObject = new JSONObject();
            jsonObject.put(PID.jsonKeyName(), "Au(T+D)");
            jsonObject.put(CID.jsonKeyName(), "demo" + i % (RANDOM.nextInt(USER_SIZE) + 1));
            jsonObject.put(DIRECT.jsonKeyName(), RANDOM.nextBoolean() ? BUY.name() : SELL.name());
            jsonObject.put(ORDER_TYPE.jsonKeyName(), MarketMatchType.LIMIT.name());
            jsonObject.put(LIMIT_PRICE.jsonKeyName(), generator.randomPrice());
            jsonObject.put(VOLUME.jsonKeyName(), (RANDOM.nextInt(VOLUME_RANGE) + 1) * BASIC_VOLUME);
            jsonObject.put(TIMESTAMP.jsonKeyName(), System.currentTimeMillis());
            order = JSONObject.parseObject(jsonObject.toJSONString(), MarketMatchOrder.class);
            list.add(order);
        }
        long start = System.currentTimeMillis();
        ENGINE.matchOrderList(list);
        long end = System.currentTimeMillis();
        double tps = 1.0D * count / ((end - start) / MILLIS_IN_ONE_SEC);
        LOGGER.info("match TPS={}", tps);
        return tps;
    }

}