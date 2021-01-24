package com.atzjhydx.weixindiancan.repository;

import com.atzjhydx.weixindiancan.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;

    @Test
    public void saveTest(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId("12345678");
        orderDetail.setDetailId("111111");
        orderDetail.setProductId("123433");
        orderDetail.setProductName("油条");
        orderDetail.setProductPrice(new BigDecimal(2.4));
        orderDetail.setProductQuantity(100);
        orderDetail.setProductIcon("https://xxxx.jpg");

        OrderDetail result = repository.save(orderDetail);
        Assert.assertNotEquals(null,result);
    }

    @Test
    public void findByOrderId() {
        List<OrderDetail> byOrderId = repository.findByOrderId("111111");
        Assert.assertNotEquals(0,byOrderId.size());
    }
}