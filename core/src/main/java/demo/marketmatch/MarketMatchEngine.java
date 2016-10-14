package demo.marketmatch;

import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.store.MarketMatchStore;
import demo.marketmatch.store.MarketMatchWaitingQueuePair;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchEngine {
    private static final MarketMatchEngine ENGINE = new MarketMatchEngine();

    private MarketMatchStore store = new MarketMatchStore();

    public static MarketMatchEngine getInstance() {
        return ENGINE;
    }

    private MarketMatchEngine() {
    }

    public void receiveAndMatch(MarketMatchOrder order) {
        String pid = order.getPid();
        MarketMatchWaitingQueuePair queuePair = store.getQueue(pid);
        MarketMatchViewer.getInstance().refreshView(pid, queuePair.match(order));
    }
}
