package com.springboot.app.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UserVO {

	private String id;

	@NotNull(message = "{user.username.notnull}")
	@NotBlank(message = "{user.username.notempty}")
	private String username;

	@NotNull(message = "{user.birthDate.notnull}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;

	@NotNull(message = "{user.countryOfResidence.notnull}")
	@NotBlank(message = "{user.countryOfResidence.notempty}")
	private String countryOfResidence;

	@Size(min = 10,max = 15)
	@JsonInclude(value = Include.NON_NULL)
	private String phoneNumber;

	@JsonInclude(value = Include.NON_NULL)
	private String gender;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
