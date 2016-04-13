package com.example.posterdemo;

import java.util.List;

public class PosterVersionBean {
	private String data;
	private String note;
	private int statue;

	public String getData() {
		return data;
	}

	public void setData(String data) {
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
