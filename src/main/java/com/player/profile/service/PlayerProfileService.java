package com.player.profile.service;

import java.util.List;
import java.util.Map;

import com.player.profile.bean.PersonalDetails;
import com.player.profile.exception.PlayerProfileException;

public interface PlayerProfileService {

	public Map<String, List<String>> fetchCountriesAndPlayersDetails() throws PlayerProfileException;

	public PersonalDetails getPlayerPersonalProfile(final String playerId) throws PlayerProfileException;

	public Map<String, List<String>> performPlayerProfileAnalysis(final String category, final String playType,
			final String opponent, final String timeline, final String filterBy, final String playerId)
					throws PlayerProfileException;

}
