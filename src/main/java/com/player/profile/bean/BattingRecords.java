package com.player.profile.bean;

import java.sql.Date;

public class BattingRecords {

	private String runs;
	private String minutes;
	private String ballsFaced;
	private String noOfFours;
	private String noOfSixes;
	private String strikeRate;
	private String position;
	private String dismissal;
	private String innings;
	private String opposition;
	private String ground;
	private Date matchDate;
	private String matchId;

	public String getRuns() {
		return runs;
	}

	public void setRuns(String runs) {
		this.runs = runs;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

	public String getBallsFaced() {
		return ballsFaced;
	}

	public void setBallsFaced(String ballsFaced) {
		this.ballsFaced = ballsFaced;
	}

	public String getNoOfFours() {
		return noOfFours;
	}

	public void setNoOfFours(String noOfFours) {
		this.noOfFours = noOfFours;
	}

	public String getNoOfSixes() {
		return noOfSixes;
	}

	public void setNoOfSixes(String noOfSixes) {
		this.noOfSixes = noOfSixes;
	}

	public String getStrikeRate() {
		return strikeRate;
	}

	public void setStrikeRate(String strikeRate) {
		this.strikeRate = strikeRate;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDismissal() {
		return dismissal;
	}

	public void setDismissal(String dismissal) {
		this.dismissal = dismissal;
	}

	public String getInnings() {
		return innings;
	}

	public void setInnings(String innings) {
		this.innings = innings;
	}

	public String getOpposition() {
		return opposition;
	}

	public void setOpposition(String opposition) {
		this.opposition = opposition;
	}

	public String getGround() {
		return ground;
	}

	public void setGround(String ground) {
		this.ground = ground;
	}

	public Date getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
}
