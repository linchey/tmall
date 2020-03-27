$(function () {
    //删除一级分类
    $(document).off("click", ".delCatalog1Btn");
    $(document).on("click",".delCatalog1Btn",function(){
        var catalog1Id = $(this).parents("tr").attr("id");
        var check=confirm("确定删除吗?");
        if(check==true){
            $.ajax({
                url: "/catalog1/delete/" + catalog1Id,
                method:"get",
                dataType:"json",
                cache:false,
                success: function (result) {
                    if(result.status=="SUCCESS"){
                        $("tr[id="+catalog1Id+"]").remove();
                        alert("删除成功");
                       // window.location.reload();
                    } else {
                        alert("删除失败");
                    }
                },
            });

        }
    }).first();
    //点击新增按钮
    $(".addCatalogBtn").click(function () {
        $(this).attr("disabled",'disabled');
        $(".addFormView").removeAttr("hidden");
    });
    //点击取消按钮
    $("#cancelCatalogBtn").click(function () {
        $(".addFormView").attr("hidden",'hidden');
        $(".addCatalogBtn").removeAttr("disabled");
    });
    //新增分类保存
    $("#saveCatalogBtn").click(function () {
        var name=$("#name").val();
        if(name==null||name==""){
            alert("分类名称不能为空！");
        }else{
            var check=confirm("确定新增吗?");
            if(check==true){
                $.ajax({
                    url: "/catalog1/add/",
                    method:"post",
                    dataType:"json",
                    cache:false,
                    data:{
                        "name":name,
                    },
                    success: function (result) {
                        if(result.status=="SUCCESS"){
                            alert("添加成功");
                            window.location.reload();
                            //$('. table-striped').reload();
                        } else {
                            alert(result.message);
                        }
                    },
                });
            }
        }
    }).first();
    //点击修改按钮
    $(document).on("click",".modifyCatalog1Btn",function(){
        $(this).parent().siblings("td").each(function () {  // 获取当前行的其他单元格
            obj_text = $(this).find("input:text");    // 判断单元格下是否有文本框
            // 如果没有文本框，则添加文本框使之可以编辑
            if (!obj_text.length) {
                $(this).html("<input type='text' style='padding-left: 8px' value='" + $(this).text() + "'>");
            } else   // 如果已经存在文本框，则将其显示为文本框修改的值
            {
                $(this).html(obj_text.val());
            }
        });
        $(this).siblings("button").remove();
        $(this).parent().append("<button  type='button' class='btn bg-olive btn-xs saveModify1Btn' >保存</button>");
        $(this).parent().append("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs cancelModify1Btn' >取消</button>");
        $(this).remove();
    }).first();
    //点击修改保存按钮
    $(document).off("click", ".saveModify1Btn");
    $(document).on("click",".saveModify1Btn",function(){
        var catalog1Name=$(this).parent().siblings("td").find("input:text").val();
        var id=$(this).parents("tr").attr("id");
        var check=confirm("确定修改吗?");
        if(check==true){
            $.ajax({
                url: "/catalog1/modify/" ,
                method:"post",
                dataType:"json",
                cache:false,
                data:{
                    "id":id,
                    "name":catalog1Name,
                },
                success: function (result) {
                    if(result.status=="SUCCESS"){
                        alert("修改成功");
                        //刷新当前页面
                            window.location.reload();


                    }else{
                        alert(result.message);
                    }
                }
            }).first();
        }
        $(this).parent().siblings("td").each(function () {  // 获取当前行的其他单元格
            obj_text = $(this).find("input:text");    // 判断单元格下是否有文本框
            $(this).children().remove();
            $(this).html(obj_text.val());
        });
        $(this).siblings("button").remove();
        $(this).parent().append("<button  type='button' class='btn bg-olive btn-xs modifyCatalog1Btn' >修改</button>");
        $(this).parent().append("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs delCatalog1Btn' >删除</button>");
        $(this).remove();
    }).first();
    //点击取消修改按钮
    $(document).on("click",".cancelModify1Btn",function(){
        $(this).parent().siblings("td").each(function () {  // 获取当前行的其他单元格
            obj_text = $(this).find("input:text");    // 判断单元格下是否有文本框
            $(this).html(obj_text.val());
        });
        $(this).siblings("button").remove();
        $(this).parent().append("<button  type='button' class='btn bg-olive btn-xs modifyCatalog1Btn' >修改</button>");
        $(this).parent().append("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs delCatalog1Btn' >删除</button>");
        $(this).remove();
    }).first();


    })