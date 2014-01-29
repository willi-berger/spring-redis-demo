package com.alturos.adcup.springredisdemo;

import java.io.Serializable;

/**
 * @author berwil
 *
 */
public class Person implements Serializable{

	private String firstName;

	private String lastName;

	public Person(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

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

	@Override
	public String toString() {

		return String.format("Person:[firstName='%s', lastName='%s']",
				firstName, lastName);
	}

}
