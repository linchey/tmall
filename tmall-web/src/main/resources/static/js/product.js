$(function() {
    $("#sub-nav-product").attr("class","active");
    var productId = $(this).attr("productid");
    $(".addCart").click(function () {
        $.ajax({
            url:"/cart/add/"+productId+"/1",
            success:function(result){
                if(result.status=="SUCCESS"){
                    toastr.info("添加购物车成功.");
                }else{
                    toastr.warn(result.message);
                }
            },
            error:function(){
                toastr.warn("发生错误,稍后重试.");
            }
        })
    });
    //点击新增按钮
    $(".addProductBtn").click(function () {
        $(this).attr("disabled",'disabled');
        $(".addFormView").removeAttr("hidden");
    });
    //点击取消按钮
    $("#cancelProductBtn").click(function () {
        $(".addFormView").attr("hidden",'hidden');
        $(".addProductBtn").removeAttr("disabled");
    });
    //一级分类点击事件
    $(".catalog1").click(function () {
        $.ajax( {
            url:"/catalog/getCatalog1",
            method:"get",
            async:false,
            cache:false,
            dataType:"json",
            success:function (result) {

                //alert(result[0].name);
                if(result.length<1){
                    $(".catalog1 option").remove();
                    $(".catalog1" ).append("<option value='0' selected disabled>NULL</option>");
                    return;
                }
                var optionString="";
                for(var i=0;i<result.length;i++){
                   // alert(result[i].id);
                    optionString+="<option value='"+result[i].id+"'>"+result[i].name+"</option>";

                }


                //$(".catalog1 option:last").remove();
                $(".catalog1").append(optionString);
            }
        }).first();
    });
    //一级分类change事件
    $(document).on("change",".catalog1",function () {
        var catalog1Id=$(this).val();
        if(catalog1Id==0){
           return;
        }
        $.ajax({
            url:"/catalog/getCatalog2?"+catalog1Id,
            method:"get",
            async:false,
            cache:false,
            data:{
                "catalog1Id":catalog1Id
            },
            dataType:"json",
            success:function (result) {
               // alert(result.length);
                if(result.length==0){
                    $(".catalog2 option").remove();
                    $(".catalog2" ).append("<option value='0' selected disabled>NULL</option>");
                    return;
                }
                var optionString="";
                for(var i=0;i<result.length;i++){
                    //alert(result[i].id);
                    optionString+="<option th:value='"+result[i].id+"'>"+result[i].name+"</option>";

                }
                $(".catalog2 option").remove();
                $(".catalog2" ).append("<option value='0' selected disabled>请选择</option>");
                $(".catalog2").append(optionString);
            }
        }).first();
    });
    //二级分类change事件
    $(document).on("change",".catalog2",function () {

    });
    //三级分类change事件
    $(document).on("change",".catalog3",function () {

    })
})