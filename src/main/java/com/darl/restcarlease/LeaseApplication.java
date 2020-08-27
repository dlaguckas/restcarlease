package com.darl.restcarlease;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class LeaseApplication {

  private @Id @GeneratedValue Long applicationId;
  private String vehicleData;
  private String personData;
  private int personIncome;
  private int requestedAmount;
  private ApplicationStatus applicationStatus;
  

  LeaseApplication() {}

  LeaseApplication(String vehicleData,
				  String personData,
				  int personIncome,
				  int requestedAmount) {

	  this.vehicleData = vehicleData;
	  this.personData = personData;
	  this.personIncome = personIncome;
	  this.requestedAmount = requestedAmount;
	  
  }
  
  enum ApplicationStatus {APPROVED, REJECTED}

	public Long getApplicationId() {
		return applicationId;
	}
	
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	
	public String getVehicleData() {
		return vehicleData;
	}
	
	public void setVehicleData(String vehicleData) {
		this.vehicleData = vehicleData;
	}
	
	public String getPersonData() {
		return personData;
	}
	
	public void setPersonData(String personData) {
		this.personData = personData;
	}
	
	public int getPersonIncome() {
		return personIncome;
	}
	
	public void setPersonIncome(int personIncome) {
		this.personIncome = personIncome;
	}
	
	public int getRequestedAmount() {
		return requestedAmount;
	}
	
	public void setRequestedAmount(int requestedAmount) {
		this.requestedAmount = requestedAmount;
	}
	
	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}
	
	public void setApplicationStatus() {
		if (this.personIncome >= 600) {
			this.applicationStatus = ApplicationStatus.APPROVED;
		}
		else {
			this.applicationStatus = ApplicationStatus.REJECTED;
		}
	}  
}