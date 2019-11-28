package jv.android.utils;

public class Calls {

	private String date;
	private String number;
	private String duration;
	private String type;
	
	public Calls() {
		date = "";
		number = "";
		duration = "";
		type = "";
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
