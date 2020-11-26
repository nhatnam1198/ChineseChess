package ai.chinesechess.controllers;

import ai.chinesechess.BUS.ChessBoardBUS;
import ai.chinesechess.models.DTO.LegalMovementDTO;
import ai.chinesechess.models.Movement;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ChessBoardController {
    private final int EMPTY = 0;
    @PostMapping("/movement")
    public Movement GetMovement(@RequestBody HashMap<String, String> position){
        double[][] realChessBoard = new double[10][9];
        ChessBoardBUS chessBoardBUS = new ChessBoardBUS();
        for(String key : position.keySet()){
            HashMap<String, String> numericMapper = getNumericMapper();
            String numericPositionString = numericMapper.get(key);
            double value = 0;
            if(position.get(key).charAt(0) == 'r'){
                value = chessBoardBUS.getChessValueBySymbol(position.get(key).charAt(1)) + 0.01;
            }else{
                value = chessBoardBUS.getChessValueBySymbol(position.get(key).charAt(1)) + 0.02;
            }
            realChessBoard[Integer.parseInt(String.valueOf(numericPositionString.charAt(0)))][Integer.parseInt(String.valueOf(numericPositionString.charAt(1)))] = value;
        }

        chessBoardBUS.minimax(2, realChessBoard, 0, true, -Integer.MAX_VALUE, Integer.MAX_VALUE, true);
        System.out.println("============================================================================= mmax");
        Movement finalMovement = chessBoardBUS.getFinalMovement();
//        int finalFromRow = chessBoardBUS.getFinalMovement().getFromRow();
//        int finalFromCol = chessBoardBUS.getFinalMovement().getFromCol();
//        int finalDestRow = chessBoardBUS.getFinalMovement().getDestRow();
//        int finalDestCol = chessBoardBUS.getFinalMovement().getDestCol();
//        System.out.println("(" + finalFromRow + ", " + finalFromCol + ") ==>" + "(" + finalDestRow +", " + finalDestCol+")");
//        realChessBoard[finalDestRow][finalDestCol] = realChessBoard[finalFromRow][finalFromCol];
//        realChessBoard[finalFromRow][finalFromCol] = EMPTY;

        return finalMovement;
    }

    private HashMap<String, String> getNumericMapper(){
        HashMap<String, String> mapper = new HashMap<String, String>();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 9; j++){
                mapper.put((char)((j+105) - (2 * j)) + Integer.toString(i), Integer.toString(i) + Integer.toString(j));
            }
        }
        return mapper;

    }

    @PostMapping("/legalMovements")
    public Movement[] GetLegalMovement(@RequestBody LegalMovementDTO legalMovementDTO){
        HashMap<String, String> chessBoard = legalMovementDTO.getChessBoard();
//        String chessName = legalMovementDTO.getChessName();
//        String chessPosition = legalMovementDTO.getChessPosition();
        double[][] realChessBoard = new double[10][9];
        ChessBoardBUS chessBoardBUS = new ChessBoardBUS();
        for(String key : chessBoard.keySet()){
            HashMap<String, String> numericMapper = getNumericMapper();
            String numericPositionString = numericMapper.get(key);
            double value = 0;
            if(chessBoard.get(key).charAt(0) == 'r'){
                value = chessBoardBUS.getChessValueBySymbol(chessBoard.get(key).charAt(1)) + 0.01;
            }else{
                value = chessBoardBUS.getChessValueBySymbol(chessBoard.get(key).charAt(1)) + 0.02;
            }
            realChessBoard[Integer.parseInt(String.valueOf(numericPositionString.charAt(0)))][Integer.parseInt(String.valueOf(numericPositionString.charAt(1)))] = value;
        }
        int[] numberOfBranch = new int[50];
        Movement[] movements = new Movement[100];
        ArrayList<Movement> legalMoves = new ArrayList<Movement>();
        chessBoardBUS.generateMovement(realChessBoard, movements, 0, numberOfBranch, false);

        return movements;
    }
}
