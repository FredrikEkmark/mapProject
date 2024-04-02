"use strict";

function getMap(mapData, startCoordinateX, startCoordinateY) {
    let map = JSON.parse(mapData); // Convert JSON string to JavaScript object// Convert JSON string to JavaScript object
    centerPoint = { x: startCoordinateX, y: startCoordinateY };
    worldMap = map;
    console.log(worldMap)
    console.log(centerPoint)
    writeMap()
}

function writeMap() {

    document.getElementById(`container`).innerHTML = "";

    let even;
    let odd;

    if (centerPoint.y % 2 === 0) {
        even = 1;
        odd = 0;
    } else {
        even = 0;
        odd = 1;
    }

    for (let y = centerPoint.y - 6; y < centerPoint.y + 7; y++) {
        console.log(y)
        let evenOdd = 0;
        if (y % 2 === 0) {
            evenOdd = even;
        } else {
            evenOdd = odd;
        }
        let lineEdge = "";
        if (y === centerPoint.y - 6) {
            lineEdge = " topEdge";
        } else if (y === centerPoint.y + 6) {
            lineEdge = " bottomEdge";
        }

        for (let x = centerPoint.x - 7; x < centerPoint.x + 8; x++) {
            let edge = "";
            if (lineEdge !== "") {
                edge = lineEdge;
                if (x + 1 - evenOdd === centerPoint.x - 8) {
                    if (lineEdge === " topEdge") {
                        edge = " leftTopEdge";
                    } else if (lineEdge === " bottomEdge") {
                        edge = " leftBottomEdge";
                    }
                } else if (x === centerPoint.x + 7) {
                    if (lineEdge === " topEdge") {
                        edge = " rightTopEdge";
                    } else if (lineEdge === " bottomEdge") {
                        edge = " rightBottomEdge";
                    }
                }

            } else if (x + 1 - evenOdd === centerPoint.x - 8) {
                edge = " leftEdge";
            } else if (x === centerPoint.x + 7) {
                edge = " rightEdge";
            }

            if (worldMap[y][x] !== null && worldMap[y][x] !== undefined) {

                console.log(worldMap[y][x])
                let xIsCorrect = worldMap[y][x].coordinates.x == x
                let yIsCorrect = worldMap[y][x].coordinates.y == y


                console.log(worldMap[y][x].coordinates.x + "." + worldMap[y][x].coordinates.y)
                console.log(xIsCorrect)
                console.log(yIsCorrect)

                let coordinates = (worldMap[y][x].coordinates.x) + "." + worldMap[y][x].coordinates.y;

                let tileType = interperateTile(worldMap[y][x].tileStyleClass);

                document.getElementById(`container`).innerHTML += `
    <div id="${coordinates}" class="${tileType}${edge}" onmouseover="getCoordinate(this.id)"></div>`;
            } else {
                console.log("null tile")
            }
        }
    }
}

function interperateTile(value) {
    switch (value) {
        case 111: case 112: case 113: case 211: case 212: case 213: case 311: case 312: case 313: return "glacier";  // glacier
        case 123: case 133: case 143: case 153: return "deepWater"; // deepWater
        case 223: case 233: case 243: case 253: return "coastalWater";  // coastalWater
        case 322: case 323: return "lowlandTundra"; // lowlandTundra
        case 422: case 423: return "highlandsTundra"; // highlandsTundra
        case 321: return "coldDesert"; // coldDesert
        case 421: return "coldDesertHills"; // coldDesertHills
        case 331: return "temperateLowlandsPlains";  // temperateLowlandsPlains
        case 332: return "temperateForest";  // temperateForest
        case 431: return "highlandHills";  // highlandHills
        case 432: return "temperateHighlandForest";  // temperateHighlandForest
        case 333: return "temperateRainforest";  // temperateRainforest
        case 433: return "temperateHighlandRainforest";  // temperateHighlandRainforest
        case 341: return "hotDesert"; // hotDesert
        case 441: return "hotDesertHills"; // hotDesertHills 0x9c2e17
        case 342: return "hotSteppe"; // hotSteppe 0x459410
        case 442: return "hotSteppeHills"; // hotSteppeHills 0x69914e
        case 351: case 352: case 343: return "tropicalSavanna"; // tropicalSavanna 0x739410
        case 451: case 452: case 443: return "tropicalSavannaHills"; // tropicalSavannaHills 0x7e8f4c
        case 353: return "tropicalRainforest"; // tropicalRainforest 0x3f5209
        case 453: return "tropicalRainforestHills"; // tropicalRainforestHills 0x464f2b
        case 411: case 412: case 413: return "glacialHeights"; // glacialHeights 0x9dc6fc
        case 511: case 521: case 531: return "frozenMountains"; // frozenMountains 0xebf0f7
        case 541: case 551: return "mountains";  // mountains 0x260f02
    }
}

function getCoordinate(id) {
    document.getElementById("coordinates").innerHTML = id ;
}

function mapUp(centerPoint) {
    centerPoint.y -= 2;
    writeMap();
}

function mapDown(centerPoint) {
    centerPoint.y += 2;
    writeMap();
}

function mapLeft(centerPoint) {
    centerPoint.x -= 2;
    writeMap();
}

function mapRight(centerPoint) {
    centerPoint.x += 2;
    writeMap(centerPoint);
}

let centerPoint;

let worldMap;

console.log("works")