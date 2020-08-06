package com.fh.util;

public class DataTablePageBean {
	// 标志，跟DataTableResult中的draw保持一致
	private Integer draw;
	// 每页展示条数
	private Integer length;
	// 起始下标
	private Integer start;



	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

}
