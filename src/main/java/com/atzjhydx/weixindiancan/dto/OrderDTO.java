package com.atzjhydx.weixindiancan.dto;

import com.atzjhydx.weixindiancan.dataobject.OrderDetail;
import com.atzjhydx.weixindiancan.enums.OrderStatusEnum;
import com.atzjhydx.weixindiancan.enums.PayStatusEnum;
import com.atzjhydx.weixindiancan.serializer.Date2LongSerializer;
import com.atzjhydx.weixindiancan.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 一个订单DTO中含有多个订单详情信息，用List存储
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)  //只会将没有null的值转为json字符串
public class OrderDTO {

    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    private String buyerOpenid;

    private BigDecimal orderAmount;

    //订单状态，默认为0，新下单
    private Integer orderStatus;

    //支付状态,默认为0，未支付
    private Integer payStatus;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    private List<OrderDetail> orderDetailList;

    @JsonIgnore
    //方便模板引擎获取到当前DTO的订单状态的具体含义
    public OrderStatusEnum getOrderStatusEnum(){
        return EnumUtil.getByCode(orderStatus,OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum(){
        return EnumUtil.getByCode(payStatus,PayStatusEnum.class);
    }
}
