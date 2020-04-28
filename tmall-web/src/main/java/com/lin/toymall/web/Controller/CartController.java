package com.lin.toymall.web.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lin.toymall.Service.CartService;
import com.lin.toymall.Service.ProductService;
import com.lin.toymall.annotations.LoginRequired;
import com.lin.toymall.bean.OmsCartItem;
import com.lin.toymall.bean.PmsProduct;
import com.lin.toymall.bean.UmsMember;
import com.lin.toymall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@RestController
public class CartController {
    @Reference
    CartService cartService;
    @Reference
    ProductService productService;
    /***
     * 加入购物车
     * @param productId 商品id
     * @param quantity 商品数量
     * @param request
     * @param response
     * @param session
     * @return
     */
    @PostMapping("addToCart")
    public ModelAndView addToCart(String productId, int quantity, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        ModelAndView mv=new ModelAndView(  );
        UmsMember user=(UmsMember) session.getAttribute( "umsMember" );
        String memberId="";
        if(user!=null){
            memberId=user.getId();
        }
        List<OmsCartItem> omsCartItems=new ArrayList<>();
        //调用商品服务查询商品信息
        PmsProduct product=productService.findProductById( productId );
        //封装成购物车信息
        OmsCartItem omsCartItem=new OmsCartItem();
        omsCartItem.setProductId( productId );
        omsCartItem.setMemberId( memberId );
        omsCartItem.setQuantity( new BigDecimal( quantity ) );
        omsCartItem.setPrice(  product.getPrice()  );
        omsCartItem.setProductPic( product.getDefaultImg() );
        omsCartItem.setProductName( product.getProductName() );
        omsCartItem.setTotalPrice( omsCartItem.getPrice().multiply( new BigDecimal( quantity )) );
        omsCartItem.setIsChecked( "1" );
        //判断用户是否登录，未登录将购物车放到用户cookie
        //cookie不存在，创建cookie，存在，判断是否已经有该购物项，合并cookie
        //用户登录,判断是否加入过该购物项，加入过，更新购物数量，没加入，插入数据
        if(StringUtils.isBlank( memberId )){
            String cartListCookies= CookieUtil.getCookieValue( request,"cartListCookies",true );
            if(StringUtils.isBlank( cartListCookies )){
                omsCartItems.add( omsCartItem );
            }else{
                omsCartItems= JSON.parseArray(cartListCookies,OmsCartItem.class);
                //判断是否已经加入过cookie
                boolean isExit=ifCartExist(omsCartItems,omsCartItem);
                if(isExit){
                    for (OmsCartItem cartItem:omsCartItems){
                        if(cartItem.getProductId().equals( omsCartItem.getProductId() )){
                            cartItem.setQuantity( cartItem.getQuantity().add( omsCartItem.getQuantity() ) );
                        }
                    }
                }else{
                    omsCartItems.add( omsCartItem );
                }
            }
            CookieUtil.setCookie( request,response,"cartListCookies", JSON.toJSONString(omsCartItems),60 * 60 * 72, true );

        }else{
            //登录状态,先从数据库里查找是否有该购物项
            OmsCartItem omsCartItem1=cartService.isExitCartByUser(memberId,productId);
            if(omsCartItem1==null){
                omsCartItem.setMemberId( memberId );
                cartService.addCart(omsCartItem);

            }else{
                omsCartItem1.setQuantity( omsCartItem1.getQuantity().add( omsCartItem.getQuantity() ) );
                cartService.modifyCart(omsCartItem1);
            }
            //同步缓存
            cartService.flushCartCache( memberId);

        }
        mv.addObject( "productInfo",product );
        mv.addObject("nums",quantity);
        mv.setViewName( "user/cart/success" );
        return mv;
    }

    private boolean ifCartExist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {
        boolean flag=false;
        for(OmsCartItem cartItem:omsCartItems){
            String productId=cartItem.getProductId();
            if( productId.equals( omsCartItem.getProductId())){
                return true;
            }
        }
        return flag;
    }

    /***
     * 选中框
     * @param isChecked
     * @param productId
     * @param request
     * @param response
     * @param session
     * @param modelMap
     * @return
     */
    @RequestMapping("checkCart")
    public ModelAndView checkCart(String isChecked,String productId,HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {
        ModelAndView mv=new ModelAndView(  );
        List<OmsCartItem> omsCartItems=new ArrayList<>(  );
        UmsMember user=(UmsMember) session.getAttribute( "umsMember" );
        String memberId = "";

        if(user!=null){
            memberId=user.getId();
        }
        if(StringUtils.isNotBlank( memberId )){
            // 将最新的数据从缓存中查出，渲染给内嵌页
            // 调用服务，修改状态
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setMemberId(memberId);
            omsCartItem.setProductId( productId );
            omsCartItem.setIsChecked(isChecked);
            cartService.checkCart(omsCartItem);
            omsCartItems = cartService.cartList(memberId);

        }else{
            String cartListCookes=CookieUtil.getCookieValue( request,"cartListCookies",true );
            if(StringUtils.isNotBlank( cartListCookes )){
                omsCartItems= JSON.parseArray(cartListCookes,OmsCartItem.class);
                for(OmsCartItem cartItem:omsCartItems){
                    if(productId.equals( cartItem.getProductId() )){
                        cartItem.setIsChecked( isChecked );
                        cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
                    }
                }
                CookieUtil.setCookie( request,response,"cartListCookies", JSON.toJSONString(omsCartItems),60 * 60 * 72, true );
            }
        }
        // 被勾选商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("cartList",omsCartItems);
        mv.setViewName( "user/cart/cartListInner" );
        return mv;
    }

    /***
     * 修改商品数量
     * @param productId
     * @param quantity
     * @param request
     * @param response
     * @param session
     * @param modelMap
     * @return
     */
    @RequestMapping("checkQuantity")
    public ModelAndView checkQuantity(String productId,String quantity,HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {
        ModelAndView mv=new ModelAndView(  );
        String memberId = "";
        List<OmsCartItem> omsCartItems=new ArrayList<>(  );
        UmsMember user=(UmsMember) session.getAttribute( "umsMember" );
        if(user!=null){
            memberId=user.getId();
        }

        if(StringUtils.isNotBlank( memberId )){
            // 调用服务，修改状态
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setMemberId(memberId);
            omsCartItem.setProductId( productId );
            omsCartItem.setQuantity( new BigDecimal(  quantity) );
            omsCartItem.setIsChecked("1");
            cartService.checkCart(omsCartItem);
            // 将最新的数据从缓存中查出，渲染给内嵌页
            omsCartItems = cartService.cartList(memberId);

        }else{
            //查询cookie
            String cartListCookes=CookieUtil.getCookieValue( request,"cartListCookies",true );
            if(StringUtils.isNotBlank( cartListCookes )){
                omsCartItems= JSON.parseArray(cartListCookes,OmsCartItem.class);
                for(OmsCartItem cartItem:omsCartItems){
                    if(productId.equals( cartItem.getProductId() )){
                        cartItem.setQuantity( new BigDecimal(quantity) );
                        cartItem.setIsChecked( "1" );
                        cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
                    }
                }
                CookieUtil.setCookie( request,response,"cartListCookies", JSON.toJSONString(omsCartItems),60 * 60 * 72, true );
            }
        }
        // 被勾选商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("cartList",omsCartItems);

        mv.setViewName( "user/cart/cartListInner" );
        return mv;
    }
    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            if(omsCartItem.getIsChecked().equals("1")){
                totalAmount = totalAmount.add(totalPrice);
            }
        }
        return totalAmount;
    }

    /***
     * 购物车列表
     * @param request
     * @param response
     * @param session
     * @param modelMap
     * @return
     */
    @GetMapping("cartList")
    @LoginRequired(loginSuccess = false)
    public ModelAndView cartList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap){

        ModelAndView mv=new ModelAndView();
        BigDecimal totalAmount=new BigDecimal( 0 );
        List<OmsCartItem>omsCartItems=new ArrayList<>( );
        UmsMember user=(UmsMember) session.getAttribute( "umsMember" );
        String memberId="";
        if(user!=null){
            memberId=user.getId();
        }
        String cartListCookes=CookieUtil.getCookieValue( request,"cartListCookies",true );
        if(StringUtils.isBlank( memberId )){
            //没有登录，查cookie
            if(StringUtils.isNotBlank( cartListCookes )){
                omsCartItems= JSON.parseArray(cartListCookes,OmsCartItem.class);
            }
        }else{
            //登录，查redis缓存,同时合并cookie里的购物车信息
            List<OmsCartItem> omsCartItems1=new ArrayList<>(  );
            if(StringUtils.isNotBlank( cartListCookes )){
               JSON.parseArray(cartListCookes,OmsCartItem.class);
                omsCartItems1= JSON.parseArray(cartListCookes,OmsCartItem.class);
            }
            omsCartItems = cartService.cartList(memberId);
            if(omsCartItems.size()!=0){
            for(OmsCartItem omsCartItem:omsCartItems){
                    for (OmsCartItem omsCartItem1:omsCartItems1){
                        //如果数据库里存在数据，quantity进行加操作,更新数据库
                        if(ifCartExist( omsCartItems, omsCartItem1 )){
                            omsCartItem.setQuantity(  omsCartItem.getQuantity().add( omsCartItem1.getQuantity()  ) );
                            cartService.modifyCart(omsCartItem);
                        }else{
                            //不存在，加入数据库
                          /*  omsCartItems.add( omsCartItem1 );
                            */
                            omsCartItem1.setMemberId( memberId );
                            cartService.addCart(omsCartItem1);

                        }
                    }
                }
            }else{
                //加入数据库
                for (OmsCartItem omsCartItem1:omsCartItems1){
                    //omsCartItems.add( omsCartItem1);
                    omsCartItem1.setMemberId( memberId );
                    cartService.addCart(omsCartItem1);
                }

            }

            omsCartItems = cartService.cartList(memberId);
            CookieUtil.setCookie( request,response,"cartListCookies", null,60 * 60 * 72, true );
        }
        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
        }
        totalAmount=getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        modelMap.put( "cartList",omsCartItems );
        mv.setViewName( "user/cart/cartList" );
        return mv;
    }

    /***
     * 批量删除
     * @param ids
     * @param request
     * @param response
     * @param session
     * @return
     */
    @PostMapping("delSelected")
    public ModelAndView delSelect(String ids,HttpServletRequest request,HttpServletResponse response,HttpSession session){
        ModelAndView mv=new ModelAndView();
        String [] productIds=ids.split( "," );
        List<OmsCartItem> omsCartItems=new ArrayList<>(  );
        UmsMember user=(UmsMember) session.getAttribute( "umsMember" );
        String memberId="";
        if(user!=null){
            memberId=user.getId();
        }
        //登录状态
        if(StringUtils.isNotBlank( memberId )){
            // 调用服务，修改状态
            OmsCartItem omsCartItem = new OmsCartItem();
            for(String productId:productIds){
                omsCartItem.setMemberId(memberId);
                omsCartItem.setProductId( productId );
                cartService.delCart(omsCartItem);
            }
            // 将最新的数据从缓存中查出，渲染给内嵌页
            omsCartItems = cartService.cartList(memberId);

        }else{
            //查询cookie
            String cartListCookes=CookieUtil.getCookieValue( request,"cartListCookies",true );
            if(StringUtils.isNotBlank( cartListCookes )){
                omsCartItems= JSON.parseArray(cartListCookes,OmsCartItem.class);
                ListIterator<OmsCartItem> iterator = omsCartItems.listIterator();
                while (iterator.hasNext()) {
                    OmsCartItem cartItem = iterator.next();
                    for(String productId:productIds){
                        if (productId.equals( cartItem.getProductId() ) ) {
                            iterator.remove();
                        }
                    }
                }
                CookieUtil.setCookie( request,response,"cartListCookies", JSON.toJSONString(omsCartItems),60 * 60 * 72, true );
            }

        }
        // 被勾选商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItems);
        mv.addObject("totalAmount",totalAmount);
        mv.addObject("cartList",omsCartItems);
        mv.setViewName( "user/cart/cartListInner" );
        return mv;

    }
    @GetMapping("delbyProductId")
    public ModelAndView delbyProductId(String productId,HttpServletRequest request,HttpServletResponse response,HttpSession session){
        ModelAndView mv=new ModelAndView();
        List<OmsCartItem> omsCartItems=new ArrayList<>(  );
        UmsMember user=(UmsMember) session.getAttribute( "umsMember" );
        String memberId="";
        if(user!=null){
            memberId=user.getId();
        }

        if(StringUtils.isNotBlank( memberId )){
            // 调用服务，修改状态
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setMemberId(memberId);
            omsCartItem.setProductId( productId );
            cartService.delCart(omsCartItem);
            // 将最新的数据从缓存中查出，渲染给内嵌页
            omsCartItems = cartService.cartList(memberId);

        }else{
            //查询cookie
            String cartListCookes=CookieUtil.getCookieValue( request,"cartListCookies",true );
            if(StringUtils.isNotBlank( cartListCookes )) {
                omsCartItems = JSON.parseArray( cartListCookes, OmsCartItem.class );
                ListIterator<OmsCartItem> iterator = omsCartItems.listIterator();
                while (iterator.hasNext()) {
                    OmsCartItem cartItem = iterator.next();
                    if (productId.equals( cartItem.getProductId() )) {
                        iterator.remove();
                    }
                }
                CookieUtil.setCookie( request, response, "cartListCookies", JSON.toJSONString( omsCartItems ), 60 * 60 * 72, true );

            }
            }
        // 被勾选商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItems);
        mv.addObject("totalAmount",totalAmount);
        mv.addObject("cartList",omsCartItems);
        mv.setViewName( "user/cart/cartListInner" );
        return mv;
    }


}

