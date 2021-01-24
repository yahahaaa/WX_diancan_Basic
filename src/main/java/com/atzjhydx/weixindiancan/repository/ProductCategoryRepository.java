package com.atzjhydx.weixindiancan.repository;

import com.atzjhydx.weixindiancan.dataobject.ProductCategory;
import com.atzjhydx.weixindiancan.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

}
