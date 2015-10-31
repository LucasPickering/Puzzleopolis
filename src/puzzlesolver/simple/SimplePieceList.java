package puzzlesolver.simple;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import puzzlesolver.Piece;
import puzzlesolver.PieceComparator;
import puzzlesolver.PieceList;
import puzzlesolver.Side;
import puzzlesolver.enums.Direction;
import puzzlesolver.enums.PieceType;

public final class SimplePieceList implements PieceList {

  /*
   * INVARIANTS
   *
   * pieceLists
   * - Non-null
   * - Contents are non-null
   * - All 4 lists always have all the same elements; they only vary in order
   * - All 4 lists are always sorted using a PieceComparator for that list's corresponding direction
   * - Contents of all internal lists are non-null
   *
   * comparators
   * - Non-null
   * - Contents are non-null
   */

  private final ArrayList<Piece>[] pieceLists;
  private final PieceComparator[] comparators = new PieceComparator[4];

  /**
   * Constructs a new {@code SimplePieceList} with an initial capacity of ten.
   */
  public SimplePieceList() {
    this(10);
  }

  /**
   * Constructs a new {@code SimplePieceList} with the given initial capacity.
   *
   * @param capacity the initial capacity of the list (non-negative)
   * @throws IllegalArgumentException if capacity is negative
   */
  @SuppressWarnings("unchecked")
  public SimplePieceList(int capacity) {
    pieceLists = new ArrayList[4];
    for (int i = 0; i < pieceLists.length; i++) {
      pieceLists[i] = new ArrayList<>(capacity);
      comparators[i] = new PieceComparator(Direction.values()[i]);
    }
  }

  /**
   * Constructs a new {@code SimplePieceList} and adds all pieces in the given array.
   *
   * @param pieces the pieces to be added (non-null, does not contain null)
   */
  public SimplePieceList(@NotNull Piece[] pieces) {
    this(pieces.length);
    addAll(pieces);
  }

  /**
   * Constructs a new {@code SimplePieceList} from the given array of 4 lists of pieces.
   *
   * @param pieceLists the lists to construct this list from; this array should satisfy all
   *                   invariants for {@code this.pieceLists}
   */
  private SimplePieceList(@NotNull ArrayList<Piece>[] pieceLists) {
    Objects.requireNonNull(pieceLists);
    this.pieceLists = pieceLists;
  }

  @Override
  public void add(Piece p) {
    for (int i = 0; i < pieceLists.length; i++) {
      pieceLists[i].add(-(Collections.binarySearch(pieceLists[i], p, comparators[i]) + 1), p);
    }
  }

  @Override
  public void addAll(@NotNull Piece[] pieces) {
    Objects.requireNonNull(pieces);
    for (Piece p : pieces) {
      add(p);
    }
  }

  @Override
  public void remove(Piece p) {
    for (ArrayList<Piece> pieceList : pieceLists) {
      pieceList.remove(p);
    }
  }

  @Override
  public void clear() {
    for (ArrayList<Piece> pieceList : pieceLists) {
      pieceList.clear();
    }
  }

  @Override
  public Piece get(@NotNull Direction dir, int i) {
    Objects.requireNonNull(dir);
    if (i < 0 || i >= pieceLists[0].size()) {
      throw new IndexOutOfBoundsException("i is out of bounds");
    }
    return pieceLists[dir.ordinal()].get(i);
  }

  @Override
  public Piece first(@NotNull Direction dir) {
    Objects.requireNonNull(dir);
    return get(dir, 0);
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public int size() {
    return pieceLists[0].size();
  }

  @Override
  public boolean containsSide(Side s) {
    for (Direction dir : Direction.values()) {
      if (binarySearch(dir, s, PieceType.values()) > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int binarySearch(@NotNull Direction dir, Side s, PieceType... pieceTypes) {
    Objects.requireNonNull(dir);
    final Stream<Piece> stream = pieceLists[dir.ordinal()].stream();
    if (pieceTypes != null) {
      final List<PieceType> pieceTypesList = Arrays.asList(pieceTypes);
      stream.filter(pieceTypesList::contains);
    }
    return Collections
        .binarySearch(stream.map(p -> p.getSide(dir)).collect(Collectors.toList()), s);
  }

  @Override
  public Piece search(@NotNull Direction dir, Side s, PieceType... pieceTypes) {
    Objects.requireNonNull(dir);
    final int index = binarySearch(dir, s, pieceTypes);
    return index < 0 ? null : get(dir, index);
  }

  @Override
  public PieceList sublist(@NotNull PieceType... pieceTypes) {
    Objects.requireNonNull(pieceTypes);
    final List<PieceType> pieceTypesList = Arrays.asList(pieceTypes);
    @SuppressWarnings("unchecked")
    ArrayList<Piece>[] filteredPieceLists = new ArrayList[4];
    for (Direction dir : Direction.values()) {
      filteredPieceLists[dir.ordinal()] = pieceLists[dir.ordinal()].stream()
          .filter(pieceTypesList::contains).collect(Collectors.toCollection(ArrayList::new));
    }
    return new SimplePieceList(filteredPieceLists);
  }
}