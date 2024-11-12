import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import edu.uwm.cs.junit.EfficiencyTestCase;
import edu.uwm.cs351.WordMultiset;
import edu.uwm.cs351.util.DefaultEntry;

public class TestEfficiency extends EfficiencyTestCase {

	private WordMultiset m;
	
	private Random random;
	
	private static final int POWER = 21; // million entries
	private static final int SIZE = (1 << (POWER-1)) - 1;
	private static final int TESTS = SIZE/POWER; 
	private static final int BASE = 10_000_000; // must be more than 1<<POWER
	
	protected String makeKey(int i) {
		return String.valueOf(BASE+i);
	}
	
	protected void setUp() {
		super.setUp();
		random = new Random();
		try {
			assert m.size() == TESTS : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		m = new WordMultiset();
		for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			int start = 1 << (power-1);
			for (int j=0; j < 1<<(POWER-power); ++j) {
				m.put(makeKey((j*incr+start)/2), Integer.valueOf(j*incr+start));
			}
		}
	}
		
	
	@Override
	protected void tearDown() {
		m = null;
		super.tearDown();
	}


	public void testA() {
		for (int i=0; i < SIZE; ++i) {
			assertEquals(SIZE,m.size());
		}
	}
	
	public void testB() {
		for (int i=0; i < TESTS; ++i) {
			assertFalse(m.add(makeKey(i+1)));
		}
	}

	public void testC() {
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(SIZE-1)+1;
			assertTrue(m.containsKey((Object)makeKey(r)));
			assertEquals(r*2,m.get((Object)makeKey(r)).intValue());
			assertNull(m.get((Object)makeKey(SIZE+1+r)));
			assertFalse(m.containsKey((Object)makeKey(SIZE+1+r)));
		}
	}
	
	public void testD() {
		Set<Integer> touched = new HashSet<Integer>();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(SIZE-1-i)+1;
			if (!touched.add(r)) continue;
			Integer val = m.remove(makeKey(r));
			assertFalse(val == null);
		}
		assertEquals(SIZE-touched.size(), m.size());
	}

	public void testE() {
		Set<Integer> touched = new HashSet<Integer>();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(SIZE-1)+1;
			if (!touched.add(r)) continue; // don't check again
			assertEquals(r*2,m.put(makeKey(r), 3).intValue());
			assertEquals(Integer.valueOf(3), m.get((Object)makeKey(r)));
		}
	}

	public void testF() {
		for (int i=0; i < TESTS; ++i) {
			assertTrue(m.removeOne(makeKey(i+1)));
			assertEquals(SIZE, m.entrySet().size());
		}
	}
	
	public void testG() {
		for (int i=1; i < TESTS; ++i) {
			assertFalse("should not contain bad entry for " + i,
					m.entrySet().contains(new DefaultEntry<>(makeKey(i*4),Integer.valueOf(i*4))));
			assertTrue("should contain entry for " + i,
					m.entrySet().contains((Object)new DefaultEntry<>(makeKey(i*4),Integer.valueOf(i*8))));
			assertFalse("should not contain non-existent entry for " + i,
					m.entrySet().contains(new DefaultEntry<>(makeKey(SIZE+1+i*4), Integer.valueOf(i*8))));	
		}
	}
	
	public void testH() {
		Set<Entry<String,Integer>> es = m.entrySet();
		for (int i=0; i < TESTS; ++i) {
			assertFalse("should not remove bad entry for " + i,
					es.remove(new DefaultEntry<>(makeKey(i*+1),Integer.valueOf(i*4+1))));
			assertTrue("should be able to remove entry for " + i, 
					es.remove((Object)new DefaultEntry<>(makeKey(i*2+1),Integer.valueOf(i*4+2))));	
			assertFalse("should not remove twice entry for " + i,
					es.remove(new DefaultEntry<>(makeKey(i*2+1),Integer.valueOf(i*4+2))));
		}
		assertEquals(SIZE-TESTS, m.size());
	}

	public void testI() {
		Set<Entry<String,Integer>> es = m.entrySet();
		for (int i=0; i < TESTS; ++i) {
			Iterator<Entry<String,Integer>> it= es.iterator();
			assertEquals(new DefaultEntry<>(makeKey(1),Integer.valueOf(2)), it.next());
		}
	}
	
	public void testJ() {
		Iterator<Entry<String,Integer>> it = m.entrySet().iterator();
		for (int i=1; i <= SIZE; ++i) {
			assertTrue("After " + i + " next(), should still have next",it.hasNext());
			assertEquals(new DefaultEntry<>(makeKey(i),Integer.valueOf(2*i)), it.next());
		}
	}
	
	public void testK() {
		int removed = 0;
		assertEquals(SIZE,m.size());
		Iterator<Entry<String,Integer>> it = m.entrySet().iterator();
		for (int i = 1; i < TESTS; ++i) {
			assertEquals(makeKey(i),it.next().getKey());
			if (random.nextBoolean()) {
				it.remove();
				++removed;
			}
		}
		assertEquals(SIZE-removed,m.size());
	}

	/// The remaining tests should be sufficiently efficient (since we deleted testN)
	/// since AbstractMap provides adequate definitions.
	
	public void testL() {
		for (int i=0; i < SIZE; ++i) {
			Set<String> s = m.keySet();
			assertEquals(SIZE, s.size());
		}
	}
	
	public void testM() {
		Set<String> s = m.keySet();
		for (int i=0; i < TESTS; ++i) {
			assertTrue(s.contains((Object)makeKey(i+1)));
		}
		assertEquals(SIZE, s.size());
	}
	
	/* reduce work for Fall 2024
	public void testN() {
		Set<String> s = m.keySet();
		int removed = 0;
		for (int i=0; i < TESTS; ++i) {
			if ((i%8) == 0) {
				++removed;
				assertTrue(s.remove((Object)makeKey(i+1)));
			} else {
				assertFalse(s.remove((Object)makeKey(-i)));
			}
		}
		assertEquals(SIZE - removed, s.size());
	}
	*/
	
	public void testO() {
		for (int i=1; i < TESTS; ++i) {
			Iterator<String> it = m.keySet().iterator();
			assertEquals(makeKey(1), it.next());
		}
	}

	public void testP() {
		Iterator<String> it = m.keySet().iterator();
		for (int i=0; i < SIZE; ++i) {
			assertTrue("After " + i + " next(), should still have next",it.hasNext());
			it.next();
		}
	}
	
	public void testQ() {
		int removed = 0;
		assertEquals(SIZE,m.size());
		Iterator<String> it = m.keySet().iterator();
		for (int i = 1; i < TESTS; ++i) {
			assertEquals(makeKey(i),it.next());
			if (random.nextBoolean()) {
				it.remove();
				++removed;
			}
		}
		assertEquals(SIZE-removed,m.size());
	}
	
	public void testR() {
		for (int i=0; i < SIZE; ++i) {
			assertEquals(SIZE, m.values().size());
		}
	}
	
	public void testS() {
		Iterator<Integer> it = m.values().iterator();
		for (int i=0; i < SIZE; ++i) {
			assertTrue("After " + i + " next(), should still have next",it.hasNext());
			it.next();
		}
	}
	
	public void testT() {
		Iterator<Integer> it = m.values().iterator();
		int removed = 0;
		for (int i=1; i < TESTS; ++i) {
			if ((i%2) == 0) {
				assertEquals(i*2, it.next().intValue());
				it.remove();
				++removed;
			} else {
				it.next();
			}
		}
		assertEquals(SIZE - removed, m.values().size());
	}
	
	public void testU() {
		String k = makeKey(SIZE/2);
		for (int i=0; i < SIZE-1; ++i) {
			assertTrue(m.removeOne(k));
		}
		assertFalse(m.removeOne(k));
		assertEquals(SIZE-1, m.size());
	}
}
