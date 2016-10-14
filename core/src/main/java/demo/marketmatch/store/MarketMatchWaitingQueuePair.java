package demo.marketmatch.store;

import demo.marketmatch.MarketMatchViewer;
import demo.marketmatch.constants.MarketMatchDirect;
import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.domain.MarketMatchOrderBook;
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

    public void match(MarketMatchOrder order) {
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
        pushToView(order, waitingBuyQueue, waitingSellQueue, matchedTrade);
    }

    private List<MarketMatchTrade> matchOrWait(MarketMatchOrder order, MarketMatchWaitingQueue matchQueue, MarketMatchWaitingQueue waitingQueue) {
        List<MarketMatchTrade> list = new ArrayList<>();
        boolean fullMatched = matchQueue.matchAndStrike(order, list);
        if (!fullMatched) {
            waitingQueue.wait(order);
        }
        return list;
    }

    private void pushToView(MarketMatchOrder order, MarketMatchWaitingQueue waitingBuyQueue, MarketMatchWaitingQueue waitingSellQueue, List<MarketMatchTrade> matchedTrade) {
        MarketMatchViewer viewer = MarketMatchViewer.getInstance();
        String pid = order.getPid();
        MarketMatchOrderBook book = new MarketMatchOrderBook();
        book.setBuyQueue(waitingBuyQueue.print());
        book.setSellQueue(waitingSellQueue.print());
        viewer.pushBook(pid, book);
        viewer.pushOrder(pid, order);
        viewer.pushTrade(pid, matchedTrade);
    }
}
