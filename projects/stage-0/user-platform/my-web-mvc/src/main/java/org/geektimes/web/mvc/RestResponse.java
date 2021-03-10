package org.geektimes.web.mvc;

/**
 * Rest 请求响应
 * @author jitwxs
 * @date 2021年03月06日 16:58
 */
public class RestResponse {
    private int code;

    private String message;

    /**
     * 转发路径
     */
    private String forwardPath;

    public RestResponse() {
    }

    public RestResponse(int code, String message, String forwardPath) {
        this.code = code;
        this.message = message;
        this.forwardPath = forwardPath;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getForwardPath() {
        return forwardPath;
    }

    public void setForwardPath(String forwardPath) {
        this.forwardPath = forwardPath;
    }

    public static RestResponse success(String message, String forwardPath) {
        return new RestResponse(0, message, forwardPath);
    }

    public static RestResponse failure(final int code, final String message) {
        return new RestResponse(code, message, null);
    }
}
