package com.sheca.unitrust.common.util.cache;

/**
 * <p> cacheManager没有对应cache的时候抛出的异常
 *
 * @author liujida
 * create: 2019-05-14
 */
public class NoSuchCacheException extends RuntimeException {
    private static final long serialVersionUID = -8713346933487367121L;

    public NoSuchCacheException() {
    }

    public NoSuchCacheException(String message) {
        super(message);
    }

    public NoSuchCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchCacheException(Throwable cause) {
        super(cause);
    }

    public NoSuchCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
