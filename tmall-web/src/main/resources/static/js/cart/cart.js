
    function toTrade(){
        window.location.href="http://localhost:8081/order/toTrade";
    }
    function toItem(productId) {
        window.location.href="http://localhost:8081/item/"+productId+".html";
    }



//勾选框改变，异步更新选中状态
    function checkProductId( chkbox){
        var productId= $(chkbox).attr("value");
        var checked=$(chkbox).prop("checked");

        var isCheckedFlag="0";
        if(checked){
            isCheckedFlag="1";
        }
        $.post("checkCart","isChecked="+isCheckedFlag+"&"+"productId="+productId,function (html) {
            // 服务会返回一个inner内嵌页面给ajax，返回的新的页面刷新替换掉原来的老的页面
            $("#cartListInner").html(html);

        });
    }
    //改变数量，异步跟新
    function checkQuantity(quantity,productId){
        var quantity= $(quantity).text();
        $.post("checkQuantity","quantity="+quantity+"&"+"productId="+productId,function (html) {
            // 服务会返回一个inner内嵌页面给ajax，返回的新的页面刷新替换掉原来的老的页面
            $("#cartListInner").html(html);

        }).first();
    }
/*
$(document).on("click",".One_ShopCon")
*/
    //购物车顶端tab
    $(".One_Topleft span:last-child").mouseover(function(){
        $(".One_Topleft span:first-child").css({"color":"black","border-bottom":"none"})
        $(this).css({"cursor":"pointer","color":"#E4393C","border-bottom":"2px solid red"})
    }).mouseout(function(){
        $(this).css({"color":"black","border-bottom":"none"});
        $(".One_Topleft span:first-child").css({"cursor":"pointer","color":"#E4393C","border-bottom":"2px solid red"})
    })
    //立即登录
    $(".one_search_load input:button").click(function(){
        $(".One_mb").show();
        $(".One_DengLu").show();
    })
    //去结算
    $(".One_ShopFootBuy>div:last-child button").mouseover(function(){
        $(this).css("cursor","pointer");
    })
    $(".One_ShopFootBuy>div:last-child button").click(function(){
        $(".One_mb").show();
        $(".One_DengLu").show();
    })
    //buyNum
    $(".One_ShopFootBuy>div:nth-child(2)").mouseover(function(){
        $(this).css("cursor","pointer")
    })
    $(".One_ShopFootBuy>div:nth-child(2)").click(function(){
        $(this).children("ol").toggle();
        $(this).children("ul").toggle();
        var dis=$(".One_ShopFootBuy>div:nth-child(2) ol").css("display");
        if(dis=="none"){
            $(".One_ShopFootBuy>div:nth-child(2) img").css("transform","rotateX(0)")
        }else if(dis=="block"){
            $(".One_ShopFootBuy>div:nth-child(2) img").css("transform","rotateX(180deg)")
        }
    })
    //右侧固定定位
    $(".One_RightFix ul>li:not(:first-child)").mouseover(function(){
        $(this).css({"cursor":"pointer","background":"#C91423"})
        $(this).children("ol").stop().animate({"left":"-75px"},200)
    }).mouseout(function(){
        $(this).css("background","#7A6E6E");
        $(this).children("ol").stop().animate({"left":"75px"},200)
    })
    //右侧底部固定定位
    $(".One_RightbtmFix ul>li").mouseover(function(){
        $(this).css({"cursor":"pointer","background":"#C91423"})
        $(this).children("ol").stop().animate({"left":"-55px"},200)
    }).mouseout(function(){
        $(this).css("background","#7A6E6E");
        $(this).children("ol").stop().animate({"left":"55px"},200)
    })
    //登录弹框tab切换
    $(".One_DengLu>div:nth-child(3) ul li").mouseover(function(){
        $(this).css("cursor","pointer")
    })
    $(".One_DengLu>div:nth-child(3) ul li").click(function(){
        var i=$(this).index();
        $(this).css({"color":"#E64346","font-weight":"bold"})
            .siblings("li").css({"color":"black","font-weight":"normal"})
        $(".One_DengLu>div:nth-child(3) ol li").eq(i).show().siblings().hide()
    })

    $(".One_Local>ul>li").eq(0).children("ol").css("display","block")
    $(".One_Local>ul>li").mouseover(function(){
        var i=$(this).index();
        $(this).css("cursor","pointer");
        $(this).children("ol").css("display","block")
    }).mouseout(function(){
        $(".One_Local>ul>li").eq(0).children("ol").css("display","block")
        $(this).children("ol").css("display","none")
    })

    $(".One_Local>ul>li>ol li").mouseover(function(){
        $(this).css({"cursor":"pointer","color":"#e64346"})
    }).mouseout(function(){
        $(this).css("color","#005AA0")
    })

    $(".One_Local>ul>li>ol li").click(function(){
        $(this).parent().parent().children("label").html($(this).html())
    })
    //购物车全选反选
    $(".One_ShopTop ul li:first-child .allCheck").click(function(){
        if($(".One_ShopTop ul li:first-child .allCheck").is(":checked")){
            // $(".check").each(function(index){
            $(".check").prop("checked",true);

            $(".check").parent().parent().parent().css("background","#fff4e8");
            $(".One_ShopBottom>div:first-child span:first-child .allCheck").prop("checked",true)
            $(".One_ShopFootBuy>div:first-child ul li:first-child .allCheck").prop("checked",true)
            // })
        }else{
            // $(".check").each(function(){
            $(".check").parent().parent().parent().css("background","none");
            $(".check").prop("checked",false);
            $(".One_ShopBottom>div:first-child span:first-child .allCheck").prop("checked",false)
            $(".One_ShopFootBuy>div:first-child ul li:first-child .allCheck").prop("checked",false)
            // })
        }
    })
    $(".One_ShopFootBuy>div:first-child ul li:first-child .allCheck").click(function(){
        if($(".One_ShopFootBuy>div:first-child ul li:first-child .allCheck").is(":checked")){
            $(".check").prop("checked",true);
            $(".check").parent().parent().parent().css("background","#fff4e8");
            $(".One_ShopBottom>div:first-child span:first-child .allCheck").prop("checked",true)
            $(".One_ShopTop ul li:first-child .allCheck").prop("checked",true);
        }else{
            $(".check").parent().parent().parent().css("background","none");
            $(".check").prop("checked",false);
            $(".One_ShopBottom>div:first-child span:first-child .allCheck").prop("checked",false)
            $(".One_ShopTop ul li:first-child .allCheck").prop("checked",false);
        }
    })
    $(".One_ShopBottom>div:first-child span:first-child .allCheck").click(function(){
        if($(".One_ShopBottom>div:first-child span:first-child .allCheck").is(":checked")){
            $(".check").prop("checked",true);
            $(".check").parent().parent().parent().css("background","#fff4e8");
        }else{
            $(".check").parent().parent().parent().css("background","none");
            $(".check").prop("checked",false);
        }
    })
    //全选框改变触发子选框的change
    $(".One_ShopFootBuy>div:first-child ul li:first-child .allCheck").change(function () {
        if($(this).is(":checked")){
            $(".check").prop("checked",true);
            $(".check").change()
        }else {
            $(".check").prop("checked",false).onChange;
            $(".check").change()
        }
    })
    $(".One_ShopTop ul li:first-child .allCheck").change(function(){
        if($(this).is(":checked")){
            $(".check").prop("checked",true);
            $(".check").change()
        }else {
            $(".check").prop("checked",false).onChange;
            $(".check").change()
        }
    })
    //页面加载完后，子选框全选中，全选框勾选
    $(function(){
        if($(".check[class='check']:checked").length==$(".check[class='check']").length&&$(".check[class='check']").length!=0){
            $(".allCheck").prop("checked",true);
        }else{
            $(".allCheck").prop("checked",false)
        }
    });
    //如果子选择框没有选中则父选框取消全选
    $(document).on("click",".check",function () {

        $(".check").each(function(){
            if($(this).prop("checked")==true){
                $(".allCheck").prop("checked",false);
            }
        })
    })
    //子选择框全部选中复选框也选中
    $(document).on("click",".check",function () {
        if($(".check[class='check']:checked").length==$(".check[class='check']").length){
            $(".allCheck").prop("checked",true);
        }else{
            $(".allCheck").prop("checked",false)
        }
    })
    //点击删除
    //点击删除出现弹框

    $(".One_ShopCon ul li>div:nth-child(2) ol>li:nth-child(6) p:first-child").click(function(){
        var that=$(this);
        if($(this).parent().siblings().children(" input").is(":checked")==false){
            $(".One_moveGzIfNull").show();
        }else{
            $(".One_isDel").show();  //删除弹框显示
        }

        var productId=$(this).parent().siblings().children(" input").attr("value");

        //确定删除
        $(".One_isDel>div:last-child button:first-child").click(function(){

            $(".One_isDel").hide();
            $(that).parent().parent().parent().remove(); //删除选中商品
            if($(".check").length==0){
                $(".allCheck").prop("checked",false);
                $(".fnt").html("￥ 0");
            }
            sumSumPrice();
            $.ajax({
                type: 'GET',
                url: '/delbyProductId?productId='+productId,
                dataType: 'json',
                success: function (html) {
                    $("#cartListInner").html(html);
                    window.location.href("cartList");
                }
            }).first();

        })


    })
    //删除已选的商品
    $(".One_ShopFootBuy>div:first-child ul li:nth-child(2)").click(function(){
        // $(".check").each(function(){
        if($(".check").is(":checked")==false){
            $(".One_moveGzIfNull").show();
        }else{
            $(".One_isDel").show();  //删除弹框显示
        }

        //确定删除
              $(".One_isDel>div:last-child button:first-child").click(function(){
                  $(".One_isDel").hide(); //删除弹框隐藏
                  var ids = '';
                  $('.check:checked').each(function(){
                          ids += this.value + ',';
                  });
                  ids=ids.substring(0,ids.length-1);
                  $.ajax({
                      type: 'POST',
                      url: '/delSelected',
                      data: {"ids": ids},
                      dataType: 'json',
                      success: function (html) {
                          // $(".check:checked").parent().parent().parent().parent().remove();  //删除选中商品
                          $("#cartListInner").html(html);
                      }
                  }).first();

              })
    })


    //点击×号关闭
    $(".One_moveGzIfNull>p span:last-child img").click(function(){
        $(".One_moveGzIfNull").hide();
    })
    $(".One_moveMyGz>p span:last-child img").click(function(){

        $(".One_moveMyGz").hide();
        $(".One_moveGzIfNull").hide();
    })
    $(".One_clearShop>p span:last-child img").click(function(){
        $(".One_clearShop").hide();
    })

    $(".One_isDel>p img").click(function(){
        $(".One_isDel").hide();
    })
    $(".One_DengLu>p:first-child span:last-child img").click(function(){
        $(".One_DengLu").hide();
    })
    //清除下柜商品
    $(".One_ShopFootBuy>div:first-child ul li:nth-child(4)").click(function(){
        $(".One_clearShop").show()
    })
    //购物车+ -
    //鼠标移入变小手
    $(".One_ShopCon ul li>div:nth-child(2) ol>li:nth-child(4) p:first-child span").mouseover(function(){
        $(this).css("cursor","pointer")
    })
    //+
    $(".One_ShopCon ul li>div:nth-child(2) ol>li:nth-child(4) p:first-child span:last-child").click(function(){
        var add=$(this).prev("span").html();
        add++;
        $(this).prev("span").html(add).change();
        //总价
        var dj=$(this).parent().parent().prev().children(".dj").html().substring(1);
        var sl=$(this).prev("span").html();
        $(this).parent().parent().parent().children("li:nth-child(5)").children(".zj").html("￥"+dj*sl+".00")
        sumSumPrice();

    })
    //-
    $(".One_ShopCon ul li>div:nth-child(2) ol>li:nth-child(4) p:first-child span:first-child").click(function(){
        var jian=$(this).next("span").html();
        jian--;
        if(jian<=0){
            jian=1;
        }
        $(this).next("span").html(jian).change();
        //总价
        var dj=$(this).parent().parent().prev().children(".dj").html().substring(1);
        var sl=$(this).next("span").html();
        $(this).parent().parent().parent().children("li:nth-child(5)").children(".zj").html("￥"+dj*sl+".00")
        sumSumPrice();

    })
    //选中当前商品背景变色
    $(".check").each(function(index){
        $(".check").eq(index).click(function(){
            if($(this).is(":checked")){
                $(this).parent().parent().parent().css("background","#fff4e8");
                $(this).parent().parent().parent().parent().children("div:last-child").css("background","#fff4e8")
            }else{
                $(this).parent().parent().parent().parent().children("div:last-child").css("background","none")
                $(this).parent().parent().parent().css("background","none")
            }
        })
    })
    //商品删除鼠标移入变色
    $(".One_ShopCon ul li>div:nth-child(2) ol>li:nth-child(6) p").mouseover(function(){
        $(this).css({"cursor":"pointer","color":"#e64346"});
    }).mouseout(function(){
        $(this).css({"cursor":"pointer","color":"gray"});
    })
    $(".One_ShopCon ul li>div:nth-child(2) ol>li:nth-child(6) p:nth-child(2)").click(function(){
        $(".One_mb").show();
        $(".One_moveMyGz").show();
    })
    $(".One_ShopCon ul li>div:nth-child(2) ol>li:nth-child(6) p:last-child").click(function(){
        $(".One_mb").show();
        $(".One_DengLu").show();
    })


    $(".One_isDel>div:last-child button").mouseover(function(){
        $(this).css("cursor","pointer");
    })

    $(".One_ShopFootBuy>div:first-child ul li:not(:first-child)").mouseover(function(){
        $(this).css({"cursor":"pointer","color":"#e64346"})
    }).mouseout(function(){
        $(this).css("color","black")
    })

    //封装总价钱函数
    function sumSumPrice(){
        console.log("计算总价");
        var zzj=0;
        $(".check").each(function () {


            if($(this).prop("checked")){
                console.log("check!!"+ $(this).parents("ol").find(".zj").html());
                var zj = $(this).parents("ol").find(".zj").html().substring(1);
                console.log(" 价格："+zj);
                zzj=zzj+parseFloat(zj);
            }
            $(".fnt").html("￥"+zzj+".00")
        })

    }
    $(".One_ShopCon ul li>div:nth-child(2)>ol>li:nth-child(2)>dd").mouseover(function(){
        $(this).css({"cursor":"pointer","color":"#e64346"})
    }).mouseout(function(){
        $(this).css("color","black")
    })
    //active
    $(".one_main_div1 .one_main_ul>li").mouseover(function(){
        $(".one_main_div1 .one_main_ul>li").removeClass("active");
        $(this).addClass("active");
    })
    //选项卡
    $(".one_main_div1 .one_main_ul li").mouseover(function() {
        $(".one_main_div1_1").eq($(this).index()).stop(true).show().siblings().stop(true).hide()
    })


    $(".fade li>div a").mouseover(function(){
        $(this).children("p").children("img").attr("src","img/one_mian_gwc2.png")
    }).mouseout(function(){
        $(this).children("p").children("img").attr("src","img/one_mian_gwc1.png")
    })
