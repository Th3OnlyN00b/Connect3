import java.util.*;

public class Minimax{
	
	public static byte play(BoardState currBoard) {		
		return minimax(currBoard).col;
	}
	
	public static UtilCol minimax(BoardState currBoard){
		switch(currBoard.winner) {
			case 1:
				return new UtilCol((byte)1,currBoard.lastCol);
			case 2:
				return new UtilCol((byte)-1, currBoard.lastCol);
		}
		
		ArrayList<BoardState> children = currBoard.children();
		
        if(children.size()==0){ //Nowhere else to go, tie
            return new UtilCol((byte)0, currBoard.lastCol);
        }
        if(currBoard.p1turn){ //Return on 1st 1, otherwise return a tie, if neither is available return first option: util -1
            UtilCol bestUtility = new UtilCol((byte)-1, children.get(0).lastCol);
            for(BoardState child : children){
                UtilCol util = minimax(child);
                if(util.util==1){
                    bestUtility = new UtilCol((byte)1,child.lastCol);} //Can't do better than guaranteed 1, return!!
                else if(util.util==0){bestUtility=new UtilCol((byte)0, child.lastCol);}
            }

            return bestUtility;
        }
        else{//Return on 1st -1, otherwise return a tie, if neither is available return first option: util 1
            UtilCol bestUtility = new UtilCol((byte)1, children.get(0).lastCol);
            for(BoardState child: children){
                UtilCol util = minimax(child);
                if(util.util==-1){
                	bestUtility = new UtilCol((byte)-1, child.lastCol);} //Can't do better than guaranteed -1, return
                else if(util.util==0){bestUtility = new UtilCol((byte)0, child.lastCol);}
            }

            return bestUtility;
        }
	}
}
