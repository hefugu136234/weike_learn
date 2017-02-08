package com.lankr.tv_cloud.facade.impl;

import java.util.List;

public class Pagination<T> {

	private static final int MAX_ROWS_GLOBAL = 100;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage_rows() {
		return Math.min(page_rows, MAX_ROWS_GLOBAL);
	}

	public void setPage_rows(int page_rows) {
		this.page_rows = page_rows;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	private int total;

	private int page_rows;

	private int begin;

	private int max_page;

	public int getMax_page() {
		return max_page;
	}

	public void setMax_page(int max_page) {
		this.max_page = max_page;
	}

	private List<T> results;

	public int dataStart() {
		return begin * page_rows;
	}

}
