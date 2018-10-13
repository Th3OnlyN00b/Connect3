import java.util.*;
public class RowTrie {
	public TrieNode root;
	
	
	public RowTrie() {
		root = new TrieNode();
	}
	
	public void insert(int[] rows, int util, int col) {
		insert(rows, (byte)util, (byte)col);
	}
	
	public String childrenString(HashMap<Integer, TrieNode> selects) {
		StringBuilder c = new StringBuilder();
		for(Map.Entry<Integer, TrieNode> entry : selects.entrySet()) {
		    Integer key = entry.getKey();
		    
		    c.append("Key: "+key);

		    // do what you have to do here
		    // In your case, another loop.
		}
		
		return c.toString();
	}
	
	public void insert(int[] rows, UtilBoard u) {
		insert(rows, u.util, u.lastCol);
	}
	public void insert(int[] rows, byte util, byte col) {
        TrieNode curr = root;
       // System.out.print("length: " + rows.length);
        
        
        for(int i=0; i<rows.length; i++){
            int c = rows[i];
           // System.out.print
            //System.out.println(childrenString(curr.children));
            if(curr.children.containsKey(c)){
            	//System.out.println("Found key! i = "+i);
                curr = curr.children.get(c);
            }else{
            	TrieNode newNode = i==rows.length-1 ? new TrieNode(col, util) : new TrieNode();
                curr.children.put(c, newNode);
                curr = newNode;
            }
        }
    }
	
	
	 public UtilBoard searchNode(int[] rows){
	        HashMap<Integer, TrieNode> children = root.children;
	        TrieNode t = null;
	        for(int i=0; i<rows.length; i++){
	            int c = rows[i];
	            if(children.containsKey(c)){
	            	//System.out.println("FOund!");
	                t = children.get(c);
	                children = t.children;
	            }else{
	                return null;
	            }
	        }
	 
	        return t.result;
	    }
	
}

class TrieNode{
	HashMap<Integer, TrieNode> children = new HashMap<>();
	public UtilBoard result =null;
	
	public TrieNode(byte col, byte util) {
		result = new UtilBoard(col, util);
	}
	
	public TrieNode() {}
	
	
}
