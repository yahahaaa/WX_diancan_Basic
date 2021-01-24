package com.atzjhydx.weixindiancan.service;

import com.atzjhydx.weixindiancan.dataobject.SellerInfo;

public interface SellerService {
    /**
     * 通过openid查询卖家信息
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
