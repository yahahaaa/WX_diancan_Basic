package com.atzjhydx.weixindiancan.aspect;

import com.atzjhydx.weixindiancan.constant.CookieConstant;
import com.atzjhydx.weixindiancan.constant.RedisConstant;
import com.atzjhydx.weixindiancan.exception.SellerAuthorizeException;
import com.atzjhydx.weixindiancan.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

//@Aspect
//@Component
//@Slf4j
//public class SellerAuthorizeAspect {
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    //设置切面，排除了登录登出的方法
//    @Pointcut("execution(public * com.atzjhydx.weixindiancan.controller.Seller*.*(..))" +
//    "&& !execution(public * com.atzjhydx.weixindiancan.controller.SellerUserController.*(..))")
//    public void verify(){}
//
//    @Before("verify()")
//    public void doVerify(){
//        //使用RequestContextHolder可以不用从controller层接口从参数获取request对象
//        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        //查询cookie中是否有token（openid组成）
//        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
//        if (cookie == null){
//            log.warn("【登录校验】Cookie中查询不到token");
//            //ExceptionHandler接口在感知到这个异常后，会去调用登录接口，实现没有登录就无法访问卖家系统
//            throw new SellerAuthorizeException();
//        }
//
//        //取redis中查询（查询redis中是否缓存了openid，当主机域名不同时，cookie无效，需要使用redis来共享token）
//        String tokenValue = redisTemplate.opsForValue().get(RedisConstant.TOKEN_PREFIX.concat(cookie.getValue()));
//        if (StringUtils.isEmpty(tokenValue)){
//            log.warn("【登录校验】redis中查询不到token");
//            throw new SellerAuthorizeException();
//        }
//    }
//}
