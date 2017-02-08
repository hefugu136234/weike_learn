;
(function(global) {
	var ns = {
		modules : {},
		instances : {}
	}, waiter = {};
	function getMappingArgs(fn) {
		var args = fn.toString().split('{')[0]
				.replace(/\s|function|\(|\)/g, '').split(','), i = 0;
		if (!args[0]) {
			args = [];
		}
		while (args[i]) {
			args[i] = require(args[i]);
			i += 1;
		}
		return args;
	}
	function newInst(key, ifExist) {
		if ((ifExist ? ns.instances[key] : !ns.instances[key])
				&& ns.modules[key]) {
			ns.instances[key] = ns.modules[key].apply(window,
					getMappingArgs(ns.modules[key]));
		}
	}
	function require(key) {
		newInst(key, false);
		return ns.instances[key] || {};
	}
	function loadJs(url) {
		var el = document.createElement('script');
		el.setAttribute('type', 'text/javascript');
		el.setAttribute('src', url);
		el.setAttribute('async', true);
		document.getElementsByTagName("head")[0].appendChild(el);
	}
	function core(key, target) {
		ns.modules[key] = target;
		newInst(key, true);
		if (!!waiter[key]) {
			var i = 0;
			while (waiter[key][i]) {
				waiter[key][i](require(key));
				i += 1;
			}
			delete waiter[key];
		}
	}
	core.use = function(key, cb) {
		cb = cb || function() {
		};
		if (ns.modules[key]) {
			cb(require(key));
		} else {
			var config = require('config');
			if (config[key]) {
				if (!waiter[key]) {
					waiter[key] = [];
					loadJs(config[key]);
				}
				waiter[key].push(cb);
			}
		}
	};
	core.get = function(key) {
		return require(key);
	};
	core.loadJs = loadJs;
	global.qcVideo = core;
})(window);
;
qcVideo(
		'api',
		function() {
			var now = function() {
				return +new Date();
			}, uuid = 0, global = window, unique = 'qcvideo_' + now(), overTime = 10000;
			var request = function(address, cbName, cb) {
				return function() {
					global[cbName] = function(data) {
						cb(data);				
						delete global[cbName];
					};
					setTimeout(function() {
						if (typeof global[cbName] !== "undefined") {
							delete global[cbName];
							cb({
								'retcode' : 10000,
								'errmsg' : '请求超时'
							});
						}
					}, overTime);
					qcVideo.loadJs(address
							+ (address.indexOf('?') > 0 ? '&' : '?')
							+ 'callback=' + cbName);
				}
			};
			var hiSender = function() {
				var img = new Image();
				return function(src) {
					img.onload = img.onerror = img.onabort = function() {
						img.onload = img.onerror = img.onabort = null;
						img = null;
					};
					img.src = src;
				};
			};
			var apdTime = function(url) {
				return url + (url.indexOf('?') > 0 ? '&' : '?') + '_=' + now();
			};
			return {
				request : function(address, cb) {
					var cbName = unique + '_callback' + (++uuid);
					request(apdTime(address), cbName, cb)();
				},
				report : function(address) {
					hiSender()(apdTime(address));
				}
			};
		});
;
qcVideo('config', function() {
	var h5 = 'http://qzonestyle.gtimg.cn/open/qcloud/video/h5';
	var flash = 'http://qzonestyle.gtimg.cn/open/qcloud/video/flash';
	return {
		'$' : h5 + '/zepto-v1.1.3.min.js?max_age=20000000',
		'h5player' : '/assets/js/qq/h5player.js',
		'flash' : flash + '/video_player.swf?max_age=1',
		'h5css' : h5 + '/video.css?ver=0531&max_age=20000000',
		set : function(key, url) {
			this[key] = url;
		}
	};
});
;
qcVideo('interval', function() {
	var git, stack = {}, length = 0, gTime = 16, uuid = 0;
	function each(cb) {
		for ( var key in stack) {
			if (false === cb.call(stack[key])) {
				return;
			}
		}
	}
	function tick() {
		var now = +new Date();
		each(function() {
			var me = this;
			!me.__time && (me.__time = now);
			if (me.__time + me._ftp <= now && me.status === 1) {
				me.__time = now;
				me._cb.call();
			}
		});
	}
	function stop() {
		var start = 0;
		each(function() {
			this.status === 1 && (start += 1);
		});
		if (start === 0 || length === 0) {
			clearInterval(git);
			git = null;
		}
	}
	function _start() {
		this.status = 1;
		!git && (git = setInterval(tick, gTime));
	}
	function _pause() {
		this.status = 0;
		this.__time = +new Date();
		stop();
	}
	function _clear() {
		delete stack[this._id];
		length -= 1;
		stop();
	}
	return function(callback, time) {
		length += 1;
		uuid += 1;+
		console.log(time)
		return stack[uuid] = {
			_id : uuid,
			_cb : callback,
			_ftp : time || gTime,
			start : _start,
			pause : _pause,
			clear : _clear
		};
	};
})
if (typeof JSON !== 'object') {
	JSON = {};
}
(function() {
	'use strict';
	function f(n) {
		return n < 10 ? '0' + n : n;
	}
	if (typeof Date.prototype.toJSON !== 'function') {
		Date.prototype.toJSON = function() {
			return isFinite(this.valueOf()) ? this.getUTCFullYear() + '-'
					+ f(this.getUTCMonth() + 1) + '-' + f(this.getUTCDate())
					+ 'T' + f(this.getUTCHours()) + ':'
					+ f(this.getUTCMinutes()) + ':' + f(this.getUTCSeconds())
					+ 'Z' : null;
		};
		String.prototype.toJSON = Number.prototype.toJSON = Boolean.prototype.toJSON = function() {
			return this.valueOf();
		};
	}
	var cx, escapable, gap, indent, meta, rep;
	function quote(string) {
		escapable.lastIndex = 0;
		return escapable.test(string) ? '"'
				+ string.replace(escapable,
						function(a) {
							var c = meta[a];
							return typeof c === 'string' ? c : '\\u'
									+ ('0000' + a.charCodeAt(0).toString(16))
											.slice(-4);
						}) + '"' : '"' + string + '"';
	}
	function str(key, holder) {
		var i, k, v, length, mind = gap, partial, value = holder[key];
		if (value && typeof value === 'object'
				&& typeof value.toJSON === 'function') {
			value = value.toJSON(key);
		}
		if (typeof rep === 'function') {
			value = rep.call(holder, key, value);
		}
		switch (typeof value) {
		case 'string':
			return quote(value);
		case 'number':
			return isFinite(value) ? String(value) : 'null';
		case 'boolean':
		case 'null':
			return String(value);
		case 'object':
			if (!value) {
				return 'null';
			}
			gap += indent;
			partial = [];
			if (Object.prototype.toString.apply(value) === '[object Array]') {
				length = value.length;
				for (i = 0; i < length; i += 1) {
					partial[i] = str(i, value) || 'null';
				}
				v = partial.length === 0 ? '[]' : gap ? '[\n' + gap
						+ partial.join(',\n' + gap) + '\n' + mind + ']' : '['
						+ partial.join(',') + ']';
				gap = mind;
				return v;
			}
			if (rep && typeof rep === 'object') {
				length = rep.length;
				for (i = 0; i < length; i += 1) {
					if (typeof rep[i] === 'string') {
						k = rep[i];
						v = str(k, value);
						if (v) {
							partial.push(quote(k) + (gap ? ': ' : ':') + v);
						}
					}
				}
			} else {
				for (k in value) {
					if (Object.prototype.hasOwnProperty.call(value, k)) {
						v = str(k, value);
						if (v) {
							partial.push(quote(k) + (gap ? ': ' : ':') + v);
						}
					}
				}
			}
			v = partial.length === 0 ? '{}' : gap ? '{\n' + gap
					+ partial.join(',\n' + gap) + '\n' + mind + '}' : '{'
					+ partial.join(',') + '}';
			gap = mind;
			return v;
		}
	}
	if (typeof JSON.stringify !== 'function') {
		escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;
		meta = {
			'\b' : '\\b',
			'\t' : '\\t',
			'\n' : '\\n',
			'\f' : '\\f',
			'\r' : '\\r',
			'"' : '\\"',
			'\\' : '\\\\'
		};
		JSON.stringify = function(value, replacer, space) {
			var i;
			gap = '';
			indent = '';
			if (typeof space === 'number') {
				for (i = 0; i < space; i += 1) {
					indent += ' ';
				}
			} else if (typeof space === 'string') {
				indent = space;
			}
			rep = replacer;
			if (replacer
					&& typeof replacer !== 'function'
					&& (typeof replacer !== 'object' || typeof replacer.length !== 'number')) {
				throw new Error('JSON.stringify');
			}
			return str('', {
				'' : value
			});
		};
	}
	if (typeof JSON.parse !== 'function') {
		cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;
		JSON.parse = function(text, reviver) {
			var j;
			function walk(holder, key) {
				var k, v, value = holder[key];
				if (value && typeof value === 'object') {
					for (k in value) {
						if (Object.prototype.hasOwnProperty.call(value, k)) {
							v = walk(value, k);
							if (v !== undefined) {
								value[k] = v;
							} else {
								delete value[k];
							}
						}
					}
				}
				return reviver.call(holder, key, value);
			}
			text = String(text);
			cx.lastIndex = 0;
			if (cx.test(text)) {
				text = text.replace(cx,
						function(a) {
							return '\\u'
									+ ('0000' + a.charCodeAt(0).toString(16))
											.slice(-4);
						});
			}
			if (/^[\],:{}\s]*$/
					.test(text
							.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@')
							.replace(
									/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
									']').replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
				j = eval('(' + text + ')');
				return typeof reviver === 'function' ? walk({
					'' : j
				}, '') : j;
			}
			throw new SyntaxError('JSON.parse');
		};
	}
}());
qcVideo('JSON', function() {
	return JSON;
});
;
qcVideo(
		'util',
		function() {
			var util = {
				paramsToObject : function(link) {
					var result = {}, pairs, pair, query, key, value;
					query = link || '';
					query = query.replace('?', '');
					pairs = query.split('&');
					for (var i = 0, j = pairs.length; i < j; i++) {
						var keyVal = pairs[i];
						pair = keyVal.split('=');
						key = pair[0];
						value = pair.slice(1).join('=');
						result[decodeURIComponent(key)] = decodeURIComponent(value);
					}
					return result;
				},
				each : function(opt, cb) {
					var key = 0;
					if (this.isArray(opt)) {
						while (opt[key]) {
							if (false === cb.call(opt[key], opt[key], key)) {
								return;
							}
							key += 1;
						}
					} else if (this.isPlainObject(opt)) {
						for (key in opt) {
							if (false === cb.call(opt[key], opt[key], key)) {
								return;
							}
						}
					}
				}
			};
			var toString = Object.prototype.toString, hasOwn = Object.prototype.hasOwnProperty, class2type = {
				'[object Boolean]' : 'boolean',
				'[object Number]' : 'number',
				'[object String]' : 'string',
				'[object Function]' : 'function',
				'[object Array]' : 'array',
				'[object Date]' : 'date',
				'[object RegExp]' : 'regExp',
				'[object Object]' : 'object'
			}, isWindow = function(obj) {
				return obj && typeof obj === "object" && "setInterval" in obj;
			};
			util.type = function(obj) {
				return obj == null ? String(obj) : class2type[toString
						.call(obj)]
						|| "object";
			};
			util.isArray = Array.isArray || function(obj) {
				return util.type(obj) === "array";
			};
			util.isPlainObject = function(obj) {
				if (!obj || util.type(obj) !== "object" || obj.nodeType
						|| isWindow(obj)) {
					return false;
				}
				if (obj.constructor
						&& !hasOwn.call(obj, "constructor")
						&& !hasOwn.call(obj.constructor.prototype,
								"isPrototypeOf")) {
					return false;
				}
				var key;
				for (key in obj) {
				}
				return key === undefined || hasOwn.call(obj, key);
			};
			util.merge = function(tar, sou, deep) {
				var name, src, copy, clone, copyIsArray;
				for (name in sou) {
					src = tar[name];
					copy = sou[name];
					if (tar !== copy) {
						if (deep
								&& copy
								&& (util.isPlainObject(copy) || (copyIsArray = util
										.isArray(copy)))) {
							if (copyIsArray) {
								copyIsArray = false;
								clone = src && util.isArray(src) ? src : [];
							} else {
								clone = src && util.isPlainObject(src) ? src
										: {};
							}
							tar[name] = util.merge(clone, copy, deep);
						} else if (copy !== undefined) {
							tar[name] = copy;
						}
					}
				}
				return tar;
			};
			return util;
		});
;
qcVideo(
		'constants',
		function() {
			return {
				SERVER_API : "http://play.video.qcloud.com/index.php",
				SERVER_API_PARAMS : {
					"file_id" : 1,
					"app_id" : 1,
					"player_id" : 1,
					"refer" : 1
				},
				REPORT_API : "http://stat.video.qcloud.com/kvcollect",
				REPORT_DATA : {
					dcid : "dc00551",
					plat : "h5",
					appid : "",
					fileid : "",
					playerid : "",
					loadingtime : 0,
					blockcount : 0,
					blocktime : 0,
					seekcount : 0,
					seekblocktime : 0,
					errorcode : 0,
					step : 1,
					ver : ""
				},
				OK_CODE : '0',
				ERROR_CODE : {
					TIME_OUT : '10000',
					REQUIRE_PWD : '11046',
					ERROR_PW : '1003',
					REQUIRE_APP_ID : '11044',
					ERROR_APP_ID : '10008',
					REQUIRE_FID : '11045',
					ERROR_FID : '10008'
				},
				ERROR_MSG : {
					'10000' : '\u8bf7\u6c42\u8d85\u65f6%2c\u8bf7\u68c0\u67e5\u7f51\u7edc\u8bbe\u7f6e',
					'11046' : '\u5bc6\u7801\u9519\u8bef\uff0c\u8bf7\u91cd\u65b0\u8f93\u5165',
					'1003' : '\u5bc6\u7801\u9519\u8bef\uff0c\u8bf7\u91cd\u65b0\u8f93\u5165'
				},
				DEFINITION_PRIORITY : {
					0 : [ 10, 110 ],
					1 : [ 20, 120 ],
					2 : [ 30, 130 ],
					4 : [ 40 ]
				},
				RESOLUTION_PRIORITY : [ 2, 1, 0, 4 ],
				DEFINITION_NAME : {
					0 : '手机',
					1 : '标清',
					2 : '高清',
					4 : '超清'
				},
				LOGO_LOCATION : {
					L_U : '0',
					L_D : '1',
					R_U : '2',
					R_D : '3'
				},
				PATCH_TYPE : {
					IMAGE : '0',
					MOVE : '1'
				},
				PATCH_LOC : {
					START : '0',
					PAUSE : '1',
					END : '2'
				},
				TAP : 'tap',
				CLICK : 'click'
			};
		});
;
qcVideo(
		'Base',
		function(util) {
			var unique = 'base_' + (+new Date()), global = window, uuid = 1, Base = function() {
			}, debug = true, realConsole = global.console, console = realConsole
					|| {}, wrap = function(fn) {
				return function() {
					if (debug) {
						try {
							var className = this['className'] || 'BASE';
							fn.apply(realConsole, [ className + ':' ]
									.concat(arguments));
						} catch (xe) {
						}
					}
				}
			};
			Base.prototype.loop = Base.loop = function() {
			};
			Base.extend = function(protoProps, staticProps) {
				protoProps = protoProps || {};
				var constructor = protoProps.hasOwnProperty('constructor') ? protoProps.constructor
						: function() {
							return sup.apply(this, arguments);
						};
				var sup = this;
				var Fn = function() {
					this.constructor = constructor;
				};
				Fn.prototype = sup.prototype;
				constructor.prototype = new Fn();
				util.merge(constructor.prototype, protoProps);
				util.merge(constructor, sup, true);
				util.merge(constructor, staticProps);
				util.merge(constructor, {
					__super__ : sup.prototype
				});
				return constructor;
			};
			Base.prototype.log = wrap(console.log || Base.loop);
			Base.prototype.debug = wrap(console.debug || Base.loop);
			Base.prototype.error = wrap(console.error || Base.loop);
			Base.prototype.info = wrap(console.info || Base.loop);
			var eventCache = {};
			var getUniqueId = function() {
				return this.__id || (this.__id = unique + (++uuid));
			};
			var initEvent = function(ctx, event) {
				var id = getUniqueId.call(ctx);
				if (!eventCache.hasOwnProperty(id)) {
					eventCache[id] = {};
				}
				if (event) {
					if (!eventCache[id][event]) {
						eventCache[id][event] = [];
					}
				}
			};
			Base.prototype.on = function(ctx, event, fn) {
				initEvent(ctx, event);
				eventCache[getUniqueId.call(ctx)][event].push(fn);
			};
			Base.prototype.fire = function(event, opt) {
				util.each((eventCache[getUniqueId.call(this)] || {})[event]
						|| [], function(fn) {
					fn.call(global, opt);
				});
			};
			Base.prototype.off = function(ctx, event, fn) {
				initEvent(ctx);
				var find = -1, list = eventCache[getUniqueId.call(ctx)][event];
				util.each(list, function(handler, index) {
					if (handler === fn) {
						find = index;
						return false;
					}
				});
				if (find !== -1) {
					list.splice(find, 1);
				}
			};
			Base.instance = function(opt, staticOpt) {
				return new (Base.extend(opt, staticOpt))();
			};
			return Base;
		});
qcVideo('css', function() {
	var css = {};
	if (document.defaultView && document.defaultView.getComputedStyle) {
		css.getComputedStyle = function(a, b) {
			var c, d, e;
			b = b.replace(/([A-Z]|^ms)/g, "-$1").toLowerCase();
			if ((d = a.ownerDocument.defaultView)
					&& (e = d.getComputedStyle(a, null))) {
				c = e.getPropertyValue(b)
			}
			return c
		}
	} else if (document.documentElement.currentStyle) {
		css.getComputedStyle = function(a, b) {
			var c, d = a.currentStyle && a.currentStyle[b], e = a.style;
			if (d === null && e && (c = e[b])) {
				d = c
			}
			return d
		}
	}
	return {
		getWidth : function(e) {
			return (css.getComputedStyle(e, 'width') || "").toLowerCase()
					.replace('px', '') | 0;
		},
		getHeight : function(e) {
			return (css.getComputedStyle(e, 'height') || "").toLowerCase()
					.replace('px', '') | 0;
		},
		textAlign : function(e) {
			e.style['text-align'] = 'center';
		}
	};
});
;
qcVideo(
		'H5',
		function(constants, api, util, Base, config, startup_tpl) {
			function getFinalVideos(o, source) {
				if (o && o.length) {
					var map = {}, ai = 0, al = o.length;
					for (; ai < al; ai++) {
						map[o[ai].split('?')[0]] = o[ai];
					}
					if (source && source.length) {
						var finalVideos = [], vi = 0, vl = source.length, st;
						for (; vi < vl; vi++) {
							st = source[vi]['url'].split('?')[0];
							if (map.hasOwnProperty(st)) {
								source[vi]['url'] = map[st];
								finalVideos.push(source[vi]);
							}
						}
						return finalVideos;
					}
				}
				return null;
			}
			var verifyDone = function(data) {
				var me = this;
				util.merge(me.store, data, true);
				util.merge(me.store, {
					'parameter' : me.option
				});
				me.loading(true);
				if (config.h5player.indexOf('?') === -1) {
					config
							.set('h5player', config.h5player
									+ '?max_age=20000000&swfv='
									+ data['version']['h5']);
				}
				qcVideo.use('h5player', function(mod) {
					me.loading();
					mod['render'](me.store);
				});
			};
			var $;
			return Base
					.extend({
						askDoor : function(firstTime, pass) {
							var me = this, store = me.store, key, address = constants.SERVER_API
									+ '?interface=Vod_Api_GetPlayInfo&1=1';
							for (key in constants.SERVER_API_PARAMS) {
								if (store.hasOwnProperty(key)) {
									address += '&' + key + '=' + store[key];
								}
							}
							if (pass !== undefined) {
								address += '&pass=' + pass;
							}
							me.loading(true);
							api
									.request(
											address,
											function(ret) {
												me.loading();
												var code = ret['retcode'] + '', data = ret['data'];
												if (code == constants.OK_CODE) {
													if (me.store.videos
															&& me.store.videos.length) {
														var finalVideos = getFinalVideos(
																me.store.videos,
																data.file_info.image_video.videoUrls);
														if (finalVideos) {
															data.file_info.image_video.videoUrls = finalVideos;
														}
													}
													verifyDone.call(me, data);
												} else {
													if ((code == constants.ERROR_CODE.REQUIRE_PWD || code == constants.ERROR_CODE.ERROR_PW)
															&& firstTime) {
														me.renderPWDPanel();
													} else {
														me
																.erTip(
																		constants.ERROR_MSG[code]
																				|| 'error code:('
																				+ code
																				+ ') ',
																		true);
													}
												}
											});
						},
						askVersion : function(tv) {
							var me = this, data = {
								file_info : {
									duration : tv.duration,
									image_video : {
										videoUrls : []
									}
								},
								player_info : {}
							};
							for ( var key in tv.urls) {
								data.file_info.image_video.videoUrls.push({
									'definition' : key,
									'url' : tv.urls[key]
								});
							}
							var address = constants.SERVER_API
									+ '?interface=Vod_Api_GetVersion&1=1';
							me.loading(true);
							api
									.request(
											address,
											function(ret) {
												me.loading();
												var code = ret['retcode'] + '', rdata = ret['data'];
												if (code == constants.OK_CODE) {
													data.version = rdata.version;
													verifyDone.call(me, data);
												} else {
													me
															.erTip(
																	constants.ERROR_MSG[code]
																			|| 'error code:('
																			+ code
																			+ ') ',
																	true);
												}
											});
						},
						className : 'PlayerH5',
						$pwd : null,
						$out : null,
						option : {},
						constructor : function(_$, targetId, opt) {
							$ = _$;
							var me = this, node = document
									.createElement("link"), defaultV = '20150508';
							me.option = opt;
							node.href = config.h5css;
							node.rel = "stylesheet";
							node.media = "screen";
							document.getElementsByTagName("head")[0]
									.appendChild(node);
							me.store = util.merge({
								"$renderTo" : $('#' + targetId),
								"version" : {
									"h5" : defaultV,
									"flash" : defaultV,
									"android" : defaultV,
									"ios" : defaultV
								}
							}, opt);
							var $out = me.$out = me.store.$renderTo
									.html(startup_tpl['main']
											({
												sure : '\u786e\u5b9a',
												errpass : '\u62b1\u6b49\uff0c\u5bc6\u7801\u9519\u8bef',
												enterpass : '\u8bf7\u8f93\u5165\u5bc6\u7801',
												videlocked : '\u8be5\u89c6\u9891\u5df2\u52a0\u5bc6'
											}));
							$out.find('[data-area="main"]').css({
								width : me.store.width,
								height : me.store.height
							});
							me.$pwd = $out.find('[data-area="pwd"]');
							if (!!opt.third_video && !!opt.third_video.urls
									&& !!opt.third_video.duration) {
								me.askVersion(opt.third_video);
							} else {
								me.askDoor(true);
							}
						},
						loading : function(visible) {
						},
						erTip : function(msg, pwdEr) {
							if (pwdEr) {
								this.$pwd.find('.txt').text(msg).css(
										'visibility', 'visible');
							}
						},
						sureHandler : function() {
							var me = this, $pwd = me.$pwd, pwd = $pwd.find(
									'input[type="password"]').val()
									+ '', able = pwd.length > 0;
							$pwd
									.find('.txt')
									.text(
											able ? ''
													: '\u62b1\u6b49\uff0c\u5bc6\u7801\u9519\u8bef')
									.css('visibility',
											(able ? 'hidden' : 'visible'));
							if (able) {
								me.askDoor(false, pwd);
							}
						},
						renderPWDPanel : function() {
							var me = this, cw = me.store.width, ch = me.store.height, $pwd = me.$pwd, $parent = $pwd
									.parent();
							$pwd
									.show()
									.on(
											'click',
											'[tx-act]',
											function(e) {
												var act = $(this)
														.attr('tx-act'), handler = me[act
														+ 'Handler'];
												handler && handler.call(me);
												e.stopPropagation();
												return false;
											});
							var pw = $pwd.width(), ph = $pwd.height(), fW = $parent
									.width();
							if (fW && fW <= pw) {
								$pwd.css({
									'left' : '0px',
									'top' : '0px'
								}).width(fW);
							} else {
								$pwd.css({
									'left' : (cw - pw) / 2 + 'px',
									'top' : (ch - ph) / 2 + 'px'
								});
							}
						}
					});
		});
;
qcVideo(
		'Player',
		function(util, Base, version, css, H5, Swf, SwfJsLink) {
			var eidUuid = 10000000;
			function getEid() {
				return 'video_' + (eidUuid++);
			}
			function setSuitableWH(opt, ele) {
				var width = opt.width, height = opt.height, pW = css
						.getWidth(ele) - 4, pH = css.getHeight(ele) - 4, rate = width
						/ height;
				if (pW < 0) {
					pW = width;
				}
				if (pH < 0) {
					pH = height;
				}
				if (pW < width) {
					width = pW;
					height = width / rate;
				}
				if (pH < height) {
					height = pH;
					width = height * rate;
				}
				opt.width = width - 0;
				opt.height = height - 0;
			}
			return Base
					.extend({
						className : 'Player',
						constructor : function(targetId, opt, listener) {
							if (util.isPlainObject(targetId)) {
								var tmp = opt;
								opt = targetId;
								targetId = tmp;
							}
							var verifyDone = function() {
								this.targetId = targetId;
								setSuitableWH(opt, ele);
								var ver = version.getPriority(), eid = getEid();
								if (ver == 'h5') {
									qcVideo.use('$', function(mod) {
										new H5(mod, targetId, opt);
									});
								} else if (ver == 'flash') {
									new Swf(targetId, eid, opt);
								} else {
									ele.innerText = '当前浏览器不能支持视频播放，可下载最新的QQ浏览器或者安装FLash即可播放';
									return;
								}
								this.targetId = targetId;
								css.textAlign(ele);
								return new SwfJsLink(eid, listener);
							};
							var ele = document.getElementById(targetId);
							opt['refer'] = document.domain;
							if (!targetId || !ele) {
								alert("没有指定有效播放器容器！");
							} else if (!opt['app_id'] || !opt['file_id']) {
								var video = opt['third_video'];
								if (video) {
									if (!video['duration']) {
										return alert("缺少视频时长参数，请补齐duration；");
									} else if (!video['urls']) {
										return alert("缺少视频地址信息，请补齐urls；");
									} else {
										return verifyDone();
									}
								}
								alert("缺少参数，请补齐appId，file_id");
							} else {
								return verifyDone.call(this);
							}
						},
						remove : function() {
							if (this.targetId) {
								document.getElementById(this.targetId).innerHTML = '';
							}
						}
					});
		});
;
qcVideo('startup', function(Base, Player) {
	return Base.instance({
		className : 'startup',
		constructor : Base.loop,
		start : function(targetId, opt) {
			new Player(targetId, opt);
		}
	});
});
qcVideo(
		'startup_tpl',
		function() {
			return {
				'main' : function(data) {
					var __p = [], _p = function(s) {
						__p.push(s)
					};
					_p('<div data-area="main" style="position: relative;background-color: #000;">\r\n\
           <div class="layer-password" data-area="pwd" style="display:none;">\r\n\
               <span class="tip" style="border: none;background-color: #242424;border-bottom: 1px solid #0073d0;position: relative;">');
					_p(data.videlocked);
					_p('</span>\r\n\
               <input class="password" placeholder="');
					_p(data.enterpass);
					_p('" type="password">\r\n\
               <span class="txt">');
					_p(data.errpass);
					_p('</span>\r\n\
               <div class="bottom">\r\n\
                   <a class="btn ok" href="#" tx-act="sure">');
					_p(data.sure);
					_p('</a>\r\n\
               </div>\r\n\
           </div>\r\n\
           <div data-area="loading" style="display:none;">\r\n\
               loading....\r\n\
           </div>\r\n\
    </div>');
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
qcVideo(
		'Swf',
		function(Base, config, JSON) {
			var getHtmlCode = function(option, eid) {
				var __ = [], address = config.flash, _ = function(str) {
					__.push(str);
				};
				var flashvars = 'auto_play=' + option.auto_play + '&file_id='
						+ option.file_id + '&app_id=' + option.app_id
						+ '&version=1&refer=' + option.refer + '&jscbid=' + eid;
				var VMode = option.VMode || 'window';
				flashvars += !!option.disable_full_screen ? '&disable_full_screen=1'
						: '&disable_full_screen=0';
				flashvars += !!option.debug ? '&debug=1' : '';
				if (option.definition !== undefined) {
					flashvars += '&definition=' + option.definition;
				}
				if (option.player_id !== undefined) {
					flashvars += '&player_id=' + option.player_id;
				}
				if (option.disable_drag !== undefined) {
					flashvars += '&disable_drag=' + option.disable_drag;
				}
				if (option.stretch_full !== undefined) {
					flashvars += '&stretch_full=' + option.stretch_full;
				}
				if (option.videos && option.videos.length) {
					flashvars += '&videos='
							+ encodeURIComponent(JSON.stringify(option.videos));
				}
				if (!!option.third_video) {
					flashvars += '&third_video='
							+ encodeURIComponent(JSON
									.stringify(option.third_video));
				}
				if (option.skin) {
					flashvars += '&skin='
							+ encodeURIComponent(JSON.stringify(option.skin));
				}
				_('<object id="' + eid + '_object" width="' + option.width
						+ 'px" height="' + option.height + 'px"');
				_('align="middle" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab#version=10,0,0,0">');
				_('<param name="FlashVars" value="' + flashvars + '"  />');
				_('<param name="Movie" value="#"  />');
				_('<param name="Src" value="' + address + '"  />');
				_('<param name="WMode" value="' + VMode + '"/>');
				_('<param name="Quality" value="High"/>');
				_('<param name="AllowScriptAccess" value="always"/>');
				_('<param name="AllowNetworking" value="all"/>');
				_('<param name="AllowFullScreen" value="true"/>');
				_('<embed id="' + eid + '_embed" width="' + option.width
						+ 'px" height="' + option.height + 'px" flashvars="'
						+ flashvars + '"');
				_('align="middle" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" allowfullscreen="true" bgcolor="#000000" quality="high"');
				_('src="' + address + '"');
				_('wmode="'
						+ VMode
						+ '" allowfullscreen="true" invokeurls="false" allownetworking="all" allowscriptaccess="always">');
				_('</object>');
				return __.join('');
			};
			return Base
					.extend({
						className : 'PlayerSwf',
						option : null,
						constructor : function(targetId, eid, opt) {
							document.getElementById(this.targetId = targetId).innerHTML = getHtmlCode(
									opt, eid);
						},
						remove : function() {
							var node = document.getElementById(this.targetId)
									|| {}, parent = node.parentNode;
							if (node.parentNode
									&& (node.parentNode.tagName || '')
											.toLowerCase() == 'object') {
								node = parent;
								parent = node.parentNode;
							}
							try {
								parent.removeChild(node);
							} catch (xe) {
							}
						}
					});
		});
;
qcVideo('SwfJsLink', function(util, JSON) {
	var global = window;
	var cap = function(str) {
		return str.replace(/(\w)/, function(v) {
			return v.toUpperCase()
		});
	};
	var tryIt = function(fn) {
		return function() {
			try {
				return fn.apply(this, arguments);
			} catch (xe) {
				return '0';
			}
		};
	};
	var pixesToInt = function(str) {
		return (str ? str + '' : '').replace('px', '') | 0;
	};
	var SwfJsLink = function(id, listeners) {
		var me = this;
		me.id = id;
		me.tecGet = id + '_tecGet';
		me.operate = id + '_operate';
		me.source = id + '_source';
		me.listeners = {};
		var type = util.type(listeners);
		if (!listeners || type == 'function') {
			me.listeners['playStatus'] = listeners || function() {
			};
		} else if (type == 'object') {
			util.merge(me.listeners, listeners)
		}
		global[id + '_callback'] = function(cmd) {
			var cmds = cmd.split(':'), key = cmds[0];
			if (me.listeners.hasOwnProperty(key)) {
				switch (key) {
				case ('playStatus'):
					me.listeners[key](cmds[1]);
					break;
				case ('fullScreen'):
					me.listeners[key](cmds[1] == '1');
					break;
				case ('dragPlay'):
					me.listeners[key](cmds[1]);
					break;
				}
			}
		}
	};
	util.each(
			[ 'volume', 'duration', 'currentTime', 'clarity', 'allClaritys' ],
			function(name) {
				SwfJsLink.prototype['get' + cap(name)] = (function(name) {
					return function() {
						try {
							var ret = this.getSwf()[this.tecGet](name);
							if (name == 'currentTime') {
								ret = ret | 0;
							}
							return ret;
						} catch (xe) {
							return '';
						}
					}
				})(name);
			});
	util.each([ 'seeking', 'suspended', 'playing', 'playEnd' ], function(name) {
		SwfJsLink.prototype['is' + cap(name)] = (function(name) {
			return function() {
				try {
					var state = this.getSwf()[this.tecGet]('playState');
					if (state == name) {
						return true;
					}
				} catch (xe) {
				}
				return false;
			}
		})(name)
	});
	util.merge(SwfJsLink.prototype,
			{
				getSwf : function() {
					var me = this;
					if (!me.swf) {
						try {
							if (!!global[this.id + '_object'][this.tecGet]) {
								this.swf = global[this.id + '_object'];
							} else {
								this.swf = global[this.id + '_embed'];
							}
						} catch (xe) {
							return {};
						}
					}
					return this.swf;
				},
				resize : function(w, h) {
					var swf = this.getSwf();
					if (swf) {
						swf.width = pixesToInt(w);
						swf.height = pixesToInt(h);
					}
				},
				getWidth : function() {
					return pixesToInt(this.getSwf().width);
				},
				getHeight : function() {
					return pixesToInt(this.getSwf().height);
				},
				pause : tryIt(function() {
					return this.getSwf()[this.operate]('pause');
				}),
				resume : tryIt(function() {
					return this.getSwf()[this.operate]('resume');
				}),
				play : tryIt(function(time) {
					return this.getSwf()[this.operate]('play', time | 0);
				}),
				setClarity : tryIt(function(clarity) {
					return this.getSwf()[this.operate]('clarity', -1, clarity);
				}),
				setFullScreen : tryIt(function(fullScreen) {
					if (!!fullScreen) {
						return this.getSwf()[this.operate]('openfullscreen');
					} else {
						return this.getSwf()[this.operate]('cancelfullscreen');
					}
				}),
				changeVideo : tryIt(function(opt) {
					return this.getSwf()[this.source](opt['file_id'],
							opt['app_id'] || '', !!opt.videos
									&& opt.videos.length ? JSON
									.stringify(opt.videos) : '',
							!!opt.third_video ? JSON.stringify(opt.third_video)
									: '', opt['auto_play'] ? 1 : 0);
				}),
				remove : tryIt(function() {
					var swf = this.getSwf();
					var parent = swf.parentNode;
					parent.removeChild(swf);
					if ((parent.tagName || '').toLowerCase() == 'object') {
						parent.parentNode.removeChild(parent);
					}
				}),
				isFullScreen : function() {
					try {
						return this.getSwf()[this.tecGet]('fullscreen') == "1";
					} catch (xe) {
					}
					return false;
				}
			});
	return SwfJsLink;
});
;
qcVideo(
		'version',
		function() {
			var agent = navigator.userAgent;
			var v = {
				IOS : agent.match(/iP(od|hone|ad)/i),
				ANDROID : (/Android/i).test(agent)
			};
			if (v.IOS) {
				v.VERSION = (function() {
					var match = agent.match(/OS (\d+)_/i);
					if (match && match[1]) {
						return match[1];
					}
				})();
			}
			if (v.ANDROID) {
				v.VERSION = (function() {
					var match = agent
							.match(/Android (\d+)(?:\.(\d+))?(?:\.(\d+))*/i), major, minor;
					if (!match) {
						return null;
					}
					major = match[1] && parseFloat(match[1]);
					minor = match[2] && parseFloat(match[2]);
					if (major && minor) {
						return parseFloat(match[1] + '.' + match[2]);
					} else if (major) {
						return major;
					} else {
						return null;
					}
				})();
			}
			v.ABLE_H5 = (function() {
				var dom = document.createElement("video");
				if (dom.canPlayType) {
					var mp4 = dom.canPlayType("video/mp4");
					if ("probably" == mp4 || "maybe" == mp4) {
						return true;
					}
				}
			})();
			v.ABLE_FLASH = (function() {
				var swf;
				if (document.all)
					try {
						swf = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
						if (swf) {
							return !0;
						}
					} catch (e) {
						return !1;
					}
				else
					try {
						if (navigator.plugins && navigator.plugins.length > 0) {
							swf = navigator.plugins["Shockwave Flash"];
							if (swf) {
								return !0;
							}
						}
					} catch (e) {
						return !1;
					}
				return !1;
			})();
			v.getPriority = function() {
				if (v.IOS || v.ANDROID) {
					return 'h5';
				}
				if (!v.ABLE_FLASH && v.ABLE_H5) {
					return 'h5';
				}
				return v.ABLE_FLASH ? 'flash' : v.ABLE_H5 ? 'h5' : '';
			};
			return v;
		});
qcVideo.use("Player", function(mod) {
	qcVideo.Player = mod;
});/* |xGv00|4e232fae6f7e94b26d07ad7d27826ed2 */