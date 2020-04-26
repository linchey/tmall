    //页面加载之后完成
$(function(){
   
    var thisTime;
    var catlog1Id=$("")
    //鼠标离开左侧内容栏
    $('.item1 ').mouseleave(function(even){
        thisTime = setTimeout(thisMouseOut,100);
    });
    //鼠标点击左侧内容栏   滑动出弹层
    $('.item1 ').mouseenter(function(){
        $(this).addClass("active").siblings().removeClass("active");
        clearTimeout(thisTime);
        var thisUB = $('.item1 ').index($(this));

        if($.trim($('.cat_subcont .cat_sublist').eq(thisUB).html()) != ""){
            $('.cat_subcont').addClass('active');
            $('.cat_sublist').hide();
            $('.cat_sublist').eq(thisUB).show();
        }else{
            $('.cat_subcont').removeClass('active');
        }
    });
    //函数——执行鼠标离开左侧内容栏的动作
    function thisMouseOut(){
        $('.cat_subcont').removeClass('active');
        $('.item1 ').removeClass('active');
    }
    $('.cat_subcont').mouseenter(function(){
        clearTimeout(thisTime);
        $('.cat_subcont').addClass('active');
    });
    $('.cat_subcont').mouseleave(function(){
        $('.cat_subcont').removeClass('active');
        $('.item1').removeClass('active');
    });
    //商品二级分类
    $(".fore_list>h3 a").click(function () {
        var catalog2Id=$(this).attr("value");
       // alert(id);
        $(this).attr("href","/search/catalog2?catalog2Id="+catalog2Id);

    });
    //商品三级分类
    $(".fore_list >ul li a ").click(function () {
        var catalog3Id=$(this).attr("value");
        //alert(id);
        $(this).attr("href","/search/catalog3?catalog3Id="+catalog3Id);
    });
 $("#submit").click(function () {
     var search=$("#search").val();
     $(this).attr("href","/search/productName?search="+search);
 })
})
