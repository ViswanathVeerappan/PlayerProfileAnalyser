package com.player.profile.servlet;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.player.profile.bean.PersonalDetails;
import com.player.profile.constants.PlayerProfileConstants;
import com.player.profile.exception.PlayerProfileException;
import com.player.profile.messages.InfoMessageConstants;
import com.player.profile.service.PlayerProfileService;

@Controller
@Scope("session")
public class PlayerProfileServlet {

	ModelAndView modelAndView;

	@Autowired
	PlayerProfileService playerProfileService;

	private String selectedPlayer;

	private Map<String, List<String>> playersMap;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView initializeHomePage() {
		modelAndView = new ModelAndView("PlayerProfileHome");
		try {
			playersMap = playerProfileService.fetchCountriesAndPlayersDetails();
			modelAndView.addObject("CountriesAndPlayersMap", playersMap);
		} catch (PlayerProfileException e) {
			if (InfoMessageConstants.SERVER_ERROR_MESSAGES.contains(e.getMessage())) {
				modelAndView = new ModelAndView("PlayerProfileHome");
				if (playersMap != null) {
					modelAndView.addObject("CountriesAndPlayersMap", playersMap);
				}
				modelAndView.addObject("errorMessage", InfoMessageConstants.MESSAGE_ID_02);
				modelAndView.addObject("typeOfView", PlayerProfileConstants.EXCEPTION_VIEW);
			}
		}
		return modelAndView;
	}

	@RequestMapping(value = "/personalProfile.do", method = RequestMethod.POST)
	public ModelAndView getPlayerPersonalprofile(@RequestParam("countrySelect") final String countryName,
			@RequestParam("playerSelect") final String playerId) {
		try {
			final PersonalDetails personalDetails = playerProfileService.getPlayerPersonalProfile(playerId);
			if (modelAndView != null && personalDetails != null) {
				selectedPlayer = playerId.trim();
				modelAndView.addObject("personalDetails", personalDetails);
				modelAndView.addObject("typeOfView", PlayerProfileConstants.PROFILE_VIEW);
			} else {
				modelAndView = new ModelAndView("PlayerProfileHome");
				modelAndView = new ModelAndView("PlayerProfileHome");
				playersMap = playerProfileService.fetchCountriesAndPlayersDetails();
				modelAndView.addObject("CountriesAndPlayersMap", playersMap);
			}
		} catch (PlayerProfileException e) {
			if (InfoMessageConstants.SERVER_ERROR_MESSAGES.contains(e.getMessage())) {
				modelAndView = new ModelAndView("PlayerProfileHome");
				if (playersMap != null) {
					modelAndView.addObject("CountriesAndPlayersMap", playersMap);
				}
				modelAndView.addObject("typeOfView", PlayerProfileConstants.EXCEPTION_VIEW);
				modelAndView.addObject("errorMessage", InfoMessageConstants.MESSAGE_ID_02);
			}
		}
		return modelAndView;
	}

	@RequestMapping(value = "/analyzeProfile.do", method = RequestMethod.POST)
	public ModelAndView analysePlayerProfile(@RequestParam("categorySelect") final String category,
			@RequestParam("playTypeSelect") final String playType,
			@RequestParam("opponentSelect") final String opponent,
			@RequestParam("timelineSelect") final String timeline,
			@RequestParam("filterBySelect") final String filterBy) {
		try {
			if (StringUtils.isNotBlank(selectedPlayer) && modelAndView != null) {
				modelAndView.addObject("category", category);
				modelAndView.addObject("playType", playType);
				modelAndView.addObject("opponent", opponent);
				modelAndView.addObject("timeline", timeline);
				modelAndView.addObject("filterBy", filterBy);
				final Map<String, List<String>> dataForGraphicalRepresentation = playerProfileService
						.performPlayerProfileAnalysis(category, playType, opponent, timeline, filterBy, selectedPlayer);
				if (dataForGraphicalRepresentation != null) {
					modelAndView.addObject("typeOfView", PlayerProfileConstants.ANALYSIS_VIEW);
					modelAndView.addObject("dataForGraphicalRepresentation", dataForGraphicalRepresentation);
					modelAndView.addObject("message", "");
				} else {
					modelAndView.addObject("typeOfView", PlayerProfileConstants.ANALYSIS_VIEW);
					modelAndView.addObject("message", InfoMessageConstants.MESSAGE_ID_19);
				}
			} else {
				modelAndView = new ModelAndView("PlayerProfileHome");
				playersMap = playerProfileService.fetchCountriesAndPlayersDetails();
				modelAndView.addObject("CountriesAndPlayersMap", playersMap);
			}
		} catch (PlayerProfileException e) {
			if (InfoMessageConstants.SERVER_ERROR_MESSAGES.contains(e.getMessage())) {
				modelAndView = new ModelAndView("PlayerProfileHome");
				modelAndView.addObject("errorMessage", InfoMessageConstants.MESSAGE_ID_02);
				modelAndView.addObject("typeOfView", PlayerProfileConstants.EXCEPTION_VIEW);
				if (playersMap != null) {
					modelAndView.addObject("CountriesAndPlayersMap", playersMap);
				}
			} else {
				modelAndView.addObject("typeOfView", PlayerProfileConstants.ANALYSIS_VIEW);
				modelAndView.addObject("message", e.getMessage());
			}
		}
		return modelAndView;
	}

}
