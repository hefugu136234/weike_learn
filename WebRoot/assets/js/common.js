var _hmt = _hmt || [];
(function() {
	var hm = document.createElement("script");
	hm.src = "//hm.baidu.com/hm.js?5ac632ebae682d95930f407bb3d186ca";
	var s = document.getElementsByTagName("script")[0];
	s.parentNode.insertBefore(hm, s);
})();

function isBlank(str) {
	return (!str || /^\s*$/.test(str));
}

function traceValue(obj, exp, def) {
	if (isBlank(def)) {
		def = '';
	}
	if (isBlank(obj) || isBlank(exp))
		return def;
	var ex = exp.split('.');
	var tmp = obj;
	for (var i = 0; i < ex.length; i++) {
		tmp = tmp[ex[i]];
		if (typeof (tmp) == 'undefined') {
			return def;
		}
	}
	return tmp;
}

Date.prototype.customFormat = function(formatString) {
	var YYYY, YY, MMMM, MMM, MM, M, DDDD, DDD, DD, D, hhhh, hhh, hh, h, mm, m, ss, s, ampm, AMPM, dMod, th;
	var dateObject = this;
	YY = ((YYYY = dateObject.getFullYear()) + "").slice(-2);
	MM = (M = dateObject.getMonth() + 1) < 10 ? ('0' + M) : M;
	MMM = (MMMM = [ "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" ][M - 1])
			.substring(0, 3);
	DD = (D = dateObject.getDate()) < 10 ? ('0' + D) : D;
	DDD = (DDDD = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
			"Friday", "Saturday" ][dateObject.getDay()]).substring(0, 3);
	th = (D >= 10 && D <= 20) ? 'th' : ((dMod = D % 10) == 1) ? 'st'
			: (dMod == 2) ? 'nd' : (dMod == 3) ? 'rd' : 'th';
	formatString = formatString.replace("#YYYY#", YYYY).replace("#YY#", YY)
			.replace("#MMMM#", MMMM).replace("#MMM#", MMM).replace("#MM#", MM)
			.replace("#M#", M).replace("#DDDD#", DDDD).replace("#DDD#", DDD)
			.replace("#DD#", DD).replace("#D#", D).replace("#th#", th);

	h = (hhh = dateObject.getHours());
	if (h == 0)
		h = 24;
	if (h > 12)
		h -= 12;
	hh = h < 10 ? ('0' + h) : h;
	hhhh = hhh < 10 ? ('0' + hhh) : hhh;
	AMPM = (ampm = hhh < 12 ? 'am' : 'pm').toUpperCase();
	mm = (m = dateObject.getMinutes()) < 10 ? ('0' + m) : m;
	ss = (s = dateObject.getSeconds()) < 10 ? ('0' + s) : s;
	return formatString.replace("#hhhh#", hhhh).replace("#hhh#", hhh).replace(
			"#hh#", hh).replace("#h#", h).replace("#mm#", mm).replace("#m#", m)
			.replace("#ss#", ss).replace("#s#", s).replace("#ampm#", ampm)
			.replace("#AMPM#", AMPM);
}
/*
 * description: Use customFormat(...) to format a Date object in anyway you
 * like. Any token (from the list below) gets replaced with the corresponding
 * value.
 * 
 * 
 * token: description: example: #YYYY# 4-digit year 1999 #YY# 2-digit year 99
 * #MMMM# full month name February #MMM# 3-letter month name Feb #MM# 2-digit
 * month number 02 #M# month number 2 #DDDD# full weekday name Wednesday #DDD#
 * 3-letter weekday name Wed #DD# 2-digit day number 09 #D# day number 9 #th#
 * day ordinal suffix nd #hhhh# 2 digit military hour 17 #hhh# military/24-based
 * hour 17 #hh# 2-digit hour 05 #h# hour 5 #mm# 2-digit minute 07 #m# minute 7
 * #ss# 2-digit second 09 #s# second 9 #ampm# "am" or "pm" pm #AMPM# "AM" or
 * "PM" PM
 * 
 * 
 * notes: If you want the current date and time, use "new Date()". e.g. var
 * curDate = new Date(); curDate.customFormat("#DD#/#MM#/#YYYY#");
 * 
 * 
 * If you want all-lowercase or all-uppercase versions of the weekday/month, use
 * the toLowerCase() or toUpperCase() methods of the resulting string. e.g.
 * curDate.customFormat("#DDDD#, #MMMM# #D#, #YYYY#").toLowerCase();
 * 
 * If you use the same format in many places in your code, I suggest creating a
 * wrapper function to this one, e.g.: Date.prototype.myDate=function(){ return
 * this.customFormat("#D#-#MMM#-#YYYY#").toLowerCase(); }
 * Date.prototype.myTime=function(){ return this.customFormat("#h#:#mm##ampm#"); }
 * var now = new Date(); alert(now.myDate());
 */