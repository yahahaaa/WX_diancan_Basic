package com.atzjhydx.weixindiancan.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductForm {


    private String productId;

    /** 名字. */
    @NotEmpty(message = "商品名不能为空")
    private String productName;

    /** 单价. */
    @NotNull(message = "商品价格不能为空")
    private BigDecimal productPrice;

    /** 库存. */
    @NotNull(message = "商品库存不能为空")
    private Integer productStock;

    /** 描述. */
    @NotEmpty(message = "商品描述不能为空")
    private String productDescription;

    /** 小图. */
    @NotEmpty(message = "商品小图不能为空")
    private String productIcon;

    /** 类目编号. */
    @NotNull(message = "商品类目编号不能为空")
    private Integer categoryType;
}
