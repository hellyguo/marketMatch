package demo.marketmatch;

import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.store.MarketMatchStore;
import demo.marketmatch.store.MarketMatchWaitingQueuePair;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchEngine {
    private MarketMatchStore store = new MarketMatchStore();
    private MarketMatchViewer viewer;

    public MarketMatchEngine(MarketMatchViewer viewer) {
        this.viewer = viewer;
    }

    public void receiveAndMatch(MarketMatchOrder order) {
        String pid = order.getPid();
        MarketMatchWaitingQueuePair queuePair = store.getQueue(pid);
        viewer.refreshView(pid, queuePair.match(order));
    }
}
