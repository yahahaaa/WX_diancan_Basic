package com.atzjhydx.weixindiancan.controller;

import com.atzjhydx.weixindiancan.dataobject.ProductCategory;
import com.atzjhydx.weixindiancan.exception.SellException;
import com.atzjhydx.weixindiancan.form.CategoryForm;
import com.atzjhydx.weixindiancan.service.CategoryService;
import com.atzjhydx.weixindiancan.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    /**
     * 类目列表
     * @param map
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(Map<String,Object> map){
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList",categoryList);
        return new ModelAndView("category/list",map);
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                              Map<String,Object> map){
        if (categoryId != null){
            ProductCategory productCategory = categoryService.findOne(categoryId);
            map.put("category",productCategory);
        }

        return new ModelAndView("category/index",map);
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm categoryForm,
                             BindingResult bindingResult,Map<String,Object> map){

        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }

        ProductCategory productCategory = new ProductCategory();
        try{
            if (categoryForm.getCategoryId() != null){
                productCategory = categoryService.findOne(categoryForm.getCategoryId());
            }

            //如果类目编号没有改，则直接更改数据库类目信息
            if (categoryForm.getCategoryType().equals(productCategory.getCategoryType())){
                //如果没有更改类目编号的话，直接更改类目信息
                BeanUtils.copyProperties(categoryForm,productCategory);
                categoryService.save(productCategory);
            }else{
                //如果类目编号改了，需要更改数据库中商品信息中的类目编号
                Integer originProductCategoryType = productCategory.getCategoryType();
                BeanUtils.copyProperties(categoryForm,productCategory);
                // 1.先更新category表
                categoryService.save(productCategory);
                // 2.再更新productInfo表
                productService.updateCategoryType(originProductCategoryType,productCategory.getCategoryType());
            }
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/category/index");
            return new ModelAndView("common/error",map);
        }

        map.put("url","/sell/seller/category/list");
        return new ModelAndView("common/success",map);

    }
}
