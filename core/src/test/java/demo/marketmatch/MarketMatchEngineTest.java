package demo.marketmatch;

import com.alibaba.fastjson.JSONObject;
import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.util.RandomPriceGenerator;
import demo.marketmatch.constants.MarketMatchType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static demo.marketmatch.constants.MarketMatchDirect.BUY;
import static demo.marketmatch.constants.MarketMatchDirect.SELL;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchEngineTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketMatchEngineTest.class);

    private static final int TEST_COUNT = 1000;
    private static final int USER_SIZE = 300;
    private static final Random RANDOM = new Random();
    private static final int BASIC_LIMIT_PRICE = 23003;
    private static final int BASIC_VOLUME = 100;
    private static final int VOLUME_RANGE = 100;

    @Test
    public void testReceiveAndMatch() throws Exception {
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
        MarketMatchEngine engine = new MarketMatchEngine(new MarketMatchViewer());
        MarketMatchOrder order = JSONObject.parseObject(json, MarketMatchOrder.class);
        engine.receiveAndMatch(order);
    }

    @Test
    public void testRandomReceiveAndMatch() throws Exception {
        MarketMatchEngine engine = new MarketMatchEngine(new MarketMatchViewer());
        RandomPriceGenerator generator = new RandomPriceGenerator(BASIC_LIMIT_PRICE, RANDOM);
        JSONObject jsonObject;
        MarketMatchOrder order;
        for (int i = 0; i < TEST_COUNT; i++) {
            jsonObject = new JSONObject();
            jsonObject.put(PID.jsonKeyName(), "Au(T+D)");
            jsonObject.put(CID.jsonKeyName(), "demo" + i % (RANDOM.nextInt(USER_SIZE) + 1));
            jsonObject.put(DIRECT.jsonKeyName(), RANDOM.nextBoolean() ? BUY.name() : SELL.name());
            jsonObject.put(ORDER_TYPE.jsonKeyName(), MarketMatchType.LIMIT.name());
            jsonObject.put(LIMIT_PRICE.jsonKeyName(), generator.randomPrice());
            jsonObject.put(VOLUME.jsonKeyName(), (RANDOM.nextInt(VOLUME_RANGE) + 1) * BASIC_VOLUME);
            jsonObject.put(TIMESTAMP.jsonKeyName(), System.currentTimeMillis());
            order = JSONObject.parseObject(jsonObject.toJSONString(), MarketMatchOrder.class);
            engine.receiveAndMatch(order);
        }
    }

}