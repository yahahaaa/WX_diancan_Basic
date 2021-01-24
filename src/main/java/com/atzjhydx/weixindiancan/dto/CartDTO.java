package com.atzjhydx.weixindiancan.dto;

import lombok.Data;

/**
 * 购物车
 */
@Data
public class CartDTO {

    //商品id
    private String productId;
    //购买的商品数量
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
