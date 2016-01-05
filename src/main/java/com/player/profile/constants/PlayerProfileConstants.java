package com.player.profile.constants;

import java.util.Arrays;
import java.util.List;

public class PlayerProfileConstants {

	public static final List<String> COUNTRIES_PLAYING_CRICKET = Arrays.asList("Afghanistan", "Australia", "Bangladesh",
			"Bermuda", "Canada", "England", "Hong Kong", "India", "Ireland", "Kenya", "Namibia", "Nepal", "Netherlands",
			"New Zealand", "Oman", "Pakistan", "Papua New Guinea", "Scotland", "South Africa", "Sri Lanka", "U.A.E.",
			"United Arab Emirates", "U.S.A", "West Indies", "Zimbabwe");

	public static final List<String> OTHER_INTERNATIONAL_TEAMS = Arrays.asList("ICC WORLD XI", "ASIA XI", "Africa XI");

	public static final String WILDCARD = "%";

	public static final String NOT_OUT = "not out";

	public static final String IMAGE_LOCATION = "/resources/images/";

	public static final String PROFILE_VIEW = "profileView";

	public static final String ANALYSIS_VIEW = "analysisView";

	public static final String VIDEO_VIEW = "videoView";

	public static final String EXCEPTION_VIEW = "exceptionView";

	public static final String CATEGORY_BATTING = "Batting";

	public static final String CATEGORY_BOWLING = "Bowling";

	public static final String CATEGORY_FIELDING = "Fielding";

	public static final List<String> LIST_OF_VALID_CATEGORIES = Arrays.asList(CATEGORY_BATTING, CATEGORY_BOWLING,
			CATEGORY_FIELDING);

	public static final String PLAY_TYPE_TEST = "Test";

	public static final String PLAY_TYPE_ODI = "ODI";

	public static final String PLAY_TYPE_T20 = "T20";

	public static final List<String> LIST_OF_VALID_PLAY_TYPES = Arrays.asList(PLAY_TYPE_TEST, PLAY_TYPE_ODI,
			PLAY_TYPE_T20);

	public static final String OPPONENT_ALL = "All";

	public static final String OPPONENT_OTHERS = "Others";

	public static final String TIMELINE_CAREER = "Career";

	public static final String TIMELINE_OTHERS = "Others";

	public static final String FILTER_BY_RUNS = "Runs";

	public static final String FILTER_BY_MINUTES = "Minutes";

	public static final String FILTER_BY_BALLS_FACED = "Balls Faced";

	public static final String FILTER_BY_NO_OF_FOURS = "No of Fours";

	public static final String FILTER_BY_NO_OF_SIXES = "No of Sixes";

	public static final String FILTER_BY_MATCHES = "Matches";

	public static final String FILTER_BY_THIRTIES = "30s";

	public static final String FILTER_BY_FIFTIES = "50s";

	public static final String FILTER_BY_HUNDREDS = "100s";

	public static final String FILTER_BY_AVERAGE = "Average";

	public static final String FILTER_BY_STRIKE_RATE = "Strike Rate";

	// Constants for graph data

	public static final String X_AXIS = "x-axis";

	public static final String Y_AXIS = "y-axis";

	public static final String YEAR = "Year[s]";

	public static final String OPPOSITION = "Opposition";

	public static final String COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_30 = "Count of runs >=30";

	public static final String COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_50 = "Count of runs >=50 and < 100";

	public static final String COUNT_OF_RUNS_GREATER_THAN_OR_EQUAL_TO_100 = "Count of runs >=100";

}
