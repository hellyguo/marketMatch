package demo.marketmatch;

import com.alibaba.fastjson.JSONObject;
import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.util.RandomPriceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

import static demo.marketmatch.constants.MarketMatchDirect.BUY;
import static demo.marketmatch.constants.MarketMatchDirect.SELL;
import static demo.marketmatch.constants.MarketMatchJsonKey.*;
import static demo.marketmatch.constants.MarketMatchType.LIMIT;
import static demo.marketmatch.constants.WebErrorCode.*;

/**
 * Created by helly on 2016/10/6.
 */
@WebServlet(name = "MarketMatchServlet", urlPatterns = {MarketMatchServlet.URI_POST_DATA, MarketMatchServlet.URI_VIEW_DATA})
public class MarketMatchServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketMatchServlet.class);

    private static final MarketMatchViewer VIEWER = new MarketMatchViewer();
    private static final MarketMatchEngine ENGINE = new MarketMatchEngine(VIEWER);

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
    private static final String UTF_8 = "UTF-8";
    private static final String ERR_CODE = "errCode";
    private static final String ERR_MSG = "errMsg";
    private static final String PARAM_PID = "pid";

    private static final Random RANDOM = new Random();
    private static final int BASIC_LIMIT_PRICE = 23003;
    private static final int BASIC_VOLUME = 100;
    private static final int VOLUME_RANGE = 100;

    static final String URI_POST_DATA = "/postData";
    static final String URI_VIEW_DATA = "/viewData";

    private RandomPriceGenerator generator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        generator = new RandomPriceGenerator(BASIC_LIMIT_PRICE, RANDOM);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        LOGGER.info("uri:{}", uri);
        if (URI_POST_DATA.equals(uri)) {
            LOGGER.info("invoke postData");
            String data = request.getParameter(PARAM_PID);
            LOGGER.info("posted data:{}", data);
            MarketMatchOrder order = createRandomOrder(data);
            ENGINE.receiveAndMatch(order);
            String retJson = VIEWER.view(order.getPid());
            writeJson(response, retJson);
        } else {
            LOGGER.warn("the uri[{}] is not allowed to execute post method", uri);
            JSONObject retJson = new JSONObject();
            retJson.put(ERR_CODE, POST_NOT_ALLOWED.errCode());
            retJson.put(ERR_MSG, POST_NOT_ALLOWED.errMsg());
            writeJson(response, retJson.toJSONString());
        }
    }

    private MarketMatchOrder createRandomOrder(String data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PID.jsonKeyName(), data);
        jsonObject.put(CID.jsonKeyName(), Thread.currentThread().getName());
        jsonObject.put(DIRECT.jsonKeyName(), RANDOM.nextBoolean() ? BUY.name() : SELL.name());
        jsonObject.put(ORDER_TYPE.jsonKeyName(), LIMIT.name());
        jsonObject.put(LIMIT_PRICE.jsonKeyName(), generator.randomPrice());
        jsonObject.put(VOLUME.jsonKeyName(), (RANDOM.nextInt(VOLUME_RANGE) + 1) * BASIC_VOLUME);
        jsonObject.put(TIMESTAMP.jsonKeyName(), System.currentTimeMillis());
        return JSONObject.parseObject(jsonObject.toJSONString(), MarketMatchOrder.class);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        LOGGER.info("uri:{}", uri);
        if (URI_VIEW_DATA.equals(uri)) {
            LOGGER.info("invoke viewData");
            String data = request.getParameter(PARAM_PID);
            LOGGER.info("got data:{}", data);
            String retJson = VIEWER.view(data);
            writeJson(response, retJson);
        } else {
            LOGGER.warn("the uri[{}] is not allowed to execute get method", uri);
            JSONObject retJson = new JSONObject();
            retJson.put(ERR_CODE, GET_NOT_ALLOWED.errCode());
            retJson.put(ERR_MSG, GET_NOT_ALLOWED.errMsg());
            writeJson(response, retJson.toJSONString());
        }
    }

    private void writeJson(HttpServletResponse response, String retJson) throws IOException {
        response.addHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
        response.setCharacterEncoding(UTF_8);
        response.getWriter().append(retJson);
        response.getWriter().flush();
    }
}
