package com.player.profile.dao;

import java.util.List;
import java.util.Map;

import com.player.profile.bean.CumulativeStatistics;
import com.player.profile.bean.PersonalDetails;
import com.player.profile.exception.PlayerProfileException;

public interface PlayerProfileDao {

	public List<String> fetchPlayersRepresentingTheCountry(final String countryName) throws PlayerProfileException;

	public PersonalDetails fetchPlayerBioData(final int playerId) throws PlayerProfileException;

	public CumulativeStatistics fetchPlayerTestCumulativeStatistics(final int playerId) throws PlayerProfileException;

	public CumulativeStatistics fetchPlayerOdiCumulativeStatistics(final int playerId) throws PlayerProfileException;

	public CumulativeStatistics fetchPlayerT20CumulativeStatistics(final int playerId) throws PlayerProfileException;

	// Filter by runs start
	public Map<String, List<String>> fetchTestBattingDataForGraphByRunsAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByRunsAgainstAllTeamsForEntireCareer(final int playerId)
			throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByRunsAgainstAllTeamsForEntireCareer(final int playerId)
			throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByRunsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByRunsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByRunsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByRunsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByRunsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByRunsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;
			// Filter by runs start

	// Filter by balls faced start
	public Map<String, List<String>> fetchTestBattingDataForGraphByBallsFacedAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByBallsFacedAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByBallsFacedAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByBallsFacedAgainstAllTeamsForAnYear(
			final int playerId, final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByBallsFacedAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByBallsFacedAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByBallsFacedAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByBallsFacedAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByBallsFacedAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;
			// Filter by balls faced end

	// Filter by minutes start
	public Map<String, List<String>> fetchTestBattingDataForGraphByMinutesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByMinutesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByMinutesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByMinutesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByMinutesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByMinutesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByMinutesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByMinutesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByMinutesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;
			// Filter by minutes end

	// Filter by number of fours data start
	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfFoursAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfFoursAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfFoursAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfFoursAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfFoursAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfFoursAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfFoursAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfFoursAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfFoursAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;
			// Filter by number of fours data end

	// Filter by number of sixes data start
	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfSixesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfSixesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfSixesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfSixesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfSixesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfSixesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfSixesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfSixesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfSixesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;
			// Filter by number of sixes data end

	// Filter by number of matches start
	public Map<String, List<String>> fetchTestBattingDataForGraphByMatchesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByMatchesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByMatchesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByMatchesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByMatchesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByMatchesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByMatchesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByMatchesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByMatchesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;
			// Filter by number of matches end

	// Filter by runs greater than 30 in T20 start
	public Map<String, List<String>> fetchT20BattingDataForGraphByThirtiesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByThirtiesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchT20BattingDataForGraphByThirtiesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;
			// Filter by runs greater than 30 in T20 end

	// Filter by runs greater than 50 in Test and ODI start
	public Map<String, List<String>> fetchTestBattingDataForGraphByFiftiesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByFiftiesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByFiftiesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByFiftiesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByFiftiesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByFiftiesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;
			// Filter by runs greater than 50 in Test and ODI end

	// Filter by centuries start
	public Map<String, List<String>> fetchTestBattingDataForGraphByHundredsAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByHundredsAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByHundredsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByHundredsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException;

	public Map<String, List<String>> fetchTestBattingDataForGraphByHundredsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	public Map<String, List<String>> fetchOdiBattingDataForGraphByHundredsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException;

	// Filter by centuries end

}
