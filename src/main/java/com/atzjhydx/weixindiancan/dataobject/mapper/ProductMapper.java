package com.atzjhydx.weixindiancan.dataobject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper {

    /**
     * 更新product_info表中的categoryType值
     * @param originCategoryType
     * @param newCategoryType
     * @return  影响的数据行数
     */
    @Update("update product_info set category_type = #{newCategoryType} where category_type = #{originCategoryType}")
    int updateCategoryType(@Param("originCategoryType") Integer originCategoryType,
                           @Param("newCategoryType") Integer newCategoryType);

    /**
     * 利用数据库行锁减库存
     * @param productQuantity
     * @param productId
     * @return
     */
    @Update("update product_info set product_stock = product_stock - #{productQuantity} where product_id = #{productId} and product_stock - #{productQuantity} >= 0")
    int decreaseStock(@Param("productQuantity") Integer productQuantity,
                      @Param("productId") String productId);


    /**
     * 利用数据库行锁加库存
     * @param productQuantity
     * @param productId
     * @return
     */
    @Update("update product_info set product_stock = product_stock + #{productQuantity} where product_id = #{productId}")
    int increaseStock(@Param("productQuantity") Integer productQuantity,
                      @Param("productId") String productId);
}
