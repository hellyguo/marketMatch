package demo.marketmatch;

import com.alibaba.fastjson.JSONObject;
import demo.marketmatch.domain.MarketMatchOrder;
import demo.marketmatch.util.HisOrderMap;
import demo.marketmatch.util.RandomOrderGenerator;
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
import java.util.*;

import static demo.marketmatch.constants.MarketMatchDirect.BUY;
import static demo.marketmatch.constants.MarketMatchDirect.SELL;
import static demo.marketmatch.constants.MarketMatchJsonKey.*;
import static demo.marketmatch.constants.MarketMatchType.LIMIT;
import static demo.marketmatch.constants.WebErrorCode.GET_NOT_ALLOWED;
import static demo.marketmatch.constants.WebErrorCode.POST_NOT_ALLOWED;

/**
 * Created by helly on 2016/10/6.
 */
@WebServlet(name = "MarketMatchServlet", urlPatterns = {MarketMatchServlet.URI_POST_DATA, MarketMatchServlet.URI_VIEW_DATA})
public class MarketMatchServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketMatchServlet.class);

    private static final MarketMatchViewer VIEWER = MarketMatchViewer.getInstance();
    private static final MarketMatchEngine ENGINE = MarketMatchEngine.getInstance();

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
    private static final String UTF_8 = "UTF-8";
    private static final String ERR_CODE = "errCode";
    private static final String ERR_MSG = "errMsg";

    static final String URI_POST_DATA = "/postData";
    static final String URI_VIEW_DATA = "/viewData";

    @Override
    public void init(ServletConfig config) throws ServletException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        LOGGER.info("uri:{}", uri);
        if (URI_POST_DATA.equals(uri)) {
            LOGGER.debug("invoke postData");
            String data = request.getParameter(PID.jsonKeyName());
            LOGGER.info("posted data:{}", data);
            MarketMatchOrder order = RandomOrderGenerator.createRandomOrder(data);
            ENGINE.postOrder(order);
            writeJson(response, VIEWER.view(order.getPid()));
        } else {
            LOGGER.warn("the uri[{}] is not allowed to execute post method", uri);
            JSONObject retJson = new JSONObject();
            retJson.put(ERR_CODE, POST_NOT_ALLOWED.errCode());
            retJson.put(ERR_MSG, POST_NOT_ALLOWED.errMsg());
            writeJson(response, retJson.toJSONString());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        LOGGER.info("uri:{}", uri);
        if (URI_VIEW_DATA.equals(uri)) {
            LOGGER.debug("invoke viewData");
            String data = request.getParameter(PID.jsonKeyName());
            LOGGER.info("got data:{}", data);
            writeJson(response, VIEWER.view(data));
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
