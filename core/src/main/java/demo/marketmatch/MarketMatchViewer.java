package demo.marketmatch;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchViewer {
    private Map<String, String> matchMap = new HashMap<>();

    void refreshView(String pid, String json) {
        matchMap.put(pid, json);
    }

    public String view(String pid) {
        String retData = matchMap.get(pid);
        return retData == null ? "{}" : retData;
    }
}
