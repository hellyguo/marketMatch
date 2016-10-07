package demo.marketmatch.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by helly on 2016/9/30.
 */
public class MarketMatchStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketMatchStore.class);
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private Map<String, MarketMatchWaitingQueuePair> store = new HashMap<>();

    public MarketMatchWaitingQueuePair getQueue(String pid) {
        MarketMatchWaitingQueuePair queue;
        try {
            readLock.lock();//首先开启读锁，从缓存中去取
            if (store.containsKey(pid)) {//如果缓存中有
                queue = store.get(pid);
                LOGGER.debug("{} direct get queue", pid);
            } else {//如果缓存中没有
                readLock.unlock();//释放读锁
                try {
                    writeLock.lock();//上写锁
                    if (store.containsKey(pid)) {//存在同时等待加写锁写同一个pid代表的queue，所以必须再次检查
                        queue = store.get(pid);
                        LOGGER.info("{} get queue written by prior write thread", pid);
                    } else {
                        queue = new MarketMatchWaitingQueuePair();
                        store.put(pid, queue);
                        LOGGER.info("{} create and write queue success", pid);
                    }
                    readLock.lock();//然后再上读锁
                } finally {
                    writeLock.unlock();//释放写锁
                }
            }
        } finally {
            readLock.unlock();//最后释放读锁
        }
        return queue;
    }
}
