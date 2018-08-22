/**
 * Draws geo Map
 * @param {*} locations map data with cities and their coordinates
 * @param {*} data incl. lat / long
 * @param {*} id dom id
 * @param {*} width 
 * @param {*} height 
 */
function drawGeoMap(locations, data, id, width, height) {
    var w = width | 1280,
        h = height | 660;

    data.nodes.forEach(function(d) {
        d.latitude = +d.latitude;
        d.longitude = +d.longitude;
    });

    var projection = d3v2.geo.mercator()
        .scale(1450)
        .translate([640, 460]);

    var path = d3v2.geo.path()
        .projection(projection);

    var svg = d3v2.select(id).insert("svg:svg", "h2")
        .attr("width", w)
        .attr("height", h);

    var states = svg.append("svg:g")
        .attr("id", "states");

    var circles = svg.append("svg:g")
        .attr("id", "circles");

    var cells = svg.append("svg:g")
        .attr("id", "cells");

    d3v2.select("input[type=checkbox]").on("change", function() {
        cells.classed("voronoi", this.checked);
    });

    states.selectAll("path")
        .data(locations["features"])
        .enter().append("svg:path")
        .attr("d", path);

    var linksByOrigin = {},
        countBySpace = {},
        locationBySpace = {},
        positions = [];

    var arc = d3v2.geo.greatArc().precision(3)
        .source(function(d) { return locationBySpace[d.source]; })
        .target(function(d) { return locationBySpace[d.target]; });

    data.links.forEach(function(connection) {
        var sourceName = connection.sourceName,
            targetName = connection.targetName,
            weight = connection.weight,
            links = linksByOrigin[sourceName] || (linksByOrigin[sourceName] = []);
        links.push({ source: sourceName, target: targetName, weight: weight });
        countBySpace[sourceName] = (countBySpace[sourceName] || 0) + 1;
        countBySpace[targetName] = (countBySpace[targetName] || 0) + 1;
    });

    var link_weight_max = d3v2.max(data.links, function(link) {
        return +link.weight;
    })

    var link_weight_min = d3v2.min(data.links, function(link) {
        return +link.weight;
    })

    var link_width_weight_scaled = d3v2.scale.log()
        .domain([link_weight_min, link_weight_max])
        .range([2, 5])

    var link_colour_weight_scaled = d3v2.scale.log()
        .domain([link_weight_min, link_weight_max])
        .range(["#679", "#235"])


    // Only consider spaces with at least one connection.
    spaces = data.nodes.filter(function(space) {
        if (countBySpace[space.name]) {
            var location = [+space.longitude, +space.latitude];
            locationBySpace[space.name] = location;
            positions.push(projection(location));
            return true;
        }
    });

    // Compute the Voronoi diagram of spaces' projected positions.
    var polygons = d3v2.geom.voronoi(positions);

    var g = cells.selectAll("g")
        .data(spaces)
        .enter().append("svg:g");

    g.append("svg:path")
        .attr("class", "cell")
        .attr("d", function(d, i) { return "M" + polygons[i].join("L") + "Z"; })
        .on("mouseover", function(d) {
            d3v2.select("h2 span")
                .text(d.name + " : " + d.userCount + " users");
        });

    var arcs = g.selectAll("path.arc")
        .data(function(d) { return linksByOrigin[d.name] || []; })
        .enter().append("svg:path")
        .attr("class", "arc")
        .style("stroke-width", 1.5)
        .style("stroke", function(d) { return link_colour_weight_scaled(d.weight) })
        .style('stroke-width', function(d) { return link_width_weight_scaled(d.weight) })
        .style("fill", "none")
        .attr("d", function(d) { return path(arc(d)); });

    arcs.append("svg:text")
        .attr("id", "title")
        .text(function(d) {
            return "test";
        });

    circles.selectAll("circle")
        .data(spaces)
        .enter().append("svg:circle")
        .attr("cx", function(d, i) { return positions[i][0]; })
        .attr("cy", function(d, i) { return positions[i][1]; })
        .attr("r", function(d) { return Math.sqrt(countBySpace[d.name]); })
        .sort(function(a, b) { return countBySpace[b.name] - countBySpace[a.name]; });

    var text = svg.append("svg:g").selectAll("g")
        .data(spaces)
        .enter().append("svg:g");

    text.append("svg:text")
        .attr("x", function(d, i) { return positions[i][0] + 20; })
        .attr("y", function(d, i) { return positions[i][1] + 12; })
        .attr("id", "title")
        .text(function(d) {
            return d.name + ": " +
                d.userCount + " users";
        });

}