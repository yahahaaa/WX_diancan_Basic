package com.atzjhydx.weixindiancan.service.impl;

import com.atzjhydx.weixindiancan.dto.OrderDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PushMessageImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private PushMessageImpl pushMessage;

    @Test
    public void orderStatus() {
        OrderDTO orderDTO = orderService.findOne("1610681369989174951");
        pushMessage.orderStatus(orderDTO);
    }
}