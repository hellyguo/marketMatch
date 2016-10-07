package demo.marketmatch.constants;

/**
 * Created by helly on 2016/10/7.
 */
public enum WebErrorCode {
    POST_NOT_ALLOWED(-10001, "不允许执行POST"), GET_NOT_ALLOWED(-10002, "不允许执行GET");

    private int errCode;
    private String errMsg;

    WebErrorCode(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public int errCode() {
        return errCode;
    }

    public String errMsg() {
        return errMsg;
    }
}
