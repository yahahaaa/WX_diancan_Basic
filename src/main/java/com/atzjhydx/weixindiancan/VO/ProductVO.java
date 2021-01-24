package com.atzjhydx.weixindiancan.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品（包含类目）
 */
@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 2404267203772573165L;

    @JsonProperty("name")   //配合文档中定义的属性名称
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

}
