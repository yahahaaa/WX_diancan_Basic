package com.atzjhydx.weixindiancan.converter;

import com.atzjhydx.weixindiancan.dataobject.OrderDetail;
import com.atzjhydx.weixindiancan.dto.OrderDTO;
import com.atzjhydx.weixindiancan.enums.ResultEnum;
import com.atzjhydx.weixindiancan.exception.SellException;
import com.atzjhydx.weixindiancan.form.OrderForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderForm2OrderDTOConverter {

    /**
     * 将orderForm转为orderDTO
     * @param orderForm
     * @return
     */
    public static OrderDTO convert(OrderForm orderForm){
        OrderDTO orderDTO = new OrderDTO();
        //orderForm中的购物车属性为json字符串形式，需要用Gson工具类转为OrderDetail形式
        ObjectMapper objectMapper = new ObjectMapper();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetails;
        try{
            orderDetails = objectMapper.readValue(orderForm.getItems(),new TypeReference<List<OrderDetail>>(){});
        }catch (Exception e){
            log.error("【对象转换】错误，string={}",orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetails);

        return orderDTO;
    }
}
