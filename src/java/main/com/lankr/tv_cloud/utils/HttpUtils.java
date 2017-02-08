package com.lankr.tv_cloud.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {
	private static final int BUFFER_SIZE_BYTE = 1024;
	private static HttpURLConnection httpConnection;
	private static BufferedInputStream bis;
	private static ByteArrayOutputStream baos;
	private static String result;

	/**
	 * ��ȡһ��url�򿪺����ҳ����
	 */
	public static String fetchContentFromUrl(String param) {

		try {
			URL url = new URL(param);

			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setConnectTimeout(5 * 1000);
			httpConnection.setReadTimeout(10 * 1000);

			if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}

			bis = new BufferedInputStream(httpConnection.getInputStream(),
					BUFFER_SIZE_BYTE);

			baos = new ByteArrayOutputStream(BUFFER_SIZE_BYTE);

			byte[] buffer = new byte[128]; // ����һ��128�ֽڵĻ�����
											// ���ڽ���ݶ����˻�����

			int readCount;

			while ((readCount = bis.read(buffer)) != -1) { // ���������ж���ݵ�buffer������
				// ʵ�ʶ������������ݵ��ֽ������readCount��
				baos.write(buffer, 0, readCount);
			}

			result = baos.toString();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			try {
				if (bis != null) {
					bis.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 
	 * @param basePath
	 *            target url path
	 * @param map
	 *            http post parameters to transport
	 * @return
	 */
	public static String sendPostRequest(String basePath,
			Map<String, String> map) {
		try {
			if (map != null && !map.isEmpty()) {

				StringBuilder body = new StringBuilder();

				for (Map.Entry<String, String> entry : map.entrySet()) {

					body.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
							.append("&");

				}
				body.deleteCharAt(body.length() - 1);
				return sendPostRequest(basePath, body.toString(), false);
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static String sendPostRequest(String basePath, String body,
			boolean json) {
		try {
			URL url = new URL(basePath);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(5 * 1000);
			urlConnection.setReadTimeout(10 * 1000);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);

			urlConnection.setRequestProperty("Content-Type",
					json ? "application/json"
							: "application/x-www-form-urlencoded");
//			urlConnection.setRequestProperty("Content-Length",
//					String.valueOf(body.length()));

			DataOutputStream outStream = new DataOutputStream(
					urlConnection.getOutputStream());
			outStream.write(body.getBytes("UTF-8"));
			outStream.flush();
			outStream.close();
			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream(),
							"utf-8"));
			String buffer;
			StringBuffer sb = new StringBuffer();
			while ((buffer = inputStream.readLine()) != null) {
				sb.append(buffer);
			}
			String respondString = sb.toString();
			return respondString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String sendGetRequest(String finalUrl) {

		try {
			URL url = new URL(finalUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setConnectTimeout(5 * 1000);
			urlConnection.setReadTimeout(10 * 1000);

			urlConnection.setDoInput(true);

			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream(),
							"utf-8"));
			String buffer;
			StringBuffer sb = new StringBuffer();
			while ((buffer = inputStream.readLine()) != null) {
				sb.append(buffer);
			}
			String respondString = sb.toString();
			/* Log.i("http-respond", respondString); */
			return respondString;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO: handle exception
		} catch (SocketTimeoutException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String sendGetRequest(String basePath, Map<String, String> map) {
		return sendGetRequest(Tools.makeGetUrl(basePath, map));
	}
	
	/**
	 * 以http方式发送请求,并将请求响应内容输出到文件
	 * 
	 * @param path
	 *            请求路径
	 * @param method
	 *            请求方法
	 * @param body
	 *            请求数据
	 * @return 返回响应的存储到文件
	 */
	public static File httpRequestToFile(String fileName, String path,
			String method, String body) {
		if (fileName == null || path == null || method == null) {
			return null;
		}

		File file = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		FileOutputStream fileOut = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(method);
			System.out.println(conn.getResponseCode());
			String contentType = conn.getContentType();
			System.out.println(contentType);
			if (contentType.equals("image/jpeg")) {
				if (null != body) {
					OutputStream outputStream = conn.getOutputStream();
					outputStream.write(body.getBytes("UTF-8"));
					outputStream.close();
				}

				inputStream = conn.getInputStream();
				if (inputStream != null) {
					file = new File(fileName);
				} else {
					return file;
				}

				// 写入到文件
				fileOut = new FileOutputStream(file);
				if (fileOut != null) {
					int c = inputStream.read();
					while (c != -1) {
						fileOut.write(c);
						c = inputStream.read();
					}
				}
			}else{
				//wx下载图片失败
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}

			/*
			 * 必须关闭文件流 否则JDK运行时，文件被占用其他进程无法访问
			 */
			try {
				inputStream.close();
				fileOut.close();
			} catch (IOException execption) {
				execption.printStackTrace();
			}
		}
		System.out.println(file.length());
		return file;
	}
	
	public static void main(String[] args) {
		System.out.println(sendGetRequest("http://vrassets.lankr.net/960p.mp4"));
	}

}
