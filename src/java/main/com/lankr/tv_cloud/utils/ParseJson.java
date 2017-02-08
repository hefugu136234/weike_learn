package com.lankr.tv_cloud.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ParseJson {

	public static void main(String[] args) throws JsonSyntaxException,
			JsonIOException, FileNotFoundException,
			UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String path = "json.json";
		String json = getJsonVal(path);
		// JSONObject myjson=new JSONObject(json);
		// Object objectp=myjson.get("p");
		// System.out.println(objectp);
		// JSONObject jsonObjectc=myjson.getJSONObject("c");
		// //System.out.println(jsonObjectc);
		// JSONObject jsonObjecta=myjson.getJSONObject("a");
		// //System.out.println(jsonObjecta);
		//buildMap(json);
		// testname();
		//System.out.println(listgetList().length());
		System.out.println(count(buildMap(3)));
	}

	/**
	 * 1.得到省市区的3个list 2.将省与市关联 3.将省、市、区关联
	 * 
	 * @param json
	 */
	public static JSONObject buildMap(int flag) {
		// TODO Auto-generated method stub
		// 总数据的json
		String path = "json.json";
		String json = getJsonVal(path);
		JSONObject dataObject = new JSONObject(json);
		// 省的List
		//JSONObject objectp = dataObject.getJSONObject("p");
		// 市的json
		JSONObject jsonCity = dataObject.getJSONObject("c");
		// 区的json
		JSONObject jsonDis = dataObject.getJSONObject("a");
		 if(flag==2){
			return jsonCity;
		}else if(flag==3){
			return jsonDis;
		}
		return null;

	}
	
	public static int count(JSONObject object){
		int i=0;
		for(String key:object.keySet()){
			JSONArray array=object.getJSONArray(key);
			i+=array.length();
		}
		return i;
	}

	/**
	 * 获取省的数据
	 */
	public static JSONArray listgetList() {
		String path = "json.json";
		String json = getJsonVal(path);
		// 总数据的json
		JSONObject dataObject = new JSONObject(json);
		// 省的List
		JSONArray objectp = dataObject.getJSONArray("p");
		return objectp;
	}

	public static void testname() {
		Map<String, Vector<String>> map = new HashMap<String, Vector<String>>();
		Vector<String> vector = new Vector<String>();
		vector.add("11");
		map.put("1", vector);
		for (String key : map.keySet()) {
			System.out.println(key + ":" + map.get(key).size());
		}
		map.get("1").add("222");
		for (String key : map.keySet()) {
			System.out.println(key + ":" + map.get(key).size());
		}
	}

	/**
	 * 测试省市区数据是不是一直
	 * 
	 * @param json
	 */
	public static void CheckJson(String json) {
		// 总数据的json
		JSONObject dataObject = new JSONObject(json);
		// 省的List
		JSONArray objectp = dataObject.getJSONArray("p");
		// List<String> priList=new ArrayList<String>();
		String[] priStrings = new String[objectp.length()];
		for (int i = 0; i < objectp.length(); i++) {
			priStrings[i] = objectp.getString(i);
		}
		// 市的json
		JSONObject jsonCity = dataObject.getJSONObject("c");
		/**
		 * JSONObject的2中遍历方式
		 */
		// for (Iterator<String> iterator = jsonCity.keys();
		// iterator.hasNext();) {
		// System.out.println(iterator.next());
		//
		// }
		// String[] cityPri = new String[jsonCity.keySet().size()];
		// int cityKey = 0;
		// for (String key : jsonCity.keySet()) {
		// // System.out.println(key);
		// // System.out.println(jsonCity.getJSONArray(key));
		// cityPri[cityKey] = key;
		// cityKey++;
		// }
		// Arrays.sort(priStrings);
		// Arrays.sort(cityPri);
		// System.out.println(Arrays.equals(priStrings, cityPri));

		Map<String, String[]> cityMap = new HashMap<String, String[]>();
		// a的key
		// 获取c中的市
		for (int i = 0; i < priStrings.length; i++) {
			JSONArray array = jsonCity.getJSONArray(priStrings[i]);
			String[] val = new String[array.length()];
			for (int j = 0; j < array.length(); j++) {
				val[j] = array.getString(j);
			}
			cityMap.put(priStrings[i], val);
		}
		// System.out.println(cityMap.keySet().size());

		// 区的json
		Map<String, Vector<String>> acityMap = new HashMap<String, Vector<String>>();
		JSONObject jsonDis = dataObject.getJSONObject("a");
		for (String akey : jsonDis.keySet()) {
			// System.out.println(akey);
			// System.out.println(jsonDis.get(akey));
			String[] val = akey.split("-");
			String per = val[0];
			String nex = val[1];
			Vector<String> vector = null;
			if (!acityMap.containsKey(per)) {
				vector = new Vector<String>();
				acityMap.put(per, vector);
			} else {
				vector = acityMap.get(per);
			}
			vector.add(nex);
		}

		int same = 0, notsame = 0;
		for (String key : acityMap.keySet()) {
			Vector<String> vector = acityMap.get(key);
			String[] val2 = getStrvalue(vector);
			String[] val1 = cityMap.get(key);
			Arrays.sort(val1);
			Arrays.sort(val2);
			if (!Arrays.equals(val1, val2)) {
				StringBuffer v1 = new StringBuffer(), v2 = new StringBuffer();
				for (int i = 0; i < val1.length; i++) {
					v1.append(val1[i] + ",");

				}
				for (int i = 0; i < val2.length; i++) {
					v2.append(val2[i] + ",");
				}
				System.out.println("v1:" + key + ":" + v1.toString());
				System.out.println("v2:" + key + ":" + v2.toString());
				notsame++;
			} else {
				same++;
			}
		}
		System.out.println("same:" + same);
		System.out.println("notsame:" + notsame);
		System.out.println("sheng:" + cityMap.keySet().size());
		System.out.println("qu:" + acityMap.keySet().size());
	}

	private static String[] getStrvalue(Vector<String> vector) {
		// TODO Auto-generated method stub]
		String[] ar = new String[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			ar[i] = vector.get(i);
		}
		return ar;

	}

	/**
	 * 读取json的文件，返回json字符串
	 */
	public static String getJsonVal(String path) {
		// FileReader
		String value = "";
		BufferedReader reader = null;
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		try {
			fileInputStream = new FileInputStream(path);
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			// 以行为单位读取
			reader = new BufferedReader(inputStreamReader);
			String temp = null;
			// 一次读取一行
			while ((temp = reader.readLine()) != null) {
				temp = temp.trim();
				value += temp;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

}
