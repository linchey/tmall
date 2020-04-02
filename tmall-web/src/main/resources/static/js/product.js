$(function() {

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

    $(document).on("click",".catalog1",function () {

        $(this).find(" option:selected").css("font-weight","bold")
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
               // $(".catalog1 option[value='0']" ).removeAttr(selected);

                $(".catalog1").append(optionString);

            }
        })
    }).first()
    //一级分类change事件
    $(document).on("change",".catalog1",function () {
        var catalog1Id=$(this).val();
        $(this).children(" option[value='"+catalog1Id+"']").attr("selected");
        $(this).find(" option:selected").siblings().css("font-weight","normal");
        catalog1Id=$(".catalog1").val()

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
    }).first();
    //二级分类change事件
    $(document).on("change",".catalog2",function () {
        var catalog2Id=$(this).val();
        $(this).find(" option:selected").css("font-weight","bold")
        $(this).find(" option:selected").siblings().css("font-weight","normal");
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
        $(this).find(" option:selected").css("font-weight","bold")
        $(this).find(" option:selected").siblings().css("font-weight","normal");
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
       }).first();

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
    //$(document).off("dblclick");//解绑
    $(document).on("dblclick", ".edit", function () {
            //判断是否已经点击，如果已经是被点击过的，就return ,不让程序再次生成input
            if ($(this).children("input").attr("type") == "text") return;
             oldValue=$(this).html();
           // alert(oldValue);
            $(this).html('<input type="text" class="modify" style=" height:30px;width:140px" />');
            //将焦点放在最后。（先赋值为空，然后再获取焦点，然后再反填oldValue）
            //这样就可以让焦点出现在最后，而不是出现在oldValue的最前面
            $(this).children("input").val(" ").focus().val(oldValue);
        }
    ).first();
    //修改

    //$(document).off("blur");//解绑
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
                        //alert("修改成功");
                        //window.location.reload();
                    } else {
                        alert('修改失败');
                        //失败后将旧值 赋值给span
                        $(".modify").parent().html(oldValue);
                    }

                }
            }).first();

        }
    ).first();
    //删除商品信息
    $(document).on("click",".delProductBtn",function () {
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
        }).first();
    }).first();
    //查询按钮
    $("#queryProductBtn").click(function () {
        var catalog1Id=$(".catalog1").val();
        //alert(catalog1Id);
        var catalog2Id=$(".catalog2").val();
        //alert(catalog2Id);
        var catalog3Id=$(".catalog3").val();
        if(catalog1Id==null||catalog1Id==""){
            alert("请选择分类");
            return;
        }
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
                $(".product1").children().remove();
                if(result.length!=0){
                    var astring="";
                    for(var i=0;i<result.length;i++){
                        astring+="<tr id='"+result[i].id+"'>" +
                            "<td style='text-align: center;line-height: 150px;'>"+ (i+1)+"</td>" +
                            "<td style='text-align: center;line-height: 150px;'><span class='edit' onselectstart='return false' field='productName' >"+ result[i].productName+"</span></td>"+
                            "<td style='text-align: center;line-height: 150px;'><span class='edit' onselectstart='return false' field='price'>"+ result[i].price+"</span></td>"+
                            "<td style='text-align: center;line-height: 150px;'><span class='edit' onselectstart='return false' field='stock'>"+ result[i].stock+"</span></td>"+
                            "<td style='text-align: center;line-height: 150px;'><span class='edit' onselectstart='return false' field='note'>"+ result[i].note+"</span></td>"+
                            "<td style='text-align: center;line-height: 150px;'><img style='width: 150px;height: 150px' src='"+result[i].defaultImg+"'></td>"+
                            "<td style='text-align: center;line-height: 150px;'>"+
                            "<button type='button' class='btn bg-olive btn-xs modifyProductImgBtn'>图片设置</button>"+
                            "<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs delProductBtn'>删除</button>"+
                            "</td>"+"</tr>";
                    }
                    $(".qurProductList").removeAttr("hidden");
                    $(".product1").append(astring);
                }

            }
        }).first();
    }).first();
    //图片设置
    $(document).on("click",".modifyProductImgBtn",function () {
        var productId=$(this).parents("tr").attr("id");
        $(this).attr("disabled","disabled");
        $(this).siblings().remove();
        $(this).after("<button style='margin-left: 20px 'type='button' class='btn bg-olive btn-xs canelProductImgBtn'>取消</button>");
        $.ajax({
            url:"/product/modifyImg",
            dataType:"json",
            type:"POST",
            data:{
                "productId":productId
            },
            success:function (result) {
                if(result.length!=0){
                    var IString="";
                    for(var i=0;i<result.length;i++){
                        IString+="<tr class='aa' id='"+result[i].id+"' style='background-color:white; '><td colspan='4'style='text-align: center;line-height: 150px;'><img style='width: 150px;height: 150px' src='"+result[i].imgUrl+"'></td><td colspan='3'style='text-align: center;line-height: 150px;'>" +
                            "<button style='margin-left: 20px 'class='btn bg-olive btn-xs setDefaultImgBtn'>设为默认图片</button>" +
                            "<button style='margin-left: 20px 'class='btn bg-olive btn-xs delProductImgBtn'>删除</button>" +
                            "</td></tr>";
                    }
                    $(".modifyProductImgBtn").parents("tr[id='"+productId+"']").after(IString);
                }

            }
        }).first();
    });
    $(document).on("click",".canelProductImgBtn",function () {
        $(".aa").slideUp();
        $(this).siblings().removeAttr("disabled");
        $(this).after("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs delProductImgBtn'>删除</button>");
        $(this).remove();
    });
    $(document).on("click",".setDefaultImgBtn",function () {
        var id=$(this).parents("tr").attr("id");
        //alert(id);
        $.ajax({
            url:"/product/modifyDefaultImg",
            type:"POST",
            dataType:"json",
            data:{
                "id":id
            },
            success:function (result) {
                if(result.status=="SUCCESS"){
                    alert("修改成功！");
                    window.location.reload();
                }else{
                    alert("修改失败");
                }
            }
        });
    });
    $(document).on("click",".delProductImgBtn",function () {
        var id=$(this).parents("tr").attr("id");
        $.ajax({
            url:"/product/delImgs",
            dataType:"json",
            type:"Post",
            data:{
                "id":id
            },
            success:function (result) {
                if(result.status=="SUCCESS"){
                    alert("删除成功！");
                    window.location.reload();
                }else{
                    alert("删除失败");
                }
            }
        }).first();
    }).first();
    //根据商品名查找
    $("#search").click(function () {
        var name=$(this).siblings(" input").val();
        //alert(name)
        if(name==""||name==null){return;}
        $.ajax({
            url:"/product/search",
            type:"POST",
            dataType:"json",
            data:{
                "productName":name
            },
            success:function (result) {
                if(result!=null||result!=""){
                    var astring="<tr id='"+result.id+"'>" +
                            "<td style='text-align: center;line-height: 150px'>1</td>" +
                            "<td style='text-align: center;line-height: 150px;'><span class='edit' onselectstart='return false' field='productName' >"+ result.productName+"</span></td>"+
                            "<td style='text-align: center;line-height: 150px;'><span class='edit' onselectstart='return false' field='price'>"+ result.price+"</span></td>"+
                            "<td style='text-align: center;line-height: 150px;'><span class='edit' onselectstart='return false' field='stock'>"+ result.stock+"</span></td>"+
                            "<td style='text-align: center;line-height: 150px;'><span class='edit' onselectstart='return false' field='note'>"+ result.note+"</span></td>"+
                            "<td style='text-align: center;line-height: 150px;'><img style='width: 150px;height: 150px' src='"+result.defaultImg+"'></td>"+
                            "<td style='text-align: center;line-height: 150px;'>"+
                            "<button type='button' class='btn bg-olive btn-xs modifyProductImgBtn'>图片设置</button>"+
                            "<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs delProductBtn'>删除</button>"+
                            "</td>"+"</tr>";
                    }
                    $(".qurProductList").removeAttr("hidden");
                    $(".product1").children().remove();
                    $(".product1").append(astring);
                }
        }).first();
    });
})