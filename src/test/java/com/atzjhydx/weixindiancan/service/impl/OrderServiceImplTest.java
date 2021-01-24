package com.atzjhydx.weixindiancan.service.impl;

import com.atzjhydx.weixindiancan.dataobject.OrderDetail;
import com.atzjhydx.weixindiancan.dto.OrderDTO;
import com.atzjhydx.weixindiancan.enums.OrderStatusEnum;
import com.atzjhydx.weixindiancan.enums.PayStatusEnum;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.logging.XMLFormatter;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final  String BUYER_OPENID = "10082008220";
    private final String ORDERID = "1610525393508185690";

    @Test
    public void create() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("张三");
        orderDTO.setBuyerPhone("15844003403");
        orderDTO.setBuyerAddress("并夕夕");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        //购物车,假设买了5个包子，10个油条
        ArrayList<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("123457");
        o1.setProductQuantity(5);
        orderDetailList.add(o1);
        OrderDetail o2 = new OrderDetail();
        o1.setProductId("123456");
        o1.setProductQuantity(10);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);
        log.info("【创建订单】result={}",result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() {
        OrderDTO result = orderService.findOne(ORDERID);
        log.info("【查询单个订单】result={}",result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findList() {
        PageRequest pageRequest = PageRequest.of(0,2);
        Page<OrderDTO> list = orderService.findList(BUYER_OPENID,pageRequest);
        Assert.assertNotEquals(0,list.getSize());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDERID);
        OrderDTO result = orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO orderDTO = orderService.findOne(ORDERID);
        OrderDTO result = orderService.finish(orderDTO);
        Assert.assertNotEquals(OrderStatusEnum.FINISHED,result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDERID);
        OrderDTO result = orderService.cancel(orderDTO);
        Assert.assertNotEquals(PayStatusEnum.SUCCESS,result.getPayStatus());
    }
}