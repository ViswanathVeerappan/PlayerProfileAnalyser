package com.player.profile.bean;

import java.util.Date;

public class LastMatchDetails {

	private String lastMatchAgainst;
	private Date lastMatchDate;

	public String getLastMatchAgainst() {
		return lastMatchAgainst;
	}

	public void setLastMatchAgainst(String lastMatchAgainst) {
		this.lastMatchAgainst = lastMatchAgainst;
	}

	public Date getLastMatchDate() {
		return lastMatchDate;
	}

	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

}
