package jv.android.utils.calendar;

import java.io.Serializable;

public class CalendarName implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3201761875146552535L;
	private long id;
	private String name;
	private String account_name;
	
	public CalendarName() {
		id = -1;
		name = "";
		account_name = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

}
