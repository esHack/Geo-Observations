package com.dev.beans;

import java.util.Date;

public class Observation {
	
	private String name;
	private String type;
	private int X;
	private int Y;
	private int Z;
	private Date date;
	private String recordedBy;
	private String recorderEmail;
	
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getRecordedBy() {
		return recordedBy;
	}
	public void setRecordedBy(String recordedBy) {
		this.recordedBy = recordedBy;
	}
	
	public String getRecorderEmail() {
		return recorderEmail;
	}
	public void setRecorderEmail(String recorderEmail) {
		this.recorderEmail = recorderEmail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public int getZ() {
		return Z;
	}
	public void setZ(int z) {
		Z = z;
	}
	

}
