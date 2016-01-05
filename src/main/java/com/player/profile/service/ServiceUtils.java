package com.player.profile.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.player.profile.bean.FilterCriteria;
import com.player.profile.constants.PlayerProfileConstants;
import com.player.profile.dao.PlayerProfileDao;
import com.player.profile.exception.PlayerProfileException;

public abstract class ServiceUtils {

	public Boolean validateRequestForProfileFetch(final String playerId) {
		Boolean isValidRequest = Boolean.FALSE;
		if (StringUtils.isNotBlank(playerId)) {
			final String playerIdAfterTrim = playerId.trim();
			if (NumberUtils.isNumber(playerIdAfterTrim)) {
				isValidRequest = Boolean.TRUE;
			}
		}
		return isValidRequest;
	}

	public FilterCriteria validateAnalyzeRequest(final String category, final String playType, final String opponent,
			final String timeline, final String filterBy, final String playerId) {
		FilterCriteria filterCriteria = null;
		final Boolean isValidCategory = validateCategory(category);
		final Boolean isValidPlayType = validatePlayType(playType);
		final Boolean isValidOpponent = validateOpponent(opponent);
		final Boolean isValidTimeline = validateTimeline(timeline);
		final Boolean isValidFilterBy = validateFilterBy(filterBy);
		if (BooleanUtils.isTrue(isValidCategory) && BooleanUtils.isTrue(isValidPlayType)
				&& BooleanUtils.isTrue(isValidOpponent) && BooleanUtils.isTrue(isValidTimeline)
				&& BooleanUtils.isTrue(isValidFilterBy)) {
			filterCriteria = new FilterCriteria();
			filterCriteria.setCategory(category.trim());
			filterCriteria.setPlayType(playType.trim());
			filterCriteria.setOpponent(opponent.trim());
			filterCriteria.setTimeline(timeline.trim());
			filterCriteria.setFilterBy(filterBy.trim());
			filterCriteria.setPlayerId(playerId.trim());
		}
		return filterCriteria;
	}

	private Boolean validateCategory(final String category) {
		Boolean isValidCategory = Boolean.FALSE;
		if (StringUtils.isNotBlank(category)) {
			if (PlayerProfileConstants.LIST_OF_VALID_CATEGORIES.contains(category.trim())) {
				isValidCategory = Boolean.TRUE;
			}
		}
		return isValidCategory;
	}

	private Boolean validatePlayType(final String playType) {
		Boolean isValidPlayType = Boolean.FALSE;
		if (StringUtils.isNotBlank(playType)) {
			if (PlayerProfileConstants.LIST_OF_VALID_PLAY_TYPES.contains(playType.trim())) {
				isValidPlayType = Boolean.TRUE;
			}
		}
		return isValidPlayType;
	}

	private Boolean validateOpponent(final String opponent) {
		Boolean isValidOpponent = Boolean.FALSE;
		if (StringUtils.isNotBlank(opponent)) {
			isValidOpponent = Boolean.TRUE;
		}
		return isValidOpponent;
	}

	private Boolean validateTimeline(final String timeline) {
		Boolean isValidTimeline = Boolean.FALSE;
		if (StringUtils.isNotBlank(timeline)) {
			final String timelineTrimmed = timeline.trim();
			if (NumberUtils.isNumber(timelineTrimmed) && StringUtils.length(timelineTrimmed) == 4) {
				isValidTimeline = Boolean.TRUE;
			} else if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timelineTrimmed)) {
				isValidTimeline = Boolean.TRUE;
			}
		}
		return isValidTimeline;
	}

	private Boolean validateFilterBy(final String filterBy) {
		Boolean isValidFilterBy = Boolean.FALSE;
		if (StringUtils.isNotBlank(filterBy)) {
			final String filterByTrimmed = filterBy.trim();
			switch (filterByTrimmed) {
			case PlayerProfileConstants.FILTER_BY_RUNS:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_MINUTES:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_BALLS_FACED:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_NO_OF_FOURS:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_NO_OF_SIXES:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_MATCHES:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_THIRTIES:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_FIFTIES:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_HUNDREDS:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_AVERAGE:
				isValidFilterBy = Boolean.TRUE;
				break;

			case PlayerProfileConstants.FILTER_BY_STRIKE_RATE:
				isValidFilterBy = Boolean.TRUE;
				break;

			default:
				break;
			}
		}
		return isValidFilterBy;
	}

	protected Map<String, List<String>> fetchDataForGraphicalRepresentation(final FilterCriteria filterCriteria,
			final PlayerProfileDao playerProfileDao) throws NumberFormatException, PlayerProfileException {
		final Map<String, List<String>> dataForGraphicalRepresentation = filterByCategory(filterCriteria,
				playerProfileDao);
		return dataForGraphicalRepresentation;
	}

	private Map<String, List<String>> filterByCategory(final FilterCriteria filterCriteria,
			final PlayerProfileDao playerProfileDao) throws NumberFormatException, PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		if (filterCriteria != null) {
			final String category = filterCriteria.getCategory();
			if (category != null) {
				switch (category) {
				case PlayerProfileConstants.CATEGORY_BATTING:
					dataForGraphicalRepresentation = filterByPlayTypeForBatting(filterCriteria, playerProfileDao);
					break;
				case PlayerProfileConstants.CATEGORY_BOWLING:
					// TODO functionality yet to be implemented
					break;
				case PlayerProfileConstants.CATEGORY_FIELDING:
					// TODO functionality yet to be implemented
					break;
				default:
					break;
				}
			}
		}
		return dataForGraphicalRepresentation;
	}

	private Map<String, List<String>> filterByPlayTypeForBatting(final FilterCriteria filterCriteria,
			final PlayerProfileDao playerProfileDao) throws NumberFormatException, PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		final String playType = filterCriteria.getPlayType();
		if (playType != null) {
			switch (playType) {
			case PlayerProfileConstants.PLAY_TYPE_TEST:
				dataForGraphicalRepresentation = performFilterationForTestBatting(filterCriteria, playerProfileDao);
				break;
			case PlayerProfileConstants.PLAY_TYPE_ODI:
				dataForGraphicalRepresentation = performFilterationForOdiBatting(filterCriteria, playerProfileDao);
				break;
			case PlayerProfileConstants.PLAY_TYPE_T20:
				dataForGraphicalRepresentation = performFilterationForT20Batting(filterCriteria, playerProfileDao);
				break;
			default:
				break;
			}
		}
		return dataForGraphicalRepresentation;
	}

	/**
	 * This method analyzes the player's profile in Test matches
	 * 
	 * @param filterCriteria
	 * @param playerProfileDao
	 * @return Map<String, List<String>>
	 * @throws PlayerProfileException
	 * @throws NumberFormatException
	 */

	private Map<String, List<String>> performFilterationForTestBatting(final FilterCriteria filterCriteria,
			final PlayerProfileDao playerProfileDao) throws NumberFormatException, PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		final String filterBy = filterCriteria.getFilterBy();
		final String playerId = filterCriteria.getPlayerId();
		final String opponent = filterCriteria.getOpponent();
		final String timeline = filterCriteria.getTimeline();
		if (filterBy != null && playerId != null) {
			switch (filterBy) {
			case PlayerProfileConstants.FILTER_BY_RUNS:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByRunsAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByRunsAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchTestBattingDataForGraphByRunsAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_BALLS_FACED:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByBallsFacedAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByBallsFacedAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchTestBattingDataForGraphByBallsFacedAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_MINUTES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByMinutesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByMinutesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchTestBattingDataForGraphByMinutesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_NO_OF_FOURS:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByNoOfFoursAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByNoOfFoursAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchTestBattingDataForGraphByNoOfFoursAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_NO_OF_SIXES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByNoOfSixesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByNoOfSixesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchTestBattingDataForGraphByNoOfSixesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_MATCHES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByMatchesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByMatchesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchTestBattingDataForGraphByMatchesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_FIFTIES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByFiftiesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByFiftiesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchTestBattingDataForGraphByFiftiesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_HUNDREDS:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByHundredsAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchTestBattingDataForGraphByHundredsAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchTestBattingDataForGraphByHundredsAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			default:
				break;
			}
		}

		return dataForGraphicalRepresentation;
	}

	/**
	 * This method analyzes the player's profile in ODI matches
	 * 
	 * @param filterCriteria
	 * @param playerProfileDao
	 * @return Map<String, List<String>>
	 * @throws PlayerProfileException
	 * @throws NumberFormatException
	 */

	private Map<String, List<String>> performFilterationForOdiBatting(final FilterCriteria filterCriteria,
			final PlayerProfileDao playerProfileDao) throws NumberFormatException, PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		final String filterBy = filterCriteria.getFilterBy();
		final String playerId = filterCriteria.getPlayerId();
		final String opponent = filterCriteria.getOpponent();
		final String timeline = filterCriteria.getTimeline();
		if (filterBy != null && playerId != null) {
			switch (filterBy) {
			case PlayerProfileConstants.FILTER_BY_RUNS:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByRunsAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByRunsAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchOdiBattingDataForGraphByRunsAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;

			case PlayerProfileConstants.FILTER_BY_BALLS_FACED:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByBallsFacedAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByBallsFacedAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchOdiBattingDataForGraphByBallsFacedAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_MINUTES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByMinutesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByMinutesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchOdiBattingDataForGraphByMinutesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_NO_OF_FOURS:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByNoOfFoursAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByNoOfFoursAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchOdiBattingDataForGraphByNoOfFoursAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_NO_OF_SIXES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByNoOfSixesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByNoOfSixesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchOdiBattingDataForGraphByNoOfSixesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_MATCHES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByMatchesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByMatchesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchOdiBattingDataForGraphByMatchesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_FIFTIES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByFiftiesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByFiftiesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchOdiBattingDataForGraphByFiftiesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_HUNDREDS:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByHundredsAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchOdiBattingDataForGraphByHundredsAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchOdiBattingDataForGraphByHundredsAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			default:
				break;
			}
		}

		return dataForGraphicalRepresentation;
	}

	/**
	 * This method analyzes the player's profile in T20 matches
	 * 
	 * @param filterCriteria
	 * @param playerProfileDao
	 * @return Map<String, List<String>>
	 * @throws PlayerProfileException
	 * @throws NumberFormatException
	 */

	private Map<String, List<String>> performFilterationForT20Batting(final FilterCriteria filterCriteria,
			final PlayerProfileDao playerProfileDao) throws NumberFormatException, PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		final String filterBy = filterCriteria.getFilterBy();
		final String playerId = filterCriteria.getPlayerId();
		final String opponent = filterCriteria.getOpponent();
		final String timeline = filterCriteria.getTimeline();
		if (filterBy != null && playerId != null) {
			switch (filterBy) {
			case PlayerProfileConstants.FILTER_BY_RUNS:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByRunsAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByRunsAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchT20BattingDataForGraphByRunsAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_BALLS_FACED:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByBallsFacedAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByBallsFacedAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchT20BattingDataForGraphByBallsFacedAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_MINUTES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByMinutesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByMinutesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchT20BattingDataForGraphByMinutesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_NO_OF_FOURS:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByNoOfFoursAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByNoOfFoursAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchT20BattingDataForGraphByNoOfFoursAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_NO_OF_SIXES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByNoOfSixesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByNoOfSixesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchT20BattingDataForGraphByNoOfSixesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_MATCHES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByMatchesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByMatchesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchT20BattingDataForGraphByMatchesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			case PlayerProfileConstants.FILTER_BY_THIRTIES:
				if (StringUtils.isNotBlank(opponent) && StringUtils.isNotBlank(timeline)) {
					// Fetch the records against all teams
					if (StringUtils.equals(PlayerProfileConstants.OPPONENT_ALL, opponent)) {
						// Entire Career
						if (StringUtils.equals(PlayerProfileConstants.TIMELINE_CAREER, timeline)) {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByThirtiesAgainstAllTeamsForEntireCareer(
											Integer.parseInt(playerId));
						} // Selected Year
						else {
							dataForGraphicalRepresentation = playerProfileDao
									.fetchT20BattingDataForGraphByThirtiesAgainstAllTeamsForAnYear(
											Integer.parseInt(playerId), Integer.parseInt(timeline));
						}
					} // Fetch the records against the selected team for the
						// entire career
					else {
						dataForGraphicalRepresentation = playerProfileDao
								.fetchT20BattingDataForGraphByThirtiesAgainstAnOpposition(Integer.parseInt(playerId),
										opponent);
					}
				}
				break;
			default:
				break;
			}
		}

		return dataForGraphicalRepresentation;
	}
}
