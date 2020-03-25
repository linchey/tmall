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
    $(document).on("click","#cancelProductBtn",function () {
        $(".addFormView").attr("hidden",'hidden');
        $(".addProductPage").attr("hidden",'hidden');
        $(".addProductBtn").removeAttr("disabled");
    });
    //点击取消按钮1
    $("#cancelProduct1Btn").click(function () {
        $(".catalog1 option").remove();
        $(".catalog2 option").remove();
        $(".catalog3 option").remove();
        $(".catalog1" ).append("<option value='0' selected disabled>请选择</option>");
        $(".catalog2" ).append("<option value='0' selected disabled>请选择</option>");
        $(".catalog3" ).append("<option value='0' selected disabled>请选择</option>");

        $(".addProductPage").attr("hidden",'hidden');
        $(".addProductBtn").removeAttr("disabled");
        $(".catalogForm").append("<button type=\"button\" id=\"cancelProductBtn\" class=\"btn btn-primary \"style=\"margin-left: 15px\">取消</button>\n")
        $(".addFormView").attr("hidden",'hidden');
        $(".showPic").html("");
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
                /*$(".catalog1 option[value='0']" ).removeAttr(selected);*/
                $(".catalog1").append(optionString);
            }
        }).first();
    });
    //一级分类change事件
    $(document).on("change",".catalog1",function () {
        var catalog1Id=$(this).val();
        $(".catalog2 option").remove();
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
                    optionString+="<option value='"+result[i].id+"'>"+result[i].name+"</option>";

                }
                $(".catalog2" ).append("<option value='0' selected disabled>请选择</option>");
                $(".catalog2").append(optionString);
                $(".catalog2").val(0);
                $(".catalog3").val(0);
                $(".addProductPage").removeAttr("hidden");
                $("#cancelProductBtn").remove();
            }
        }).first();
    });
    //二级分类change事件
    $(document).on("change",".catalog2",function () {
        var catalog2Id=$(this).val();
        $(".catalog3 option").remove();
        $.ajax({
            url:"/catalog/getCatalog3?"+catalog2Id,
            method:"get",
            async:false,
            cache:false,
            data:{
                "catalog2Id":catalog2Id
            },
            dataType:"json",
            success:function (result) {
                // alert(result.length);
                if(result.length==0){
                    $(".catalog3 option").remove();
                    $(".catalog3" ).append("<option value='0' selected disabled>NULL</option>");
                    return;
                }
                var optionString="";
                for(var i=0;i<result.length;i++){
                    //alert(result[i].id);
                    optionString+="<option value='"+result[i].id+"'>"+result[i].name+"</option>";

                }

                $(".catalog3" ).append("<option value='0' selected disabled>请选择</option>");
                $(".catalog3").append(optionString);
                $(".catalog3").val(0);
                //$(".addProductPage").removeAttr("hidden");

            }
        }).first();
    });
    //三级分类change事件
    $(document).on("change",".catalog3",function () {

        //$(".addProductPage").removeAttr("hidden");

    });
    //图片上传功能
    $('.file').on('change', function () {
        var formData = new FormData();
        formData.append("file",$('.file')[0].files[0]);
        var filename = $('.file')[0].files[0].name;
       $.ajax({
           url:"/product/fileUpload",
           type:'POST',
           data:formData,
           processData: false,			//对数据不做处理
           cache:false,      				//上传文件不需要缓存
           contentType: false,
           mimeType:"multipart/form-data",
           success:function (data) {
               data=JSON.stringify(data);
               //alert(data);
               $(".showPic").append("<img src="+data+">");

           }
       }).first();

    });
    $(" img").each(function() {
        var maxWidth = 150; // 图片最大宽度
        var maxHeight = 150; // 图片最大高度
        var ratio = 0; // 缩放比例
        var width = $(this).width(); // 图片实际宽度
        var height = $(this).height(); // 图片实际高度

        // 检查图片是否超宽
        if(width > maxWidth){
            ratio = maxWidth / width; // 计算缩放比例
            $(this).css("width", maxWidth); // 设定实际显示宽度
            height = height * ratio; // 计算等比例缩放后的高度
            $(this).css("height", height); // 设定等比例缩放后的高度
        }

        // 检查图片是否超高
        if(height > maxHeight){
            ratio = maxHeight / height; // 计算缩放比例
            $(this).css("height", maxHeight); // 设定实际显示高度
            width = width * ratio; // 计算等比例缩放后的高度
            $(this).css("width", width * ratio); // 设定等比例缩放后的高度
        }
    });

})