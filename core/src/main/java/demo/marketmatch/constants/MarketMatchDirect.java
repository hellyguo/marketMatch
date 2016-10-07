package demo.marketmatch.constants;

/**
 * Created by helly on 2016/9/30.
 */
public enum MarketMatchDirect {
    BUY, SELL;

    private static final String[] BUY_NAMES = {"买一", "买二", "买三", "买四", "买五"};
    private static final String[] SELL_NAMES = {"卖一", "卖二", "卖三", "卖四", "卖五"};

    public int newTopPrice(int currentTopPrice, int newPrice) {
        int topPrice = currentTopPrice;
        switch (this) {
            case BUY:
                if (newPrice > topPrice) {
                    topPrice = newPrice;
                }
                break;
            case SELL:
                if (newPrice < topPrice) {
                    topPrice = newPrice;
                }
                break;
            default:
                throw new RuntimeException("unknown direct");
        }
        return topPrice;
    }

    public int nextTopPrice(int topPrice) {
        int newTopPrice = topPrice;
        switch (this) {
            case BUY:
                newTopPrice--;
                break;
            case SELL:
                newTopPrice++;
                break;
            default:
                throw new RuntimeException("unknown direct");
        }
        return newTopPrice;
    }

    public boolean isFit(int topPrice, int newPrice) {
        boolean fit;
        switch (this) {
            case BUY:
                fit = newPrice <= topPrice;
                break;
            case SELL:
                fit = newPrice >= topPrice;
                break;
            default:
                throw new RuntimeException("unknown direct");
        }
        return fit;
    }

    public String printName(int idx) {
        if (BUY.equals(this)) {
            return BUY_NAMES[idx];
        } else {
            return SELL_NAMES[idx];
        }
    }
}
