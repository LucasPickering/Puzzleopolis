package puzzlesolver.ui.fx_2d;

import org.apache.commons.lang3.ArrayUtils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import puzzlesolver.Piece;
import puzzlesolver.Point;
import puzzlesolver.Side;
import puzzlesolver.Solver;
import puzzlesolver.arrays.PointsBuilder;
import puzzlesolver.constants.UIConstants;
import puzzlesolver.enums.Direction;
import puzzlesolver.simple.SimpleSide;

public class PuzzleRenderer {

  private Paint[] colors = new Paint[]{Color.NAVY, Color.DARKSLATEBLUE,
                                       Color.DARKGOLDENROD, Color.DARKOLIVEGREEN,
                                       Color.MAROON, Color.DARKVIOLET,
                                       Color.DARKCYAN, Color.DARKORCHID};

  /**
   * Get a global point from a local point.
   *
   * @param localPoint   local point to calculate from
   * @param orientation  direction of parent side
   * @param pieceX       x-position in 2D points array
   * @param pieceY       y-position in 2D points array
   * @param windowWidth  width of window to render in
   * @param windowHeight width of window to render in
   * @return global point
   */
  public static Point getGlobalPoint(Point localPoint, Direction orientation, int pieceX,
                                     int pieceY, int puzzleWidth, int puzzleHeight,
                                     double windowWidth, double windowHeight) {
    // May have to scale. For now, made a placeholder.
    final double scaledPaddingX = UIConstants.VISUAL_PIECE_WIDTH;
    final double scaledPaddingY = UIConstants.VISUAL_PIECE_HEIGHT;

    final double scaledPieceWidth = (windowWidth - (scaledPaddingX * 2)) / puzzleWidth;
    final double scaledPieceHeight = (windowHeight - (scaledPaddingY * 2)) / puzzleHeight;

    final double scaleX = scaledPieceWidth / UIConstants.VISUAL_PIECE_WIDTH;
    final double scaleY = scaledPieceHeight / UIConstants.VISUAL_PIECE_HEIGHT;

    final double pieceGlobalX = scaledPaddingX + pieceX * scaledPieceWidth;
    final double pieceGlobalY = scaledPaddingY + pieceY * scaledPieceHeight;

    double pointGlobalX = pieceGlobalX;
    double pointGlobalY = pieceGlobalY;

    switch (orientation) {
      case NORTH:
        pointGlobalX += scaleX * localPoint.x;
        pointGlobalY -= scaleY * localPoint.y;
        break;
      case EAST:
        pointGlobalX += scaledPieceWidth;
        pointGlobalX += scaleX * localPoint.y;
        pointGlobalY += scaleY * localPoint.x;
        break;
      case SOUTH:
        pointGlobalY += scaledPieceHeight;
        pointGlobalX += scaleX * localPoint.x;
        pointGlobalY += scaleY * localPoint.y;
        break;
      case WEST:
        pointGlobalY += scaleY * localPoint.x;
        pointGlobalX -= scaleX * localPoint.y;
        break;
    }

    return new Point(pointGlobalX, pointGlobalY);
  }

  public void drawPuzzle(GraphicsContext gc, Solver solver) {
    gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

    Piece[][] solution = solver.getSolution();

    for (int x = 0; x < solution.length; x++) {
      for (int y = 0; y < solution[x].length; y++) {
        Piece piece = solution[x][y];

        if (piece != null) {
          drawPiece(gc, piece, x, y, solution.length, solution[x].length);
        }
      }
    }
  }

  public void drawPiece(GraphicsContext gc, Piece piece, int x, int y,
                        int puzzleWidth, int puzzleHeight) {
    gc.setStroke(colors[colors.length - 1 - piece.getPieceType().ordinal() % colors.length]);
    gc.setFill(colors[(piece.getPieceType().ordinal()) % colors.length]);
    gc.setLineJoin(StrokeLineJoin.ROUND);
    gc.setLineCap(StrokeLineCap.ROUND);
    double windowWidth = gc.getCanvas().getWidth();
    double windowHeight = gc.getCanvas().getHeight();

    gc.setLineWidth((windowWidth + windowHeight) / 2 / 200);

    PointsBuilder xs = new PointsBuilder();
    PointsBuilder ys = new PointsBuilder();

    for (Direction direction : Direction.values()) {
      Side s = piece.getSide(direction);
      if (s != null) {
        Point[] points = ((SimpleSide) s).getPoints();
        if (direction == Direction.SOUTH || direction == Direction.WEST) {
          Point temp = points[0];
          points[0] = points[points.length - 1];
          points[points.length - 1] = temp;
        }
        for (Point point : points) {
          Point globalPoint = PuzzleRenderer
            .getGlobalPoint(point, direction, x, y,
                            puzzleWidth, puzzleHeight,
                            windowWidth, windowHeight);

          xs.add(globalPoint.x);
          ys.add(globalPoint.y);
        }
      }
    }
    double[] primitiveXs = ArrayUtils.toPrimitive(xs.toPoints());
    double[] primitiveYs = ArrayUtils.toPrimitive(ys.toPoints());
    gc.fillPolygon(primitiveXs, primitiveYs, xs.size());
    gc.strokePolygon(primitiveXs, primitiveYs, xs.size());
  }


  public void drawNextPiece(GraphicsContext gc, Solver solver) {
    gc.setFill(Color.PALEVIOLETRED);
    gc.setStroke(Color.INDIANRED);

    Piece[][] solution = solver.getSolution();

    final double width = gc.getCanvas().getWidth();
    final double height = gc.getCanvas().getHeight();

    final double puzzleWidth = solution.length;
    final double puzzleHeight = solution[0].length;

    gc.fillRect(width * (puzzleWidth - 1) / puzzleWidth, height * (puzzleWidth - 1) / puzzleHeight,
                width / puzzleWidth, height / puzzleHeight);

    drawPiece(gc, solver.getUnplacedPieces());
  }
}
