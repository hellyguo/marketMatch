package demo.marketmatch;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchViewer {
    private static final MarketMatchViewer VIEWER = new MarketMatchViewer();
    private static final JSONObject EMPTY = new JSONObject();

    private Map<String, JSONObject> matchMap = new HashMap<>();

    public static MarketMatchViewer getInstance() {
        return VIEWER;
    }

    private MarketMatchViewer() {
    }

    public void refreshView(String pid, JSONObject json) {
        matchMap.put(pid, json);
    }

    public JSONObject view(String pid) {
        JSONObject retData = matchMap.get(pid);
        return retData == null ? EMPTY : retData;
    }
}
