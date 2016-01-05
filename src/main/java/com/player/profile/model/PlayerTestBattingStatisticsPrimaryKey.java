package com.player.profile.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the player_test_batting_stats database table.
 * 
 */
@Embeddable
public class PlayerTestBattingStatisticsPrimaryKey implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "PLAYER_ID", insertable = false, updatable = false)
	private int playerId;

	@Column(name = "MATCH_ID")
	private String matchId;

	@Column(name = "INNINGS")
	private String innings;

	public int getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getMatchId() {
		return this.matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public String getInnings() {
		return this.innings;
	}

	public void setInnings(String innings) {
		this.innings = innings;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PlayerTestBattingStatisticsPrimaryKey)) {
			return false;
		}
		PlayerTestBattingStatisticsPrimaryKey castOther = (PlayerTestBattingStatisticsPrimaryKey) other;
		return (this.playerId == castOther.playerId) && this.matchId.equals(castOther.matchId)
				&& this.innings.equals(castOther.innings);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.playerId;
		hash = hash * prime + this.matchId.hashCode();
		hash = hash * prime + this.innings.hashCode();

		return hash;
	}
}