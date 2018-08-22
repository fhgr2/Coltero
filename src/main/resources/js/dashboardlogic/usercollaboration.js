/**
 * Draws Boxplot based on Date Quarter as Key
 * @param json
 * @param id
 * @param label
 */
function drawBoxPlotQuarterBased(json, id, label) {

    json.forEach(function(d) {
        tempDate = new Date(d.date + '-01');
        d.date = tempDate;
        d.value = +d.value;
    });

    var facts = crossfilter(json);

    var quarterDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });

    var authorsGroup = quarterDimension.group().reduce(
        function(i, d) {
            i.push(d.value);
            return i;
        },
        function(i, d) {
            i.splice(indexOf(d.value), 1);
            return i;
        },
        function() {
            return [];
        }
    );

    var commentatorsPlot = dc.boxPlot(id)
        .width(600)
        .height(400)
        .yAxisLabel(label)
        .margins({
            top: 40,
            bottom: 40,
            left: 80,
            right: 20
        })
        .elasticY(true)
        .dimension(quarterDimension)
        .group(authorsGroup);

    var outlierVisible = true;
    d3.selectAll(id).on("dblclick", function() {
        if (outlierVisible) {
            commentatorsPlot.selectAll('.outlier')
                .attr("r", 0);
            outlierVisible = false;
        } else {
            commentatorsPlot.selectAll('.outlier')
                .attr("r", 5);
            outlierVisible = true;
        }
    });

    commentatorsPlot.render();

}

/**
 * Draws Boxplot based on Date Quarter as Key
 * @param json
 * @param id
 * @param label
 */
function drawBoxPlotQuarterBasedAuthors(json, id, label) {

    json.forEach(function(d) {
        tempDate = new Date(d.date + '-01');
        d.date = tempDate;
        d.count = +d.count;
    });

    var facts = crossfilter(json);

    var quarterDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });


    var authorsGroup = quarterDimension.group().reduce(
        function(i, d) {
            i.push(d.count);
            return i;
        },
        function(i, d) {
            i.splice(indexOf(d.count), 1);
            return i;
        },
        function() {
            return [];
        }
    );

    var commentatorsPlot = dc.boxPlot(id)
        .width(600)
        .height(400)
        .yAxisLabel(label)
        .margins({
            top: 40,
            bottom: 40,
            left: 80,
            right: 20
        })
        .elasticY(true)
        .dimension(quarterDimension)
        .group(authorsGroup);

    var outlierVisible = true;
    d3.selectAll(id).on("dblclick", function() {
        if (outlierVisible) {
            commentatorsPlot.selectAll('.outlier')
                .attr("r", 0);
            outlierVisible = false;
        } else {
            commentatorsPlot.selectAll('.outlier')
                .attr("r", 5);
            outlierVisible = true;
        }
    });

    commentatorsPlot.render();

}
/**
 * Draws Boxplot based on Quarter for interactions
 * @param json
 * @param id
 * @param label
 */
function drawBoxPlotQuarterBasedInteractions(json, id, label) {
    json.forEach(function(d) {
        tempDate = new Date(d.date + '-01');
        d.date = tempDate;
    });

    var facts = crossfilter(json);

    var quarterDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });

    var group = quarterDimension.group().reduce(
        function(i, d) {
            i.push(d.interactions);
            return i;
        },
        function(i, d) {
            i.splice(indexOf(d.interactions), 1);
            return i;
        },
        function() {
            return [];
        }
    );

    var plot = dc.boxPlot(id)
        .width(600)
        .height(400)
        .yAxisLabel(label)
        .elasticY(true)
        .margins({
            top: 40,
            bottom: 40,
            left: 80,
            right: 20
        })
        .dimension(quarterDimension)
        .group(group);

    var outlierVisible = true;
    d3.selectAll(id).on("dblclick", function() {
        if (outlierVisible) {
            plot.selectAll('.outlier')
                .attr("r", 0);
            outlierVisible = false;
        } else {
            plot.selectAll('.outlier')
                .attr("r", 5);
            outlierVisible = true;
        }
    });
    plot.render();
}

/**
 * Draws DayOfWork Barchart
 * @param json
 * @param id
 * @param label
 */
function drawDaysOfWorkBarchart(json, id, label) {
    var facts = crossfilter(json);
    var dayDimension = facts.dimension(function(d) {
        return d.dow;
    });
    var dayGroup = dayDimension.group().reduceSum(function(d) {
        return d.count;
    });

    var barChart = dc.barChart(id) //# = id, . = class
        .width(600)
        .height(300)
        .margins({ top: 40, bottom: 40, left: 80, right: 20 })
        .dimension(dayDimension)
        .brushOn(false)
        .yAxisLabel(label)
        .group(dayGroup)
        .x(d3.scale.ordinal().domain(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']))
        .xUnits(dc.units.ordinal)
        .render();
}

/**
 * Draws Working-hours Barchart
 * @param json
 * @param id
 * @param label
 */
function drawWorkingHoursBarchart(json, id, label) {
    var facts = crossfilter(json);
    var dayDimension = facts.dimension(function(d) {
        return d.hour - 1;
    });
    var dayGroup = dayDimension.group().reduceSum(function(d) {
        return d.count;
    });

    var barChart = dc.barChart(id) //# = id, . = class
        .width(600)
        .height(300)
        .margins({
            top: 40,
            bottom: 40,
            left: 80,
            right: 20
        })
        .brushOn(false)
        .dimension(dayDimension)
        .yAxisLabel(label)
        .group(dayGroup)
        .x(d3.scale.linear().domain([0, 24]))
        .render();
}

/**
 * Draws created Spaces Barchart
 * @param json
 * @param id
 * @param label
 */
function drawAvailableSpaces(json, id, label) {
    json.forEach(function(d) {
        var date = d.date + '-01';
        var tempDate = new Date(d.date);
        d.date = tempDate
    });

    var facts = crossfilter(json); // row of data, indexing data...

    var dateDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });

    var dateGroup = dateDimension.group().reduceSum(function(d) { return d.count; });

    var lineChart = dc.lineChart(id) //# = id, . = class
        .width(600)
        .height(300)
        .renderHorizontalGridLines(true)
        .margins({ top: 40, bottom: 40, left: 80, right: 20 })
        .brushOn(false)
        .renderArea(true)
        .dimension(dateDimension)
        .group(dateGroup)
        .yAxisLabel(label)
        .x(d3.scale.ordinal())
        .xUnits(dc.units.ordinal)
        .render();
}
/**
 * Draws Space Types
 * @param json
 * @param id
 * @param label
 */
function drawSpaceTypes(json, id, label) {
    json.forEach(function(d) {
        var date = d.date + '-01';
        var tempDate = new Date(d.date);
        d.date = tempDate
    });

    var facts = crossfilter(json); // row of data, indexing data...

    var dateDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });

    var typeGroup = dateDimension.group().reduce(
        function(i, d) {
            i[d.type] = (i[d.type] || 0) + d.count;
            return i;
        },
        function(i, d) {
            i[d.type] = (i[d.type] || 0) - d.count;
            return i;
        },
        function() {
            return {};
        }
    )
    var lineChart1 = dc.compositeChart(id);
    lineChart1
        .width(600)
        .height(300)
        .dimension(dateDimension)
        .group(typeGroup)
        .margins({
            top: 40,
            bottom: 40,
            left: 80,
            right: 20
        })
        .renderHorizontalGridLines(true)
        .colors(d3.scale.category10())
        .yAxisLabel(label)
        .shareTitle(false)
        .legend(dc.legend().x(20).y(5).itemHeight(13).gap(5))
        .x(d3.scale.ordinal())
        .xUnits(dc.units.ordinal)
        .compose([
            dc.lineChart(lineChart1).group(typeGroup, "Personal").dimension(dateDimension).valueAccessor(function(d) {
                return d.value['personal'] || 0;
            }).title(function(d) {
                return d.value['personal'] || 0;
            }).colors("blue"),
            dc.lineChart(lineChart1).group(typeGroup, "Global").dimension(dateDimension).valueAccessor(function(d) {
                return d.value['global'] || 0;
            }).title(function(d) {
                return d.value['global'] || 0;
            }).colors("orange")
        ]);
    lineChart1.render();
}

/**
 * Draws Tagged Space Barchart
 * @param json
 * @param id
 * @param label
 */
function drawTaggedSpacesBarchart(json, id, label, spaceTag) {

    if (Object.keys(json).length == 0) {
        $(id).text("No data available...")
    } else {
        json.forEach(function(d) {
            var date = d.date + '-01';
            var tempDate = new Date(d.date);
            d.date = tempDate
        });

        var facts = crossfilter(json); // row of data, indexing data...

        var dateDimension = facts.dimension(function(d) {
            var quarter = getQuarter(d.date);
            return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
        });
        var taggedGroup = dateDimension.group().reduce(
            function(i, d) {
                i["total"] = (i["total"] || 0) + d.totalSpaces;
                i["tagged"] = (i["tagged"] || 0) + d.taggedSpaces;
                return i;
            },
            function(i, d) {
                i["total"] = (i["total"] || 0) - d.totalSpaces;
                i["tagged"] = (i["tagged"] || 0) - d.taggedSpaces;
                return i;
            },
            function() {
                return {};
            }
        );

        var lineChart = dc.lineChart(id) //# = id, . = class
            .width(600)
            .height(300)
            .margins({
                top: 40,
                bottom: 40,
                left: 80,
                right: 20
            })
            .renderHorizontalGridLines(true)
            .brushOn(false)
            .valueAccessor(function(d) {
                return d.value["tagged"] || 0;
            })
            .title(function(d) {
                if (d.value["total"] != 0) {
                    var precent = ((d.value["tagged"] / d.value["total"]) * 100).toFixed(2);
                }
                if (isNaN(precent) || precent == null) {
                    precent = 0;
                }
                return precent + "%";
            })
            .dimension(dateDimension)
            .group(taggedGroup, spaceTag)
            .legend(dc.legend().x(110).y(40).itemHeight(15).gap(5))
            .yAxisLabel(label)
            .x(d3.scale.ordinal())
            .xUnits(dc.units.ordinal);

        lineChart.render();
    }
}

/**
 * Draws line Charts for Spaces with all tags except the predefined tags above...
 * @param {*} json 
 * @param {*} id 
 * @param {*} label 
 */
function drawOtherSpacesLineChart(json, id, label) {
    var lineChart1 = dc.compositeChart(id);
    var fieldSet = new Set();

    json.forEach(function(d) {
        var date = d.date + '-01';
        var tempDate = new Date(d.date);
        d.date = tempDate
        fieldSet.add(d.spaceTagName); // add all distinct spaces for dynamic stacking
    });

    var facts = crossfilter(json);

    var dateDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });

    var totalSpaceGroup = dateDimension.group().reduceSum(function(d) {
        return d.totalSpaces;
    });

    var taggedGroup = dateDimension.group().reduce(function(i, d) {
            i[d.spaceTagName + "-tagged"] = (i[d.spaceTagName] || 0) + d.spaceCount;
            i[d.spaceTagName + "-total"] = (i[d.spaceTagName] || 0) + d.totalSpaces;
            return i;
        }, function(i, d) {
            i[d.spaceTagName + "-tagged"] = (i[d.spaceTagName] || 0) - d.spaceCount;
            i[d.spaceTagName + "-total"] = (i[d.spaceTagName] || 0) - d.totalSpaces;
            return i;
        },
        function() {
            return {};
        });

    var chartArray = new Array();
    var index = 0;
    fieldSet.forEach(function(field, i) {
            chartArray.push(dc.lineChart(lineChart1, field)
                .dimension(dateDimension)
                .group(taggedGroup, field.replace(/\b\w/g, function(l) {
                    return l.toUpperCase()
                }))
                .valueAccessor(function(d) {
                    return d.value[field + "-tagged"] || 0;
                }).title(function(d) {
                    var total = d.value[field + "-total"];
                    if (total > 0) {
                        return ((d.value[field + "-tagged"] / total) * 100).toFixed(2) + "%";
                    }
                    return 0.0 + "%";
                }))
        }

    );
    lineChart1
        .width(600)
        .height(300)
        .margins({
            top: 40,
            bottom: 40,
            left: 80,
            right: 20
        })

    .renderTitle(true)
        .dimension(dateDimension)
        .group(totalSpaceGroup)
        .renderHorizontalGridLines(true)
        .colors(d3.scale.category20b())
        .shareColors(true)
        .shareTitle(false)
        .yAxisLabel(label)
        .legend(dc.legend().x(110).y(40).itemHeight(15).gap(5))
        .x(d3.scale.ordinal())
        .xUnits(dc.units.ordinal)
        .compose(
            chartArray
        );

    lineChart1.render();
}
/**
 * Draws line Chart with Isolated Spaces
 * @param {*} json 
 * @param {*} id 
 * @param {*} label 
 */
function drawIsolatedSpaces(json, id, label) {
    json.forEach(function(d) {
        var tempDate = new Date(d.date);
        d.date = tempDate;
    });

    var facts = crossfilter(json); // row of data, indexing data...

    var dateDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });

    var isolatedGroup = dateDimension.group().reduceSum(function(d) {
        return d.count || 0;
    })

    var lineChart = dc.lineChart(id)
        .width(600)
        .height(300)
        .margins({
            top: 40,
            bottom: 40,
            left: 80,
            right: 20
        })
        .dimension(dateDimension)
        .group(isolatedGroup)
        .yAxisLabel(label)
        .renderHorizontalGridLines(true)
        .renderArea(true)
        .x(d3.scale.ordinal())
        .xUnits(dc.units.ordinal);
    lineChart.xAxis().ticks(4);
    lineChart.render();
}

/**
 * Draws active / inactive user Barchart
 * @param {*} json 
 * @param {*} id 
 * @param {*} label 
 */
function drawActiveUsersBarChart(json, id, label) {
    json.forEach(function(d) {
        var date = d.date + '-01';
        var tempDate = new Date(d.date);
        d.date = tempDate
    });

    function getQuarter(d) {
        d = d || new Date(); // If no date supplied, use today
        var q = [1, 2, 3, 4];
        return q[Math.floor(d.getMonth() / 3)];
    }
    var facts = crossfilter(json); // row of data, indexing data...

    var dateDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });

    var activeGroup = dateDimension.group().reduce(function(i, d) {
            i["active"] = (i["active"] || 0) + d.active;
            i["inactive"] = (i["inactive"] || 0) + d.inactive;
            return i;
        },
        function(i, d) {
            i["active"] = (i["active"] - d.active);
            i["inactive"] = i["inactive"] - d.inactive;
            return i;
        },
        function(d) {
            return {};
        });

    var barChart = dc.lineChart(id) //# = id, . = class
        .width(600)
        .height(300)
        .margins({
            top: 40,
            bottom: 40,
            left: 80,
            right: 20
        })
        .title(function(d) {
            var total = d.value["active"] + d.value["inactive"];
            var precent = (d.value["active"] / total);
            if (isNaN(precent)) {
                precent = 0;
            }
            return (precent * 100).toFixed(2) + "% users are active";
        })
        .brushOn(false)
        .renderHorizontalGridLines(true)
        .dimension(dateDimension)
        .group(activeGroup, "Active users", function(d) {
            var total = d.value["active"] + d.value["inactive"];
            var precent = (d.value["active"] / total);
            if (isNaN(precent)) {
                precent = 0;
            }
            return (precent * 100).toFixed(2)
        })
        .renderArea(true)
        .yAxisLabel(label)
        .x(d3.scale.ordinal())
        .xUnits(dc.units.ordinal)
        .y(d3.scale.linear().domain([0, 100]));

    barChart.render();

}
/**
 * Draws top 10 spaces on user count as table
 * @param {*} json 
 * @param {*} id 
 */
function drawTop10SpaceUserCount(json, id) {

    var facts = crossfilter(json);
    var dateDimension = facts.dimension(function(d) {
        return d.spaceName;
    });

    var dataTable = dc.dataTable(id)
        .width(600)
        .height(300)
        .dimension(dateDimension)
        .showGroups(false)
        // .size(5)
        .group(function(d) {
            return d.type;
        })
        .columns([{
            label: "Space title",
            format: function(d) {
                return d.spaceName;
            }
        }, {
            label: "# users",
            format: function(d) {
                return d.count;
            }
        }])
        .sortBy(function(d) {
            return d.count;
        })
        .order(d3.descending);

    dataTable.render();
}

/**
 * Draws top 10 space comments 
 * @param {*} json 
 * @param {*} id 
 */
function drawTop10SpaceCommentCount(json, id) {

    var facts = crossfilter(json);
    var dateDimension = facts.dimension(function(d) {
        return d.spaceName;
    });

    var dataTable = dc.dataTable(id)
        .width(600)
        .height(300)
        .dimension(dateDimension)
        .showGroups(false)
        // .size(5)
        .group(function(d) {
            return d.type;
        })
        .columns([{
            label: "Space title",
            format: function(d) {
                return d.spaceName;
            }
        }, {
            label: "# words",
            format: function(d) {
                return d.wordCount;
            }
        }])
        .sortBy(function(d) {
            return d.wordCount;
        })
        .order(d3.descending);

    dataTable.render();
}

/**
 * Draws top 10 values as table
 * @param {*} json 
 * @param {*} id 
 */
function drawTop10SpaceWordCounts(json, id) {

    var facts = crossfilter(json);
    var dateDimension = facts.dimension(function(d) {
        return d.spaceName;
    });

    var dataTable = dc.dataTable(id)
        .width(600)
        .height(300)
        .dimension(dateDimension)
        .showGroups(false)
        // .size(5)
        .group(function(d) {
            return d.type;
        })
        .columns([{
            label: "Space title",
            format: function(d) {
                return d.spaceName;
            }
        }, {
            label: "# words",
            format: function(d) {
                return d.wordCount;
            }
        }])
        .sortBy(function(d) {
            return d.wordCount;
        })
        .order(d3.descending);

    dataTable.render();
}


/**
 * Draws top 10 values as table
 * @param {*} json 
 * @param {*} id 
 */
function drawTop10AuthorsCommentatorsUploaders(json, id) {

    var facts = crossfilter(json);
    var dateDimension = facts.dimension(function(d) {
        return d.spaceName;
    });

    var dataTable = dc.dataTable(id)
        .width(600)
        .height(300)
        .dimension(dateDimension)
        .showGroups(false)
        // .size(5)
        .group(function(d) {
            return d.type;
        })
        .columns([{
            label: "Space title",
            format: function(d) {
                return d.spaceName;
            }
        }, {
            label: "# commtores",
            format: function(d) {
                return d.count;
            }
        }])
        .sortBy(function(d) {
            return d.count;
        })
        .order(d3.descending);

    dataTable.render();
}

/**
 * Draws top 10 values as table
 * @param {*} json 
 * @param {*} id 
 */
function drawTop10CollaborativeSpaces(json, id) {

    var facts = crossfilter(json);
    var dateDimension = facts.dimension(function(d) {
        return d.spaceName;
    });

    var dataTable = dc.dataTable(id)
        .width(600)
        .height(300)
        .dimension(dateDimension)
        .showGroups(false)
        // .size(5)
        .group(function(d) {
            return d.type;
        })
        .columns([{
            label: "Space title",
            format: function(d) {
                return d.spaceName;
            }
        }, {
            label: "# authors",
            format: function(d) {
                return d.count;
            }
        }])
        .sortBy(function(d) {
            return d.count;
        })
        .order(d3.descending);

    dataTable.render();
}

/**
 * Draws top 10 values as table
 * @param {*} json 
 * @param {*} id 
 */
function drawTop10CommentedSpaces(json, id) {

    var facts = crossfilter(json);
    var dateDimension = facts.dimension(function(d) {
        return d.spaceName;
    });

    var dataTable = dc.dataTable(id)
        .width(600)
        .height(300)
        .dimension(dateDimension)
        .showGroups(false)
        // .size(5)
        .group(function(d) {
            return d.type;
        })
        .columns([{
            label: "Space title",
            format: function(d) {
                return d.spaceName;
            }
        }, {
            label: "# comments",
            format: function(d) {
                return d.count;
            }
        }])
        .sortBy(function(d) {
            return d.count;
        })
        .order(d3.descending);

    dataTable.render();
}

function drawActiveUserPerLocationChart(json, id, label) {
    var lineChart1 = dc.compositeChart(id);

    var fieldSet = new Set();
    json.forEach(function(d) {
        var date = d.date + '-01';
        var tempDate = new Date(d.date);
        d.date = tempDate
        fieldSet.add(d.location); // add all distinct spaces for dynamic stacking
    });

    function getQuarter(d) {
        d = d || new Date(); // If no date supplied, use today
        var q = [1, 2, 3, 4];
        return q[Math.floor(d.getMonth() / 3)];
    }

    var facts = crossfilter(json); // row of data, indexing data...

    var dateDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });

    var locationGroup = dateDimension.group().reduce(function(i, d) {
            i[d.location + "active"] = (i[d.location + "active"] || 0) + d.activeUsersInLocation;
            i[d.location + "inactive"] = (i[d.location + "inactive"] || 0) + d.allActiveUsers;
            return i;
        }, function(i, d) {
            i[d.location + "active"] = (i[d.location + "active"] || 0) - d.activeUsersInLocation;
            i[d.location + "inactive"] = (i[d.location + "inactive"] || 0) - d.allActiveUsers;
            return i;
        },
        function() {
            return {};
        });

    var chartArray = new Array();
    fieldSet.forEach(function(field, i) {
            chartArray.push(dc.lineChart(lineChart1, field)
                .dimension(dateDimension)
                .group(locationGroup, field)
                .title(function(d) {
                    var total = (d.value[field + "active"]) + (d.value[field + "inactive"]) || 0;
                    if (!isNaN(total)) {
                        var precent = (d.value[field + "active"] / total) || 0;
                        return (precent * 100).toFixed(2) + "\n + " +
                            d.value[field + "active"];
                    }
                })
                .valueAccessor(function(d) {
                    var total = (d.value[field + "active"]) + (d.value[field + "inactive"]) || 0;
                    if (!isNaN(total)) {
                        var precent = (d.value[field + "active"] / total) || 0;
                        return (precent * 100).toFixed(2);
                    }
                })
            );
        }

    );

    lineChart1
        .width(600)
        .height(300)
        .margins({
            top: 10,
            bottom: 30,
            right: 10,
            left: 200
        })
        .dimension(dateDimension)
        .colors(d3.scale.category20b())
        .shareColors(true)
        .shareTitle(false)
        .renderTitle(true)
        .group(locationGroup)
        .renderHorizontalGridLines(true)
        .yAxisLabel(label)
        .legend(dc.legend().x(20).y(5).itemHeight(13).gap(5))
        .x(d3.scale.ordinal())
        .xUnits(dc.units.ordinal)
        .y(d3.scale.sqrt().domain([0, 100]))
        ._rangeBandPadding(1)
        .compose(
            chartArray
        );

    lineChart1.render();
}
/**
 * Draws Collaboration-metric Chart
 * @param {*} json 
 * @param {*} id 
 */
function drawPieChartCollaborationMetric(json, id) {
    var facts = crossfilter(json); // row of data, indexing data...

    var all = facts.groupAll();

    var sumtotal = all.reduceSum(function(d) {
        return d.count;
    }).value();

    var typeDimension = facts.dimension(function(d) {
        return d.type;
    });

    var typeGroup = typeDimension.group().reduceSum(function(d) {
        return d.count;
    })

    var pieChart = dc.pieChart(id) //# = id, . = class
        .width(600)
        .height(300)
        .radius(150)
        .innerRadius(80)
        .renderLabel(false)
        .legend(dc.legend().x(20).y(5).itemHeight(13).gap(5))
        .title(function(d) {
            if (d.key == "collaboration") {
                return ((d.value / sumtotal) * 100).toFixed(2) + "% " + d.key;
            }
            if (d.key == "documentation") {
                return ((d.value / sumtotal) * 100).toFixed(2) + "% " + d.key;
            }
        })

    .colors(d3.scale.category10())
        .transitionDuration(1500)
        .dimension(typeDimension)
        .group(typeGroup);

    pieChart.on('pretransition', function(chart) {
        pieChart.selectAll('.dc-legend-item text')
            .text('')
            .append('tspan')
            .text(function(d) {
                return d.name.replace(/\b\w/g, function(l) { return l.toUpperCase() });
            })
            .append('tspan')
            .attr('x', 150)
            .attr('text-anchor', 'end')
            .text(function(d) {
                return ((d.data / sumtotal) * 100).toFixed(2) + " %";
            });
    });
    pieChart.render();
}

/**
 * Draws word-cloud
 * @param {*} json 
 * @param {*} id 
 */
function drawWordCloud(json, id) {
    var wordcloudChart = dc.wordcloudChart(id);

    var facts = crossfilter(json);

    var wordDim = facts.dimension(function(d) {
        return d.name;
    });

    var wordGroup = wordDim.group().reduceSum(function(d) {
        return d.count;
    });

    wordcloudChart
        .height(300)
        .width(600)
        .minY(-20)
        .minX(-150)
        .relativeSize(5)
        .dimension(wordDim)
        .group(wordGroup)
        .valueAccessor(function(d) {
            return d.value;
        })
        .font('inherit')
        .title(function(d) {
            return d.key + " occurence: " + d.value;
        });
    wordcloudChart.render();
}

/**
 * Draws word-cloud
 * @param {*} json 
 * @param {*} id 
 */
function drawTagCloudSpace(json, id) {
    var wordcloudChart = dc.wordcloudChart(id);

    var facts = crossfilter(json);

    var wordDim = facts.dimension(function(d) {
        return d.name;
    });

    var wordGroup = wordDim.group().reduceSum(function(d) {
        return d.count;
    });

    wordcloudChart
        .height(300)
        .width(600)
        .minY(-20)
        .minX(-150)
        .relativeSize(12)
        .dimension(wordDim)
        .group(wordGroup)
        .valueAccessor(function(d) {
            return d.value;
        })
        .font('inherit')
        .title(function(d) {
            return d.key + " occurence: " + d.value;
        });
    wordcloudChart.render();
}
/**
/**
 * Draws line chart with accumulated number of tags over time
 * @param {*} json 
 * @param {*} id 
 * @param {*} label 
 */
function drawTagCount(json, id, label) {
    json.forEach(function(d) {
        var tempDate = new Date(d.date);
        d.date = tempDate;
    });

    var facts = crossfilter(json); // row of data, indexing data...

    var dateDimension = facts.dimension(function(d) {
        var quarter = getQuarter(d.date);
        return d.date.getFullYear().toString().substr(-2) + ' Q' + quarter;
    });

    var typeGroup = dateDimension.group().reduceSum(function(d) {
        return d.count;
    });

    var lineChart = dc.lineChart(id) //# = id, . = class
        .width(600)
        .height(300)
        .margins({
            top: 10,
            bottom: 30,
            right: 10,
            left: 80
        })
        .dimension(dateDimension)
        .group(typeGroup)
        .yAxisLabel(label)
        .renderHorizontalGridLines(true)
        .renderArea(true)
        .x(d3.scale.ordinal())
        .xUnits(dc.units.ordinal);

    lineChart.yAxis().ticks(5);
    lineChart.xAxis().ticks(4);
    lineChart.render();
}
/**
 * Draws PieChart userlocations
 * @param {*} json 
 * @param {*} id 
 */
function drawSpaceUserLoactionRatios(json, id) {
    var facts = crossfilter(json);

    var all = facts.groupAll();

    var sumtotal = all.reduceSum(function(d) {
        return d.count;
    }).value();

    var typeDimension = facts.dimension(function(d) {
        return d.location;
    });

    var typeGroup = typeDimension.group().reduceSum(function(d) {
        return d.count;
    });

    var pieChart = dc.pieChart(id) //# = id, . = class
        .height(300)
        .width(600)
        .radius(150)
        .innerRadius(80)
        .renderLabel(false)
        .legend(dc.legend().x(20).y(5).itemHeight(13).gap(5))
        .title(function(d) {
            return ((d.value / sumtotal) * 100).toFixed(2) + "% from location " + d.key;
        })
        .colors(d3.scale.category10())
        .transitionDuration(1500)
        .dimension(typeDimension)
        .group(typeGroup);

    pieChart.render();
}
/**
 * Pie Chart attachments (tagged / untagged) ratio
 * @param {*} json 
 * @param {*} id 
 */
function drawAttachmentTagRatio(json, id) {
    var facts = crossfilter(json);

    var all = facts.groupAll();

    var sumtotal = all.reduceSum(function(d) {
        return d.count;
    }).value();

    var typeDimension = facts.dimension(function(d) {
        return d.type;
    });

    var typeGroup = typeDimension.group().reduceSum(function(d) {
        return d.count;
    });

    var pieChart = dc.pieChart(id) //# = id, . = class
        .height(300)
        .width(600)
        .radius(150)
        .innerRadius(80)
        .renderLabel(false)
        .legend(dc.legend().x(20).y(5).itemHeight(13).gap(5))
        .title(function(d) {
            if (d.key == "tagged") {
                return ((d.value / sumtotal) * 100).toFixed(2) + "% " + d.key;
            }
            if (d.key == "untagged") {
                return ((d.value / sumtotal) * 100).toFixed(2) + "% " + d.key;
            }
        })
        .colors(d3.scale.category10())
        .transitionDuration(1500)
        .dimension(typeDimension)
        .group(typeGroup);

    pieChart.on('pretransition', function(chart) {
        pieChart.selectAll('.dc-legend-item text')
            .text('')
            .append('tspan')
            .text(function(d) {
                return d.name.replace(/\b\w/g, function(l) {
                    return l.toUpperCase()
                });
            })
            .append('tspan')
            .attr('x', 110)
            .attr('text-anchor', 'end')
            .text(function(d) {
                return ((d.data / sumtotal) * 100).toFixed(2) + " %";
            });
    });
    pieChart.render();
}

/**
 * Draws user connections based on user
 * @param {*} json 
 * @param {*} id 
 */
function drawUserConnectionTable(json, id) {

    var facts = crossfilter(json);
    var dateDimension = facts.dimension(function(d) {
        return d;
    });

    var dataTable = dc.dataTable(id)
        .width(300)
        .height(600)
        .dimension(dateDimension)
        .showGroups(false)
        // .size(5)
        .group(function(d) {
            return d;
        })
        .size(Infinity)
        .columns([{
            label: "Username",
            format: function(d) {
                return d;
            }
        }])
        .sortBy(function(d) {
            return d;
        })
        .order(d3.ascending);

    dataTable.render();


}