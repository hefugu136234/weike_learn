/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月20日
 * 	@modifyDate 2016年5月20日
 *  
 */
package com.lankr.tv_cloud.exception;

import com.lankr.tv_cloud.codes.Code;

/**
 * @author Kalean.Xiang
 *
 */
public class CodeException extends Exception {

	/**
	 * 
	 */

	private CodeException() {
		super();
	}

	private CodeException(String msg) {
		super(msg);
	}

	public CodeException(Code code) {
		this(code.toString());
	}
}
