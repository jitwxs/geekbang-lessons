package org.geektimes.web.mvc;

/**
 * Rest 请求响应
 * @author jitwxs
 * @date 2021年03月06日 16:58
 */
public class RestResponse {
    private int code;

    private String message;

    public RestResponse() {
    }

    public RestResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
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

    public static RestResponse success(String message) {
        return new RestResponse(0, message);
    }

    public static RestResponse failure(final int code, final String message) {
        return new RestResponse(code, message);
    }
}
