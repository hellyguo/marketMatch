package demo.marketmatch.constants;

/**
 * Created by helly on 2016/9/30.
 */
public enum MarketMatchJsonKey {
    PID("pid"),
    CID("cid"),
    DIRECT("direct"),
    ORDER_TYPE("orderType"),
    LIMIT_PRICE("limitPrice"),
    VOLUME("volume"),
    TIMESTAMP("timestamp");

    private String jsonKeyName;

    MarketMatchJsonKey(String jsonKeyName) {
        this.jsonKeyName = jsonKeyName;
    }

    public String jsonKeyName() {
        return jsonKeyName;
    }
}
