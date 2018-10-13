
public class tester {
	public static void main(String[] args) {
		byte[] f = {1,0,0,0,0};
		byte[] s = {1,2,0,0,0};
		byte[] t = {2,1,0,0,0};
		byte[] f2 = {1,2,0,0,0};
		byte[][] b = {f2,t,s,f};
		BoardState board = new BoardState(4,5);
		/*board.p1turn = false;
		board.board =b;
		System.out.println(board.toString());
		System.out.println("Winner: " + board.victory(0, 0));*/
		System.out.println("Starting Pruned Minimax");
		//int col1 = PrunedMinimax.play(board);
		//System.out.println("Pruned Minimax Returns: " + col1 + ". Starting normie minimax");
		
		BoardState board2 = new BoardState(4,5);
		//int col = Minimax.play(board2);
		
		//System.out.println("Minimax returns: " +col);
	}
}
