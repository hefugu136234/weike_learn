;
qcVideo('Action', function($, Component, PlayerConst) {
	var EVENT = PlayerConst.EVENT;
	return Component.extend({
		className : 'Action',
		init : function() {
			this.bind('a', function(dom) {
				var $me = $(dom);
				var isPlaying = $me.hasClass('btn-stop');
				if (isPlaying) {
					this.fire('FIRE', {
						event : EVENT.UI_PAUSE,
						value : this.status.played
					});
				} else {
					this.fire('FIRE', {
						event : EVENT.UI_PLAY,
						value : this.status.played
					});
				}
			});
		},
		setStatus : function(isPause) {
			this.$el.find('a').get(0).className = isPause ? 'btn-stop'
					: 'btn-start';
		}
	});
});
;
qcVideo('Component', function(Base) {
	return Base.extend({
		className : 'Component',
		tapEvent : 'click',
		destroy : function() {
			if (this.$el) {
				this.$el.remove();
				delete this.$el;
				delete this.status;
				delete this.store;
			}
		},
		constructor : function(store, status, $el) {
			var me = this;
			me.$el = $el;
			me.status = status;
			me.store = store;
			me.enable_tag = true;
			me.init();
		},
		init : Base.loop,
		visible : function(visible) {
			if (this.$el) {
				this.$el[visible ? 'show' : 'hide']();
			}
		},
		enable : function(enable) {
			this.enable_tag = enable;
		},
		bind : function(match, fn) {
			var me = this, get = function(ctx, hand) {
				return function(e) {
					if (ctx.enable_tag) {
						hand.call(ctx, this, e);
					}
					e.stopPropagation();
					return false;
				};
			};
			if (match) {
				me.$el.find(match).on(me.tapEvent, get(me, fn));
			} else {
				me.$el.on(me.tapEvent, get(me, fn));
			}
		}
	});
});
;
qcVideo('Definition', function(Component, PlayerConst) {
	return Component.extend({
		className : 'Definition',
		init : function() {
			this.bind(null, function() {
				this.fire(PlayerConst.EVENT.UI_CLICK_DEFINITION);
			});
		},
		setText : function(t) {
			this.$el.find('a').text(t);
		}
	});
});
;
qcVideo('Definition_panel', function($, Component, MediaPlayer_tpl,
		PlayerConst, constants) {
	var EVENT = PlayerConst.EVENT;
	return Component.extend({
		className : 'Definition_panel',
		init : function() {
			var me = this;
			var data = me.store.getExistDefinition();
			if (data) {
				me.$el.html(MediaPlayer_tpl['definition_panel'](data));
			}
			me.bind('a', function(dom) {
				var $li = $(dom).parent();
				if (!$li.hasClass('cur')) {
					me.fire('FIRE', {
						event : EVENT.UI_SWITCH_DEFINITION,
						value : $li.attr('resolution')
					});
				}
			});
		},
		setCurResolution : function(def) {
			this.$el.find('li.cur').removeClass('cur');
			this.$el.find('li[resolution="' + def + '"]').addClass('cur');
		}
	});
});
;
qcVideo('Fullscreen', function(Component, PlayerConst) {
	return Component.extend({
		className : 'Fullscreen',
		init : function() {
			this.bind('a', function() {
				this.fire('FIRE', {
					event : PlayerConst.EVENT.UI_FULL_SCREEN
				});
			});
		}
	});
});
;
qcVideo('Paster', function(Component) {
	return Component.extend({
		className : 'Paster',
		init : function() {
			var me = this;
		}
	});
});
;
qcVideo('Progressbar', function($, Component, PlayerConst, h5Drag) {
	var EVENT = PlayerConst.EVENT;
	return Component.extend({
		className : 'Progressbar',
		getTime : function(secs) {
			secs = secs | 0;
			var pack = function(t) {
				return (t | 0) < 10 ? "0" + t : t;
			};
			var date = new Date(secs * 1000), h = date.getHours() - 8, m = date
					.getMinutes(), s = date.getSeconds();
			if (!this.haveHour) {
				return pack(m) + ":" + pack(s);
			} else {
				return pack(h) + ":" + pack(m) + ":" + pack(s);
			}
		},
		init : function() {
			var me = this;
			var $round = me.$el.find('.round');
			var $cur = me.$el.find('.current');
			var $all = me.$el.find('.all');
			var $loaded = me.$el.find('.ash');
			var $played = me.$el.find('.on');
			me.on(me.status, EVENT.OS_LOADED_META_DATA, function() {
				me.haveHour = me.status.duration > 3600;
				$all.text(me.getTime(me.status.duration));
				$cur.text(me.getTime(0));
			});
			me.on(me.status, EVENT.OS_TIME_UPDATE, function() {
				if (me._moving) {
					return;
				}
				var u = me.status.loaded, d = me.status.duration, r = 0;
				if (d > 0) {
					r = u * 100 / d;
				}
				$loaded.width(r.toFixed(1) + '%');
			});
			me.on(me.status, EVENT.OS_PROGRESS, function() {
				if (me._moving) {
					return;
				}
				var u = me.status.played, d = me.status.duration, r = 0;
				if (d > 0) {
					r = u * 100 / d;
				}
				r = r.toFixed(1) + '%';
				$played.width(r);
				$round.css('left', r);
				$cur.text(me.getTime(u));
			});
			me.bind('.progress-bar', function(dom, e) {
				var rate = (e.offsetX | 0) / $(dom).width();
				me.fire('FIRE', {
					event : EVENT.UI_PLAY,
					value : rate * me.status.duration
				});
			});
			new h5Drag(me, $round, me.$el.find('.progress-bar'), function() {
				me._moving = true;
			}, null, function(rate) {
				var d = me.status.duration, u = rate * d;
				$played.width(rate + '%');
				$round.css('left', rate + '%');
				$cur.text(me.getTime(u));
				me._moving = false;
				me.fire('FIRE', {
					event : EVENT.UI_PLAY,
					value : u
				});
			});
		}
	});
});
;
qcVideo('Setting_panel', function($, Component, PlayerConst, h5Drag) {
	var EVENT = PlayerConst.EVENT;
	return Component.extend({
		className : 'Setting_panel',
		init : function() {
			var me = this;
			var $bar = me.$el.find('.progress-bar');
			var $volume = $bar.find('[data-mode="volume"]');
			var $round = $bar.find('[data-mode="round"]');
			var set = function(rate) {
				rate = rate * 100;
				$volume.width(rate + '%');
			};
			new h5Drag(me, $round, $bar, null, function(rate) {
				set(rate);
			}, function(rate) {
				set(rate);
				$round.css('left', rate * 100 + '%');
				me.fire('FIRE', {
					event : EVENT.UI_SET_VOLUME,
					value : rate
				});
			});
		},
		setVolume : function(num) {
			var val = (num * 100) + '%';
			var $bar = this.$el.find('.progress-bar');
			$bar.find('[data-mode="volume"]').width(val);
			$bar.find('[data-mode="round"]').css('left', val);
		}
	});
});
;
qcVideo('Top', function($, Component, PlayerConst) {
	var EVENT = PlayerConst.EVENT;
	return Component.extend({
		className : 'Top',
		init : function() {
			this.bind('[data-mode="back"]', function() {
				this.fire(EVENT.UI_BACK);
			});
			this.bind('[data-mode="setting"]', function() {
				this.fire(PlayerConst.EVENT.UI_SETTING, this.className);
			});
		}
	});
});
;
qcVideo('h5Drag', function($, Base) {
	return Base.extend({
		className : 'Drag',
		constructor : function(ctx, $drag, $range, onStart, onMove, onEnd) {
			var me = this, getTouchLeft = function(e) {
				return e.targetTouches[0].pageX
			}, getRate = function() {
				return ($drag.css('left').replace('px', '') - 0) / me.maxLeft;
			};
			$(document.body).on(
					'touchstart',
					function(e) {
						if (e.target === $drag.get(0) && ctx.enable_tag) {
							var range = $range.offset();
							me.sourceL = getTouchLeft(e);
							me.maxLeft = range.width;
							me.initLeft = me.maxLeft
									* ($drag.css('left').replace('%', '') - 0)
									/ 100;
							onStart && onStart();
						} else {
							me.sourceL = null;
						}
					}).on('touchmove', function(e) {
				if (me.sourceL !== null) {
					var diff = getTouchLeft(e) - me.sourceL;
					var newLeft = me.initLeft + diff;
					if (newLeft >= me.maxLeft || newLeft <= 0) {
						return;
					}
					$drag.css('left', newLeft + 'px');
					onMove && onMove(getRate());
				}
			}).on('touchend', function(e) {
				if (me.sourceL !== null) {
					onEnd && onEnd(getRate());
				}
			});
		}
	});
});
;
qcVideo(
		'MediaPlayer',
		function(Base, PlayerControl, PlayerSystem, PlayerStore, PlayerConst,
				MediaPlayer_tpl) {
			var P_MODE = PlayerControl.MODE;
			return Base
					.extend({
						system : !1,
						store : !1,
						control : !1,
						className : 'MediaPlayer',
						destroy : function() {
							if (this.control) {
								this.control.destroy();
								this.system.destroy();
								this.store.destroy();
								delete this.control;
								delete this.system;
								delete this.store;
							}
						},
						initVideo : function(video) {
							var me = this;
							if (video) {
								me.system.setUrl(video['url']);
							}
						},
						constructor : function(info) {
							var me = this, setting = info.setting, $renderTo = setting.$renderTo, EVENT = PlayerConst.EVENT, store = me.store = new PlayerStore(
									info), video = store
									.getVideoInfo(setting.definition), $container = $renderTo
									.html(MediaPlayer_tpl['main']({
										width : setting.width,
										height : setting.height
									})).find('div'), system = me.system = new PlayerSystem(
									$container), status = system.getStatus();
							me.log('width==', video.width, 'height==',
									video.height, 'outerWidth==', $renderTo
											.width() - 4, 'outerHeight==',
									$renderTo.height() - 4);
							me.on(status, EVENT.OS_LOADED_META_DATA,
									function() {
										if (me.whenMetaReady.autoPlay) {
											system.play(me.whenMetaReady.time);
										} else {
											system.pause();
										}
									});
							me.initVideo(video);
							me.whenMetaReady = {
								time : 0,
								autoPlay : setting.isAutoPlay
							};
							system.setPoster(store.getStartPatch());
						}
					});
		});
qcVideo(
		'MediaPlayer_tpl',
		function() {
			return {
				'main' : function(data) {
					var __p = [], _p = function(s) {
						__p.push(s)
					};
					_p('<div style="width:');
					_p(data.width);
					_p('px;height:');
					_p(data.height);
					_p('px;margin: 0px auto;position:relative;background-color: #000;"></div>');
					return __p.join("");
				},
				'controller' : function(data) {
					var __p = [], _p = function(s) {
						__p.push(s)
					};
					_p('<div h5-controller>\r\n\
           <!--娓呮櫚搴﹀脊妗�-->\r\n\
           <div class="sidebar" component="definition_panel" style="z-index: 2;"></div>\r\n\
           <!--閿欒鎻愮ず-->\r\n\
           <!--\r\n\
           <div class="layer-wifi">\r\n\
              <span class="tip">鎻愰啋锛氭偍姝ｅ湪浣跨敤闈瀢ifi缃戠粶锛岀户缁�<br>鎾斁灏嗕骇鐢熸祦閲忚垂鐢�</span>\r\n\
              <a class="btn-play" href="#">缁х画鎾斁</a>\r\n\
           </div>\r\n\
           -->\r\n\
   \r\n\
           <!--璁剧疆闈㈡澘-->\r\n\
           <div class="about-list" component="setting_panel" style="z-index: 2;">\r\n\
               <div class="volume-wrap">\r\n\
                   <div class="cell"><i class="ico-mute"></i></div>\r\n\
                   <div class="cell progress-bar-wrap">\r\n\
                       <i class="progress-bar">\r\n\
                           <i class="ash" style="width:100%;"></i>\r\n\
                           <i class="on" style="width:0%;" data-mode="volume"></i>\r\n\
                           <i class="round" style="left:0%;" data-mode="round"></i>\r\n\
                       </i>\r\n\
                   </div>\r\n\
                   <div class="cell"><i class="ico-volume"></i></div>\r\n\
               </div>\r\n\
           </div>\r\n\
   \r\n\
           <div class="top-box" style="z-index: 2;" component="top">\r\n\
               <!--<div class="cell btn-wrap"><a href="#" class="btn-back" data-mode="back"></a></div>-->\r\n\
               <div class="cell title-wrap"><span data-name="video"> </span></div>\r\n\
               <!--<div class="cell btn-wrap"><a href="#" class="btn-step" data-mode="setting"></a></div>-->\r\n\
           </div>\r\n\
   \r\n\
           <div class="bottom-box" style="z-index: 3;">\r\n\
               <!--鏆傚仠銆佹挱鏀�-->\r\n\
               <div class="cell btn-wrap" component="action">\r\n\
                   <a class="btn-start" href="#"></a>\r\n\
               </div>\r\n\
               <!--杩涘害鏉�-->\r\n\
               <div class="cell progress-bar-wrap" component="progressbar">\r\n\
                   <span class="current"> </span>\r\n\
                   <i class="progress-bar">\r\n\
                       <i class="ash" style="width:0%;"></i>\r\n\
                       <i class="on" style="width:0%;"></i>\r\n\
                       <i class="round" style="left:0%;"></i>\r\n\
                   </i>\r\n\
                   <span class="all"> </span>\r\n\
               </div>\r\n\
               <!--娓呮櫚搴�-->\r\n\
               <div class="cell btn-wrap" component="definition"><a class="btn" href="#">鏍囨竻</a></div>\r\n\
               <!--<div class="cell btn-wrap" component="fullscreen"><a class="btn-zoom" href="#"></a></div>-->\r\n\
               <!--<div class="cell btn-wrap"><a class="btn" href="">閫夐泦</a></div>-->\r\n\
           </div>\r\n\
   \r\n\
           <!--灞忓箷閿�-->\r\n\
           <!--<a class="btn-lock"></a>-->\r\n\
       </div>');
					return __p.join("");
				},
				'definition_panel' : function(data) {
					var __p = [], _p = function(s) {
						__p.push(s)
					};
					_p('<ul class="distinct">');
					for (var i = 0, j = data.length; i < j; i++) {
						var itm = data[i];
						_p('                <li resolution="');
						_p(itm.resolution);
						_p('"><a href="#">');
						_p(itm.resolutionName);
						_p('</a></li>');
					}
					_p('    </ul>');
					return __p.join("");
				},
				'video' : function(data) {
					var __p = [], _p = function(s) {
						__p.push(s)
					};
					_p('<video id="');
					_p(data.vid);
					_p('" width="100%" height="100%" controls=\'true\' style="z-index: 1;position: absolute;top: 0px;left: 0px;">\r\n\
           <source src="');
					_p(data.url);
					_p('" type="video/mp4"/>\r\n\
       </video>');
					return __p.join("");
				},
				__escapeHtml : (function() {
					var a = {
						"&" : "&amp;",
						"<" : "&lt;",
						">" : "&gt;",
						"'" : "&#39;",
						'"' : "&quot;",
						"/" : "&#x2F;"
					}, b = /[&<>'"\/]/g;
					return function(c) {
						if (typeof c !== "string")
							return c;
						return c ? c.replace(b, function(b) {
							return a[b] || b
						}) : ""
					}
				})()
			}
		});
;
qcVideo('PlayerConst', function() {
	return {
		EVENT : {
			OS_TIME_UPDATE : 'OS_TIME_UPDATE',
			OS_PROGRESS : 'OS_PROGRESS',
			OS_LOADED_META_DATA : 'OS_LOADED_META_DATA',
			OS_PLAYER_END : 'OS_PLAYER_END',
			OS_VIDEO_LOADING : 'OS_VIDEO_LOADING',
			OS_ERROR : 'OS_ERROR',
			OS_BLOCK : 'OS_BLOCK',
			UI_SET_VOLUME : 'UI_SET_VOLUME',
			UI_SEEK_TIME : 'UI_SEEK_TIME',
			UI_SWITCH_DEFINITION : 'UI_SWITCH_DEFINITION',
			UI_PAUSE : 'UI_PAUSE',
			UI_PLAY : 'UI_PLAY',
			UI_CLICK_DEFINITION : 'UI_CLICK_DEFINITION',
			UI_BACK : 'UI_BACK',
			UI_SETTING : 'UI_SETTING',
			UI_FULL_SCREEN : 'UI_FULL_SCREEN'
		},
		ERROR : {
			DISABLE_VISIT : '褰撳墠瑙嗛鏃犳硶璁块棶'
		}
	};
});
;
qcVideo(
		'PlayerControl',
		function($, Base, PlayerConst, MediaPlayer_tpl) {
			var EVENT = PlayerConst.EVENT;
			var capitalize = function(str) {
				str = str || '';
				return str.charAt(0).toUpperCase() + str.slice(1);
			};
			var PlayerControl = Base
					.extend({
						className : 'PlayerControl',
						destroy : function() {
							var me = this;
							delete me.store;
							if (me.children) {
								for ( var name in me.children) {
									me.children[name].destroy();
								}
								delete me.children;
							}
							delete me.childNames;
							delete me.$container;
						},
						constructor : function(store, status, $renderTo) {
							var me = this;
							me.store = store;
							me.children = {};
							me.childNames = [];
							$renderTo.append(MediaPlayer_tpl['controller']());
							(me.$container = $renderTo.find('[h5-controller]'))
									.find('[component]')
									.each(
											function() {
												var $me = $(this), component = capitalize($me
														.attr('component')), Component = qcVideo
														.get(component), child = me.children[component] = new Component(
														store, status, $me);
												me.childNames.push(component);
												me.on(child, 'FIRE', function(
														obj) {
													me.fire(obj.event,
															obj.value);
												});
												if (component === 'Definition') {
													me
															.on(
																	child,
																	EVENT.UI_CLICK_DEFINITION,
																	function() {
																		me.children['Definition_panel']
																				.visible(true);
																	});
												} else if (component === 'Top') {
													me.on(child, EVENT.UI_BACK,
															function() {
															});
													me
															.on(
																	child,
																	EVENT.UI_SETTING,
																	function(
																			val) {
																		me.children['Setting_panel']
																				.visible(true);
																	});
												}
											});
						},
						setVideo : function(video) {
							this.video = video;
							this.children['Definition_panel']
									.setCurResolution(video.resolution);
							this.children['Definition']
									.setText(video.resolutionName);
							this.children['Setting_panel'].setVolume(0.5);
							this.$container.find('[data-name="video"]').text(
									video.name);
						},
						activeScreen : function() {
							var me = this;
							if (me.hideControlTimeout) {
								clearTimeout(me.hideControlTimeout);
								delete me.hideControlTimeout;
							}
							me.$container.find('.top-box').show();
							me.$container.find('.bottom-box').show();
							if (me.mode === PlayerControl.MODE.PLAY) {
								me.hideControlTimeout = setTimeout(function() {
									me.$container.find('.top-box').hide();
									me.$container.find('.bottom-box').hide();
								}, 3000);
							}
						},
						setMode : function(mode, msg) {
							var me = this, MODE = PlayerControl.MODE, hideNames, defaultHide = {
								'Setting_panel' : 1,
								'Definition_panel' : 1
							}, disableNames;
							switch (mode) {
							case (MODE.WAIT):
								disableNames = {
									'Progressbar' : 1,
									'Action' : 1,
									'Top' : 1
								};
								break;
							case (MODE.READY):
								break;
							case (MODE.PAUSE):
								me.children['Action'].setStatus(false);
								break;
							case (MODE.PLAY):
								me.children['Action'].setStatus(true);
								break;
							case (MODE.BLOCK):
								me.children['Action'].setStatus(false);
								break;
							case (MODE.ERROR):
								break;
							case (MODE.END):
								break;
							case (MODE.FULL):
								break;
							}
							if (!hideNames) {
								hideNames = defaultHide;
							}
							$.each(me.childNames, function(_, name) {
								var child = me.children[name];
								child.visible(!hideNames
										|| !hideNames.hasOwnProperty(name));
								child.enable(!disableNames
										|| !disableNames.hasOwnProperty(name));
							});
							me.mode = mode;
							me.activeScreen();
						}
					});
			PlayerControl.MODE = {
				WAIT : 'wait',
				READY : 'ready',
				PAUSE : 'pause',
				PLAY : 'play',
				BLOCK : 'block',
				ERROR : 'error',
				END : 'end',
				FULL : 'full'
			};
			return PlayerControl;
		});
;
qcVideo('PlayerStatus', function(Base, PlayerConst, interval) {
	var time = 1000 / 60, blockTime = time * 300, EVENT = PlayerConst.EVENT;
	return Base.extend({
		className : 'PlayerStatus',
		clear : function() {
			var me = this;
			me.played = 0;
			me.duration = 0;
			me.loaded = 0;
			me.loaded_overtime = 0;
			me.errorCode = 0;
			me.equalRead();
			me.timeTask.start();
		},
		equalRead : function() {
			var me = this;
			me.__read = {
				played : me.played,
				duration : me.duration,
				loaded : me.loaded
			};
		},
		constructor : function() {
			var me = this;
			me.timeTask = interval(function() {
				var bool = false;
				if (me.__read.duration !== me.duration) {
					bool = true;
					me.fire(EVENT.OS_LOADED_META_DATA);
				}
				if (me.__read.played !== me.played) {
					bool = true;
					me.fire(EVENT.OS_PROGRESS);
				}
				if (me.__read.loaded !== me.loaded) {
					bool = true;
					me.fire(EVENT.OS_TIME_UPDATE);
				}
				me.equalRead();
				if (!bool && me.__isMaybeBlockStatus()) {
					if (me.played >= me.duration && me.duration > 0) {
						me.fire(EVENT.OS_PLAYER_END);
					}
					var now = +new Date();
					if (now - me.status_start > blockTime
							&& me.__getStatusValue() === me.status_value) {
						me.fire(EVENT.OS_BLOCK);
					} else {
						me.fire(EVENT.OS_VIDEO_LOADING);
					}
				}
			}, time);
			me.clear();
		},
		destroy : function() {
			this.timeTask.clear();
		},
		__getStatusValue : function() {
			return (this.played - 0) + ':' + (this.loaded - 0) + ':'
					+ (this.duration - 0);
		},
		__isMaybeBlockStatus : function() {
			return this.status === 'play' || this.status === 'load';
		},
		setRunningStatus : function(status) {
			this.status = status;
			this.status_start = +new Date();
			this.status_value = this.__getStatusValue();
		},
		set_duration : function(num) {
			this.duration = num - 0;
		},
		set_loaded : function(num) {
			this.loaded = num - 0;
		},
		set_played : function(num) {
			this.played = num - 0;
		}
	});
});
;
qcVideo(
		'PlayerStore',
		function($, Base, util, constants) {
			return Base
					.extend({
						className : 'PlayerStore',
						constructor : function(store) {
							this.store = store;
						},
						destroy : function() {
							delete this.store;
						},
						_getPoster : function(pos) {
							var ret;
							if (this.store.patch && this.store.patch[pos]) {
								ret = this.store.patch[pos].url;
							}
							if (!ret) {
								if (this.store.imgUrls
										&& this.store.imgUrls.length) {
									ret = this.store.imgUrls[0].url;
								}
							}
							return ret;
						},
						getStartPatch : function() {
							return this
									._getPoster(constants.PATCH_LOC.START | 0);
						},
						getPausePatch : function() {
							return this
									._getPoster(constants.PATCH_LOC.PAUSE | 0);
						},
						getSopPatch : function() {
							return this._getPoster(constants.PATCH_LOC.END | 0);
						},
						getAllDefinition : function() {
							var ret = {}, pir = constants.DEFINITION_PRIORITY, def, ind;
							for (def in this.store.videos) {
								for (ind in pir) {
									$
											.each(
													pir[ind],
													function(_, i) {
														if (def == i) {
															ret[def] = {
																'resolution' : ind,
																'resolutionName' : constants.DEFINITION_NAME[ind]
															};
															return false;
														}
													});
								}
							}
							return ret;
						},
						getExistDefinition : function() {
							var dup = {}, list = [], all = this
									.getAllDefinition(), k, t;
							for (k in all) {
								t = all[k]['resolution'];
								if (!dup[t]) {
									list.push(all[k]);
									dup[t] = true;
								}
							}
							list.sort(function(a, b) {
								return a['resolution'] < b['resolution'];
							});
							return list;
						},
						getVideoInfo : function(res, def) {
							var me = this, rst, videos = me.store.videos, resolutionPriority = constants.RESOLUTION_PRIORITY, getRstFromDefPriority = function(
									res) {
								var arr = constants.DEFINITION_PRIORITY[res];
								if (arr && arr.length > 0) {
									return videos[arr[0]] || videos[arr[1]]
											|| null;
								}
							};
							me.debug(':getVideoInfo:娓呮櫚搴�==' + res
									+ ';鎸囧畾鐗囨簮==' + def);
							res = res === undefined ? null : (res | 0);
							def = def === undefined ? null : (def | 0);
							if (def !== null) {
								rst = videos[def];
							}
							if (!rst && res !== null) {
								rst = getRstFromDefPriority(res);
							}
							if (!rst) {
								for (var i = 0, j = resolutionPriority.length; i < j; i++) {
									rst = getRstFromDefPriority(resolutionPriority[i]);
									if (rst) {
										break;
									}
								}
							}
							if (rst) {
								var map = me.getAllDefinition()[rst['definition']];
								me.debug(':getVideoInfo result:娓呮櫚搴�=='
										+ map['resolution'] + ';鍚嶇О=='
										+ map['resolutionName']);
								return util.merge({
									resolution : map['resolution'],
									resolutionName : map['resolutionName']
								}, rst);
							}
						}
					});
		});
;
qcVideo('PlayerSystem', function($, Base, PlayerStatus, MediaPlayer_tpl,
		interval) {
	var getId = function() {
		return 'video_id_' + (+new Date());
	}, time = 1000 / 60;
	return Base.extend({
		className : 'PlayerSystem',
		constructor : function($renderTo) {
			var me = this;
			me.$renderTo = $renderTo;
			me.status = new PlayerStatus();
			me.timeTask = interval(function() {
				if (me.video) {
					var r = me.video.buffered, loaded = 0;
					if (r) {
						for (var i = 0; i < r.length; i++) {
							loaded = r.end(i) - 0;
						}
					}
					me.status.set_loaded(loaded);
					me.status.set_played(me.video.currentTime);
				}
			}, time);
		},
		destroy : function() {
			this.timeTask.clear();
			delete this.$renderTo;
			this.status.destroy();
			delete this.status;
		},
		getStatus : function() {
			return this.status;
		},
		callMethod : function(mtd) {
			try {
				this.video[mtd]();
				this.status.setRunningStatus(mtd);
			} catch (xe) {
			}
		},
		_bind : function() {
			var me = this;
			var getHandler = function(event) {
				return function(e) {
					var video = me.video;
					switch (event) {
					case ('loadedmetadata'):
						me.metadatadone = true;
						me.status.set_duration(video.duration);
						me.timeTask.start();
						break;
					case ('error'):
						me.log(event, e);
						break;
					}
				};
			};
			$.each('loadedmetadata,error'.split(','), function(_, event) {
				me.$video.on(event, getHandler(event));
			});
		},
		setUrl : function(src) {
			var me = this, $renderTo = me.$renderTo, tpl = me.tpl = {
				vid : getId(),
				width : $renderTo.width(),
				height : $renderTo.height(),
				url : src
			};
			me.metadatadone = false;
			me.timeTask.pause();
			me.status.clear();
			me.$video && me.$video.remove();
			$renderTo.prepend(MediaPlayer_tpl['video'](tpl));
			me.video = (me.$video = $('#' + tpl.vid)).get(0);
			me._bind();
		},
		play : function(time) {
			var me = this;
			if (me.video) {
				if (me.metadatadone) {
					if (time !== undefined) {
						me.video.currentTime = time;
					}
					me.callMethod('play');
				} else {
					me.callMethod('load');
				}
			}
		},
		pause : function(time) {
			var me = this;
			if (me.video) {
				if (me.metadatadone) {
					me.callMethod('pause');
					if (time !== undefined) {
						me.video.currentTime = time;
					}
				} else {
					me.callMethod('load');
				}
			}
		},
		setVolume : function(num) {
			if (this.video) {
				this.video.volume = num;
			}
		},
		setPoster : function(url) {
			if (this.video && url) {
				this.video.poster = url;
			}
		}
	});
});
;
qcVideo('h5player', function($, Base, constants, util, MediaPlayer) {
	return Base.instance({
		className : 'h5player',
		constructor : Base.loop,
		render : function(opt) {
			var parameter = opt.parameter || {}, store = {
				patch : {},
				videos : {},
				setting : {
					width : opt.width,
					height : opt.height,
					$renderTo : opt.$renderTo,
					isAutoPlay : parameter['auto_play'] == 1,
					file_id : parameter['file_id'],
					app_id : parameter['app_id'],
					definition : parameter['definition']
				}
			}, fileInfo = opt.file_info, video = fileInfo.image_video.videoUrls
					|| [], patch = opt.player_info.patch_info || [];
			store.imgUrls = fileInfo.image_video.imgUrls;
			$.each(patch, function(_, item) {
				store.patch[item['location_type'] | 0] = {
					'url' : item['patch_url'],
					'redirect' : item['patch_redirect_url'],
					'type' : item['patch_type']
				};
			});
			$.each(video, function(_, item) {
				var def = item['definition'] | 0;
				store.videos[def] = {
					'definition' : def,
					'name' : fileInfo['name'] || '',
					'url' : item['url'],
					'height' : item['vheight'],
					'width' : item['vwidth']
				};
			});
			this.mediaPlayer = new MediaPlayer(store);
		},
		destroy : function() {
			this.mediaPlayer.destroy();
		}
	});
});/* |xGv00|82195e2a14881d7bfc885280e712c115 */