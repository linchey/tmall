package com.lin.toymall.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lin.toymall.Service.CartService;
import com.lin.toymall.Service.OrderService;
import com.lin.toymall.Service.ProductService;
import com.lin.toymall.Service.UserAddressService;
import com.lin.toymall.annotations.LoginRequired;
import com.lin.toymall.bean.OmsCartItem;
import com.lin.toymall.bean.OmsOrder;
import com.lin.toymall.bean.OmsOrderItem;
import com.lin.toymall.bean.UmsMemberReceiveAddress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Reference
    CartService cartService;
    @Reference
    OrderService orderService;
    @Reference
    UserAddressService userAddressService;
    @Reference
    ProductService productService;
    @Reference
    UserAddressService addressService;
    //去结算页面
    @GetMapping("/toTrade")
    @LoginRequired(loginSuccess = true)
    public ModelAndView toTrade(HttpServletRequest request, HttpSession session){
        ModelAndView mv=new ModelAndView("user/order/trade");
        String memerId= (String) request.getAttribute( "memberId" );
        String userName= (String) request.getAttribute( "username" );
        List<UmsMemberReceiveAddress> addressList=userAddressService.findByUserId( memerId );
        //将购物车集合转化为页面计算清单集合
        List<OmsCartItem> cartItems=cartService.cartList( memerId );
        //订单项目列表
        List<OmsOrderItem> omsOrderItems=new ArrayList<>();
        for (OmsCartItem omsCartItem:cartItems){
            if("1".equals( omsCartItem.getIsChecked() )){
                OmsOrderItem omsOrderItem=new OmsOrderItem();
                omsOrderItem.setProductId( omsCartItem.getProductId() );
                omsOrderItem.setProductName( omsCartItem.getProductName() );
                omsOrderItem.setProductPic( omsCartItem.getProductPic() );
                omsOrderItem.setQuantity( omsCartItem.getQuantity() );
                omsOrderItems.add( omsOrderItem );
            }
        }
        //生成交易码
        String tradeCode=orderService.getTradeCode(memerId);
        mv.addObject( "tradeCode",tradeCode );
        mv.addObject("omsOrderItems", omsOrderItems);
        mv.addObject("userAddressList", addressList);
        mv.addObject(  "totalAmount", getTotalAmount(cartItems));

        return mv;
    }

    /***
     * 订单提交
     * @param receiveAddressId
     * @param totalAmount
     * @param tradeCode
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("submitOrder")
    @LoginRequired(loginSuccess = true)
    public ModelAndView submitOrder(String receiveAddressId, BigDecimal totalAmount,String note, String tradeCode, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        ModelAndView mv=new ModelAndView(  );
        String memerId= (String) request.getAttribute( "memberId" );
        String username=(String) request.getAttribute( "username" );
        //检查交易码
        String checkResult=orderService.checkTradCode(memerId,tradeCode);
        if("success".equals( checkResult )){
            //检验交易码通过
            List<OmsOrderItem>omsOrderItems=new ArrayList<>();
            OmsOrder omsOrder=new OmsOrder();
            omsOrder.setCreateTime( new Date(  ) );
            omsOrder.setModifyTime(new Date(  ));
            String orderCode="toymall";
            //将时间戳拼接到订单号
            SimpleDateFormat sdf=new SimpleDateFormat( "YYYYMMDDHHmmss" );
            orderCode=orderCode+System.currentTimeMillis();
            omsOrder.setOrderCode( orderCode );
            UmsMemberReceiveAddress receiveAddress=addressService.getAddressById( receiveAddressId );
            omsOrder.setReceviceCity(receiveAddress.getCity());
            omsOrder.setReceviceDetailAddress(receiveAddress.getDetailAddress());
            omsOrder.setReceviceName(receiveAddress.getName());
            omsOrder.setRecevicePhone(receiveAddress.getPhone());
            omsOrder.setRecevicePostCode(receiveAddress.getPostCode());
            omsOrder.setReceviceProvince(receiveAddress.getProvince());
            omsOrder.setReceviceRegion(receiveAddress.getRegion());
            omsOrder.setTotalAmount( totalAmount );
            omsOrder.setMemberName(username);
            omsOrder.setStatus( "0");

            List<OmsCartItem> omsCartItems=cartService.cartList( memerId );
            if(omsCartItems.size()==0){
                mv.setViewName( "user/order/tradeFail" );
                return mv;
            }
            for (OmsCartItem cartItem:omsCartItems){
                if("1".equals( cartItem.getIsChecked() )){
                    //校验价格
                    OmsOrderItem omsOrderItem=new OmsOrderItem();
                    boolean checkPrice=productService.checkPrice(cartItem.getProductId(),cartItem.getPrice());
                    if(checkPrice==false){
                        mv.setViewName( "user/order/tradeFail" );
                        return mv;
                    }
                    //校验库存
                    omsOrderItem.setOrderCode( orderCode );
                    omsOrderItem.setProductId( cartItem.getProductId() );
                    omsOrderItem.setQuantity( cartItem.getQuantity() );
                    omsOrderItem.setProductPic( cartItem.getProductPic() );
                    omsOrderItem.setProductName( cartItem.getProductName() );

                    omsOrderItem.setTotalAmount(cartItem.getTotalPrice());

                    omsOrderItem.setCartId( cartItem.getId() );
                    omsOrderItems.add( omsOrderItem );
                }
            }
            //保存订单信息
            omsOrder.setMemberId( memerId );
            omsOrder.setOmsOrderItems( omsOrderItems );
            omsOrder.setNote( note );
            orderService.saveOrder(omsOrder);
            //进入支付页面
            mv.setViewName( "user/pay/pay" );
        }else{
            mv.setViewName( "user/order/tradeFail" );
            return mv;
        }
        return mv;
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            if ("1".equals( omsCartItem.getIsChecked())) {
                totalAmount = totalAmount.add(totalPrice);
            }
        }

        return totalAmount;
    }
    @RequestMapping("delOrder")
    @LoginRequired(loginSuccess = true)
    public ModelAndView delOrder(String orderId){
        ModelAndView mv=new ModelAndView(  );
        orderService.delOrder( orderId );
        mv.setViewName( "redirect:/user/myOrder" );
        return mv;
    }
    @RequestMapping("cancelOrder")
    @LoginRequired(loginSuccess = true)
    public ModelAndView cancelOrder(String orderId){
        ModelAndView mv=new ModelAndView(  );
        orderService.cancelOrder( orderId );
        mv.setViewName( "redirect:/user/myOrder" );
        return mv;
    }
}
