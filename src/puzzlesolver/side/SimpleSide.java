package puzzlesolver.side;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import puzzlesolver.Point;

/**
 * An implementation of {@code Side}, where each side is represented by a list of {@code Point}s.
 *
 * The coordinates of the {@code Point}s are on an axis relative to the side. The x-axis is the
 * line between the two endpoints of the side, and the y-axis is perpendicular to that, going away
 * from the center of the piece.
 *
 * <h4>Assertions:</h4>
 * <ul>
 *   <li><code>points.length > 1</code></li>
 * </ul>
 */
public final class SimpleSide implements Side {

  /**
   * List of points on the side.
   *
   * points[i].x = distance from left side points[i].y = height from side base
   */
  private final Point[] points;

  /**
   * The {@link SideType} of this {@link SimpleSide}.
   */
  private SideType sideType;

  /**
   * Constructs a new {@code SimpleSide} from the given points.
   *
   * @param points the series of points making up the side, relative to the piece.
   *
   *               These must be in the desired order along the side.
   */
  public SimpleSide(@NotNull Point... points) throws IllegalArgumentException {
    if (points.length < 2) {
      throw new IllegalArgumentException(String.format("Must have at least 2 points: %d found.",
                                                       points.length));
    }

    this.points = points.clone();
    Arrays.sort(this.points, 0, this.points.length, Comparator.<Point>naturalOrder());
  }

  /**
   * Calculate the {@link SideType} based on the points making up this {@link SimpleSide}.
   *
   * Should only be called by {@link #getSideType()}. All other functions should call
   * {@link this.getSideType()}.
   *
   * Assuming {@link #sideType} is currently {@code null}.
   *
   * @return the type of the side.
   */
  private SideType findSideType() {

    if (points.length == 2) {
      return sideType = SideType.FLAT;
    }

    final double maxHeight = points[0].y;

    for (int i = 1; i < points.length; i++) {
      if (points[i].y < maxHeight) {
        return sideType = SideType.IN;
      } else if (points[i].y > maxHeight) {
        return sideType = SideType.OUT;
      }
    }

    return sideType = SideType.FLAT;
  }

  /**
   * Compares this {@code SimpleSide} to the given {@code other} {@code SimpleSide}
   *
   * @param other the {@link Side} to compare this side to.
   * @return 0 if they are equivalent.
   * @throws NullPointerException if the {@code other} {@link Side} is {@code null}.
   * @throws ClassCastException if the {@link Side} given is not a {@link SimpleSide}.
   */
  @Override
  public int compareTo(@NotNull Side other) {
    Objects.requireNonNull(other);
    if (!SimpleSide.class.isInstance(other)) {
      throw new ClassCastException(String.format("Cannot compare %s to %s.", getClass().toString(),
                                                 other.getClass().toString()));
    }

    SimpleSide simpleOther = (SimpleSide) other;

    if (!getSideType().equals(other.getSideType())) {
      return getSideType().compareTo(other.getSideType());
    }

    if (getCornerDistance() == other.getCornerDistance()) {
      return Double.compare(getCornerDistance(), other.getCornerDistance());
    }

    Point[] otherPoints = simpleOther.getPoints();

    assert points != null && otherPoints != null;
    if (points.length != otherPoints.length) {
      return Integer.compare(points.length, otherPoints.length);
    }

    for (int point = 0; point < points.length; point++) {
      if (!points[point].equals(otherPoints[point])) {
        return points[point].compareTo(otherPoints[point]);
      }
    }

    return 0;
  }

  /**
   * Get a copy of the {@link Point}s, not the original {@link Point}s.
   *
   * @return a copy of the array of {@link Point}s representing this side
   */
  public Point[] getPoints() {
    Point[] copy = new Point[points.length];
    for (int i = 0; i < points.length; i++) {
        copy[i] = points[i].clone();
    }
    return copy;
  }

  @Override
  public double getCornerDistance() {
    return points[points.length - 1].x;
  }

  @Override
  public SideType getSideType() {
    return (sideType == null) ? findSideType() : sideType;
  }

  @Override
  public Side copy() {
    return new SimpleSide(getPoints());
  }
}
