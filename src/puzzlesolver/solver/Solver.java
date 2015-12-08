package puzzlesolver.solver;

import puzzlesolver.Piece;
import puzzlesolver.piecelist.PieceList;

public interface Solver {

  /**
   * Initializes this solver to use the given array of pieces.
   *
   * @param pieces the pieces that make up the puzzle, in any order
   */
  void init(Piece[] pieces);

  /**
   * Executes the next step in the solver, placing exactly one piece.
   *
   * @return false if more steps are needed to solve the puzzle, true if the solution is complete
   */
  boolean nextStep();

  /**
   * Gets the list of pieces that have not yet been placed into the solution. No copying is done, so
   * <i>please</i> don't screw with this object! Read only!
   *
   * @return all unplaced pieces
   */
  PieceList getUnplacedPieces();

  /**
   * Gets this solver's solution in its current place. All pieces in the solution will be in their
   * correct spot, with null spots for unplaced pieces. No copying is done, so <i>please</i> don't
   * screw with this object! Read only!
   *
   * @return a 2-D array representing all pieces that have been placed, in their final spots
   */
  Piece[][] getSolution();

  /**
   * Gets the x-value of the piece that this solver will work on next.
   * @return the x-value of the next spot to fill
   */
  int getX();

  /**
   * Gets the y-value of the piece that this solver will work on next.
   * @return the y-value of the next spot to fill
   */
  int getY();
}