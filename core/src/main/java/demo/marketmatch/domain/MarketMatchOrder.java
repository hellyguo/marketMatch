package demo.marketmatch.domain;

import demo.marketmatch.constants.MarketMatchDirect;
import demo.marketmatch.constants.MarketMatchType;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchOrder {
    private String pid;

    private String cid;

    private MarketMatchDirect direct;

    private MarketMatchType orderType;

    private int limitPrice;

    private int volume;

    private long timestamp;

    private int leftVolume;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public MarketMatchDirect getDirect() {
        return direct;
    }

    public void setDirect(MarketMatchDirect direct) {
        this.direct = direct;
    }

    public MarketMatchType getOrderType() {
        return orderType;
    }

    public void setOrderType(MarketMatchType orderType) {
        this.orderType = orderType;
    }

    public int getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(int limitPrice) {
        this.limitPrice = limitPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
        this.leftVolume = volume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLeftVolume() {
        return leftVolume;
    }

    public void setLeftVolume(int leftVolume) {
        this.leftVolume = leftVolume;
    }

    public boolean isFullMatched() {
        return leftVolume == 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MarketMatchOrder{");
        sb.append("pid='").append(pid).append('\'');
        sb.append(", cid='").append(cid).append('\'');
        sb.append(", direct=").append(direct);
        sb.append(", orderType=").append(orderType);
        sb.append(", limitPrice=").append(limitPrice);
        sb.append(", volume=").append(volume);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
