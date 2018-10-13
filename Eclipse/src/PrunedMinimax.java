import java.util.ArrayList;

public class PrunedMinimax implements Connect3{
	
	public int play() {
		return minimax(new C3(4,5)).lastCol;
	}
	
	public int play(C3 currBoard) {		
		return minimax(currBoard).lastCol;
	}
	
	public static UtilBoard minimax(C3 state){
        //System.out.println("Minimax called!!!!");
        switch(state.winner) {
        case 1: 
        	return new UtilBoard(state.lastCol, 1);
        case 2:
        	return new UtilBoard(state.lastCol, -1);
        }
        ArrayList<C3> children = state.children();
        if(children.size()==0){ 
            return new UtilBoard(state.lastCol,0);
        }
        if(state.p1Turn){ //Return on 1st 1, otherwise return a tie, if neither is available return first option: util -1
            UtilBoard bestUtility = new UtilBoard(children.get(0).lastCol,-1);
            for(C3 child : children){
                UtilBoard util = minimax(child);
                if(util.util==1){

                    return new UtilBoard(child.lastCol,1);} //Can't do better than guaranteed 1, return!!
                else if(util.util==0){bestUtility=new UtilBoard(child.lastCol,0);}
            }

            return bestUtility;
        }
        else{//Return on 1st -1, otherwise return a tie, if neither is available return first option: util 1
            UtilBoard bestUtility = new UtilBoard(children.get(0).lastCol,1);
            for(C3 child: children){
                UtilBoard util = minimax(child);
                if(util.util==-1){return new UtilBoard(child.lastCol,-1);} //Can't do better than guaranteed -1, return
                else if(util.util==0){bestUtility = new UtilBoard(child.lastCol,0);}
            }

            return bestUtility;
        }
    }

}
