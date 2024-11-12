import java.util.BitSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs351.WordMultiset;
import edu.uwm.cs351.WordMultiset.Spy.Node;
import junit.framework.TestCase;

public class TestInternals extends TestCase {

	protected WordMultiset.Spy spy;
	protected WordMultiset self;
	
	@Override // implementation
	public void setUp() {
		spy = new WordMultiset.Spy();
	}
	
	protected WordMultiset.Spy.Node n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;
	
	protected WordMultiset.Spy.Node newNode(String key, int c, WordMultiset.Spy.Node l, WordMultiset.Spy.Node r) {
		return new WordMultiset.Spy.Node(key, c, l, r);
	}
	
	protected WordMultiset.Spy.Node newNode(String key, int c, Object l, Object r, Object n) {
		WordMultiset.Spy.Node result = new WordMultiset.Spy.Node(key, c, null, null);
		result.setLeft(l);
		result.setRight(r);
		result.setNext(n);
		return result;
	}
	
	protected int reports = 0;
	
	protected <T> void assertReporting(T expected, boolean expectReport, Supplier<T> test) {
		reports = 0;
		Consumer<String> savedReporter = spy.getReporter();
		try {
			spy.setReporter((String message) -> {
				++reports;
				if (message == null || message.trim().isEmpty()) {
					assertFalse("Uninformative report is not acceptable", true);
				}
				if (!expectReport) {
					assertFalse("Reported error incorrectly: " + message, true);
				}
			});
			assertEquals(expected, test.get());
			if (expectReport) {
				assertEquals("Expected exactly one invariant error to be reported", 1, reports);
			}
			spy.setReporter(null);
		} finally {
			spy.setReporter(savedReporter);
		}
	}
	
	protected void assertCheckInRange(int res, WordMultiset.Spy.Node r, String lo, String hi, Node f, Node n) {
		assertReporting(res, res < 0, () -> spy.checkInRange(r, lo, hi, f, n));
	}
	
	protected void assertWellFormed(boolean expected, WordMultiset r) {
		assertReporting(expected, !expected, () -> spy.wellFormed(r));
	}
	
	
	public void testC00() {
		assertCheckInRange(0, null, null, null, null, null);
	}
	
	public void testC01() {
		assertCheckInRange(0, null, "A", null, null, null);
	}

	public void testC02() {
		assertCheckInRange(0, null, null, "Z", null, null);
	}
	
	public void testC03() {
		n0 = newNode(null, 0, null, null);
		assertCheckInRange(-1, n0, null, null, null, null);
	}
	
	public void testC04() {
		n0 = newNode(null, 0, null, null);
		assertCheckInRange(-1, n0, "A", "Z", null, null);
	}
	
	public void testC05() {
		n0 = newNode(null, 0, null, null);
		assertCheckInRange(0, null, null, null, n0, n0);
	}
	
	public void testC06() {
		n0 = newNode(null, 0, null, null);
		n1 = newNode(null, 0, null, null);
		assertCheckInRange(-1, null, null, null, n0, n1);
	}
	
	public void testC07() {
		n1 = newNode("apple", 0, null, null);
		assertCheckInRange(0, null, "c", "d", n1, n1);
	}
	
	public void testC08() {
		n1 = newNode("apple", 0, null, null);
		assertCheckInRange(-1, null, "c", "d", null, n1);
	}

	public void testC09() {
		n1 = newNode("apple", 0, null, null);
		assertCheckInRange(-1, null, "c", "d", n1, null);
	}

	
	public void testC10() {
		n1 = newNode("apple", 1, null, null);
		assertCheckInRange(1, n1, null, null, n1, null);
	}
	
	public void testC11() {
		n1 = newNode("apple", 8, null, null);
		n2 = newNode("orange", 4, null, null);
		n1.setNext(n2);
		assertCheckInRange(1, n1, "A", null, n1, n2);
	}
	
	public void testC12() {
		n1 = newNode("apple", 0, null, null);
		assertCheckInRange(-1, n1, null, "ball", n1, null);
	}
	
	public void testC13() {
		n1 = newNode("apple", 1, null, null);
		assertCheckInRange(-1, n1, "all", "ball", null, null);
	}
	
	public void testC14() {
		n1 = newNode("apple", 2, null, null);
		assertCheckInRange(-1, n1, "ball", null, null, null);
	}
	
	public void testC15() {
		n1 = newNode("apple", 1, null, null);
		assertCheckInRange(-1, n1, null, "z", n1, n1);
	}
	
	public void testC16() {
		n1 = newNode("apple", 3, null, null);
		n2 = newNode("apple", 6, null, null);
		assertCheckInRange(-1, n1, "a", "z", n2, null);
	}
	
	public void testC17() {
		n1 = newNode("apple", 1, null, null);
		assertCheckInRange(-1, n1, "apple", "ball", n1, null);
	}
	
	public void testC18() {
		n1 = newNode("apple", 1, null, null);
		assertCheckInRange(-1, n1, "all", "apple", n1, null);
	}
	
	public void testC19() {
		n1 = newNode("apple", 1, null, null);
		assertCheckInRange(-1, n1, null, "a", null, n1);
	}
	
	public void testC20() {
		n1 = newNode("apple", 8, null, null);
		n2 = newNode("orange", 4, null, null);
		assertCheckInRange(-1, n1, "A", null, n1, n2);
	}
	
	public void testC21() {
		n1 = newNode("apple", 8, null, null);
		n2 = newNode("orange", 4, null, null);
		n1.setNext(n2);
		assertCheckInRange(-1, n1, "A", null, n1, null);
	}

	public void testC22() {
		n1 = newNode("apple", 8, null, null);
		n2 = newNode("orange", 4, null, null);
		n4 = newNode("orange", 4, null, null);
		n1.setNext(n2);
		assertCheckInRange(-1, n1, "A", null, n1, n4);
	}
	
	public void testC23() {
		n1 = newNode("apple", 8, null, null);
		n2 = newNode("orange", 4, null, null);
		n3 = newNode("apple", 8, null, null);
		n1.setNext(n2);
		n3.setNext(n2);
		assertCheckInRange(-1, n1, "A", null, n3, n2);
	}

	
	public void testC30() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 4, null, n1);
		n2.setNext(n1);
		assertCheckInRange(2, n2, null, null, n2, null);
	}
	
	public void testC31() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 4, null, n1);
		assertCheckInRange(-1, n2, null, null, n2, null);
	}
	
	public void testC32() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 4, null, n1);
		assertCheckInRange(-1, n2, null, null, n2, n1);
	}

	public void testC33() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 4, null, n1);
		n3 = newNode("orange", 2, null, null);
		n2.setNext(n3);
		assertCheckInRange(-1, n2, null, null, n2, null);
	}

	public void testC34() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 4, null, n1);
		n3 = newNode("pear", 2, null, null);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, null, null, n2, n3);
	}

	public void testC35() {
		n1 = newNode("orange", 0, null, null);
		n2 = newNode("apple", 4, null, n1);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, null, null, n2, null);
	}
	
	public void testC36() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", -1, null, n1);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, null, null, n2, null);
	}

	public void testC37() {
		n0 = newNode("banana", 0, null, null);
		n1 = newNode("orange", 5, null, null);
		n2 = newNode("apple", 3, null, n1);
		n2.setNext(n1);
		n1.setNext(n0);
		assertCheckInRange(2, n2, null, "pear", n2, n0);
	}

	public void testC38() {
		n0 = newNode("banana", 0, null, null);
		n1 = newNode("orange", 5, null, null);
		n2 = newNode("apple", 3, null, n1);
		n2.setNext(n1);
		n1.setNext(n0);
		assertCheckInRange(-1, n2, null, "pear", n2, null);
	}
	
	public void testC39() {
		n0 = newNode("banana", 0, null, null);
		n1 = newNode("orange", 5, null, null);
		n2 = newNode("apple", 3, null, n1);
		n2.setNext(n0);
		n1.setNext(n0);
		assertCheckInRange(-1, n2, null, "pear", n2, n0);
	}
	
	public void testC40() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 1, null, n1);
		n2.setNext(n1);
		assertCheckInRange(2, n2, null, "pear", n2, null);
	}
	
	public void testC41() {
		n0 = newNode(null, -1, null, null);
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 1, null, n1);
		n2.setNext(n1);
		n1.setNext(n0);
		assertCheckInRange(2, n2, "all", "set", n2, n0);
	}
	
	public void testC42() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 1, null, n1);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, "all", "ball", n2, null);
	}
	
	public void testC43() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 1, null, n1);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, "all", "orange", n2, null);
	}
	
	public void testC44() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 1, null, n1);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, null, "lemon", n2, null);
	}
	
	public void testC45() {
		n1 = newNode(null, 0, null, null);
		n2 = newNode("apple", 1, null, n1);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, null, null, n2, null);
	}
	
	public void testC46() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("apple", 1, null, n1);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, null, null, n2, null);
	}
	
	public void testC47() {
		n1 = newNode("orange", 2, null, null);
		n2 = newNode("apple", 1, n1, null);
		n1.setNext(n2);
		assertCheckInRange(-1, n2, null, null, n1, null);
	}
	
	public void testC48() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("apple", 1, n1, null);
		n1.setNext(n2);
		assertCheckInRange(-1, n2, null, null, n1, null);
	}
	
	public void testC49() {
		n1 = newNode("orange", -2, null, null);
		n2 = newNode("apple", 1, null, n1);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, "all", "ball", n1, null);		
	}
	
	
	public void testC50() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("pear", 3, n1, null);
		n1.setNext(n2);
		assertCheckInRange(2, n2, null, null, n1, null);
	}
	
	public void testC51() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("pear", 3, n1, null);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, null, null, n2, null);
	}
	
	public void testC52() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("pear", 3, n1, null);
		assertCheckInRange(-1, n2, null, null, n1, null);
	}

	public void testC53() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("pear", 3, n1, null);
		n1.setNext(n2);
		assertCheckInRange(-1, n2, null, null, n2, null);
	}

	public void testC54() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("pear", 3, n1, null);
		n1.setNext(n2);
		n2.setNext(n1);
		assertCheckInRange(2, n2, null, null, n1, n1);
	}

	public void testC55() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("pear", 3, n1, null);
		n1.setNext(n2);
		n2.setNext(n1);
		assertCheckInRange(-1, n2, null, null, n1, null);
	}

	public void testC56() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("pear", 3, n1, null);
		n3 = newNode("banana", 0, null, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(2, n2, "act", null, n1, n3);
	}

	public void testC57() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("pear", 3, n1, null);
		n3 = newNode("banana", 0, null, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n2, "act", null, n1, null);
	}
	
	public void testC58() {
		n1 = newNode("apple", -1, null, null);
		n2 = newNode("pear", 3, n1, null);
		n3 = newNode("banana", 0, null, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n2, "act", null, n1, n3);
	}

	public void testC59() {
		n1 = newNode("apple", 1, null, null);
		n2 = newNode("pear", 3, n1, null);
		n3 = newNode("banana", 0, null, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n2, "act", null, n2, n3);
	}
	
	public void testC60() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, n1, null);
		n3 = newNode("quince", 1, n2, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(2, n2, null, "quince", n1, n3);
	}

	public void testC61() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, n1, null);
		n3 = newNode("quince", 1, n2, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n2, null, "quince", n1, n2);
	}

	public void testC62() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, n1, null);
		n3 = newNode("quince", 1, n2, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n2, null, "quince", n1, null);
	}

	public void testC63() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, n1, null);
		n1.setNext(n2);
		assertCheckInRange(2, n2, "act", "raspberry", n1, null);
	}
	
	public void testC64() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, n1, null);
		n1.setNext(n2);
		assertCheckInRange(-1, n2, "date", null, n1, null);
	}
	
	public void testC65() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, n1, null);
		n1.setNext(n2);
		assertCheckInRange(-1, n2, null, "orange", n1, null);
	}
	
	public void testC66() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, n1, null);
		n1.setNext(n2);
		assertCheckInRange(-1, n2, "date", "orance", n1, null);
	}
	
	public void testC67() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, n1, null);
		n1.setNext(n2);
		assertCheckInRange(-1, n2, "apple", "raspberry", null, null);
	}
	
	public void testC68() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, n1);
		n3 = newNode("raspberry", 0, null, n2);
		n1.setNext(n2);
		assertCheckInRange(-1, n2, null, "raspberry", n1, n3);
	}
	
	public void testC69() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, n1, null);
		n3 = newNode("raspberry", 0, null, n2);
		n1.setNext(n2);
		n2.setNext(n3);
		n1.setRight(n2);
		assertCheckInRange(-1, n2, null, "quince", n1, null);
	}
	
	public void testC70() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n1, n2);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(3, n3, null, null, n1, null);
	}
	
	public void testC71() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n1, n2);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, null, null, n1, n3);
	}
	
	public void testC72() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n1, n2);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, null, null, n1, n2);
	}
	
	public void testC73() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n1, n2);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n3, null, null, n1, null);
	}
	
	public void testC74() {
		n1 = newNode("apple", 0, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n1, n2);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, null, null, n1, null);
	}
	
	public void testC75() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n2, n1);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, null, null, n1, null);
	}
	
	public void testC76() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n1, n2);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, "date", null, n1, null);
	}
	
	public void testC77() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n1, n2);
		n4 = newNode("raspberry", 1, null, null);
		n1.setNext(n3);
		n3.setNext(n2);
		n2.setNext(n4);
		assertCheckInRange(-1, n3, "date", null, n1, null);
	}
	
	public void testC78() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n1, n2);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, null, "paper", n1, null);
	}
	
	public void testC79() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("pear", 3, null, null);
		n3 = newNode("orange", 1, n1, n2);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, "date", "paper", n1, null);
	}
	
	public void testC80() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("banana", 3, n1, null);
		n3 = newNode("orange", 1, n2, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(3, n3, null, "pear", n1, null);
	}
	
	public void testC81() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("banana", 3, n1, null);
		n3 = newNode("orange", 1, n2, null);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, null, "pear", n1, null);
	}
	
	public void testC82() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("banana", 3, n1, null);
		n3 = newNode("orange", 1, n2, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n3, "awful", "pear", n1, null);
	}
	
	public void testC83() {
		n1 = newNode("apple", 2, null, null);
		n2 = newNode("banana", 3, n1, null);
		n3 = newNode("orange", 1, n2, null);
		n4 = newNode("lemon", 0, null, null);
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		assertCheckInRange(3, n3, null, "pear", n1, n4);
	}
	
	public void testC84() {
		n1 = newNode("pear", 3, null, null);
		n2 = newNode("melon", 3, null, n1);
		n3 = newNode("grape", 2, null, n2);
		n3.setNext(n2);
		n2.setNext(n1);
		assertCheckInRange(3, n3, "cherry", "raspberry", n3, null);
	}
	
	public void testC85() {
		n1 = newNode("pear", 3, null, null);
		n2 = newNode("melon", 3, null, n1);
		n3 = newNode("grape", 2, null, n2);
		n4 = newNode("lemon", 0, null, null);
		n3.setNext(n2);
		n2.setNext(n1);
		n1.setNext(n4);
		assertCheckInRange(3, n3, "cherry", "raspberry", n3, n4);
	}
	
	public void testC86() {
		n1 = newNode("pear", 3, null, null);
		n2 = newNode("melon", 3, null, n1);
		n3 = newNode("grape", 2, null, n2);
		n3.setNext(n2);
		n2.setNext(n1);
		assertCheckInRange(-1, n3, "cherry", "orange", n3, null);
	}
	
	public void testC87() {
		n1 = newNode("pear", 3, null, null);
		n2 = newNode("melon", 3, null, n1);
		n3 = newNode("grape", 2, null, n2);
		n4 = newNode("lemon", 0, null, null);
		n3.setNext(n2);
		n2.setNext(n1);
		n1.setNext(n4);
		assertCheckInRange(-1, n3, "cherry", "orange", n3, n4);
	}
	
	public void testC88() {
		n1 = newNode("pear", 3, null, null);
		n2 = newNode("melon", 3, null, n1);
		n3 = newNode("grape", 2, null, n2);
		n3.setNext(n2);
		n2.setNext(n1);
		assertCheckInRange(-1, n3, null, "pear", n3, null);
	}
	
	public void testC89() {
		n1 = newNode("pear", 3, null, null);
		n2 = newNode("melon", 3, null, n1);
		n3 = newNode("grape", 2, null, n2);
		n4 = newNode("lemon", 0, null, null);
		n3.setNext(n2);
		n2.setNext(n1);
		n1.setNext(n4);
		assertCheckInRange(-1, n3, null, "pear", n3, n4);
	}
	
	public void testC90() {
		n1 = newNode("grape", 2, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n3);
		assertCheckInRange(3, n3, null, null, n2, null);
	}
	
	public void testC91() {
		n1 = newNode("grape", 2, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n3);
		assertCheckInRange(-1, n3, null, null, n1, null);
	}
	
	public void testC92() {
		n1 = newNode("grape", 2, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n3);
		assertCheckInRange(-1, n3, null, null, n3, null);
	}
	
	public void testC93() {
		n1 = newNode("grape", 2, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n4 = newNode("raspberry", 5, n3, null);
		n2.setNext(n1);
		n1.setNext(n3);
		assertCheckInRange(-1, n3, null, null, n2, n4);
	}
	
	public void testC94() {
		n1 = newNode("grape", 2, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n1.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n3, null, null, n2, null);
	}
	
	public void testC95() {
		n1 = newNode("grape", 2, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n3);
		assertCheckInRange(3, n3, "apple", "orange", n2, null);
	}
	
	public void testC96() {
		n1 = newNode("grape", 2, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n4 = newNode("raspberry", 5, n3, null);
		n2.setNext(n1);
		n1.setNext(n3);
		n3.setNext(n4);
		assertCheckInRange(3, n3, "apple", "orange", n2, n4);
	}
	
	public void testC97() {
		n1 = newNode("grape", 2, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n4 = newNode("raspberry", 5, n3, null);
		n2.setNext(n1);
		n1.setNext(n3);
		n3.setNext(n4);
		assertCheckInRange(-1, n3, "apple", "orange", n2, null);
	}
	
	public void testC98() {
		n1 = newNode("grape", 2, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n4 = newNode("raspberry", 5, n3, null);
		n2.setNext(n1);
		n1.setNext(n3);
		n3.setNext(n4);
		assertCheckInRange(-1, n3, "apple", "orange", n2, n3);
	}
	
	public void testC99() {
		n1 = newNode("grape", 0, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n4 = newNode("raspberry", 5, n3, null);
		n2.setNext(n1);
		n1.setNext(n3);
		n3.setNext(n4);
		assertCheckInRange(-1, n3, "apple", "orange", n2, n4);
	}
	
	public void testD00() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n3);
		n3.setNext(n1);
		assertCheckInRange(-1, n3, "apple", "pear", n2, null);
	}
	
	public void testD01() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n3);
		assertCheckInRange(-1, n3, "apple", "pear", n2, null);
	}
	
	public void testD02() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n3);
		assertCheckInRange(-1, n3, null, null, n2, null);
	}
	
	public void testD03() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n3);
		n3.setNext(n1);
		assertCheckInRange(-1, n3, null, null, n2, null);
	}
	
	public void testD04() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 4, n1, null);
		n3 = newNode("grape", 2, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(3, n3, null, null, n3, null);
	}
	
	public void testD05() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 4, n1, null);
		n3 = newNode("grape", 2, null, n2);
		n4 = newNode("raspberry", 4, n3, null);
		n3.setNext(n1);
		n1.setNext(n2);
		n2.setNext(n4);
		assertCheckInRange(3, n3, null, null, n3, n4);
	}
	
	public void testD06() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 4, n1, null);
		n3 = newNode("grape", 2, null, n2);
		n4 = newNode("raspberry", 4, n3, null);
		n3.setNext(n1);
		n1.setNext(n2);
		n2.setNext(n4);
		assertCheckInRange(-1, n3, null, null, n3, null);
	}
	
	public void testD07() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 4, n1, null);
		n3 = newNode("grape", 2, null, n2);
		n4 = newNode("raspberry", 4, n3, null);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(-1, n3, null, null, n3, n4);
	}
	
	public void testD08() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 4, n1, null);
		n3 = newNode("grape", 2, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(3, n3, "apple", "raspberry", n3, null);
	}
	
	public void testD09() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 4, n1, null);
		n3 = newNode("grape", 2, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		n2.setNext(n2);
		assertCheckInRange(-1, n3, "apple", "raspberry", n3, null);
	}
	
	public void testD10() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 2, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(-1, n3, "apple", "raspberry", n3, null);
	}
	
	public void testD11() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(-1, n3, null, null, n3, null);
	}
	
	public void testD12() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 5, null, n2);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, null, null, n3, null);
	}
	
	public void testD13() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 5, null, n2);
		n1.setNext(n3);
		n3.setNext(n2);
		assertCheckInRange(-1, n3, null, null, n1, null);
	}
	
	public void testD14() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(-1, n3, "apple", "party", n3, null);
	}
	
	public void testD15() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(-1, n3, "lemon", "raspberry", n3, null);
	}

	public void testD20() {
		n0 = newNode(null, 0, null, null);
		n2 = newNode("melon", 3, null, n0);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n2);
		n2.setNext(n0);
		assertCheckInRange(-1, n3, "cherry", null, n3, null);
	}

	public void testD21() {
		n0 = newNode(null, 0, null, null);
		n2 = newNode("melon", 3, null, n0);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n2);
		// n2.setNext(n0);
		assertCheckInRange(-1, n3, "cherry", null, n3, null);
	}
	
	public void testD22() {
		n0 = newNode(null, 0, null, null);
		n2 = newNode("banana", 1, n0, null);
		n3 = newNode("orange", 8, n2, null);
		n0.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n3, null, null, n0, null);
	}
	
	public void testD23() {
		n0 = newNode(null, 0, null, null);
		n2 = newNode("banana", 1, n0, null);
		n3 = newNode("orange", 8, n2, null);
		n0.setNext(n2);
		n2.setNext(n3);
		assertCheckInRange(-1, n3, null, null, n2, null);
	}
	
	public void testD24() {
		n1 = newNode(null, 0, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n3);
		assertCheckInRange(-1, n3, "apple", "orange", n2, null);
	}
	
	public void testD25() {
		n1 = newNode(null, 0, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n3);
		n1.setNext(n3);
		assertCheckInRange(-1, n3, "apple", "orange", n2, null);
	}
	
	public void testD26() {
		n1 = newNode(null, 0, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(-1, n3, "apple", "raspberry", n3, null);
	}
	
	public void testD27() {
		n1 = newNode(null, 4, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(-1, n3, "apple", "raspberry", n3, null);
	}
	
	public void testD28() {
		n1 = newNode(new String("grape"), 3, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(-1, n3, "apple", "raspberry", n3, null);
	}
	
	public void testD29() {
		n1 = newNode("null", 1, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		assertCheckInRange(3, n3, "apple", "raspberry", n3, null);
	}
	
	public void testD30() {
		n1 = newNode("apple", 3, null, null);
		n2 = newNode("banana", 1, n1, null);
		n3 = newNode("orange", 8, n2, null);
		n1.setNext(n2);
		n2.setNext(n3);
		n1.setRight(n3);
		assertCheckInRange(-1, n3, null, "pear", n1, null);
	}
	
	public void testD31() {
		n1 = newNode("apple", 3, null, null);
		n2 = newNode("banana", 1, n1, null);
		n3 = newNode("orange", 8, n2, null);
		n1.setNext(n1);
		n2.setNext(n3);
		n1.setRight(n3);
		assertCheckInRange(-1, n3, null, "pear", n1, null);
	}
	
	public void testD32() {
		n1 = newNode("pear", 2, null, null);
		n2 = newNode("melon", 3, null, n1);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n2);
		n2.setNext(n1);
		n1.setLeft(n3);
		assertCheckInRange(-1, n3, "cherry", "raspberry", n3, null);
	}
	
	public void testD33() {
		n1 = newNode("pear", 2, null, null);
		n2 = newNode("melon", 3, null, n1);
		n3 = newNode("grape", 5, null, n2);
		n3.setNext(n2);
		n2.setNext(n3);
		n1.setLeft(n3);
		assertCheckInRange(-1, n3, "cherry", "raspberry", n3, null);
	}
	
	public void testD34() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 3, null, n2);
		n3.setNext(n1);
		n1.setNext(n2);
		n1.setLeft(n3);
		assertCheckInRange(-1, n3, "apple", "raspberry", n3, null);
	}
	
	public void testD35() {
		n1 = newNode("orange", 8, null, null);
		n2 = newNode("pear", 2, n1, null);
		n3 = newNode("grape", 3, null, n2);
		n3.setNext(n3);
		n1.setNext(n2);
		n1.setLeft(n3);
		assertCheckInRange(-1, n3, "apple", "raspberry", n3, null);
	}
	
	public void testD36() {
		n1 = newNode("grape", 5, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n3);
		assertCheckInRange(3, n3, null, null, n2, null);
	}
	
	public void testD37() {
		n1 = newNode("grape", 5, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n3);
		n1.setRight(n3);
		assertCheckInRange(-1, n3, "apple", "raspberry", n2, null);
	}
	
	public void testD38() {
		n1 = newNode("grape", 5, null, null);
		n2 = newNode("banana", 1, null, n1);
		n3 = newNode("lemon", 1, n2, null);
		n2.setNext(n1);
		n1.setNext(n2);
		n1.setRight(n3);
		assertCheckInRange(-1, n3, "apple", "raspberry", n2, null);
	}
	
	WordMultiset.Spy.Node n12, n34, n56, n78, n14, n58, n18;
	
	private WordMultiset.Spy.Node makeBig() {
		n1 = newNode("12", 1, null, null);
		n2 = newNode("21", 2, null, null);
		n3 = newNode("28", 3, null, null);
		n4 = newNode("42", 4, null, null);
		n5 = newNode("59", 5, null, null);
		n6 = newNode("68", 6, null, null);
		n7 = newNode("81", 7, null, null);
		n8 = newNode("92", 8, null, null);
		
		n12 = newNode("17", 12, n1, n2);
		n34 = newNode("33", 34, n3, n4);
		n56 = newNode("60", 56, n5, n6);
		n78 = newNode("85", 78, n7, n8);
		n14 = newNode("23", 14, n12, n34);
		n58 = newNode("75", 58, n56, n78);
		n18 = newNode("54", 18, n14, n58);
		
		n0 = newNode(null, 0, null, n18);
		n0.setNext(n1);
		
		n1.setNext(n12);
		n12.setNext(n2);
		n2.setNext(n14);
		n14.setNext(n3);
		n3.setNext(n34);
		n34.setNext(n4);
		n4.setNext(n18);
		n18.setNext(n5);
		n5.setNext(n56);
		n56.setNext(n6);
		n6.setNext(n58);
		n58.setNext(n7);
		n7.setNext(n78);
		n78.setNext(n8);
		
		return n18;
	}
	
	public void testE00() {
		n9 = makeBig();
		assertCheckInRange(15, n9, "10", "95", n1, null);
	}
	
	public void testE01() {
		n9 = makeBig();
		assertCheckInRange(-1, n9, "13", "95", n1, null);
	}
	
	public void testE02() {
		n9 = makeBig();
		n1.setString("19");
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE03() {
		n9 = makeBig();
		n2.setString("26");
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE04() {
		n9 = makeBig();
		n3.setString("22");
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE05() {
		n9 = makeBig();
		n4.setString("55");
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE06() {
		n9 = makeBig();
		n5.setString("51");
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE07() {
		n9 = makeBig();
		n6.setString("77");
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE08() {
		n9 = makeBig();
		n7.setString("73");
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE09() {
		n9 = makeBig();
		n8.setString("97");
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE10() {
		n9 = makeBig();
		WordMultiset.Spy.Node n10 = newNode("100", 0, null, null);
		n8.setNext(n10);
		assertCheckInRange(15, n9, "10", "95", n1, n10);
	}

	public void testE11() {
		n9 = makeBig();
		n1.setString(null);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE12() {
		n9 = makeBig();
		n2.setString(null);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE13() {
		n9 = makeBig();
		n3.setString(null);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE14() {
		n9 = makeBig();
		n4.setString(null);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE15() {
		n9 = makeBig();
		n5.setString(null);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void tesE16() {
		n9 = makeBig();
		n6.setString(null);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE17() {
		n9 = makeBig();
		n7.setString(null);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE18() {
		n9 = makeBig();
		n8.setString(null);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE21() {
		n9 = makeBig();
		n1.setRight(n9);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE22() {
		n9 = makeBig();
		n2.setRight(n9);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE23() {
		n9 = makeBig();
		n3.setRight(n9);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE24() {
		n9 = makeBig();
		n4.setRight(n9);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE25() {
		n9 = makeBig();
		n5.setLeft(n9);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE26() {
		n9 = makeBig();
		n6.setLeft(n9);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE27() {
		n9 = makeBig();
		n7.setLeft(n9);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE28() {
		n9 = makeBig();
		n8.setLeft(n9);
		assertCheckInRange(-1, n9, "10", "95", n1, null);
	}
	
	public void testE30() {
		n9 = makeBig();
		assertCheckInRange(-1, n0, null, null, n0, null);
	}
	
	public void testE31() {
		n9 = makeBig();
		n1.setNext(n2);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE32() {
		n9 = makeBig();
		n2.setNext(n3);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE33() {
		n9 = makeBig();
		n3.setNext(n4);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE34() {
		n9 = makeBig();
		n4.setNext(n5);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE35() {
		n9 = makeBig();
		n5.setNext(n6);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE36() {
		n9 = makeBig();
		n6.setNext(n7);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE37() {
		n9 = makeBig();
		n7.setNext(n8);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE38() {
		n9 = makeBig();
		n8.setNext(newNode("100", 1, null, null));
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE39() {
		n9 = makeBig();
		n9.setNext(null);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE40() {
		n9 = makeBig();
		n0.setCount(0);
		assertCheckInRange(15, n9, null, null, n1, null);
	}
	
	public void testE41() {
		n9 = makeBig();
		n1.setCount(0);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE42() {
		n9 = makeBig();
		n2.setCount(0);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE43() {
		n9 = makeBig();
		n3.setCount(0);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE44() {
		n9 = makeBig();
		n4.setCount(0);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE45() {
		n9 = makeBig();
		n5.setCount(0);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE46() {
		n9 = makeBig();
		n6.setCount(0);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE47() {
		n9 = makeBig();
		n7.setCount(0);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE48() {
		n9 = makeBig();
		n8.setCount(0);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	public void testE49() {
		n9 = makeBig();
		n9.setCount(0);
		assertCheckInRange(-1, n9, null, null, n1, null);
	}
	
	
	public void testW00() {
		self = spy.newInstance(null, 0, 14);
		assertWellFormed(false, self);
	}
	
	public void testW01() {
		self = spy.newInstance(null, -1, 14);
		assertWellFormed(false, self);
	}
	
	public void testW02() {
		n0 = newNode(null, 0, null, null);
		self = spy.newInstance(n0, 0, 14);
		assertWellFormed(true, self);
	}
	
	public void testW03() {
		n0 = newNode(null, 1, null, null);
		self = spy.newInstance(n0, 0, 14);
		assertWellFormed(false, self);
	}

	public void testW04() {
		n0 = newNode(null, 0, null, null);
		self = spy.newInstance(n0, 1, 14);
		assertWellFormed(false, self);
	}

	public void testW05() {
		n0 = newNode(null, 0, null, null);
		self = spy.newInstance(n0, -1, 14);
		assertWellFormed(false, self);
	}
	
	public void testW06() {
		n1 = newNode("", 1, null, null);
		n0 = newNode(null, 0, n1, null);
		self = spy.newInstance(n0, 0, 14);
		assertWellFormed(false, self);
	}
	
	public void testW07() {
		n1 = newNode("", 1, null, null);
		n0 = newNode(null, 0, n1, null);
		self = spy.newInstance(n0, 1, 14);
		assertWellFormed(false, self);
	}

	public void testW08() {
		n1 = newNode("", 0, null, null);
		self = spy.newInstance(n1, 0, 14);
		assertWellFormed(false, self);
	}

	public void testW09() {
		n1 = newNode("", 0, null, null);
		self = spy.newInstance(n1, 1, 14);
		assertWellFormed(false, self);
	}

	public void testW10() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		n0.setNext(n1);
		self = spy.newInstance(n0, 1, 14);
		assertWellFormed(true, self);
	}

	public void testW11() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		self = spy.newInstance(n0, 1, 14);
		assertWellFormed(false, self);
	}

	public void testW12() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, n1, n1);
		n0.setNext(n1);
		self = spy.newInstance(n0, 1, 14);
		assertWellFormed(false, self);
	}

	public void testW13() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		n0.setNext(n1);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}

	public void testW20() {
		n1 = newNode("hello", 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(true, self);
	}

	public void testW21() {
		n1 = newNode("hello", 0, null, null);
		n2 = newNode("goodbye", 0, null, n1);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		self = spy.newInstance(n0, 1, 14);
		assertWellFormed(false, self);
	}

	public void testW22() {
		n1 = newNode("hello", 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n3 = newNode("persimmon", 1, null, null);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		n1.setNext(n3);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}
	
	public void testW23() {
		n1 = newNode("hello", 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n1);
		n1.setNext(n2);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}
	
	public void testW24() {
		n1 = newNode("hello", -1, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}
	
	public void testW25() {
		n1 = newNode("hello", 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		self = spy.newInstance(n0, 3, 14);
		assertWellFormed(false, self);
	}
	
	public void testW26() {
		n1 = newNode("hello", 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n0 = newNode(null, 1, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}

	public void testW27() {
		n1 = newNode("hello", 4, null, null);
		n2 = newNode("goodbye", 6, n1, null);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}

	public void testW28() {
		n1 = newNode("hello", 4, null, null);
		n2 = newNode("goodbye", 6, n1, null);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n1);
		n1.setNext(n2);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}

	public void testW29() {
		n1 = newNode("hello", 4, null, null);
		n2 = newNode("goodbye", 6, n1, null);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n1);
		n1.setNext(n2);
		self = spy.newInstance(n2, -1, 14);
		assertWellFormed(false, self);
	}
	
	public void testW30() {
		n1 = newNode("hello", 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n1);
		n1.setNext(n2);
		n1.setLeft(n2);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}
	
	public void testW31() {
		n1 = newNode(null, 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}
	
	public void testW32() {
		n1 = newNode(null, 0, null, null);
		n2 = newNode("goodbye", 6, n1, null);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n1);
		n1.setNext(n2);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}
	
	public void testW33() {
		n1 = newNode(null, 4, null, null);
		n2 = newNode("goodbye", 6, n1, null);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n1);
		n1.setNext(n2);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}
	
	public void testW34() {
		n1 = newNode(null, 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		self = spy.newInstance(n2, -1, 14);
		assertWellFormed(false, self);
	}
	
	public void testW35() {
		n1 = newNode("null", 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(true, self);
	}
	
	public void testW36() {
		n1 = newNode("null", 4, null, null);
		n2 = newNode("goodbye", 6, null, n1);
		n3 = newNode("world", 1, null,null);
		n0 = newNode(null, 0, null, n2);
		n0.setNext(n2);
		n2.setNext(n1);
		n1.setNext(n3);
		self = spy.newInstance(n0, 2, 14);
		assertWellFormed(false, self);
	}
	
	public void testW50() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		assertWellFormed(true, self);
	}

	public void testW51() {
		n9 = makeBig();
		self = spy.newInstance(n0, 14, 14);
		assertWellFormed(false, self);
	}

	public void testW52() {
		n9 = makeBig();
		self = spy.newInstance(n0, 30, 14);
		assertWellFormed(false, self);
	}
	
	public void testW53() {
		n9 = makeBig();
		self = spy.newInstance(n0, 15, 14);
		n4.setString(null);
		assertWellFormed(false, self);
	}
	
	public void testW54() {
		n9 = makeBig();
		self = spy.newInstance(n0, -1, 14);
		n4.setString(null);
		assertWellFormed(false, self);
	}
	
	public void testW55() {
		n9 = makeBig();
		self = spy.newInstance(n0, 15, 14);
		n4.setString(new String("54"));
		assertWellFormed(false, self);
	}
	
	public void testW56() {
		n9 = makeBig();
		self = spy.newInstance(n0, -1, 14);
		n4.setString(new String("54"));
		assertWellFormed(false, self);
	}

	public void testW57() {
		n9 = makeBig();
		self = spy.newInstance(n0, 15, 14);
		n4.setRight(n9);
		assertWellFormed(false, self);
	}

	public void testW58() {
		n9 = makeBig();
		self = spy.newInstance(n0, -1, 14);
		n4.setRight(n9);
		assertWellFormed(false, self);
	}

	public void testW59() {
		n9 = makeBig();
		self = spy.newInstance(n0, 15, 14);
		n1.setRight(n9);
		n2.setRight(n9);
		n3.setRight(n9);
		n5.setLeft(n9);
		n6.setLeft(n9);
		n7.setLeft(n9);
		assertWellFormed(false, self);
	}
	
	public void testW60() {
		n9 = makeBig();
		self = spy.newInstance(n0, 15, 14);
		n0.setNext(n9);
		assertWellFormed(false, self);
	}
	
	public void testW61() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		n1.setNext(n2);
		assertWellFormed(false, self);
	}
	
	public void testW62() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		n2.setNext(n3);
		assertWellFormed(false, self);
	}
	
	public void testW63() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		n3.setNext(n4);
		assertWellFormed(false, self);
	}
	
	public void testW64() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		n4.setNext(n0);
		assertWellFormed(false, self);
	}
	
	public void testW65() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		n5.setNext(n6);
		assertWellFormed(false, self);
	}
	
	public void testW66() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		n6.setNext(n7);
		assertWellFormed(false, self);
	}
	
	public void testW67() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		n7.setNext(n8);
		assertWellFormed(false, self);
	}
	
	public void testW68() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		n8.setNext(n0);
		assertWellFormed(false, self);
	}
	
	public void testW69() {
		makeBig();
		self = spy.newInstance(n0, 15, 14);
		n0.setNext(null);
		assertWellFormed(false, self);
	}
	
	
	public void testX00() {
		n0 = newNode(null, 0, null, null);
		assertNull(spy.getNode(null, "hello", false, n0));
	}
	
	public void testX01() {
		n0 = newNode(null, 0, null, null);
		Object n = spy.getNode(null, "hello", true, n0);
		
		assertEquals(newNode("hello", 0, null, null), n);
		
		n1 = newNode(null, 0, null, null);
		n1.setRight(n);
		n1.setNext(n);
		assertEquals(n1, n0);
	}
	
	public void testX02() {
		n1 = newNode("world", 3, null, null);
		n2 = newNode("bye", 2, null, n1, n1);
		
		Object res = spy.getNode(null, "hello", true, n2);
		assertEquals(newNode("hello", 0, null, null, n1), res);
		
		assertEquals(newNode("world", 3, res, null, null), n1);
		assertEquals(newNode("bye", 2, null, n1, res), n2);
	}
	
	public void testX03() {
		// don't look at keys or count of parents!
		n1 = newNode(null, 0, null, null);
		n2 = newNode(null, 0, null, n1, n1);
		
		Object res = spy.getNode(null, "hello", true, n2);
		assertEquals(newNode("hello", 0, null, null, n1), res);
		
		assertEquals(newNode(null, 0, res, null, null), n1);
		assertEquals(newNode(null, 0, null, n1, res), n2);
	}
	
	public void testX04() {
		n0 = newNode("lovely", 1, null, null);
		n1 = newNode("world", 3, n0, null);
		n2 = newNode("bye", 2, null, n1, n0);
		
		Object res = spy.getNode(null, "hello", true, n2);
		assertEquals(newNode("hello", 0, null, null, n0), res);

		assertEquals(newNode("lovely", 1, res, null, null), n0);
		assertEquals(newNode("world", 3, n0, null, null), n1);
		assertEquals(newNode("bye", 2, null, n1, res), n2);
	}
	
	public void testX05() {
		// Don't look at the strings in other nodes!
		n0 = newNode("", 1, null, null);
		n1 = newNode("", 3, n0, null);
		n2 = newNode("", 2, null, n1, n0);
		
		Object res = spy.getNode(null, "hello", true, n2);
		assertEquals(newNode("hello", 0, null, null, n0), res);

		assertEquals(newNode("", 1, res, null, null), n0);
		assertEquals(newNode("", 3, n0, null, null), n1);
		assertEquals(newNode("", 2, null, n1, res), n2);
	}
	
	public void testX06() {
		n1 = newNode("good", 1, null, null);
		n2 = newNode("world", 3, n1, null);
		n1.setNext(n2);
		
		Object res = spy.getNode(null, "hello", true, n1);
		assertEquals(newNode("hello", 0, null, null, n2), res);
		
		assertEquals(newNode("good", 1, null, res, res), n1);
		assertEquals(newNode("world", 3, n1, null, null), n2);
	}
	
	public void testX07() {
		// Don't look at strings of other nodes
		n1 = newNode(null, 1, null, null);
		n2 = newNode(null, 3, n1, null);
		n1.setNext(n2);
		
		Object res = spy.getNode(null, "hello", true, n1);
		assertEquals(newNode("hello", 0, null, null, n2), res);
		
		assertEquals(newNode(null, 1, null, res, res), n1);
		assertEquals(newNode(null, 3, n1, null, null), n2);
	}
	
	public void testX08() {
		n1 = newNode(null, 1, null, null);
		n2 = newNode(null, 3, n1, null);
		n1.setNext(n2);
		
		assertNull(spy.getNode(null, "hello", false, n1));
		
		assertEquals(newNode(null, 1, null, null, n2), n1);
		assertEquals(newNode(null, 3, n1, null, null), n2);
	}
	
	public void testX09() {
		n0 = newNode("lovely", 1, null, null);
		n1 = newNode("world", 3, n0, null);
		n2 = newNode("bye", 2, null, n1, n0);
		
		assertNull(spy.getNode(null, "hello", false, n2));

		assertEquals(newNode("lovely", 1, null, null, null), n0);
		assertEquals(newNode("world", 3, n0, null, null), n1);
		assertEquals(newNode("bye", 2, null, n1, n0), n2);
	}
	
	public void testX10() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		n0.setNext(n1);
		
		assertNull(spy.getNode(n1, "bye", false, n0));
	}
	
	public void testX11() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		n0.setNext(n1);
		
		assertSame(n1, spy.getNode(n1, new String("hello"), false, n0));
	}
	
	public void testX12() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		n0.setNext(n1);
		
		assertNull(spy.getNode(n1, new String("world"), false, n0));
	}
	
	public void testX13() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		n0.setNext(n1);
		
		Object res = spy.getNode(n1, "bye", true, n0);

		assertEquals(newNode("bye", 0, null, null, n1), res);
		assertEquals(newNode(null, 0, null, n1, res), n0);
		assertEquals(newNode("hello", 2, res, null, null), n1);
	}
	
	public void testX14() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		n0.setNext(n1);
		
		assertSame(n1, spy.getNode(n1, new String("hello"), true, n0));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("hello", 2, null, null, null), n1);
	}
	
	public void testX15() {
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		n0.setNext(n1);
		
		Object res = spy.getNode(n1, "world", true, n0);

		assertEquals(newNode("world", 0, null, null, null), res);
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("hello", 2, null, res, res), n1);
	}
	
	public void testX16() {
		n2 = newNode("world", 1, null, null);
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n2);
		n2.setLeft(n1);
		n0.setNext(n1);
		n1.setNext(n2);
		
		Object res = spy.getNode(n1, "bye", true, n0);

		assertEquals(newNode("bye", 0, null, null, n1), res);
		assertEquals(newNode(null, 0, null, n2, res), n0);
		assertEquals(newNode("hello", 2, res, null, n2), n1);
		assertEquals(newNode("world", 1, n1, null, null), n2);
	}
	
	public void testX17() {
		n2 = newNode("bye", 5, null, null);
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n2);
		n2.setRight(n1);
		n2.setNext(n1);
		n0.setNext(n2);
		
		Object res = spy.getNode(n1, "world", true, n2);

		assertEquals(newNode("world", 0, null, null, null), res);
		assertEquals(newNode(null, 0, null, n2, n2), n0);
		assertEquals(newNode("hello", 2, null, res, res), n1);
		assertEquals(newNode("bye", 5, null, n1, n1), n2);		
	}
	
	public void testX18() {
		n2 = newNode("ye", 5, null, null);
		n1 = newNode("hello", 2, null, null);
		n0 = newNode(null, 0, null, n1);
		n2.setLeft(n1);
		n0.setNext(n1);
		n1.setNext(n2);
		
		Object res = spy.getNode(n1, "world", true, n0);

		assertEquals(newNode("world", 0, null, null, n2), res);
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("hello", 2, null, res, res), n1);
		assertEquals(newNode("ye", 5, n1, null, null), n2);	
	}
	
	public void testX20() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		assertNull(spy.getNode(n2, "apple", false, n0));
		
		assertEquals(newNode(null, 0, null, n2, n1), n0);
		assertEquals(newNode("banana", 1, null, null, n2), n1);
		assertEquals(newNode("grape", 8, n1, null, null), n2);
	}
	
	public void testX21() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		assertSame(n1, spy.getNode(n2, new String("banana"), false, n0));
		
		assertEquals(newNode(null, 0, null, n2, n1), n0);
		assertEquals(newNode("banana", 1, null, null, n2), n1);
		assertEquals(newNode("grape", 8, n1, null, null), n2);
	}
	
	public void testX22() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		assertNull(spy.getNode(n2, "date", false, n0));
		
		assertEquals(newNode(null, 0, null, n2, n1), n0);
		assertEquals(newNode("banana", 1, null, null, n2), n1);
		assertEquals(newNode("grape", 8, n1, null, null), n2);
	}
	
	public void testX23() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		assertSame(n2, spy.getNode(n2, new String("grape"), false, n0));
		
		assertEquals(newNode(null, 0, null, n2, n1), n0);
		assertEquals(newNode("banana", 1, null, null, n2), n1);
		assertEquals(newNode("grape", 8, n1, null, null), n2);
	}
	
	public void testX24() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		assertNull(spy.getNode(n2, "pear", false, n0));
		
		assertEquals(newNode(null, 0, null, n2, n1), n0);
		assertEquals(newNode("banana", 1, null, null, n2), n1);
		assertEquals(newNode("grape", 8, n1, null, null), n2);
	}
	
	public void testX25() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		Object res = spy.getNode(n2, "apple", true, n0);
		assertEquals(newNode("apple", 0, null, null, n1), res);
		
		assertEquals(newNode(null, 0, null, n2, res), n0);
		assertEquals(newNode("banana", 1, res, null, n2), n1);
		assertEquals(newNode("grape", 8, n1, null, null), n2);
	}
	
	public void testX26() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		assertSame(n1, spy.getNode(n2, new String("banana"), true, n0));
		
		assertEquals(newNode(null, 0, null, n2, n1), n0);
		assertEquals(newNode("banana", 1, null, null, n2), n1);
		assertEquals(newNode("grape", 8, n1, null, null), n2);
	}
	
	public void testX27() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		Object res = spy.getNode(n2, "date", true, n0);
		assertEquals(newNode("date", 0, null, null, n2), res);
		
		assertEquals(newNode(null, 0, null, n2, n1), n0);
		assertEquals(newNode("banana", 1, null, res, res), n1);
		assertEquals(newNode("grape", 8, n1, null, null), n2);
	}
	
	public void testX28() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		assertSame(n2, spy.getNode(n2, new String("grape"), true, n0));
		
		assertEquals(newNode(null, 0, null, n2, n1), n0);
		assertEquals(newNode("banana", 1, null, null, n2), n1);
		assertEquals(newNode("grape", 8, n1, null, null), n2);
	}
	
	public void testX29() {
		n1 = newNode("banana", 1, null, null);
		n2 = newNode("grape", 8, n1, null);
		n0 = newNode(null, 0, null, n2, n1);
		n1.setNext(n2);
		
		Object res = spy.getNode(n2, "pear", true, n0);
		assertEquals(newNode("pear", 0, null, null, null), res);
		
		assertEquals(newNode(null, 0, null, n2, n1), n0);
		assertEquals(newNode("banana", 1, null, null, n2), n1);
		assertEquals(newNode("grape", 8, n1, res, res), n2);
	}
	
	public void testX30() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		assertNull(spy.getNode(n1, "cherry", false, n0));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("lemon", 5, null, n2, n2), n1);
		assertEquals(newNode("quince", 7, null, null, null), n2);
	}
	
	public void testX31() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		assertSame(n1, spy.getNode(n1, "lemon", false, n0));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("lemon", 5, null, n2, n2), n1);
		assertEquals(newNode("quince", 7, null, null, null), n2);
	}
	
	public void testX32() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		assertNull(spy.getNode(n1, "orange", false, n0));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("lemon", 5, null, n2, n2), n1);
		assertEquals(newNode("quince", 7, null, null, null), n2);
	}
	
	public void testX33() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		assertSame(n2, spy.getNode(n1, "quince", false, n0));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("lemon", 5, null, n2, n2), n1);
		assertEquals(newNode("quince", 7, null, null, null), n2);
	}

	public void testX34() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		assertNull(spy.getNode(n1, "raspberry", false, n0));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("lemon", 5, null, n2, n2), n1);
		assertEquals(newNode("quince", 7, null, null, null), n2);
	}

	public void testX35() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		Object res = spy.getNode(n1, "cherry", true, n0);
		assertEquals(newNode("cherry", 0, null, null, n1), res);
		
		assertEquals(newNode(null, 0, null, n1, res), n0);
		assertEquals(newNode("lemon", 5, res, n2, n2), n1);
		assertEquals(newNode("quince", 7, null, null, null), n2);
	}
	
	public void testX36() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		assertSame(n1, spy.getNode(n1, "lemon", true, n0));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("lemon", 5, null, n2, n2), n1);
		assertEquals(newNode("quince", 7, null, null, null), n2);
	}
	
	public void testX37() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		Object res = spy.getNode(n1, "orange", true, n0);
		assertEquals(newNode("orange", 0, null, null, n2), res);
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("lemon", 5, null, n2, res), n1);
		assertEquals(newNode("quince", 7, res, null, null), n2);
	}
	
	public void testX38() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		assertSame(n2, spy.getNode(n1, "quince", true, n0));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("lemon", 5, null, n2, n2), n1);
		assertEquals(newNode("quince", 7, null, null, null), n2);
	}

	public void testX39() {
		n2 = newNode("quince", 7, null, null);
		n1 = newNode("lemon", 5, null, n2, n2);
		n0 = newNode(null, 0, null, n1, n1);
		
		Object res = spy.getNode(n1, "raspberry", true, n0);
		assertEquals(newNode("raspberry", 0, null, null, null), res);
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("lemon", 5, null, n2, n2), n1);
		assertEquals(newNode("quince", 7, null, res, res), n2);
	}

	
	
	public void testY00() {
		n0 = newNode(null, 0, null, null);
		self = spy.newInstance(n0, 0, 1);
		
		assertNull(spy.getNode(self, "hello", false));
		
		assertEquals(newNode(null, 0, null, null, null), n0);
	}
	
	public void testY01() {
		n0 = newNode(null, 0, null, null);
		self = spy.newInstance(n0, 0, 2);
		
		Object res = spy.getNode(self, "hello", true);
		
		assertEquals(newNode("hello", 0, null, null, null), res);
		assertEquals(newNode(null, 0, null, res, res), n0);
	}
	
	public void testY10() {
		n1 = newNode("hello", 3, null, null);
		n0 = newNode(null, 0, null, n1, n1);
		self = spy.newInstance(n0, 1, 3);
		
		assertNull(spy.getNode(self, "bye", false));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("hello", 3, null, null), n1);
	}
	
	public void testY11() {
		n1 = newNode("hello", 3, null, null);
		n0 = newNode(null, 0, null, n1, n1);
		self = spy.newInstance(n0, 1, 3);
		
		assertSame(n1, spy.getNode(self, "hello", false));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("hello", 3, null, null), n1);
	}
	
	public void testY12() {
		n1 = newNode("hello", 3, null, null);
		n0 = newNode(null, 0, null, n1, n1);
		self = spy.newInstance(n0, 1, 4);
		
		assertNull(spy.getNode(self, "world", false));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("hello", 3, null, null), n1);
	}
	
	public void testY13() {
		n1 = newNode("hello", 3, null, null);
		n0 = newNode(null, 0, null, n1, n1);
		self = spy.newInstance(n0, 1, 5);
		
		Object res = spy.getNode(self, "bye", true);
		
		assertEquals(newNode("bye", 0, null, null, n1), res);
		assertEquals(newNode(null, 0, null, n1, res), n0);
		assertEquals(newNode("hello", 3, res, null, null), n1);
	}
	
	public void testY14() {
		n1 = newNode("hello", 3, null, null);
		n0 = newNode(null, 0, null, n1, n1);
		self = spy.newInstance(n0, 1, 6);
		
		assertSame(n1, spy.getNode(self, "hello", true));
		
		assertEquals(newNode(null, 0, null, n1, n1), n0);
		assertEquals(newNode("hello", 3, null, null), n1);
	}

	
	protected void checkBigTree(BitSet exc) {
		if (!exc.get(0)) assertEquals(newNode(null, 0, null, n18, n1), n0);
		
		if (!exc.get(1)) assertEquals(newNode("12", 1, null, null, n12), n1);
		if (!exc.get(2)) assertEquals(newNode("21", 2, null, null, n14), n2);
		if (!exc.get(3)) assertEquals(newNode("28", 3, null, null, n34), n3);
		if (!exc.get(4)) assertEquals(newNode("42", 4, null, null, n18), n4);
		if (!exc.get(5)) assertEquals(newNode("59", 5, null, null, n56), n5);
		if (!exc.get(6)) assertEquals(newNode("68", 6, null, null, n58), n6);
		if (!exc.get(7)) assertEquals(newNode("81", 7, null, null, n78), n7);
		if (!exc.get(8)) assertEquals(newNode("92", 8, null, null, null), n8);
		
		if (!exc.get(12)) assertEquals(newNode("17", 12, n1, n2, n2), n12);
		if (!exc.get(34)) assertEquals(newNode("33", 34, n3, n4, n4), n34);
		if (!exc.get(56)) assertEquals(newNode("60", 56, n5, n6, n6), n56);
		if (!exc.get(78)) assertEquals(newNode("85", 78, n7, n8, n8), n78);
		if (!exc.get(14)) assertEquals(newNode("23", 14, n12, n34, n3), n14);
		if (!exc.get(58)) assertEquals(newNode("75", 58, n56, n78, n7), n58);
		if (!exc.get(18)) assertEquals(newNode("54", 18, n14, n58, n5), n18);
	}
	
	protected BitSet b(int... vals) {
		BitSet result = new BitSet();
		for (int v : vals) {
			result.set(v);
		}
		return result;
	}
	
	public void testY20() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "07", false));
		checkBigTree(b());
	}
	
	public void testY21() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "14", false));
		checkBigTree(b());
	}
	
	public void testY22() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "19", false));
		checkBigTree(b());
	}
	
	public void testY23() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "22", false));
		checkBigTree(b());
	}
	
	public void testY24() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "25", false));
		checkBigTree(b());
	}
	
	public void testY25() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "30", false));
		checkBigTree(b());
	}
	
	public void testY26() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "36", false));
		checkBigTree(b());
	}
	
	public void testY27() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "48", false));
		checkBigTree(b());
	}
	
	public void testY28() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "56", false));
		checkBigTree(b());
	}
	
	public void testY29() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "59.5", false));
		checkBigTree(b());
	}

	public void testY30() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "64", false));
		checkBigTree(b());
	}
	
	public void testY31() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "72", false));
		checkBigTree(b());
	}
	
	public void testY32() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "80", false));
		checkBigTree(b());
	}
	
	public void testY33() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "84", false));
		checkBigTree(b());
	}
	
	public void testY34() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "90", false));
		checkBigTree(b());
	}
	
	public void testY35() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		assertNull(spy.getNode(self, "97", false));
		checkBigTree(b());
	}

	public void testY40() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n1, spy.getNode(self, "12", false));
		checkBigTree(b());
	}

	public void testY41() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n12, spy.getNode(self, "17", false));
		checkBigTree(b());
	}

	public void testY42() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n2, spy.getNode(self, "21", false));
		checkBigTree(b());
	}

	public void testY43() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n14, spy.getNode(self, "23", false));
		checkBigTree(b());
	}

	public void testY44() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n3, spy.getNode(self, "28", false));
		checkBigTree(b());
	}

	public void testY45() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n34, spy.getNode(self, "33", false));
		checkBigTree(b());
	}

	public void testY46() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n4, spy.getNode(self, "42", false));
		checkBigTree(b());
	}

	public void testY47() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n18, spy.getNode(self, "54", false));
		checkBigTree(b());
	}

	public void testY48() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n5, spy.getNode(self, "59", false));
		checkBigTree(b());
	}

	public void testY49() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n56, spy.getNode(self, "60", false));
		checkBigTree(b());
	}

	public void testY50() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n6, spy.getNode(self, "68", false));
		checkBigTree(b());
	}

	public void testY51() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n58, spy.getNode(self, "75", false));
		checkBigTree(b());
	}

	public void testY52() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n7, spy.getNode(self, "81", false));
		checkBigTree(b());
	}

	public void testY53() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n78, spy.getNode(self, "85", false));
		checkBigTree(b());
	}

	public void testY54() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n8, spy.getNode(self, "92", false));
		checkBigTree(b());
	}

	public void testY60() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n1, spy.getNode(self, "12", true));
		checkBigTree(b());
	}

	public void testY61() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n12, spy.getNode(self, "17", true));
		checkBigTree(b());
	}

	public void testY62() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n2, spy.getNode(self, "21", true));
		checkBigTree(b());
	}

	public void testY63() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n14, spy.getNode(self, "23", true));
		checkBigTree(b());
	}

	public void testY64() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n3, spy.getNode(self, "28", true));
		checkBigTree(b());
	}

	public void testY65() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n34, spy.getNode(self, "33", true));
		checkBigTree(b());
	}

	public void testY66() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n4, spy.getNode(self, "42", true));
		checkBigTree(b());
	}

	public void testY67() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n18, spy.getNode(self, "54", true));
		checkBigTree(b());
	}

	public void testY68() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n5, spy.getNode(self, "59", true));
		checkBigTree(b());
	}

	public void testY69() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n56, spy.getNode(self, "60", true));
		checkBigTree(b());
	}

	public void testY70() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n6, spy.getNode(self, "68", true));
		checkBigTree(b());
	}

	public void testY71() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n58, spy.getNode(self, "75", true));
		checkBigTree(b());
	}

	public void testY72() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n7, spy.getNode(self, "81", true));
		checkBigTree(b());
	}

	public void testY73() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n78, spy.getNode(self, "85", true));
		checkBigTree(b());
	}

	public void testY74() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);
		assertSame(n8, spy.getNode(self, "92", true));
		checkBigTree(b());
	}
	
	public void testY80() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "07", true);
		assertEquals(newNode("07", 0, null, null, n1), res);
		checkBigTree(b(0,1));
		assertEquals(newNode(null, 0, null, n18, res), n0);
		assertEquals(newNode("12", 1, res, null, n12), n1);
	}
	
	public void testY81() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "14", true);
		assertEquals(newNode("14", 0, null, null, n12), res);
		checkBigTree(b(1));
		assertEquals(newNode("12", 1, null, res, res), n1);
	}
	
	public void testY82() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "19", true);
		assertEquals(newNode("19", 0, null, null, n2), res);
		checkBigTree(b(12, 2));
		assertEquals(newNode("17", 12, n1, n2, res), n12);
		assertEquals(newNode("21", 2, res, null, n14), n2);
	}
	
	public void testY83() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "22", true);
		assertEquals(newNode("22", 0, null, null, n14), res);
		checkBigTree(b(2));
		assertEquals(newNode("21", 2, null, res, res), n2);
	}
	
	public void testY84() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "25", true);
		assertEquals(newNode("25", 0, null, null, n3), res);
		checkBigTree(b(14,3));
		assertEquals(newNode("23", 14, n12, n34, res), n14);
		assertEquals(newNode("28", 3, res, null, n34), n3);
	}
	
	public void testY85() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "30", true);
		assertEquals(newNode("30", 0, null, null, n34), res);
		checkBigTree(b(3));
		assertEquals(newNode("28", 3, null, res, res), n3);
	}
	
	public void testY86() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "36", true);
		assertEquals(newNode("36", 0, null, null, n4), res);
		checkBigTree(b(34, 4));
		assertEquals(newNode("33", 34, n3, n4, res), n34);
		assertEquals(newNode("42", 4, res, null, n18), n4);
	}
	
	public void testY87() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "48", true);
		assertEquals(newNode("48", 0, null, null, n18), res);
		checkBigTree(b(4));
		assertEquals(newNode("42", 4, null, res, res), n4);
	}
	
	public void testY88() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "56", true);
		assertEquals(newNode("56", 0, null, null, n5), res);
		checkBigTree(b(18, 5));
		assertEquals(newNode("54", 18, n14, n58, res), n18);
		assertEquals(newNode("59", 5, res, null, n56), n5);
	}
	
	public void testY89() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "59.5", true);
		assertEquals(newNode("59.5", 0, null, null, n56), res);
		checkBigTree(b(5));
		assertEquals(newNode("59", 5, null, res, res), n5);
	}

	public void testY90() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "64", true);
		assertEquals(newNode("64", 0, null, null, n6), res);
		checkBigTree(b(56, 6));
		assertEquals(newNode("60", 56, n5, n6, res), n56);
		assertEquals(newNode("68", 6, res, null, n58), n6);
	}
	
	public void testY91() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "72", true);
		assertEquals(newNode("72", 0, null, null, n58), res);
		checkBigTree(b(6));
		assertEquals(newNode("68", 6, null, res, res), n6);
	}
	
	public void testY92() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "80", true);
		assertEquals(newNode("80", 0, null, null, n7), res);
		checkBigTree(b(58, 7));
		assertEquals(newNode("75", 58, n56, n78, res), n58);
		assertEquals(newNode("81", 7, res, null, n78), n7);
	}
	
	public void testY93() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "84", true);
		assertEquals(newNode("84", 0, null, null, n78), res);
		checkBigTree(b(7));
		assertEquals(newNode("81", 7, null, res, res), n7);
	}
	
	public void testY94() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);	
		Object res = spy.getNode(self, "90", true);
		assertEquals(newNode("90", 0, null, null, n8), res);
		checkBigTree(b(78, 8));
		assertEquals(newNode("85", 78, n7, n8, res), n78);
		assertEquals(newNode("92", 8, res, null, null), n8);
	}
	
	public void testY95() {
		makeBig();
		self = spy.newInstance(n0, 15, 50);		
		Object res = spy.getNode(self, "97", true);
		assertEquals(newNode("97", 0, null, null, null), res);
		checkBigTree(b(8));
		assertEquals(newNode("92", 8, null, res, res), n8);
	}
	
}