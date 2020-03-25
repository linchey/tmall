$(function () {

    //删除用户地址
    $(".delAddresslBtn").click(function () {
        var addressId = $(this).attr("id");
        $.ajax({
            url: "/user/userAddress/delete/" + addressId,
            method:"get",

            dataType:"json",
            success: function (result) {
                if(result.status=="SUCCESS"){
                    $("tr[addressId="+addressId+"]").remove();
                    alert("删除成功");
                    window.location.reload();
                } else {
                    alert("删除失败");
                }
            },
        })
    });

    //保存用户地址
    $("#addAddressBtn").click(function () {
        $.ajax({
            url: "/user/userAddress/add/",
            method:"post",
            dataType:"json",
            data:{
                "id":$("#id").val(),
                "name":$("#name").val(),
                "postCode":$("#postCode").val(),
                "phone":$("#phone").val(),
                "province":$("#province").val(),
                "city":$("#city").val(),
                "region":$("#region").val(),
                "detailAddress":$("#detailAddress").val(),
            },
            success: function (result) {
                if(result.status=="SUCCESS"){
                    alert("添加成功");
                    //关闭模态框
                    $("#addAddress").modal("hide");
                    //刷新当前页面
                    window.location.reload();
                } else {
                    alert(result.message);
                }
            },
        })
    });
    //点击修改按钮
    $(".modifyAddresslBtn").click(function () {
        var addressId = $(this).attr("id");
        $.ajax({
            url: "/user/userAddress/modify/" + addressId,
            method:"get",
            dataType:"json",
            success: function (result) {
                $("#editAddress").modal("show");
                var str=eval(result)
                $("#name1").val(str.name);
                $("#postCode1").val(str.postCode);
                $("#phone1").val(str.phone);
                $("#province1").val(str.province);
                $("#city1").val(str.city);
                $("#region1").val(str.region);
                $("#detailAddress1").val(str.detailAddress);
                $("#editAddresslBtn").val(str.id);
            },

        })
    });
    //修改用户地址
    $("#editAddresslBtn").click(function () {
        $("#editAddress").modal("hide");
        var addressId = $(this).val();
        $.ajax({
            url: "/user/userAddress/modify/" ,
            method:"post",
            dataType:"json",
            data:{
                "id":addressId,
                "name":$("#name1").val(),
                "postCode":$("#postCode1").val(),
                "phone":$("#phone1").val(),
                "province":$("#province1").val(),
                "city":$("#city1").val(),
                "region":$("#region1").val(),
                "detailAddress":$("#detailAddress1").val(),
            },
            success: function (result) {
                if(result.status=="SUCCESS"){
                    alert("修改成功");
                    //关闭模态框

                    //刷新当前页面
                    window.location.reload();
                }else{
                    alert(result.message);
                }
            },

        })
    });
})