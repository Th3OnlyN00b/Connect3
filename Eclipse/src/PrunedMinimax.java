import java.util.ArrayList;

public class PrunedMinimax implements Connect3{
	
	public int play(BoardState currBoard) {		
		return minimax(currBoard).col;
	}
	
	public static UtilCol minimax(BoardState state){
        //System.out.println("Minimax called!!!!");
        if(state.winner!=0){ //Someone has won, return!!!
            if(state.winner==1){

                UtilCol done = new UtilCol((byte)1, state.lastCol);
                return done;
            }
            else{

                UtilCol done = new UtilCol((byte)-1, state.lastCol);
                return done;
            }
        }
        ArrayList<BoardState> children = state.children();
        if(children.size()==0){ //Nowhere else to go, tue
        //    System.out.println("Draw!");
            return new UtilCol((byte)0, state.lastCol);
        }
        if(state.p1turn){ //Return on 1st 1, otherwise return a tie, if neither is available return first option: util -1
            UtilCol bestUtility = new UtilCol((byte)-1, children.get(0).lastCol);
            for(BoardState child : children){
                UtilCol util = minimax(child);
                if(util.util==1){
                    return new UtilCol((byte) 1, child.lastCol);} //Can't do better than guaranteed 1, return!!
                else if(util.util==0){bestUtility=new UtilCol((byte)0, child.lastCol);}
            }

            return bestUtility;
        }
        else{//Return on 1st -1, otherwise return a tie, if neither is available return first option: util 1
            UtilCol bestUtility = new UtilCol((byte)1, children.get(0).lastCol);
            for(BoardState child: children){
                UtilCol util = minimax(child);
                if(util.util==-1){return new UtilCol((byte)-1,child.lastCol);} //Can't do better than guaranteed -1, return
                else if(util.util==0){bestUtility = new UtilCol((byte)0, child.lastCol);}
            }

            return bestUtility;
        }
    }
}
