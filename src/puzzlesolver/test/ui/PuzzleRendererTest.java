package puzzlesolver.test.ui;

import org.junit.Test;

import puzzlesolver.Constants;
import puzzlesolver.Point;
import puzzlesolver.enums.Direction;
import puzzlesolver.ui.fx.PuzzleRenderer;

import static org.junit.Assert.assertEquals;

public class PuzzleRendererTest {

  Point p0_0 = new Point(0, 0);

  @Test
  public void testGetRequiredWidth() throws Exception {

  }

  @Test
  public void testGetRequiredHeight() throws Exception {

  }

  @Test
  public void testGlobalPointFromLocalPoint() throws Exception {
    assertEquals(new Point(Constants.UI.VISUAL_PIECE_PADDING, Constants.UI.VISUAL_PIECE_PADDING - (Constants.UI.VISUAL_PIECE_HEIGHT / 2)),
                 PuzzleRenderer.globalPointFromLocalPoint(p0_0, Direction.NORTH, 0, 0));
    assertEquals(new Point(Constants.UI.VISUAL_PIECE_PADDING, Constants.UI.VISUAL_PIECE_PADDING + (Constants.UI.VISUAL_PIECE_HEIGHT / 2)),
                 PuzzleRenderer.globalPointFromLocalPoint(p0_0, Direction.SOUTH, 0, 0));
    assertEquals(new Point(Constants.UI.VISUAL_PIECE_PADDING + (Constants.UI.VISUAL_PIECE_WIDTH / 2), Constants.UI.VISUAL_PIECE_PADDING),
                 PuzzleRenderer.globalPointFromLocalPoint(p0_0, Direction.EAST, 0, 0));
    assertEquals(new Point(Constants.UI.VISUAL_PIECE_PADDING - (Constants.UI.VISUAL_PIECE_WIDTH / 2), Constants.UI.VISUAL_PIECE_PADDING),
                 PuzzleRenderer.globalPointFromLocalPoint(p0_0, Direction.WEST, 0, 0));

    assertEquals(Constants.UI.VISUAL_PIECE_WIDTH,
                 PuzzleRenderer.globalPointFromLocalPoint(p0_0, Direction.NORTH, 1, 0).x
                 - PuzzleRenderer.globalPointFromLocalPoint(p0_0, Direction.NORTH, 0, 0).x,
                 0.01);

  }
}
