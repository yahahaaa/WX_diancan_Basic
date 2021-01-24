package com.atzjhydx.weixindiancan.dataobject.mapper;

import com.atzjhydx.weixindiancan.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryMapperTest {

    @Resource
    private ProductCategoryMapper mapper;

    @Test
    public void insertByMap() {
        Map<String,Object> category = new HashMap<>();
        category.put("categoryName","饮料");
        category.put("category_type",new Integer(7));
        int result = mapper.insertByMap(category);
        Assert.assertEquals(1,result);
    }

    @Test
    public void insertByObject() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("西餐");
        productCategory.setCategoryType(new Integer(7));
        int result = mapper.insertByObject(productCategory);
        Assert.assertEquals(1,result);
    }

    @Test
    public void findByCategoryType() {
        ProductCategory result = mapper.findByCategoryType(new Integer(7));
        Assert.assertNotNull(result);
    }

    @Test
    public void findByCategoryName() {
    }

    @Test
    public void updateByCategoryType() {
        int result = mapper.updateByCategoryType("清蒸", 7);
        Assert.assertEquals(1,result);
    }

    @Test
    public void updateByObject() {
    }

    @Test
    public void deleteByCategoryType() {
    }

    @Test
    public void selectByCategoryType() {
        ProductCategory productCategory = mapper.selectByCategoryType(7);
        Assert.assertNotNull(productCategory);
    }
}