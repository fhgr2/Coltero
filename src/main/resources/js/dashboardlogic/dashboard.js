/**
 * Converts date to d3plus conform date in style of
 * yyyy-mm
 * @param date
 * @returns {string}
 */
var baseUrl;

function setBaseUrl(url) {
    if (url == undefined) {
        baseUrl = AJS.params.baseUrl;
    }
}

function boxPlotDateHelper(date) {
    var date = new Date(date);
    var month = (date.getMonth() + 1).toString();
    if (month.length < 2) month = '0' + month;
    var year = date.getFullYear();
    return year + "-" + month;
}

/**
 * Queries Global KPI for Coltero Collaboration
 * @param callback
 */
function getColteroKpi(callback) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/counts/QRY_COUNTS",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}

/**
 * Queries UserInteractionData for Boxplot
 * @param callback
 */
function getUserInteractionData(callback) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/users/QRY_USR_INTERACTIONS",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}


/**
 * Queries interactions per Day Data
 * @param callback
 */
function getInteractionPerDay(callback) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/interactions/QRY_INT_DOW",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}

/**
 * Queries interactions per Hour Data
 * @param callback
 */
function getInteractionPerHour(callback) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/interactions/QRY_INT_HOURS",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}
/**
 * Queries created spaces over time
 * @param callback
 */
function getSpaceCreations(callback) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/spaces/QRY_SPC_CREATION",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}
/**
 * Queries spcaes types over time
 * @param callback
 */
function getSpaceTypes(callback) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/spaces/QRY_SPC_TYPES",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}

/**
 * Queries spaces with tag development
 * @param callback
 */
function getSpacesWithTag(callback, qryparam) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/spaces/" + qryparam,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}
/**
 * Executes querie by path and qryparam more generic
 * @param {*} callback 
 * @param {*} path 
 * @param {*} qryparam 
 */
function genericQuerieExecutor(callback, path, qryparam) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/" + path + "/" + qryparam,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}

/**
 * 
 * @param {*} callback1 city coordinates
 * @param {*} callback2 cities and properties
 * @param {*} path 
 * @param {*} qryparam 
 */
function querieWorldMap(callback, path, qryparam) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/" + path + "/" + qryparam,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}

/**
 * Queries City coordinates for whole map
 * @param {*} callback 
 * @param {*} path 
 * @param {*} qryparam 
 */
function querieWorldMapLocationData(callback, path, qryparam) {
    $.ajax({
        url: baseUrl + "/rest/dashboard/1.0/" + path + "/" + qryparam,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(response) {
            callback(response);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus + " : " + errorThrown);
        }
    });
}