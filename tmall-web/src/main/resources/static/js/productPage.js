$(function(){
        //1.楼梯什么时候显示，800px scroll--->scrollTop
        $(window).on('scroll',function(){
            var $scroll=$(this).scrollTop();
            if($scroll>=940){
                $('.posi').show();
            }else{
                $('.posi').hide();
            }
        });
        $(".crumb .crumb-item-one").mouseover(function() {
			$(this).children(".crumb-item-two").css({
				"display": "block"
			})
			$(this).css({
				"border-bottom": "none",
				"height": "30px"
			})
		}).mouseout(function() {
			$(this).children(".crumb-item-two").css({
				"display": "none"
			})
			$(this).css({
				"border": "solid 1px #737373",
				"height": "22px"
			})
		})
		//top右下拉
		$(".contact").mouseover(function() {
			$(this).css({
				"height": "30px"
			})
		}).mouseout(function() {
			$(this).css({
				"height": "22px"
			})
		});

		$(".box-attr dd").click(function() {
			$(this).css({
				"border": "solid 1px red"
			}).siblings("dd").css({
				"border": "solid 1px #ccc"
			})
		})

       //红边框
		$(".box-attr-2 dd").click(function() {
			$(this).addClass("redborder").siblings("dd").removeClass("redborder");
            switchSkuId();
		})
		//加减
		$("#jia").click(function() {
			var n = $(this).parent().parent().prev("input").val();
			var num = parseInt(n) + 1;
			if(num == 0) {
				return;
			}
			$(this).parent().parent().prev("input").val(num);
		})

		$("#jian").click(function() {
			var n = $(this).parent().parent().prev("input").val();
			var num = parseInt(n) - 1
			if(num == 0) {
				return;
			}
			$(this).parent().parent().prev("input").val(num)
		})

		//左右滚动
		$("#left").click(function() {
			$(".box-lh-one ul").stop().animate({
				"left": "-297px"
			})
			$(this).css({
				"color": "#000"
			})
			$("#right").css({
				"color": "#ccc"
			})
		})
		$("#right").click(function() {
			$(".box-lh-one ul").stop().animate({
				"left": 0
			})
			$(this).css({
				"color": "#000"
			})
			$("#left").css({
				"color": "#ccc"
			})
		})

		//换图片
		$(".box-lh-one li").mouseover(function() {
			$(this).css({
				"padding": "0",
				"border": "solid 1px red"

			}).siblings().css({
				"padding": "1px",
				"border": "none"
			})

		})



		function Zoom(imgbox, hoverbox, l, t, x, y, h_w, h_h, showbox) {
			var moveX = x - l - (h_w / 2);
			//鼠标区域距离
			var moveY = y - t - (h_h / 2);
			//鼠标区域距离
			if(moveX < 0) {
				moveX = 0
			}
			if(moveY < 0) {
				moveY = 0
			}
			if(moveX > imgbox.width() - h_w) {
				moveX = imgbox.width() - h_w
			}
			if(moveY > imgbox.height() - h_h) {
				moveY = imgbox.height() - h_h
			}
			//判断鼠标使其不跑出图片框
			var zoomX = showbox.width() / imgbox.width()
			//求图片比例
			var zoomY = showbox.height() / imgbox.height()

			showbox.css({
				left: -(moveX * zoomX),
				top: -(moveY * zoomY)
			})
			hoverbox.css({
				left: moveX,
				top: moveY
			})
			//确定位置

		}

		function Zoomhover(imgbox, hoverbox, showbox) {
			var l = imgbox.offset().left;
			var t = imgbox.offset().top;
			var w = hoverbox.width();
			var h = hoverbox.height();
			var time;
			$(".probox img,.hoverbox").mouseover(function(e) {
				var x = e.pageX;
				var y = e.pageY;
				$(".hoverbox,.showbox").show();
				hoverbox.css("opacity", "0.3")
				time = setTimeout(function() {
					Zoom(imgbox, hoverbox, l, t, x, y, w, h, showbox)
				}, 1)
			}).mousemove(function(e) {
				var x = e.pageX;
				var y = e.pageY;
				time = setTimeout(function() {
					Zoom(imgbox, hoverbox, l, t, x, y, w, h, showbox)
				}, 1)
			}).mouseout(function() {
				showbox.parent().hide()
				hoverbox.hide();
			})
		}
		$(function() {
			Zoomhover($(".probox img"), $(".hoverbox"), $(".showbox img"));
			$(".box-lh-one ul li").hover(function() {
				$('.img1').attr("src", $(this).find('img').attr('src'));
			})
		})
	//添加购物车按钮
	$("#cartBtn").click(function () {
		var canClick=$(this).attr("canClick");
		if(canClick=='0'){
			return ;
		}
		$("#itemForm").submit();
	})
    })


