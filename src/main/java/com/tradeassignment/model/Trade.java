package com.tradeassignment.model;

import java.util.Date;

public class Trade {
	
	private String tradeId;
	private int version;
	private String counterPartyId;
	private String bookId;
	private Date maturityDate;
	private Date createdDate;
	private String expired;	
	
	public Trade(String tradeId, int version, String counterPartyId, String bookId, Date maturityDate, Date createdDate,
			String expired) {
		super();
		this.tradeId = tradeId;
		this.version = version;
		this.counterPartyId = counterPartyId;
		this.bookId = bookId;
		this.maturityDate = maturityDate;
		this.createdDate = createdDate;
		this.expired = expired;
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getCounterPartyId() {
		return counterPartyId;
	}
	public void setCounterPartyId(String counterPartyId) {
		this.counterPartyId = counterPartyId;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public Date getMaturityDate() {
		return maturityDate;
	}
	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String isExpired() {
		return expired;
	}
	public void setExpired(String expired) {
		this.expired = expired;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("["+tradeId +", ");
		sb.append(version +", ");
		sb.append(counterPartyId +", ");
		sb.append(bookId +", ");
		sb.append(maturityDate +", ");
		sb.append(createdDate +", ");
		sb.append(expired +"]");
		
		return sb.toString();
	}

}
