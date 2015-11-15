package puzzlesolver.test.simple;

import org.junit.Test;

import puzzlesolver.Point;
import puzzlesolver.enums.SideType;
import puzzlesolver.Side;
import puzzlesolver.simple.SimpleSide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SimpleSideTest {
  Side s2 = new SimpleSide(new Point(0, 0),
                           new Point(5, 1),
                           new Point(10, 0));
  Side s3 = new SimpleSide(new Point(0, 0),
                           new Point(8.1, 0));
  Side s4 = new SimpleSide(new Point(0, 0),
                           new Point(5, 1),
                           new Point(10, 0));

  @Test
  public void testToString() throws Exception {
    System.out.println(s2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor() throws Exception {
    Side s1 = new SimpleSide();
  }

  @Test
  public void testCompareTo() throws Exception {
    assertEquals(0, s2.compareTo(s2));
    assertEquals(0, s3.compareTo(s3));
    assertNotEquals(0, s2.compareTo(s3));
    assertNotEquals(0, s3.compareTo(s2));
    assertEquals(0, s2.compareTo(s4));
    assertEquals(0, s4.compareTo(s2));
  }

  @Test
  public void testGetCornerDistance() throws Exception {
    assertEquals(10, s2.getCornerDistance(), 0.01);
    assertEquals(8.1, s3.getCornerDistance(), 0.01);
  }

  @Test
  public void testGetSideType() throws Exception {
    assertEquals(SideType.OUT, s2.getSideType());
    assertEquals(SideType.FLAT, s3.getSideType());
  }
}
