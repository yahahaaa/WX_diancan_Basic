package com.atzjhydx.weixindiancan.dataobject;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@ToString
@Entity
@DynamicUpdate
public class ProductCategory {

    //类目id（主键、自增类型）
    @Id
    @GeneratedValue
    private Integer categoryId;

    //类目名字
    private String categoryName;

    //类目编号
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    public ProductCategory() {}

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
