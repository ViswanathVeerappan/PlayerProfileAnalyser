<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.js"></script>
<spring:url value="/resources/js/PlayerProfileAnalyser.js" var="mainJs" />
<spring:url value="/resources/css/PlayerProfileAnalyser.css"
	var="mainCss"></spring:url>
<script src="${mainJs}"></script>
<link rel="stylesheet" type="text/css" href="${mainCss}">
<script type="text/javascript">
	function addCountriesOption() {
		var countryOption = document.getElementById("countrySelect");
		var optionDefault = document.createElement("option");
		optionDefault.text = 'Select a Country';
		optionDefault.value = '';
		countryOption.appendChild(optionDefault);
		<c:forEach var="country" items="${CountriesAndPlayersMap}">
		var countryName = "${country.key}";
		var option = document.createElement("option");
		option.text = countryName;
		option.value = countryName;
		countryOption.appendChild(option);
		</c:forEach>
	}

	function addPlayersOption() {
		var countrySelected = document.getElementById("countrySelect").value;
		<c:forEach var="country" items="${CountriesAndPlayersMap}">
		if ("${country.key}" == countrySelected) {
			var playersString = '<c:out value = "${country.value}"/>';
			var playersList = playersString.split(",");
			var playerOption = document.getElementById("playerSelect");
			playerOption.options.length = 0;
			var optionDefault = document.createElement("option");
			optionDefault.text = 'Select a Player';
			optionDefault.value = '';
			playerOption.appendChild(optionDefault);
			if (playersList.length == 1) {
				var player = playersList[0].substring(1);
				player = player.substring(0, player.length - 1);
				var playerDetails = player.split(":");
				var option = document.createElement("option");
				option.value = playerDetails[0];
				option.text = playerDetails[1];
				playerOption.appendChild(option);
			} else {
				for (i = 0; i < playersList.length; i++) {
					var option = document.createElement("option");
					if (i == 0) {
						var player = playersList[i].substring(1);
						var playerDetails = player.split(":");
						var option = document.createElement("option");
						option.value = playerDetails[0];
						option.text = playerDetails[1];
						playerOption.appendChild(option);
					} else if (i == playersList.length - 1) {
						var player = playersList[i].substring(0,
								playersList[i].length - 1);
						var playerDetails = player.split(":");
						var option = document.createElement("option");
						option.value = playerDetails[0];
						option.text = playerDetails[1];
						playerOption.appendChild(option);
					} else {
						var player = playersList[i];
						var playerDetails = player.split(":");
						var option = document.createElement("option");
						option.value = playerDetails[0];
						option.text = playerDetails[1];
						playerOption.appendChild(option);
					}
				}
			}
		}
		</c:forEach>
	}

	function changeView() {
		var typeOfView = '<c:out value = "${typeOfView}"/>';
		if (typeOfView == "profileView") {
			$("#analayzerView").hide();
			$("#exceptionView").hide();
			$("#logoView").hide();
		} else if (typeOfView == "analysisView") {
			$("#profileView").hide();
			$("#logoView").hide();
			$("#exceptionView").hide();
			var categoryUsed = '<c:out value = "${category}"/>';
			var playTypeUsed = '<c:out value = "${playType}"/>';
			var opponentUsed = '<c:out value = "${opponent}"/>';
			var timelineUsed = '<c:out value = "${timeline}"/>';
			var filterByUsed = '<c:out value = "${filterBy}"/>';
			var hasPlayedTest = '<c:out value = "${personalDetails.testOpponentsList}"/>';
			if (playTypeUsed == "Test") {
				var testOpponentsList = '<c:out value = "${personalDetails.testOpponentsList}"/>';
				var timeline = '<c:out value = "${personalDetails.testCareerTimeline}"/>';
				loadFilterValues(testOpponentsList, timeline, categoryUsed,
						playTypeUsed, opponentUsed, timelineUsed, filterByUsed,
						hasPlayedTest);
			} else if (playTypeUsed == "ODI") {
				var odiOpponentList = '<c:out value = "${personalDetails.odiOpponentsList}"/>';
				var timeline = '<c:out value = "${personalDetails.odiCareerTimeline}"/>';
				loadFilterValues(odiOpponentList, timeline, categoryUsed,
						playTypeUsed, opponentUsed, timelineUsed, filterByUsed,
						hasPlayedTest);
			} else {
				var t20OpponentList = '<c:out value = "${personalDetails.t20OpponentsList}"/>';
				var timeline = '<c:out value = "${personalDetails.t20CareerTimeline}"/>';
				loadFilterValues(t20OpponentList, timeline, categoryUsed,
						playTypeUsed, opponentUsed, timelineUsed, filterByUsed,
						hasPlayedTest);
			}
			var message = '<c:out value = "${message}"/>';
			if (message == "") {
				var dataString = "";
				var headerString = "";
				var xAxis = "";
				var yAxis = "";
				<c:forEach var="dataForGraph" items="${dataForGraphicalRepresentation}">
				if ("${dataForGraph.key}" == "header") {
					headerString = '<c:out value = "${dataForGraph.value}"/>';
				} else if ("${dataForGraph.key}" == "Data") {
					dataString = '<c:out value = "${dataForGraph.value}"/>';
				} else if ("${dataForGraph.key}" == "x-axis") {
					xAxis = '<c:out value = "${dataForGraph.value}"/>';
				} else {
					yAxis = '<c:out value = "${dataForGraph.value}"/>';
				}
				</c:forEach>
				if (headerString != "" && dataString != "" && xAxis != ""
						&& yAxis != "") {
					analyseUserData(headerString, dataString);
					displayXandYAxes(xAxis, yAxis);
				}
			} else {
				document.getElementById("messageDiv").className = "";
				document.getElementById("messageSpan").innerHTML = message;
			}

		} else if (typeOfView == "exceptionView") {
			$("#analayzerView").hide();
			$("#profileView").hide();
			$("#logoView").hide();
			var message = '<c:out value = "${errorMessage}"/>';
			document.getElementById("exceptionMessage").innerHTML = message;
		} else {
			$("#analayzerView").hide();
			$("#profileView").hide();
			$("#exceptionView").hide();
		}
	}

	function addOpponentsOption() {
		var playType = document.getElementById("playTypeSelect").value;
		if (playType == "Test") {
			var testOpponentsList = '<c:out value = "${personalDetails.testOpponentsList}"/>';
			addOpponentsOptionFunction(testOpponentsList);
		} else if (playType == "ODI") {
			var odiOpponentList = '<c:out value = "${personalDetails.odiOpponentsList}"/>';
			addOpponentsOptionFunction(odiOpponentList);
		} else {
			var t20OpponentList = '<c:out value = "${personalDetails.t20OpponentsList}"/>';
			addOpponentsOptionFunction(t20OpponentList);
		}
	}

	function addTimelineOption() {
		var opponent = document.getElementById("opponentSelect").value;
		if (opponent == "All") {
			var playType = document.getElementById("playTypeSelect").value;
			if (playType == "Test") {
				var testCareerTimelineList = '<c:out value = "${personalDetails.testCareerTimeline}"/>';
				addTimelineOptionFunction(testCareerTimelineList);
			} else if (playType == "ODI") {
				var odiCareerTimelineList = '<c:out value = "${personalDetails.odiCareerTimeline}"/>';
				addTimelineOptionFunction(odiCareerTimelineList);
			} else {
				var t20CareerTimelineList = '<c:out value = "${personalDetails.t20CareerTimeline}"/>';
				addTimelineOptionFunction(t20CareerTimelineList);
			}
		} else {
			var timlineOption = document.getElementById("timelineSelect");
			timlineOption.options.length = 0;
			var optionDefault = document.createElement("option");
			optionDefault.text = 'Timeline';
			optionDefault.value = '';
			timlineOption.appendChild(optionDefault);
			var optionDefault1 = document.createElement("option");
			optionDefault1.text = 'Career';
			optionDefault1.value = 'Career';
			timlineOption.appendChild(optionDefault1);
		}
	}
</script>
<title>Player Profile Analyzer</title>
</head>
<body onload="addCountriesOption();changeView()">
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Player Profile Analyzer</a>
			</div>
			<div class="navbar-collapse collapse">
				<form class="navbar-form navbar-right" method="post"
					action="personalProfile.do">
					<select class="form-control" id="countrySelect"
						name="countrySelect" onchange="addPlayersOption()" required>
					</select> <select class="form-control" id="playerSelect" name="playerSelect"
						required>
						<option value="">Select a Player</option>
					</select>
					<button type="submit" class="btn btn-primary">Submit</button>
				</form>
			</div>
		</div>
	</nav>
	<!--Section to display the analysis on player's profile starts-->
	<div id="analayzerView" class="container">
		<div class="row">
			<div class="col-lg-2"></div>
			<div class="col-lg-8">
				<div class="panel panel-primary">
					<div class="panel-heading" id="analyzerPanelHeading">
						<div align="left" class="navbar-brand">${personalDetails.name}</div>
						<div align="right">
							<button type="submit" class="btn btn-primary"
								onclick="goToProfileView()">View Profile</button>
						</div>
					</div>
					<div class="panel-body" id="analyzerPanelBody">
						<div align="center">
							<form class="form-inline" method="post" role="form"
								action="analyzeProfile.do">
								<div class="form-group">
									<select class="form-control input-large" id="categorySelect"
										name="categorySelect" required>
										<option value="">Category</option>
										<option value="Batting">Batting</option>
									</select>
								</div>
								<div class="form-group">
									<select class="form-control input-large" id="playTypeSelect"
										name="playTypeSelect"
										onChange="addOpponentsOption(); addFilterByOption()" required>
										<option value="">Play Type</option>
										<c:if test="${personalDetails.testOpponentsList!=null}">
											<option value="Test">Test</option>
										</c:if>
										<c:if test="${personalDetails.odiOpponentsList!=null}">
											<option value="ODI">ODI</option>
										</c:if>
										<c:if test="${personalDetails.t20OpponentsList!=null}">
											<option value="T20">T20</option>
										</c:if>
									</select>
								</div>
								<div class="form-group">
									<select class="form-control input-large" id="opponentSelect"
										name="opponentSelect" onChange="addTimelineOption()" required>
										<option value="">Opponent</option>
									</select>
								</div>
								<div class="form-group">
									<select class="form-control input-large" id="timelineSelect"
										name="timelineSelect" required>
										<option value="">Timeline</option>
									</select>
								</div>
								<div class="form-group">
									<select class="form-control input-large" id="filterBySelect"
										name="filterBySelect" required>
										<option value="">Filter By</option>
									</select>
								</div>
								<div class="form-group">
									<button type="submit" class="btn btn-primary">Submit</button>
								</div>
							</form>
						</div>
						<div>
							<div id="canvasDiv" align="center" class="hidden">
								<canvas id="canvas" width="650" height="430"></canvas>
							</div>
							<div id="axesDiv" class="hidden" align="center">
								<label>x-axis: </label><span id="xaxis"></span> <label>y-axis:
								</label><span id="yaxis"></span>
							</div>
						</div>
						<div id="messageDiv" class="hidden" align="center">
							<span id="messageSpan"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--Section to display the analysis on player's profile ends-->

	<!--Section to display the personal profile of the player starts-->
	<div id="profileView" class="container">
		<div class="row">
			<div class="col-lg-2"></div>
			<div class="col-lg-8">
				<div class="panel panel-primary">
					<div class="panel-heading" id="profilePanelHeading">
						<div align="left" class="navbar-brand">${personalDetails.name}</div>
						<div align="right">
							<button type="submit" class="btn btn-primary"
								onclick="goToProfileAnalyzerView()">Analyze Profile</button>
						</div>
					</div>
					<div class="panel-body" id="profilePanelBody">
						<div id="data-div" class="col-sm-9">
							<dl class="dl-horizontal">
								<dt>Full Name</dt>
								<dd>${personalDetails.fullName}</dd>
								<dt>Birth Date</dt>
								<dd>${personalDetails.birthDate}</dd>
								<dt>Birth Place</dt>
								<dd>${personalDetails.birthPlace}</dd>
								<dt>Team[s]</dt>
								<dd>${personalDetails.teamsPlayedFor}</dd>
								<c:if test="${personalDetails.role!= null}">
									<dt>Role</dt>
									<dd>${personalDetails.role}</dd>
								</c:if>
								<c:if test="${personalDetails.battingStyle!= null}">
									<dt>Batting Style</dt>
									<dd>${personalDetails.battingStyle}</dd>
								</c:if>
								<c:if test="${personalDetails.bowlingStyle != null}">
									<dt>Bowling Style</dt>
									<dd>${personalDetails.bowlingStyle}</dd>
								</c:if>
								<c:if test="${personalDetails.testOpponentsList != null}">
									<dt>Test Debut Against</dt>
									<dd>${personalDetails.testDebutDetails.debutAgainst}</dd>
									<dt>Test Debut Date</dt>
									<dd>${personalDetails.testDebutDetails.debutDate}</dd>
								</c:if>
								<c:if test="${personalDetails.odiOpponentsList!= null}">
									<dt>ODI Debut Against</dt>
									<dd>${personalDetails.odiDebutDetails.debutAgainst}</dd>
									<dt>ODI Debut Date</dt>
									<dd>${personalDetails.odiDebutDetails.debutDate}</dd>
								</c:if>
								<c:if test="${personalDetails.t20OpponentsList!= null}">
									<dt>T20 Debut Against</dt>
									<dd>${personalDetails.t20DebutDetails.debutAgainst}</dd>
									<dt>ODI Debut Date</dt>
									<dd>${personalDetails.t20DebutDetails.debutDate}</dd>
								</c:if>
							</dl>
						</div>
						<div id="imageDiv" class="col-sm-3">
							<div>
								<img alt="${personalDetails.name}" class="img-rounded"
									width="150" height="200"
									src="<c:url value="${personalDetails.imageLocation}"/>">
							</div>
						</div>

						<div class="col-sm-12">
							<table class="table table-condensed">
								<thead>
									<tr>
										<th></th>
										<th>Mat</th>
										<th>Inns</th>
										<th>NO</th>
										<th>Runs</th>
										<th>HS</th>
										<th>Ave</th>
										<th>BF</th>
										<th>SR</th>
										<th>100s</th>
										<th>50s</th>
										<th>4s</th>
										<th>6s</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${personalDetails.testCumulativeStatistics!= null}">
										<tr>
											<th>Tests</th>
											<td>${personalDetails.testCumulativeStatistics.noOfMatches}</td>
											<td>${personalDetails.testCumulativeStatistics.noOfInnings}</td>
											<td>${personalDetails.testCumulativeStatistics.noOfNotOuts}</td>
											<td>${personalDetails.testCumulativeStatistics.totalRuns}</td>
											<td>${personalDetails.testCumulativeStatistics.highestScore}</td>
											<td>${personalDetails.testCumulativeStatistics.battingAverage}</td>
											<td>${personalDetails.testCumulativeStatistics.totalBallsFaced}</td>
											<td>${personalDetails.testCumulativeStatistics.strikeRate}</td>
											<td>${personalDetails.testCumulativeStatistics.noOfCenturies}</td>
											<td>${personalDetails.testCumulativeStatistics.noOfFifties}</td>
											<td>${personalDetails.testCumulativeStatistics.noOfFours}</td>
											<td>${personalDetails.testCumulativeStatistics.noOfSixes}</td>
										</tr>
									</c:if>
									<c:if test="${personalDetails.odiCumulativeStatistics!= null}">
										<tr>
											<th>ODIs</th>
											<td>${personalDetails.odiCumulativeStatistics.noOfMatches}</td>
											<td>${personalDetails.odiCumulativeStatistics.noOfInnings}</td>
											<td>${personalDetails.odiCumulativeStatistics.noOfNotOuts}</td>
											<td>${personalDetails.odiCumulativeStatistics.totalRuns}</td>
											<td>${personalDetails.odiCumulativeStatistics.highestScore}</td>
											<td>${personalDetails.odiCumulativeStatistics.battingAverage}</td>
											<td>${personalDetails.odiCumulativeStatistics.totalBallsFaced}</td>
											<td>${personalDetails.odiCumulativeStatistics.strikeRate}</td>
											<td>${personalDetails.odiCumulativeStatistics.noOfCenturies}</td>
											<td>${personalDetails.odiCumulativeStatistics.noOfFifties}</td>
											<td>${personalDetails.odiCumulativeStatistics.noOfFours}</td>
											<td>${personalDetails.odiCumulativeStatistics.noOfSixes}</td>
										</tr>
									</c:if>
									<c:if test="${personalDetails.t20CumulativeStatistics!= null}">
										<tr>
											<th>T20s</th>
											<td>${personalDetails.t20CumulativeStatistics.noOfMatches}</td>
											<td>${personalDetails.t20CumulativeStatistics.noOfInnings}</td>
											<td>${personalDetails.t20CumulativeStatistics.noOfNotOuts}</td>
											<td>${personalDetails.t20CumulativeStatistics.totalRuns}</td>
											<td>${personalDetails.t20CumulativeStatistics.highestScore}</td>
											<td>${personalDetails.t20CumulativeStatistics.battingAverage}</td>
											<td>${personalDetails.t20CumulativeStatistics.totalBallsFaced}</td>
											<td>${personalDetails.t20CumulativeStatistics.strikeRate}</td>
											<td>${personalDetails.t20CumulativeStatistics.noOfCenturies}</td>
											<td>${personalDetails.t20CumulativeStatistics.noOfFifties}</td>
											<td>${personalDetails.t20CumulativeStatistics.noOfFours}</td>
											<td>${personalDetails.t20CumulativeStatistics.noOfSixes}</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
						<div class="col-sm-12">
							<p class="text-right">-Stats of each player shown will be
								updated at the end of day's play</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--Section to display the personal profile of the player ends-->
	<div id="exceptionView" class="container">
		<div class="row">
			<div class="col-lg-2"></div>
			<div class="col-lg-8">
				<div class="alert alert-danger" align="center">
					<span id="exceptionMessage"></span>
				</div>
			</div>
		</div>
	</div>

	<div id="logoView" class="container">
		<div class="row">
			<div class="col-lg-2"></div>
			<div class="col-lg-8" align="center">
				<img alt="logo" class="img-rounded" width="420" height="560"
					src="<c:url value="/resources/images/logo.jpg"/>">
			</div>
		</div>
	</div>
</body>
</html>