package com.atzjhydx.weixindiancan.service.impl;

import com.atzjhydx.weixindiancan.dataobject.ProductCategory;
import com.atzjhydx.weixindiancan.enums.CodeEnum;
import com.atzjhydx.weixindiancan.enums.ProductStatusEnum;
import com.atzjhydx.weixindiancan.service.impl.CategoryServiceImpl;
import com.atzjhydx.weixindiancan.utils.EnumUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void findOne() throws Exception{
        ProductCategory productCategory = categoryService.findOne(1);
        Assert.assertNotEquals(new Integer(1),productCategory.getCategoryId());
    }

    @Test
    public void findAll() throws Exception{
        List<ProductCategory> productCategoryList = categoryService.findAll();
        Assert.assertNotEquals(0,productCategoryList.size());
    }

    @Test
    public void findByCategoryTypeIn() throws Exception{
        List<ProductCategory> byCategoryTypeIn = categoryService.findByCategoryTypeIn(Arrays.asList(3));
        Assert.assertNotEquals(0,byCategoryTypeIn.size());
    }

    @Test
    public void save() throws Exception{
        ProductCategory productCategory = new ProductCategory("男生专享", 2);
        ProductCategory result = categoryService.save(productCategory);
        Assert.assertNotNull(result);
    }
}
