package com.atzjhydx.weixindiancan.service.impl;

import com.atzjhydx.weixindiancan.config.WechatAccountConfig;
import com.atzjhydx.weixindiancan.dto.OrderDTO;
import com.atzjhydx.weixindiancan.service.PushMessage;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class PushMessageImpl implements PushMessage {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Override
    public void orderStatus(OrderDTO orderDTO) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        //设置模板id
        templateMessage.setTemplateId("ithVue6Ouo6w3_qDZ2xV4NpNrM4eFmg61a_njz8MWNE");
        //设置openid
        templateMessage.setToUser(orderDTO.getBuyerOpenid());
        //设置通知内容
        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first","亲，记得收货"),
                new WxMpTemplateData("keyword1","微信点餐"),
                new WxMpTemplateData("keyword2","18868812345"),
                new WxMpTemplateData("keyword3",orderDTO.getOrderId()),
                new WxMpTemplateData("keyword4",orderDTO.getOrderStatusEnum().getMessage()),
                new WxMpTemplateData("keyword5","￥" + orderDTO.getOrderAmount()),
                new WxMpTemplateData("remark","欢迎再次光临"));
        templateMessage.setData(data);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            //仅仅只记录状态，不抛出异常
            log.error("【微信模板消息】发送失败{}",e);
        }
    }
}
