package com.atzjhydx.weixindiancan.service.impl;

import com.atzjhydx.weixindiancan.dto.OrderDTO;
import com.atzjhydx.weixindiancan.enums.ResultEnum;
import com.atzjhydx.weixindiancan.exception.SellException;
import com.atzjhydx.weixindiancan.service.OrderService;
import com.atzjhydx.weixindiancan.service.PayService;
import com.atzjhydx.weixindiancan.utils.JsonUtil;
import com.atzjhydx.weixindiancan.utils.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    private static final String ORDER_NAME = "微信点餐订单";

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    /**
     * 使用SDK调用微信统一下单API，返回预付单信息
     * @param orderDTO
     * @return
     */
    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
        log.info("【微信支付】request={}", JsonUtil.toJson(payRequest));
        //调用下单API,获取预付单信息
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("【微信支付】response={}",JsonUtil.toJson(payResponse));

        return payResponse;
    }

    /**
     * 使用SDK处理微信异步通知支付完成的业务
     * @param notifyData
     * @return
     */
    @Override
    public PayResponse notify(String notifyData) {

        //1.验证签名（确保是微信官方发来的，很重要，防止恶意请求）
        //2.判断支付状态（是否是已支付状态）
        //上面两点sdk已经帮我们做了

        //解析微信异步通知发送来的xml文件（在这里判断支付是成功还是失败，如果失败，在方法里直接抛出异常）
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】异步通知，payResponse={}",payResponse);

        //3.查询订单
        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());

        //判断订单是否存在
        if (orderDTO == null){
            log.error("【微信支付】异步通知，订单不存在，orderId={}",payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //判断金额是否一致（判断下单时候的金额和支付时候的金额是否是一致的）!!!!!
        if (!MathUtil.equals(payResponse.getOrderAmount(),orderDTO.getOrderAmount().doubleValue())){
            log.error("【微信支付】异步通知，订单金额不一致,orderId={},微信通知金额={}，系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderDTO.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        //修改订单支付状态
        orderService.paid(orderDTO);

        return payResponse;
    }

    /**
     * 使用SDK处理微信退款业务
     * @param orderDTO
     * @return
     */
    @Override
    public RefundResponse refund(OrderDTO orderDTO) {

        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
        log.info("【微信退款】,request={}",refundRequest);
        RefundResponse refundReponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】,response={}",refundReponse);

        return refundReponse;
    }
}
