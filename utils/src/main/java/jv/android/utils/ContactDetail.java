package jv.android.utils;

import java.util.ArrayList;
import java.util.List;

public class ContactDetail {

	private String id;
	private String name;
	private List<String> homeFax;
	private List<String> workFax;
	private List<String> otherFax;
	private List<String> homePhone;
	private List<String> workPhone;
	private List<String> mobilePhone;
	private List<String> otherPhone;
	private List<String> customPhone;
	private List<String> homeEmail;
	private List<String> workEmail;
	private List<String> mobileEmail;
	private List<String> otherEmail;
	private List<String> customEmail;
	
	public ContactDetail() {
		id = "";
		name = "";
		homeFax = new ArrayList<String>();
		workFax = new ArrayList<String>();
		otherFax = new ArrayList<String>();
		homePhone = new ArrayList<String>();
		workPhone = new ArrayList<String>();
		mobilePhone = new ArrayList<String>();
		otherPhone = new ArrayList<String>();
		customPhone = new ArrayList<String>();
		homeEmail = new ArrayList<String>();
		workEmail = new ArrayList<String>();
		mobileEmail = new ArrayList<String>();
		otherEmail = new ArrayList<String>();
		customEmail = new ArrayList<String>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void addHomeFax(String homeFax) {
		this.homeFax.add(homeFax);
	}	
	public void addWorkFax(String workFax) {
		this.workFax.add(workFax);
	}	
	public void addOtherFax(String otherFax) {
		this.otherFax.add(otherFax);
	}	
	public void addHomePhone(String homePhone) {
		this.homePhone.add(homePhone);
	}	
	public void addWorkPhone(String workPhone) {
		this.workPhone.add(workPhone);
	}	
	public void addMobilePhone(String mobilePhone) {
		this.mobilePhone.add(mobilePhone);
	}	
	public void addOtherPhone(String otherPhone) {
		this.otherPhone.add(otherPhone);
	}	
	public void addCustomPhone(String customPhone) {
		this.customPhone.add(customPhone);
	}	
	public void addHomeEmail(String homeEmail) {
		this.homeEmail.add(homeEmail);
	}	
	public void addWorkEmail(String workEmail) {
		this.workEmail.add(workEmail);
	}	
	public void addMobileEmail(String mobileEmail) {
		this.mobileEmail.add(mobileEmail);
	}	
	public void addOtherEmail(String otherEmail) {
		this.otherEmail.add(otherEmail);
	}	
	public void addCustomEmail(String customEmail) {
		this.customEmail.add(customEmail);
	}	
	
	public String getHomeFax(int index) {
		return this.homeFax.get(index);
	}	
	public String addWorkFax(int index) {
		return this.workFax.get(index);
	}	
	public String addOtherFax(int index) {
		return this.otherFax.get(index);
	}	
	public String addHomePhone(int index) {
		return this.homePhone.get(index);
	}	
	public String addWorkPhone(int index) {
		return this.workPhone.get(index);
	}	
	public String addMobilePhone(int index) {
		return this.mobilePhone.get(index);
	}	
	public String addOtherPhone(int index) {
		return this.otherPhone.get(index);
	}	
	public String addCustomPhone(int index) {
		return this.customPhone.get(index);
	}	
	public String addHomeEmail(int index) {
		return this.homeEmail.get(index);
	}	
	public String addWorkEmail(int index) {
		return this.workEmail.get(index);
	}	
	public String addMobileEmail(int index) {
		return this.mobileEmail.get(index);
	}	
	public String addOtherEmail(int index) {
		return this.otherEmail.get(index);
	}	
	public String addCustomEmail(int index) {
		return this.customEmail.get(index);
	}	
	
	public List<String> getHomeFax() {
		return homeFax;
	}
	public void setHomeFax(List<String> homeFax) {
		this.homeFax = homeFax;
	}
	public List<String> getWorkFax() {
		return workFax;
	}
	public void setWorkFax(List<String> workFax) {
		this.workFax = workFax;
	}
	public List<String> getOtherFax() {
		return otherFax;
	}
	public void setOtherFax(List<String> otherFax) {
		this.otherFax = otherFax;
	}
	public List<String> getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(List<String> homePhone) {
		this.homePhone = homePhone;
	}
	public List<String> getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(List<String> workPhone) {
		this.workPhone = workPhone;
	}
	public List<String> getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(List<String> mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public List<String> getOtherPhone() {
		return otherPhone;
	}
	public void setOtherPhone(List<String> otherPhone) {
		this.otherPhone = otherPhone;
	}
	public List<String> getHomeEmail() {
		return homeEmail;
	}
	public void setHomeEmail(List<String> homeEmail) {
		this.homeEmail = homeEmail;
	}
	public List<String> getWorkEmail() {
		return workEmail;
	}
	public void setWorkEmail(List<String> workEmail) {
		this.workEmail = workEmail;
	}
	public List<String> getMobileEmail() {
		return mobileEmail;
	}
	public void setMobileEmail(List<String> mobileEmail) {
		this.mobileEmail = mobileEmail;
	}
	public List<String> getOtherEmail() {
		return otherEmail;
	}
	public void setOtherEmail(List<String> otherEmail) {
		this.otherEmail = otherEmail;
	}
	public List<String> getCustomPhone() {
		return customPhone;
	}
	public void setCustomPhone(List<String> customPhone) {
		this.customPhone = customPhone;
	}
	public List<String> getCustomEmail() {
		return customEmail;
	}
	public void setCustomEmail(List<String> customEmail) {
		this.customEmail = customEmail;
	}
	
}
