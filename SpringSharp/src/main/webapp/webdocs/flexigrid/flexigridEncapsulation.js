/**
 * 针对业务系统。对flexigrid进行了二次封装 jiamiao 20090728 iss
 * 
 * 
 * colModel的配置如下
 * 
 * colModel : [ {display: '序号', name : 'id', width : 50, sortable : false,
 * align: 'left'}, {display: '题名', name : 'papTitle', width : 150, sortable :
 * true, align: 'left'}, {display: '作者', name : 'papAuthor', width : 100,
 * sortable : true, align: 'left'}, {display: '期刊', name : 'papJourney', width :
 * 100, sortable : true, align: 'left'}, {display: '发表日期', name : 'papPubtime',
 * width : $(window).width()-462, sortable : true, align: 'left'} ],
 * 
 */
(function($) {
	var docloaded = false;

	$(document).ready(function() {
		docloaded = true;
	});

	$.fn.extend({
		flexigridenc : function(p) {
			p = $.extend({
				colModel : [],// 列参数
				params : [],//参数
				searchitems : false,//查询窗口
				buttons : false,// 按钮
				rpOptions : [10, 15, 20, 25, 50, 100],
				title : '',// 表头的名字
				classMethod : '',// 要调用的方法
				usepager : true,//是否启用分页
				rp : 10,// 预设每页行数
				page : 1,// 预设当前页数
				sortname : '',// 预设指定哪列进行排序
				sortorder : '',// 预设排序的方式
				procmsg : '数据读取中,请稍候...',// 读取时的信息
				pagestat : '显示从 {from} 到 {to} 条数据，共有 {total} 条数据',// 分页器的显示信息
				nomsg : '找不到符合条件的数据',// 没有数据时的信息
				width : $(this).parent().width() - 2 || $(this).width() - 2 || 'auto',
				height : 328, // 表格高度(注意不能使用100%之类的字符串)
				adaptiveHeight : true, //自适应高度
				isSuggest : false, //是否为放大镜
				tableId : 'flexlist', //默认的tableId名称
				userFunc : false,//提交前调用的方法
				query: '', //查询
				qtype: '', //查询对应的字段
				callback : '', //数据加载完成时自定义方法
				slider : false, //是否显示滑动
				printbutton : true,
				exportbutton : true,
				isEncode : false,
				resizable : false,
			 	paramCache:true,
			 	colDrag : true, //是否可以拖拽列头
			 	submitSuccessFunc:'',
			 	submitErrorFunc:''
				}, p);
				if(p.isSuggest == false)
				{
					var indent = 0;//缩进值 
					if($.browser.msie) {
						if($.browser.version == 6.0 || $.browser.version == 7.0) {
							indent = 50;
						}
						else if($.browser.version == 8.0) {
							indent = 100;
						}
					}
					
					var _tableId = $(this).attr("id") || p.tableId;
					if(_tableId)
					{
						if($("#" + _tableId).size() > 0) {
							$("#" + _tableId).parent().resize(function(){
								$("#" + _tableId).parent().parent().width(document.documentElement.clientWidth - indent);
							});
						}
						else {
							$(this).parent().resize(function(){
								$(this).parent().parent().width(document.documentElement.clientWidth - indent);
							});
						}
					}
					else {
						$(this).parent().resize(function(){
							$(this).parent().parent().width(document.documentElement.clientWidth - indent);
						});
					}
				}
			
				if (!docloaded) {
					$(document).ready(function() {
						$.flexme(this, p);
					});
				} else {
					$.flexme(this, p);
				}
				
				return this;
		},
		serializeArray4Flex : function(p) {
			p = $.extend({
						isEncode : false
					}, p);
			return this.map(function() {
						return this.elements
								? $.makeArray(this.elements)
								: this;
					}).filter(function() {
				return this.name
						&& !this.disabled
						&& (this.checked
								|| /select|textarea/i.test(this.nodeName) || /text|hidden|password|search/i
								.test(this.type));
			}).map(function(i, elem) {
				var val = $(this).val();
				if (p.isEncode) {
					val = encodeURIComponent(val);
				}
				
				return val == null ? null : $.isArray(val) ? $.map(val,
						function(val, i) {
							if (!(elem.name == "rp" || elem.name == "strClassMethod")) {
								return {
									name : elem.name,
									value : val
								};
							}
						}) : (!(elem.name == "rp" || elem.name == "strClassMethod")) ? {
					name : elem.name,
					value : val
				} : null;
			}).get();
		}
	});
	
	/**
	 * 二次封装的flexigrid实现。可满足个性化需求
	 */
	$.flexme = function(t, p) {
		
		var _params = new Array();
		for (var param in p.params) {
			_params.push({
						name : param,
						value : encodeURIComponent(p.params[param])
					});
		}		

		//alert(_params_jasper);
		
		//生成打印和导出按钮
		if(p.printbutton || p.exportbutton) {
			p.buttons = new Array();
			if(p.printbutton){
				p.buttons.push({
					name : '打印',
					bclass : 'print',
					onpress : function(com, grid) {
						if ($("#printpanel").length == 0)
						{
							var dialogHtml = '';
							dialogHtml += '<div id="printpanel" title="打印">';
							dialogHtml += '<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0"><tbody>';
							dialogHtml += '<tr height="30">';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="20%" align="right">页面边距：</td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="65%" align="left">顶部&nbsp;<select name="pageTop"><option value="20" selected>20px</option><option value="30">30px</option><option value="40">40px</option><option value="50">50px</option><option value="60">60px</option></select>&nbsp;&nbsp;&nbsp;&nbsp;左对齐&nbsp;<select name="pageLeft"><option value="5" >5px</option><option value="10">10px</option><option value="15">15px</option><option value="20" selected>20px</option><option value="25">25px</option></select></td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '</tr>';
							dialogHtml += '<tr height="30">';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="20%" align="right"></td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="65%" align="left">底部&nbsp;<select name="pageButtom"><option value="20" selected>20px</option><option value="30">30px</option><option value="40">40px</option><option value="50">50px</option><option value="60">60px</option></select>&nbsp;&nbsp;&nbsp;&nbsp;右对齐&nbsp;<select name="pageRight"><option value="5" >5px</option><option value="10">10px</option><option value="15">15px</option><option value="20" selected>20px</option><option value="25">25px</option></select></td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '</tr>';
							dialogHtml += '<tr height="30">';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="20%" align="right">打印内容：</td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="65%" align="left"><input type="radio" name="pageChoose" value="this" checked="true"/>当前页&nbsp;&nbsp;<input type="radio" name="pageChoose" value="all"/>所有页</td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '</tr>';
							dialogHtml += '<tr height="30">';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="20%" align="right">打印方向：</td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="65%" align="left"><input type="radio" name="pageType" value="1" checked="true"/>竖向&nbsp;&nbsp;<input type="radio" name="pageType" value="2"/>横向</td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '</tr>';
							dialogHtml += '</tbody></table></div>';
							$('body').prepend(dialogHtml);
						}

						$("#printpanel").dialog({
							bgiframe : true,
							resizable: false,
							width : 400,
							modal : true,
							buttons : {
								"关闭" : function() {
									$(this).dialog('close');
								},
								"确定" : function() {
									var pageType = $('input[type="radio"][name="pageType"][checked="true"]', this).val();
									var pageSize = $('select[name="pageSize"]', this).val();
									var pageTop = $('select[name="pageTop"]', this).val();
									var pageLeft = $('select[name="pageLeft"]', this).val();
									var pageButtom = $('select[name="pageButtom"]', this).val();
									var pageRight = $('select[name="pageRight"]', this).val();
									var pageChoose = $('input[type="radio"][name="pageChoose"][checked="true"]', this).val();
									
									var idsArray = new Array();
									var titleArray = new Array();
									var tdWidthArray = new Array();
									var elTypeArray = new Array();
									
									$.each($('.hDiv > .hDivBox > table > thead > tr > th', grid), function(i, n) {
										if($('div:visible',this).size() > 0){
											
											if($('div:visible', this).parent().data('cm').elType == null){
												idsArray.push($(this).attr('idx'));
												titleArray.push($('div:visible', this).text());
												tdWidthArray.push($('div:visible', this).parent().data('cm').width);
												elTypeArray.push('String');
											}
											else if($('div:visible', this).parent().data('cm').elType == 'link'){
												idsArray.push($(this).attr('idx'));
												titleArray.push($('div:visible', this).text());
												tdWidthArray.push($('div:visible', this).parent().data('cm').width);
												elTypeArray.push('link');
											}
											else if($('div:visible', this).parent().data('cm').elType == 'rownum'){
												idsArray.push($(this).attr('idx'));
												titleArray.push($('div:visible', this).text());
												tdWidthArray.push($('div:visible', this).parent().data('cm').width);
												elTypeArray.push('rownum');
											}
											else if($('div:visible', this).parent().data('cm').elType == 'checkbox'){
												idsArray.push($(this).attr('idx'));
												titleArray.push($('div:visible', this).text());
												tdWidthArray.push($('div:visible', this).parent().data('cm').width);
												elTypeArray.push('checkbox');												
											}
										}
									});

									var _p = $(".hDiv > .hDivBox > table > thead > tr > th > div:visible",grid).parent().data("p");
									
									//alert($.param(_p.params));
									
									//------------------------------------begin
									$(this).dialog('close');
									//showSending();
									
									var tempForm = document.createElement("form");
									tempForm.action = '/NASApp/JasperPrintServlet';
									tempForm.method = "post";
									tempForm.target = "_blank";
									$(tempForm).attr("id","tempPrintForm");
									
									$(tempForm).append("<input type='hidden' name='strClassMethod' value='"+ p.classMethod +"'/>");
									$(tempForm).append("<input type='hidden' name='usepager' value='"+ p.usepager +"'/>");
									$(tempForm).append("<input type='hidden' name='pageType' value='"+ pageType +"'/>");
									$(tempForm).append("<input type='hidden' name='pageTop' value='"+ pageTop +"'/>");
									$(tempForm).append("<input type='hidden' name='pageButtom' value='"+ pageButtom+"'/>");
									$(tempForm).append("<input type='hidden' name='pageLeft' value='"+ pageLeft +"'/>");
									$(tempForm).append("<input type='hidden' name='pageRight' value='"+ pageRight +"'/>");
									$(tempForm).append("<input type='hidden' name='pageChoose' value='"+ pageChoose +"'/>");
									$(tempForm).append("<input type='hidden' name='ids' value='"+ idsArray.join(',') +"'/>");
									$(tempForm).append("<input type='hidden' name='tdWidths' value='"+ tdWidthArray.join(',') +"'/>");
									$(tempForm).append("<input type='hidden' name='titles' value='"+ titleArray.join(',') +"'/>");
									$(tempForm).append("<input type='hidden' name='elTypes' value='"+ elTypeArray.join(',')+"'/>");
									$(tempForm).append("<input type='hidden' name='page' value='"+ _p.page +"'/>");
									$(tempForm).append("<input type='hidden' name='sortname' value='"+ _p.sortname+"'/>");
									$(tempForm).append("<input type='hidden' name='sortorder' value='"+ _p.sortorder+"'/>");
									$(tempForm).append("<input type='hidden' name='rp' value='"+ _p.rp+"'/>");
									$(tempForm).append("<input type='hidden' name='tableTitle' value='"+ p.title+"'/>");
									var parameters=_p.params;
								 	for(var x=0;x<parameters.length;x++){		
								 		$(tempForm).append("<input type='hidden' name='"+parameters[x].name+"' value='"+decodeURIComponent(parameters[x].value)+"'/>");
								 	}
								 	
									$('body').append($(tempForm));
									
									$(tempForm).submit();
								}
							},
							close : function() {
								$("#printpanel").remove();
							}
						});
					}
				});
				p.buttons.push({separator: true});
			}
			if(p.exportbutton){
				p.buttons.push({
					name : '导出',
					bclass : 'export',
					onpress : function(com, grid) {
					
						if ($("#exportpanel").length == 0)
						{
							var dialogHtml = '';
							dialogHtml += '<div id="exportpanel" title="导出">';
							dialogHtml += '<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0"><tbody>';
							dialogHtml += '<tr height="30">';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="20%" align="right">导出内容：</td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '<td width="65%" align="left"><input type="radio" name="pageChoose" value="this" checked="true"/>当前内容&nbsp;&nbsp;<input type="radio" name="pageChoose" value="all"/>所有内容</td>';
							dialogHtml += '<td width="5%"></td>';
							dialogHtml += '</tr>';
							dialogHtml += '</tbody></table></div>';
							$('body').prepend(dialogHtml);
						}
					
						$("#exportpanel").dialog({
							bgiframe : true,
							resizable: false,
							width : 400,
							modal : true,
							buttons : {
								"关闭" : function() {
									$(this).dialog('close');
								},
								"确定" : function() {
									var pageChoose = $('input[type="radio"][name="pageChoose"][checked="true"]', this).val();
									
									var idsArray = new Array();
									var titleArray = new Array();
									var tdWidthArray = new Array();
									var elTypeArray = new Array();
									
									$.each($('.hDiv > .hDivBox > table > thead > tr > th', grid), function(i, n) {
										if($('div:visible',this).size() > 0){											
											if($('div:visible', this).parent().data('cm').elType == null){
												idsArray.push($(this).attr('idx'));
												titleArray.push($('div:visible', this).text());
												//tdWidthArray.push($('div:visible', this).parent().data('cm').width);
												tdWidthArray.push($('div:visible', this).css("width").replace("px",""));
												elTypeArray.push('String');												
											}
											else if($('div:visible', this).parent().data('cm').elType == 'link'){
												idsArray.push($(this).attr('idx'));
												titleArray.push($('div:visible', this).text());
												//tdWidthArray.push($('div:visible', this).parent().data('cm').width);
												tdWidthArray.push($('div:visible', this).css("width").replace("px",""));
												elTypeArray.push('link');												
											}
											else if($('div:visible', this).parent().data('cm').elType == 'rownum'){
												idsArray.push($(this).attr('idx'));
												titleArray.push($('div:visible', this).text());
												//tdWidthArray.push($('div:visible', this).parent().data('cm').width);
												tdWidthArray.push($('div:visible', this).css("width").replace("px",""));
												elTypeArray.push('rownum');												
											}
											else if($('div:visible', this).parent().data('cm').elType == 'checkbox'){
												idsArray.push($(this).attr('idx'));
												titleArray.push($('div:visible', this).text());
												//tdWidthArray.push($('div:visible', this).parent().data('cm').width);
												tdWidthArray.push($('div:visible', this).css("width").replace("px",""));
												elTypeArray.push('checkbox');												
											}
										}
									});

									var _p = $(".hDiv > .hDivBox > table > thead > tr > th > div:visible",grid).parent().data("p");
									
									$(this).dialog('close');
									
									$.post('/SpringSharp/FlexiGridServlet?strIsEncode=true&strIsExcel=true&' + $.param(_p.params)+"&export=true",
									{
										strUsePager:p.usepager,
										strClassMethod : p.classMethod, //执行方法
										pageChoose : pageChoose, //打印内容
										
										//params : $.param(_p.params), //参数
										ids : idsArray.join(','), //IDs
										tdWidths : tdWidthArray.join(','), //列宽
										titles : encodeURIComponent(titleArray.join(',')), // 标题
										elTypes:elTypeArray.join(','),
										
										page : _p.page, // 预设的每页行数
										sortname : _p.sortname, // 预设指定哪列进行排序
										sortorder : _p.sortorder, // 预设排序的方式
										rp : _p.rp // resultsperpage,预设的分页大小

										//colModel : p.colModel,
										//dataType : 'json' // 回传数据的类型,可以为xml和json
									},function(data)
									{	
										window.location.href('/NASApp/DownLoadFileServlet?file=' + data);
									});
								}
							},
							close : function() {
								$("#exportpanel").remove();
							}
						});
					}
				});
				p.buttons.push({separator: true});
			}
		}
		var contextUrl = '/SpringSharp/FlexiGridServlet?strUsePager='+p.usepager+'&strIsEncode=true&strClassMethod=' + p.classMethod;
		var grid = $(t).flexigrid({
			colModel : p.colModel,
			searchitems : p.searchitems,//查询窗口
			buttons : p.buttons,
			url : contextUrl,
			method : 'POST',// 数据传送模式
			dataType : 'json',// 回传数据的类型,可以为xml和json
			rp : p.rp,// resultsperpage,预设的分页大小
			newp : p.page,// 预设的每页行数
			sortname : p.sortname,// 预设指定哪列进行排序
			sortorder : p.sortorder,// 预设排序的方式
			usepager : p.usepager,// 是否启用分页器
			title : p.title,// 标题
			useRp : true,// 使用分页大小选择器
			striped : true,// 奇偶行颜色相互交叉,不设置默认为true
			showTableToggleBtn : true,// 显示或关闭隐藏栏位的开启器
			width : p.width,
			height : p.height,// 表格高度(注意不能使用100%之类的字符串)
			adaptiveHeight : p.adaptiveHeight,//自适应高度
			rpOptions : p.rpOptions,
			resizable : p.resizable,// 是否可调整列表窗口大小,不设置默认为true
			onSubmit : p.userFunc,// 数据提交时调用的自定义函数
			nowrap : true,// 是否重叠,true为不启用重叠,flase为启用重叠
			procmsg : p.procmsg,// 读取时的信息
			pagestat : p.pagestat,// 分页器的显示信息
			nomsg : p.nomsg,// 没有数据时的信息
			isSuggest : p.isSuggest,//是否为放大镜
			params : _params, //参数
			query: p.query,//查询
			qtype: p.qtype,//查询对应的字段
			callback : p.callback,//数据加载完成时自定义方法
			slider : p.slider,
			isEncode : p.isEncode,
			paramCache:p.paramCache,
			colDrag : p.colDrag,
			submitSuccessFunc:p.submitSuccessFunc,
			submitErrorFunc:p.submitErrorFunc
		});
	};

	$.ajaxSubmit= function(tableID) {
		$("#" + tableID).flexOptions({
					newp : 1
				}).ajaxSubmit();
		return false;
	}
	$.gridReload = function(tableID) {
		$("#" + tableID).flexOptions({
					newp : 1
				}).flexReload();
		return false; 
	};
	
	$.gridReloadMe = function(tableID,page) {
		$("#" + tableID).flexOptions({newp : page}).flexReload();
		return false;
	};

	$.addFormData = function(formID, tableID) {
		if($("#" + formID).size() == 0) {
			if($("form[name='"+formID+"']").size() > 0) {
				$("form[name='"+formID+"']").attr("id",formID);
			}
			else {
				alert("无法通过" + formID + "找到form。请为其添加id和name");
			}
		}
		var dt = $("#" + formID).serializeArray4Flex({
					isEncode : true
				});
		$("#" + tableID).flexOptions({
					params : dt
				});
		return true;
	}
	
	/**
	 * 为页面上的某元素赋值
	 */
	$.fn.setValue = function(value)
	{
		if (!docloaded) {
			var t = this;
			$(document).ready(function() {
				if(value && value!="" && value!="-1") {
					$(t).val(value);
				}
			});
		} else {
			if(value && value!="" && value!="-1") {
				$(this).val(value);
			}
		}
	};
	
	$.flexPost = function(url, data, callback){
		$.post(
			url, data, function(data){callback(eval(data));}, "script"
		);
	};

	$.Message = {
		setData : function(data, message) {
			return {
				strResult: data,
				strMessage: message
			};
		}
	};
	
	$(function(){
		$(".hiflex").flexigrid({
			showTableToggleBtn: true,
			useRp: true
		});
	});
	
})(jQuery);