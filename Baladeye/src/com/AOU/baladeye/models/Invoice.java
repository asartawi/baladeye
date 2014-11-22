package com.AOU.baladeye.models;

public class Invoice {
	private String id;
	private String userId;
	private String type;
	private String value;
	private String extraDetails;
	private String billDate;
	private boolean isPaid;

	public Invoice(String id, String userId, String type, String value,
			String extraDetails, String billDate, boolean isPaid) {
		super();
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.value = value;
		this.extraDetails = extraDetails;
		this.billDate = billDate;
		this.isPaid = isPaid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExtraDetails() {
		return extraDetails;
	}

	public void setExtraDetails(String extraDetails) {
		this.extraDetails = extraDetails;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
}
