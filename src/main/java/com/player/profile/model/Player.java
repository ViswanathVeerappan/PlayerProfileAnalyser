package com.player.profile.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the players database table.
 * 
 */
@Entity
@Table(name = "players")
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PLAYER_ID")
	private int playerId;

	@Column(name = "CRICINFO_ID")
	private int cricinfoId;

	@Column(name = "PLAYER_NAME")
	private String playerName;

	// bi-directional many-to-one association to PlayerOdiBattingStat
	@OneToMany(mappedBy = "player")
	private List<PlayerOdiBattingStatistics> playerOdiBattingStats;

	// bi-directional many-to-one association to PlayerT20BattingStat
	@OneToMany(mappedBy = "player")
	private List<PlayerT20BattingStatistics> playerT20BattingStats;

	// bi-directional many-to-one association to PlayerTestBattingStat
	@OneToMany(mappedBy = "player")
	private List<PlayerTestBattingStatistics> playerTestBattingStats;

	// bi-directional one-to-one association to PlayersDetail
	@OneToOne(mappedBy = "player")
	private PlayersDetails playersDetail;

	public int getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getCricinfoId() {
		return this.cricinfoId;
	}

	public void setCricinfoId(int cricinfoId) {
		this.cricinfoId = cricinfoId;
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public List<PlayerOdiBattingStatistics> getPlayerOdiBattingStats() {
		return this.playerOdiBattingStats;
	}

	public void setPlayerOdiBattingStats(List<PlayerOdiBattingStatistics> playerOdiBattingStats) {
		this.playerOdiBattingStats = playerOdiBattingStats;
	}

	public PlayerOdiBattingStatistics addPlayerOdiBattingStat(PlayerOdiBattingStatistics playerOdiBattingStat) {
		getPlayerOdiBattingStats().add(playerOdiBattingStat);
		playerOdiBattingStat.setPlayer(this);

		return playerOdiBattingStat;
	}

	public PlayerOdiBattingStatistics removePlayerOdiBattingStat(PlayerOdiBattingStatistics playerOdiBattingStat) {
		getPlayerOdiBattingStats().remove(playerOdiBattingStat);
		playerOdiBattingStat.setPlayer(null);

		return playerOdiBattingStat;
	}

	public List<PlayerT20BattingStatistics> getPlayerT20BattingStats() {
		return this.playerT20BattingStats;
	}

	public void setPlayerT20BattingStats(List<PlayerT20BattingStatistics> playerT20BattingStats) {
		this.playerT20BattingStats = playerT20BattingStats;
	}

	public PlayerT20BattingStatistics addPlayerT20BattingStat(PlayerT20BattingStatistics playerT20BattingStat) {
		getPlayerT20BattingStats().add(playerT20BattingStat);
		playerT20BattingStat.setPlayer(this);

		return playerT20BattingStat;
	}

	public PlayerT20BattingStatistics removePlayerT20BattingStat(PlayerT20BattingStatistics playerT20BattingStat) {
		getPlayerT20BattingStats().remove(playerT20BattingStat);
		playerT20BattingStat.setPlayer(null);

		return playerT20BattingStat;
	}

	public List<PlayerTestBattingStatistics> getPlayerTestBattingStats() {
		return this.playerTestBattingStats;
	}

	public void setPlayerTestBattingStats(List<PlayerTestBattingStatistics> playerTestBattingStats) {
		this.playerTestBattingStats = playerTestBattingStats;
	}

	public PlayerTestBattingStatistics addPlayerTestBattingStat(PlayerTestBattingStatistics playerTestBattingStat) {
		getPlayerTestBattingStats().add(playerTestBattingStat);
		playerTestBattingStat.setPlayer(this);

		return playerTestBattingStat;
	}

	public PlayerTestBattingStatistics removePlayerTestBattingStat(PlayerTestBattingStatistics playerTestBattingStat) {
		getPlayerTestBattingStats().remove(playerTestBattingStat);
		playerTestBattingStat.setPlayer(null);

		return playerTestBattingStat;
	}

	public PlayersDetails getPlayersDetail() {
		return this.playersDetail;
	}

	public void setPlayersDetail(PlayersDetails playersDetail) {
		this.playersDetail = playersDetail;
	}

}