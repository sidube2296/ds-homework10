package edu.uwm.cs351;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import edu.uwm.cs351.util.AbstractEntry;

/**
 * Multiset of strings, sorted lexicographically.
 */
public class WordMultiset extends AbstractMap<String, Integer> // extends something
{
	private static class Node extends AbstractEntry<String, Integer> // extends something
	{
		String string;
		int count;
		Node left, right, next;
		Node (String s) { string = s; count = 1; }
		@Override
		public String getKey() {
			// TODO Auto-generated method stub
			return string;
		}
		@Override
		public Integer getValue() {
			// TODO Auto-generated method stub
			return count;
		}	
		
		@Override
        public Integer setValue(Integer value) {
            if (value == null || value <= 0) throw new IllegalArgumentException("Value must be positive");   
            Integer o = count;
            count = value;
            return o;
        }
		
	}
	
	private Node dummy;
	private int numEntries;
	private int version;
	
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);
	
	/**
	 * Used to report an error found when checking the invariant.
	 * By providing a string, this will help debugging the class if the invariant should fail.
	 * @param error string to print to report the exact error found
	 * @return false always
	 */
	private static boolean report(String error) {
		reporter.accept(error);
		return false;
	}

	private int reportNeg(String error) {
		report(error);
		return -1;
	}

	/**
	 * Count all the nodes in this subtree, 
	 * while checking that all the keys are all in the range (lo,hi),
	 * and that the keys are arranged in BST form,
	 * and that the node are linked in-order.
	 * If a problem is found, -1 is returned and exactly one problem is reported.
	 * <p>
	 * @param n the root of the subtree to check
	 * @param lo if non-null then all strings in the subtree rooted
	 * 				at n must be [lexicographically] greater than this parameter
	 * @param hi if non-null then all strings in the subtree rooted
	 * 				at n must be [lexicographically] less than this parameter
	 * @param first the node that should be first in this tree, or if the tree
	 *        is empty, then this should be the next node
	 * @param next the node that should be linked next after this subtree.
	 * @return number of nodes in the subtree, or -1 is there is a problem.
	 */
	private int checkInRange(Node n, String lo, String hi, Node first, Node next)
	{
		// TODO: Update this method
		if (n == null){
	        if (first != next) return reportNeg("Empty subtree should link " + first + " to " + next);
	        return 0;
	    }

		if (n.string == null) return reportNeg("null word found");
		
		if (n.count <= 0 && n != dummy) return reportNeg("Node count mismatch");
		
		//first check node r
		if (lo != null && (n.string.equals(lo) || n.string.compareTo(lo) < 0))
			return reportNeg("Detected node outside of low bound: "+n.string);
		if (hi != null && (n.string.equals(hi) || n.string.compareTo(hi) > 0))
			return reportNeg("Detected node outside of high bound: "+n.string);
		
		//check subtrees
		int leftSubtree = checkInRange(n.left, lo, n.string, first, n); // fix: checkInRange(n.left, lo, n.string);
		if (leftSubtree < 0) return -1;
		
		int rightSubtree = checkInRange(n.right, n.string, hi, n.next, next); // fix: checkInRange(n.right, n.string, hi);
		if (rightSubtree < 0) return -1;
				
		//otherwise return 1 + nodes in subtrees
		return 1 + leftSubtree + rightSubtree;
	}
	
	/**
	 * Check the invariant.  
	 * Returns false if any problem is found. 
	 * @return whether invariant is currently true.
	 * If false is returned then exactly one problem has been reported.
	 */
	private boolean wellFormed() {
		// TODO: Update this method
	    if (dummy == null) return report("Dummy node is null");
	    
	    if (dummy.string != null) return report("String in dummy node is null");
	    
	    if (dummy.count != 0) return report("Dummy node count should be zero");
	    
	    if (dummy.left != null) return report("Dummy node have left child");

	    if (dummy.next == null && numEntries > 0) return report("Error in dummy node's next reference");
	    
		int n = checkInRange(dummy.right, null, null, dummy.next, null); // fix: checkInRange(root, null, null);
		if (n < 0) return false; // problem already reported
		if (n != numEntries) return report("manyNodes is " + numEntries + " but should be " + n);
		return true;
	}

	private WordMultiset(boolean unused) { } // do not modify, used by Spy
	
	/**
	 * Creates an empty multiset
	 */
	public WordMultiset() {
		// TODO: Implement the constructor (BEFORE the assertion!)
		dummy = new Node(null);
		dummy.count = 0;
	    dummy.left = null;
	    dummy.right = null;
	    dummy.next = null;
	    numEntries = 0;
	    version = 0;
		assert wellFormed() : "invariant false at end of constructor";
	}
	
	@Override // required
	public int size() {
		assert wellFormed() : "invariant false at start of size()";
		return numEntries;
	}
	
	/**
	 * Look for the node for a particular key, creating it if requested
	 * if it doesn't exist.
	 * @param r subtree to look for the key, may be null
	 * @param key key to look for for, must not be null
	 * @param create whether to create a node with count 0 if not in tree
	 * @param before the node before the first node in this subtree, must not be null
	 * @return node with this key, or null if it is not present and create is false
	 */
	private Node getNode(Node r, String key, boolean create, Node before) {
		// TODO: When creating the node, use the fact that
		// either "before" or "before.next" will be the parent of the new node. 
		if (r == null) {
		if (!create) return null;
		Node n = new Node(key);
		n.count = 0;
		if (before.right == null) before.right = n; 
		else before.next.left = n;       
		n.next = before.next;
		before.next = n;
		return n;
	 }
	    if (key.compareTo(r.string) == 0) return r;
	    if (key.compareTo(r.string) < 0) {
	        if (r.left == null && create) {
	            Node n = new Node(key);
	            n.count = 0;
	            r.left = n;
	            n.next = r;
	            before.next = n;
	            return n;
	        }
	        return getNode(r.left, key, create, before);
	    } 
	    else {
	        if (r.right == null && create) {
	            Node n = new Node(key);
	            n.count = 0;       
	            r.right = n;
	            n.next = r.next;
	            r.next = n;
	            return n;
	        }
	        return getNode(r.right, key, create, r);
	    }
	}
	
	/**
	 * Find the node with the given key, creating it if necessary 
	 * (but only if "create" is true).  If create is true, then
	 * the data structure invariant will be temporarily false
	 * due to the node with a zero count.  The caller must address
	 * the issue immediately.
	 * @param key word to look for, must not be null
	 * @param create whether to create a node with count=0 if not present.
	 * @return node, or null if not present (and create is false)
	 */
	private Node getNode(String key, boolean create) {
		// TODO
		return getNode(dummy.right, key, create, dummy);
	}
	
	
	/**
	 * Add a new string to the multiset. If it already exists, 
	 * increase the count for the string and return false.
	 * Otherwise, set the count to one and return true.
	 * @param str the string to add (must not be null)
	 * @return true if str was added, false otherwise
	 * @throws NullPointerException if str is null
	 */
	public boolean add(String str) {
		assert wellFormed() : "invariant false at start of add";
		boolean result = false;
		// TODO: Implement this method
		assert wellFormed() : "invariant false at end of add";
		return result;
	}
	
	/**
	 * Associates the specified value with the specified key in this map.
	 * If the map previously contained a mapping for the key, the old value is replaced.
	 * @param key key with which the specified value is to be associated, must not be null
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with key, or null if there was no mapping for key.
	 * @throws NullPointerException if the key is null
	 */
	@Override
	public Integer put(String key, Integer value) {
		if (key == null) throw new NullPointerException("Key is null");	    
	    if (value == null || value <= 0) throw new IllegalArgumentException("Value is null");	      
	    assert wellFormed() : "invariant false at start of put";
	    Node node = getNode(key, true);
	    if (node == null) throw new NullPointerException("No node to create");
	    Integer o = (node.count == 0) ? null : node.count;
	    node.count = value;
	    if (o == null && value > 0) {
	    	numEntries++;
	    	version++;
	    }
	    else if (o != null && value == 0) remove(key);
	    assert wellFormed() : "invariant false at end of put";
	    return o;
	}
	
	/**
	 * Remove one copy of a word from the multiset.
	 * If there are multiple copies, then we just adjust the count,
	 * and the map is unaffected (iterators don't go stale).
	 * @param str string to remove one of, may be null (but ignored if so)
	 * @return true if the word was in the multiset.
	 */
	public boolean removeOne(String str) {
		assert wellFormed() : "invariant false at start of removeOne";
		boolean result = false;
		// TODO: implement this method
		assert wellFormed() : "invariant false at end of removeOne";
		return result;
	}
		
	
	private final EntrySet entrySet = new EntrySet();
	
	@Override // required
	public Set<Map.Entry<String,Integer>> entrySet() {
		return entrySet;
	}
	
	private class EntrySet extends AbstractSet<Map.Entry<String, Integer>> // extends something
 implements Set<Entry<String, Integer>>
	{

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return WordMultiset.this.size();
		}

		@Override //random
		public Iterator<Entry<String, Integer>> iterator() {
			// TODO Auto-generated method stub
			return new MyIterator();
		}

	}
	
	
	// TODO: Implement iterator class
	 private class MyIterator implements Iterator<Map.Entry<String, Integer>> {
		 private Node current;
		 private Node lastReturned;
		 private int expectedVersion;
		 private boolean canRemove;

	        /**
	         * MyIterator constructor initializes the iterator to start at the first real node.
	         */
	        public MyIterator() {
	        	this.current = dummy.next;
	            this.lastReturned = null;
	            this.expectedVersion = version;
	            this.canRemove = false;
	        }
	        
	        public boolean wellFormed() {
	        	// Check the outer WordMultiset's invariant
	            if (!WordMultiset.this.wellFormed()) {
	                reporter.accept("Iterator: WordMultiset is not well-formed");
	                return false;
	            }
	            // Check if the version matches
	            if (this.expectedVersion != WordMultiset.this.version) {
	                reporter.accept("Iterator: version mismatch");
	                return false;
	            }
	            // Check if the current node is in the tree
	            if (current != null) {
	                Node found = WordMultiset.this.getNode(current.string, false);
	                if (found != current) {
	                    reporter.accept("Iterator: Current node is not in the tree");
	                    return false;
	                }
	            }
	            return true;
	        }

	        /**
	         * Checks if the iterator has more elements.
	         * @return true if there is a next element, false otherwise
	         */
	        @Override //required
	        public boolean hasNext() {
	        	if (version != expectedVersion) throw new ConcurrentModificationException("WordMultiset modified during iteration");
	            return current != null;
	        }
	        
	        /**
	         * Returns the next entry in the iterator
	         * @return the next Map.Entry<String, Integer>
	         * @throws NoSuchElementException if there are no more elements
	         * @throws ConcurrentModificationException if the WordMultiset was modified after the iterator was created
	         */
	        @Override
	        public Map.Entry<String, Integer> next() {
	        	if (version != expectedVersion) throw new ConcurrentModificationException("WordMultiset modified during iteration");
	            if (!hasNext()) throw new NoSuchElementException("No more elements to iterate");           
	            lastReturned = current;
	            current = current.next;
	            canRemove = true;
	            assert wellFormed() : "Invariant failed at end of Iterators' next():";
	            return lastReturned;
	        }

	        @Override
	        public void remove() {
	        	if (!canRemove) throw new IllegalStateException("Cannot remove before Itrators' remove()");
	            if (version != expectedVersion) throw new ConcurrentModificationException("WordMultiset modified during iteration");
	            assert wellFormed() : "Invariant failed at start of remove():";
	            WordMultiset.this.remove(lastReturned.string);
	            expectedVersion = version;
	            lastReturned = null;
	            canRemove = false;
	            assert wellFormed() : "Invariant failed at end of Iterators' remove():";
	        }
	     }
		
	/**
	 * Used for testing the invariant.  Do not change this code.
	 */
	public static class Spy {
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}
		
		/**
		 * A public version of the data structure's internal node class.
		 * This class is only used for testing.
		 */
		public static class Node extends WordMultiset.Node {
			// Even if Eclipse suggests it: do not add any fields to this class!
			/**
			 * Create a node with null data and null next fields.
			 */
			public Node() {
				this(null, 0, null, null);
			}
			
			@Override // implementation
			public String toString() {
				return "Node@" + String.format("%x", System.identityHashCode(this)) + "(" + string + ":" + count + ")";
			}
			
			/**
			 * Create a node with the given values
			 * @param s data for new node, may be null
			 * @param c count for new node
			 * @param l left for new node, may be null
			 * @param r right for new node, may be null
			 */
			public Node(String s, int c, Node l, Node r) {
				super(null);
				this.string = s;
				this.count = c;
				this.left = l;
				this.right = r;
				this.next = null;
			}
			
			public String getString() { return string; }
			public int getCount() { return count; }
			public Node getLeft() { return (Node)left; }
			public Node getRight() { return (Node)right; }
			public Node getNext() { return (Node)next; }
			
			/**
			 * A debugging equality check.  If equality fails, we
			 * report problems in the same manner as invariant checks 
			 */
			public boolean equals(Object x) {
				if (x == null) return report("not equal: null");
				if ((!(x instanceof WordMultiset.Node))) return report("Wrong type: " + x.getClass());
				WordMultiset.Node n = (WordMultiset.Node)x;
				if (!Objects.equals(string, n.string)) return report("Expected string: " + string + " but got " + n.string);
				if (count != n.count) return report("Expected count = " + count + ", but was " + n.count);
				if (left != n.left) return report("Expect left = " + left + ", but was " + n.left);
				if (right != n.right) return report("Expect right = " + right + ", but was " + n.right);
				if (next != n.next) return report("Expect next = " + next + ", but was " + n.next);
				return true;
			}
			
			/**
			 * Change the data in the node.
			 * @param s new string to use
			 */
			public void setString(String s) {
				this.string = s;
			}
			
			/**
			 * Set the count in this node
			 * @param c new count
			 */
			public void setCount(int c) {
				this.count = c;
			}
			
			/**
			 * Change a node by setting the "left" field.
			 * @param n new left field, may be null.
			 */
			public void setLeft(Object n) {
				this.left = (WordMultiset.Node)n;
			}
			
			/**
			 * Change a node by setting the "right" field.
			 * @param n new right field, may be null.
			 */
			public void setRight(Object n) {
				this.right = (WordMultiset.Node)n;
			}
			
			/**
			 * Change a node by setting the "next" field.
			 * @param n new next field, may be null.
			 */
			public void setNext(Object n) {
				this.next = (WordMultiset.Node)n;
			}
			
		}

		/**
		 * Create a debugging instance of the ADT
		 * with a particular data structure.
		 * @param r dummy node, may be null
		 * @param m many nodes
		 * @param v TODO
		 * @return a new instance of a BallSeq with the given data structure
		 */
		public WordMultiset newInstance(Node r, int m, int v) {
			WordMultiset result = new WordMultiset(false);
			result.dummy = r;
			result.numEntries = m;
			result.version = v;
			return result;
		}

		/**
		 * Return whether debugging instance meets the 
		 * requirements on the invariant.
		 * @param lx instance of to use, must not be null
		 * @return whether it passes the check
		 */
		public boolean wellFormed(WordMultiset lx) {
			return lx.wellFormed();
		}
		
		/**
		 * Return the result of the helper method checkInRange
		 * @param n node to check for
		 * @param lo lower bound
		 * @param hi upper bound
		 * @param first node at start
		 * @param next next node
		 * @return result of running checkInRange on a debugging instance of Lexicon
		 */
		public int checkInRange(Node n, String lo, String hi, Node first, Node next) {
			WordMultiset wm = new WordMultiset(false);
			wm.dummy = null;
			wm.numEntries = -1;
			return wm.checkInRange(n,lo,hi,first,next);
		}
		
		/**
		 * Run the getNode helper method on the given arguments
		 * @param r subtree to look in
		 * @param key string to look for
		 * @param create whether to create a node if not found
		 * @param b node before the subtree
		 * @return the node
		 */
		public WordMultiset.Node getNode(Node r, String key, boolean create, Node b) {
			WordMultiset wm = new WordMultiset(false);
			wm.dummy = null;
			wm.numEntries = -1;
			return wm.getNode(r, key, create, b);
		}
		
		/**
		 * Run getNode in a test instance.
		 * @param wm test instance to use
		 * @param key string to look for
		 * @param create whether to create a node if not found
		 * @return result of calling getNode method
		 */
		public WordMultiset.Node getNode(WordMultiset wm, String key, boolean create) {
			return wm.getNode(key, create);
		}
	}
	
}
