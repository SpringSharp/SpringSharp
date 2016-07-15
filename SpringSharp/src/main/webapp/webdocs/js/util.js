//屏蔽IE的部分按键
//author:  leiyang  2007-07-18

//配置是否使用屏蔽鼠标右键(true为使用，false为不使用)
var bContextMenu = false;
//配置是否使用屏蔽鼠标左键拖动(true为使用，false为不使用)
var bSelectStart = false;
//配置是否使用屏蔽F1帮助(true为使用，false为不使用)
var bHelp = false;
//配置是否使用屏蔽部分IE快捷键(true为使用，false为不使用)
var bKeydown = false;
	
if(bContextMenu){
	//屏蔽  鼠标右键(不包含Input type="text"和textarea)
	window.document.oncontextmenu = function(){
		var oElement = window.event.srcElement;
		if(oElement.tagName == "INPUT"){
			if(oElement.type == "text"){
				return true;
			}
		}
		if(oElement.tagName == "TEXTAREA") {
			return true;
		}
		return false;
	};
}

if(bSelectStart){
	//屏蔽  鼠标左键拖动(不包含Input type="text"和textarea)
	window.document.onselectstart = function(){
		var oElement = window.event.srcElement;
		if(oElement.tagName == "INPUT"){
			if(oElement.type == "text"){
				return true;
			}
		}
		if(oElement.tagName == "TEXTAREA") {
			return true;
		}
		return false;
	};
}

if(bHelp){
	//屏蔽  F1帮助  
	window.onhelp = function(){
		window.event.keyCode = 505;
		return false;
	};
}

if(bKeydown){
	window.document.onkeydown = function(){  
		//屏蔽  Alt + 方向键 ←  
		//屏蔽  Alt + 方向键 →
		if((window.event.altKey) && ((window.event.keyCode==37) || (window.event.keyCode==39))){
			window.event.keyCode = 505;
			return false;
		}
	
		//屏蔽  BackSpace(不包含Input type="text"和textarea)
		if(window.event.keyCode==8){
			var oElement = window.event.srcElement;
			if(oElement.tagName == "INPUT"){
				if(oElement.type == "text"){
					return true;
				}
			}
			if(oElement.tagName == "TEXTAREA") {
				return true;
			}
			window.event.keyCode = 505;
			return false;
		}
		
		//屏蔽  F2 - F12 键
		if((window.event.keyCode>=113) && (window.event.keyCode<=123)){
			window.event.keyCode = 505;
	        return false; 
		}
		
		//屏蔽  Ctrl + (a - z (不包含c、v))键
		if(window.event.ctrlKey && ((window.event.keyCode>=65) && (window.event.keyCode<=90))){
			if((window.event.keyCode == 67) || (window.event.keyCode == 86)){
				return true;
			}
			window.event.keyCode = 505;
	        return false; 
		}
		
		//屏蔽  Ctrl + (F1 - F12)键
		if(window.event.ctrlKey && ((window.event.keyCode>=112) && (window.event.keyCode<=123))){
			window.event.keyCode = 505;
	        return false; 
		}
	};
}

//当(window)窗口改变大小时
window.onresize = function(){
	try{
		var oBody = window.document.body;
		var oDivSending = window.document.getElementById("sending");
		var oDivBackground = window.document.getElementById("sendingBackground");
		var oDivSendingCurrency = window.document.getElementById("sendingCurrency");
	
		if((oBody != null) && (oDivSending != null) && (oDivBackground != null)){
			//网页正文全文高
			var bodyHeigth = oBody.scrollHeight;
			//网页正文全文宽
			var bodyWidth = oBody.scrollWidth;
			//网页可见区域高
			var displayHeight = oBody.clientHeight;
			//网页可见区域宽
			var displayWidth = oBody.clientWidth;
			//DivSending高
			var divSendingHeight = $("#sending").height();
	
			//获取oBody的位置
			var bbPosition = $("body").offset();
			
			//设置oDivBackground
			oDivBackground.style.height = bodyHeigth;
			oDivBackground.style.width = bodyWidth;
	
			//设置DivSending位置
			var divSendingLeft = bbPosition.left;
			//var divSendingTop = displayHeight/2 - divSendingHeight + oBody.scrollTop;
			var divSendingTop = 0;
			if(displayHeight <= divSendingHeight){
				divSendingTop = 0;
			}
			else {
				divSendingTop = displayHeight/2 - divSendingHeight/2;
			}
			oDivSending.style.left =divSendingLeft;
			oDivSending.style.top = divSendingTop;
		}
		
		if((oBody != null) && (oDivSendingCurrency != null) && (oDivBackground != null)){
			//网页正文全文高
			var bodyHeigth = oBody.scrollHeight;
			//网页正文全文宽
			var bodyWidth = oBody.scrollWidth;
			//网页可见区域高
			var displayHeight = oBody.clientHeight;
			//网页可见区域宽
			var displayWidth = oBody.clientWidth;
			//DivSending高
			var divSendingHeight = $("#sendingCurrency").height();
	
			//获取oBody的位置
			var bbPosition =  $("body").offset();
			
			//设置oDivBackground
			oDivBackground.style.height = bodyHeigth;
			oDivBackground.style.width = bodyWidth;
	
			//设置DivSending位置
			var divSendingLeft = bbPosition.left;
			//var divSendingTop = displayHeight/2 - divSendingHeight + oBody.scrollTop;
			var divSendingTop = 0;
			if(displayHeight <= divSendingHeight){
				divSendingTop = 0;
			}
			else {
				divSendingTop = displayHeight/2 - divSendingHeight/2;
			}
			oDivSendingCurrency.style.left = divSendingLeft;
			oDivSendingCurrency.style.top = divSendingTop;
		}
	}
	catch(ex){
		/*var debug = "";
		for(var i in ex){
			debug += i + "  = " + ex[i] + "\n";
		}
		alert(debug);*/
		return;
	}
};

//当(window)滚动条滚动时
window.onscroll = function(){
	try{
		var oBody = window.document.body;
		var oDivSending = window.document.getElementById("sending");
		var oDivBackground = window.document.getElementById("sendingBackground");
		var oDivSendingCurrency = window.document.getElementById("sendingCurrency");
			
		if((oBody != null) && (oDivSending != null) && (oDivBackground != null)){
			//网页正文全文高
			var bodyHeigth = oBody.scrollHeight;
			//网页正文全文宽
			var bodyWidth = oBody.scrollWidth;
			//网页可见区域高
			var displayHeight = oBody.clientHeight;
			//网页可见区域宽
			var displayWidth = oBody.clientWidth;
			
			//DivSending高
			var divSendingHeight = $("#sending").height();
	
			//获取oBody的位置
			var bbPosition = $("body").offset();
			
			//设置oDivBackground
			oDivBackground.style.height = bodyHeigth;
			oDivBackground.style.width = bodyWidth;
	
			//设置DivSending位置
			var divSendingLeft = bbPosition.left;
			//var divSendingTop = displayHeight/2 - divSendingHeight/2 + oBody.scrollTop;
			var divSendingTop = 0;
			if(displayHeight <= divSendingHeight){
				divSendingTop = 0;
			}
			else {
				divSendingTop = displayHeight/2 - divSendingHeight/2;
			}
			oDivSending.style.left =divSendingLeft;
			oDivSending.style.top = divSendingTop;
		}
		
		if((oBody != null) && (oDivSendingCurrency != null) && (oDivBackground != null)){
			//网页正文全文高
			var bodyHeigth = oBody.scrollHeight;
			//网页正文全文宽
			var bodyWidth = oBody.scrollWidth;
			//网页可见区域高
			var displayHeight = oBody.clientHeight;
			//网页可见区域宽
			var displayWidth = oBody.clientWidth;
			//DivSending高
			var divSendingHeight = $("#sendingCurrency").height();
	
			//获取oBody的位置
			var bbPosition = $("body").offset();
			
			//设置oDivBackground
			oDivBackground.style.height = bodyHeigth;
			oDivBackground.style.width = bodyWidth;
	
			//设置DivSending位置
			var divSendingLeft = bbPosition.left;
			//var divSendingTop = displayHeight/2 - divSendingHeight + oBody.scrollTop;
			var divSendingTop = 0;
			if(displayHeight <= divSendingHeight){
				divSendingTop = 0;
			}
			else {
				divSendingTop = displayHeight/2 - divSendingHeight/2;
			}
			oDivSendingCurrency.style.left =divSendingLeft;
			oDivSendingCurrency.style.top = divSendingTop;
		}
	}
	catch(ex){
		/*var debug = "";
		for(var i in ex){
			debug += i + "  = " + ex[i] + "\n";
		}
		alert(debug);*/
		return;
	}
};

//***********************************************************************
//控制显示"正在执行中, 请稍候..."提示信息的方法
function showSending(){
	gnIsShowSending = 1;
	
	
	try{
		var oBody = window.document.body;
		if(oBody != null ){
			//网页正文全文高
			var bodyHeigth = oBody.scrollHeight;
			//网页正文全文宽
			var bodyWidth = oBody.scrollWidth;
			//网页可见区域高
			var displayHeight = oBody.clientHeight;
			//网页可见区域宽
			var displayWidth = oBody.clientWidth;
			//获取oBody的位置
			var bbPosition = $("body").offset();
			
			//在内存中创建一个oDivSending对象
			var oDivSending = document.createElement("DIV");
			var subHTML = "<table width=\"100%\"><tr><td align=\"center\">";
			subHTML += "<table bgcolor=\"#ff9900\" width=\"380\" height=\"70\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">";
			subHTML += "<tr><td bgcolor=\"#eeeeee\" align=\"center\"><img style='vertical-align:middle;' src=\"/webdocs/SpringSharp/graphics/loading.gif\"/> 正在执行中, 请稍候 ...</td></tr></table></td></tr></table>";
			
			oDivSending.innerHTML = subHTML;
			oDivSending.id = "sending";
			oDivSending.style.position = "absolute";
			oDivSending.style.zIndex = 501;
			oBody.appendChild(oDivSending);
			
			//获取DivSending高度
			var divSendingHeight = $(oDivSending).height();
			//获取DivSending宽度
			var divSendingWidth = $(oDivSending).width();
			
			
			//在内存中创建一个oDivBackground对象
			var oDivBackground = document.createElement("DIV");
			//oDivBackground = window.document.createElement("IFRAME");
			oDivBackground.id = "sendingBackground";
			oDivBackground.style.position = "absolute";
			oDivBackground.style.left = bbPosition.left;
			oDivBackground.style.top = bbPosition.top;
			oDivBackground.style.zIndex = 500;
			//oDivBackground.style.backgroundColor = "beige";
			oDivBackground.style.backgroundColor = "white";
			oDivBackground.style.filter = "progid:DXImageTransform.Microsoft.Alpha(Opacity=60, FinishOpacity=30, Style=0, StartX=0,  FinishX=0, StartY=0, FinishY=0)";
			oDivBackground.style.height = bodyHeigth;
			oDivBackground.style.width = bodyWidth;
			oDivBackground.style.display = "block";
			oBody.appendChild(oDivBackground);
			
			
			//disabled所有的<SELECT>标签
			/*var oSelectColl = window.document.getElementsByTagName("SELECT");
			for(var i=0; i<oSelectColl.length; i++) {
				var oSelect = oSelectColl[i];
				var oParent = oSelect.parentNode;
				
				var selectId = oSelect.id
				var selectName = oSelect.name;
				var selectValue = "";
	
				if(oSelect.multiple == true){
					//有select标签有multiple属性
					for(var j=0; j<oSelect.options.length; j++){
						var oOption = oSelect.options(j);
						//if(oOption.selected == true)
						selectValue = oOption.value;
	
						var oHidden =  window.document.createElement("INPUT");
						oHidden.type = "hidden";
						if(selectId != ""){
							oHidden.id = selectId;
						}
						if(selectName != ""){
							oHidden.name = selectName;
						}
						oHidden.value = selectValue;
						oParent.appendChild(oHidden);
					}
				}
				else{
					//有select标签为单选
					if(oSelect.options.length > 0 && oSelect.selectedIndex >= 0){
						selectValue = oSelect.options(oSelect.selectedIndex).value;
					}
					
					var oHidden =  window.document.createElement("INPUT");
					oHidden.type = "hidden";
					if(selectId != ""){
						oHidden.id = selectId;
					}
					if(selectName != ""){
						oHidden.name = selectName;
					}
					oHidden.value = selectValue;
					oParent.appendChild(oHidden);
				}
				
				oSelect.disabled = "true";
			}
			*/
			//设置DivSending位置
			var divSendingLeft;
			if(bbPosition.left == 0){
				divSendingLeft = displayWidth/2 - divSendingWidth/2;
			}else{
				divSendingLeft = bbPosition.left;
			}
			//var divSendingTop = displayHeight/2 - divSendingHeight + oBody.scrollTop;
			var divSendingTop = 0;
			if(displayHeight <= divSendingHeight){
				divSendingTop = 0;
			}
			else {
				divSendingTop = displayHeight/2 - divSendingHeight/2;
			}
			oDivSending.style.left = divSendingLeft;
			oDivSending.style.top = divSendingTop-35;
			oDivSending.style.display = "block";
				
			//oIsShowSending.value = "false";
			oBody.focus(); 
		}
	}
	catch(ex){
		//var debug = "";
		//for(var i in ex){
		//	debug += i + "  = " + ex[i] + "\n";
		//}
		//alert(debug);
		return;
	}
}
function hideSending(){
	$('#sending').remove();
	$('#sendingBackground').remove();
}

//iframe onLoad
function iframeOnLoad(){
	var oDivBackground = window.document.getElementById("sendingBackground");
	var oDivSending = window.document.getElementById("sending");
	var oIsShowSending =  window.document.getElementById("mainIsShowSending");
	if((oDivSending != null) && (oDivBackground != null)){
		window.document.body.removeChild(oDivBackground);
		window.document.body.removeChild(oDivSending);
		
	}
	if(oIsShowSending != null){
		oIsShowSending.value = "true";
	}
}

//-->
