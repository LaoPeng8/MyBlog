//在页面内 点击生成 "富强"等字体

//定义一个函数，实现随机生成十六进制颜色值
function getColor() {
	//定义字符串变量colorValue存放可以构成十六进制颜色值的值
	var colorValue = "0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f";
	//以","为分隔符，将colorValue字符串分割为字符数组["0","1",...,"f"]
	var colorArray = colorValue.split(",");
	var color = "#"; //定义一个存放十六进制颜色值的字符串变量，先将#存放进去
	//使用for循环语句生成剩余的六位十六进制值
	for (var i = 0; i < 6; i++) {
		//colorArray[Math.floor(Math.random()*16)]随机取出
		// 由16个元素组成的colorArray的某一个值，然后将其加在color中，
		//字符串相加后，得出的仍是字符串
		color += colorArray[Math.floor(Math.random() * 16)];
	}
	return color;
}


//定义获取词语下标
var a_idx = 0;
jQuery(document).ready(function($) {
	//点击body时触发事件
	$("body").click(function(e) {
		var color = getColor(); //获取颜色 如#ff6651
		//需要显示的词语
		var a = new Array("富强", "民主", "文明", "和谐", "自由", "平等", "公正", "法治", "爱国", "敬业", "诚信", "友善");
		//设置词语给span标签
		var $i = $("<span/>").text(a[a_idx]);
		//下标等于原来下标+1  余 词语总数
		a_idx = (a_idx + 1) % a.length;
		//获取鼠标指针的位置，分别相对于文档的左和右边缘。
		//获取x和y的指针坐标
		var x = e.pageX,
			y = e.pageY;
		//在鼠标的指针的位置给$i定义的span标签添加css样式
		$i.css({
			"z-index": 999999999999999999999999999999999999999999999999999999999999999999999,
			"top": y - 20,
			"left": x,
			"position": "absolute",
			"font-weight": "bold",
			"color": color,
		});
		//在body添加这个标签
		$("body").append($i);
		//animate() 方法执行 CSS 属性集的自定义动画。
		//该方法通过CSS样式将元素从一个状态改变为另一个状态。CSS属性值是逐渐改变的，这样就可以创建动画效果。
		//详情请看http://www.w3school.com.cn/jquery/effect_animate.asp
		$i.animate({
			//将原来的位置向上移动180
			"top": y - 180,
			"opacity": 0
			//1500动画的速度
		}, 1500, function() {
			//时间到了自动删除
			$i.remove();
		});
	});
});


//鼠标动，点和线就会随着鼠标动，鼠标停止点和线就会以鼠标为中心，聚拢成圆
!(function() {
	function n(n, e, t) {
		return n.getAttribute(e) || t;
	}

	function e(n) {
		return document.getElementsByTagName(n);
	}

	function t() {
		var t = e("script"),
			o = t.length,
			i = t[o - 1];

		return {
			l: o,
			z: n(i, "zIndex", -1),
			o: n(i, "opacity", 0.5),
			c: n(i, "color", "0,0,0"),
			n: n(i, "count", 99)
		};
	}

	function o() {
		(a = m.width =
			window.innerWidth ||
			document.documentElement.clientWidth ||
			document.body.clientWidth),
		(c = m.height =
			window.innerHeight ||
			document.documentElement.clientHeight ||
			document.body.clientHeight);
	}

	function i() {
		r.clearRect(0, 0, a, c);

		var n, e, t, o, m, l;

		s.forEach(function(i, x) {
				for (
					i.x += i.xa,
					i.y += i.ya,
					i.xa *= i.x > a || i.x < 0 ? -1 : 1,
					i.ya *= i.y > c || i.y < 0 ? -1 : 1,
					r.fillRect(i.x - 0.5, i.y - 0.5, 1, 1),
					e = x + 1; e < u.length; e++
				)
					(n = u[e]),
					null !== n.x &&
					null !== n.y &&
					((o = i.x - n.x),
						(m = i.y - n.y),
						(l = o * o + m * m),
						l < n.max &&
						(n === y &&
							l >= n.max / 2 &&
							((i.x -= 0.03 * o), (i.y -= 0.03 * m)),
							(t = (n.max - l) / n.max),
							r.beginPath(),
							(r.lineWidth = t / 2),
							(r.strokeStyle = "rgba(" + d.c + "," + (t + 0.2) + ")"),
							r.moveTo(i.x, i.y),
							r.lineTo(n.x, n.y),
							r.stroke()));
			}),
			x(i);
	}

	var a,
		c,
		u,
		m = document.createElement("canvas"),
		d = t(),
		l = "c_n" + d.l,
		r = m.getContext("2d"),
		x =
		window.requestAnimationFrame ||
		window.webkitRequestAnimationFrame ||
		window.mozRequestAnimationFrame ||
		window.oRequestAnimationFrame ||
		window.msRequestAnimationFrame ||
		function(n) {
			window.setTimeout(n, 1e3 / 45);
		},
		w = Math.random,
		y = {
			x: null,
			y: null,
			max: 2e4
		};
	(m.id = l),
	(m.style.cssText =
		"position:fixed;top:0;left:0;z-index:" + d.z + ";opacity:" + d.o),
	e("body")[0].appendChild(m),
		o(),
		(window.onresize = o),
		(window.onmousemove = function(n) {
			(n = n || window.event), (y.x = n.clientX), (y.y = n.clientY);
		}),
		(window.onmouseout = function() {
			(y.x = null), (y.y = null);
		});

	for (var s = [], f = 0; d.n > f; f++) {
		var h = w() * a,
			g = w() * c,
			v = 2 * w() - 1,
			p = 2 * w() - 1;
		s.push({
			x: h,
			y: g,
			xa: v,
			ya: p,
			max: 6e3
		});
	}

	(u = s.concat([y])),
	setTimeout(function() {
		i();
	}, 100);
})();
