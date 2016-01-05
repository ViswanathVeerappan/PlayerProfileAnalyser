package com.player.profile.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.player.profile.bean.CumulativeStatistics;
import com.player.profile.bean.FilterCriteria;
import com.player.profile.bean.PersonalDetails;
import com.player.profile.constants.PlayerProfileConstants;
import com.player.profile.dao.PlayerProfileDao;
import com.player.profile.exception.PlayerProfileException;
import com.player.profile.messages.InfoMessageConstants;

@Service
public class PlayerProfileServiceImpl extends ServiceUtils implements PlayerProfileService {

	@Autowired
	PlayerProfileDao playerProfileDao;

	/**
	 * This method fetches the names of the players representing the cricket
	 * playing nation
	 * 
	 * @throws PlayerProfileException
	 * 
	 */

	public Map<String, List<String>> fetchCountriesAndPlayersDetails() throws PlayerProfileException {
		final Map<String, List<String>> countriesAndPlayersMap = new LinkedHashMap<String, List<String>>();
		try {
			for (String country : PlayerProfileConstants.COUNTRIES_PLAYING_CRICKET) {
				final List<String> playersList = playerProfileDao.fetchPlayersRepresentingTheCountry(country);
				if (CollectionUtils.isNotEmpty(playersList)) {
					countriesAndPlayersMap.put(country, playersList);
				}
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_02);
		}
		return countriesAndPlayersMap;
	}

	/**
	 * This method fetches the personal profile the selected player
	 * 
	 * @throws PlayerProfileException
	 */
	public PersonalDetails getPlayerPersonalProfile(final String playerId) throws PlayerProfileException {
		PersonalDetails personalDetails = null;
		try {
			if (BooleanUtils.isTrue(validateRequestForProfileFetch(playerId))) {
				final String playerIdAfterTrim = playerId.trim();
				personalDetails = playerProfileDao.fetchPlayerBioData(Integer.parseInt(playerIdAfterTrim));
				if (personalDetails != null) {
					personalDetails.setImageLocation(
							PlayerProfileConstants.IMAGE_LOCATION + personalDetails.getName().trim() + ".jpg");
					final CumulativeStatistics testCumulativeStatistics = playerProfileDao
							.fetchPlayerTestCumulativeStatistics(Integer.parseInt(playerIdAfterTrim));
					personalDetails.setTestCumulativeStatistics(testCumulativeStatistics);
					final CumulativeStatistics odiCumulativeStatistics = playerProfileDao
							.fetchPlayerOdiCumulativeStatistics(Integer.parseInt(playerIdAfterTrim));
					personalDetails.setOdiCumulativeStatistics(odiCumulativeStatistics);
					final CumulativeStatistics t20CumulativeStatistics = playerProfileDao
							.fetchPlayerT20CumulativeStatistics(Integer.parseInt(playerIdAfterTrim));
					personalDetails.setT20CumulativeStatistics(t20CumulativeStatistics);
				}
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_02);
		}
		return personalDetails;
	}

	/**
	 * This method performs the analysis on the player's profile
	 * 
	 * @throws PlayerProfileException
	 */
	public Map<String, List<String>> performPlayerProfileAnalysis(final String category, final String playType,
			final String opponent, final String timeline, final String filterBy, final String playerId)
					throws PlayerProfileException {
		Map<String, List<String>> playerProfileAnalysisMap = null;
		try {
			final FilterCriteria filterCriteria = validateAnalyzeRequest(category, playType, opponent, timeline,
					filterBy, playerId);
			if (filterCriteria != null) {
				playerProfileAnalysisMap = fetchDataForGraphicalRepresentation(filterCriteria, playerProfileDao);
			}
		} catch (NumberFormatException e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_02);
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_02);
		}
		return playerProfileAnalysisMap;
	}

}
