import java.util.ArrayList;

public class Random {
	public ArrayList<Integer> possibleMoves = new ArrayList<>();
	public int play() {
		return play(new C3(4,5));
	}
	
	public int play(C3 currBoard) {	
		for(Integer i : currBoard.validColumns) {
			possibleMoves.add(i);
		}
		return rand(currBoard);
	}
	
	
	public int rand(C3 state) {
		byte goal = state.p1Turn ? (byte)1 : (byte)2;
		ArrayList<C3> children = state.children();
		
		 
		
		for(C3 child : children) {
			if(child.winner!=0) {
				if(child.winner==goal) {
					return child.lastCol;
				}
				
			}
			else {
				for(C3 c : child.children()) {
					if(c.winner!=0&&c.winner!=goal) {
						possibleMoves.remove((Object)child.lastCol);
					}
				}
				
			}
		}
		
		if(possibleMoves.size()>0) {
			return possibleMoves.get((int)(Math.random()*possibleMoves.size()));
		}
		else {
			for(Integer i : state.validColumns) {
				return i;
			}
		}
		
		
		return 1;
	}
}
