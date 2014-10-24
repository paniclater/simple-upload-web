package com.paniclater;

public class UploadedFile {
	private String name;
	private int id;
	private byte[] contents;

	public byte[] getContents() {
		return contents;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}
