/*
 * Flexigrid for jQuery - New Wave Grid
 *
 * Copyright (c) 2008 Paulo P. Marinas (webplicity.net/flexigrid)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * $Date: 2008-07-14 00:09:43 +0800 (Tue, 14 Jul 2008) $
 */
(function($){
		  
	$.addFlex = function(t,p)
	{
		if (t.grid) return false; //return if already exist	
		// apply default properties
		p = $.extend({
			 height: 200, //flexigrid插件的高度，单位为px
			 width: 'auto', //宽度值，auto表示根据每列的宽度自动计算
			 striped: true, //是否显示斑纹效果，默认是奇偶交互的形式
			 novstripe: false,
			 minwidth: 30, //列的最小宽度
			 minheight: 80, //列的最小宽度
			 adaptiveHeight : false,
			 resizable: true, //resizable table是否可伸缩
			 colDrag : true,//drag col
			 url: false, //ajax url,ajax方式对应的url地址
			 method: 'POST', // data sending method,数据发送方式
			 dataType: 'xml', // type of data loaded,数据加载的类型，xml,json
			 errormsg: 'Connection Error', //错误提升信息
			 usepager: false, //是否分页
			 nowrap: true, //是否不换行
			 page: 1, //current page,默认当前页
			 total: 1, //total pages,总页面数
			 useRp: true, //use the results per page select box,是否可以动态设置每页显示的结果数
			 rp: 10, // results per page,每页默认的结果数
			 rpOptions: [10,15,20,25,40],//可选择设定的每页结果数
			 title: false,//是否包含标题
			 pagestat: 'Displaying {from} to {to} of {total} items', //显示当前页和总页面的样式
			 procmsg: 'Processing, please wait ...',//正在处理的提示信息
			 query: '',//搜索查询的条件
			 qtype: '',//搜索查询的类别
			 nomsg: 'No items',//无结果的提示信息
			 usertext: '',//user text
			 isSuggest: false,// is a Suggest Object
			 minColToggle: 1, //minimum allowed column to be hidden
			 showToggleBtn: true, //show or hide column toggle popup
			 hideOnSubmit: true,//显示遮盖
			 autoload: true,//自动加载
			 blockOpacity: 0.5,//透明度设置
			 onToggleCol: false,//当在行之间转换时
			 onChangeSort: false,//当改变排序时
			 onSuccess: false, //成功后执行
			 onSubmit: false, // using a custom populate function 调用自定义的计算函数
			 callback : '', // custom function
			 slider : false,
			 isEncode : false,
			 paramCache:true,
			 tag:true,//设置是否是点击过“下一页、前一页、起始页、末页”
			 oldParm: [],  //新老数据替换
			 submitSuccessFunc : '',
			 submitErrorFunc : ''
		  }, p);

		$(t)
		.show() //show if hidden
		.attr({cellPadding: 0, cellSpacing: 0, border: 0})  //remove padding and spacing
		.removeAttr('width') //remove width properties	
		;
		
		if(p.colModel){
			$.each(p.colModel, function(){
				if(this.elType == 'checkbox') {
					p.minColToggle ++;
				}
			});
		}
		
		//create grid class
		var g = {
			hset : {},
			rePosDrag: function () {

			var cdleft = 0 - this.hDiv.scrollLeft;
			if (this.hDiv.scrollLeft>0) cdleft -= Math.floor(p.cgwidth/2);
			$(g.cDrag).css({top:g.hDiv.offsetTop+1});
			var cdpad = this.cdpad;
			
			$('div',g.cDrag).hide();
			
			var ghs =$('thead tr:first th:visible',g.hDiv);
			$('thead tr:first th:visible',this.hDiv).each
				(
						
			 	function ()
					{
					var n = ghs.index(this);

					var cdpos = parseInt($('div',this).width());
					var ppos = cdpos;
					if (cdleft==0) 
							cdleft -= Math.floor(p.cgwidth/2); 

					cdpos = cdpos + cdleft + cdpad;
					
					$('div:eq('+n+')',g.cDrag).css({'left':cdpos+'px'}).show();

					cdleft = cdpos;
					}
				);
				
			},
			fixHeight: function (newH) {
					newH = false;
					if (!newH) newH = $(g.bDiv).height();
					var hdHeight = $(this.hDiv).height();
					$('div',this.cDrag).each(
						function ()
							{
								$(this).height(newH+hdHeight);
							}
					);
					
					var nd = parseInt($(g.nDiv).height());
					
					if (nd>newH)
						$(g.nDiv).height(newH).width(200);
					else
						$(g.nDiv).height('auto').width('auto');
					
					$(g.block).css({height:newH,marginBottom:(newH * -1)});
					
					var hrH = g.bDiv.offsetTop + newH;
					if (p.height != 'auto' && p.resizable) hrH = g.vDiv.offsetTop;
					$(g.rDiv).css({height: hrH});
				
			},
			dragStart: function (dragtype,e,obj) { //default drag function start
				
				if (dragtype=='colresize') //column resize
					{
						$(g.nDiv).hide();$(g.nBtn).hide();
						var n = $('div',this.cDrag).index(obj);
						var ow = $('th:visible div:eq('+n+')',this.hDiv).width();
						$(obj).addClass('dragging').siblings().hide();
						$(obj).prev().addClass('dragging').show();
						
						this.colresize = {startX: e.pageX, ol: parseInt(obj.style.left), ow: ow, n : n };
						$('body').css('cursor','col-resize');
					}
				else if (dragtype=='vresize') //table resize
					{
						var hgo = false;
						$('body').css('cursor','row-resize');
						if (obj) 
							{
							hgo = true;
							$('body').css('cursor','col-resize');
							}
						this.vresize = {h: p.height, sy: e.pageY, w: p.width, sx: e.pageX, hgo: hgo};
						
					}

				else if (dragtype=='colMove') //column header drag
					{
						$(g.nDiv).hide();$(g.nBtn).hide();
						this.hset = $(this.hDiv).offset();
						this.hset.right = this.hset.left + $('table',this.hDiv).width();
						this.hset.bottom = this.hset.top + $('table',this.hDiv).height();
						this.dcol = obj;
						this.dcoln = $('th',this.hDiv).index(obj);
						
						this.colCopy = document.createElement("div");
						this.colCopy.className = "colCopy";
						this.colCopy.innerHTML = obj.innerHTML;
						if ($.browser.msie)
						{
						this.colCopy.className = "colCopy ie";
						}
						
						
						$(this.colCopy).css({position:'absolute',float:'left',display:'none', textAlign: obj.align});
						$('body').append(this.colCopy);
						$(this.cDrag).hide();
						
					}
														
				$('body').noSelect();
			
			},
			dragMove: function (e) {
			
				if (this.colresize) //column resize
					{
						var n = this.colresize.n;
						var diff = e.pageX-this.colresize.startX;
						var nleft = this.colresize.ol + diff;
						var nw = this.colresize.ow + diff;
						if (nw > p.minwidth)
							{
								$('div:eq('+n+')',this.cDrag).css('left',nleft);
								this.colresize.nw = nw;
							}
					}
				else if (this.vresize) //table resize
					{
						var v = this.vresize;
						var y = e.pageY;
						var diff = y-v.sy;
						
						if (!p.defwidth) p.defwidth = p.width;
						
						if (p.width != 'auto' && !p.nohresize && v.hgo)
						{
							var x = e.pageX;
							var xdiff = x - v.sx;
							var newW = v.w + xdiff;
							if (newW > p.defwidth)
								{
									this.gDiv.style.width = newW + 'px';
									p.width = newW;
								}
						}
						
						var newH = v.h + diff;
						if ((newH > p.minheight || p.height < p.minheight) && !v.hgo)
							{
								this.bDiv.style.height = newH + 'px';
								p.height = newH;
								this.fixHeight(newH);
							}
						v = null;
					}
				else if (this.colCopy) {
					$(this.dcol).addClass('thMove').removeClass('thOver'); 
					if (e.pageX > this.hset.right || e.pageX < this.hset.left || e.pageY > this.hset.bottom || e.pageY < this.hset.top)
					{
						//this.dragEnd();
						$('body').css('cursor','move');
					}
					else 
					$('body').css('cursor','pointer');
					$(this.colCopy).css({top:e.pageY + 10,left:e.pageX + 20, display: 'block'});
				}													
			
			},
			dragEnd: function () {

				if (this.colresize)
					{
						var n = this.colresize.n;
						var nw = this.colresize.nw;

								$('th:visible div:eq('+n+')',this.hDiv).css('width',nw);
								$('tr',this.bDiv).each (
									function ()
										{
										$('td:visible div:eq('+n+')',this).css('width',nw);
										}
								);
								this.hDiv.scrollLeft = this.bDiv.scrollLeft;


						$('div:eq('+n+')',this.cDrag).siblings().show();
						$('.dragging',this.cDrag).removeClass('dragging');
						this.rePosDrag();
						this.fixHeight();
						this.colresize = false;
					}
				else if (this.vresize)
					{
						this.vresize = false;
					}
				else if (this.colCopy)
					{
						$(this.colCopy).remove();
						if (this.dcolt != null)
							{
							
							
							if (this.dcoln>this.dcolt)
								
								$('th:eq('+this.dcolt+')',this.hDiv).before(this.dcol);
							else
								$('th:eq('+this.dcolt+')',this.hDiv).after(this.dcol);
							
							
							
							this.switchCol(this.dcoln,this.dcolt);
							$(this.cdropleft).remove();
							$(this.cdropright).remove();
							this.rePosDrag();
							
																			
							}
						
						this.dcol = null;
						this.hset = null;
						this.dcoln = null;
						this.dcolt = null;
						this.colCopy = null;
						
						$('.thMove',this.hDiv).removeClass('thMove');
						$(this.cDrag).show();
					}										
				$('body').css('cursor','default');
				$('body').noSelect(false);
			},
			toggleCol: function(cid,visible) {
				
				var ncol = $("th[axis='col"+cid+"']",this.hDiv)[0];
				var n = $('thead th',g.hDiv).index(ncol);
				var cb = $('input[value='+cid+']',g.nDiv)[0];
				
				
				if (visible==null)
					{
						visible = ncol.hide;
					}
				
				
				if ($('input:checked',g.nDiv).length<p.minColToggle&&!visible) return false;
				
				if (visible)
					{
						ncol.hide = false;
						$(ncol).show();
						cb.checked = true;
					}
				else
					{
						ncol.hide = true;
						$(ncol).hide();
						cb.checked = false;
					}
					
						$('tbody tr',t).each
							(
								function ()
									{
										if (visible)
											$('td:eq('+n+')',this).show();
										else
											$('td:eq('+n+')',this).hide();
									}
							);							
				
				this.rePosDrag();
				
				if (p.onToggleCol) p.onToggleCol(cid,visible);
				
				return visible;
			},
			switchCol: function(cdrag,cdrop) { //switch columns
				
				$('tbody tr',t).each
					(
						function ()
							{
								if (cdrag>cdrop)
									$('td:eq('+cdrop+')',this).before($('td:eq('+cdrag+')',this));
								else
									$('td:eq('+cdrop+')',this).after($('td:eq('+cdrag+')',this));
							}
					);
					
					//switch order in nDiv
					if (cdrag>cdrop)
						$('tr:eq('+cdrop+')',this.nDiv).before($('tr:eq('+cdrag+')',this.nDiv));
					else
						$('tr:eq('+cdrop+')',this.nDiv).after($('tr:eq('+cdrag+')',this.nDiv));
						
					if ($.browser.msie&&$.browser.version<7.0) $('tr:eq('+cdrop+') input',this.nDiv)[0].checked = true;	
					
					this.hDiv.scrollLeft = this.bDiv.scrollLeft;
			},			
			scroll: function() {
					this.hDiv.scrollLeft = this.bDiv.scrollLeft;
					this.rePosDrag();
			},
			
			/**
			 * 方法名称：addData
			 * 方法描述：渲染组装flexigrid表格数据
			 * 修改历史：伊利二期为提高查询加载效率修改
			 * 修改时间：2016年5月3日 
			 * 修改人：  邢凌霄  
			 * 修改描述：原来的逻辑是通过Dom操作，将tr td生成，并添加到行列中，最后在通过检索循环触发单元格的proccess事件和行默认事件（如单击选中事件），
			 * 而且在上述两个行为中存在致使浏览器重绘Dom的操作。还存在一些不必要的循环,这样的逻辑在表格行列较多，如果客户机性能较差，又是IE8的情况下影响非常之明显。
			 * 调整为添加行列统一将生成的html push到数组一次性交付给tbody，同时在生成单元格html时处理process事件（不要再次查找和循环，避免多次重绘dom）
			 * */
			addData: function (data) { //parse data
				if (p.preProcess){
					data = p.preProcess(data);
				}
				$('.pReload',this.pDiv).removeClass('loading');
				this.loading = false;

				if (!data) 
				{
					$('.pPageStat',this.pDiv).html(p.errormsg);	
					$('.pUserText', this.pDiv).html("");
					return false;
				}
					
				if (p.dataType=='xml')
				{
					p.usertext = +$('rows usertext',data).text();
				}else{
					p.usertext = data.usertext;
				}	

				if (p.dataType=='xml'){
					p.total = +$('rows total',data).text();
				}
				else{
					p.total = data.total;
				}
					
				if (p.total==0)
				{
					$('tr, a, td, div',t).unbind();
					$(t).empty();
					p.pages = 1;
					p.page = 1;
					this.buildpager();
					$('.pPageStat',this.pDiv).html(p.nomsg);
					$('.pUserText', this.pDiv).html("");
					return false;
				}
				
				if(p.usertext) {
					g._pDiv.style.display = '';
					var _brCount = p.usertext.split("<br>").length;
					if(_brCount>1){
						$(this._pDiv).height(_brCount*22);
					}
					$('.pUserText', this._pDiv).html(p.usertext);
				}else{
					if(g._pDiv){
						g._pDiv.style.display = 'none';
					}
				}
				
				p.pages = Math.ceil(p.total/p.rp);
				
				if (p.dataType=='xml')
					p.page = +$('rows page',data).text();
				else
					p.page = data.page;
				
				this.buildpager();

				//build new body
				var tbhtml = [];
				tbhtml.push("<tbody>");
				var ths = $('thead tr:first th', g.hDiv);
				if (p.dataType=='json') 
				{
					$.each
					(
					 data.rows,
					 function(i,row) 
					 	{
					 	
							tbhtml.push("<tr")
							if (i % 2 && p.striped){
							///	tr.className = 'erow';
								tbhtml.push(" class = 'erow' ");
							}
							if(!row.validate)
							{
								
								tbhtml.push(" style='color:red' class = 'notFalsified' ");
							}
							if (row.id){
								tbhtml.push(" id='", "row", row.id, "'");
							}
							tbhtml.push(">");
							//add cell
							var a = g.hDiv;
							$(ths).each
							(
							 	function ()
									{
							
										i = row.id-1;
										var idx = $(this).attr('axis').substr(3);
										var cm = p.colModel[idx];
										var cmFirst = p.colModel[0];
										//td.align = this.align;
										tbhtml.push("<td align = '"+this.align+"'>");
										if(cmFirst.elType && cmFirst.elType == 'rownum'){
											idx = idx - 1;
										}
										if(cm.elType && cm.elType == 'checkbox')
										{
											var value = row.cell[idx]
											if(value=='')value=i;
											if(!row.validate)
											{
												tbhtml.push('<input type="checkbox" isFalsified=true class="checkbox_flex" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName + '" value="'+value  +'"  chkExcept="' + cm.chkExcept + '" />');
											}else
											{
												tbhtml.push('<input type="checkbox" class="checkbox_flex" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName + '" value="'+value  +'"  chkExcept="' + cm.chkExcept + '" />')
											}
										}
										else if(cm.elType && cm.elType == 'radio')
										{
											tbhtml.push('<input type="radio" id="'+ cm.elName +'" name="'+ cm.elName + '" value="'+ idx +'"  chkExcept="' + cm.chkExcept + '" />');
										}
										else if(cm.elType && cm.elType == 'rownum')
										{
											tbhtml.push(row.id);
										}
										else if(cm.elType && cm.elType == 'hidden')
										{
											tbhtml.push('<input type="hidden" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName + '" value="'+ row.cell[idx] +'" />');
										}
										else if(cm.elType && cm.elType == 'link')
										{
											var fArgs = row.cell[idx].split("#");//对于超链接为金额的特殊处理方式 add by xiangzhou 20120807
											if(fArgs.length == 1){
												fArgs = row.cell[idx].split(",");
											}
											if(fArgs.length > 1){
												var fArgStr = cm.methodName;
												var noLink = true;
												if(cm.elDisplay && !cm.elDisplay == '')
												{
													for(var fi=0; fi<fArgStr.length; fi++)
													{
														fArgStr = fArgStr.replace('?',fArgs[fi]);
													}
													fArgStr = '<a id="'+ cm.elName + '_' + i +'" name="'+ cm.elName + '" href=\'javascript:'+ fArgStr +'\' >'+ cm.elDisplay +'</a>';
												}
												else if(cm.isShow){//对于财务报表 添加isShow属性 根据是否有报表判断是否显示此列 add by zk 2012-12-06
													if(fArgs[0] > 0){
														for(var i=1; i<fArgs.length; i++){
															fArgStr = fArgStr.replace('?',fArgs[i]);
														}
														fArgStr = '<a id="'+ cm.elName + '_' + i +'" name="'+ cm.elName + '" href=\'javascript:'+ fArgStr +'\' >'+ cm.display +'</a>';
													}else{
														fArgStr = "&nbsp;";
													}
												}
												else
												{
													if(fArgs[0]=="") fArgs[0] = "&nbsp;"
													for(var _fi=1; _fi<fArgs.length; _fi++){
														if(fArgs[_fi] !=" "){
															noLink = false;
														}
													}
													if(noLink == true){
														fArgStr = fArgs[0];
													}else{
														for(var fi=1; fi<fArgStr.length; fi++)
														{ 
															fArgStr = fArgStr.replace('?',fArgs[fi]);
														}
														if(!row.validate)
														{
															fArgStr = '<a style="color:red" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName + '" href=\'javascript:'+ fArgStr +'\' >'+ fArgs[0] +'</a>';

														}else
														{															
															fArgStr = '<a id="'+ cm.elName + '_' + i +'" name="'+ cm.elName + '" href=\'javascript:'+ fArgStr +'\' >'+ fArgs[0] +'</a>';
														}
													}
												}
											}else{
												fArgStr = fArgs[0];
											}
											tbhtml.push(fArgStr);
										}
										else if(cm.elType && cm.elType == 'textEdit')
										{
											var fArgStr = '<input type="text" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName +'" value="'+ row.cell[idx] +'" '+ cm.elAttr  +' />';
											tbhtml.push(fArgStr);
										}
										else if(cm.elType && cm.elType == 'amountEdit')
										{
											var fArgStr = '<input type="text" class="amountEdit_flex" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName +'" value=" '+ row.cell[idx] +' " '+  cm.elAttr +' />';
											tbhtml.push(fArgStr);
										}
										else if(cm.elType && cm.elType == 'datepickerEdit')
										{
											var fArgStr = '<input type="text" class="datepickerEdit_flex" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName +'" value=" '+ row.cell[idx] +' " '+  cm.elAttr +' />';
											tbhtml.push(fArgStr);
										}
										else if(cm.elType && cm.elType == 'button')
										{
											var fArgs = row.cell[idx].split(",");
											var fArgStr = cm.methodName;
											
											for(var fi=0; fi<fArgStr.length; fi++)
											{
												fArgStr = fArgStr.replace('?',fArgs[fi]);
											}

											if(cm.elDisplay && !cm.elDisplay == '')
											{
												fArgStr = '<input type="button" class="button" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName +'" value=" '+ cm.elDisplay +' " onclick=\''+ fArgStr +'\'  />';
											}
											else
											{
												fArgStr = '<input type="button" class="button" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName +'" value=" '+ cm.display +' " onclick=\''+ fArgStr +'\'  />';
											}
											tbhtml.push(fArgStr);
										}
										else if(cm.elType && cm.elType == 'textareaEdit')
										{
											var fArgStr = '<textarea id="'+ cm.elName + '_' + i +'" name="'+ cm.elName +'" '+ cm.elAttr  +' >' + cm.display + '</textarea>';
											tbhtml.push(fArgStr);
										}
										else if(cm.elType && cm.elType == 'selectEdit')
										{
											//结构:1:安莉,2:张三,3王五
											var optionStr = "";
											var options = row.cell[idx].split(",");
											$.each(options, function(){
												optionStr += "<option value=\"" + this.split(":")[0] + "\">"+ this.split(":")[1] + "</option>";
											});
											var fArgStr = '<select id="'+ cm.elName + '_' + i +'" name="'+ cm.elName +'" '+ cm.elAttr  +' >' + optionStr + '</select>';
											$(td).append(fArgStr);
											tbhtml.push(fArgStr);
										}
										else if(cm.elType && cm.elType == 'suggestEdit')
										{
											//suggestMethod结构:suggestMethod:function(p){p.suggest({...});}
											var fArgStr = '<input type="text" id="'+ cm.elName + '_' + i +'" name="'+ cm.elName +'" value=" '+ cm.display +' " ' + '" suggestMethod=" '+ cm.suggestMethod +' " '+ cm.elAttr  +' />';
											tbhtml.push(fArgStr);
										}
										else if(cm.elType && cm.elType == 'convertValue')
										{
											//执行elAttr指定的方法，对数据进行格式化
											//alert( row.cell[idx]);
											var fArgStr = eval(cm.elAttr);
											tbhtml.push(fArgStr);
										}
										else
										{
											tbhtml.push(row.cell[idx]);
										}
										
											
										tbhtml.push("</td>");
										td = null;
									}
							); 
							
							
							if ($('thead',this.gDiv).length<1) //handle if grid has no headers
							{
								for (idx=0;idx<cell.length;idx++)
								{
									tbhtml.push("<td>")
									
									var tdInner = row.cell[idx] || "&nbsp;";
									td = null;
									tbhtml.push(tdInner);
									tbhtml.push("</td>");
								}
							}							
							
							tr = null;
							tbhtml.push("</tr>");
							//alert(tbhtml);
							
						}
					);				
					
				} else if (p.dataType=='xml') {

				i = 1;

				$("rows row",data).each
				(
				 
				 	function ()
						{
							
							i++;
							
							var tr = document.createElement('tr');
							if (i % 2 && p.striped) tr.className = 'erow';

							var nid =$(this).attr('id');
							if (nid) tr.id = 'row' + nid;
							
							nid = null;
							
							var robj = this;

							
							
							$('thead tr:first th',g.hDiv).each
							(
							 	function ()
								{
									
									var td = document.createElement('td');
									var idx = $(this).attr('axis').substr(3);
									td.align = this.align;
									td.innerHTML = $("cell:eq("+ idx +")",robj).text();
									$(tr).append(td);
									td = null;
								}
							);
							
							
							if ($('thead',this.gDiv).length<1) //handle if grid has no headers
							{
								$('cell',this).each
								(
								 	function ()
									{
									var td = document.createElement('td');
									td.innerHTML = $(this).text();
									$(tr).append(td);
									td = null;
									}
								);
							}
							
							$(tbody).append(tr);
							tr = null;
							robj = null;
						}
				);
				
				}

				$('tr',t).unbind();
				$(t).empty();
				
				tbhtml.push("</tbody>");
				//alert(tbhtml);
				$(t).html(tbhtml.join(""));
				this.addCellProp();
				this.addRowProp();
				
				//this.fixHeight($(this.bDiv).height());
				
				this.rePosDrag();
				
				tbody = null; data = null; i = null; 
				
				if (p.onSuccess) p.onSuccess();
				if (p.hideOnSubmit) $(g.block).remove();//$(t).show();
				
				this.hDiv.scrollLeft = this.bDiv.scrollLeft;
				if ($.browser.opera) $(t).css('visibility','visible');
				
				//eval custom function
				if(typeof(p.callback) == "function"){
					p.callback(data);	
				}
				else if(p.callback && p.callback.length > 0) eval(p.callback);
				
			},
			changeSort: function(th) { //change sortorder
			
				if (this.loading) return true;
				
				$(g.nDiv).hide();$(g.nBtn).hide();
				
				if (p.sortname == $(th).attr('abbr'))
					{
						if (p.sortorder=='asc') p.sortorder = 'desc'; 
						else p.sortorder = 'asc';						
					}
				
				$(th).addClass('sorted').siblings().removeClass('sorted');
				$('.sdesc',this.hDiv).removeClass('sdesc');
				$('.sasc',this.hDiv).removeClass('sasc');
				$('div',th).addClass('s'+p.sortorder);
				p.sortname= $(th).attr('abbr');
				
				if (p.onChangeSort)
					p.onChangeSort(p.sortname,p.sortorder);
				else
					this.populate();				
			
			},
			buildpager: function(){ //rebuild pager based on new properties
			
			$('.pcontrol input',this.pDiv).val(p.page);
			$('.pcontrol span',this.pDiv).html(p.pages);
			
			var r1 = (p.page-1) * p.rp + 1; 
			var r2 = r1 + p.rp - 1; 
			
			if (p.total<r2) r2 = p.total;
			
			var stat = p.pagestat;
			
			stat = stat.replace(/{from}/,r1);
			stat = stat.replace(/{to}/,r2);
			stat = stat.replace(/{total}/,p.total);
			
			$('.pPageStat',this.pDiv).html(stat);
			
			//新增slider翻页方式  add by jiamiao 20090914
			if(p.slider) {
				var pageCount;
				$('.pPageSliderCount',this.pDiv).html(p.page);
				$('.pPageSlider',this.pDiv).slider({
					orientation: "horizontal",//If the orientation is not correctly detected you can set this option to 'horizontal' or 'vertical'.
					range: "max",
					min: 1,
					max : (function(total, rp) {
									pageCount = Math.round(total / rp);
									if (total % rp > 0)
										pageCount++;
									if (pageCount == 0)
										pageCount = 1;
									return pageCount;
								})(p.total, p.rp),
					value: p.page,
					slide: function(event, ui) {
						$('.pPageSliderCount',g.pDiv).html(ui.value);
						g.changePage('slider');
					}
				}).css("width", pageCount > 100 ? "400px" : "200px");
				}
			},
			populate: function () { //get latest data
				if (this.loading) return true;

				
				if (p.onSubmit)
				{
					var gh = p.onSubmit();
					if (!gh) return false;
				}

				this.loading = true;
				if (!p.url) return false;
				
				$('.pPageStat',this.pDiv).html(p.procmsg);
				$('.pUserText', this.pDiv).html("");
				
				$('.pReload',this.pDiv).addClass('loading');
				
				$(g.block).css({top:g.bDiv.offsetTop});
				
				if (p.hideOnSubmit) $(this.gDiv).prepend(g.block); //$(t).hide();
				
				if ($.browser.opera) $(t).css('visibility','hidden');
				
				if (!p.newp) p.newp = 1;
				
				if (p.page>p.pages) p.page = p.pages;
				//var param = {page:p.newp, rp: p.rp, sortname: p.sortname, sortorder: p.sortorder, query: p.query, qtype: p.qtype};
				
				//20090903 add by miaojia
				if(p.isEncode && p.isEncode == true) {
					p.query = $('input[name=q]', g.tDiv).val();
					p.qtype = $('select[name=qtype]', g.tDiv).val();
					p.query = encodeURIComponent(p.query);
					p.qtype = encodeURIComponent(p.qtype);
				}
				
				var param = [
					 { name : 'page', value : p.newp }
					,{ name : 'rp', value : p.rp }
					,{ name : 'sortname', value : p.sortname}
					,{ name : 'sortorder', value : p.sortorder }
					,{ name : 'query', value : p.query}
					,{ name : 'qtype', value : p.qtype}
				];
				
				
				//add by jiamiao 2009-08-10
				var formObj = $($.getThisFormObj(this));
				formObj.prepend($.createHideObject({ name : 'flexigrid_page', value : p.newp }));
				formObj.prepend($.createHideObject({ name : 'flexigrid_rp', value : p.rp }));
				formObj.prepend($.createHideObject({ name : 'flexigrid_sortname', value : p.sortname}));
				formObj.prepend($.createHideObject({ name : 'flexigrid_sortorder', value : p.sortorder }));
				formObj.prepend($.createHideObject({ name : 'flexigrid_query', value : p.query}));
				formObj.prepend($.createHideObject({ name : 'flexigrid_qtype', value : p.qtype}));
				if(p.tag||!p.paramCache){//如果走的是下页，则不进去组织数据，用以前数据 oldParm
					if (p.params)
						{
							for (var pi = 0; pi < p.params.length; pi++) param[param.length] = p.params[pi];
							p.oldParm=param;
						}
				}else{
					p.oldParm[0].value = p.newp;
					p.oldParm[1].value =p.rp;
					param=p.oldParm;
					p.tag=true;
				}
				$(".hDivBox > table > thead > tr > th", this.hDiv).data("p", p);	
				showSending();
				$.ajax({
					   type: p.method,
					   url: p.url,
				  	   data: param,
					   dataType: p.dataType,
					   success: function(data){
					   		if(data==null){
					   		hideSending();
						   		return;
					   		}
							if(data.rows.length==1&&data.page==1&&p.isSuggest == true)
					   		{
					   			//g.addData(data);
					   			var cell = data.rows[0].cell[0];
					   			var params = cell.substring(cell.indexOf("$.suggest.fillForm({")+19,cell.lastIndexOf("})\">")+1);
					   			params = eval("("+params+")");
					   			$.suggest.fillForm(params);
					   			$("#"+p.panelname+"_panel").dialog("close");
					   		}
					   		else
					   		{
								g.addData(data);
							}
							hideSending();
							
						},
					   error: function(data) { 
					   	   alert(data.responseText);
						   hideSending();
						   $(".gBlock").remove(	);
						   try { if (p.onError) p.onError(data); } catch (e) {} }
					 });
				
			},
			ajaxSubmit : function() {
					p.onSubmit();
					var param = [
						 { name : 'page', value : p.newp }
						,{ name : 'rp', value : p.rp }
						,{ name : 'sortname', value : p.sortname}
						,{ name : 'sortorder', value : p.sortorder }
						,{ name : 'query', value : p.query}
						,{ name : 'qtype', value : p.qtype}
					];
					if(p.tag||!p.paramCache){//如果走的是下页，则不进去组织数据，用以前数据 oldParm
						if (p.params)
						{
							for (var pi = 0; pi < p.params.length; pi++) param[param.length] = p.params[pi];
							p.oldParm=param;
						}
					}
					else{
						p.oldParm[0].value = p.newp;
						p.oldParm[1].value =p.rp;
						param=p.oldParm;
						p.tag=true;
					}
					showSending();
					$.ajax({
							   type: p.method,
							   url: p.url,
						  	   data: param,
							   dataType: p.dataType,
							   success: function(data){
							   	   hideSending();
							   	   if(p.submitSuccessFunc==''){
							   	   		alert('操作成功');
							   	   }
    						   	   else if(typeof(p.submitSuccessFunc) == "function"){
										p.submitSuccessFunc(data);	
							   	   }else if(p.submitSuccessFunc&&p.submitSuccessFunc.length>0){
							   	   		eval(p.submitSuccessFunc+"('"+data+"')");
							   	   }
							   },
							   error: function(data) {
								   hideSending();
								   if(p.submitErrorFunc==''){
								   	 	alert(data.responseText);
								   }
								   else if(typeof(p.submitErrorFunc) == "function"){
										p.submitErrorFunc(data);	
								   }
								   else if(p.submitErrorFunc&&p.submitErrorFunc.length>0){
								   	   	eval(p.submitErrorFunc+"('"+data+"')");
								   }
							  }
					});
				
			},
			doSearch: function () {
				p.query = $('input[name=q]', g.tDiv).val();
				p.qtype = $('select[name=qtype]', g.tDiv).val();
				p.newp = 1;

				this.populate();				
			},
			changePage: function (ctype){ //change page
				
				if (this.loading) return true;
			
				switch(ctype)
				{
					
					case 'first': 
							p.newp = 1;
							p.tag=false;
							if(p.slider) 
								$('.pPageSlider',g.pDiv).slider("option", "value", p.newp); 
							break;
					case 'prev': 
							p.tag=false;
							if (p.page>1) 
								p.newp = parseInt(p.page) - 1; 
							if(p.slider) 
							$('.pPageSlider',g.pDiv).slider("option", "value", p.newp); 
							break;
					case 'next': 
							p.tag=false;
							if (p.page<p.pages) 
								p.newp = parseInt(p.page) + 1; 
							if(p.slider) 
								$('.pPageSlider',g.pDiv).slider("option", "value", p.newp); 
							break;
					case 'last': 
							p.tag=false;
							p.newp = p.pages; 
							if(p.slider) 
								$('.pPageSlider',g.pDiv).slider("option", "value", p.newp); 
							break;
					case 'input': 
							p.tag=false;
							var nv = parseInt($('.pcontrol input',this.pDiv).val());
							if (isNaN(nv)) nv = 1;
						if (nv<1) nv = 1;
							else if (nv > p.pages) nv = p.pages;
							$('.pcontrol input',this.pDiv).val(nv);
						p.newp = nv;
							if(p.slider) $('.pPageSlider',g.pDiv).slider("option", "value", p.newp); 
							break;
					//新增对slider的支持。add by jiamiao 20090914
					case 'slider': p.newp = $('.pPageSliderCount',g.pDiv).text();	p.tag=false; break;
				}
			
				if (p.newp==p.page) return false;
				
				if (p.onChangePage) 
					p.onChangePage(p.newp);
				else				
					this.populate();
			
			},
			addCellProp: function ()
			{
					$('tbody tr td',g.bDiv).each
					(
						function ()
						{
								var tdDiv = document.createElement('div');
								var n = $('td',$(this).parent()).index(this);
								var pth = $('th:eq('+n+')',g.hDiv).get(0);
		
								if (pth!=null)
								{
								if (p.sortname==$(pth).attr('abbr')&&p.sortname) 
									{
									this.className = 'sorted';
									}
								 $(tdDiv).css({textAlign:pth.align,width: $('div:first',pth)[0].style.width});
								 
								 if (pth.hide) $(this).css('display','none');
								 
								 }
								 
								 if (p.nowrap==false) $(tdDiv).css('white-space','normal');
								 
								 if (this.innerHTML=='') this.innerHTML = '&nbsp;';
								 
								 //tdDiv.value = this.innerHTML; //store preprocess value
								 tdDiv.innerHTML = this.innerHTML;
								 
								 var prnt = $(this).parent()[0];
								 var pid = false;
								 if (prnt.id) pid = prnt.id.substr(3);
								 
								 if (pth!=null)
								 {
								 if (pth.process) pth.process(tdDiv,pid);
								 }
								 
								$(this).empty().append(tdDiv).removeAttr('width'); //wrap content

								//add editable event here 'dblclick'

						}
					);
					
			},
			/**
			 * 方法名称：getCheckedIDs
			 * 方法描述：获取选中行ID数组
			 * 创建时间：2016年5月18日 
			 * 创建人：  邢凌霄  
			 * 创建描述：
			 * 1、调用方法：$("#flexlist").getCheckedIDs(); 
			 * 2、返回值样例：{value1,value2,value3}
			 * 3、取值说明：value1,value2即为ID数组
			 * */
	         getCheckedIDs: function() {
                var ids = [];
                $(":checkbox:checked", g.bDiv).each(function() {
                    ids.push($(this).val());
                });
                return ids;
            },
        	/**
			 * 方法名称：getCheckedRows
			 * 方法描述：获取选中行数据数组
			 * 创建时间：2016年5月18日 
			 * 创建人：  邢凌霄  
			 * 创建描述：
			 * 1、调用方法：$("#flexlist").getCheckedRows();
			 * 2、返回值样例：{map1,map2,map3},
			 * 3、取值说明：key即为colModel中的name名称，value即为显示的值
			 * 代码调用示例：$.each($("#flexlist").getCheckedRows(),function(i,value){
			 * 					console.log(value["key"]);
			 * 				});
			 * */
			getCheckedRows: function(){
				var rtnList = new Array();
				var objRows = $('.trSelected', $(t));
				if(objRows!=null){
					for(var i=0;i<objRows.length;i++){
						 var row = objRows[i];
						 var rowMap = new Object();
						 for(var j=0;j<row.cells.length;j++){
					       var cellName = p.colModel[j].name;
					       var value = row.cells[j].innerText;
					       value=value.replace(/^\n+|\n+$/g,"");
					       var s = "rowMap." + cellName + '="'+ value + '"';
					       eval(s); 
				 	   }
					   rtnList[rtnList.length] = rowMap;
				  }
				}
				return rtnList;
			},
			getCellDim: function (obj) // get cell prop for editable event
			{
				var ht = parseInt($(obj).height());
				var pht = parseInt($(obj).parent().height());
				var wt = parseInt(obj.style.width);
				var pwt = parseInt($(obj).parent().width());
				var top = obj.offsetParent.offsetTop;
				var left = obj.offsetParent.offsetLeft;
				var pdl = parseInt($(obj).css('paddingLeft'));
				var pdt = parseInt($(obj).css('paddingTop'));
				return {ht:ht,wt:wt,top:top,left:left,pdl:pdl, pdt:pdt, pht:pht, pwt: pwt};
			},
			addRowProp: function()
			{
					$('tbody tr',g.bDiv).each
					(
						function (i,n)
						{
						if(p.isSuggest == true && i==0) {
							$(this).toggleClass('trSelected'); 
							$(this).find("td:first > div:first").focus();
						}
							
						$(this)
						.click(
							function (e) 
							{ 
								//var obj = (e.target || e.srcElement); if (obj.href || obj.type) return true;
								//$(this).toggleClass('trSelected');
								//if (p.singleSelect) $(this).siblings().removeClass('trSelected');
								
								var obj = e.srcElement;
								if(obj.type != 'checkbox') return true;
								$(this).toggleClass('trSelected');
								if (p.singleSelect) $(this).siblings().removeClass('trSelected');
							}
						)
						.dblclick(
							function ()
							{
								if(p.isSuggest == true) {
									var href = $(this).find("td:first > div:first > a:first").attr("href");
									if(href.indexOf("javascript:") == 0)
										href = href.substring("javascript:".length,href.length);
									eval(href);
								}
							}
						)
						.keydown(
							function(e)
							{
								if(p.isSuggest == true) {
									var trSelected = $('.trSelected',g.bDiv);
									if(e.keyCode==38 && $(trSelected).prev().length > 0) {
										$(trSelected).removeClass('trSelected');
										$(trSelected).prev().toggleClass('trSelected'); 
										$(trSelected).prev().focus();
									}
									else if(e.keyCode==40 && $(trSelected).next().length > 0) {
										$(trSelected).removeClass('trSelected');
										$(trSelected).next().toggleClass('trSelected'); 
										$(trSelected).next().focus();
									}
									else if(e.keyCode==13) {
										var href = $(trSelected).find("td:first > div:first > a:first").attr("href");
										if(href.indexOf("javascript:") == 0)
											href = href.substring("javascript:".length,href.length);
										eval(href);
									}
								}
							}
						).mousedown(
							function (e)
							{
								if(p.isSuggest == true) {
									$(this).siblings().removeClass('trSelected');
									$(this).toggleClass('trSelected'); 
									g.multisel = true; 
									this.focus();
									$(g.gDiv).noSelect();
								}
							}
						)
						.mouseup(
							function ()
							{
								if(p.isSuggest == true) {
									if (g.multisel)
									{
									$(this).siblings().removeClass('trSelected');
									g.multisel = false;
									$(g.gDiv).noSelect(false);
									}
								}
							}
						)
						.hover(
							function (e) 
							{ 
							if (g.multisel) 
								{
								$(this).toggleClass('trSelected'); 
								}
							},
							function () {}						
						)
						;
						
						if ($.browser.msie&&$.browser.version<7.0)
							{
								$(this)
								.hover(
									function () { $(this).addClass('trOver'); },
									function () { $(this).removeClass('trOver'); }
								)
								;
							}
						}
					);
					
					
			},
			pager: 0
			};		
		
		//create model if any
		if (p.colModel)
		{
			thead = document.createElement('thead');
			tr = document.createElement('tr');
			
			
			for (i=0;i<p.colModel.length;i++)
				{
					var cm = p.colModel[i];
					var th = document.createElement('th');
					
					if(cm.elType && cm.elType == 'checkbox')
					{
						$(th).append('<input type="checkbox" class="checkbox_flex" id="'+ cm.elName +'_all" name="'+ cm.elName +'_all" />');
						
						if (cm.name&&cm.sortable)
							$(th).attr('abbr',cm.name);
						
						th.idx = i;
						$(th).attr('axis','col'+i);
						
						if (cm.align)
							th.align = cm.align;
							
						if (cm.width) 
							$(th).attr('width',cm.width);
	
						if (cm.hide)
							{
							th.hide = true;
							}
							
						if (cm.process)
							{
								th.process = cm.process;
							}
					}
					else if(cm.elType && cm.elType == 'rownum')
					{
						$(th).append('<input type="hidden" value="'+ cm.display +'"/>' + cm.display);
						
						if (cm.name&&cm.sortable)
							$(th).attr('abbr',cm.name);
						
						th.idx = i;
						$(th).attr('axis','col'+i);
						
						if (cm.align)
							th.align = cm.align;
							
						if (cm.width) 
							$(th).attr('width',cm.width);
	
						if (cm.hide)
							{
							th.hide = true;
							}
							
						if (cm.process)
							{
								th.process = cm.process;
							}
					}
					else if(cm.elType && cm.elType == 'hidden')
					{
						$(th).append('<input type="hidden" id="'+ cm.elName +'_all" />');
						
						if (cm.name&&cm.sortable)
							$(th).attr('abbr',cm.name);
						
						th.idx = i;
						$(th).attr('axis','col'+i);
						
						if (cm.align)
							th.align = cm.align;
							
						if (cm.width) 
							$(th).attr('width',cm.width);
	
						//if (cm.hide)
						//	{
						//	th.hide = true;
						//	}
						th.hide = true;
							
						if (cm.process)
							{
								th.process = cm.process;
							}
					}
					else
					{
						th.innerHTML = cm.display;
						
						if (cm.name&&cm.sortable)
							$(th).attr('abbr',cm.name);
						
						th.idx = i;
						$(th).attr('axis','col'+i);
						
						if (cm.align)
							th.align = cm.align;
							
						if (cm.width) 
							$(th).attr('width',cm.width);
	
						if (cm.hide)
							{
							th.hide = true;
							}
							
						if (cm.process)
							{
								th.process = cm.process;
							}
					}
					$(th).data('cm',cm).data('p', p);
					$(tr).append(th);
				}
			$(thead).append(tr);
			$(t).prepend(thead);
		} // end if p.colmodel	

		//init divs
		g.gDiv = document.createElement('div'); //create global container
		g.mDiv = document.createElement('div'); //create title container
		g.hDiv = document.createElement('div'); //create header container
		g.bDiv = document.createElement('div'); //create body container
		g.vDiv = document.createElement('div'); //create grip
		g.rDiv = document.createElement('div'); //create horizontal resizer
		g.cDrag = document.createElement('div'); //create column drag
		g.block = document.createElement('div'); //creat blocker
		g.nDiv = document.createElement('div'); //create column show/hide popup
		g.nBtn = document.createElement('div'); //create column show/hide button
		g.iDiv = document.createElement('div'); //create editable layer
		g.tDiv = document.createElement('div'); //create toolbar
		
		if (p.usepager){ 
			g.pDiv = document.createElement('div'); //create pager container
		}
		g.hTable = document.createElement('table');

		//set gDiv
		g.gDiv.className = 'flexigrid';
		if (p.width!='auto') g.gDiv.style.width = p.width + 'px';

		//add conditional classes
		if ($.browser.msie)
			$(g.gDiv).addClass('ie');
		
		if (p.novstripe)
			$(g.gDiv).addClass('novstripe');

		$(t).before(g.gDiv);
		$(g.gDiv)
		.append(t)
		;

		//set toolbar or set search button
		if (p.buttons || p.searchitems) 
		{
			g.tDiv.className = 'tDiv';
			g.tDiv.innerHTML = '<div class="tDiv2"></div>';
			
			if(p.buttons)
			{
				for (i=0;i<p.buttons.length;i++)
					{
						var btn = p.buttons[i];
						if (!btn.separator)
						{
							var btnGroup = document.createElement('div');
							btnGroup.className = 'pGroup';
							
							var btnDiv = document.createElement('div');
							btnDiv.className = 'fbutton';
							btnDiv.innerHTML = "<div><span>"+btn.name+"</span></div>";
							
							if (btn.bclass) 
								$('span', btnDiv).addClass(btn.bclass).css({paddingLeft:20});
							btnDiv.onpress = btn.onpress;
							btnDiv.name = btn.name;
							if (btn.onpress)
							{
								$(btnDiv).click
								(	
									function () 
									{
									this.onpress(this.name,g.gDiv);
									}
								);
							}
							
							$(btnGroup).append(btnDiv);
							$('div[class=tDiv2]', g.tDiv).append(btnGroup);
							
							if ($.browser.msie&&$.browser.version<7.0)
							{
								$(btnDiv).hover(function(){$(this).addClass('fbOver');},function(){$(this).removeClass('fbOver');});
							}
							
						} else {
							$('div[class=tDiv2]', g.tDiv).append("<div class='btnseparator'></div>");
						}
					}
				}
			
				//add search button
				if (p.searchitems)
					{
						var sitems = p.searchitems;
						
						var soptHtml = '';
						var sel = '';
						for (var s = 0; s < sitems.length; s++)
						{
							if (p.qtype=='' && sitems[s].isdefault==true)
							{
								p.qtype = sitems[s].name;
								sel = 'selected="selected"';
							}
							soptHtml += "<option value='" + sitems[s].name + "' " + sel + " >" + sitems[s].display + "&nbsp;&nbsp;</option>";						
						}
						
						if (p.qtype=='') p.qtype = sitems[0].name;
						
						$('div[class=tDiv2]', g.tDiv).append('<div class="pGroup">查询： <input type="text" size="30" name="q" /></div><div class="btnseparator"></div><div class="pGroup"><span class="pcontrol"><select name="qtype">'+soptHtml+'</select>&nbsp;&nbsp;<input name="butSuggestQuery" type="button" class="button" value=" 确 认 " /></span></div><div class="btnseparator"></div>');
	
						$('input[name=q]', g.tDiv).keydown(function(e){if(e.keyCode==13) g.doSearch()});
						$('input[name=butSuggestQuery]', g.tDiv).click(function(){
							g.doSearch();
						});
					}
			
			
				$(g.tDiv).append("<div style='clear:both'></div>");
				$(g.gDiv).prepend(g.tDiv);
		}
		
		//set hDiv
		g.hDiv.className = 'hDiv';

		$(t).before(g.hDiv);

		//set hTable
			g.hTable.cellPadding = 0;
			g.hTable.cellSpacing = 0;
			$(g.hDiv).append('<div class="hDivBox"></div>');
			$('div',g.hDiv).append(g.hTable);
			var thead = $("thead:first",t).get(0);
			if (thead) $(g.hTable).append(thead);
			thead = null;
		
		if (!p.colmodel) var ci = 0;

		//setup thead			
			$('thead tr:first th',g.hDiv).each
			(
			 	function ()
				{
					var thdiv = document.createElement('div');
					var cm;
					if(p && p.colModel && p.colModel.length > 0)
						cm = p.colModel[ci];
				
					if ($(this).attr('abbr'))
						{
						$(this).click(
							function (e) 
								{
									
									if (!$(this).hasClass('thOver')) return false;
									var obj = (e.target || e.srcElement);
									if (obj.href || obj.type) return true; 
									g.changeSort(this);
								}
						)
						;
						
						if ($(this).attr('abbr')==p.sortname)
							{
							this.className = 'sorted';
							thdiv.className = 's'+p.sortorder;
							}
						}
						
						if (this.hide) $(this).hide();
						
						if (!p.colmodel)
						{
							$(this).attr('axis','col' + ci++);
						}
						
						
					 $(thdiv).css({textAlign:this.align, width: this.width + 'px'});
					 thdiv.innerHTML = this.innerHTML;
					
					if(cm && cm.elType && cm.elType == 'checkbox')
						{
							$(this).empty().append(thdiv).removeAttr('width');
							$(thdiv)
							.mousedown(function (e) 
								{
									if($('input', thdiv).attr('checked'))
										{
											$('input', thdiv).attr('checked','');
											
											$(t).find('input').each(
												function (){
													
													if($(this).attr('name') == cm.elName && $(this).attr('checked'))
													{
														//if(($(this).attr('chkExcept') == "true" && !$(this).attr('disabled')) || $(this).attr('chkExcept') != "true"){
														if(!$(this).attr('disabled')){
														var objTr = $(this).parent().parent().parent();
														$(objTr).siblings().removeClass('trSelected');
														
														$(this).attr('checked', '');
														}
													}
												}
											);
										}
									else
										{
											$('input', thdiv).attr('checked','true');
											
											$(t).find('input').each(
												function (){
													if($(this).attr('name') == cm.elName && !$(this).attr('checked'))
													{
														//if(($(this).attr('chkExcept') == "true" && !$(this).attr('disabled')) || $(this).attr('chkExcept') != "true"){
														if(!$(this).attr('disabled')){
														var objTr = $(this).parent().parent().parent();
														$(objTr).toggleClass('trSelected');

														$(this).attr('checked', 'true');
														}
													}
												}
											);
										}
									
								});
						}
					else
						{
							var lock = $(this).hasClass("lockme") || !p.colDrag;
							$(this).empty().append(thdiv).removeAttr('width')
							.mousedown(function (e) 
								{
									if(!lock) {
										g.dragStart('colMove',e,this);
									}
								})
							.hover(
								function(){
									if (!g.colresize&&!$(this).hasClass('thMove')&&!g.colCopy) $(this).addClass('thOver');
									
									if ($(this).attr('abbr')!=p.sortname&&!g.colCopy&&!g.colresize&&$(this).attr('abbr')) $('div',this).addClass('s'+p.sortorder);
									else if ($(this).attr('abbr')==p.sortname&&!g.colCopy&&!g.colresize&&$(this).attr('abbr'))
										{
											var no = '';
											if (p.sortorder=='asc') no = 'desc';
											else no = 'asc';
											$('div',this).removeClass('s'+p.sortorder).addClass('s'+no);
										}
									
									if (g.colCopy) 
										{
										var n = $('th',g.hDiv).index(this);
										
										if (n==g.dcoln) return false;
										
										
										
										if (n<g.dcoln) $(this).append(g.cdropleft);
										else $(this).append(g.cdropright);
										
										g.dcolt = n;
										
										} else if (!g.colresize) {
											
										var nv = $('th:visible',g.hDiv).index(this);
										var onl = parseInt($('div:eq('+nv+')',g.cDrag).css('left'));
										var nw = parseInt($(g.nBtn).width()) + parseInt($(g.nBtn).css('borderLeftWidth'));
										nl = onl - nw + Math.floor(p.cgwidth/2);
										
										$(g.nDiv).hide();$(g.nBtn).hide();
										
										$(g.nBtn).css({'left':nl,top:g.hDiv.offsetTop}).show();
										
										var ndw = parseInt($(g.nDiv).width());
										
										$(g.nDiv).css({top:g.bDiv.offsetTop});
										
										if ((nl+ndw)>$(g.gDiv).width())
											$(g.nDiv).css('left',onl-ndw+1);
										else
											$(g.nDiv).css('left',nl);
											
										if ($(this).hasClass('sorted')) 
											$(g.nBtn).addClass('srtd');
										else
											$(g.nBtn).removeClass('srtd');
											
										}
										
								},
								function(){
									$(this).removeClass('thOver');
									if ($(this).attr('abbr')!=p.sortname) $('div',this).removeClass('s'+p.sortorder);
									else if ($(this).attr('abbr')==p.sortname)
										{
											var no = '';
											if (p.sortorder=='asc') no = 'desc';
											else no = 'asc';
											
											$('div',this).addClass('s'+p.sortorder).removeClass('s'+no);
										}
									if (g.colCopy) 
										{								
										$(g.cdropleft).remove();
										$(g.cdropright).remove();
										g.dcolt = null;
										}
								})
							; //wrap content
						}
				}
			);

		//set bDiv
		g.bDiv.className = 'bDiv';
		$(t).before(g.bDiv);
		$(g.bDiv)
		.css({ height: (p.height=='auto') ? 'auto' : p.height+"px"})
		.scroll(function (e) {g.scroll()})
		.append(t)
		;
		
		if (p.height == 'auto') 
		{
		$('table',g.bDiv).addClass('autoht');
		}


		//add td properties
		g.addCellProp();
		
		//add row properties
		g.addRowProp();
		
		//set cDrag
		
		var cdcol = $('thead tr:first th:first',g.hDiv).get(0);
		
		if (cdcol != null)
		{		
		g.cDrag.className = 'cDrag';
		g.cdpad = 0;
		
		g.cdpad += (isNaN(parseInt($('div',cdcol).css('borderLeftWidth'))) ? 0 : parseInt($('div',cdcol).css('borderLeftWidth'))); 
		g.cdpad += (isNaN(parseInt($('div',cdcol).css('borderRightWidth'))) ? 0 : parseInt($('div',cdcol).css('borderRightWidth'))); 
		g.cdpad += (isNaN(parseInt($('div',cdcol).css('paddingLeft'))) ? 0 : parseInt($('div',cdcol).css('paddingLeft'))); 
		g.cdpad += (isNaN(parseInt($('div',cdcol).css('paddingRight'))) ? 0 : parseInt($('div',cdcol).css('paddingRight'))); 
		g.cdpad += (isNaN(parseInt($(cdcol).css('borderLeftWidth'))) ? 0 : parseInt($(cdcol).css('borderLeftWidth'))); 
		g.cdpad += (isNaN(parseInt($(cdcol).css('borderRightWidth'))) ? 0 : parseInt($(cdcol).css('borderRightWidth'))); 
		g.cdpad += (isNaN(parseInt($(cdcol).css('paddingLeft'))) ? 0 : parseInt($(cdcol).css('paddingLeft'))); 
		g.cdpad += (isNaN(parseInt($(cdcol).css('paddingRight'))) ? 0 : parseInt($(cdcol).css('paddingRight'))); 

		$(g.bDiv).before(g.cDrag);
		
		var cdheight = $(g.bDiv).height();
		var hdheight = $(g.hDiv).height();
		
		$(g.cDrag).css({top: -hdheight + 'px'});
		
		$('thead tr:first th',g.hDiv).each
		(
		 	function ()
			{
				var cgDiv = document.createElement('div');
				$(g.cDrag).append(cgDiv);
				if (!p.cgwidth) p.cgwidth = $(cgDiv).width();
				$(cgDiv).css({height: cdheight + hdheight})
				.mousedown(function(e){g.dragStart('colresize',e,this);})
				;
				if ($.browser.msie&&$.browser.version<7.0)
				{
					g.fixHeight($(g.gDiv).height());
					$(cgDiv).hover(
						function () 
						{
						g.fixHeight();
						$(this).addClass('dragging') 
						},
						function () { if (!g.colresize) $(this).removeClass('dragging') }
					);
				}
			}
		);
		
		//g.rePosDrag();
							
		}
		

		//add strip		
		if (p.striped) 
			$('tbody tr:odd',g.bDiv).addClass('erow');
			
			
		if (p.resizable && p.height !='auto') 
		{
		g.vDiv.className = 'vGrip';
		$(g.vDiv)
		.mousedown(function (e) { g.dragStart('vresize',e)})
		.html('<span></span>');
		$(g.bDiv).after(g.vDiv);
		}
		
		if (p.resizable && p.width !='auto' && !p.nohresize) 
		{
		g.rDiv.className = 'hGrip';
		$(g.rDiv)
		.mousedown(function (e) {g.dragStart('vresize',e,true);})
		.html('<span></span>')
		.css('height',$(g.gDiv).height())
		;
		if ($.browser.msie&&$.browser.version<7.0)
		{
			$(g.rDiv).hover(function(){$(this).addClass('hgOver');},function(){$(this).removeClass('hgOver');});
		}
		$(g.gDiv).append(g.rDiv);
		}

		
		// add pager
		if (p.usepager)
		{
			g.pDiv.className = 'pDiv';
			g.pDiv.innerHTML = '<div class="pDiv2"></div>';
			$(g.bDiv).after(g.pDiv);
			var html = ' <div class="pGroup"> <div class="pFirst pButton"><span></span></div><div class="pPrev pButton"><span></span></div> </div> <div class="btnseparator"></div> <div class="pGroup"><span class="pcontrol">当前&nbsp;<input name="cp" type="text" size="4" value="1" />&nbsp;页&nbsp;&nbsp;共 <span> 1 </span>&nbsp;页</span></div> <div class="btnseparator"></div> <div class="pGroup"> <div class="pNext pButton"><span></span></div><div class="pLast pButton"><span></span></div> </div> <div class="btnseparator"></div> <div class="pGroup"> <div class="pReload pButton"><span></span></div> </div> <div class="btnseparator"></div> <div class="pGroup"><span class="pPageStat"></span></div> <div class="btnseparator"></div><div class="pGroup"><span class="pUserText"></span></div>';
				
				if(p.slider) {
					html += '<div style="float:right;"><div style="float:left;width:70px;">滑动分页：</div><div class="pPageSlider" style="float:left;width:200px;"></div>&nbsp;&nbsp;&nbsp;&nbsp;当前<span class="pPageSliderCount"></span>页</div>';
				}
				
			$('div',g.pDiv).html(html);
			
			$('.pReload',g.pDiv).click(function(){g.populate()});
			$('.pFirst',g.pDiv).click(function(){g.changePage('first')});
			$('.pPrev',g.pDiv).click(function(){g.changePage('prev')});
			$('.pNext',g.pDiv).click(function(){g.changePage('next')});
			$('.pLast',g.pDiv).click(function(){g.changePage('last')});
			$('.pcontrol input',g.pDiv).keydown(function(e){if(e.keyCode==13) g.changePage('input')});
			if ($.browser.msie&&$.browser.version<7) $('.pButton',g.pDiv).hover(function(){$(this).addClass('pBtnOver');},function(){$(this).removeClass('pBtnOver');});
			
			if (p.useRp)
			{
				var opt = "";
				for (var nx=0;nx<p.rpOptions.length;nx++)
				{
					if (p.rp == p.rpOptions[nx]) sel = 'selected="selected"'; else sel = '';
					 opt += "<option value='" + p.rpOptions[nx] + "' " + sel + " >" + p.rpOptions[nx] + "&nbsp;&nbsp;</option>";
				};
				$('.pDiv2',g.pDiv).prepend("<div class='pGroup'><select name='rp'>"+opt+"</select></div> <div class='btnseparator'></div>");
				$('select',g.pDiv).change(
					function ()
					{
						if (p.onRpChange)
						{
							p.onRpChange(+this.value);
						}
						else
						{
							if(p.adaptiveHeight && !p.isSuggest)
							{
								//设置 flexigrid的高度
								$(g.bDiv).height((+this.value)*30 + 28);
							}
						
						p.newp = 1;
						p.rp = +this.value;
						g.populate();
						}
					}
				);
			}
			//调整用户自定义文本显示位置 by xiangzhou 20120806
			if(p.isSuggest != true){
				g._pDiv = document.createElement('div');
				g._pDiv.className = 'pDiv';
				g._pDiv.innerHTML = ' <div class="pDiv2"><div class="pGroup"><span class="pUserText"></span></div></div> ';
				$(g.pDiv).after(g._pDiv);
				g._pDiv.style.display = 'none';
			}
		}else{
			//调整用户自定义文本显示位置于底部  add by lxxing 2016年4月25日  在不分页情况也也显示用户自定义文本
			if(p.isSuggest != true){
				g._pDiv = document.createElement('div');
				g._pDiv.className = 'pDiv';
				g._pDiv.innerHTML = ' <div class="pDiv2"><div class="pGroup"><span class="pUserText"></span></div></div> ';
				$(g.bDiv).after(g._pDiv);
				g._pDiv.style.display = 'none';
			}
		}
	
		// add title
		if (p.title)
		{
			g.mDiv.className = 'mDiv';
			g.mDiv.innerHTML = '<div class="ftitle">'+p.title+'</div>';
			$(g.gDiv).prepend(g.mDiv);
			if (p.showTableToggleBtn)
			{
				$(g.mDiv).append('<div class="ptogtitle" title="Minimize/Maximize Table"><span></span></div>');
				$('div.ptogtitle',g.mDiv).click
				(
				 	function ()
					{
						$(g.gDiv).toggleClass('hideBody');
						$(this).toggleClass('vsble');
					}
				);
			}
			//g.rePosDrag();
		}

		//setup cdrops
		g.cdropleft = document.createElement('span');
		g.cdropleft.className = 'cdropleft';
		g.cdropright = document.createElement('span');
		g.cdropright.className = 'cdropright';

		//add block
		g.block.className = 'gBlock';
		var gh = $(g.bDiv).height();
		var gtop = g.bDiv.offsetTop;
		$(g.block).css(
		{
			width: g.bDiv.style.width,
			height: gh,
			background: 'white',
			position: 'relative',
			marginBottom: (gh * -1),
			zIndex: 1,
			top: gtop,
			left: '0px'
		}
		);
		$(g.block).fadeTo(0,p.blockOpacity);				
		
		// add column control
		if ($('th',g.hDiv).length)
		{
			
			g.nDiv.className = 'nDiv';
			g.nDiv.innerHTML = "<table cellpadding='0' cellspacing='0'><tbody></tbody></table>";
			$(g.nDiv).css(
			{
				marginBottom: (gh * -1),
				display: 'none',
				top: gtop
			}
			).noSelect()
			;
			
			var cn = 0;
			

			$('th div',g.hDiv).each
			(
			 	function ()
				{
					var kcol = $("th[axis='col" + cn + "']",g.hDiv)[0];
					var chk = 'checked="checked"';
					if (kcol.style.display=='none') chk = '';
					
					if($(this).find('input').length > 0)
					{
						$('tbody',g.nDiv).append('<tr style="display: none"><td class="ndcol1"><input type="checkbox" '+ chk +' class="togCol" value="'+ cn +'" /></td><td class="ndcol2">'+this.innerHTML+'</td></tr>');
					}
					else
					{
						$('tbody',g.nDiv).append('<tr><td class="ndcol1"><input type="checkbox" '+ chk +' class="togCol" value="'+ cn +'" /></td><td class="ndcol2">'+this.innerHTML+'</td></tr>');
					}
					cn++;
				}
			);
			
			if ($.browser.msie&&$.browser.version<7.0)
				$('tr',g.nDiv).hover
				(
				 	function () {$(this).addClass('ndcolover');},
					function () {$(this).removeClass('ndcolover');}
				);
			
			$('td.ndcol2',g.nDiv).click
			(
			 	function ()
				{
					if ($('input:checked',g.nDiv).length<=p.minColToggle&&$(this).prev().find('input')[0].checked) return false;
					return g.toggleCol($(this).prev().find('input').val());
				}
			);
			
			$('input.togCol',g.nDiv).click
			(
			 	function ()
				{
					
					if ($('input:checked',g.nDiv).length<p.minColToggle&&this.checked==false) return false;
					$(this).parent().next().trigger('click');
					//return false;
				}
			);


			$(g.gDiv).prepend(g.nDiv);
			
			$(g.nBtn).addClass('nBtn')
			.html('<div></div>')
			.attr('title','Hide/Show Columns')
			.click
			(
			 	function ()
				{
			 		$(g.nDiv).toggle(); return true;
				}
			);
			
			if (p.showToggleBtn) $(g.gDiv).prepend(g.nBtn);
			
		}
		
		// add date edit layer
		$(g.iDiv)
		.addClass('iDiv')
		.css({display:'none'})
		;
		$(g.bDiv).append(g.iDiv);
		
		// add flexigrid events
		$(g.bDiv)
		.hover(function(){$(g.nDiv).hide();$(g.nBtn).hide();},function(){if (g.multisel) g.multisel = false;})
		;
		$(g.gDiv)
		.hover(function(){},function(){$(g.nDiv).hide();$(g.nBtn).hide();})
		;
		
		//add document events
		$(document)
		.mousemove(function(e){g.dragMove(e)})
		.mouseup(function(e){g.dragEnd()})
		.hover(function(){},function (){g.dragEnd()})
		;
		
		//browser adjustments
		if ($.browser.msie&&$.browser.version<7.0)
		{
			$('.hDiv,.bDiv,.mDiv,.pDiv,.vGrip,.tDiv, .sDiv',g.gDiv)
			.css({width: '100%'});
			$(g.gDiv).addClass('ie6');
			if (p.width!='auto') $(g.gDiv).addClass('ie6fullwidthbug');			
		} 
		
		g.rePosDrag();
		g.fixHeight();
		
		//make grid functions accessible
		t.p = p;
		t.grid = g;
		
		// load data
		if (p.url&&p.autoload) 
		{
			g.populate();
		}
		
		return t;		
			
	};
		
	
	var docloaded = false;

	$(document).ready(function () {docloaded = true} );

	
	$.fn.flexigrid = function(p) {
		return this.each( function() {
		if (!docloaded)
		{
			$(this).hide();
			var t = this;
			$(document).ready
			(
				function ()
				{
				$.addFlex(t,p);
				}
			);
		} else {
			$.addFlex(this,p);
		}
	});

	}; //end flexigrid

	$.fn.ajaxSubmit=function(p){
		
		return this.each(function(){
			if (this.grid&&this.p.url)this.grid.ajaxSubmit();
		});
	};

	$.fn.flexReload = function(p) { // function to reload grid

		return this.each( function() {
				if (this.grid&&this.p.url) this.grid.populate();
			});

	}; //end flexReload
	$.fn.flexOptions = function(p) { //function to update general options

		return this.each( function() {
				if (this.grid) $.extend(this.p,p);
			});

	}; //end flexOptions
	//add by lxxing 2016年5月18日   新增一个获取选中行ID的方法
    $.fn.getCheckedIDs = function() {
        if (this[0].grid) {
            return this[0].grid.getCheckedIDs();
        }
        return [];
    };
    $.fn.getCheckedRows = function() {
    	 if (this[0].grid) {
             return this[0].grid.getCheckedRows();
         }
    };
	$.fn.flexToggleCol = function(cid,visible) { // function to reload grid

		return this.each( function() {
				if (this.grid) this.grid.toggleCol(cid,visible);
			});

	}; //end flexToggleCol

	$.fn.flexAddData = function(data) { // function to add data to grid

		return this.each( function() {
				if (this.grid) this.grid.addData(data);
			});

	};

	$.fn.noSelect = function(p) { //no select plugin by me :-)

		if (p == null) 
			prevent = true;
		else
			prevent = p;

		if (prevent) {
		
		return this.each(function ()
			{
				if ($.browser.msie||$.browser.safari) $(this).bind('selectstart',function(){return false;});
				else if ($.browser.mozilla) 
				{
					$(this).css('MozUserSelect','none');
					$('body').trigger('focus');
				}
				else if ($.browser.opera) $(this).bind('mousedown',function(){return false;});
				else $(this).attr('unselectable','on');
			});
			
		} else {

		
		return this.each(function ()
		{
			if ($.browser.msie||$.browser.safari) $(this).unbind('selectstart');
			else if ($.browser.mozilla) $(this).css('MozUserSelect','inherit');
			else if ($.browser.opera) $(this).unbind('mousedown');
			else $(this).removeAttr('unselectable','on');
		});
		
		}

	}; //end noSelect
		
	/**
	 * function $.getThisFormObj(obj)
	 * add by miaojia 2009-08-07
	 */
	$.getThisFormObj = function(obj) {
		var formObj;
		$.each($("form"), function(i,n){
			if($(n).find(obj)){
				formObj = n;
			}
		});
		return formObj;
	};
	
	/**
	 * 新建一个hidden隐藏域
	 */
	$.createHideObject = function(p) {
		p = $.extend({
			id : "",
			name : "",
			value : ""
		}, p);
		if($(document).find("#" + p.name).length > 0) {
			$("#" + p.name).val(p.value);
			return false;
		}
//		if($(document).find("#" + p.name).length > 0) return false;
		var inputObj = document.createElement("INPUT");
		inputObj.type="hidden";
		inputObj.id=p.name;
		inputObj.name=p.name;
		inputObj.value=p.value;
		return inputObj;
	};
	

})(jQuery);