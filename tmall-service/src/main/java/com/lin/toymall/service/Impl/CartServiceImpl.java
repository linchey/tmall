package com.lin.toymall.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lin.toymall.Service.CartService;
import com.lin.toymall.bean.OmsCartItem;
import com.lin.toymall.service.mapper.CartMapper;
import com.lin.toymall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public OmsCartItem isExitCartByUser(String memberId, String productId) {
        OmsCartItem omsCartItem=new OmsCartItem();
        omsCartItem.setMemberId( memberId );
        omsCartItem.setProductId( productId );
        omsCartItem=cartMapper.selectOne( omsCartItem );
        return omsCartItem;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        if(StringUtils.isNotBlank( omsCartItem.getMemberId() )){
            cartMapper.insertSelective( omsCartItem );
            flushCartCache(omsCartItem.getMemberId());
        }
    }

    @Override
    public void modifyCart(OmsCartItem omsCartItem1) {
        Example example=new Example( OmsCartItem.class );
        example.createCriteria().andEqualTo( "id",omsCartItem1.getId() );
        cartMapper.updateByExampleSelective(omsCartItem1,example);
        flushCartCache(omsCartItem1.getMemberId());
    }

    @Override
    public void flushCartCache(String memberId) {
        Jedis jedis=null;
        OmsCartItem omsCartItem=new OmsCartItem();
        omsCartItem.setMemberId( memberId );
        List<OmsCartItem> omsCartItems=cartMapper.select(omsCartItem);
        try{
            jedis= redisUtil.getJedis();
            Map<String,String> map=new HashMap<>();
            for (OmsCartItem cartItem:omsCartItems){
                cartItem.setTotalPrice(cartItem.getPrice().multiply( cartItem.getQuantity() ));
                map.put( cartItem.getProductId(), JSON.toJSONString(cartItem) );

            }
            String key="umsMember:"+memberId+":cart";
            jedis.del( "umsMember:"+memberId+":cart" );
            jedis.hmset( key,map );
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            jedis.close();
        }
    }

    @Override
    public List<OmsCartItem> cartList(String memberId) {
        List<OmsCartItem> omsCartItems=new ArrayList<>(  );
        Jedis jedis=null;
        try{
            jedis=redisUtil.getJedis();
            List<String> hvals = jedis.hvals("umsMember:" + memberId + ":cart");
            if(hvals.size()!=0){

                for (String hval:hvals){
                    OmsCartItem cartItem=JSON.parseObject( hval,OmsCartItem.class );
                    omsCartItems.add( cartItem );
                }
                return omsCartItems;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        //omsCartItems=selectCartListfromDb(memberId);
        flushCartCache( memberId );
        return omsCartItems;
    }

    private List<OmsCartItem> selectCartListfromDb(String memberId) {

        OmsCartItem omsCartItem=new OmsCartItem();
        omsCartItem.setMemberId( memberId );
        List<OmsCartItem> omsCartItems=cartMapper.select(omsCartItem);
        return omsCartItems;
    }


    @Override
    public void checkCart(OmsCartItem omsCartItem) {

        Example e = new Example(OmsCartItem.class);

        e.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productId",omsCartItem.getProductId());

        cartMapper.updateByExampleSelective(omsCartItem,e);

        // 缓存同步
        flushCartCache(omsCartItem.getMemberId());
    }

    @Override
    public void delCart(OmsCartItem omsCartItem) {
        Example e = new Example(OmsCartItem.class);

        e.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productId",omsCartItem.getProductId());

        cartMapper.deleteByExample(e);
        // 缓存同步
        flushCartCache(omsCartItem.getMemberId());
    }

    @Override
    public OmsCartItem findCartItem(String cartId) {
        OmsCartItem omsCartItem=cartMapper.selectByPrimaryKey( cartId );
        return omsCartItem;
    }


}
