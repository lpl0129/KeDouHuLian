package com.stay.toolslibrary.net;

import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;


public class NetExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;


    public static ResponeThrowable handleException(Throwable e) {
        ResponeThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponeThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.errormsg = "网络链接异常,请稍后再试";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            //服务器自定义错误,,如果服务器定义code重复,则在此处重新定义code,保持本地统一
            ServerException resultException = (ServerException) e;
            ex = new ResponeThrowable(resultException, resultException.code);
            ex.errormsg = resultException.errormsg;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponeThrowable(e, ERROR.PARSE_ERROR);
            ex.errormsg = "数据解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponeThrowable(e, ERROR.NETWORD_ERROR);
            ex.errormsg = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponeThrowable(e, ERROR.SSL_ERROR);
            ex.errormsg = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponeThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.errormsg = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponeThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.errormsg = "连接超时";
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponeThrowable(e, ERROR.NETCON_UNKNOWN);
            ex.errormsg = "网络链接异常,请稍后再试";
            return ex;
        } else {
            ex = new ResponeThrowable(e, ERROR.UNKNOWN);
            ex.errormsg = "未知错误";
            return ex;
        }

    }

    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 100000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 100100;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 100200;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 100300;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 100500;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 100600;
        /**
         * UnknownHostException
         */
        public static final int NETCON_UNKNOWN = 100700;
        public static final int SERIVCE_ERROR = 100800;
    }

    public static class ResponeThrowable extends Exception {
        public int code;


        public String errormsg="";

        public ResponeThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;

        }
    }

    public static class ServerException extends RuntimeException {
        public int code;
        public String errormsg;

        public ServerException(int code, String errormsg) {
            this.code = code;
            this.errormsg = errormsg;
            if (TextUtils.isEmpty(errormsg)) {
                this.errormsg = "网络链接异常,请稍后再试";
            }
        }
    }
}

