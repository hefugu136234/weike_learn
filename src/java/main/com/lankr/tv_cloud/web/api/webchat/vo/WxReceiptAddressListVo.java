package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ReceiptAddress;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class WxReceiptAddressListVo extends BaseAPIModel{
	
	private List<WxReceiptAddressVo> receiptAddressVos;

	public List<WxReceiptAddressVo> getReceiptAddressVos() {
		return receiptAddressVos;
	}

	public void setReceiptAddressVos(List<WxReceiptAddressVo> receiptAddressVos) {
		this.receiptAddressVos = receiptAddressVos;
	}
	
	public void buildDefault(ReceiptAddress receiptAddress,List<ReceiptAddress> list){
		this.setStatus(Status.SUCCESS);
		receiptAddressVos=new ArrayList<WxReceiptAddressVo>();
		if(receiptAddress!=null){
			WxReceiptAddressVo vo=new WxReceiptAddressVo();
			vo.listData(receiptAddress);
			receiptAddressVos.add(vo);
		}
		if(list!=null&&!list.isEmpty()){
			for (ReceiptAddress model : list) {
				WxReceiptAddressVo vo=new WxReceiptAddressVo();
				vo.listData(model);
				receiptAddressVos.add(vo);
			}
		}
	}
	
	public void buildNoDefault(List<ReceiptAddress> list){
		this.setStatus(Status.SUCCESS);
		receiptAddressVos=new ArrayList<WxReceiptAddressVo>();
		if(list!=null&&!list.isEmpty()){
			for (ReceiptAddress model : list) {
				WxReceiptAddressVo vo=new WxReceiptAddressVo();
				vo.listData(model);
				receiptAddressVos.add(vo);
			}
		}
	}

}
