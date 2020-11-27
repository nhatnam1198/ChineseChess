package ai.chinesechess.BUS;

import ai.chinesechess.models.Movement;

public class ChessBoardBUS {
    private final int BOARD_HEIGHT = 10;
    private final int BOARD_WIDTH = 9;
    private int EMPTY = 0;

    public Movement getFinalMovement() {
        return finalMovement;
    }

    public void setFinalMovement(Movement finalMovement) {
        this.finalMovement = finalMovement;
    }

    public Movement finalMovement = new Movement();

    public double[][] getChess() {
        return chess;
    }

    public void setChess(double[][] chess) {
        this.chess = chess;
    }

    public double[][] chess = new double[BOARD_HEIGHT][BOARD_WIDTH];
    private int[][] chessColor = new int[BOARD_HEIGHT][BOARD_WIDTH];

    public double[][] getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(double[][] chessBoard) {
        this.chessBoard = chessBoard;
    }

    private double[][] chessBoard = new double[BOARD_HEIGHT][BOARD_WIDTH];
    public boolean IS_GAME_END = false;
    private static final int CHARIOT = 90; // xe
    private static final int CANON = 50; // pháo
    private static final int GENERAL = 999999; // tướng
    private static final int SOLDIER_BEFORE = 10; // tốt trước khi sang sông
    private static final int SOLDIER_AFTER = 15; // tốt sau khi sang sông
    private static final int ELEPHANT = 25; // tượng
    private static final int ADVISOR = 20; // sĩ
    private static final int HORSE = 40; // mã
    private static final int SOLIDER_AFTER = 15;
    private static final double SOLDIER_BEFORE_CROSSING_RIVER_WHITE= 10.01;
    private static final double SOLDIER_BEFORE_CROSSING_RIVER_BLACK = 10.02;
    private static final double	SOLIDER_AFTER_CROSSING_RIVER_WHITE = 15.01;
    private static final double SOLIDER_AFTER_CROSSING_RIVER_BLACK = 15.02;
    private int DARK = 1;
    private int WHITE = 0;
    private void tryMakeMove(double[][] chessBoard, Movement movement) {
        int fromRow = movement.getFromRow();
        int fromCol = movement.getFromCol();
        int destRow = movement.getDestRow();
        int destCol = movement.getDestCol();
        chessBoard[destRow][destCol] = chessBoard[fromRow][fromCol];
        chessBoard[fromRow][fromCol] = EMPTY;
    }

    private void backTrack(double[][] chessBoard, Movement movement) {
    }

    private boolean isWhiteSide(double piece) {
        return standardizedValue(piece) == 0.01;
    }

    private double standardizedValue(double value) {
        // 0.23000000000333 to 0.23
        return (int) (Math.round((value - Math.floor(value)) * 100)) / 100.0;
    }
    public double getChessValueBySymbol(char symbol,int row, boolean isWhite){
        double additionalValue = 0;
        if(isWhite == true){
            additionalValue = 0.01;
        }else{
            additionalValue = 0.02;
        }
        if(symbol == 'R'){
            return CHARIOT + additionalValue;
        }
        if(symbol == 'N'){
            return HORSE + additionalValue;
        }
        if(symbol == 'B'){
            return ELEPHANT + additionalValue;
        }
        if(symbol == 'A'){
            return ADVISOR + additionalValue;
        }
        if(symbol == 'K'){
            return GENERAL + additionalValue;
        }
        if(symbol == 'C'){
            return CANON + additionalValue;
        }
        if(symbol == 'P'){
            if(isWhite == true && row >= 5){
                return SOLIDER_AFTER + additionalValue;
            }
            if(isWhite == false && row <= 4){
                return SOLIDER_AFTER + additionalValue;
            }
            return SOLDIER_BEFORE + additionalValue;
        }
        return 0;
    }
    private boolean hasEnemy(double[][] chessBoard, int i, int j, boolean isWhite) {
        if (isWhite) {
            if (standardizedValue(chessBoard[i][j]) == 0.02) {
                return true;
            }
        } else {
            if (standardizedValue(chessBoard[i][j]) == 0.01) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPositionOfGeneral(double[][] chessBoard, int i, int j, boolean isWhite) {
        if (!isWhite) {
            if (i < BOARD_HEIGHT && i >= BOARD_HEIGHT - 3 && j >= BOARD_WIDTH / 2 - 1 && j <= BOARD_WIDTH / 2 + 1) {
                return true;
            }
        } else {
            if ((i <= 2 && i >= 0) && (j >= BOARD_WIDTH / 2 - 1 && j <= BOARD_WIDTH / 2 + 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPositionOfAdvisor(double[][] chessBoard, int i, int j, boolean isWhite) {
        if (!isWhite) {
            if ((i == 9 && (j == 3 || j == 5)) || (i == 8 && j == 4) || (i == 7 && (j == 3 || j == 5))) {
                return true;
            }
        } else {
            if ((i == 0 && (j == 3 || j == 5)) || (i == 1 && j == 4) || (i == 2 && (j == 3 || j == 5))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPositionOfElephant(double[][] chessBoard, int i, int j, boolean isWhite) {
        if (!isWhite) {
            if ((i == 9 && (j == 2 || j == 6)) || (i == 7 && (j == 4 || j == 0 || j == 8))
                    || (i == 5 && (j == 2 || j == 6))) {
                return true;
            }
        } else {
            if ((i == 0 && (j == 2 || j == 6)) || (i == 2 && (j == 4 || j == 0 || j == 8))
                    || (i == 4 && (j == 2 || j == 6))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPositionOfHorse(double[][] chessBoard, int fromRow, int fromCol, int destRow, int destCol) {
        if ((destRow == fromRow + 1 || destRow == fromRow - 1) && destCol == fromCol + 2
                && chessBoard[fromRow][fromCol + 1] == EMPTY) {
            return true;
        } else if ((destRow == fromRow + 1 || destRow == fromRow - 1) && destCol == fromCol - 2
                && chessBoard[fromRow][fromCol - 1] == EMPTY) {
            return true;
        } else if (destRow == fromRow + 2 && (destCol == fromCol + 1 || destCol == fromCol - 1)
                && chessBoard[fromRow + 1][fromCol] == EMPTY) {
            return true;
        } else if (destRow == fromRow - 2 && (destCol == fromCol + 1 || destCol == fromCol - 1)
                && chessBoard[fromRow - 1][fromCol] == EMPTY) {
            return true;
        }
        return false;
    }
    private boolean faceToFace(int i, int j, double[][] chessBoard) {
        if (i < BOARD_HEIGHT && i >= BOARD_HEIGHT - 3) {
            i--;
            while (i >= 0 && chessBoard[i][j] != GENERAL) {
                if (chessBoard[i][j] != EMPTY) {
                    return false;
                }
                i--;
            }
            if (i < 0) {
                return false;
            }
        } else if (i <= 2 && i >= 0) {
            i++;
            while (i < BOARD_HEIGHT && chessBoard[i][j] != GENERAL) {
                if (chessBoard[i][j] != EMPTY) {
                    return false;
                }
                i++;
            }
            if (i == BOARD_HEIGHT) {
                return false;
            }
        }
        return true;
    }

    public void generateMovement(double[][] chessBoard, Movement[] movement, int level, int[] numberOfBranch, boolean isWhite) {
        numberOfBranch[level] = 0;
        // for each piece in chess board
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                double chess = chessBoard[i][j];

                if (chessBoard[i][j] != EMPTY) {
                    if (isWhite) {
                        if (isWhiteSide(chess)) {
                            switch ((int) Math.floor(chess)) {
                                case CHARIOT:
                                    genChariotMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case CANON:
                                    genCanonMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case GENERAL:
                                    genGeneralMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case SOLDIER_BEFORE:
                                    genSoldierBeforeMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case SOLIDER_AFTER:
                                    genSoldierAfterMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case ADVISOR:
                                    genAdvisorMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case ELEPHANT:
                                    genElephantMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case HORSE:
                                    genHorseMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                            }

                        }
                    } else {
                        if (!isWhiteSide(chess)) {
                            switch ((int) Math.floor(chess)) {
                                case CHARIOT:
                                    genChariotMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case CANON:
                                    genCanonMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case GENERAL:
                                    genGeneralMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case SOLDIER_BEFORE:
                                    genSoldierBeforeMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case SOLIDER_AFTER:
                                    genSoldierAfterMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case ADVISOR:
                                    genAdvisorMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case ELEPHANT:
                                    genElephantMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                                case HORSE:
                                    genHorseMovement(chessBoard, i, j, numberOfBranch, movement, level, isWhite);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void genCanonMovement(double[][] chessBoard, int i, int j, int[] numberOfBranch, Movement[] movement, int level, boolean isWhite) {
        int chessRow = i;
        int chessCol = j;
        // To bottom
        // tìm ngòi
        for (int k = i + 1; k < BOARD_HEIGHT - 1; k++) {
            if (chessBoard[k][j] != EMPTY) {
                for (int k2 = k + 1; k2 < BOARD_HEIGHT; k2++) {
                    if (hasEnemy(chessBoard, k2, j, isWhite)) {
                        Movement move = new Movement();
                        move.setFrom(i, j);
                        move.setDest(k2, j);
                        movement[numberOfBranch[level]] = move;
                        numberOfBranch[level]++;
                        break;
                    }
                }
                break;
            }
        }
        while (chessRow < BOARD_HEIGHT) {
            chessRow++;
            if (chessRow == BOARD_HEIGHT) {
                chessRow = i;
                break;
            }
            if (chessBoard[chessRow][j] == EMPTY) {
                Movement move = new Movement();
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                break;
            }
        }
        // To top
        // tìm ngòi
        for (int k = i - 1; k > 0; k--) {
            if (chessBoard[k][j] != EMPTY) {
                for (int k2 = k - 1; k2 >= 0; k2--) {
                    if (hasEnemy(chessBoard, k2, j, isWhite)) {
                        Movement move = new Movement();
                        move.setFrom(i, j);
                        move.setDest(k2, j);
                        movement[numberOfBranch[level]] = move;
                        numberOfBranch[level]++;
                        break;
                    }
                }
                break;
            }
        }
        while (chessRow >= 0) {
            chessRow--;
            if (chessRow < 0) {
                chessRow = i;
                break;
            }
            if (chessBoard[chessRow][j] == EMPTY) {
                Movement move = new Movement();
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                break;
            }
        }
        // To right
        // tìm ngòi
        for (int k = j + 1; k < BOARD_WIDTH - 1; k++) {
            if (chessBoard[i][k] != EMPTY) {
                for (int k2 = k + 1; k2 < BOARD_WIDTH; k2++) {
                    if (hasEnemy(chessBoard, i, k2, isWhite)) {
                        Movement move = new Movement();
                        move.setFrom(i, j);
                        move.setDest(i, k2);
                        movement[numberOfBranch[level]] = move;
                        numberOfBranch[level]++;
                        break;
                    }
                }
                break;
            }
        }
        while (chessCol < BOARD_WIDTH) {
            chessCol++;
            if (chessCol == BOARD_WIDTH) {
                chessCol = j;
                break;
            }
            if (chessBoard[i][chessCol] == EMPTY) {
                Movement move = new Movement();
                move.setFrom(i, j);
                move.setDest(i, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessCol = j;
                break;
            }
        }
        // To left
        // tìm ngòi
        for (int k = j - 1; k > 0; k--) {
            if (chessBoard[i][k] != EMPTY) {
                for (int k2 = k - 1; k2 >= 0; k2--) {
                    if (hasEnemy(chessBoard, i, k2, isWhite)) {
                        Movement move = new Movement();
                        move.setFrom(i, j);
                        move.setDest(i, k2);
                        movement[numberOfBranch[level]] = move;
                        numberOfBranch[level]++;
                        break;
                    }
                }
                break;
            }
        }
        while (chessCol >= 0) {
            chessCol--;
            if (chessCol < 0) {
                chessCol = j;
                break;
            }
            if (chessBoard[i][chessCol] == EMPTY) {
                Movement move = new Movement();
                move.setFrom(i, j);
                move.setDest(i, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessCol = j;
                break;
            }
        }
    }

    private void genHorseMovement(double[][] chessBoard, int i, int j, int[] numberOfBranch, Movement[] movement, int level, boolean isWhite) {
        int chessRow = i;
        int chessCol = j;
        // To bottom left
        chessRow++;
        chessCol -= 2;
        if (chessRow < BOARD_HEIGHT && chessCol >= 0) {
            if (checkPositionOfHorse(chessBoard, i, j, chessRow, chessCol)
                    && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("HORSE");
                move.setFrom(i, j);
                move.setDest(chessRow, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                chessCol = j;
            }
        } else {
            chessRow = i;
            chessCol = j;
        }

        chessRow = i;
        chessCol = j;
        chessRow += 2;
        chessCol--;
        if (chessRow < BOARD_HEIGHT && chessCol >= 0) {
            if (checkPositionOfHorse(chessBoard, i, j, chessRow, chessCol)
                    && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("HORSE");
                move.setFrom(i, j);
                move.setDest(chessRow, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                chessCol = j;
            }
        } else {
            chessRow = i;
            chessCol = j;
        }

        // To bottom right
        chessRow = i;
        chessCol = j;
        chessRow++;
        chessCol += 2;
        if (chessRow < BOARD_HEIGHT && chessCol < BOARD_WIDTH) {
            if (checkPositionOfHorse(chessBoard,i, j, chessRow, chessCol)
                    && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("HORSE");
                move.setFrom(i, j);
                move.setDest(chessRow, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                chessCol = j;
            }
        } else {
            chessRow = i;
            chessCol = j;
        }

        chessRow = i;
        chessCol = j;
        chessRow += 2;
        chessCol++;
        if (chessRow < BOARD_HEIGHT && chessCol < BOARD_WIDTH) {
            if (checkPositionOfHorse(chessBoard,i, j, chessRow, chessCol)
                    && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("HORSE");
                move.setFrom(i, j);
                move.setDest(chessRow, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                chessCol = j;
            }
        } else {
            chessRow = i;
            chessCol = j;
        }

        // To top left
        chessRow = i;
        chessCol = j;
        chessRow--;
        chessCol -= 2;
        if (chessRow >= 0 && chessCol >= 0) {
            if (checkPositionOfHorse(chessBoard,i, j, chessRow, chessCol)
                    && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("HORSE");
                move.setFrom(i, j);
                move.setDest(chessRow, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                chessCol = j;
            }
        } else {
            chessRow = i;
            chessCol = j;
        }

        chessRow = i;
        chessCol = j;
        chessRow -= 2;
        chessCol--;
        if (chessRow >= 0 && chessCol >= 0) {
            if (checkPositionOfHorse(chessBoard,i, j, chessRow, chessCol)
                    && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("HORSE");
                move.setFrom(i, j);
                move.setDest(chessRow, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                chessCol = j;
            }
        } else {
            chessRow = i;
            chessCol = j;
        }

        // To top right
        chessRow = i;
        chessCol = j;
        chessRow--;
        chessCol += 2;
        if (chessRow >= 0 && chessCol < BOARD_WIDTH) {
            if (checkPositionOfHorse(chessBoard,i, j, chessRow, chessCol)
                    && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("HORSE");
                move.setFrom(i, j);
                move.setDest(chessRow, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                chessCol = j;
            }
        } else {
            chessRow = i;
            chessCol = j;
        }

        chessRow = i;
        chessCol = j;
        chessRow -= 2;
        chessCol++;
        if (chessRow >= 0 && chessCol < BOARD_WIDTH) {
            if (checkPositionOfHorse(chessBoard,i, j, chessRow, chessCol)
                    && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("HORSE");
                move.setFrom(i, j);
                move.setDest(chessRow, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
                chessCol = j;
            }
        } else {
            chessRow = i;
            chessCol = j;
        }
    }

    private void genElephantMovement(double[][] chessBoard, int i, int j, int[] numberOfBranch, Movement[] movement, int level,
                                     boolean isWhite) {
        int chessRow = i;
        int chessCol = j;
        // To bottom left
        if (chessRow < BOARD_HEIGHT && chessCol >= 0) {
            chessRow += 2;
            chessCol -= 2;
            if (chessRow >= BOARD_HEIGHT || chessCol < 0) {
                chessRow = i;
                chessCol = j;
//				break;
            }
            if (chessRow != i && chessCol != j) {
                if (checkPositionOfElephant(chessBoard, chessRow, chessCol, isWhite) && chessBoard[i + 1][j - 1] == EMPTY
                        && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                    Movement move = new Movement();
                    move.setMovementName("ELEPHANT");
                    move.setFrom(i, j);
                    move.setDest(chessRow, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
                    chessCol = j;
//				break;
                }
            }
        }
        // To bottom right
        chessRow = i;
        chessCol = j;
        if (chessRow < BOARD_HEIGHT && chessCol < BOARD_WIDTH) {
            chessRow += 2;
            chessCol += 2;
            if (chessRow >= BOARD_HEIGHT || chessCol >= BOARD_WIDTH) {
                chessRow = i;
                chessCol = j;
//				break;
            }
            if (chessRow != i && chessCol != j) {
                if (checkPositionOfElephant(chessBoard, chessRow, chessCol, isWhite) && chessBoard[i + 1][j + 1] == EMPTY
                        && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                    Movement move = new Movement();
                    move.setMovementName("ELEPHANT");
                    move.setFrom(i, j);
                    move.setDest(chessRow, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
                    chessCol = j;
//				break;
                }
            }
        }
        // To top left
        chessRow = i;
        chessCol = j;
        if (chessRow >= 0 && chessCol >= 0) {
            chessRow -= 2;
            chessCol -= 2;
            if (chessRow < 0 || chessCol < 0) {
                chessRow = i;
                chessCol = j;
//				break;
            }
            if (chessRow != i && chessCol != j) {
                if (checkPositionOfElephant(chessBoard, chessRow, chessCol, isWhite) && chessBoard[i - 1][j - 1] == EMPTY
                        && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                    Movement move = new Movement();
                    move.setMovementName("ELEPHANT");
                    move.setFrom(i, j);
                    move.setDest(chessRow, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
                    chessCol = j;
//				break;
                }
            }
        }
        // To top right
        chessRow = i;
        chessCol = j;
        if (chessRow >= 0 && chessCol < BOARD_WIDTH) {
            chessRow -= 2;
            chessCol += 2;
            if (chessRow < 0 || chessCol >= BOARD_WIDTH) {
                chessRow = i;
                chessCol = j;
//				break;
            }
            if (chessRow != i && chessCol != j) {
                if (checkPositionOfElephant(chessBoard, chessRow, chessCol, isWhite) && chessBoard[i - 1][j + 1] == EMPTY
                        && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                    Movement move = new Movement();
                    move.setMovementName("ELEPHANT");
                    move.setFrom(i, j);
                    move.setDest(chessRow, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
                    chessCol = j;
//				break;
                }
            }
        }
    }

    private void genAdvisorMovement(double[][] chessBoard, int i, int j, int[] numberOfBranch, Movement[] movement, int level,
                                    boolean isWhite) {
        int chessRow = i;
        int chessCol = j;
        // To bottom left
        if (chessRow < BOARD_HEIGHT && chessCol >= 3) {
            chessRow++;
            chessCol--;
            if (chessRow >= BOARD_HEIGHT || chessCol < 3) {
                chessRow = i;
                chessCol = j;
//				break;
            }
            if (chessRow != i && chessCol != j) {
                if (checkPositionOfAdvisor(chessBoard, chessRow, chessCol, isWhite)
                        && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                    Movement move = new Movement();
                    move.setMovementName("ADVISOR");
                    move.setFrom(i, j);
                    move.setDest(chessRow, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
                    chessCol = j;
//				break;
                }
            }
        }
        // To bottom right
        chessRow = i;
        chessCol = j;
        if (chessRow < BOARD_HEIGHT && chessCol <= 5) {
            chessRow++;
            chessCol++;
            if (chessRow >= BOARD_HEIGHT || chessCol > 5) {
                chessRow = i;
                chessCol = j;
//				break;
            }
            if (chessRow != i && chessCol != j) {
                if (checkPositionOfAdvisor(chessBoard, chessRow, chessCol, isWhite)
                        && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                    Movement move = new Movement();
                    move.setMovementName("ADVISOR");
                    move.setFrom(i, j);
                    move.setDest(chessRow, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
                    chessCol = j;
//				break;
                }
            }
        }
        // To top left
        chessRow = i;
        chessCol = j;
        if (chessRow >= 0 && chessCol >= 3) {
            chessRow--;
            chessCol--;
            if (chessRow < 0 || chessCol < 3) {
                chessRow = i;
                chessCol = j;
//				break;
            }
            if (chessRow != i && chessCol != j) {
                if (checkPositionOfAdvisor(chessBoard, chessRow, chessCol, isWhite)
                        && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                    Movement move = new Movement();
                    move.setMovementName("ADVISOR");
                    move.setFrom(i, j);
                    move.setDest(chessRow, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
                    chessCol = j;
//				break;
                }
            }
        }
        // To top right
        chessRow = i;
        chessCol = j;
        if (chessRow >= 0 && chessCol <= 5) {
            chessRow--;
            chessCol++;
            if (chessRow < 0 || chessCol > 5) {
                chessRow = i;
                chessCol = j;
//				break;
            }
            if (chessRow != i && chessCol != j) {
                if (checkPositionOfAdvisor(chessBoard, chessRow, chessCol, isWhite)
                        && (chessBoard[chessRow][chessCol] == EMPTY || hasEnemy(chessBoard, chessRow, chessCol, isWhite))) {
                    Movement move = new Movement();
                    move.setMovementName("ADVISOR");
                    move.setFrom(i, j);
                    move.setDest(chessRow, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
                    chessCol = j;
//				break;
                }
            }
        }
    }

    private void genChariotMovement(double[][] chessBoard, int i, int j, int[] numberOfBranch, Movement[] movement, int level,
                                    boolean isWhite) {
        int chessRow = i;
        int chessCol = j;
        // To bottom
        while (chessRow < BOARD_HEIGHT) {
            chessRow++;
            if (chessRow == BOARD_HEIGHT) {
                chessRow = i;
                break;
            }

            if (chessBoard[chessRow][j] == EMPTY) {
                Movement move = new Movement();
                move.setMovementName("CHARIOT");
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else if (hasEnemy(chessBoard, chessRow, j, isWhite)) {
                Movement move = new Movement();
                move.setMovementName("CHARIOT");
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
                chessRow = i;
                break;
            } else {
                chessRow = i;
                break;
            }
        }
        // To top
        while (chessRow >= 0) {
            chessRow--;
            if (chessRow < 0) {
                chessRow = i;
                break;
            }
            if (chessBoard[chessRow][j] == EMPTY) {
                Movement move = new Movement();
                move.setMovementName("CHARIOT");
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else if (hasEnemy(chessBoard, chessRow, j, isWhite)) {
                Movement move = new Movement();
                move.setMovementName("CHARIOT");
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
                chessRow = i;
                break;
            } else {
                chessRow = i;
                break;
            }
        }
        // To right
        while (chessCol < BOARD_WIDTH) {
            chessCol++;
            if (chessCol == BOARD_WIDTH) {
                chessCol = j;
                break;
            }
            if (chessBoard[i][chessCol] == EMPTY) {
                Movement move = new Movement();
                move.setMovementName("CHARIOT");
                move.setFrom(i, j);
                move.setDest(i, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else if (hasEnemy(chessBoard, i, chessCol, isWhite)) {
                Movement move = new Movement();
                move.setMovementName("CHARIOT");
                move.setFrom(i, j);
                move.setDest(i, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
                chessCol = j;
                break;
            } else {
                chessCol = j;
                break;
            }
        }
        // To left
        while (chessCol >= 0) {
            chessCol--;
            if (chessCol < 0) {
                chessCol = j;
                break;
            }
            if (chessBoard[i][chessCol] == EMPTY) {
                Movement move = new Movement();
                move.setMovementName("CHARIOT");
                move.setFrom(i, j);
                move.setDest(i, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else if (hasEnemy(chessBoard, i, chessCol, isWhite)) {
                Movement move = new Movement();
                move.setMovementName("CHARIOT");
                move.setFrom(i, j);
                move.setDest(i, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
                chessCol = j;
                break;
            } else {
                chessCol = j;
                break;
            }
        }
    }


    private void genGeneralMovement(double[][] chessBoard, int i, int j, int[] numberOfBranch, Movement[] movement,
                                    int level, boolean isWhite) {
        int chessRow = i;
        int chessCol = j;
        // To bottom
//		while (chessRow < BOARD_HEIGHT) {
        chessRow++;
        if (chessRow >= BOARD_HEIGHT || (chessRow < 7 && chessRow >= 3)) {
            chessRow = i;
//				break;
        }
        if (chessRow != i) {
            if (checkPositionOfGeneral(chessBoard, chessRow, j, isWhite)
                    && (chessBoard[chessRow][j] == EMPTY || hasEnemy(chessBoard, chessRow, j, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("GENERAL");
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
//				break;
            }
        }
//		}
        // To top
//		while (chessRow >= 0) {
        chessRow = i;
        chessCol = j;
        chessRow--;
        if (chessRow < 0 || (chessRow < 7 && chessRow >= 3)) {
            chessRow = i;
//				break;
        }
        if (chessRow != i) {
            if (checkPositionOfGeneral(chessBoard, chessRow, j, isWhite)
                    && (chessBoard[chessRow][j] == EMPTY || hasEnemy(chessBoard, chessRow, j, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("GENERAL");
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessRow = i;
//				break;
            }
        }
        // To right
//		while (chessCol < BOARD_WIDTH) {
        chessRow = i;
        chessCol = j;
        chessCol++;
        if (chessCol < 3 || chessCol > 5) {
            chessCol = j;
//				break;
        }
        if (chessCol != j) {
            if (checkPositionOfGeneral(chessBoard, chessRow, chessCol, isWhite)
                    && !faceToFace(chessRow, chessCol, chessBoard)
                    && (chessBoard[i][chessCol] == EMPTY || hasEnemy(chessBoard, i, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("GENERAL");
                move.setFrom(i, j);
                move.setDest(i, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessCol = j;
            }
        }
        // To left
//		while (chessCol >= 0) {
        chessRow = i;
        chessCol = j;
        chessCol--;
        if (chessCol < 3 || chessCol > 5) {
            chessCol = j;
//				break;
        }
        if (chessCol != j) {
            if (checkPositionOfGeneral(chessBoard, chessRow, chessCol, isWhite)
                    && !faceToFace(chessRow, chessCol, chessBoard)
                    && (chessBoard[i][chessCol] == EMPTY || hasEnemy(chessBoard, i, chessCol, isWhite))) {
                Movement move = new Movement();
                move.setMovementName("GENERAL");
                move.setFrom(i, j);
                move.setDest(i, chessCol);
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            } else {
                chessCol = j;
            }
        }
    }

    private void genSoldierBeforeMovement(double[][] chessBoard, int i, int j, int[] numberOfBranch, Movement[] movement, int level,
                                          boolean isWhite) {
        int chessRow = i;
        int chessCol = j;
        // To bottom
        if (isWhite) {
            chessRow++;
            if (chessBoard[chessRow][j] == EMPTY || hasEnemy(chessBoard, chessRow, j, isWhite)) {
                Movement move = new Movement();
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                move.setMovementName("SOLIDER_BEFORE");
                if (chessRow >= BOARD_HEIGHT / 2) {
                    move.setSoliderMovement(true);
                }
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            }
        }
        // To top
        else {
            chessRow--;
            if (chessBoard[chessRow][j] == EMPTY || hasEnemy(chessBoard, chessRow, j, !isWhite)) {
                Movement move = new Movement();
                move.setMovementName("SOLIDER_BEFORE");
                move.setFrom(i, j);
                move.setDest(chessRow, j);
                if (chessRow < BOARD_HEIGHT / 2) {
                    move.setSoliderMovement(true);
                }
                movement[numberOfBranch[level]] = move;
                numberOfBranch[level]++;
            }
        }
        // To right
//		while (chessCol < BOARD_WIDTH) {
//			chessCol++;
//			if (chessCol == BOARD_WIDTH) {
//				chessCol = j;
//				break;
//			}
//			if (chessBoard[i][chessCol] == EMPTY || hasEnemy(chessBoard, i, chessCol, isWhite)) {
//				Movement move = new Movement();
//				move.setFrom(i, j);
//				move.setDest(i, chessCol);
//				movement[numberOfBranch[level]] = move;
//				numberOfBranch[level]++;
//			}
//		}
        // To left
//		while (chessCol >= 0) {
//			chessCol--;
//			if (chessCol < 0) {
//				chessCol = j;
//				break;
//			}
//			if (chessBoard[i][chessCol] == EMPTY || hasEnemy(chessBoard, i, chessCol, isWhite)) {
//				Movement move = new Movement();
//				move.setFrom(i, j);
//				move.setDest(i, chessCol);
//				movement[numberOfBranch[level]] = move;
//				numberOfBranch[level]++;
//			}
//		}
    }

    private void genSoldierAfterMovement(double[][] chessBoard, int i, int j, int[] numberOfBranch, Movement[] movement, int level,
                                         boolean isWhite) {
        int chessRow = i;
        int chessCol = j;
        // To bottom
        if (chessRow < BOARD_HEIGHT && isWhite) {
            chessRow++;
            if (chessRow == BOARD_HEIGHT) {
                chessRow = i;
//				break;
            }
            if (chessRow != i) {
                if (chessBoard[chessRow][j] == EMPTY || hasEnemy(chessBoard, chessRow, j, isWhite)) {
                    Movement move = new Movement();
                    move.setFrom(i, j);
                    move.setMovementName("SOLIDER_AFTER");
                    move.setDest(chessRow, j);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
//				break;
                }
            }
        }

        // To top
        chessRow = i;
        chessCol = j;
        if (chessRow >= 0 && !isWhite) {
            chessRow--;
            if (chessRow < 0) {
                chessRow = i;
//				break;
            }
            if (chessRow != i) {
                if (chessBoard[chessRow][j] == EMPTY || hasEnemy(chessBoard, chessRow, j, isWhite)) {
                    Movement move = new Movement();
                    move.setFrom(i, j);
                    move.setMovementName("SOLIDER_AFTER");
                    move.setDest(chessRow, j);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessRow = i;
//				break;
                }
            }
        }
        // To right
        chessRow = i;
        chessCol = j;
        if (chessCol < BOARD_WIDTH) {
            chessCol++;
            if (chessCol == BOARD_WIDTH) {
                chessCol = j;
//				break;
            }
            if (chessCol != j) {
                if (chessBoard[i][chessCol] == EMPTY || hasEnemy(chessBoard, i, chessCol, isWhite)) {
                    Movement move = new Movement();
                    move.setFrom(i, j);
                    move.setMovementName("SOLIDER_AFTER");
                    move.setDest(i, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessCol = j;
                }
            }
        }
        // To left
        chessRow = i;
        chessCol = j;
        if (chessCol >= 0) {
            chessCol--;
            if (chessCol < 0) {
                chessCol = j;
//				break;
            }
            if (chessCol != j) {
                if (chessBoard[i][chessCol] == EMPTY || hasEnemy(chessBoard, i, chessCol, isWhite)) {
                    Movement move = new Movement();
                    move.setFrom(i, j);
                    move.setMovementName("SOLIDER_AFTER");
                    move.setDest(i, chessCol);
                    movement[numberOfBranch[level]] = move;
                    numberOfBranch[level]++;
                } else {
                    chessCol = j;
                }
            }
        }
    }

    private int evaluate(double[][] chessBoard) {
        int score1 = 0;
        int score2 = 0;
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (isWhiteSide(chessBoard[i][j])) {
                    score1 += (int) Math.floor(chessBoard[i][j]);
                } else {
                    score2 -= (int) Math.floor(chessBoard[i][j]);
                }

            }
        }

        return score1 + score2;
    }

    public int minimax(int depth, double[][] temp, int level, boolean isMaximizingPlayer, int alpha, int beta,
                       boolean isWhite) {
        double[][] chessBoard = new double[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[i].length; j++) {
                chessBoard[i][j] = temp[i][j];
            }
        }
        int[] numberOfBranch = new int[50];
        Movement[] movement = new Movement[200];
        if (depth == 0) {
            return evaluate(chessBoard);
        }
        generateMovement(chessBoard, movement, level, numberOfBranch, isWhite);
        if (isMaximizingPlayer) {
            int bestValue = -Integer.MAX_VALUE;
            for (int k = 0; k < numberOfBranch[level]; k++) {
                // try
                double enemyChessValue = 0;
                int fromRow = movement[k].getFromRow();
                int fromCol = movement[k].getFromCol();
                int destRow = movement[k].getDestRow();
                int destCol = movement[k].getDestCol();
                if (hasEnemy(chessBoard, destRow, destCol, isWhite)) {
                    enemyChessValue = chessBoard[destRow][destCol];
                }
                chessBoard[destRow][destCol] = chessBoard[fromRow][fromCol];
                chessBoard[fromRow][fromCol] = EMPTY;
                if (movement[k].isSoliderMovement()) {
                    chessBoard[destRow][destCol] = SOLIDER_AFTER_CROSSING_RIVER_WHITE;
                }
                int value;
                if (enemyChessValue == 999999.02) {
                    value = 999999999 - level;

                } else {
                    value = minimax(depth - 1, chessBoard, level + 1, false, alpha, beta, !isWhite);
                }
                if (value > bestValue) {
                    bestValue = Math.max(bestValue, value);
                    if (level == 0) {

                        finalMovement.setFrom(fromRow, fromCol);
                        finalMovement.setDest(destRow, destCol);
                        if(value == 999999999 - level){
                            finalMovement.setGameEnd(true);
                        }else{
                            finalMovement.setGameEnd(false);
                        }
                    }
                }

                alpha = Math.max(alpha, bestValue);

                // backtrack
                chessBoard[fromRow][fromCol] = chessBoard[destRow][destCol];
                chessBoard[destRow][destCol] = enemyChessValue;
                if (movement[k].isSoliderMovement()) {
                    chessBoard[destRow][destCol] = SOLDIER_BEFORE_CROSSING_RIVER_WHITE;
                }
                if (beta <= alpha) {
                    break;
                }
            }
            return bestValue;
        } else {
            int worstValue = Integer.MAX_VALUE;
            for (int k = 0; k < numberOfBranch[level]; k++) {
                // try
                double enemyChessValue = 0;
                int fromRow = movement[k].getFromRow();
                int fromCol = movement[k].getFromCol();
                int destRow = movement[k].getDestRow();
                int destCol = movement[k].getDestCol();
                if (hasEnemy(chessBoard, destRow, destCol, isWhite)) {
                    enemyChessValue = chessBoard[destRow][destCol];
                }
                chessBoard[destRow][destCol] = chessBoard[fromRow][fromCol];
                chessBoard[fromRow][fromCol] = EMPTY;
                if (movement[k].isSoliderMovement()) {
                    chessBoard[destRow][destCol] = SOLIDER_AFTER_CROSSING_RIVER_BLACK;
                }
                int value = minimax(depth - 1, chessBoard, level + 1, true, alpha, beta, !isWhite);

                if (value < worstValue) {
                    if (value == 1000489) {
                        System.out.println();
                    }
                    worstValue = Math.min(value, worstValue);
                }
                beta = Math.min(worstValue, beta);

                // backtrack
                chessBoard[fromRow][fromCol] = chessBoard[destRow][destCol];
                chessBoard[destRow][destCol] = enemyChessValue;
                if (movement[k].isSoliderMovement()) {
                    chessBoard[destRow][destCol] = SOLDIER_BEFORE_CROSSING_RIVER_WHITE;
                }
                if (alpha >= beta) {
                    break;
                }
            }
            return worstValue;
        }
    }

    public void initChessBoard() {
        // initialize chessBoard
        initChessPosition();
//		initChessColor();

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                System.out.print(chess[i][j] + "   ");
            }
            System.out.println();
        }

    }

    private void initChessColor() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                chessColor[i][j] = 0;
            }
        }
        int DARK = 2;
        int WHITE = 1;

        chessColor[0][0] = DARK;
        chessColor[0][1] = DARK;
        chessColor[0][2] = DARK;
        chessColor[0][3] = DARK;
        chessColor[0][4] = DARK;
        chessColor[0][5] = DARK;
        chessColor[0][6] = DARK;
        chessColor[0][7] = DARK;
        chessColor[0][8] = DARK;

        chessColor[9][0] = WHITE;
        chessColor[9][1] = WHITE;
        chessColor[9][2] = WHITE;
        chessColor[9][3] = WHITE;
        chessColor[9][4] = WHITE;
        chessColor[9][5] = WHITE;
        chessColor[9][6] = WHITE;
        chessColor[9][7] = WHITE;
        chessColor[9][8] = WHITE;

        for (int i = 0; i < BOARD_WIDTH; i += 2) {
            chessColor[3][i] = DARK;
            chessColor[6][i] = WHITE;
        }

        chessColor[2][1] = DARK;
        chessColor[7][1] = WHITE;
        chessColor[2][7] = DARK;
        chessColor[7][7] = WHITE;
    }

    private void initChessPosition() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                this.chess[i][j] = 0;
            }
        }

        // Declare white chess value
        double GENERAL_WHITE = 999999.01; // tướng
        double ADVISOR_WHITE = 20.01; // sĩ
        double ELEPHANT_WHITE = 25.01; // tượng
        double HORSE_WHITE = 40.01; // mã
        double CHARIOT_WHITE = 90.01; // xe
        double CANON_WHITE = 50.01; // pháo
        double SOLDIER_BEFORE_CROSSING_RIVER_WHITE = 10.01; // tốt trước khi qua sông
        double SOLIDER_AFTER_CROSSING_RIVER_WHITE = 15.01; // tốt sau khi qua sông
        double EMPTY_WHITE = 0.01;

        // Declare black chess value
        double GENERAL_BLACK = 999999.02;
        double ADVISOR_BLACK = 20.02;
        double ELEPHANT_BLACK = 25.02;
        double HORSE_BLACK = 40.02;
        double CHARIOT_BLACK = 90.02;
        double CANON_BLACK = 50.02;
        double SOLDIER_BEFORE_CROSSING_RIVER_BLACK = 10.02;
        double SOLIDER_AFTER_CROSSING_RIVER_BLACK = 15.02;
        double EMPTY_BLACK = 0.02;

        chess[0][0] = CHARIOT_WHITE;
        chess[0][1] = HORSE_WHITE;
        chess[0][2] = ELEPHANT_WHITE;
        chess[0][3] = ADVISOR_WHITE;
        chess[0][4] = GENERAL_WHITE;
        chess[0][5] = ADVISOR_WHITE;
        chess[0][6] = ELEPHANT_WHITE;
        chess[0][7] = HORSE_WHITE;
        chess[0][8] = CHARIOT_WHITE;

        chess[9][0] = CHARIOT_BLACK;
        chess[9][1] = HORSE_BLACK;
        chess[9][2] = ELEPHANT_BLACK;
        chess[9][3] = ADVISOR_BLACK;
        chess[9][4] = GENERAL_BLACK;
        chess[9][5] = ADVISOR_BLACK;
        chess[9][6] = ELEPHANT_BLACK;
        chess[9][7] = HORSE_BLACK;
        chess[9][8] = CHARIOT_BLACK;

        for (int i = 0; i < BOARD_WIDTH; i += 2) {
            chess[3][i] = SOLDIER_BEFORE_CROSSING_RIVER_WHITE;
            chess[6][i] = SOLDIER_BEFORE_CROSSING_RIVER_BLACK;
        }


        chess[2][1] = CANON_WHITE;
        chess[2][7] = CANON_WHITE;
        chess[7][1] = CANON_BLACK;

        chess[7][7] = CANON_BLACK;
    }
}
