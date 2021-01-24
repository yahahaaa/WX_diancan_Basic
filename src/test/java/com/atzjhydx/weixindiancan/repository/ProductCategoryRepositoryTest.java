package com.atzjhydx.weixindiancan.repository;

import com.atzjhydx.weixindiancan.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    @Transactional  //在测试里，执行完方法就会回滚，不会将修改留在数据库
    public void findOneTest(){
        ProductCategory productCategory = repository.findById(1).orElse(null);
        productCategory.setCategoryType(9);
        repository.save(productCategory);
    }

    @Test
    @Transactional
    public void saveTest(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(3);
        repository.save(productCategory);
    }

    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> list = Arrays.asList(3);
        List<ProductCategory> result = repository.findByCategoryTypeIn(list);
        System.out.println("--------------------"+result.size());
        //利用断言判断
        Assert.assertNotEquals(0,result.size());
    }
}
