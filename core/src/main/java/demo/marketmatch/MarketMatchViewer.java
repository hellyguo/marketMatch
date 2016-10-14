package demo.marketmatch;

import com.alibaba.fastjson.JSONObject;
import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.domain.MarketMatchOrderBook;
import demo.marketmatch.domain.MarketMatchTrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchViewer {
    private static final MarketMatchViewer VIEWER = new MarketMatchViewer();
    private static final JSONObject EMPTY = new JSONObject();
    private static final int MAX_SIZE = 100;

    private Map<String, Map<String, Object>> viewerMap = new HashMap<>();

    public static MarketMatchViewer getInstance() {
        return VIEWER;
    }

    private MarketMatchViewer() {
    }

    public void pushBook(String pid, MarketMatchOrderBook book) {
        put(pid, "book", book);
    }

    private void put(String pid, String name, Object obj) {
        if (viewerMap.containsKey(pid)) {
            viewerMap.get(pid).put(name, obj);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put(name, obj);
            viewerMap.put(pid, map);
        }
    }

    public void pushOrder(String pid, MarketMatchOrder order) {
        append(pid, "order", order);
    }

    private void append(String pid, String name, Object object) {
        if (viewerMap.containsKey(pid)) {
            Map<String, Object> map = viewerMap.get(pid);
            if (map.containsKey(name)) {
                List<Object> list = ((List<Object>) map.get(name));
                list.add(0, object);
                while (list.size() > MAX_SIZE) {
                    list.remove(MAX_SIZE);
                }
            } else {
                putList(pid, name, object);
            }
        } else {
            putList(pid, name, object);
        }
    }

    private void putList(String pid, String name, Object object) {
        List<Object> list = new ArrayList<>();
        list.add(object);
        put(pid, name, list);
    }

    public void pushTrade(String pid, List<MarketMatchTrade> trades) {
        appendAll(pid, "trade", trades);
    }

    private void appendAll(String pid, String name, List<MarketMatchTrade> list) {
        for (MarketMatchTrade trade : list) {
            append(pid, name, trade);
        }
    }

    public JSONObject view(String pid) {
        JSONObject json;
        if (viewerMap.containsKey(pid)) {
            Map<String, Object> retData = viewerMap.get(pid);
            json = new JSONObject();
            json.putAll(retData);
        } else {
            json = EMPTY;
        }
        return json;
    }
}
