package demo.marketmatch.util;

import java.util.Random;

/**
 * Created by hellyguo on 2016/6/2.
 */
public class RandomPriceGenerator {

    private static final int[] CHANGE_RANGES = new int[]{
            61/*[0,60]*/, 51/*[0,50]*/, 41/*[0,40]*/,
            31/*[0,30]*/, 21/*[0,20]*/, 19/*[0,18]*/,
            17/*[0,16]*/, 15/*[0,14]*/, 13/*[0,12]*/,
            11/*[0,10]*/, 11/*[0,10]*/, 11/*[0,10]*/,
            11/*[0,10]*/, 11/*[0,10]*/, 11/*[0,10]*/,
            9/*[0,8]*/, 9/*[0,8]*/, 9/*[0,8]*/,
            9/*[0,8]*/, 9/*[0,8]*/, 9/*[0,8]*/,
            9/*[0,8]*/, 9/*[0,8]*/, 9/*[0,8]*/,
            9/*[0,8]*/, 9/*[0,8]*/, 9/*[0,8]*/,
            9/*[0,8]*/, 9/*[0,8]*/, 9/*[0,8]*/,
            7/*[0,6]*/, 7/*[0,6]*/, 7/*[0,6]*/,
            7/*[0,6]*/, 7/*[0,6]*/, 7/*[0,6]*/,
            7/*[0,6]*/, 7/*[0,6]*/, 7/*[0,6]*/,
            7/*[0,6]*/, 7/*[0,6]*/, 7/*[0,6]*/,
            7/*[0,6]*/, 7/*[0,6]*/, 7/*[0,6]*/,
            7/*[0,6]*/, 7/*[0,6]*/, 7/*[0,6]*/,
            7/*[0,6]*/, 7/*[0,6]*/, 7/*[0,6]*/,
            7/*[0,6]*/, 7/*[0,6]*/, 7/*[0,6]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            5/*[0,4]*/, 5/*[0,4]*/, 5/*[0,4]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/,
            3/*[0,2]*/, 3/*[0,2]*/, 3/*[0,2]*/};
    private static final Random RANDOM = new Random(System.nanoTime());

    private int price;
    private Random random = RANDOM;

    public RandomPriceGenerator(int initVal) {
        this.price = initVal;
    }

    public RandomPriceGenerator(int initVal, Random random) {
        this.price = initVal;
        this.random = random;
    }

    public int randomPrice() {
        price = price + randomAroundVal();
        return price;
    }

    private int randomAroundVal() {
        int nextRange = CHANGE_RANGES[price % CHANGE_RANGES.length];
        int nextRangeHalf = (nextRange - 1) / 2;
        return random.nextInt(nextRange) - nextRangeHalf;
    }

}
