/**
 * Return Quarter of Year
 * @param d
 * @returns {number}
 */
function getQuarter(d) {
    d = d || new Date(); // If no date supplied, use today
    var q = [1, 2, 3, 4];
    return q[Math.floor(d.getMonth() / 3)];
}