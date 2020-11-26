package ai.chinesechess.models.DTO;

import java.util.HashMap;

public class LegalMovementDTO {
    public HashMap<String, String> getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(HashMap<String, String> chessPosition) {
        this.chessBoard = chessPosition;
    }

//    public String getChessName() {
//        return chessName;
//    }
//
//    public void setChessName(String chessName) {
//        this.chessName = chessName;
//    }

    private HashMap<String, String> chessBoard;
//    private String chessName;
//
//    public void setChessPosition(String chessPosition) {
//        this.chessPosition = chessPosition;
//    }
//
//    public String getChessPosition() {
//        return chessPosition;
//    }
//
//    private String chessPosition;
}
