package com.atzjhydx.weixindiancan.controller;

import com.atzjhydx.weixindiancan.config.ProjectUrlConfig;
import com.atzjhydx.weixindiancan.enums.ResultEnum;
import com.atzjhydx.weixindiancan.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 微信网页授权
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    /**
     * 第一步：用户同意授权，获取code
     *
     * 在确保微信公众账号拥有授权作用域（scope参数）的权限的前提下（服务号获得高级接口后，默认拥有scope参数中的
     * snsapi_base和snsapi_userinfo），引导关注者打开如下页面：
     *
     * https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
     * 若提示“该链接无法访问”，请检查参数是否填写错误，是否拥有scope参数对应的授权作用域权限。
     *
     * 参考链接(请在微信客户端中打开此链接体验):
     *
     * scope为snsapi_base
     * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx520c15f417810387&redirect_uri=https%3A%2F%2Fchong.qq.com%2Fphp%2Findex.php%3Fd%3D%26c%3DwxAdapter%26m%3DmobileDeal%26showwxpaytitle%3D1%26vb2ctag%3D4_2030_5_1194_60&response_type=code&scope=snsapi_base&state=123#wechat_redirect
     *
     * scope为snsapi_userinfo
     * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf0e81c3bee622d60&redirect_uri=http%3A%2F%2Fnba.bluewebgame.com%2Foauth_response.php&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect
     *
     * 尤其注意：跳转回调redirect_uri，应当使用https链接来确保授权code的安全性。
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl")String returnUrl) throws UnsupportedEncodingException {

        //1.配置（配置在WechatMpConfig配置文件中）
        //2.调用方法
        String url = projectUrlConfig.getWechatMpAuthorize() + "/wechat/userInfo"; //授权回调地址，回调下面的userInfo接口
        //构造授权地址
        //sdk的说明文档地址:https://github.com/Wechat-Group/WxJava/wiki/MP_OAuth2%E7%BD%91%E9%A1%B5%E6%8E%88%E6%9D%83
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl,"UTF-8"));
        log.info("【微信网页授权】获取code，result={}", redirectUrl);
        return "redirect:" + redirectUrl;
    }

    /**
     * 第二步：通过code换取网页授权access_token
     *
     * 首先请注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。
     * 公众号可通过下述接口来获取网页授权access_token。如果网页授权的作用域为snsapi_base，则本步骤中获取到网页授权access_token的同时，
     * 也获取到了openid，snsapi_base式的网页授权流程即到此为止。
     *
     * 尤其注意：由于公众号的secret和获取到的access_token安全级别都非常高，必须只保存在服务器，不允许传给客户端。后续刷新access_token、
     * 通过access_token获取用户信息等步骤，也必须从服务器发起。
     *
     * 请求方法
     *
     * 获取code后，请求以下链接获取access_token： https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code")String code,
                         @RequestParam("state")String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try{
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code); //取得code后再次请求某地址，获取openid
        }catch (WxErrorException e){
            log.error("【微信网页授权】{} ",e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:" + returnUrl + "?openid=" + openId;
    }

    /**
     * 卖家扫码登录（微信开放平台）
     * @param returnUrl
     * @return
     */
    @GetMapping("/qrAuthorize")
    public String qrAuthorize(@RequestParam("returnUrl")String returnUrl) throws UnsupportedEncodingException {
        String url = projectUrlConfig.getWechatOpenAuthorize() + "/sell/wechat/qrUserInfo"; //授权回调，回调下面的quUserInfo接口
        String redirectUrl = wxOpenService.buildQrConnectUrl(url,WxConsts.QrConnectScope.SNSAPI_LOGIN, URLEncoder.encode(returnUrl,"UTF-8"));
        return "redirect:" + redirectUrl;
    }


    @GetMapping("qrUserInfo")
    public String qrUserInfo(@RequestParam("code")String code,
                             @RequestParam("state")String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try{
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);//再次请求某地址，获取accessToken和openid
        }catch (WxErrorException e){
            log.error("【微信网页授权】{}",e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl + "?openid=" + openId;
    }
}
