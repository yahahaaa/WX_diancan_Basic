package com.atzjhydx.weixindiancan.service.impl;

import com.atzjhydx.weixindiancan.dataobject.SellerInfo;
import com.atzjhydx.weixindiancan.repository.SellerInfoRepository;
import com.atzjhydx.weixindiancan.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    /**
     * 根据openid查询卖家信息
     * @param openid
     * @return
     */
    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        SellerInfo sellerInfo = repository.findByOpenid(openid);
        return sellerInfo;
    }
}
