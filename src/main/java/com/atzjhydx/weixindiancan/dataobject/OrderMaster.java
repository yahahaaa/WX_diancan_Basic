package com.atzjhydx.weixindiancan.dataobject;

import com.atzjhydx.weixindiancan.enums.OrderStatusEnum;
import com.atzjhydx.weixindiancan.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 下单动作产生的订单信息，订单具体内容在OrderDetail中
 */
@Entity
@Data
@DynamicUpdate
public class OrderMaster {

    @Id
    //订单id
    private String orderId;

    //买家姓名
    private String buyerName;

    //买家手机号
    private String buyerPhone;

    //买家地址
    private String buyerAddress;

    //买家微信Openid
    private String buyerOpenid;

    //订单总金额
    private BigDecimal orderAmount;

    //订单状态，默认为0，新下单
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    //支付状态,默认为0，未支付
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    private Date createTime;

    private Date updateTime;
}
