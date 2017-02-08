/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月6日
 * 	@modifyDate 2016年5月6日
 *  
 */
package com.lankr.tv_cloud.codes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Kalean.Xiang
 *
 */
public class CodeProvider {

	private static Log logger = LogFactory.getLog(CodeProvider.class);

	private List<Code> codes;

	public final void init() {
		if (codes != null && !codes.isEmpty())
			return;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(CodeProvider.class.getClassLoader()
					.getResourceAsStream("com/lankr/tv_cloud/codes/codes.xml"));
			Element element = doc.getDocumentElement();
			NodeList list = element.getElementsByTagName("code");
			codes = new ArrayList<Code>(list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				Element ele = (Element) list.item(i);
				String s = ele.getAttribute("status");
				Code c = new Code(Integer.valueOf(ele.getAttribute("id")),
						ele.getAttribute("name"), ele.getAttribute("message"),
						s);
				codes.add(c);

			}
			logger.info("codes load completed...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Code code(int id) {
		if (codes != null && !codes.isEmpty()) {
			for (int i = 0; i < codes.size(); i++) {
				Code c = codes.get(i);
				if (c.getId() == id) {
					return c;
				}
			}
		}
		return new Code(-1000, "unknow", "unknow", null);
	}

	public Code codeOk() {
		return code(0);
	}
}
