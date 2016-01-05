package com.player.profile.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.player.profile.bean.CumulativeStatistics;
import com.player.profile.bean.DebutDetails;
import com.player.profile.bean.PersonalDetails;
import com.player.profile.constants.PlayerProfileConstants;
import com.player.profile.exception.PlayerProfileException;
import com.player.profile.messages.InfoMessageConstants;
import com.player.profile.model.Player;
import com.player.profile.model.PlayerOdiBattingStatistics;
import com.player.profile.model.PlayerT20BattingStatistics;
import com.player.profile.model.PlayerTestBattingStatistics;
import com.player.profile.model.PlayersDetails;

@Repository
public class PlayerProfileDaoImpl implements PlayerProfileDao {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	@Transactional
	public List<String> fetchPlayersRepresentingTheCountry(final String countryName) throws PlayerProfileException {
		final List<String> playersList = new ArrayList<String>();
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select p1 from Player p1 inner join p1.playersDetail p2 where p2.teamsPlayedFor"
						+ " like :countryName order by p1.playerName";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("countryName", countryName + PlayerProfileConstants.WILDCARD);
				@SuppressWarnings("unchecked")
				final List<Object> playerObjectsList = query.list();
				if (CollectionUtils.isNotEmpty(playerObjectsList)) {
					for (int i = 0; i < playerObjectsList.size(); i++) {
						final Player player = (Player) playerObjectsList.get(i);
						final String playerId = String.valueOf(player.getPlayerId());
						final String playerName = player.getPlayerName();
						playersList.add(playerId + ":" + playerName);
					}
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return playersList;
	}

	@Override
	@Transactional
	public PersonalDetails fetchPlayerBioData(final int playerId) throws PlayerProfileException {
		PersonalDetails personalDetails = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "from PlayersDetails p1 where p1.playerId = :playerId";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				@SuppressWarnings("unchecked")
				final List<Object> objectList = query.list();
				final String hqlQuery1 = "from Player p1 where p1.playerId = :playerId";
				final Query query1 = getSessionFactory().getCurrentSession().createQuery(hqlQuery1);
				query1.setParameter("playerId", playerId);
				@SuppressWarnings("unchecked")
				final List<Object> playerNameList = query1.list();
				if (CollectionUtils.isNotEmpty(objectList) && CollectionUtils.isNotEmpty(playerNameList)) {
					final PlayersDetails playersDetails = (PlayersDetails) objectList.get(0);
					final Player player = (Player) playerNameList.get(0);
					if (playersDetails != null && player != null) {
						personalDetails = new PersonalDetails();
						personalDetails.setName(player.getPlayerName());
						personalDetails.setFullName(playersDetails.getFullName());
						personalDetails.setBirthDate(playersDetails.getBirthDate());
						personalDetails.setBirthPlace(playersDetails.getBirthPlace());
						personalDetails.setTeamsPlayedFor(playersDetails.getTeamsPlayedFor());
						personalDetails.setRole(playersDetails.getRole());
						personalDetails.setBattingStyle(playersDetails.getBattingStyle());
						personalDetails.setBowlingStyle(playersDetails.getBowlingStyle());
						final DebutDetails testDebutDetails = new DebutDetails();
						testDebutDetails.setDebutAgainst(playersDetails.getTestDebutAgainst());
						testDebutDetails.setDebutDate(playersDetails.getTestDebutDate());
						personalDetails.setTestDebutDetails(testDebutDetails);
						final DebutDetails odiDebutDetails = new DebutDetails();
						odiDebutDetails.setDebutAgainst(playersDetails.getOdiDebutAgainst());
						odiDebutDetails.setDebutDate(playersDetails.getOdiDebutDate());
						personalDetails.setOdiDebutDetails(odiDebutDetails);
						final DebutDetails t20DebutDetails = new DebutDetails();
						t20DebutDetails.setDebutAgainst(playersDetails.getT20iDebutAgainst());
						t20DebutDetails.setDebutDate(playersDetails.getT20iDebutDate());
						personalDetails.setT20DebutDetails(t20DebutDetails);
						final String hqlQuery2 = "Select distinct (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics where "
								+ "id.playerId = :playerId order by date ASC";
						final Query query2 = getSessionFactory().getCurrentSession().createQuery(hqlQuery2);
						query2.setParameter("playerId", playerId);
						@SuppressWarnings("unchecked")
						final List<String> playerTestCareerTimelineList = query2.list();
						if (CollectionUtils.isNotEmpty(playerTestCareerTimelineList)) {
							personalDetails.setTestCareerTimeline(playerTestCareerTimelineList);
						}
						final String hqlQuery3 = "Select distinct (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics where "
								+ "id.playerId = :playerId order by date ASC";
						final Query query3 = getSessionFactory().getCurrentSession().createQuery(hqlQuery3);
						query3.setParameter("playerId", playerId);
						@SuppressWarnings("unchecked")
						final List<String> playerOdiCareerTimelineList = query3.list();
						if (CollectionUtils.isNotEmpty(playerOdiCareerTimelineList)) {
							personalDetails.setOdiCareerTimeline(playerOdiCareerTimelineList);
						}
						final String hqlQuery4 = "Select distinct (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics where "
								+ "id.playerId = :playerId order by date ASC";
						final Query query4 = getSessionFactory().getCurrentSession().createQuery(hqlQuery4);
						query4.setParameter("playerId", playerId);
						@SuppressWarnings("unchecked")
						final List<String> playerT20CareerTimelineList = query4.list();
						if (CollectionUtils.isNotEmpty(playerT20CareerTimelineList)) {
							personalDetails.setT20CareerTimeline(playerT20CareerTimelineList);
						}
						final Criteria criteriaTestOpponents = getSessionFactory().getCurrentSession()
								.createCriteria(PlayerTestBattingStatistics.class);
						criteriaTestOpponents.add(Restrictions.eq("id.playerId", playerId));
						criteriaTestOpponents.setProjection(Projections.distinct(Projections.property("opposition")));
						criteriaTestOpponents.addOrder(Order.asc("opposition"));
						@SuppressWarnings("unchecked")
						final List<String> testOpponentsList = criteriaTestOpponents.list();
						if (CollectionUtils.isNotEmpty(testOpponentsList)) {
							personalDetails.setTestOpponentsList(testOpponentsList);
						}
						final Criteria criteriaOdiOpponents = getSessionFactory().getCurrentSession()
								.createCriteria(PlayerOdiBattingStatistics.class);
						criteriaOdiOpponents.add(Restrictions.eq("id.playerId", playerId));
						criteriaOdiOpponents.setProjection(Projections.distinct(Projections.property("opposition")));
						criteriaOdiOpponents.addOrder(Order.asc("opposition"));
						@SuppressWarnings("unchecked")
						final List<String> odiOpponentsList = criteriaOdiOpponents.list();
						if (CollectionUtils.isNotEmpty(odiOpponentsList)) {
							personalDetails.setOdiOpponentsList(odiOpponentsList);
						}
						final Criteria criteriaT20Opponents = getSessionFactory().getCurrentSession()
								.createCriteria(PlayerT20BattingStatistics.class);
						criteriaT20Opponents.add(Restrictions.eq("id.playerId", playerId));
						criteriaT20Opponents.setProjection(Projections.distinct(Projections.property("opposition")));
						criteriaT20Opponents.addOrder(Order.asc("opposition"));
						@SuppressWarnings("unchecked")
						final List<String> t20OpponentsList = criteriaT20Opponents.list();
						if (CollectionUtils.isNotEmpty(t20OpponentsList)) {
							personalDetails.setT20OpponentsList(t20OpponentsList);
						}
					}
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return personalDetails;
	}

	@Override
	@Transactional
	public CumulativeStatistics fetchPlayerTestCumulativeStatistics(final int playerId) throws PlayerProfileException {
		CumulativeStatistics testCumulativeStatistics = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final Criteria criteriaMatchCount = getSessionFactory().getCurrentSession()
						.createCriteria(PlayerTestBattingStatistics.class)
						.setProjection(Projections.countDistinct("id.matchId"));
				criteriaMatchCount.add(Restrictions.eq("id.playerId", playerId));
				final Long matchCount = (Long) criteriaMatchCount.uniqueResult();
				if (matchCount != 0) {
					testCumulativeStatistics = new CumulativeStatistics();
					testCumulativeStatistics.setNoOfMatches(String.valueOf(matchCount));
					final Criteria criteriaInningsCount = getSessionFactory().getCurrentSession()
							.createCriteria(PlayerTestBattingStatistics.class)
							.setProjection(Projections.count("id.innings"));
					criteriaInningsCount.add(Restrictions.not(Restrictions.in("runs", Arrays.asList("DNB", "TDNB"))));
					criteriaInningsCount.add(Restrictions.eq("id.playerId", playerId));
					final Long inningsCount = (Long) criteriaInningsCount.uniqueResult();
					testCumulativeStatistics.setNoOfInnings(String.valueOf(inningsCount));
					final String hqlQuery = "Select sum(runs), sum(ballsFaced), sum(noOfFours), sum(noOfSixes) from"
							+ " PlayerTestBattingStatistics where id.playerId = :playerId  and runs not in ('DNB','TDNB')";
					final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
					query.setParameter("playerId", playerId);
					@SuppressWarnings("unchecked")
					final List<Object[]> groupList = query.list();
					if (CollectionUtils.isNotEmpty(groupList)) {
						for (Object[] arr : groupList) {
							testCumulativeStatistics.setTotalRuns((String) arr[0]);
							testCumulativeStatistics.setTotalBallsFaced((String) arr[1]);
							testCumulativeStatistics.setNoOfFours((String) arr[2]);
							testCumulativeStatistics.setNoOfSixes((String) arr[3]);
							final Float strikeRateValue = (Float.valueOf(testCumulativeStatistics.getTotalRuns())
									/ Float.valueOf(testCumulativeStatistics.getTotalBallsFaced())) * 100;
							testCumulativeStatistics.setStrikeRate(String.format("%.2f", strikeRateValue));
						}
					}
					final String hqlQuery1 = "Select count(dismissal) from PlayerTestBattingStatistics where id.playerId = :playerId  and"
							+ " DISMISSAL = 'not out'";
					final Query query1 = getSessionFactory().getCurrentSession().createQuery(hqlQuery1);
					query1.setParameter("playerId", playerId);
					final Long dismissalCount = (Long) query1.uniqueResult();
					if (dismissalCount != null) {
						testCumulativeStatistics.setNoOfNotOuts(String.valueOf(dismissalCount));
						final Integer denominator = (Integer.valueOf(testCumulativeStatistics.getNoOfInnings())
								- (Integer.valueOf(testCumulativeStatistics.getNoOfNotOuts())));
						if (denominator != 0) {
							final Float average = (Float.valueOf(testCumulativeStatistics.getTotalRuns())
									/ denominator);
							testCumulativeStatistics.setBattingAverage(String.format("%.2f", average));
						} else {
							testCumulativeStatistics.setBattingAverage("-");
						}
					}
					final String hqlQuery2 = "Select count(runs) from PlayerTestBattingStatistics where id.playerId = :playerId  and"
							+ " runs not in ('DNB','TDNB') and CAST(runs AS int)  >= 100";
					final Query query2 = getSessionFactory().getCurrentSession().createQuery(hqlQuery2);
					query2.setParameter("playerId", playerId);
					final Long noOfHundreds = (Long) query2.uniqueResult();
					if (noOfHundreds != null) {
						testCumulativeStatistics.setNoOfCenturies(String.valueOf(noOfHundreds));
					}
					final String hqlQuery3 = "Select count(runs) from PlayerTestBattingStatistics where id.playerId = :playerId  and"
							+ " runs not in ('DNB','TDNB') and CAST(runs AS int)  between 50 and 99";
					final Query query3 = getSessionFactory().getCurrentSession().createQuery(hqlQuery3);
					query3.setParameter("playerId", playerId);
					final Long noOfFifties = (Long) query3.uniqueResult();
					if (noOfFifties != null) {
						testCumulativeStatistics.setNoOfFifties(String.valueOf(noOfFifties));
					}
					final String hqlQuery4 = "Select max(CAST(runs AS int)) from PlayerTestBattingStatistics where id.playerId = :playerId";
					final Query query4 = getSessionFactory().getCurrentSession().createQuery(hqlQuery4);
					query4.setParameter("playerId", playerId);
					final Integer maxScore = (Integer) query4.uniqueResult();
					if (maxScore != null) {
						final Criteria criteriaDismissalType = getSessionFactory().getCurrentSession()
								.createCriteria(PlayerTestBattingStatistics.class);
						criteriaDismissalType.add(Restrictions.eq("id.playerId", playerId));
						criteriaDismissalType.add(Restrictions.eq("runs", String.valueOf(maxScore)));
						String dismissalType = null;
						@SuppressWarnings("unchecked")
						final List<PlayerTestBattingStatistics> playerTestBattingStatisticsList = criteriaDismissalType
								.list();
						for (int i = 0; i < playerTestBattingStatisticsList.size(); i++) {
							if (StringUtils.equalsIgnoreCase(playerTestBattingStatisticsList.get(i).getDismissal(),
									PlayerProfileConstants.NOT_OUT)) {
								dismissalType = playerTestBattingStatisticsList.get(i).getDismissal();
							}
						}
						if (dismissalType != null) {
							testCumulativeStatistics.setHighestScore(String.valueOf(maxScore) + "*");
						} else {
							testCumulativeStatistics.setHighestScore(String.valueOf(maxScore));
						}
					}
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return testCumulativeStatistics;
	}

	@Override
	@Transactional
	public CumulativeStatistics fetchPlayerOdiCumulativeStatistics(final int playerId) throws PlayerProfileException {
		CumulativeStatistics odiCumulativeStatistics = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final Criteria criteriaMatchCount = getSessionFactory().getCurrentSession()
						.createCriteria(PlayerOdiBattingStatistics.class)
						.setProjection(Projections.countDistinct("id.matchId"));
				criteriaMatchCount.add(Restrictions.eq("id.playerId", playerId));
				final Long matchCount = (Long) criteriaMatchCount.uniqueResult();
				if (matchCount != 0) {
					odiCumulativeStatistics = new CumulativeStatistics();
					odiCumulativeStatistics.setNoOfMatches(String.valueOf(matchCount));
					final Criteria criteriaInningsCount = getSessionFactory().getCurrentSession()
							.createCriteria(PlayerOdiBattingStatistics.class)
							.setProjection(Projections.count("id.innings"));
					criteriaInningsCount.add(Restrictions.not(Restrictions.in("runs", Arrays.asList("DNB", "TDNB"))));
					criteriaInningsCount.add(Restrictions.eq("id.playerId", playerId));
					final Long inningsCount = (Long) criteriaInningsCount.uniqueResult();
					odiCumulativeStatistics.setNoOfInnings(String.valueOf(inningsCount));
					final String hqlQuery = "Select sum(runs), sum(ballsFaced), sum(noOfFours), sum(noOfSixes) from"
							+ " PlayerOdiBattingStatistics where id.playerId = :playerId  and runs not in ('DNB','TDNB')";
					final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
					query.setParameter("playerId", playerId);
					@SuppressWarnings("unchecked")
					final List<Object[]> groupList = query.list();
					if (CollectionUtils.isNotEmpty(groupList)) {
						for (Object[] arr : groupList) {
							odiCumulativeStatistics.setTotalRuns((String) arr[0]);
							odiCumulativeStatistics.setTotalBallsFaced((String) arr[1]);
							odiCumulativeStatistics.setNoOfFours((String) arr[2]);
							odiCumulativeStatistics.setNoOfSixes((String) arr[3]);
							final Float strikeRateValue = (Float.valueOf(odiCumulativeStatistics.getTotalRuns())
									/ Float.valueOf(odiCumulativeStatistics.getTotalBallsFaced())) * 100;
							odiCumulativeStatistics.setStrikeRate(String.format("%.2f", strikeRateValue));
						}
					}
					final String hqlQuery1 = "Select count(dismissal) from PlayerOdiBattingStatistics where id.playerId = :playerId  and"
							+ " DISMISSAL = 'not out'";
					final Query query1 = getSessionFactory().getCurrentSession().createQuery(hqlQuery1);
					query1.setParameter("playerId", playerId);
					final Long dismissalCount = (Long) query1.uniqueResult();
					if (dismissalCount != null) {
						odiCumulativeStatistics.setNoOfNotOuts(String.valueOf(dismissalCount));
						final Integer denominator = (Integer.valueOf(odiCumulativeStatistics.getNoOfInnings())
								- (Integer.valueOf(odiCumulativeStatistics.getNoOfNotOuts())));
						if (denominator != 0) {
							final Float average = (Float.valueOf(odiCumulativeStatistics.getTotalRuns()) / denominator);
							odiCumulativeStatistics.setBattingAverage(String.format("%.2f", average));
						} else {
							odiCumulativeStatistics.setBattingAverage("-");
						}
					}
					final String hqlQuery2 = "Select count(runs) from PlayerOdiBattingStatistics where id.playerId = :playerId  and"
							+ " runs not in ('DNB','TDNB') and CAST(runs AS int)  >= 100";
					final Query query2 = getSessionFactory().getCurrentSession().createQuery(hqlQuery2);
					query2.setParameter("playerId", playerId);
					final Long noOfHundreds = (Long) query2.uniqueResult();
					if (noOfHundreds != null) {
						odiCumulativeStatistics.setNoOfCenturies(String.valueOf(noOfHundreds));
					}
					final String hqlQuery3 = "Select count(runs) from PlayerOdiBattingStatistics where id.playerId = :playerId  and"
							+ " runs not in ('DNB','TDNB') and CAST(runs AS int)  between 50 and 99";
					final Query query3 = getSessionFactory().getCurrentSession().createQuery(hqlQuery3);
					query3.setParameter("playerId", playerId);
					final Long noOfFifties = (Long) query3.uniqueResult();
					if (noOfFifties != null) {
						odiCumulativeStatistics.setNoOfFifties(String.valueOf(noOfFifties));
					}
					final String hqlQuery4 = "Select max(CAST(runs AS int)) from PlayerOdiBattingStatistics where id.playerId = :playerId";
					final Query query4 = getSessionFactory().getCurrentSession().createQuery(hqlQuery4);
					query4.setParameter("playerId", playerId);
					final Integer maxScore = (Integer) query4.uniqueResult();
					if (maxScore != null) {
						final Criteria criteriaDismissalType = getSessionFactory().getCurrentSession()
								.createCriteria(PlayerOdiBattingStatistics.class);
						criteriaDismissalType.add(Restrictions.eq("id.playerId", playerId));
						criteriaDismissalType.add(Restrictions.eq("runs", String.valueOf(maxScore)));
						String dismissalType = null;
						@SuppressWarnings("unchecked")
						final List<PlayerOdiBattingStatistics> playerOdiBattingStatisticsList = criteriaDismissalType
								.list();
						for (int i = 0; i < playerOdiBattingStatisticsList.size(); i++) {
							if (StringUtils.equalsIgnoreCase(playerOdiBattingStatisticsList.get(i).getDismissal(),
									PlayerProfileConstants.NOT_OUT)) {
								dismissalType = playerOdiBattingStatisticsList.get(i).getDismissal();
							}
						}
						if (dismissalType != null) {
							odiCumulativeStatistics.setHighestScore(String.valueOf(maxScore) + "*");
						} else {
							odiCumulativeStatistics.setHighestScore(String.valueOf(maxScore));
						}
					}
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return odiCumulativeStatistics;
	}

	@Override
	@Transactional
	public CumulativeStatistics fetchPlayerT20CumulativeStatistics(final int playerId) throws PlayerProfileException {
		CumulativeStatistics t20CumulativeStatistics = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final Criteria criteriaMatchCount = getSessionFactory().getCurrentSession()
						.createCriteria(PlayerT20BattingStatistics.class)
						.setProjection(Projections.countDistinct("id.matchId"));
				criteriaMatchCount.add(Restrictions.eq("id.playerId", playerId));
				final Long matchCount = (Long) criteriaMatchCount.uniqueResult();
				if (matchCount != 0) {
					t20CumulativeStatistics = new CumulativeStatistics();
					t20CumulativeStatistics.setNoOfMatches(String.valueOf(matchCount));
					final Criteria criteriaInningsCount = getSessionFactory().getCurrentSession()
							.createCriteria(PlayerT20BattingStatistics.class)
							.setProjection(Projections.count("id.innings"));
					criteriaInningsCount.add(Restrictions.not(Restrictions.in("runs", Arrays.asList("DNB", "TDNB"))));
					criteriaInningsCount.add(Restrictions.eq("id.playerId", playerId));
					final Long inningsCount = (Long) criteriaInningsCount.uniqueResult();
					t20CumulativeStatistics.setNoOfInnings(String.valueOf(inningsCount));
					final String hqlQuery = "Select sum(runs), sum(ballsFaced), sum(noOfFours), sum(noOfSixes) from"
							+ " PlayerT20BattingStatistics where id.playerId = :playerId  and runs not in ('DNB','TDNB')";
					final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
					query.setParameter("playerId", playerId);
					@SuppressWarnings("unchecked")
					final List<Object[]> groupList = query.list();
					if (CollectionUtils.isNotEmpty(groupList)) {
						for (Object[] arr : groupList) {
							t20CumulativeStatistics.setTotalRuns((String) arr[0]);
							t20CumulativeStatistics.setTotalBallsFaced((String) arr[1]);
							t20CumulativeStatistics.setNoOfFours((String) arr[2]);
							t20CumulativeStatistics.setNoOfSixes((String) arr[3]);
							final Float strikeRateValue = (Float.valueOf(t20CumulativeStatistics.getTotalRuns())
									/ Float.valueOf(t20CumulativeStatistics.getTotalBallsFaced())) * 100;
							t20CumulativeStatistics.setStrikeRate(String.format("%.2f", strikeRateValue));
						}
					}
					final String hqlQuery1 = "Select count(dismissal) from PlayerT20BattingStatistics where id.playerId = :playerId  and"
							+ " DISMISSAL = 'not out'";
					final Query query1 = getSessionFactory().getCurrentSession().createQuery(hqlQuery1);
					query1.setParameter("playerId", playerId);
					final Long dismissalCount = (Long) query1.uniqueResult();
					if (dismissalCount != null) {
						t20CumulativeStatistics.setNoOfNotOuts(String.valueOf(dismissalCount));
						final Integer denominator = (Integer.valueOf(t20CumulativeStatistics.getNoOfInnings())
								- (Integer.valueOf(t20CumulativeStatistics.getNoOfNotOuts())));
						if (denominator != 0) {
							final Float average = (Float.valueOf(t20CumulativeStatistics.getTotalRuns()) / denominator);
							t20CumulativeStatistics.setBattingAverage(String.format("%.2f", average));
						} else {
							t20CumulativeStatistics.setBattingAverage("-");
						}
					}
					final String hqlQuery2 = "Select count(runs) from PlayerT20BattingStatistics where id.playerId = :playerId  and"
							+ " runs not in ('DNB','TDNB') and CAST(runs AS int)  >= 100";
					final Query query2 = getSessionFactory().getCurrentSession().createQuery(hqlQuery2);
					query2.setParameter("playerId", playerId);
					final Long noOfHundreds = (Long) query2.uniqueResult();
					if (noOfHundreds != null) {
						t20CumulativeStatistics.setNoOfCenturies(String.valueOf(noOfHundreds));
					}
					final String hqlQuery3 = "Select count(runs) from PlayerT20BattingStatistics where id.playerId = :playerId  and"
							+ " runs not in ('DNB','TDNB') and CAST(runs AS int)  between 50 and 99";
					final Query query3 = getSessionFactory().getCurrentSession().createQuery(hqlQuery3);
					query3.setParameter("playerId", playerId);
					final Long noOfFifties = (Long) query3.uniqueResult();
					if (noOfFifties != null) {
						t20CumulativeStatistics.setNoOfFifties(String.valueOf(noOfFifties));
					}
					final String hqlQuery4 = "Select max(CAST(runs AS int)) from PlayerT20BattingStatistics where id.playerId = :playerId";
					final Query query4 = getSessionFactory().getCurrentSession().createQuery(hqlQuery4);
					query4.setParameter("playerId", playerId);
					final Integer maxScore = (Integer) query4.uniqueResult();
					if (maxScore != null) {
						final Criteria criteriaDismissalType = getSessionFactory().getCurrentSession()
								.createCriteria(PlayerT20BattingStatistics.class);
						criteriaDismissalType.add(Restrictions.eq("id.playerId", playerId));
						criteriaDismissalType.add(Restrictions.eq("runs", String.valueOf(maxScore)));
						String dismissalType = null;
						@SuppressWarnings("unchecked")
						final List<PlayerT20BattingStatistics> playerT20BattingStatisticsList = criteriaDismissalType
								.list();
						for (int i = 0; i < playerT20BattingStatisticsList.size(); i++) {
							if (StringUtils.equalsIgnoreCase(playerT20BattingStatisticsList.get(i).getDismissal(),
									PlayerProfileConstants.NOT_OUT)) {
								dismissalType = playerT20BattingStatisticsList.get(i).getDismissal();
							}
						}
						if (dismissalType != null) {
							t20CumulativeStatistics.setHighestScore(String.valueOf(maxScore) + "*");
						} else {
							t20CumulativeStatistics.setHighestScore(String.valueOf(maxScore));
						}
					}
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}

		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return t20CumulativeStatistics;
	}

	// Filter by runs start

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByRunsAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(runs AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_RUNS));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByRunsAgainstAllTeamsForEntireCareer(final int playerId)
			throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(runs AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_RUNS));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByRunsAgainstAllTeamsForEntireCareer(final int playerId)
			throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(runs AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_RUNS));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByRunsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(runs AS int)), opposition from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_RUNS));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByRunsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(runs AS int)), opposition from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_RUNS));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByRunsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(runs AS int)), opposition from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_RUNS));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByRunsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(runs AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_RUNS));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByRunsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(runs AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_RUNS));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByRunsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(runs AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_RUNS));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	// Filter by runs end

	// Filter by balls faced end

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByBallsFacedAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(ballsFaced AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_BALLS_FACED));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByBallsFacedAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(ballsFaced AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_BALLS_FACED));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByBallsFacedAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(ballsFaced AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_BALLS_FACED));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByBallsFacedAgainstAllTeamsForAnYear(
			final int playerId, final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(ballsFaced AS int)), opposition from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_BALLS_FACED));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByBallsFacedAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(ballsFaced AS int)), opposition from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_BALLS_FACED));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByBallsFacedAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(ballsFaced AS int)), opposition from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_BALLS_FACED));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByBallsFacedAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(ballsFaced AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_BALLS_FACED));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByBallsFacedAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(ballsFaced AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_BALLS_FACED));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByBallsFacedAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(ballsFaced AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_BALLS_FACED));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	// Filter by balls faced end

	// Filter by minutes start

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByMinutesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(minutes AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MINUTES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByMinutesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(minutes AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MINUTES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByMinutesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(minutes AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MINUTES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByMinutesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(minutes AS int)), opposition from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MINUTES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByMinutesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(minutes AS int)), opposition from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MINUTES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByMinutesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(minutes AS int)), opposition from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MINUTES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByMinutesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(minutes AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MINUTES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByMinutesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(minutes AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MINUTES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByMinutesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(minutes AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MINUTES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}
	// Filter by minutes end

	// Filter by number of fours data start
	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfFoursAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfFours AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_FOURS));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_04);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfFoursAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfFours AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_FOURS));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_04);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfFoursAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfFours AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_FOURS));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_04);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfFoursAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfFours AS int)), opposition from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_FOURS));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_05);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfFoursAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfFours AS int)), opposition from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_FOURS));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_05);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfFoursAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfFours AS int)), opposition from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_FOURS));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_05);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfFoursAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfFours AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_FOURS));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_06);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfFoursAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfFours AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_FOURS));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_06);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfFoursAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfFours AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_FOURS));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_06);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	// Filter by number of sixes data start
	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfSixesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfSixes AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_SIXES));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_07);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfSixesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfSixes AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_SIXES));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_07);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfSixesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfSixes AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_SIXES));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_07);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfSixesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfSixes AS int)), opposition from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_SIXES));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_08);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfSixesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfSixes AS int)), opposition from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_SIXES));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_08);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfSixesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfSixes AS int)), opposition from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_SIXES));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_08);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByNoOfSixesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfSixes AS int)), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_SIXES));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_09);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByNoOfSixesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfSixes AS int)), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_SIXES));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_09);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByNoOfSixesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select sum(CAST(noOfSixes AS int)), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and runs not in ('DNB', 'TDNB') and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_NO_OF_SIXES));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_09);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}
	// Filter by number of sixes data end

	// Filter by number of matches start
	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByMatchesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(distinct match_id), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MATCHES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByMatchesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(distinct match_id), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MATCHES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByMatchesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(distinct match_id), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId group by (EXTRACT(YEAR FROM date)) order by "
						+ "(EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MATCHES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByMatchesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(distinct match_id), opposition from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MATCHES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByMatchesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(distinct match_id), opposition from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MATCHES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	public Map<String, List<String>> fetchT20BattingDataForGraphByMatchesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(distinct match_id), opposition from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and (EXTRACT(YEAR FROM date)) = :year group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MATCHES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByMatchesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(distinct match_id), (EXTRACT(YEAR FROM date)) from PlayerTestBattingStatistics "
						+ "where id.playerId = :playerId and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MATCHES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByMatchesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(distinct match_id), (EXTRACT(YEAR FROM date)) from PlayerOdiBattingStatistics "
						+ "where id.playerId = :playerId and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MATCHES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByMatchesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(distinct match_id), (EXTRACT(YEAR FROM date)) from PlayerT20BattingStatistics "
						+ "where id.playerId = :playerId and opposition = :opposition group by (EXTRACT(YEAR FROM date))"
						+ " order by (EXTRACT(YEAR FROM date)) ASC";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.FILTER_BY_MATCHES));
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}
	// Filter by number of matches end

	// Filter by runs greater than 30 in T20 start

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByThirtiesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerT20BattingStatistics where id.playerId = :playerId  and"
						+ " runs not in ('DNB','TDNB') and CAST(runs AS int) >=30 group by EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_30));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_10);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByThirtiesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), opposition from PlayerT20BattingStatistics where id.playerId = :playerId and"
						+ " runs not in ('DNB','TDNB') and (EXTRACT(YEAR FROM date)) = :year and CAST(runs AS int) >=30 group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_30));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_11);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchT20BattingDataForGraphByThirtiesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerT20BattingStatistics where id.playerId = :playerId "
						+ "and opposition = :opposition and runs not in ('DNB','TDNB') and CAST(runs AS int) >=30"
						+ " group by (EXTRACT(YEAR FROM date)) order by (EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_30));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_12);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	// Filter by runs greater than 50 in Test and ODI start
	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByFiftiesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerTestBattingStatistics where id.playerId = :playerId  and"
						+ " runs not in ('DNB','TDNB') and CAST(runs AS int) between 50 and 99 group by EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_50));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_13);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByFiftiesAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerOdiBattingStatistics where id.playerId = :playerId  and"
						+ " runs not in ('DNB','TDNB') and CAST(runs AS int)  between 50 and 99 group by EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_50));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_13);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByFiftiesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), opposition from PlayerTestBattingStatistics where id.playerId = :playerId and"
						+ " runs not in ('DNB','TDNB') and (EXTRACT(YEAR FROM date)) = :year and CAST(runs AS int)  between 50 and 99 group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_50));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_14);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByFiftiesAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), opposition from PlayerOdiBattingStatistics where id.playerId = :playerId and"
						+ " runs not in ('DNB','TDNB') and (EXTRACT(YEAR FROM date)) = :year and CAST(runs AS int)  between 50 and 99 group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_50));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_14);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByFiftiesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerTestBattingStatistics where id.playerId = :playerId "
						+ "and opposition = :opposition and runs not in ('DNB','TDNB') and CAST(runs AS int)  between 50 and 99 group by "
						+ "(EXTRACT(YEAR FROM date)) order by (EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_50));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_15);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByFiftiesAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerOdiBattingStatistics where id.playerId = :playerId "
						+ "and opposition = :opposition and runs not in ('DNB','TDNB') and CAST(runs AS int)  between 50 and 99 group by "
						+ "(EXTRACT(YEAR FROM date)) order by (EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_50));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_15);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}
	// Filter by runs greater than 50 in Test and ODI end

	// Filter by centuries start
	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByHundredsAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerTestBattingStatistics where id.playerId = :playerId  and"
						+ " runs not in ('DNB','TDNB') and CAST(runs AS int) >=100 group by EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_100));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_16);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByHundredsAgainstAllTeamsForEntireCareer(
			final int playerId) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerOdiBattingStatistics where id.playerId = :playerId  and"
						+ " runs not in ('DNB','TDNB') and CAST(runs AS int) >=100 group by EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_100));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_16);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByHundredsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), opposition from PlayerTestBattingStatistics where id.playerId = :playerId and"
						+ " runs not in ('DNB','TDNB') and (EXTRACT(YEAR FROM date)) = :year and CAST(runs AS int)  >=100 group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_100));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_17);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByHundredsAgainstAllTeamsForAnYear(final int playerId,
			final int year) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), opposition from PlayerOdiBattingStatistics where id.playerId = :playerId and"
						+ " runs not in ('DNB','TDNB') and (EXTRACT(YEAR FROM date)) = :year and CAST(runs AS int)  >=100 group by opposition";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("year", year);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				final List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final String header = (String) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(header);
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.OPPOSITION));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_100));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_17);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchTestBattingDataForGraphByHundredsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerTestBattingStatistics where id.playerId = :playerId "
						+ "and opposition = :opposition and runs not in ('DNB','TDNB') and CAST(runs AS int) >=100 group by "
						+ "(EXTRACT(YEAR FROM date)) order by (EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				final List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_100));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_18);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}

	@Override
	@Transactional
	public Map<String, List<String>> fetchOdiBattingDataForGraphByHundredsAgainstAnOpposition(final int playerId,
			final String opposition) throws PlayerProfileException {
		Map<String, List<String>> dataForGraphicalRepresentation = null;
		try {
			if (getSessionFactory() != null && getSessionFactory().getCurrentSession() != null) {
				final String hqlQuery = "Select count(runs), EXTRACT(YEAR FROM date) from PlayerOdiBattingStatistics where id.playerId = :playerId "
						+ "and opposition = :opposition and runs not in ('DNB','TDNB') and CAST(runs AS int) >=100 group by "
						+ "(EXTRACT(YEAR FROM date)) order by (EXTRACT(YEAR FROM date))";
				final Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
				query.setParameter("playerId", playerId);
				query.setParameter("opposition", opposition);
				final List<String> headersList = new ArrayList<String>();
				final List<String> dataList = new ArrayList<String>();
				@SuppressWarnings("unchecked")
				final List<Object[]> filteredDataList = query.list();
				if (CollectionUtils.isNotEmpty(filteredDataList)) {
					for (Object[] arr : filteredDataList) {
						if (arr[0] != null && arr[1] != null) {
							final Long data = (Long) arr[0];
							final Integer header = (Integer) arr[1];
							if (data != null && header != null && data != 0) {
								headersList.add(String.valueOf(header));
								dataList.add(String.valueOf(data));
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(headersList) && CollectionUtils.isNotEmpty(dataList)
						&& dataList.size() == headersList.size()) {
					dataForGraphicalRepresentation = new LinkedHashMap<String, List<String>>();
					dataForGraphicalRepresentation.put("header", headersList);
					dataForGraphicalRepresentation.put("Data", dataList);
					dataForGraphicalRepresentation.put(PlayerProfileConstants.X_AXIS,
							Arrays.asList(PlayerProfileConstants.YEAR));
					dataForGraphicalRepresentation.put(PlayerProfileConstants.Y_AXIS,
							Arrays.asList(PlayerProfileConstants.COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_100));
				} else {
					throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_18);
				}
			} else {
				throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_01);
			}
		} catch (PlayerProfileException e) {
			throw e;
		} catch (Exception e) {
			throw new PlayerProfileException(InfoMessageConstants.MESSAGE_ID_03);
		}
		return dataForGraphicalRepresentation;
	}
}
