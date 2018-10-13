
public class tester {
	public static void main(String[] args) {
		C3 board = new C3(4, 5);

		byte[] f =  {0,0,0,0,0};
		byte[] s =  {0,0,0,0,0};
		byte[] t =  {0,0,0,0,0};
		byte[] f2 = {1,0,0,0,1};
		byte[][] b = {f2,t,s,f};
		
		board.board = b;
		
		//board.p1Turn = false;
		board.lastCol = 0;
		board.validColumns.remove(0);
		RowTrie test = new RowTrie();
		
		int[] one = {100,234,215,23,12};
		int[] two = {100, 234, 215, 11, 12};
		int[] three = {100,234,215,11,12};
		
		test.insert(one, 1, 1);
		test.searchNode(two);
		
		//test.insert(two, 2, 2);
		//UtilBoard two2 = test.searchNode(two);

		test.insert(three, 3, 3);
		UtilBoard one1 = test.searchNode(one);
		UtilBoard three3 = test.searchNode(three);
		
		
		System.out.println(one1==null);
		
		int[] encoded = board.encodeRows();
		
		for(int i=0;i<encoded.length;i++) {
			System.out.println(encoded[i]);
		}
		
		
		
		PrunedMinimax m1 = new PrunedMinimax();
		Minimax m2 = new Minimax();
		
		
		Foresight fo = new Foresight();
		int col3 = fo.play(board);
		
		Random r = new Random();
		int col4 = r.play(board);
		
		
		
		System.out.println("Foresight result: " + col3);
		System.out.println("Random result: " + col4);
		
		for(int i=board.board.length-1;i>=0;i--){
            for(int j=0;j<board.board[0].length;j++){
                System.out.print(board.board[i][j]);
            }
            System.out.println();
        }
		//int col1 = m1.play(board);
		//.out.println("Pruned output: " + col1);
		
		System.out.println("Starting normie minimax");
		
		int col2 = m2.play(board);
		
		System.out.print("Normie output: " + col2);
		
		
	}
}
