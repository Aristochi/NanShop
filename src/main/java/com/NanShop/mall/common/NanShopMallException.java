package com.NanShop.mall.common;

public class NanShopMallException extends RuntimeException {

    public NanShopMallException() {
    }

    public NanShopMallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new NanShopMallException(message);
    }

}
