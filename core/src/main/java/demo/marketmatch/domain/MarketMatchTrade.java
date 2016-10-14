package demo.marketmatch.domain;

import demo.marketmatch.constants.MarketMatchDirect;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchTrade {
    private String pid;

    private String counterParty1;
    private String counterParty2;
    private int matchPrice;
    private int matchVolume;
    private MarketMatchDirect direct;
    private long timestamp;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCounterParty1() {
        return counterParty1;
    }

    public void setCounterParty1(String counterParty1) {
        this.counterParty1 = counterParty1;
    }

    public String getCounterParty2() {
        return counterParty2;
    }

    public void setCounterParty2(String counterParty2) {
        this.counterParty2 = counterParty2;
    }

    public int getMatchPrice() {
        return matchPrice;
    }

    public void setMatchPrice(int matchPrice) {
        this.matchPrice = matchPrice;
    }

    public int getMatchVolume() {
        return matchVolume;
    }

    public void setMatchVolume(int matchVolume) {
        this.matchVolume = matchVolume;
    }

    public MarketMatchDirect getDirect() {
        return direct;
    }

    public void setDirect(MarketMatchDirect direct) {
        this.direct = direct;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MarketMatchTrade{");
        sb.append("pid='").append(pid).append('\'');
        sb.append(", counterParty1='").append(counterParty1).append('\'');
        sb.append(", counterParty2='").append(counterParty2).append('\'');
        sb.append(", matchPrice=").append(matchPrice);
        sb.append(", matchVolume=").append(matchVolume);
        sb.append(", direct=").append(direct);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
