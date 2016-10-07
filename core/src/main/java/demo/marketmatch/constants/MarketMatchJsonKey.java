package demo.marketmatch.constants;

/**
 * Created by helly on 2016/9/30.
 */
public enum MarketMatchJsonKey {
    PID("pid", String.class),
    CID("cid", String.class),
    DIRECT("direct", MarketMatchDirect.class),
    ORDER_TYPE("orderType", MarketMatchType.class),
    LIMIT_PRICE("limitPrice", Integer.class),
    VOLUME("volume", Integer.class),
    TIMESTAMP("timestamp", Long.class);

    private String jsonKeyName;
    private Class jsonValType;

    MarketMatchJsonKey(String jsonKeyName, Class jsonValType) {
        this.jsonKeyName = jsonKeyName;
        this.jsonValType = jsonValType;
    }

    public String jsonKeyName(){
        return jsonKeyName;
    }

    public <T> Class<T> jsonValType(){
        return jsonValType;
    }
}
