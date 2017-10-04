package com.nttdata.myapp.web.rest.vm;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Person {
	private String firstName;
	private String lastName;
	private Date birthDate;
	private boolean isBornToday;
	private boolean isOlderThanEighteen;

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public boolean isBornToday() {
		return isBornToday;
	}

	public void setBornToday(boolean isBornToday) {
		this.isBornToday = isBornToday;
	}

	public boolean isOlderThanEighteen() {
		return isOlderThanEighteen;
	}

	public void setOlderThanEighteen(boolean isOlderThanEighteen) {
		this.isOlderThanEighteen = isOlderThanEighteen;
	}

}
