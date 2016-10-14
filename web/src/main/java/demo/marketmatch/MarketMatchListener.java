package demo.marketmatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by helly on 2016/10/6.
 */
@WebListener
public class MarketMatchListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketMatchListener.class);
    private static final MarketMatchEngine ENGINE = MarketMatchEngine.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("try to start loop thread");
        ENGINE.startLoop();
        LOGGER.info("loop thread started success");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("try to destroy loop thread");
        ENGINE.stopLoop();
        LOGGER.info("loop thread destroyed success");
    }
}
