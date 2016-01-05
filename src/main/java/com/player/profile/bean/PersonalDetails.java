package com.player.profile.bean;

import java.util.Date;
import java.util.List;

public class PersonalDetails {

	private String name;
	private String fullName;
	private Date birthDate;
	private String birthPlace;
	private String teamsPlayedFor;
	private String role;
	private String battingStyle;
	private String bowlingStyle;
	private DebutDetails testDebutDetails;
	private DebutDetails odiDebutDetails;
	private DebutDetails t20DebutDetails;
	private String imageLocation;
	private CumulativeStatistics testCumulativeStatistics;
	private CumulativeStatistics odiCumulativeStatistics;
	private CumulativeStatistics t20CumulativeStatistics;
	private List<String> testCareerTimeline;
	private List<String> odiCareerTimeline;
	private List<String> t20CareerTimeline;
	private List<String> testOpponentsList;
	private List<String> odiOpponentsList;
	private List<String> t20OpponentsList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getTeamsPlayedFor() {
		return teamsPlayedFor;
	}

	public void setTeamsPlayedFor(String teamsPlayedFor) {
		this.teamsPlayedFor = teamsPlayedFor;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getBattingStyle() {
		return battingStyle;
	}

	public void setBattingStyle(String battingStyle) {
		this.battingStyle = battingStyle;
	}

	public String getBowlingStyle() {
		return bowlingStyle;
	}

	public void setBowlingStyle(String bowlingStyle) {
		this.bowlingStyle = bowlingStyle;
	}

	public DebutDetails getTestDebutDetails() {
		return testDebutDetails;
	}

	public void setTestDebutDetails(DebutDetails testDebutDetails) {
		this.testDebutDetails = testDebutDetails;
	}

	public DebutDetails getOdiDebutDetails() {
		return odiDebutDetails;
	}

	public void setOdiDebutDetails(DebutDetails odiDebutDetails) {
		this.odiDebutDetails = odiDebutDetails;
	}

	public DebutDetails getT20DebutDetails() {
		return t20DebutDetails;
	}

	public void setT20DebutDetails(DebutDetails t20DebutDetails) {
		this.t20DebutDetails = t20DebutDetails;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public CumulativeStatistics getTestCumulativeStatistics() {
		return testCumulativeStatistics;
	}

	public void setTestCumulativeStatistics(CumulativeStatistics testCumulativeStatistics) {
		this.testCumulativeStatistics = testCumulativeStatistics;
	}

	public CumulativeStatistics getOdiCumulativeStatistics() {
		return odiCumulativeStatistics;
	}

	public void setOdiCumulativeStatistics(CumulativeStatistics odiCumulativeStatistics) {
		this.odiCumulativeStatistics = odiCumulativeStatistics;
	}

	public CumulativeStatistics getT20CumulativeStatistics() {
		return t20CumulativeStatistics;
	}

	public void setT20CumulativeStatistics(CumulativeStatistics t20CumulativeStatistics) {
		this.t20CumulativeStatistics = t20CumulativeStatistics;
	}

	public List<String> getTestCareerTimeline() {
		return testCareerTimeline;
	}

	public void setTestCareerTimeline(List<String> testCareerTimeline) {
		this.testCareerTimeline = testCareerTimeline;
	}

	public List<String> getOdiCareerTimeline() {
		return odiCareerTimeline;
	}

	public void setOdiCareerTimeline(List<String> odiCareerTimeline) {
		this.odiCareerTimeline = odiCareerTimeline;
	}

	public List<String> getT20CareerTimeline() {
		return t20CareerTimeline;
	}

	public void setT20CareerTimeline(List<String> t20CareerTimeline) {
		this.t20CareerTimeline = t20CareerTimeline;
	}

	public List<String> getTestOpponentsList() {
		return testOpponentsList;
	}

	public void setTestOpponentsList(List<String> testOpponentsList) {
		this.testOpponentsList = testOpponentsList;
	}

	public List<String> getOdiOpponentsList() {
		return odiOpponentsList;
	}

	public void setOdiOpponentsList(List<String> odiOpponentsList) {
		this.odiOpponentsList = odiOpponentsList;
	}

	public List<String> getT20OpponentsList() {
		return t20OpponentsList;
	}

	public void setT20OpponentsList(List<String> t20OpponentsList) {
		this.t20OpponentsList = t20OpponentsList;
	}

}
