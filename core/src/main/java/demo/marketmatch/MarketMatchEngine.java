package demo.marketmatch;

import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.store.MarketMatchStore;
import demo.marketmatch.store.MarketMatchWaitingQueuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchEngine {
    private static final MarketMatchEngine ENGINE = new MarketMatchEngine();

    private MarketMatchStore store = new MarketMatchStore();

    private AtomicBoolean active = new AtomicBoolean(true);
    private ReentrantLock lock = new ReentrantLock();
    private Condition signal = lock.newCondition();
    private List<MarketMatchOrder> orderList = new ArrayList<>();

    private Thread loopThread;

    public static MarketMatchEngine getInstance() {
        return ENGINE;
    }

    private MarketMatchEngine() {
    }

    public void postOrder(MarketMatchOrder order) {
        try {
            lock.lock();
            orderList.add(order);
            signal.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void fetchOrder() {
        List<MarketMatchOrder> fetchedOrder;
        while (active.get()) {
            fetchedOrder = new ArrayList<>();
            try {
                lock.lock();
                if (orderList.isEmpty()) {
                    signal.await();
                }
                fetchedOrder.addAll(orderList);
                orderList.clear();
            } catch (InterruptedException e) {
                continue;
            } finally {
                lock.unlock();
            }
            if (!fetchedOrder.isEmpty()) {
                matchOrderList(fetchedOrder);
            }
        }
    }

    void matchOrderList(List<MarketMatchOrder> fetchedOrder) {
        fetchedOrder.forEach(this::receiveAndMatch);
    }

    void matchOrder(MarketMatchOrder fetchedOrder) {
        receiveAndMatch(fetchedOrder);
    }

    private void receiveAndMatch(MarketMatchOrder order) {
        String pid = order.getPid();
        MarketMatchWaitingQueuePair queuePair = store.getQueue(pid);
        queuePair.match(order);
    }

    public void startLoop() {
        loopThread = new Thread(this::fetchOrder, "matchEngineLoop");
        loopThread.setDaemon(true);
        loopThread.start();
    }

    public void stopLoop() {
        active.set(false);
        try {
            lock.lock();
            signal.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
