package com.player.profile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the player_test_batting_stats database table.
 * 
 */
@Entity
@Table(name = "player_test_batting_stats")
public class PlayerTestBattingStatistics implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PlayerTestBattingStatisticsPrimaryKey id;

	@Column(name = "BALLS_FACED")
	private String ballsFaced;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "DISMISSAL")
	private String dismissal;

	@Column(name = "GROUND")
	private String ground;

	@Column(name = "MINUTES")
	private String minutes;

	@Column(name = "NO_OF_FOURS")
	private String noOfFours;

	@Column(name = "NO_OF_SIXES")
	private String noOfSixes;

	@Column(name = "OPPOSITION")
	private String opposition;

	@Column(name = "POSITION")
	private String position;

	@Column(name = "RUNS")
	private String runs;

	@Column(name = "STRIKE_RATE")
	private String strikeRate;

	// bi-directional many-to-one association to Player
	@ManyToOne
	@JoinColumn(name = "PLAYER_ID", insertable = false, updatable = false)
	private Player player;

	public PlayerTestBattingStatisticsPrimaryKey getId() {
		return this.id;
	}

	public void setId(PlayerTestBattingStatisticsPrimaryKey id) {
		this.id = id;
	}

	public String getBallsFaced() {
		return this.ballsFaced;
	}

	public void setBallsFaced(String ballsFaced) {
		this.ballsFaced = ballsFaced;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDismissal() {
		return this.dismissal;
	}

	public void setDismissal(String dismissal) {
		this.dismissal = dismissal;
	}

	public String getGround() {
		return this.ground;
	}

	public void setGround(String ground) {
		this.ground = ground;
	}

	public String getMinutes() {
		return this.minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

	public String getNoOfFours() {
		return this.noOfFours;
	}

	public void setNoOfFours(String noOfFours) {
		this.noOfFours = noOfFours;
	}

	public String getNoOfSixes() {
		return this.noOfSixes;
	}

	public void setNoOfSixes(String noOfSixes) {
		this.noOfSixes = noOfSixes;
	}

	public String getOpposition() {
		return this.opposition;
	}

	public void setOpposition(String opposition) {
		this.opposition = opposition;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRuns() {
		return this.runs;
	}

	public void setRuns(String runs) {
		this.runs = runs;
	}

	public String getStrikeRate() {
		return this.strikeRate;
	}

	public void setStrikeRate(String strikeRate) {
		this.strikeRate = strikeRate;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}