// var color = d3.scale.category20()

function color(group) {
    if (group == 0) {
        return "rgb(31,119,180)";
    }
    if (group == 1) {
        return "rgb(174,199,232)";
    }
    if (group == 2) {
        return "rgb(255,127,14)"
    }
    return "rgb(31,119,180)";
}

/**
 * Draws networkgraph 
 * @param {*} json 
 * @param {*} id 
 */
function drawForcedGraph(json, id, type) {
    var tip;
    var graph;
    var link;
    var node;
    var svg;

    //Create an array logging what is connected to what
    var linkedByIndex;

    //Set up the colour scale<
    // var color = d3.scale.category20();

    var force;

    json['links'].forEach(function(link) {
        json['nodes'].forEach(function(node) {
            if (node.name == link.sourceName) {
                link.source = json['nodes'].indexOf(node);
            }
            if (node.name == link.targetName) {
                link.target = json['nodes'].indexOf(node);
            }
        })
    });

    graph = json;

    //Constants for the SVG
    var width = 700,
        height = 530;

    //Set up the force layout
    force = d3.layout.force()
        .charge(function(node) {
            return -500;
        })
        .linkDistance(function(link) {
            return 150;
        })
        .size([width, height]);

    //Append a SVG to the body of the html page. Assign this SVG as an object to svg
    svg = d3.select(id).append("svg")
        .attr("width", width)
        .attr("height", height);

    tip = d3.tip()
        .attr('class', 'd3-tip')
        .offset([-10, 0])
        .html(function(d) {
            if (d.group == 99) {
                // no data available
                return "<b>" + ((typeof(type) !== "undefined" && type == "space") ? d.name : "You") + "</b>" + "<p>" + "no data available" + "</p>";
            }
            if (d.group != 0) {
                return "<b>" + d.name + "</b>" + " Layer: " + d.group + "<p>" + d.tags + "</p>";
            }
            return "<b>" + ((type == "space") ? d.name : "You") + "</b>" + "<p>" + d.tags + "</p>";
        });
    svg.call(tip);

    //Creates the graph data structure out of the json data
    force.nodes(graph.nodes)
        .links(graph.links)
        .start();


    //Create all the line svgs but without locations yet
    link = svg.selectAll(".link")
        .data(graph.links)
        .enter().append("line")
        .attr("class", "link")
        .style("stroke-width", function(d) {
            return 1;
        });

    var drag = force.drag()
        .on("dragstart", dragstart);

    //Do the same with the circles for the nodes - no
    node = svg.selectAll(".node")
        .data(graph.nodes)
        .enter().append("circle")
        .attr("class", "node")
        .attr("r", function(d) {
            return 10;
        })
        .style("fill", function(d) {
            return color(d.group);
        })
        .on('mouseover', tip.show)
        .on('mouseout', tip.hide)
        .on('dblclick', connectedNodes)
        .call(drag); //Added code;

    function dragstart(d) {
        d3.select(this).classed("fixed", d.fixed = true);
    }

    //Now we are giving the SVGs co-ordinates - the force layout is generating the co-ordinates which this code is using to update the attributes of the SVG elements
    force.on("tick", function() {
        link.attr("x1", function(d) {
                return d.source.x;
            })
            .attr("y1", function(d) {
                return d.source.y;
            })
            .attr("x2", function(d) {
                return d.target.x;
            })
            .attr("y2", function(d) {
                return d.target.y;
            });
        svg.selectAll("circle").attr("cx", function(d) {
                return d.x;
            })
            .attr("cy", function(d) {
                return d.y;
            });
        svg.selectAll("text").attr("x", function(d) {
                return d.x;
            })
            .attr("y", function(d) {
                return d.y;
            });
    });
    directAttachedNodes();



    // Calculates directly attached Nodes and their links
    function directAttachedNodes() {
        //Toggle stores whether the highlighting is on
        toggle = 0;

        //Create an array logging what is connected to what
        linkedByIndex = {};
        graph.links.forEach(function(d) {
            linkedByIndex[d.source.index + "," + d.target.index] = 1;
        });
    }

    //This function looks up whether a pair are neighbours
    function neighboring(node_a, node_b) {
        return linkedByIndex[node_a.index + "," + node_b.index] || linkedByIndex[node_b.index + "," + node_a.index] || node_a.index == node_b.index;
    }

    function connectedNodes() {

        if (toggle == 0) {
            //Reduce the opacity of all but the neighbouring nodes
            d = d3.select(this).node().__data__;
            node.style("opacity", function(o) {
                return neighboring(d, o) | neighboring(o, d) ? 1 : 0.1; //
            });

            link.style("opacity", function(o) {
                return d.index == o.source.index | d.index == o.target.index ? 1 : 0.0;
            });

            //Reduce the op

            toggle = 1;
        } else {
            //Put them back to opacity=1
            node.style("opacity", 1);
            link.style("opacity", 1);
            toggle = 0;
        }

    }
}


/**
 * draws network for author and commentators, dragable 
 * @param {*} json 
 * @param {*} id 
 */
function drawAuthorCommentatorNetwork(json, id, width, height) {
    var tip;
    var graph;
    var graphRec;
    var link;
    var node;
    var svg;

    // contains 0 || 1 for visible or not (connected nodes)
    var toggle;
    //Create an array logging what is connected to what
    var linkedByIndex;

    //Set up the colour scale
    // var color = d3.scale.category20();

    var force;
    json['links'].forEach(function(link) {
        json['nodes'].forEach(function(node) {
            if (node.name == link.sourceName) {
                link.source = json['nodes'].indexOf(node);
            }
            if (node.name == link.targetName) {
                link.target = json['nodes'].indexOf(node);
            }
        })
    });


    execute(json);

    function execute(graphData) {
        var link_weight_max = d3.max(graphData.links, function(link) {
            return +link.weight;
        });
        var link_weight_min = d3.min(graphData.links, function(link) {
            return +link.weight;
        });

        var link_width_weight_scaled = d3.scale.log()
            .domain([link_weight_min, link_weight_max])
            .range([1, 4]);

        var node_weight_max = d3.max(graphData.nodes, function(node) {
            return +node.userCount;
        });
        var node_weight_min = d3.min(graphData.nodes, function(node) {
            return +node.userCount;
        });

        var node_size_weight_scaled = d3.scale.linear()
            .domain([node_weight_min, node_weight_max])
            .range([10, 30]);

        graph = graphData;

        //Constants for the SVG
        var w = width || 700,
            h = height || 530;

        //Set up the force layout
        force = d3.layout.force()
            .charge(function(node) {
                return -200;
            })
            .linkDistance(function(link) {
                return 70;
            })
            .size([w, h]);

        //Append a SVG to the body of the html page. Assign this SVG as an object to svg
        svg = d3.select(id).append("svg")
            .attr("width", w)
            .attr("height", h);

        tip = d3.tip()
            .attr('class', 'd3-tip')
            .offset([-10, 0])
            .html(function(d) {
                if (d.group != 0) {
                    return "<b>" + d.name + "</b>" + "</p>" + " Layer: " + d.group + " weight: " + d.weight + "</p>";
                }
                return "<b>You</b>";
            });
        svg.call(tip);

        //Creates the graph data structure out of the json data
        force.nodes(graph.nodes)
            .links(graph.links)
            .start();


        //Create all the line svgs but without locations yet
        link = svg.selectAll(".link")
            .data(graph.links)
            .enter().append("line")
            .attr("class", "link")
            .style("stroke-width", function(d) {
                return link_width_weight_scaled(d.weight);
            });
        var drag = force.drag()
            .on("dragstart", dragstart);
        //Do the same with the circles for the nodes - no
        node = svg.selectAll(".node")
            .data(graph.nodes)
            .enter().append("circle")
            .attr("class", "node")
            .attr("r", function(d) {
                return node_size_weight_scaled(d.userCount);
            })
            .style("fill", function(d) {
                return color(d.group);
            })
            .on('mouseover', tip.show)
            .on('mouseout', tip.hide)
            .on('dblclick', connectedNodes) //Added code;
            .call(drag);

        function dragstart(d) {
            d3.select(this).classed("fixed", d.fixed = true);
        }

        //Now we are giving the SVGs co-ordinates - the force layout is generating the co-ordinates which this code is using to update the attributes of the SVG elements
        force.on("tick", function() {
            link.attr("x1", function(d) {
                    return d.source.x;
                })
                .attr("y1", function(d) {
                    return d.source.y;
                })
                .attr("x2", function(d) {
                    return d.target.x;
                })
                .attr("y2", function(d) {
                    return d.target.y;
                });
            svg.selectAll("circle").attr("cx", function(d) {
                    return d.x;
                })
                .attr("cy", function(d) {
                    return d.y;
                });
            svg.selectAll("text").attr("x", function(d) {
                    return d.x;
                })
                .attr("y", function(d) {
                    return d.y;
                });
        });
        directAttachedNodes();

    }

    // Calculates directly attached Nodes and their links
    function directAttachedNodes() {
        //Toggle stores whether the highlighting is on
        toggle = 0;

        //Create an array logging what is connected to what
        linkedByIndex = {};
        graph.links.forEach(function(d) {
            linkedByIndex[d.source.index + "," + d.target.index] = 1;
        });

    }

    //This function looks up whether a pair are neighbours
    function neighboring(node_a, node_b) {
        return linkedByIndex[node_a.index + "," + node_b.index] || linkedByIndex[node_b.index + "," + node_a.index] || node_a.index == node_b.index;
    }

    function connectedNodes() {

        if (toggle == 0) {
            //Reduce the opacity of all but the neighbouring nodes
            d = d3.select(this).node().__data__;
            node.style("opacity", function(o) {
                return neighboring(d, o) | neighboring(o, d) ? 1 : 0.1; //
            });

            link.style("opacity", function(o) {
                return d.index == o.source.index | d.index == o.target.index ? 1 : 0.0;
            });

            //Reduce the op

            toggle = 1;
        } else {
            //Put them back to opacity=1
            node.style("opacity", 1);
            link.style("opacity", 1);
            toggle = 0;
        }

    }
}

/**
 * Draws networkgraph 
 * @param {*} json 
 * @param {*} id 
 */
function drawSharedSpacesNetwork(json, id) {
    var tip;
    var graph;
    var graphRec;
    var link;
    var node;
    var svg;

    //Create an array logging what is connected to what
    var linkedByIndex;

    //Set up the colour scale<
    var color = d3.scale.category20();

    var force;

    // map node indexes to nodes
    json['links'].forEach(function(link) {
        json['nodes'].forEach(function(node) {
            if (node.name == link.sourceName) {
                link.source = json['nodes'].indexOf(node);
            }
            if (node.name == link.targetName) {
                link.target = json['nodes'].indexOf(node);
            }
        })
    });


    graph = json;

    var link_weight_max = d3.max(graph.links, function(link) {
        return +link.weight;
    });
    var link_weight_min = d3.min(graph.links, function(link) {
        return +link.weight;
    });

    var link_width_weight_scaled = d3.scale.log()
        .domain([link_weight_min, link_weight_max])
        .range([1, 4]);

    var node_weight_max = d3.max(graph.nodes, function(node) {
        return +node.userCount;
    });
    var node_weight_min = d3.min(graph.nodes, function(node) {
        return +node.userCount;
    });

    var node_size_weight_scaled = d3.scale.linear()
        .domain([node_weight_min, node_weight_max])
        .range([10, 30]);


    //Constants for the SVG
    var width = 700,
        height = 530;

    //Set up the force layout
    force = d3.layout.force()
        .charge(function(node) {
            return -500;
        })
        .linkDistance(function(link) {
            return 200;
        })
        .size([width, height]);

    //Append a SVG to the body of the html page. Assign this SVG as an object to svg
    svg = d3.select(id).append("svg")
        .attr("width", width)
        .attr("height", height);

    tip = d3.tip()
        .attr('class', 'd3-tip')
        .offset([-10, 0])
        .html(function(d) {
            return "<b>" + d.name + "</b>";
        });
    svg.call(tip);

    //Creates the graph data structure out of the json data
    force.nodes(graph.nodes)
        .links(graph.links)
        .start();


    //Create all the line svgs but without locations yet
    link = svg.selectAll(".link")
        .data(graph.links)
        .enter().append("line")
        .attr("class", "link")
        .style("stroke-width", function(d) {
            return link_width_weight_scaled(d.weight);
        });

    var drag = force.drag()
        .on("dragstart", dragstart);

    //Do the same with the circles for the nodes - no
    node = svg.selectAll(".node")
        .data(graph.nodes)
        .enter().append("circle")
        .attr("class", "node")
        .attr("r", function(d) {
            return node_size_weight_scaled(d.userCount);
        })
        .style("fill", function(d) {
            return color(d.group);
        })
        .on('mouseover', tip.show)
        .on('mouseout', tip.hide)
        .on('dblclick', connectedNodes)
        .call(drag); //Added code;

    function dragstart(d) {
        d3.select(this).classed("fixed", d.fixed = true);
    }

    //Now we are giving the SVGs co-ordinates - the force layout is generating the co-ordinates which this code is using to update the attributes of the SVG elements
    force.on("tick", function() {
        link.attr("x1", function(d) {
                return d.source.x;
            })
            .attr("y1", function(d) {
                return d.source.y;
            })
            .attr("x2", function(d) {
                return d.target.x;
            })
            .attr("y2", function(d) {
                return d.target.y;
            });
        svg.selectAll("circle").attr("cx", function(d) {
                return d.x;
            })
            .attr("cy", function(d) {
                return d.y;
            });
        svg.selectAll("text").attr("x", function(d) {
                return d.x;
            })
            .attr("y", function(d) {
                return d.y;
            });
    });
    directAttachedNodes();



    // Calculates directly attached Nodes and their links
    function directAttachedNodes() {
        //Toggle stores whether the highlighting is on
        toggle = 0;

        //Create an array logging what is connected to what
        linkedByIndex = {};
        graph.links.forEach(function(d) {
            linkedByIndex[d.source.index + "," + d.target.index] = 1;
        });
    }

    //This function looks up whether a pair are neighbours
    function neighboring(node_a, node_b) {
        return linkedByIndex[node_a.index + "," + node_b.index] || linkedByIndex[node_b.index + "," + node_a.index] || node_a.index == node_b.index;
    }

    function connectedNodes() {

        if (toggle == 0) {
            //Reduce the opacity of all but the neighbouring nodes
            d = d3.select(this).node().__data__;
            node.style("opacity", function(o) {
                return neighboring(d, o) | neighboring(o, d) ? 1 : 0.1; //
            });

            link.style("opacity", function(o) {
                return d.index == o.source.index | d.index == o.target.index ? 1 : 0.0;
            });

            //Reduce the op

            toggle = 1;
        } else {
            //Put them back to opacity=1
            node.style("opacity", 1);
            link.style("opacity", 1);
            toggle = 0;
        }

    }
}

/**
 * Draws networkgraph 
 * @param {*} json 
 * @param {*} id 
 */
function drawCoSpaceNetwork(json, id, width, height) {
    var tip;
    var graph;
    var graphRec;
    var link;
    var node;
    var svg;

    //Create an array logging what is connected to what
    var linkedByIndex;

    //Set up the colour scale<
    // var color = d3.scale.category20();

    var force;

    // map node indexes to nodes
    json['links'].forEach(function(link) {
        json['nodes'].forEach(function(node) {
            if (node.name == link.sourceName) {
                link.source = json['nodes'].indexOf(node);
            }
            if (node.name == link.targetName) {
                link.target = json['nodes'].indexOf(node);
            }
        })
    });


    graph = json;

    var link_weight_max = d3.max(graph.links, function(link) {
        return +link.weight;
    });
    var link_weight_min = d3.min(graph.links, function(link) {
        return +link.weight;
    });

    var link_width_weight_scaled = d3.scale.log()
        .domain([link_weight_min, link_weight_max])
        .range([1, 4]);

    var node_weight_max = d3.max(graph.nodes, function(node) {
        return +node.userCount;
    });
    var node_weight_min = d3.min(graph.nodes, function(node) {
        return +node.userCount;
    });

    var node_size_weight_scaled = d3.scale.linear()
        .domain([node_weight_min, node_weight_max])
        .range([10, 30]);


    //Constants for the SVG
    var width = width || 700,
        height = height || 530;

    //Set up the force layout
    force = d3.layout.force()
        .charge(function(node) {
            return -500;
        })
        .linkDistance(function(link) {
            return 200;
        })
        .size([width, height]);

    //Append a SVG to the body of the html page. Assign this SVG as an object to svg
    svg = d3.select(id).append("svg")
        .attr("width", width)
        .attr("height", height);

    tip = d3.tip()
        .attr('class', 'd3-tip')
        .offset([-10, 0])
        .html(function(d) {
            if (d.group == 101) {
                return "<b>" + d.name + "</b>" + "</p>" + " weight: " + d.weight + "</p>";
            }
            if (d.group != 0) {
                return "<b>" + d.name + "</b>" + "</p>" + " Layer: " + d.group + " weight: " + d.weight + "</p>";
            }
            return "<b>This Space</b>";
        });
    svg.call(tip);

    //Creates the graph data structure out of the json data
    force.nodes(graph.nodes)
        .links(graph.links)
        .start();


    //Create all the line svgs but without locations yet
    link = svg.selectAll(".link")
        .data(graph.links)
        .enter().append("line")
        .attr("class", "link")
        .style("stroke-width", function(d) {
            return link_width_weight_scaled(d.weight);
        });

    var drag = force.drag()
        .on("dragstart", dragstart);

    //Do the same with the circles for the nodes - no
    node = svg.selectAll(".node")
        .data(graph.nodes)
        .enter().append("circle")
        .attr("class", "node")
        .attr("r", function(d) {
            return node_size_weight_scaled(d.userCount);
        })
        .style("fill", function(d) {
            return color(d.group);
        })
        .on('mouseover', tip.show)
        .on('mouseout', tip.hide)
        .on('dblclick', connectedNodes)
        .call(drag); //Added code;

    function dragstart(d) {
        d3.select(this).classed("fixed", d.fixed = true);
    }

    //Now we are giving the SVGs co-ordinates - the force layout is generating the co-ordinates which this code is using to update the attributes of the SVG elements
    force.on("tick", function() {
        link.attr("x1", function(d) {
                return d.source.x;
            })
            .attr("y1", function(d) {
                return d.source.y;
            })
            .attr("x2", function(d) {
                return d.target.x;
            })
            .attr("y2", function(d) {
                return d.target.y;
            });
        svg.selectAll("circle").attr("cx", function(d) {
                return d.x;
            })
            .attr("cy", function(d) {
                return d.y;
            });
        svg.selectAll("text").attr("x", function(d) {
                return d.x;
            })
            .attr("y", function(d) {
                return d.y;
            });
    });
    directAttachedNodes();



    // Calculates directly attached Nodes and their links
    function directAttachedNodes() {
        //Toggle stores whether the highlighting is on
        toggle = 0;

        //Create an array logging what is connected to what
        linkedByIndex = {};
        graph.links.forEach(function(d) {
            linkedByIndex[d.source.index + "," + d.target.index] = 1;
        });
    }

    //This function looks up whether a pair are neighbours
    function neighboring(node_a, node_b) {
        return linkedByIndex[node_a.index + "," + node_b.index] || linkedByIndex[node_b.index + "," + node_a.index] || node_a.index == node_b.index;
    }

    function connectedNodes() {

        if (toggle == 0) {
            //Reduce the opacity of all but the neighbouring nodes
            d = d3.select(this).node().__data__;
            node.style("opacity", function(o) {
                return neighboring(d, o) | neighboring(o, d) ? 1 : 0.1; //
            });

            link.style("opacity", function(o) {
                return d.index == o.source.index | d.index == o.target.index ? 1 : 0.0;
            });

            //Reduce the op

            toggle = 1;
        } else {
            //Put them back to opacity=1
            node.style("opacity", 1);
            link.style("opacity", 1);
            toggle = 0;
        }

    }
}

/**
 * tag network
 * @param {*} json 
 * @param {*} id 
 */

function drawTagNetwork(json, id, width, height) {
    var tip;
    var graph;
    var graphRec;
    var link;
    var node;
    var svg;

    //Create an array logging what is connected to what
    var linkedByIndex;

    //Set up the colour scale<
    // var color = d3.scale.category20();

    var force;

    // map node indexes to nodes
    json['links'].forEach(function(link) {
        json['nodes'].forEach(function(node) {
            if (node.tag == link.sourceName) {
                link.source = json['nodes'].indexOf(node);
            }
            if (node.tag == link.targetName) {
                link.target = json['nodes'].indexOf(node);
            }
        })
    });


    graph = json;

    var link_weight_max = d3.max(graph.links, function(link) {
        return +link.weight;
    });
    var link_weight_min = d3.min(graph.links, function(link) {
        return +link.weight;
    });

    var link_width_weight_scaled = d3.scale.log()
        .domain([link_weight_min, link_weight_max])
        .range([1, 4]);

    var node_weight_max = d3.max(graph.nodes, function(node) {
        return +node.weight;
    });
    var node_weight_min = d3.min(graph.nodes, function(node) {
        return +node.weight;
    });

    var node_size_weight_scaled = d3.scale.linear()
        .domain([node_weight_min, node_weight_max])
        .range([10, 30]);


    //Constants for the SVG
    var width = width || 700,
        height = height || 530;

    //Set up the force layout
    force = d3.layout.force()
        .charge(function(node) {
            return -500;
        })
        .linkDistance(function(link) {
            return 200;
        })
        .size([width, height]);

    //Append a SVG to the body of the html page. Assign this SVG as an object to svg
    svg = d3.select(id).append("svg")
        .attr("width", width)
        .attr("height", height);

    tip = d3.tip()
        .attr('class', 'd3-tip')
        .offset([-10, 0])
        .html(function(d) {
            return "<b>" + d.tag + "</b>";
        });
    svg.call(tip);

    //Creates the graph data structure out of the json data
    force.nodes(graph.nodes)
        .links(graph.links)
        .start();


    //Create all the line svgs but without locations yet
    link = svg.selectAll(".link")
        .data(graph.links)
        .enter().append("line")
        .attr("class", "link")
        .style("stroke-width", function(d) {
            return link_width_weight_scaled(d.weight);
        });

    var drag = force.drag()
        .on("dragstart", dragstart);

    //Do the same with the circles for the nodes - no
    node = svg.selectAll(".node")
        .data(graph.nodes)
        .enter().append("circle")
        .attr("class", "node")
        .attr("r", function(d) {
            return node_size_weight_scaled(d.weight);
        })
        .style("fill", function(d) {
            return color(d.group);
        })
        .on('mouseover', tip.show)
        .on('mouseout', tip.hide)
        .on('dblclick', connectedNodes)
        .call(drag); //Added code;

    function dragstart(d) {
        d3.select(this).classed("fixed", d.fixed = true);
    }

    //Now we are giving the SVGs co-ordinates - the force layout is generating the co-ordinates which this code is using to update the attributes of the SVG elements
    force.on("tick", function() {
        link.attr("x1", function(d) {
                return d.source.x;
            })
            .attr("y1", function(d) {
                return d.source.y;
            })
            .attr("x2", function(d) {
                return d.target.x;
            })
            .attr("y2", function(d) {
                return d.target.y;
            });
        svg.selectAll("circle").attr("cx", function(d) {
                return d.x;
            })
            .attr("cy", function(d) {
                return d.y;
            });
        svg.selectAll("text").attr("x", function(d) {
                return d.x;
            })
            .attr("y", function(d) {
                return d.y;
            });
    });
    directAttachedNodes();



    // Calculates directly attached Nodes and their links
    function directAttachedNodes() {
        //Toggle stores whether the highlighting is on
        toggle = 0;

        //Create an array logging what is connected to what
        linkedByIndex = {};
        graph.links.forEach(function(d) {
            linkedByIndex[d.source.index + "," + d.target.index] = 1;
        });
    }

    //This function looks up whether a pair are neighbours
    function neighboring(node_a, node_b) {
        return linkedByIndex[node_a.index + "," + node_b.index] || linkedByIndex[node_b.index + "," + node_a.index] || node_a.index == node_b.index;
    }

    function connectedNodes() {

        if (toggle == 0) {
            //Reduce the opacity of all but the neighbouring nodes
            d = d3.select(this).node().__data__;
            node.style("opacity", function(o) {
                return neighboring(d, o) | neighboring(o, d) ? 1 : 0.1; //
            });

            link.style("opacity", function(o) {
                return d.index == o.source.index | d.index == o.target.index ? 1 : 0.0;
            });

            //Reduce the op

            toggle = 1;
        } else {
            //Put them back to opacity=1
            node.style("opacity", 1);
            link.style("opacity", 1);
            toggle = 0;
        }

    }

}
}