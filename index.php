<html>  
<LINK href="style.css" rel="stylesheet" type="text/css"> 
 <head>  
  <script type="application/javascript">  
 function encode(orig){
 var encoded = "0";
		for (var i = 0; i<orig.length; ++i){
			if (parseInt(""+orig.charAt(i)) == 0){
				encoded += encoded.charAt(i);
			}
			else{
				encoded += ""+((parseInt(""+encoded.charAt(i))+1)%2);
			}
		}
		return encoded;
 }
function draw() {  
	var ids = new Array();
	var topbar = 200;
	var numVideos = 8;
	var vidHeight = 165;
	var width = 400;
	var height = 40;
	var gray = "rgb(127,127,127)";
	var green = "rgb(19,200,19)";
	//generate random ids, but all according to frame number modulo numVideos
	for (var i = 0; i<numVideos; ++i){
		ids[i] = ((numVideos)*2*(i+1)%256)+i;
	}
	 
		var top_orig = topbar.toString(2);
		//lazy zero padding
		while (top_orig.length < 8){
			top_orig = "0"+top_orig;
		}
		var top = encode(top_orig);
	for (var i = 0; i < ids.length; ++i){
		var canvasID = new String(""+i);
		while (canvasID.length < 2){
			canvasID = "0"+canvasID;
		}
		canvasID = "frame" + canvasID;
		var canvas = document.getElementById(canvasID);  
		var ctx = canvas.getContext("2d"); 
		var bottom_orig = ids[i].toString(2);
		while (bottom_orig.length < 8){
			bottom_orig = "0"+bottom_orig;
		}
		var bottom = encode(bottom_orig);
		var values = "";
		var prev_val = 0;
		var prev = gray;
		
		for (var j = 0; j < 8; ++j){
			var bit = parseInt(top.charAt(j));
			if (bit == 0){
				ctx.fillStyle = gray;
			}
			else{
				ctx.fillStyle = green;
			}
			ctx.fillRect(j*width/8, 0, width/8, height);
			ctx.fillStyle = "rgb(0,0,0)";
			ctx.fillRect(0, height, width, vidHeight);
			if (j < 8-bottom.length){
				ctx.fillStyle = gray;
			}
			else{
				var bit = parseInt(bottom.charAt(j-(8-bottom.length)));
				if (bit == 0){
					ctx.fillStyle = gray;
				}
				else{
					ctx.fillStyle = green;
				}
			}
			
			ctx.fillRect(j*width/8, height+vidHeight, width/8, height);
		}
	}
}  
  </script>  
 </head>  
<body onload="draw()">  
	<div id = 'content'>
		<div id = 'center'>
			<div id = 'videobox'>
				<div class = 'video'>
					<canvas width = "400" height = "245" id="frame00"></canvas> 
						<video width = "400" autoplay = "autoplay" loop = "loop">
							<source src = 'walle/demo00.mp4' type = "video/mp4" >
						</video>
				</div>
				<div class = 'video'>
					<canvas width = "400" height = "245" id="frame01"></canvas> 
						<video width = "400" autoplay = "autoplay" loop = "loop">
							<source src = 'walle/demo01.mp4' type = "video/mp4" >
						</video>
				</div>
				<div class = 'video'>
					<canvas width = "400" height = "245" id="frame02"></canvas> 
						<video width = "400" autoplay = "autoplay" loop = "loop">
							<source src = 'walle/demo02.mp4' type = "video/mp4" >
						</video>
				</div>
				<div class = 'video'>
					<canvas width = "400" height = "245" id="frame04"></canvas> 
						<video width = "400" autoplay = "autoplay" loop = "loop">
							<source src = 'walle/demo04.mp4' type = "video/mp4" >
						</video>
				</div>
				<div class = 'video'>
					<h1>Audio Demo</h1>
				</div>
				<div class = 'video'>
					<canvas width = "400" height = "245" id="frame05"></canvas> 
						<video width = "400" autoplay = "autoplay" loop = "loop">
							<source src = 'walle/demo05.mp4' type = "video/mp4" >
						</video>
				</div>
				<div class = 'video'>
					<canvas width = "400" height = "245" id="frame06"></canvas> 
						<video width = "400" autoplay = "autoplay" loop = "loop">
							<source src = 'walle/demo06.mp4' type = "video/mp4" >
						</video>
				</div>
				<div class = 'video'>
					<canvas width = "400" height = "245" id="frame07"></canvas> 
						<video width = "400" autoplay = "autoplay" loop = "loop">
							<source src = 'walle/demo07.mp4' type = "video/mp4" >
						</video>
				</div>
				<div class = 'video'>
					<canvas width = "400" height = "245" id="frame03"></canvas> 
						<video width = "400" autoplay = "autoplay" loop = "loop">
							<source src = 'walle/demo03.mp4' type = "video/mp4" >
						</video>
				</div>
			</div>
	   </div>
	</div>
 </body>  
</html>  
<!--<html>
<head>

<script type = 'application/javascript'>
function drawBars(){
	var ids = new Array();
	var numVideos = 8;
	var width = 400;
	var gray = "rgb(127, 127, 127)";
	var green = "rgb(12, 127, 12)";
	//generate random ids, but all according to frame number modulo numVideos
	for (var i = 0; i<numVideos; ++i){
		ids[i] = ((numVideos)*2*(i+1)%256)+i;
	}
	//for (var i = 0; i<numVideos; ++i){
		//var canvasID = new String(""+i);
		//while (canvasID.length < 2){
		//	canvasID = "0"+canvasID;
		//}
		//canvasID = "frame" + canvasID;
		var canvasID = "frame00";
		var canvas = document.getElementById('frame00');
		var ctx = canvas.getContext("2D");
		var num = ""+8;
		num = parseInt(num, 2);
		for (var j = 0; j < 8; ++i){
			if (j < num.length){
				ctx.fillStyle = gray;
			}
			else{
				var bit = (""+num).charAt(j-num.length);
				switch (bit){
					case '0': ctx.fillStyle = gray; break;
					case '1': ctx.fillStyle = green; break;
				}
			}
			ctx.fillRect(j*width/8, 0, width/8, width/8);
		}
	//}
}

</script>
</head>
<body onload = 'drawBars()'>
<h1>Audio Demo v1</h1>

<div id = 'content'>
	<div id = 'center'>
		<div id = 'videobox'>
			
			<canvas width="400" id = 'frame00'></canvas>
			<!--
			<div class = 'video' id = 'one'>
				<video width = "400" autoplay = "autoplay" loop = "loop">
					<source src = 'walle/demo00.mp4' type = "video/mp4" >
				</video>
			</div>
			<div class = 'video' id = 'two'>
				<video width ="400" autoplay = "autoplay" loop = "loop">
					<source src = 'walle/demo01.mp4' type = "video/mp4" />
				</video>
			</div>
			<div class = 'video' id = 'three'>
				<video width = "400" autoplay = "autoplay" loop = "loop">
					<source src = 'walle/demo02.mp4' type = "video/mp4" >
				</video>
			</div>
			<div class = 'video' id = 'four'>
				<video width ="400" autoplay = "autoplay" loop = "loop">
					<source src = 'walle/demo03.mp4' type = "video/mp4" />
				</video>
			</div>
			<div class = 'video' id = 'five'>
				<video width = "400" autoplay = "autoplay" loop = "loop">
					<source src = 'walle/demo04.mp4' type = "video/mp4" >
				</video>
			</div>
			<div class = 'video' id = 'six'>
				<video width ="400" autoplay = "autoplay" loop = "loop">
					<source src = 'walle/demo05.mp4' type = "video/mp4" />
				</video>
			</div>
			<div class = 'video' id = 'seven'>
				<video width = "400" autoplay = "autoplay" loop = "loop">
					<source src = 'walle/demo06.mp4' type = "video/mp4" >
				</video>
			</div>
			<div class = 'video' id = 'eight'>
				<video width ="400" autoplay = "autoplay" loop = "loop">
					<source src = 'walle/demo07.mp4' type = "video/mp4" />
				</video>
			</div>-->
		<!--</div>
	</div>
</div>
</body>
</html> -->