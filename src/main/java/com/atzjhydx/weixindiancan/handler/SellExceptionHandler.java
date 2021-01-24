package com.atzjhydx.weixindiancan.handler;

import com.atzjhydx.weixindiancan.VO.ResultVO;
import com.atzjhydx.weixindiancan.config.ProjectUrlConfig;
import com.atzjhydx.weixindiancan.exception.SellException;
import com.atzjhydx.weixindiancan.exception.SellerAuthorizeException;
import com.atzjhydx.weixindiancan.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class SellExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    public ResultVO handlerSellerException(SellException e){
        return ResultVOUtil.error(e.getCode(),e.getMessage());
    }

    /**
     * 如果未登录就去扫码登录
     * @return
     */
    @ExceptionHandler(value = SellerAuthorizeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handlerAuthorizeException(){
        return new ModelAndView("redirect:".
                concat(projectUrlConfig.getWechatOpenAuthorize()).
                //扫码登录后最后重定向到
                concat("/sell/wechat/qrAuthorize").
                concat("?returnUrl=").
                concat(projectUrlConfig.getSell()).
                concat("/sell/seller/login"));
    }

}
