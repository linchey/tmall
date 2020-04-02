$(function () {
    //添加购物车按钮
    $("#cartBtn").click(function () {
        var canClick=$(this).attr("canClick");
        if(canClick=='0'){
            return ;
        }
        $("#itemForm").submit();
    })
})