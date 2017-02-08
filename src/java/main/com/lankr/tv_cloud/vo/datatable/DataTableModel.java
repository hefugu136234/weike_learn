package com.lankr.tv_cloud.vo.datatable;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.snapshot.AbstractItem;

public class DataTableModel<T> extends BaseAPIModel {

	public int getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public List<T> getAaData() {
		return aaData;
	}

	public void setAaData(List<T> aaData) {
		this.aaData = aaData;
	}

	int iTotalRecords;

	int iTotalDisplayRecords;

	public List<T> aaData = new ArrayList<T>();

	public static <M extends AbstractItem<?>, N extends BaseModel> DataTableModel<?> makeInstance(
			List<N> persists, Class<M> item) {
		DataTableModel model = new DataTableModel<M>();
		model.setStatus(Status.SUCCESS);
		if (persists != null) {
			for (N n : persists) {
				try {
					AbstractItem i = item.getDeclaredConstructor(n.getClass())
							.newInstance(n);
					i.build();
					model.aaData.add(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.api.BaseAPIModel#toJSON()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.api.BaseAPIModel#toJSON()
	 */
	// private static ObjectMapper mapper = new ObjectMapper();
	// static {
	// // mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	// mapper.setVisibilityChecker(mapper.getSerializationConfig()
	// .getDefaultVisibilityChecker()
	// .withFieldVisibility(Visibility.ANY)
	// .withGetterVisibility(Visibility.NONE)
	// .withSetterVisibility(Visibility.NONE)
	// .withCreatorVisibility(Visibility.NONE));
	// mapper.setSerializationInclusion(Include.NON_NULL);
	// }

	@Override
	public String toJSON() {
		// try {
		// return mapper.writeValueAsString(this);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return super.toJSON();
		return toJSON(JsonConvertor.JACKSON);
	}
}
