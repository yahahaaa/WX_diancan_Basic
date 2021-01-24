package com.atzjhydx.weixindiancan.controller;

import com.atzjhydx.weixindiancan.config.ProjectUrlConfig;
import com.atzjhydx.weixindiancan.constant.CookieConstant;
import com.atzjhydx.weixindiancan.constant.RedisConstant;
import com.atzjhydx.weixindiancan.dataobject.SellerInfo;
import com.atzjhydx.weixindiancan.enums.ResultEnum;
import com.atzjhydx.weixindiancan.service.SellerService;
import com.atzjhydx.weixindiancan.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    /**
     * 登录
     * @param openid    微信扫码登录鉴权后获取openid直接重定向到该接口
     * @param response  用来设置cookie
     * @param map       当用户不存在时存储错误信息和跳转路径
     * @return
     */
    @GetMapping("login")
    public ModelAndView login(@RequestParam("openid")String openid,
                      HttpServletResponse response,
                      Map<String,Object> map){

        //1. openid去和数据库里的数据匹配
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if (sellerInfo == null){
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error");
        }

        //2. 设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        redisTemplate.opsForValue().set(RedisConstant.TOKEN_PREFIX.concat(token),openid,expire, TimeUnit.SECONDS);

        //3. 设置token至cookie
        CookieUtil.set(response,CookieConstant.TOKEN,token,expire);

        //使用绝对地址，跳转时使用完整http地址（跳转到订单列表页面）
        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/sell/seller/order/list");
    }

    /**
     * 登出，通过request获取用户cookie信息
     * @param request
     * @param response
     * @param map
     */
    @GetMapping("logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String,Object> map){
        //从cookie数组里查询，需要遍历数组找到我们想要的cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null){
            //清除redis
            redisTemplate.opsForValue().getOperations().delete(RedisConstant.TOKEN_PREFIX.concat(cookie.getValue()));

            //清除cookie，清除cookie时将时间设为0即可删除
            CookieUtil.set(response,CookieConstant.TOKEN,null,0);
        }

        map.put("msg",ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);
    }
}
