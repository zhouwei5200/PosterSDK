package com.example.posterdemo;

import java.util.List;

public class PosterBean {
	private List<PosterBeanItem> data;
	private String note;
	private int statue;

	public List<PosterBeanItem> getData() {
		return data;
	}

	public void setData(List<PosterBeanItem> data) {
		this.data = data;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getStatue() {
		return statue;
	}

	public void setStatue(int statue) {
		this.statue = statue;
	}
	@Override
	public String toString() {
		return "PosterBean [data=" + data + ", note=" + note + ", statue=" + statue + "]";
	}
}
