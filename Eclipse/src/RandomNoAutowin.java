import java.util.ArrayList;

public class RandomNoAutowin implements Connect3{
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
	
	private int rand(C3 board) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		for(int i : board.validColumns) {
			a.add(i);
		}
		int sel = (int)(Math.random()*a.size());
		return a.get(sel);
	}

}
