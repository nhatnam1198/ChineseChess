$(document).ready(function () {
    init();
    $('.loader').hide();
    $('#chieuTuong').hide();
});
$('#btnReload').on('click', function () {
    var flag = confirm('Bạn có muốn chơi lại không');
    if (flag === true) {
        $('#chieuTuong').hide();
        $('.loader').hide();
        init();
    }
});

let DOMAIN = 'https://chinese-chess-ai.herokuapp.com/'
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
    if(source === target){
        return 'snapback';
    }
    if(board.position()[target] == 'rK'){
        const r = confirm("GAME OVER YOU WIN");
        if (r === true){
            init();
            $('.loader').hide();
            $('#chieuTuong').hide();
        }
        return;
    }
    var isFetching = true;
    console.log(isFetching);
    $('.loader').hide();
    $('.loader').show();
    removeGreySquares();


    if(!isLegalMove(source, target))
        return "snapback";



    // update chess board
    board.position(newPos)

    var settings = {
        "url": DOMAIN + "movement?position=",
        "method": "POST",
        "async": "false",
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify(board.position()),
    };

    $.ajax(settings).done(function (data) {
        isFetching = false;
        console.log(isFetching);
        $('.loader').hide();
        let fromRow = data.fromRow;
        let fromCol = data.fromCol;
        let destRow = data.destRow;
        let destCol = data.destCol;

        console.log(data);

        let fromPosition = alphabetPositionMapper[fromRow.toString() + fromCol.toString()];
        let destPosition = alphabetPositionMapper[destRow.toString() + destCol.toString()];


        board.move(fromPosition +"-" +destPosition);
        if(data.gameEnd == true){
           const r = confirm('GAME OVER. YOU LOST');
            if (r === true) {
                $('#chieuTuong').hide();
                $('.loader').hide();
                init();
            }
        }

        checkmate();
        updateChessPossibleMoves();
    })

}
function updateChessPossibleMoves(){
    console.log("function is called");
    console.log(board.position());
    var settings = {
        "url": DOMAIN + "legalMovements",
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

function checkmate() {
    var settings = {
        "url": DOMAIN + "checkmate?position=",
        "method": "POST",
        "async": "false",
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify(board.position()),
    };

    $.ajax(settings).done(function (data) {

        if (data.gameEnd == true) {
            $('#chieuTuong').show();
        }
        else {
            $('#chieuTuong').hide();
        }
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


