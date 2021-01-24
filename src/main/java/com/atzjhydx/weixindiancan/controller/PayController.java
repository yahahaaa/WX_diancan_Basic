package com.atzjhydx.weixindiancan.controller;

import com.atzjhydx.weixindiancan.dto.OrderDTO;
import com.atzjhydx.weixindiancan.enums.ResultEnum;
import com.atzjhydx.weixindiancan.exception.SellException;
import com.atzjhydx.weixindiancan.service.OrderService;
import com.atzjhydx.weixindiancan.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    /**
     * 支付接口
     * @param orderId
     * @param returnUrl
     * @param map
     * @return
     */
    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId")String orderId,
                               @RequestParam("openid")String openid,
                               @RequestParam("returnUrl")String returnUrl,
                               Map<String,Object> map){

        //1.查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //检查当前支付人是否是下订单的人（忽略代支付）
        if (orderDTO.getBuyerOpenid() != openid){
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }

        //2.发起支付（调用统一下单接口，微信会回调这个接口，所以需要进行目录授权）
        PayResponse payResponse = payService.create(orderDTO);
        map.put("payResponse",payResponse);
        map.put("returnUrl",returnUrl);

        //3.调用统一下单接口后，需要返回一段特定的js代码，并将一些信息传入
        return new ModelAndView("pay/create",map);
    }

    /**
     * 微信异步通知支付完成
     * 微信异步通知接口必须在请求统一下单接口时就需要作为参数传送
     * @param notifyData
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData){
        payService.notify(notifyData);

        //返回给微信的处理结果（当处理成功后，可以返回一段特定的xml代码通知微信支付业务已经处理完成）
        return new ModelAndView("pay/success");
    }
}
