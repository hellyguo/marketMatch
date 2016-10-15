package demo.marketmatch.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by helly on 2016/10/14.
 */
public class MarketMatchOrderBook {
    private List<Map<String, Object>> buyQueue;
    private List<Map<String, Object>> sellQueue;

    public List<Map<String, Object>> getBuyQueue() {
        return buyQueue;
    }

    public void setBuyQueue(List<Map<String, Object>> buyQueue) {
        this.buyQueue = buyQueue;
    }

    public List<Map<String, Object>> getSellQueue() {
        return sellQueue;
    }

    public void setSellQueue(List<Map<String, Object>> sellQueue) {
        this.sellQueue = sellQueue;
    }
}
