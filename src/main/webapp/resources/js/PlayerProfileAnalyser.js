/**
 * 
 */

function goToProfileAnalyzerView() {
	$("#analayzerView").show();
	$("#profileView").hide();
}

function goToProfileView() {
	$("#analayzerView").hide();
	$("#profileView").show();
}

function addOpponentsOptionFunction(list) {
	var listOfOpponents = list.split(",");
	var opponentsOption = document.getElementById("opponentSelect");
	opponentsOption.options.length = 0;
	var optionDefault = document.createElement("option");
	optionDefault.text = 'Opponent';
	optionDefault.value = '';
	opponentsOption.appendChild(optionDefault);
	var optionDefault1 = document.createElement("option");
	optionDefault1.text = 'All';
	optionDefault1.value = 'All';
	opponentsOption.appendChild(optionDefault1);
	if (listOfOpponents.length == 1) {
		var optionValue = listOfOpponents[0].substring(1);
		optionValue = optionValue.substring(0, optionValue.length - 1);
		var option = document.createElement("option");
		option.value = optionValue;
		option.text = optionValue;
		opponentsOption.appendChild(option);
	} else {
		for (i = 0; i < listOfOpponents.length; i++) {
			if (i == 0) {
				var optionValue = listOfOpponents[i].substring(1);
				var option = document.createElement("option");
				option.value = optionValue;
				option.text = optionValue;
				opponentsOption.appendChild(option);
			} else if (i == listOfOpponents.length - 1) {
				var optionValue = listOfOpponents[i];
				optionValue = optionValue.substring(0, optionValue.length - 1);
				var option = document.createElement("option");
				option.value = optionValue;
				option.text = optionValue;
				opponentsOption.appendChild(option);
			} else {
				var optionValue = listOfOpponents[i];
				var option = document.createElement("option");
				option.value = optionValue;
				option.text = optionValue;
				opponentsOption.appendChild(option);
			}
		}

	}
}

function addTimelineOptionFunction(list) {
	var listOfTimeline = list.split(",");
	var timelineOption = document.getElementById("timelineSelect");
	timelineOption.options.length = 0;
	var optionDefault = document.createElement("option");
	optionDefault.text = 'Timeline';
	optionDefault.value = '';
	timelineOption.appendChild(optionDefault);
	var optionDefault1 = document.createElement("option");
	optionDefault1.text = 'Career';
	optionDefault1.value = 'Career';
	timelineOption.appendChild(optionDefault1);
	if (listOfTimeline.length == 1) {
		var optionValue = stOfTimeline[0].substring(1);
		optionValue = optionValue.substring(0, optionValue.length - 1);
		var option = document.createElement("option");
		option.value = optionValue;
		option.text = optionValue;
		timelineOption.appendChild(option);
	} else {
		for (i = 0; i < listOfTimeline.length; i++) {
			if (i == 0) {
				var optionValue = listOfTimeline[i].substring(1);
				var option = document.createElement("option");
				option.value = optionValue;
				option.text = optionValue;
				timelineOption.appendChild(option);
			} else if (i == listOfTimeline.length - 1) {
				var optionValue = listOfTimeline[i];
				optionValue = optionValue.substring(0, optionValue.length - 1);
				var option = document.createElement("option");
				option.value = optionValue;
				option.text = optionValue;
				timelineOption.appendChild(option);
			} else {
				var optionValue = listOfTimeline[i];
				var option = document.createElement("option");
				option.value = optionValue;
				option.text = optionValue;
				timelineOption.appendChild(option);
			}
		}

	}
}

function addFilterByOption() {
	var filterByOption = document.getElementById("filterBySelect");
	var playType = document.getElementById("playTypeSelect").value;
	filterByOption.options.length = 0;
	var optionDefault = document.createElement("option");
	optionDefault.text = 'FilterBy';
	optionDefault.value = '';
	filterByOption.appendChild(optionDefault);
	var option1 = document.createElement("option");
	option1.text = 'Runs';
	option1.value = 'Runs';
	filterByOption.appendChild(option1);
	var option2 = document.createElement("option");
	option2.text = 'Balls Faced';
	option2.value = 'Balls Faced';
	filterByOption.appendChild(option2);
	var option3 = document.createElement("option");
	option3.text = 'Minutes';
	option3.value = 'Minutes';
	filterByOption.appendChild(option3);
	var option4 = document.createElement("option");
	option4.text = 'No of Fours';
	option4.value = 'No of Fours';
	filterByOption.appendChild(option4);
	var option5 = document.createElement("option");
	option5.text = 'No of Sixes';
	option5.value = 'No of Sixes';
	filterByOption.appendChild(option5);
	var option6 = document.createElement("option");
	option6.text = 'Matches';
	option6.value = 'Matches';
	filterByOption.appendChild(option6);
	if (playType == "Test" || playType == "ODI") {
		var option7 = document.createElement("option");
		option7.text = '50s';
		option7.value = '50s';
		filterByOption.appendChild(option7);
		var option8 = document.createElement("option");
		option8.text = '100s';
		option8.value = '100s';
		filterByOption.appendChild(option8);
	} else {
		var option7 = document.createElement("option");
		option7.text = '30s';
		option7.value = '30s';
		filterByOption.appendChild(option7);
	}
}

function analyseUserData(headerString, dataString) {
	var headerStringList = headerString.split(",");
	var dataStringList = dataString.split(",");
	var labelData = loadDataForGraph(headerStringList);
	var dataValue = loadDataForGraph(dataStringList);
	if (labelData.length != 0 && dataValue.length != 0) {
		document.getElementById("canvasDiv").className = "";
		var ctx = $("#canvas").get(0).getContext("2d");
		var data = {
			labels : labelData,
			datasets : [ {
				fillColor : "grey",
				strokeColor : "grey",
				highlightFill : "yellow",
				highlightStroke : "brown",
				scaleGridLineColor : "black",
				data : dataValue
			} ]
		};
		var chart = new Chart(ctx).Bar(data);
	}
}

function loadDataForGraph(list) {
	var labelData = [];
	if (list.length == 1) {
		var header = list[0].substring(1);
		header = header.substring(0, header.length - 1);
		labelData.push(header);
	} else {
		for (i = 0; i < list.length; i++) {
			if (i == 0) {
				var header = list[i].substring(1);
				labelData.push(header);
			} else if (i == list.length - 1) {
				var header = list[i];
				header = header.substring(0, header.length - 1);
				labelData.push(header);
			} else {
				labelData.push(list[i]);
			}
		}
	}
	return labelData;
}

function displayXandYAxes(xAxis, yAxis) {
	if (xAxis != "" && yAxis != "" && xAxis.length > 1 && yAxis.length > 1) {
		document.getElementById("axesDiv").className = "";
		var xAxisValue = xAxis.substring(1, xAxis.length - 1);
		var yAxisValue = yAxis.substring(1, yAxis.length - 1);
		document.getElementById("xaxis").innerHTML = xAxisValue;
		document.getElementById("yaxis").innerHTML = yAxisValue;
	}
}

function loadFilterValues(opponentList, timelineList, categoryUsed,
		playTypeUsed, opponentUsed, timelineUsed, filterByUsed, hasPlayedTest) {
	saveCategoryUsed(categoryUsed);
	savePlayTypeUsed(playTypeUsed, hasPlayedTest);
	saveFilterBy(playTypeUsed, filterByUsed);
	saveOpponent(opponentList, opponentUsed);
	saveTimeline(timelineList, timelineUsed, opponentUsed);
}

function saveCategoryUsed(categoryUsed) {
	var category = [ "Batting" ];
	var categorySelect = document.getElementById("categorySelect");
	categorySelect.options.length = 0;
	var optionDefault = document.createElement("option");
	optionDefault.text = 'Category';
	optionDefault.value = '';
	categorySelect.appendChild(optionDefault);
	for (i = 0; i < category.length; i++) {
		categorySelect.appendChild(returnOption(categoryUsed, category[i]));
	}
}

function savePlayTypeUsed(playTypeUsed, hasPlayedTest) {
	var playType = [];
	if (hasPlayedTest != "") {
		playType = [ "Test", "ODI", "T20" ];
	} else {
		playType = [ "ODI", "T20" ];
	}
	var playTypeSelect = document.getElementById("playTypeSelect");
	playTypeSelect.options.length = 0;
	var optionDefault = document.createElement("option");
	optionDefault.text = 'Play Type';
	optionDefault.value = '';
	playTypeSelect.appendChild(optionDefault);
	for (i = 0; i < playType.length; i++) {
		playTypeSelect.appendChild(returnOption(playTypeUsed, playType[i]));
	}
}

function saveOpponent(opponentList, opponentUsed) {
	var opponentSelect = document.getElementById("opponentSelect");
	opponentSelect.options.length = 0;
	var optionDefault = document.createElement("option");
	optionDefault.text = 'Opponent';
	optionDefault.value = '';
	opponentSelect.appendChild(optionDefault);
	opponentSelect.appendChild(returnOption(opponentUsed, 'All'));
	var list = editStringAndCreateList(opponentList);
	for (i = 0; i < list.length; i++) {
		opponentSelect.appendChild(returnOption(opponentUsed, list[i]));
	}
}

function saveTimeline(timelineList, timelineUsed, opponentUsed) {
	var timelineSelect = document.getElementById("timelineSelect");
	timelineSelect.options.length = 0;
	var optionDefault = document.createElement("option");
	optionDefault.text = 'Timeline';
	optionDefault.value = '';
	timelineSelect.appendChild(optionDefault);
	timelineSelect.appendChild(returnOption(timelineUsed, 'Career'));
	if (opponentUsed == 'All') {
		var list = editStringAndCreateList(timelineList);
		for (i = 0; i < list.length; i++) {
			timelineSelect.appendChild(returnOption(timelineUsed, list[i]));
		}
	}
}

function editStringAndCreateList(listString) {
	var list = [];
	var splitList = listString.split(",");
	if (splitList.length == 1) {
		var stringObject = splitList[0].substring(1);
		stringObject = stringObject.substring(0, stringObject.length - 1);
		list.push(stringObject);
	} else {
		for (i = 0; i < splitList.length; i++) {
			if (i == 0) {
				var stringObject = splitList[i].substring(1);
				list.push(stringObject);
			} else if (i == splitList.length - 1) {
				var stringObject = splitList[i];
				stringObject = stringObject.substring(0,
						stringObject.length - 1);
				list.push(stringObject);
			} else {
				var stringObject = splitList[i];
				list.push(stringObject);
			}
		}
	}
	return list;
}

function saveFilterBy(playTypeUsed, filterByUsed) {
	var filterBySelect = document.getElementById("filterBySelect");
	filterBySelect.options.length = 0;
	var optionDefault = document.createElement("option");
	optionDefault.text = 'FilterBy';
	optionDefault.value = '';
	filterBySelect.appendChild(optionDefault);
	if (playTypeUsed == 'T20') {
		var filterBy = [ "Runs", "Balls Faced", "Minutes", "No of Fours",
				"No of Sixes", "Matches", "30s" ];
		for (i = 0; i < filterBy.length; i++) {
			filterBySelect.appendChild(returnOption(filterByUsed, filterBy[i]));
		}
	} else {
		var filterBy = [ "Runs", "Balls Faced", "Minutes", "No of Fours",
				"No of Sixes", "Matches", "50s", "100s" ];
		for (i = 0; i < filterBy.length; i++) {
			filterBySelect.appendChild(returnOption(filterByUsed, filterBy[i]));
		}
	}

}

function returnOption(typeUsed, optionValue) {
	var option = document.createElement("option");
	option.text = optionValue;
	option.value = optionValue;
	if (typeUsed == optionValue) {
		option.selected = true;
	}
	return option;
}
