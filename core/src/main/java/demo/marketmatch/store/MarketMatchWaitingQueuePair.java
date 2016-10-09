package demo.marketmatch.store;

import com.alibaba.fastjson.JSONObject;
import demo.marketmatch.constants.MarketMatchDirect;
import demo.marketmatch.domain.MarketMatchOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static demo.marketmatch.constants.MarketMatchDirect.BUY;
import static demo.marketmatch.constants.MarketMatchDirect.SELL;


/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchWaitingQueuePair {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketMatchWaitingQueuePair.class);
    private MarketMatchWaitingQueue waitingBuyQueue = new MarketMatchWaitingQueue(BUY);
    private MarketMatchWaitingQueue waitingSellQueue = new MarketMatchWaitingQueue(SELL);

    public JSONObject match(MarketMatchOrder order) {
        LOGGER.debug("[{}]", order);
        MarketMatchDirect direct = order.getDirect();
        switch (direct) {
            case BUY:
                matchOrWait(order, waitingSellQueue, waitingBuyQueue);
                break;
            case SELL:
                matchOrWait(order, waitingBuyQueue, waitingSellQueue);
                break;
            default:
                LOGGER.warn("[{}] is unknown direct", direct);
        }
        return composeJson(waitingBuyQueue, waitingSellQueue);
    }

    private void matchOrWait(MarketMatchOrder order, MarketMatchWaitingQueue matchQueue, MarketMatchWaitingQueue waitingQueue) {
        boolean fullMatched = matchQueue.matchAndStrike(order);
        if (!fullMatched) {
            waitingQueue.wait(order);
        }
    }

    private JSONObject composeJson(MarketMatchWaitingQueue waitingBuyQueue, MarketMatchWaitingQueue waitingSellQueue) {
        JSONObject retJson = new JSONObject();
        retJson.put("buyLines", waitingBuyQueue.print());
        retJson.put("sellLines", waitingSellQueue.print());
        return retJson;
    }
}
