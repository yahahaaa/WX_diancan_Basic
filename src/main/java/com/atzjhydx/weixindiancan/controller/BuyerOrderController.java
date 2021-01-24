package com.atzjhydx.weixindiancan.controller;

import com.atzjhydx.weixindiancan.VO.ResultVO;
import com.atzjhydx.weixindiancan.converter.OrderForm2OrderDTOConverter;
import com.atzjhydx.weixindiancan.dto.OrderDTO;
import com.atzjhydx.weixindiancan.enums.ResultEnum;
import com.atzjhydx.weixindiancan.exception.SellException;
import com.atzjhydx.weixindiancan.form.OrderForm;
import com.atzjhydx.weixindiancan.service.BuyerService;
import com.atzjhydx.weixindiancan.service.OrderService;
import com.atzjhydx.weixindiancan.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    /**
     * 创建订单，当前用户创建
     * @param orderForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm,
                                               BindingResult bindingResult){
        //校参（orderForm中所有的属性都有非空验证，非空验证结果保存在bindingResult）
        if (bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确，orderForm={}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        //将orderForm类型转为orderDTO类型（orderForm是前端传向后台的属性，orderDTO是后台业务之间流转的属性）
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);

        //购物车不能为空
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        //创建订单业务层
        OrderDTO createResult = orderService.create(orderDTO);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",createResult.getOrderId());
        return ResultVOUtil.success(map);
    }

    /**
     * 订单列表，当前用户查询订单列表
     * @param openid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page",defaultValue = "0")Integer page,
                                         @RequestParam(value = "size",defaultValue = "10")Integer size){

        //检验用户是否登录
        if (StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        //分页查询
        PageRequest request = PageRequest.of(page,size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);

        return ResultVOUtil.success(orderDTOPage.getContent());
    }

    /**
     * 订单详情
     * @param openid
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid")String openid,
                                     @RequestParam("orderId")String orderId){

        //根据orderId查询对应的订单详情
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderDTO);
    }

    /**
     * 取消订单
     * @param openid
     * @param orderId
     * @return
     */
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid")String openid,
                           @RequestParam("orderId")String orderId){

       buyerService.cancelOrder(openid, orderId);

       return ResultVOUtil.success();
    }
}
