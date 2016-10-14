package demo.marketmatch.util;

import com.alibaba.fastjson.JSONObject;
import demo.marketmatch.domain.MarketMatchOrder;

import java.util.Random;

import static demo.marketmatch.constants.MarketMatchDirect.BUY;
import static demo.marketmatch.constants.MarketMatchDirect.SELL;
import static demo.marketmatch.constants.MarketMatchJsonKey.*;
import static demo.marketmatch.constants.MarketMatchType.LIMIT;

/**
 * Created by helly on 2016/10/14.
 */
public class RandomOrderGenerator {

    private static final Random RANDOM = new Random();
    private static final String USER_PREFIX = "demo";
    private static final int USER_RANGE = 10;
    private static final int BASIC_LIMIT_PRICE = 23003;
    private static final int BASIC_VOLUME = 100;
    private static final int VOLUME_RANGE = 100;

    private static final RandomPriceGenerator GENERATOR = new RandomPriceGenerator(BASIC_LIMIT_PRICE);

    public static MarketMatchOrder createRandomOrder(String data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PID.jsonKeyName(), data);
        jsonObject.put(CID.jsonKeyName(), USER_PREFIX + RANDOM.nextInt(USER_RANGE));
        jsonObject.put(DIRECT.jsonKeyName(), RANDOM.nextBoolean() ? BUY.name() : SELL.name());
        jsonObject.put(ORDER_TYPE.jsonKeyName(), LIMIT.name());
        jsonObject.put(LIMIT_PRICE.jsonKeyName(), GENERATOR.randomPrice());
        jsonObject.put(VOLUME.jsonKeyName(), (RANDOM.nextInt(VOLUME_RANGE) + 1) * BASIC_VOLUME);
        jsonObject.put(TIMESTAMP.jsonKeyName(), System.currentTimeMillis());
        return JSONObject.parseObject(jsonObject.toJSONString(), MarketMatchOrder.class);
    }
}
