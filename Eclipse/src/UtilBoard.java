class UtilBoard {
    public byte lastCol;
    public byte util;

    public UtilBoard(int col, int util){
        this.lastCol = (byte)col;
        this.util = (byte)util;
    }
}