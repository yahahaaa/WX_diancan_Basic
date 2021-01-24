package com.atzjhydx.weixindiancan.service.impl;

import com.atzjhydx.weixindiancan.dataobject.ProductInfo;
import com.atzjhydx.weixindiancan.dataobject.mapper.ProductMapper;
import com.atzjhydx.weixindiancan.dto.CartDTO;
import com.atzjhydx.weixindiancan.enums.ProductStatusEnum;
import com.atzjhydx.weixindiancan.enums.ResultEnum;
import com.atzjhydx.weixindiancan.exception.SellException;
import com.atzjhydx.weixindiancan.repository.ProductInfoRepository;
import com.atzjhydx.weixindiancan.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository repository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    //@Cacheable(cacheNames = "product", key = "123")
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).orElse(null);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    //@CachePut(cacheNames = "product",key = "123")
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO:cartDTOList){
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
            //利用数据库行锁修改库存信息 and stock > 0
            if (productInfo == null){
                log.error("【增库存】商品不存在，当前商品ID为：{}",cartDTO.getProductId());
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            productMapper.increaseStock(cartDTO.getProductQuantity(),cartDTO.getProductId());
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO:cartDTOList){
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
            //检验购物车中的商品是否真实存在
            if (productInfo == null){
                log.error("【减库存】商品不存在，当前商品ID为：{}",cartDTO.getProductId());
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //查询当前库存是否还够此次消费
//            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
//            if (result < 0){
//                throw new SellException(ResultEnum.PRODUCT_STOCK_NOT_ENOUGH);
//            }
            //倘若还够的话，使用乐观锁机制，防止超卖问题
            int results = productMapper.decreaseStock(cartDTO.getProductQuantity(), cartDTO.getProductId());
            //如果results为1，表示成功扣减库存，如果不为1，说明由于并发问题，此时数据库中的库存已经不够了
            if (results != 1){
                throw new SellException(ResultEnum.PRODUCT_STOCK_NOT_ENOUGH);
            }
        }
    }

    @Override
    @Transactional
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = repository.findById(productId).orElse(null);
        if (productInfo == null){
            log.error("【商品上架】商品ID：{} 不存在",productId);
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatusEnum() == ProductStatusEnum.UP){
            log.error("【商品上架】商品状态不正确,当前商品状态为：{}",productInfo.getProductStatusEnum().getMessage());
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //更新
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = repository.findById(productId).orElse(null);
        if (productInfo == null){
            log.error("【商品下架】商品ID:{} 不存在",productId);
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatusEnum() != ProductStatusEnum.UP){
            log.error("【商品下架】商品状态不正确,当前商品状态为：{}",productInfo.getProductStatusEnum().getMessage());
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //更新
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(productInfo);
    }

    @Override
    public void updateCategoryType(Integer originCategoryType,Integer newCategoryType) {
        productMapper.updateCategoryType(originCategoryType,newCategoryType);
    }
}
