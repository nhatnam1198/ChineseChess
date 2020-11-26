package ai.chinesechess.models;

public class Movement {
    private int fromRow;
    private int fromCol;
    private int destRow;
    private int destCol;

    public String getMovementName() {
        return movementName;
    }

    public void setMovementName(String movementName) {
        this.movementName = movementName;
    }

    private String movementName;
    private boolean isSoliderMovement;



    public Movement(){

    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getDestRow() {
        return destRow;
    }

    public int getDestCol() {
        return destCol;
    }

    public void setFrom(int fromRow, int fromCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
    }

    public void setDest(int destRow, int destCol) {
        this.destRow = destRow;
        this.destCol = destCol;
    }
    public boolean isSoliderMovement() {
        return isSoliderMovement;
    }

    public void setSoliderMovement(boolean soliderMovement) {
        isSoliderMovement = soliderMovement;
    }
}