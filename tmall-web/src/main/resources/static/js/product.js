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

        $(".productList").attr("hidden","hidden");
    });
    //点击取消按钮
    $(document).on("click","#cancelProductBtn",function () {
        $(".addFormView").attr("hidden",'hidden');
        $(".addProductPage").attr("hidden",'hidden');
        $(".addProductBtn").removeAttr("disabled");
        $(".productList").removeAttr("hidden");
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
        $(".row").append("<button type=\"button\" id=\"cancelProductBtn\" class=\"btn btn-primary \"style=\"margin-left: 15px\">取消</button>\n")
        $(".addFormView").attr("hidden",'hidden');
        $(".file").parents(".addPic").siblings().remove();
        $(".productList").removeAttr("hidden");
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
    $(document).on("change",".file", function () {
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
               $('.file').parents(".addPic").html("<img style='width: 100%;height: 100%' src="+data+">");
               $(".addPic").parent(".row").append("<div style='margin-left: 20px' class=\"addPic\"><label ><i class=\"fa fa-plus\"></i><input style=\"position:absolute;opacity:0;\"type=\"file\" class=\"file\" name=\"file\"></label>")
           }
       });

    });
//提交按钮
    $("#saveProductBtn").click(function () {
        var catalog1Id=$(".catalog1").val();
        //alert(catalog1Id);
        var catalog2Id=$(".catalog2").val();
        //alert(catalog2Id);
        var catalog3Id=$(".catalog3").val();
        //alert(catalog3Id)
        var productName=$("#productName").val();
        var price=$("#price").val();
        var stock=$("#stock").val();
        if(stock==null||stock==0){
            stock=0;
        }
        var note=$("#note").val();
        var images=$(".addPic").find(" img");
        var imageUrls= new Array();
        if(images.length==0){
            alert("请至少选择一张图片")
        }else{
            for(var i=0;i<images.length;i++){
                imageUrls[i]=$(images[i]).attr("src");
                //alert($(images[i]).attr("src"));
            }
            if(catalog1Id!=0||catalog1Id!=null){
                if(catalog2Id==0||catalog2Id==null){
                    catalog2Id=null;
                    catalog3Id=null;
                }else if(catalog3Id==0||catalog3Id==null){
                    catalog3Id=null;
                }
                /*  alert(catalog1Id);
                  alert(catalog2Id);
                  alert(catalog3Id);*/
                var data={"catalog1Id":catalog1Id,
                    "catalog2Id":catalog2Id,
                    "catalog3Id":catalog3Id,
                    "productName":productName,
                    "price":price,
                    "stock":stock,
                    "note":note,
                    "defaultImg":"",
                    "productImages":imageUrls
                };
                $.ajax({
                    url:"/product/addProduct",
                    type:'POST',

                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(data),
                    success:function (result) {

                        if(result.status=="SUCCESS"){
                            alert("添加成功！");
                            window.location.reload();
                        }else{
                            alert("已经存在该产品！不要重复添加商品");
                        }
                    }
                });
            }else{
                alert("请选择分类信息");
            }
        }


    });
/*修改商品信息*/
    var oldValue="";
//**** 鼠标双击事件
    $(document).off("dblclick");//解绑
    $(document).on("dblclick", ".edit", function () {
            //判断是否已经点击，如果已经是被点击过的，就return ,不让程序再次生成input
            if ($(this).children("input").attr("type") == "text") return;
             oldValue=$(this).html();
            alert(oldValue);
            $(this).html('<input type="text" class="modify" style=" height:30px;width:140px" />');
            //将焦点放在最后。（先赋值为空，然后再获取焦点，然后再反填oldValue）
            //这样就可以让焦点出现在最后，而不是出现在oldValue的最前面
            $(this).children("input").val(" ").focus().val(oldValue);
        }
    );
    //修改

    $(document).off("blur");//解绑
    //失去焦点时进行修改请求
    $(document).on("blur", ".modify", function () {
            //alert(oldValue);
            var ID = $(this).parents('tr').attr('id');

            //输入的值
            var newValue = $(this).val();

            //要修改的字段名称
            var field = $(this).parent().attr('field');

            $.ajax({
                url: "/product/modify",
                type: "post",
                dataType:"json",
                data: {
                    id: ID,
                    field:field,
                    newValue: newValue
                },
                success: function (result) {

                    if (result.status=="SUCCESS") {
                        $(".modify").parent().html(newValue);
                        //成功后将新输入的值 赋值给span
                        alert("修改成功");
                        //window.location.reload();
                    } else {
                        alert('修改失败');
                        //失败后将旧值 赋值给span
                        $(".modify").parent().html(oldValue);
                    }

                }
            });

        }
    );
    //删除商品信息
    $(".delProductBtn").click(function () {
        var productId=$(this).parents("tr").attr("id");
        $.ajax({
            url:"/product/delete/"+productId,
            dataType:"json",
            type:"Post",
            data:{
                "id":productId
            },
            success:function (result) {
                if(result.status=="SUCCESS"){
                    alert("删除成功！");
                    window.location.reload();
                }else{
                    alert("删除失败");
                }
            }
        });
    });
    //查询按钮
    $("#queryProductBtn").click(function () {
        var catalog1Id=$(".catalog1").val();
        //alert(catalog1Id);
        var catalog2Id=$(".catalog2").val();
        //alert(catalog2Id);
        var catalog3Id=$(".catalog3").val();
        $.ajax({
            url:"/product/query",
            data:{
                catalog1Id:catalog1Id,
                catalog2Id:catalog2Id,
                catalog3Id:catalog3Id
            },
            dataType:"json",
            type:"POST",
            success:function (result) {

            }
        });
    });
})