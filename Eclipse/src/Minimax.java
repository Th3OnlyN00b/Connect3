import java.util.*;

public class Minimax implements Connect3{
	RowTrie loaded = new RowTrie();
	int foundn = 0;
	
	public int play() {
		return minimax(new C3(4,5)).lastCol;
	}
	
	public int play(C3 currBoard) {		
		return minimax(currBoard).lastCol;
	}
	
	public UtilBoard minimax(C3 state){
        //System.out.println("Minimax called!!!!");
		//SEARCH FIRST!!!!!!!!!
		int[] encR = state.encodeRows();
		UtilBoard found = loaded.searchNode(encR);
		if(found!=null) {
			foundn++;
			if(foundn%100000==0) {System.out.println("Found!!!! Num: "+foundn +"Col: " + found.lastCol + " Util: " + found.util + " P1 turn?: "+ state.p1Turn);}
			return found;}
		
        switch(state.winner) {
        case 1: 
        	loaded.insert(encR, 1, state.lastCol);
        	return new UtilBoard(state.lastCol, 1);
        case 2:
        	loaded.insert(encR, -1, state.lastCol);
        	return new UtilBoard(state.lastCol, -1);
        }
        ArrayList<C3> children = state.children();
        if(children.size()==0){ 
        	loaded.insert(encR, 0, state.lastCol);
            return new UtilBoard(state.lastCol,0);
        }
        
        UtilBoard bestUtility;
        if(state.p1Turn){ //Return on 1st 1, otherwise return a tie, if neither is available return first option: util -1
            bestUtility = new UtilBoard(children.get(0).lastCol,-1);
            for(C3 child : children){
                UtilBoard util = minimax(child);
                if(util.util==1){
                    
                	bestUtility = new UtilBoard(child.lastCol,1);
                    loaded.insert(encR, bestUtility);
                    } //Can't do better than guaranteed 1, return!!
                else if(util.util==0&&bestUtility.util!=1){
                	bestUtility = new UtilBoard(child.lastCol,0);
                	}
            }
           // System.out.println("Plater 1 Turn!!!" + bestUtility.lastCol);

        }
        else{//Return on 1st -1, otherwise return a tie, if neither is available return first option: util 1
            bestUtility = new UtilBoard(children.get(0).lastCol,1);
            for(C3 child: children){
                UtilBoard util = minimax(child);
                if(util.util==-1){bestUtility = new UtilBoard(child.lastCol,-1);} //Can't do better than guaranteed -1, return
                else if(util.util==0&&bestUtility.util!=-1){
                	bestUtility = new UtilBoard(child.lastCol,0);
                	loaded.insert(encR, bestUtility);
                    }
            }
           // System.out.println("Plater 2 Turn!!!" + bestUtility.lastCol);
        }
        loaded.insert(encR, bestUtility);
        return bestUtility;
    }
}
