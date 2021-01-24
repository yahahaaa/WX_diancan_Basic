package com.atzjhydx.weixindiancan.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    SUCCESS(0,"成功"),
    PARAM_ERROR(1,"参数不正确"),
    PRODUCT_NOT_EXIST(10,"商品不存在"),
    PRODUCT_STOCK_NOT_ENOUGH(11,"库存不足"),
    ORDER_NOT_EXIST(12,"订单不存在"),
    ORDER_DETAIL_NOT_EXIST(13,"订单详情不存在"),
    ORDER_STATUS_ERROR(14,"订单状态异常"),
    ORDER_UPDATE_ERROR(15,"订单更新失败"),
    ORDER_DETAIL_EMPTY(16,"订单详情不存在"),
    ORDER_PAY_STATUS_ERROR(17,"订单支付状态异常"),
    CART_EMPTY(18,"购物车为空"),
    ORDER_OWNER_ERROR(19,"该订单不属于当前用户"),
    WECHAT_MP_ERROR(20,"微信公众账号方面错误"),
    WXPAY_NOTIFY_MONEY_VERIFY_ERROR(21,"微信支付异步通知金额校验不通过"),
    ORDER_CANCEL_SUCCESS(22,"订单取消成功"),
    ORDER_FINISH_SUCCESS(23,"完结订单成功"),
    PRODUCT_STATUS_ERROR(24,"商品状态不正确"),
    LOGIN_FAIL(25,"登录失败,登录信息不正确"),
    LOGOUT_SUCCESS(26,"登出成功"),
    PAYMENT_FAILED(27,"支付失败,由于缺少认证商户号");

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
