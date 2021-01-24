package com.atzjhydx.weixindiancan.repository;

import com.atzjhydx.weixindiancan.dataobject.SellerInfo;
import com.atzjhydx.weixindiancan.utils.KeyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerInfoRepositoryTest {

    @Autowired
    private SellerInfoRepository repository;

    @Test
    public void findByOpenid() {
        SellerInfo result = repository.findByOpenid("abc");
        Assert.assertEquals("abc",result.getOpenid());
    }

    @Test
    public void save(){
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setOpenid("abc");
        sellerInfo.setUsername("admin");
        sellerInfo.setPassword("123456");
        sellerInfo.setSellerId(KeyUtil.genUniqueKey());

        SellerInfo result = repository.save(sellerInfo);
        Assert.assertNotNull(result);
    }
}