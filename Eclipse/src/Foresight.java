import java.util.*;
public class Foresight {
	public ArrayList<Integer> possibleMoves = new ArrayList<>();
	public ArrayList<Integer> guaranteedMoves = new ArrayList<>();

	public byte goal;
	public int play() {
		return play(new C3(4,5));
	}
	
	public int play(C3 currBoard) {	
		goal = currBoard.p1Turn ? (byte)1 : (byte)-1;
		
		for(Integer i : currBoard.validColumns) {
			possibleMoves.add((int)i);
		}
		
		foresight(currBoard);
		
		if(guaranteedMoves.size()>0) {
			return guaranteedMoves.get((int)(Math.random()*guaranteedMoves.size()));
		}
		else if(possibleMoves.size()>0) {
			System.out.print("HERE!");
			return possibleMoves.get((int)(Math.random()*possibleMoves.size()));
		}
		else {
			for(Integer i: currBoard.validColumns) {
				return i;
			}
		}
		
		return 1;
	}
	public void foresight(C3 state) {
		ArrayList<C3> children = state.children();
		
		for(C3 child : children) {
			UtilBoard expected = foresightR(child, 2);
			if(expected.util!=0) {
				if(expected.util==goal) {
					System.out.println("expectedUtil: " + expected.util + " Goal: " + goal+" Guaranteed: "+ child.lastCol);
					guaranteedMoves.add((int)child.lastCol);
				}
				else {
					possibleMoves.remove((Object)child.lastCol);
				}
			}
		}
		
		
	}
	
	public UtilBoard foresightR(C3 state, int steps) {
		
		switch(state.winner) {
        case 1: 
        	System.out.println("Won putting in: " + state.lastCol);
        	return new UtilBoard(state.lastCol, 1);
        case 2:
        	System.out.println("Col win: "+state.lastCol);
        	return new UtilBoard(state.lastCol, -1);
        }
		
		if(steps==0) {
			return new UtilBoard(state.lastCol, 0);
		}
		
        ArrayList<C3> children = state.children();
        if(children.size()==0){ 
            return new UtilBoard(state.lastCol,0);
        }
        if(state.p1Turn){ //Return on 1st 1, otherwise return a tie, if neither is available return first option: util -1
            UtilBoard bestUtility = new UtilBoard(children.get(0).lastCol,-1);
            for(C3 child : children){
                UtilBoard util = foresightR(child, steps-1);
                if(util.util==1){

                    return new UtilBoard(child.lastCol,1);} //Can't do better than guaranteed 1, return!!
                else if(util.util==0){bestUtility=new UtilBoard(child.lastCol,0);}
            }

            return bestUtility;
        }
        else{//Return on 1st -1, otherwise return a tie, if neither is available return first option: util 1
            UtilBoard bestUtility = new UtilBoard(children.get(0).lastCol,1);
            for(C3 child: children){
                UtilBoard util = foresightR(child, steps-1);
                if(util.util==-1){return new UtilBoard(child.lastCol,-1);} //Can't do better than guaranteed -1, return
                else if(util.util==0){bestUtility = new UtilBoard(child.lastCol,0);}
            }

            return bestUtility;
        }
    }
}
