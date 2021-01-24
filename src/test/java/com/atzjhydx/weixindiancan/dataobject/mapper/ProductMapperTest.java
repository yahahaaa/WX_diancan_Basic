package com.atzjhydx.weixindiancan.dataobject.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
//webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT由于项目中使用了webSocket，所以如果不加测试通不过
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductMapperTest {

    @Resource
    private ProductMapper productMapper;

    @Test
    public void updateCategoryType() {
        int result = productMapper.updateCategoryType(4, 2);
        //目前数据库表product_info表中有两个商品的categoryType等于2，所以返回的结果应该为2，表示成功更改了两条数据
        Assert.assertEquals(2,result);
    }
}