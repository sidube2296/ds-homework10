package edu.uwm.cs351;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import edu.uwm.cs351.util.AbstractEntry;

/**
 * Multiset of strings, sorted lexicographically.
 */
public class WordMultiset // extends something
{
	private static class Node // extends something
	{
		String string;
		int count;
		Node left, right, next;
		Node (String s) { string = s; count = 1; }
		
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
		if (n == null) return 0;
		if (n.string == null) return reportNeg("null word found");
		//first check node r
		if (lo != null && (n.string.equals(lo) || n.string.compareTo(lo) < 0))
			return reportNeg("Detected node outside of low bound: "+n.string);
		if (hi != null && (n.string.equals(hi) || n.string.compareTo(hi) > 0))
			return reportNeg("Detected node outside of high bound: "+n.string);
		
		//check subtrees
		int leftSubtree =  -1; // fix: checkInRange(n.left, lo, n.string);
		if (leftSubtree < 0) return -1;
		
		int rightSubtree = -1; // fix: checkInRange(n.right, n.string, hi);
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
		int n = -1; // fix: checkInRange(root, null, null);
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
		assert wellFormed() : "invariant false at end of constructor";
	}
	
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
		return r;
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
		return null; // TODO
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
	
	public Set<Map.Entry<String,Integer>> entrySet() {
		return entrySet;
	}
	
	private class EntrySet // extends something
 implements Set<Entry<String, Integer>>
	{

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean contains(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Iterator<Entry<String, Integer>> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] toArray() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T[] toArray(T[] a) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean add(Entry<String, Integer> e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean remove(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends Entry<String, Integer>> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub
			
		}
	}
	
	// TODO: Implement iterator class
		
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
