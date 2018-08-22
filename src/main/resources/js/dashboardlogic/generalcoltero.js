/**
 * Change General KPI
 * @param values (rest response)
 */
function changeKpiValues(values) {
    $('#total-users').html(values.userCount);
    $('#total-pages').html(values.pageCount);
    $('#total-comments').html(values.commentCount);
    $('#total-commentators').html(values.commentatorsCount);
    $('#total-authors').html(values.authorsCount);
    $('#total-uploaders').html(values.uploadersCount);
    $('#total-slackers').html(values.slackersCount);
    $('#total-taggers').html(values.traggersCount);
    $('#total-likers').html(values.likersCount);
}

/**
 * SpaceDashboard
 * Get simple Comment and PageCount of space
 */
function getSpaceCommentPageCount(json) {
    $('#total-comments').html(json.commentSum);
    $('#total-pages').html(json.pageSum);
}

/**
 * PageDashboard
 * 
 * @param {*} json 
 */
function getPageComments(json) {
    $('#chart-comments').html(json.comments);
}
/**
 * Commentators and Authors
 * @param {*} json 
 */
function getPageComAuthors(json) {
    $('#chart-commentators').html(json.commentators);
    $('#chart-authors').html(json.authors);
}
/**
 * Counts words on page
 * @param {*} json 
 */
function getPageWordCount(json) {
    $('#chart-words').html(json.wordCount);
}

/**
 * User dashboard
 * @param {*} json 
 */
function getAllUserStatistics(json) {
    $('#chart-reactions').html(json.reactions);
    $('#chart-reactionsAvg').html(json.reactionsAvg);
    $('#chart-contentrange').html(json.userRange);
    $('#chart-contentrangeAvg').html(json.userRangeAvg);
    $('#chart-connections').html(json.userConnections);
    $('#chart-connectionsAvg').html(json.userConnectionsAvg);
    $('#chart-writtenwords').html(json.writtenWords);
    $('#chart-writtenwordsavg').html(json.writtenWordsAvg);
}