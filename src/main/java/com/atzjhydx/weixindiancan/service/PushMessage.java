package com.atzjhydx.weixindiancan.service;

import com.atzjhydx.weixindiancan.dto.OrderDTO;

public interface PushMessage {

    /**
     * 订单状态变更消息
     * @param orderDTO
     */
    void orderStatus(OrderDTO orderDTO);
}
