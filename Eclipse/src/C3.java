import java.util.*;

public class C3 {
    public byte[][] board;
    public Set<Integer> validColumns;
    public boolean p1Turn;
    byte winner = 0;
    int lastCol = -1;
    
    


    public C3(){
        this.lastCol = -1;
        this.board =null;
    	p1Turn = true;
    }
    
    public C3(int rows, int cols) {
    	this.board = new byte[rows][cols];
    	this.validColumns = new HashSet<Integer>();
    	for(int i=0;i<cols;i++) {
    		validColumns.add(i);
    	}
    	
    }

    
    
    public int encodeRow(int row){
    	
    	StringBuilder baseThree = new StringBuilder();
    	for(int i=0;i<board[row].length;i++) {
    		baseThree.append(board[row][i]);
    	}
    	int encoding = Integer.parseInt(baseThree.toString(), 3);
    	
    	return encoding;
    }
    
    
    public int[] encodeRows(){
    	int[] ret = new int[board.length];
    	for(int i=0;i<board.length;i++) {
    		ret[i] = encodeRow(i);
    	}
    	
    	return ret;
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

    public boolean backslash(int x, int y, int target){ //checl bacslash diagonal victory
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

    public C3 cloner(){ //Create new C3 instance for each place
        C3 clone = new C3();
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
        clone.p1Turn = this.p1Turn;
        clone.winner = this.winner;
        clone.lastCol = this.lastCol;

        return clone;
    }

    public C3 place(int col){ //place whoevers turn it is in specified column
        C3 newState = this.cloner();
        newState.lastCol = col;
        int h = -1;

        for(int i=newState.board.length-2;i>=0;i--){
            if(newState.board[i][col]!=0){
                h=i;
                break;
            }
        }

        newState.board[h+1][col] = newState.p1Turn ? (byte)1 : (byte)2;

        if(h+1==newState.board.length-1){
            newState.validColumns.remove(col);
        }

        if(newState.victory(h+1,col)){
            newState.winner = newState.board[h+1][col];
        }
        newState.p1Turn = !newState.p1Turn;

        return newState;
    }

    public ArrayList<C3> children(){
        ArrayList<C3> childs = new ArrayList<>();
        for(Integer col : validColumns){
            childs.add(place(col));
        }

        return childs;
    }
    
    public String toJSString() {
    	String retStr = "d";
    	for(int i = 0; i < board.length; i++) {
    		for(int j = 0; j < board[0].length; j++) {
    			retStr += board[i][j];
    		}
    	}
    	return retStr;
    }


}
