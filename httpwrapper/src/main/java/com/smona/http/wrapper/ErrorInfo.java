package com.smona.http.wrapper;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 1:45 PM
 */
public class ErrorInfo {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String url;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
