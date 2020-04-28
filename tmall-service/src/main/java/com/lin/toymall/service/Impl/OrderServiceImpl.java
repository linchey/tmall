package com.lin.toymall.service.Impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lin.toymall.Service.CartService;
import com.lin.toymall.Service.OrderService;
import com.lin.toymall.bean.OmsCartItem;
import com.lin.toymall.bean.OmsOrder;
import com.lin.toymall.bean.OmsOrderItem;
import com.lin.toymall.service.mapper.OrderItemMapper;
import com.lin.toymall.service.mapper.OrderMapper;
import com.lin.toymall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Reference
    CartService cartService;
    /***
     * 生成交易码
     * @param memerId
     * @return
     */
    @Override
    public String getTradeCode(String memerId) {
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            String key="user:"+memerId+":TradeCode";
            String tradeCode= UUID.randomUUID().toString();
            jedis.setex(key,60*15,tradeCode);
            return  tradeCode;
        }finally {
            jedis.close();
        }
    }

    /***
     * 校验交易码
     * @param memerId
     * @return
     */
    @Override
    public String checkTradCode(String memerId, String tradeCode) {
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            String key="user:"+memerId+":TradeCode";
            jedis.get( key );
           //验证成功，使用lua脚本删除缓存数据，防止高并发情况下过次验证通过
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Long eval=(Long) jedis.eval( script, Collections.singletonList( key ),Collections.singletonList( tradeCode ) );
            if(eval!=null&&eval!=0){
                return  "success";
            }else{
                return "fail";
            }

        }finally {
            jedis.close();
        }
    }

    /**
     * 订单保存并删除购物车信息
     * @param omsOrder
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void saveOrder(OmsOrder omsOrder) {
        // 保存订单表
        orderMapper.insertSelective(omsOrder);
        String orderId = omsOrder.getId();
        String memberId=omsOrder.getMemberId();
        // 保存订单详情
        List<OmsOrderItem> omsOrderItems = omsOrder.getOmsOrderItems();
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            omsOrderItem.setOrderId(orderId);
            OmsCartItem cartItem=cartService.findCartItem(omsOrderItem.getCartId());
            orderItemMapper.insertSelective(omsOrderItem);
            // 删除购物车数据
             cartService.delCart(cartItem);
        }
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            String key="orderId:"+orderId+":info";
            String order=JSON.toJSONString(omsOrder);
            jedis.setex( key,60*15, order);

        }finally {
            jedis.close();
        }

    }

    @Override
    public List<OmsOrder> findAll() {
        List<OmsOrder> omsOrders=orderMapper.selectAll();
        return omsOrders;
    }

    @Override
    public List<OmsOrderItem> getOrderItems(String orderId) {

        List<OmsOrderItem> omsOrderItems= new ArrayList<>(  );
        OmsOrder order=new OmsOrder();
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            String orderStr=jedis.get(  "orderId:"+orderId+":info");
            if (StringUtils.isNotBlank( orderStr )){
                order= JSON.parseObject(orderStr,OmsOrder.class);
                omsOrderItems=order.getOmsOrderItems();
                return omsOrderItems;
            }

        }finally {
            jedis.close();

        }
        OmsOrderItem omsOrderItem=new OmsOrderItem();
        omsOrderItem.setOrderId( orderId );
        omsOrderItems=orderItemMapper.select( omsOrderItem );
        order.setId( orderId );
        order.setOmsOrderItems( omsOrderItems );
        flushOrderCache(order);
        return omsOrderItems;
    }

    private void flushOrderCache(OmsOrder omsOrder) {
        Jedis jedis=null;
        String orderId=omsOrder.getId();
        try {
            jedis=redisUtil.getJedis();
            jedis.del(  "orderId:"+orderId+":info" );
            String order=JSON.toJSONString( omsOrder );
            jedis.setex( "orderId:"+orderId+":info",60*15, order);
        }finally {
            jedis.close();

        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public String delOrder(String orderId) {

        //删除order
        orderMapper.deleteByPrimaryKey( orderId );
        //删除orderItem
        List<OmsOrderItem> omsOrderItems=getOrderItems( orderId );
        for (OmsOrderItem omsOrderItem:omsOrderItems){
            Example example=new Example( OmsOrderItem.class );
            example.createCriteria().andEqualTo( "orderId" ,omsOrderItem.getOrderId());
            orderItemMapper.deleteByExample( example );
        }
        //删除缓存
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            jedis.del(  "orderId:"+orderId+":info" );
        }finally {
            jedis.close();
        }
        return "success";
    }

    @Override
    public List<OmsOrder> findAllOrder(String memberId) {
        OmsOrder omsOrder=new OmsOrder();
        omsOrder.setMemberId( memberId );
        List<OmsOrder> omsOrders=new ArrayList<>(  );
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            String omsOrdersStr=jedis.get("user:"+memberId+":orders");
            if(StringUtils.isNotBlank( omsOrdersStr )){
                omsOrders= JSON.parseArray(omsOrdersStr,OmsOrder.class);
                return omsOrders;
            }
        }finally {
            jedis.close();
        }
        omsOrders=orderMapper.select( omsOrder );

       // jedis.setex( "user:"+memberId+":orders",60*60*24, JSON.toJSONString(omsOrders));
        return omsOrders;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void modifyOrder(String orderId,String memberId) {
        OmsOrder omsOrder=orderMapper.selectByPrimaryKey( orderId );
        omsOrder.setStatus( "1" );
        orderMapper.updateByPrimaryKeySelective( omsOrder );
        omsOrder.setOmsOrderItems( getOrderItems( orderId ) );
        flushOrderCache( omsOrder );
       /* Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            String order=JSON.toJSONString(omsOrder);
            jedis.del( "orderId:"+orderId+":info" );
            jedis.setex( "orderId:"+orderId+":info",60*15, order);
        }finally {
            jedis.close();
        }*/
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void cancelOrder(String orderId) {
        OmsOrder omsOrder=orderMapper.selectByPrimaryKey( orderId );
        omsOrder.setStatus( "2" );
        orderMapper.updateByPrimaryKeySelective( omsOrder );
        omsOrder.setOmsOrderItems( getOrderItems( orderId ) );
        flushOrderCache( omsOrder );
       /* Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            String order=JSON.toJSONString(omsOrder);
            jedis.del( "orderId:"+orderId+":info" );
            jedis.setex( "orderId:"+orderId+":info",60*15, order);
        }finally {
            jedis.close();
        }*/
    }
}
