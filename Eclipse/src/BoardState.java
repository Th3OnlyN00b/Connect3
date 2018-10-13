import java.util.*;
public class BoardState {

	public byte[][] board;
	public Set<Integer> validColumns;
	public boolean p1turn;
	public byte winner;
	public byte lastCol;
	
	public BoardState(int col, int row) {
		this.board = new byte[col][row];
		
		this.validColumns = new HashSet<>();
		for(int i=0;i<col;i++) {
			validColumns.add(i);
		}
		
		this.p1turn = true;
		this.winner=0;
		
		
	}
	
	public BoardState() {}
	
	public ArrayList<BoardState> children(){
        ArrayList<BoardState> childs = new ArrayList<>();
        for(Integer col : validColumns){
            childs.add(place(col));
        }

        return childs;
    }

	public BoardState place(int col){ //place whoevers turn it is in specified column
        BoardState newState = this.cloner();
        newState.lastCol = (byte)col;
        int h = -1;

        for(int i=newState.board.length-2;i>=0;i--){
            if(newState.board[i][col]!=0){
                h=i;
                break;
            }
        }

        newState.board[h+1][col] = newState.p1turn ? (byte)1 : (byte)2;

        if(h+1==newState.board.length-1){
            newState.validColumns.remove(col);
        }

        if(newState.victory(h+1,col)){
            newState.winner = newState.board[h+1][col];
        }
        newState.p1turn = !newState.p1turn;

        return newState;
    }

	
	public boolean victory(int x, int y){ //Check to see if board is in victory state after each place, only checks spaces around most recent place
        int target = board[x][y];
        return (backslash(x,y,target)||checkV(x,y,target)||checkH(x,y,target)||slash(x,y,target));
    }

    public boolean checkH(int x, int y, int target){ //Check horizontal victory
        if(y!=0){
            if(y!=1){
                if(board[x][y-2]==target&&board[x][y-1]==target){
                    return true;
                }
            }
            if(y!=board[0].length-1){
                if(board[x][y-1]==target&&board[x][y+1]==target){
                    return true;
                }
            }
        }
        if(y<board[0].length-2){
            if(board[x][y+1]==target&&board[x][y+2]==target){
                return true;
            }
        }
        return false;
    }

    public boolean checkV(int x, int y, int target){ //check vertical victtory
        if(x>1){
            if(board[x-1][y]==target&&board[x-2][y]==target){
                return true;
            }
        }
        if(x<board.length-2){
            if(board[x+1][y]==target&&board[x+2][y]==target){
                return true;
            }
        }

        if(x>0&&x<board.length-1){
            if(board[x-1][y]==target&&board[x+1][y]==target){
                return true;
            }
        }

        return false;
    }

    public boolean slash(int x, int y,int target){//Check slash diagonal victory
        if(x>1&&y>1){
            if(board[x-1][y-1]==target&&board[x-2][y-2]==target){
                return true;
            }
        }
        if(x<board.length-2&&y<board[0].length-2){
            if(board[x+1][y+1]==target&&board[x+2][y+2]==target){
                return true;
            }
        }
        if(x<board.length-1&&x>0&&y<board[0].length-1&&y>0){
            if(board[x+1][y+1]==target&&board[x-1][y-1]==target){
                return true;
            }
        }
        return false;
    }

    public boolean backslash(int x, int y, int target){ //check backslash diagonal victory
        if(x>1&&y<board[0].length-2){
            if(board[x-1][y+1]==target&&board[x-2][y+2]==target){
                return true;
            }
        }
        if(x<board.length-2&&y>1){
            if(board[x+1][y-1]==target&&board[x+2][y-2]==target){
                return true;
            }
        }
        if(x<board.length-1&&x>0&&y<board[0].length-1&&y>0){
            if(board[x+1][y-1]==target&&board[x-1][y+1]==target){
                return true;
            }
        }

        return false;
    }

    public BoardState cloner(){ //Create new C3 instance for each place
        BoardState clone = new BoardState();
        clone.board = new byte[this.board.length][this.board[0].length];
        for(int i = 0;i<this.board.length;i++){
            for(int j=0;j<this.board[0].length;j++){
                clone.board[i][j] = this.board[i][j];
            }
        }
        clone.validColumns = new HashSet<Integer>();
        for(Integer i : this.validColumns){
            clone.validColumns.add((int)i);
        }
        clone.p1turn = this.p1turn;
        clone.winner = this.winner;

        return clone;
    }
	
    public String toString() {
    	StringBuilder s = new StringBuilder();
    	for(int i=this.board.length-1;i>=0;i--){
            for(int j=0;j<this.board[0].length;j++){
                s.append(this.board[i][j]);
            }
           	s.append('\n');
        }
    	
    	return s.toString();
    }
	
}
