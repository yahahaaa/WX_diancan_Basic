package com.atzjhydx.weixindiancan.repository;

import com.atzjhydx.weixindiancan.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository repository;

    @Test
    public void saveTest(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123457");
        orderMaster.setBuyerName("leeMZ");
        orderMaster.setBuyerPhone("15888884548");
        orderMaster.setBuyerAddress("并夕夕");
        orderMaster.setBuyerOpenid("123abc");
        orderMaster.setOrderAmount(new BigDecimal(2.5));

        OrderMaster result = repository.save(orderMaster);
        Assert.assertNotNull(result);
    }

    private final String OPENID = "123abc";

    @Test
    public void findByBuyerOpenid() {
        PageRequest pageRequest = PageRequest.of(0,2);
        Page<OrderMaster> result = repository.findByBuyerOpenid(OPENID, pageRequest);

        Assert.assertNotEquals(0,result.getSize());
    }
}