package demo.marketmatch.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by helly on 2016/10/10.
 */
public class HisOrderMap<K, V> extends LinkedHashMap<K, V> {
    private int size;

    public HisOrderMap(int size) {
        super(size, 0.75f, false);
        this.size = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > size;
    }
}
