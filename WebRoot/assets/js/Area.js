
function initComplexArea(a, k, h, p, q, d, b, l) {
    var f = initComplexArea.arguments;
    var m = document.getElementById(a);
    var o = document.getElementById(k);
    //var n = document.getElementById(h);
    var e = 0;
    var c = 0;
    if (p != undefined) {
        if (d != undefined) {
            d = parseInt(d);
        }
        else {
            d = 0;
        }
        if (b != undefined) {
            b = parseInt(b);
        }
        else {
            b = 0;
        }
        if (l != undefined) {
            l = parseInt(l);
        }
        else {
            l = 0
        }
        //n[0] = new Option("请选择 ", 0);
        for (e = 0; e < p.length; e++) {
            if (p[e] == undefined) {
                continue;
            }
            if (f[6]) {
                if (f[6] == true) {
                    if (e == 0) {
                        continue
                    }
                }
            }
            m[c] = new Option(p[e], e);
            if (d == e) {
                m[c].selected = true;
            }
            c++
        }
        if (q[d] != undefined) {
            c = 0; for (e = 0; e < q[d].length; e++) {
                if (q[d][e] == undefined) { continue }
                if (f[6]) {
                    if ((f[6] == true) && (d != 71) && (d != 81) && (d != 82)) {
                        if ((e % 100) == 0) { continue }
                    }
                } o[c] = new Option(q[d][e], e);
                if (b == e) { o[c].selected = true } c++
            }
        }else{
			o[0] = new Option("请选择 ", 0);
		}
    }
}
function changeComplexProvince(f, k, e, d,pri_text) {
    var c = changeComplexProvince.arguments; var h = document.getElementById(e);
    var province=document.getElementById(pri_text);
    var g = document.getElementById(d); var b = 0; var a = 0; removeOptions(h); f = parseInt(f);
    //加载市
    if(area_array[f]!=undefined){
    	province.value=area_array[f];
    }else{
    	province.value='';
    }
    if (k[f] != undefined) {
        for (b = 0; b < k[f].length; b++) {
            if (k[f][b] == undefined) { continue }
            if (c[3]) { if ((c[3] == true) && (f != 71) && (f != 81) && (f != 82)) { if ((b % 100) == 0) { continue } } }
            h[a] = new Option(k[f][b], b); a++
        }
    }
	else{
		 h[0] = new Option("请选择 ", 0);
	}
/*    removeOptions(g); g[0] = new Option("请选择 ", 0);
    if (f == 11 || f == 12 || f == 31 || f == 71 || f == 50 || f == 81 || f == 82) {
        if ($("#" + d + "_div"))
        { $("#" + d + "_div").hide(); }
    }
    else {
        if ($("#" + d + "_div")) { $("#" + d + "_div").show(); }
    }
	*/
}

 
function changeCity(c, a) {
//    $("#" + a).html('<option value="0" >请选择</option>');
//    $("#" + a).unbind("change");
	var city_text=document.getElementById(a);
	if(c.length==4){
		var index1 = c.substring(0, 2);
		index1 = parseInt(index1); 
		c = parseInt(c);
		var city=sub_array[index1][c];
		if(city==undefined){
			city_text.value='';
		}else{
			city_text.value=city;
		}
		
	}else{
		city_text.value='';
	}
//    var str = "";     
//    str += "<option value='0' >请选择</option>";
//    for (var i = c * 100; i < _d.length; i++) {
//        if (_d[i] == undefined) continue; 
//        str += "<option value='" + i + "' >" + _d[i] + "</option>";
//    }
//    $("#" + a).html(str);
    
}

function removeOptions(c) {
    if ((c != undefined) && (c.options != undefined)) {
        var a = c.options.length;
        for (var b = 0; b < a; b++) {
            c.options[0] = null;
        }
    }
}
