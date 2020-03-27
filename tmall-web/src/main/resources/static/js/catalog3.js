$(function () {
    //删除三级分类
    $(document).off("click", ".delCatalog3Btn");
    $(document).on("click",".delCatalog3Btn",function(){
        var catalog3Id = $(this).parents("tr").attr("id");
        var check=confirm("确定删除吗?");
        if(check==true){
            $.ajax({
                url: "/catalog3/delete/" + catalog3Id,
                method:"get",
                dataType:"json",
                success: function (result) {
                    if(result.status=="SUCCESS"){
                        $("tr[id="+catalog3Id+"]").remove();
                        alert("删除成功");
                        //$('. table-striped').reload();
                        //window.location.reload();
                    } else {
                        alert("删除失败");
                    }
                },
            }).first();
        }
    });
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
        var catalog2Id=$(".catalog2").val();
        //alert(catalog2Id);
        var name=$("#name").val();
       // alert(name);
        if(catalog2Id==0||catalog2Id==null){
            alert("请先选择分类");
        }else if(name==null||name==""){
            alert("分类名称不能为空！");
        }else{
            var check=confirm("确定新增吗?");
            if(check==true){
                $.ajax({
                url: "/catalog3/add/",
                method:"post",
                dataType:"json",
                data:{
                    "catalog2Id":catalog2Id,
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
            }).first();

            }

        }
    });
  /*  //点击修改按钮
    $(document).on("click",".modifyCataloglBtn",function(){
        var id=$(this).parents("tr").attr("id");
        var str = $(this).text();
        var str1=$(this).next().text();
        if(str=="修改"&&str1=="删除"){
            str="保存";
            $(this).text(str);
            $(this).siblings().remove();
            $(this).parent().append("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs cancelModifyBtn' >取消</button>");
            $(this).parent().siblings("td").each(function () {  // 获取当前行的其他单元格
                obj_text = $(this).find("input:text");    // 判断单元格下是否有文本框
                // 如果没有文本框，则添加文本框使之可以编辑
                if (!obj_text.length) {
                    $(this).html("<input type='text' style='padding-left: 8px' th:id='"+id +"'value='" + $(this).text() + "'>");
                } else   // 如果已经存在文本框，则将其显示为文本框修改的值
                {

                    $(this).html(obj_text.val());
                }

            });

        } else{
            str="修改";
            $(this).text(str);
            var catalog2Name=$(this).parent().siblings("td").find("input:text").val();
            var catalog3Name=$(this).parent().prev().find("input:text").val();

            $.ajax({
                url: "/catalog3/modify/" ,
                method:"post",
                dataType:"json",

                data:{
                    "id":id,
                    "catalog2Name":catalog2Name,
                    "name":catalog3Name,
                },
                success: function (result) {
                    if(result.status=="SUCCESS"){
                        alert("修改成功");

                        //刷新当前页面
                        window.location.reload();
                    }else{
                        alert(result.message);
                    }
                },
            });
            $(this).siblings().remove();
            $(this).parent().append("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs delCatalog3Btn' >删除</button>");

        }


    });
    //修改取消
    $(document).on("click",".cancelModifyBtn",function(){
        var id=$(this).parents("tr").attr("id");
        $(this).parent().siblings("td").each(function () {
            obj_text = $(this).find("input:text");
            $(this).html(obj_text.val());
        });
        $(this).prev("button").text("修改");
        $(this).parent().append("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs delCatalog3Btn' >删除</button>");
        $(this).remove();
    })
*/
  //点击修改按钮
    $(document).on("click",".modifyCataloglBtn",function(){
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
        $(this).parent().append("<button  type='button' class='btn bg-olive btn-xs saveModifyBtn' >保存</button>");
        $(this).parent().append("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs cancelModifyBtn' >取消</button>");
        $(this).remove();
    });
    //点击存按钮
    $(document).off("click", ".saveModifyBtn");//移除监听
    $(document).on("click",".saveModifyBtn",function(){

        var catalog2Name=$(this).parent().siblings("td").find("input:text").val();
        var catalog3Name=$(this).parent().prev().find("input:text").val();
        var id=$(this).parents("tr").attr("id");
        var check=confirm("确定修改吗?");
        if(check==true){
            $.ajax({
                url: "/catalog3/modify/" ,
                method:"post",
                dataType:"json",

                data:{
                    "id":id,
                    "catalog2Name":catalog2Name,
                    "name":catalog3Name,
                },
                success: function (result) {
                    if(result.status=="SUCCESS"){
                        alert("修改成功");
                        //刷新当前页面
                        //window.location.reload();

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
        $(this).parent().append("<button  type='button' class='btn bg-olive btn-xs modifyCataloglBtn' >修改</button>");
        $(this).parent().append("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs delCatalog3Btn' >删除</button>");
        $(this).remove();
    });
    //点击取消按钮
    $(document).on("click",".cancelModifyBtn",function(){
        $(this).parent().siblings("td").each(function () {  // 获取当前行的其他单元格
            obj_text = $(this).find("input:text");    // 判断单元格下是否有文本框
            $(this).html(obj_text.val());

        });
        $(this).siblings("button").remove();
        $(this).parent().append("<button  type='button' class='btn bg-olive btn-xs modifyCataloglBtn' >修改</button>");
        $(this).parent().append("<button style='margin-left: 30px' type='button' class='btn bg-olive btn-xs delCatalog3Btn' >删除</button>");
        $(this).remove();
    });
})