package demo.marketmatch.store;

import com.alibaba.fastjson.JSONObject;
import demo.marketmatch.constants.MarketMatchDirect;
import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.domain.MarketMatchTrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
        List<MarketMatchTrade> matchedTrade;
        switch (direct) {
            case BUY:
                matchedTrade = matchOrWait(order, waitingSellQueue, waitingBuyQueue);
                break;
            case SELL:
                matchedTrade = matchOrWait(order, waitingBuyQueue, waitingSellQueue);
                break;
            default:
                matchedTrade = new ArrayList<>();
                LOGGER.warn("[{}] is unknown direct", direct);
        }
        return composeJson(waitingBuyQueue, waitingSellQueue, matchedTrade);
    }

    private List<MarketMatchTrade> matchOrWait(MarketMatchOrder order, MarketMatchWaitingQueue matchQueue, MarketMatchWaitingQueue waitingQueue) {
        List<MarketMatchTrade> list = new ArrayList<>();
        boolean fullMatched = matchQueue.matchAndStrike(order, list);
        if (!fullMatched) {
            waitingQueue.wait(order);
        }
        return list;
    }

    private JSONObject composeJson(MarketMatchWaitingQueue waitingBuyQueue, MarketMatchWaitingQueue waitingSellQueue, List<MarketMatchTrade> matchedTrade) {
        JSONObject retJson = new JSONObject();
        retJson.put("buyLines", waitingBuyQueue.print());
        retJson.put("sellLines", waitingSellQueue.print());
        retJson.put("matched", matchedTrade);
        return retJson;
    }
}
