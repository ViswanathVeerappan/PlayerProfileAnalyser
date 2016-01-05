package com.player.profile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the players_details database table.
 * 
 */
@Entity
@Table(name = "players_details")
public class PlayersDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PLAYER_ID")
	private int playerId;

	@Column(name = "BATTING_STYLE")
	private String battingStyle;

	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTH_DATE")
	private Date birthDate;

	@Column(name = "BIRTH_PLACE")
	private String birthPlace;

	@Column(name = "BOWLING_STYLE")
	private String bowlingStyle;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "ODI_DEBUT_AGAINST")
	private String odiDebutAgainst;

	@Temporal(TemporalType.DATE)
	@Column(name = "ODI_DEBUT_DATE")
	private Date odiDebutDate;

	@Column(name = "ROLE")
	private String role;

	@Column(name = "T20I_DEBUT_AGAINST")
	private String t20iDebutAgainst;

	@Temporal(TemporalType.DATE)
	@Column(name = "T20I_DEBUT_DATE")
	private Date t20iDebutDate;

	@Column(name = "TEAMS_PLAYED_FOR")
	private String teamsPlayedFor;

	@Column(name = "TEST_DEBUT_AGAINST")
	private String testDebutAgainst;

	@Temporal(TemporalType.DATE)
	@Column(name = "TEST_DEBUT_DATE")
	private Date testDebutDate;

	// bi-directional one-to-one association to Player
	@OneToOne
	@JoinColumn(name = "PLAYER_ID")
	private Player player;

	public int getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getBattingStyle() {
		return this.battingStyle;
	}

	public void setBattingStyle(String battingStyle) {
		this.battingStyle = battingStyle;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return this.birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getBowlingStyle() {
		return this.bowlingStyle;
	}

	public void setBowlingStyle(String bowlingStyle) {
		this.bowlingStyle = bowlingStyle;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getOdiDebutAgainst() {
		return this.odiDebutAgainst;
	}

	public void setOdiDebutAgainst(String odiDebutAgainst) {
		this.odiDebutAgainst = odiDebutAgainst;
	}

	public Date getOdiDebutDate() {
		return this.odiDebutDate;
	}

	public void setOdiDebutDate(Date odiDebutDate) {
		this.odiDebutDate = odiDebutDate;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getT20iDebutAgainst() {
		return this.t20iDebutAgainst;
	}

	public void setT20iDebutAgainst(String t20iDebutAgainst) {
		this.t20iDebutAgainst = t20iDebutAgainst;
	}

	public Date getT20iDebutDate() {
		return this.t20iDebutDate;
	}

	public void setT20iDebutDate(Date t20iDebutDate) {
		this.t20iDebutDate = t20iDebutDate;
	}

	public String getTeamsPlayedFor() {
		return this.teamsPlayedFor;
	}

	public void setTeamsPlayedFor(String teamsPlayedFor) {
		this.teamsPlayedFor = teamsPlayedFor;
	}

	public String getTestDebutAgainst() {
		return this.testDebutAgainst;
	}

	public void setTestDebutAgainst(String testDebutAgainst) {
		this.testDebutAgainst = testDebutAgainst;
	}

	public Date getTestDebutDate() {
		return this.testDebutDate;
	}

	public void setTestDebutDate(Date testDebutDate) {
		this.testDebutDate = testDebutDate;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}