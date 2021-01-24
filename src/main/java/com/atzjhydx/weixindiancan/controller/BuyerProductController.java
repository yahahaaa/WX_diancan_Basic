package com.atzjhydx.weixindiancan.controller;

import com.atzjhydx.weixindiancan.VO.ProductInfoVO;
import com.atzjhydx.weixindiancan.VO.ProductVO;
import com.atzjhydx.weixindiancan.VO.ResultVO;
import com.atzjhydx.weixindiancan.dataobject.ProductCategory;
import com.atzjhydx.weixindiancan.dataobject.ProductInfo;
import com.atzjhydx.weixindiancan.service.CategoryService;
import com.atzjhydx.weixindiancan.service.ProductService;
import com.atzjhydx.weixindiancan.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @Cacheable(cacheNames = "product",key = "123")  //首次return的返回值会执行方法代码，第二次获取return的返回值直接在缓存中获取
    public ResultVO list(){

        //1.查询所有上架的商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        //2.查询类目（查询已经在上架商品的类目）
        List<Integer> collect = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        //查询已经商家的商品所属的ProductCategory
        List<ProductCategory> productCategories = categoryService.findByCategoryTypeIn(collect);

        //3.数据拼装（最后以productVO集合的形式返回前端）
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory:productCategories){
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo:productInfoList){
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }
}
