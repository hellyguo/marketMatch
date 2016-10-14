package demo.marketmatch.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import demo.marketmatch.constants.MarketMatchDirect;
import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.domain.MarketMatchTrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static demo.marketmatch.constants.MarketMatchDirect.SELL;

/**
 * Created by helly on 2016/9/30.
 */
class MarketMatchWaitingQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketMatchWaitingQueue.class);
    private static final int MAX_SIZE = 5;
    private static final int MAX_DISTANCE = 100;

    private int topPrice = 0;
    private MarketMatchDirect direct;
    private LinkedHashMap<Integer, LinkedList<MarketMatchOrder>> data = new LinkedHashMap<>(1000, 0.75f, true);

    MarketMatchWaitingQueue(MarketMatchDirect direct) {
        this.direct = direct;
        switch (direct) {
            case BUY:
                topPrice = 0;
                break;
            case SELL:
                topPrice = Integer.MAX_VALUE;
                break;
            default:
                throw new RuntimeException("unknown direct");
        }
    }

    /**
     * match the order and strike the bargain
     *
     * @param order order info
     * @return true, if full matched and has no order left; false if not full matched and has order left
     */
    boolean matchAndStrike(MarketMatchOrder order, List<MarketMatchTrade> matchedTrade) {
        if (direct.isFit(topPrice, order.getLimitPrice())) {
            LinkedList<MarketMatchOrder> orderList;
            Iterator<MarketMatchOrder> iterator;
            MarketMatchOrder waitingOrder;
            for (int pointToTopPrice = topPrice;
                 direct.isFit(pointToTopPrice, order.getLimitPrice());
                 pointToTopPrice = direct.nextTopPrice(pointToTopPrice)) {
                orderList = data.get(pointToTopPrice);
                if (orderList == null) {
                    continue;
                }
                topPrice = pointToTopPrice;
                iterator = orderList.iterator();
                while (iterator.hasNext()) {
                    waitingOrder = iterator.next();
                    matchedTrade.add(match(order, waitingOrder));
                    if (waitingOrder.isFullMatched()) {
                        iterator.remove();
                    }
                    if (order.isFullMatched()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void wait(MarketMatchOrder order) {
        int limitPrice = order.getLimitPrice();
        topPrice = direct.newTopPrice(topPrice, limitPrice);
        LinkedList<MarketMatchOrder> orderList;
        if (data.containsKey(limitPrice)) {
            orderList = data.get(limitPrice);
        } else {
            orderList = new LinkedList<>();
            data.put(limitPrice, orderList);
        }
        orderList.add(order);
    }

    private MarketMatchTrade match(MarketMatchOrder order, MarketMatchOrder waitingOrder) {
        int matchVolume;
        int leftVolume = order.getLeftVolume();
        int waitingLeftVolume = waitingOrder.getLeftVolume();
        if (leftVolume > waitingLeftVolume) {
            matchVolume = waitingLeftVolume;
            order.setLeftVolume(leftVolume - waitingLeftVolume);
            waitingOrder.setLeftVolume(0);
        } else if (leftVolume < waitingLeftVolume) {
            matchVolume = leftVolume;
            waitingOrder.setLeftVolume(waitingLeftVolume - leftVolume);
            order.setLeftVolume(0);
        } else {
            matchVolume = leftVolume;
            order.setLeftVolume(0);
            waitingOrder.setLeftVolume(0);
        }
        return composeTrade(order, waitingOrder, matchVolume, leftVolume, waitingLeftVolume);
    }

    private MarketMatchTrade composeTrade(MarketMatchOrder order, MarketMatchOrder waitingOrder, int matchVolume, int leftVolume, int waitingLeftVolume) {
        LOGGER.info("{}:{}'s order[{}]@[{}][total {}/before {}/left {}] matches {} lot(s) {}'s order[{}]@[{}][total {}/before {}/left {}]@{} done",
                order.getPid(), order.getCid(), order.getDirect(), order.getLimitPrice(), order.getVolume(), leftVolume, order.getLeftVolume(), matchVolume,
                waitingOrder.getCid(), waitingOrder.getDirect(), waitingOrder.getLimitPrice(), waitingOrder.getVolume(), waitingLeftVolume, waitingOrder.getLeftVolume(),
                waitingOrder.getLimitPrice());
        MarketMatchTrade trade = new MarketMatchTrade();
        trade.setPid(order.getPid());
        trade.setCounterParty1(order.getCid());
        trade.setCounterParty2(waitingOrder.getCid());
        trade.setMatchPrice(waitingOrder.getLimitPrice());
        trade.setMatchVolume(matchVolume);
        trade.setDirect(order.getDirect());
        trade.setTimestamp(System.currentTimeMillis());
        return trade;
    }

    public List<Map<String, Object>> print() {
        int idx = 0;
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        List<MarketMatchOrder> orderList;
        int size = data.size();
        size = size > MAX_SIZE ? MAX_SIZE : size;
        for (int pointToTopPrice = topPrice;
             Math.abs(topPrice - pointToTopPrice) < MAX_DISTANCE && list.size() < size;
             pointToTopPrice = direct.nextTopPrice(pointToTopPrice)) {
            orderList = data.get(pointToTopPrice);
            if (orderList == null || orderList.isEmpty()) {
                continue;
            }
            map = new HashMap<>();
            map.put("name", direct.printName(idx));
            map.put("limitPrice", pointToTopPrice);
            map.put("volume", sumVolume(orderList));
            list.add(map);
            idx++;
        }
        if (SELL.equals(direct)) {
            Collections.reverse(list);
        }
        return list;
    }

    private int sumVolume(List<MarketMatchOrder> orderList) {
        return orderList.stream().mapToInt(MarketMatchOrder::getLeftVolume).sum();
    }
}
