import java.util.*;
public class Hueristic implements Connect3{
	public int play() {
		return play(new C3(4,5));
	}
	
	public int play(C3 currBoard) {		
		return bleep(currBoard);
	}
	
	
	public int heuristic(C3 board, byte goal) {
		if(board.winner==goal) {
			return 10000;
		}
		int good = 0;
		for(int i =0;i<board.board[0].length;i+=2) {
			for(int j=0;j<board.board.length;j+=2) {
				if(j<board.board.length-1) {
					if(board.board[j+1][i]==goal) {good++;}
				}
				if(j>0) {
					if(board.board[j-1][i]==goal) {good++;}
				}
				if(i<board.board[0].length-1) {
					if(board.board[j][i+1]==goal) {good++;}
				}
				if(i>0) {
					if(board.board[j][i-1]==goal) {good++;}
				}
			}
		}
		
		return good;
			
	}
	
	public int bleep(C3 state) {
		ArrayList<C3> children = state.children();
		int bestCol = children.get(0).lastCol;
		int bestRating = Integer.MIN_VALUE;
		
		for(C3 child: children) {
			int rating = heuristic(child, state.p1Turn ? (byte)1 : (byte)2)*3;
			for(C3 c : child.children()) {
				rating-=heuristic(c, child.p1Turn ? (byte)1 : (byte)2 )/2;
			}
			if(rating>bestRating) {
				bestCol=child.lastCol;
				bestRating = rating;
			}
			
		}
		return bestCol;
		
	}
}
