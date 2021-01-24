package com.atzjhydx.weixindiancan.dataobject.mapper;

import com.atzjhydx.weixindiancan.dataobject.ProductCategory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductCategoryMapper {

    /**
     * 新增
     * 通过map注入
     * @param map
     * @return
     */
    @Insert("insert into product_category(category_name, category_type) values (#{categoryName, jdbcType=VARCHAR}, #{category_type, jdbcType=INTEGER})")
    int insertByMap(Map<String, Object> map);

    /**
     * 新增
     * 通过对象注入
     * @param productCategory
     * @return
     */
    @Insert("insert into product_category(category_name, category_type) values (#{categoryName, jdbcType=VARCHAR}, #{categoryType, jdbcType=INTEGER})")
    int insertByObject(ProductCategory productCategory);


    /**
     * 查询，并指定要查询的字段
     *
     */
    @Select("select * from product_category where category_type = #{categoryType}")
    @Results({
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "category_name", property = "categoryName"),
            @Result(column = "category_type", property = "categoryType")
    })
    ProductCategory findByCategoryType(Integer categoryType);

    /**
     * 查询，并指定要查询的字段
     * @param categoryName
     * @return
     */
    @Select("select * from product_category where category_name = #{categoryName}")
    @Results({
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "category_name", property = "categoryName"),
            @Result(column = "category_type", property = "categoryType")
    })
    List<ProductCategory> findByCategoryName(String categoryName);

    /**
     * 通过参数更新
     * @param categoryName
     * @param categoryType
     * @return
     */
    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
    int updateByCategoryType(@Param("categoryName") String categoryName,
                             @Param("categoryType") Integer categoryType);

    /**
     * 通过对象更新
     * @param productCategory
     * @return
     */
    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
    int updateByObject(ProductCategory productCategory);

    /**
     * 删除，一般不会用，多是逻辑删除
     * @param categoryType
     * @return
     */
    @Delete("delete from product_category where category_type = #{categoryType}")
    int deleteByCategoryType(Integer categoryType);

    /**
     * 使用XML配置文件查询
     * @param categoryType
     * @return
     */
    ProductCategory selectByCategoryType(Integer categoryType);
}
