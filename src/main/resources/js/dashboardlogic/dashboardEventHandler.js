/**
 * Handler for Tag Tabs
 * Just calculated if clicked
 */
var tagTabCalculated = false;

function calculateAndDrawTagTab() {
    if (!tagTabCalculated) {
        genericQuerieExecutor(function(json) {
            drawWordCloud(json, "#chart-tag-cloud")
        }, "tags", "qry_top_tags");

        genericQuerieExecutor(function(json) {
            drawTagCount(json, "#chart-numbertags", "Number of tags")
        }, "tags", "QRY_TAG_OT");

        genericQuerieExecutor(function(json) {
            drawAttachmentTagRatio(json, "#chart-tagratio")
        }, "tags", "QRY_ATT_RATIO");

        genericQuerieExecutor(function(json) {
            drawTagNetwork(json, "#network-tag")
        }, "tags", "QRY_TAG_NETWORK");
    }
    tagTabCalculated = true;
}

/**
 * Handler for space tabs
 * Just calculated if clicked
 */
var spaceCalculated = false;

function calculateAndDrawSpace() {
    if (!spaceCalculated) {

        getSpaceCreations(function(json) {
            drawAvailableSpaces(json, "#linechart-numberofspaces", "Number of available spaces");
        });

        getSpaceTypes(function(json) {
            drawSpaceTypes(json, "#linechart-spacetypes", "Space types");
        });
        getSpacesWithTag(function(json) {
            drawTaggedSpacesBarchart(json, "#stackedbar-project", "All Spaces vs. spaces with tag project", "Project");
        }, "QRY_SPC_PROJECT");

        getSpacesWithTag(function(json) {
            drawTaggedSpacesBarchart(json, "#stackedbar-development", "All Spaces vs. spaces with tag development", "Development");
        }, "QRY_SPC_DEVELOPMENT");

        getSpacesWithTag(function(json) {
            drawTaggedSpacesBarchart(json, "#stackedbar-collaboration", "All Spaces vs. spaces with tag collaboration", "Collaboration");
        }, "QRY_SPC_COLLABORATION");

        getSpacesWithTag(function(json) {
            drawTaggedSpacesBarchart(json, "#stackedbar-department", "All Spaces vs. spaces with tag department", "Department");
        }, "QRY_SPC_DEPARTMENT");

        getSpacesWithTag(function(json) {
            drawTaggedSpacesBarchart(json, "#stackedbar-documentation", "All Spaces vs. spaces with tag documentation", "Documentation");
        }, "QRY_SPC_DOCUMENTATION");

        getSpacesWithTag(function(json) {
            drawTaggedSpacesBarchart(json, "#stackedbar-knowledge", "All Spaces vs. spaces with tag knowledge-base", "Knowledge-Base");
        }, "QRY_SPC_KNOWLEDGE-BASE");

        getSpacesWithTag(function(json) {
            drawOtherSpacesLineChart(json, "#stackedbar-other", "All Spaces vs. spaces with other tags", "Other tags");
        }, "QRY_SPC_OTHERPSACES");

        // Isolated Spaces
        genericQuerieExecutor(function(json) {
            drawIsolatedSpaces(json, "#linechart-isolated", "Number of isolated spaces")
        }, "spaces", "QRY_SPC_ISOLATED");

        // top 10 
        genericQuerieExecutor(function(json) {
            drawTop10SpaceUserCount(json, "#chart-top10usercount")
        }, "spaces", "QRY_SPC_TOPUSERCOUNT");

        genericQuerieExecutor(function(json) {
            drawTop10SpaceWordCounts(json, "#chart-top10words")
        }, "spaces", "QRY_SPC_WORDCOUNT");

        genericQuerieExecutor(function(json) {
                drawTop10SpaceCommentCount(json, "#chart-top10commented")
            }, "spaces",
            "QRY_SPC_COMMENTCOUNT");

        genericQuerieExecutor(function(json) {
            drawTop10AuthorsCommentatorsUploaders(json, "#chart-top10commtores")
        }, "spaces", "QRY_SPC_TOPAUTHCOMUPL");

        genericQuerieExecutor(function(json) {
            drawTop10CollaborativeSpaces(json,
                "#chart-top10collaboration")
        }, "spaces", "QRY_SPC_TOPCOLLABORATIVE");

        genericQuerieExecutor(function(json) {
            drawTop10CommentedSpaces(json, "#chart-top10discussed")
        }, "spaces", "QRY_SPC_TOPCOMMENTED");

        genericQuerieExecutor(function(json) {
            drawActiveUsersBarChart(json,
                "#barchart-activeusers", "Active users in %")
        }, "users", "QRY_USR_INACTIVE");

        genericQuerieExecutor(function(json) {
            drawActiveUserPerLocationChart(json, "#chart-activeuserslocation",
                "Number of active users per location in %")
        }, "users", "QRY_USR_ACTIVEPERLOCATION");
    }
    spaceCalculated = true;
}

/**
 * Handler for user tab
 * just calculate when clicked
 */
var userCalculated = false;

function calculatedAndDrawUser() {
    if (!userCalculated) {

        getInteractionPerDay(function(json) {
            drawDaysOfWorkBarchart(json, "#barchart-dow", "Userinteractions per day");
        });

        getInteractionPerHour(function(json) {
            drawWorkingHoursBarchart(json, "#barchart-wokinghours", "Userinteractions per hour");
        });
        getUserInteractionData(function(json) {
            drawBoxPlotQuarterBasedInteractions(json, "#boxplot-interactions", "Number of user-interactions")
        });
    }
    userCalculated = true;
}

/**
 * Handler for content tab
 * just calculate when clicked
 */
var contentCalculated = false;

function calculateAndDrawContent() {
    if (!contentCalculated) {

        genericQuerieExecutor(function(json) {
            drawBoxPlotQuarterBased(json, "#boxplot-comments", "Number of comments")
        }, "boxplots", "QRY_BOX_COMMENTS");

        genericQuerieExecutor(function(json) {
            drawBoxPlotQuarterBased(json, "#boxplot-likes", "Number of likes")
        }, "boxplots", "QRY_BOX_COMMENTS");

        genericQuerieExecutor(function(json) {
            drawBoxPlotQuarterBased(json, "#boxplot-tags", "Number of tags")
        }, "boxplots", "QRY_BOX_TAGS");

        genericQuerieExecutor(function(json) {
            drawBoxPlotQuarterBased(json, "#boxplot-attachments", "Number of attachments")
        }, "boxplots", "QRY_BOX_ATTACHMENTS");
    }
    contentCalculated = true;
}

/**
 * Handler for User and Collaboration tab
 * just calculate when clicked
 */
var userAndCollaborationCalculated = false;

function calculateAndDrawUserAndCollaboration() {
    if (!userAndCollaborationCalculated) {
        //collaboration metric as pie
        genericQuerieExecutor(function(json) {
            drawPieChartCollaborationMetric(json, "#chart-collabmetric")
        }, "collaboration", "QRY_SPC_COLLABVSDOCUMEN");

        genericQuerieExecutor(function(json) {
            drawBoxPlotQuarterBasedAuthors(json, "#boxplot-authors", "Number of authors")
        }, "boxplots", "QRY_BOX_AUTHORS");

        genericQuerieExecutor(function(json) {
            drawBoxPlotQuarterBasedAuthors(json, "#boxplot-commentators", "Number of commentators")
        }, "boxplots", "QRY_BOX_COMMENTATORS");

        genericQuerieExecutor(function(json) {
            drawSharedSpacesNetwork(json, "#location-network")
        }, "spaces", "QRY_SPC_SHAREDLOCATION");

        genericQuerieExecutor(function(json) {
            drawCoSpaceNetwork(json, "#network-cospace")
        }, "spaces", "QRY_SPC_NETWORK");
    }
    userAndCollaborationCalculated = true;
}

var spaceCOntentIsCalculated = false;

function calculateSpaceContent() {
    if (!spaceContentIsCalculated) {
        genericQuerieExecutor(function(json) {
            getSpaceCommentPageCount(json);
        });
    }
    spaceContentIsCalculated = true;
}