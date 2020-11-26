$(document).ready(function () {
    init();
});

let board;
let possibleMoves;
const BOARD_HEIGHT = 10;
const BOARD_WIDTH = 9;
let numericPositionMapper;
let alphabetPositionMapper;
let config = {
    showNotation: true,
    position: 'start',

    snapbackSpeed: 500,

    orientation: 'black',
    draggable: true,
    onMouseoutSquare: onMouseoutSquare,
    onDragStart: onDragStart,
    onDrop: onDrop,
    onMouseoverSquare: onMouseoverSquare,
};
function onMouseoverSquare (square, piece) {

    // get list of possible moves for this square
    if(piece != false){
        greySquare(square);
        for(i = 0; i < possibleMoves.length; i++){
            if(possibleMoves[i] != null){
                let fromRow = possibleMoves[i].fromRow;
                let fromCol = possibleMoves[i].fromCol;
                let destRow = possibleMoves[i].destRow;
                let destCol = possibleMoves[i].destCol;
                if(alphabetPositionMapper[fromRow.toString() + fromCol.toString()] != square){
                    continue;
                }
                let possiblePosition = alphabetPositionMapper[destRow.toString() + destCol.toString()];
                greySquare(possiblePosition);
            }
        }
        // var settings = {
        //     "url": "http://localhost:6030/legalMovements",
        //     "method": "POST",
        //     "headers": {
        //         "Content-Type": "application/json"
        //     },
        //     "data": JSON.stringify({
        //         chessPosition: board.position(),
        //         chessName: piece,
        //         chessPosition: numericPositionMapper[square]
        //     })
        // };

        // $.ajax(settings).done(function(data){
        //     if(data!= null)
        //
        //     for (let i = 0; i < data.length; i++) {
        //         let fromRow = data[i].fromRow;
        //         let fromCol = data[i].fromCol;
        //         let destRow = data[i].destRow;
        //         let destCol = data[i].destCol;
        //         console.log(data[i]);
        //         console.log(alphabetPositionMapper[fromRow.toString() + fromCol.toString()])
        //         console.log(square);
        //         if(alphabetPositionMapper[fromRow.toString() + fromCol.toString()] != square){
        //             return;
        //         }
        //         let possiblePosition = alphabetPositionMapper[destRow.toString() + destCol.toString()];
        //         console.log(possiblePosition)
        //         greySquare(possiblePosition);
        //     }
        // })
    }else{
        return;
    }


    // highlight the square they moused over
    greySquare(square);

    // highlight the possible squares for this piece

}
function greySquare (square) {
    let $square = $('#myBoard .square-' + square);

    $square.addClass('highlight');
}
function onMouseoutSquare (square, piece) {
    removeGreySquares();
}
function removeGreySquares () {
    $('#myBoard .square-2b8ce').removeClass('highlight');
}
function isLegalMove(source, target){
    for(i = 0; i < possibleMoves.length; i++){
        if(possibleMoves[i] != null){
            let fromRow = possibleMoves[i].fromRow;
            let fromCol = possibleMoves[i].fromCol;
            let destRow = possibleMoves[i].destRow;
            let destCol = possibleMoves[i].destCol;
            if(alphabetPositionMapper[fromRow.toString() + fromCol.toString()] == source){
                if(alphabetPositionMapper[destRow.toString() + destCol.toString()] == target)   {
                    return true;
                }
            }
        }
    }
    return false;
}
function onDrop(source, target, piece, newPos, oldPos, orientation){
    removeGreySquares();
    if(source == target){
        return 'snapback';
    }

    if(!isLegalMove(source, target))
        return "snapback";

    if(board.position()[target] == 'rK'){
        alert("GAME OVER YOU WIN");
        return;
    }

    // update chess board
    board.position(newPos)

    var settings = {
        "url": "http://localhost:6030/movement?position=",
        "method": "POST",
        "async": "false",
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify(board.position()),
    };

    $.ajax(settings).done(function (data) {
        let fromRow = data.fromRow;
        let fromCol = data.fromCol;
        let destRow = data.destRow;
        let destCol = data.destCol;
        if(data.gameEnd == true){
            alert("GAME OVER");
        }
        console.log(data);

        let fromPosition = alphabetPositionMapper[fromRow.toString() + fromCol.toString()];
        let destPosition = alphabetPositionMapper[destRow.toString() + destCol.toString()];


        board.move(fromPosition +"-" +destPosition);
        updateChessPossibleMoves();
    })


}
function updateChessPossibleMoves(){
    console.log("function is called");
    console.log(board.position());
    var settings = {
        "url": "http://localhost:6030/legalMovements",
        "method": "POST",
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            chessBoard: board.position()
        })
    };

    $.ajax(settings).done(function(data){
        possibleMoves = data;
        console.log(possibleMoves);
    })
}
function init() {
    // const board = Xiangqiboard('myBoard', 'start');
    alphabetPositionMapper = initAlphabetPositionMapper();
    numericPositionMapper = initAlphabetPositionMapper()
    board = Xiangqiboard('myBoard', config);
    updateChessPossibleMoves();
}

// function mapChessBoardPosition(){
//     let chessBoard = {};
//     for(i = 0; i < BOARD_HEIGHT; i++){
//         for(j = 0; j < BOARD_WIDTH; j++){
//             chessBoard[i.toString() + j.toString()] = 0;
//         }
//     }
//
//     let position = board.position();
//     console.log(position);
//     for(let property in position){
//         if(position[property].charAt(0) == 'r'){
//             if(position[property].charAt(1) == 'R'){
//                 let numericPosition = numericPositionMapper[property];
//                 chessBoard[numericPosition] =
//             }
//         }
//     }
// }
function onDragStart (source, piece, position, orientation) {
    // do not pick up pieces if the game is over


    // only pick up pieces for the side to move
    if (piece.search(/^r/) !== -1) {
        return false;
    }
}
function initAlphabetPositionMapper(){
    let mapper = {};
    for(i = 0; i < 10; i++){
        for(j = 0; j < 9; j++){
            mapper[i.toString()+j.toString()] =String.fromCharCode((j+105) - (2 * j)) + i.toString();
        }
    }
    return mapper;
}

function initNumericPositionMapper(){
    let mapper = {};
    for(i = 0; i < 10; i++){
        for(j = 0; j < 9; j++){
            mapper[String.fromCharCode((j+105) - (2 * j)) + i.toString()] =i.toString()+j.toString();
        }
    }
    return mapper;
}


