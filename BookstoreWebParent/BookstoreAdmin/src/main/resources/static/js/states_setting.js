var buttonLoad4States;
var dropDownCountry4States;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function () {
    buttonLoad4States = $("#buttonLoadCountriesForStates");
    dropDownCountry4States = $("#dropDownCountriesForStates");
    dropDownStates = $("#dropDownStates");
    buttonAddState = $("#buttonAddState");
    buttonUpdateState = $("#buttonUpdateState");
    buttonDeleteState = $("#buttonDeleteState");
    labelStateName = $("#labelStateName");
    fieldStateName = $("#fieldStateName");

    buttonLoad4States.click(function () {
        loadCountries4States();
    });

    dropDownCountry4States.on("change", function () {
        loadStates4Country();
    });

    dropDownStates.on("change", function () {
        changeFormStateToSelectedState();
    });

    buttonAddState.click(function () {
        if (buttonAddState.val() == "Add") {
            addState();
        } else {
            changeFormStateToNew();
        }
    });

    buttonUpdateState.click(function () {
        updateState();
    });

    buttonDeleteState.click(function () {
        deleteState();
    });
});

function changeFormStateToNew() {
    buttonAddState.val("Add");
    labelStateName.text("State/Province Name:");

    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    fieldStateName.val("").focus();

}

function changeFormStateToSelectedState() {
    buttonAddState.prop("value", "New");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    labelStateName.text("Selected State/Province:");

    selectedStateName = $("#dropDownStates option:selected").text();
    fieldStateName.val(selectedStateName);
}

function loadStates4Country() {
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "states/list_by_country/" + countryId;

    $.get(url, function (rensponseJSON) {
        dropDownStates.empty();
        $.each(rensponseJSON, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        });
    }).done(function () {
        changeFormStateToNew();
        showToastMessage("All sates have been loaded for country " + selectedCountry.text());
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to sever or server encountered an error");
    });
}

function loadCountries4States() {
    url = contextPath + "countries/list";
    $.get(url, function (rensponseJSON) {
        dropDownCountry4States.empty();
        $.each(rensponseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountry4States);
        });
    }).done(function () {
        buttonLoad4States.val("Refresh Country List");
        showToastMessage("All countries have been loaded");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to sever or server encountered an error");
    });
}

function deleteState() {
    stateId = dropDownStates.val();
    url = contextPath + "states/delete/" + stateId;
    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
    }).done(function () {
        $("#dropDownStates option[value='" + stateId + "']").remove();
        changeFormStateToNew();
        showToastMessage("The state has been deleted");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to sever or server encountered an error");
    });
}

function validateFormState(){
    formState = document.getElementById("formState");
    if(!formState.checkValidity()) {
        formState.reportValidity();
        return false;
    }
    return true;
}

function updateState() {
    if(!validateFormState()) return;

    url = contextPath + "states/save";
    stateId = dropDownStates.val();
    stateName = fieldStateName.val();

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (stateId) {
        $("#dropDownStates option:selected").text(stateName)
        showToastMessage("The state has been update");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to sever or server encountered an error");
    });
}

function addState() {
    if(!validateFormState()) return;

    url = contextPath + "states/save";
    stateName = fieldStateName.val();

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (stateId) {
        selectedNewlyAddedState(stateId, stateName);
        showToastMessage("The new state has been added");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to sever or server encountered an error");
    });

}

function selectedNewlyAddedState(stateId, stateName) {
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

    $("#dropDownStates option[value='" + stateId + "']").prop("selected", true);
    fieldStateName.val("").focus();
}