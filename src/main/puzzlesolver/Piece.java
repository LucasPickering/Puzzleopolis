package puzzlesolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import puzzlesolver.constants.Constants;
import puzzlesolver.enums.Direction;
import puzzlesolver.enums.PieceType;
import puzzlesolver.side.Side;

/**
 * A class to represent a 4-sided puzzle piece.
 *
 * This piece has a type that MUST be determined during solution and CANNOT be provided to it or
 * determined during generation.
 *
 * This can also represent potential pieces, which are pieces generated by the solver which have 1-4
 * known sides and potentially multiple piece types. No matter what, this piece must have AT LEAST
 * ONE non-null side.
 */
public class Piece {

    /**
     * Sides are ordered in the same way as {@link Direction}: NORTH, EAST, SOUTH, WEST.
     */
    private Side[] sides;
    private PieceType[] pieceTypes;

    /**
     * Constructs a new Piece with the given 4 sides.
     *
     * @param sides array of sides, length MUST be 4
     */
    private Piece(Side[] sides) {
        this.sides = sides;
    }

    /**
     * Gets a clone of the side of this piece in the given direction.
     *
     * @param dir the direction of the piece to be retrieved
     * @return a clone of the side in the given direction
     */
    public Side getSide(Direction dir) {
        return sides[dir.ordinal()] != null ? sides[dir.ordinal()].copy() : null;
    }

    /**
     * Gets the {@link PieceType} type of this piece. This was calculated upon construction of the
     * object. The order of this array is GUARANTEED to match the order of {@link PieceType}.
     *
     * @return all possibly types for this piece
     */
    public PieceType[] getPieceTypes() {
        if (pieceTypes == null) {
            pieceTypes = findPieceTypes();
        }
        return pieceTypes.clone();
    }

    /**
     * Gets the type of this piece, if a has a single definite type. DO NOT call this in any
     * situation where this piece may have more than one possible type.
     *
     * @return the single, definite type of this piece
     * @throws IllegalStateException if there is no definite type for this piece
     */
    public PieceType getPieceType() {
        if (getPieceTypes().length == 1) {
            return getPieceTypes()[0];
        }
        throw new IllegalStateException("No single type for this piece!");
    }

    private PieceType[] findPieceTypes() {
        List<PieceType> types = new ArrayList<>(PieceType.values().length);
        for (PieceType pieceType : PieceType.values()) {
            if (pieceType.canBeType(this)) {
                types.add(pieceType);
            }
        }
        return types.toArray(new PieceType[types.size()]);
    }

    /**
     * Is this piece <i>definitely</i> of the given type? Specifcally, does this piece have exactly
     * one possible piece type, which is equal to the given piece type.
     *
     * @param pieceType the type to check for
     * @return true if the given type is the <i>only</i> type that this piece can be, false
     * otherwise
     */
    public boolean definitelyType(PieceType pieceType) {
        PieceType[] types = getPieceTypes();
        return types.length == 1 && types[0] == pieceType;
    }

    /**
     * Is the side in the given direction {@code null}?
     *
     * @param dir the direction of the side
     * @return true if the side in that direction is {@code null}, false otherwise
     */
    public boolean sideNull(Direction dir) {
        return sides[dir.ordinal()] == null;
    }

    /**
     * Indicates whether some other object is "equal to" this one. An object can only be equal to
     * this one if it is a piece and {@link Side#equals} returns true for all four {@link
     * Direction}s.
     *
     * @param o the other object
     * @return true if o is a Piece with identical sides, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Piece)) {
            return false;
        }

        Piece p = (Piece) o;

        for (int i = 0; i < Constants.NUM_SIDES; i++) {
            final Side otherSide = p.getSide(Direction.values()[i]);
            if (sides[i] != otherSide && sides[i] != null && !sides[i].equals(otherSide)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sides);
    }

    /**
     * Indicates whether the given piece <i>could</i> be equal to this one. Two pieces <i>could</i>
     * be equal if for each dir in {@link Direction}, the sides on each piece in that direction
     * match each other, <i>or</i>, one of those sides is {@code null}
     *
     * @param p the other piece
     * @return true if all sides match, with null being considered a match with anything, false
     * otherwise
     */
    public boolean maybeEquals(Piece p) {
        for (int i = 0; i < Constants.NUM_SIDES; i++) {
            final Side otherSide = p.getSide(Direction.values()[i]);
            if (sides[i] != null && otherSide != null && !sides[i].equals(otherSide)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rotates the sides of this piece from {@param from} to {@param to}. For example, {@code
     * rotate(NORTH, EAST)} rotates the piece one space clockwise, so that the old north side is the
     * new east side. Does not clone the piece first, so use this wisely!
     *
     * @param from the direction to be rotated from
     * @param to   the direction to be rotated to
     * @return this, after rotation
     */
    public Piece rotate(Direction from, Direction to) {
        final Side[] newSides = new Side[Constants.NUM_SIDES];
        int rotAmt = (to.ordinal() - from.ordinal()) % Constants.NUM_SIDES;
        if (rotAmt < 0) {
            rotAmt += Constants.NUM_SIDES;
        }
        System.arraycopy(sides, 0, newSides, rotAmt, Constants.NUM_SIDES - rotAmt);
        System.arraycopy(sides, Constants.NUM_SIDES - rotAmt, newSides, 0, rotAmt);
        sides = newSides;
        return this;
    }

    /**
     * Makes a clone of thise piece, then rotates that clone from {@code from} to {@code to}.
     * Rotation is done by calling {@link #rotate}, so this rotation will follow that method's
     * rules.
     *
     * @param from the direction to be rotated from
     * @param to   the direction to be rotated to
     * @return the rotated copy
     */
    public Piece copyRotate(Direction from, Direction to) {
        return copy().rotate(from, to);
    }

    /**
     * Makes a deep copy of this piece, copying each side as well.
     *
     * @return a deep copy of this piece
     */
    public Piece copy() {
        final Builder builder = new Builder();
        for (Direction dir : Direction.values()) {
            final Side s = sides[dir.ordinal()];
            if (s != null) {
                builder.setSide(sides[dir.ordinal()], dir); // Builder clones the side
            }
        }

        final Piece piece = builder.build();

        // If we have this piece's types already, carry them over to the clone
        if (pieceTypes != null) {
            piece.pieceTypes = pieceTypes.clone();
        }
        return piece;
    }

    public static class Builder {

        private final Side[] sides = new Side[Constants.NUM_SIDES];

        /**
         * Sets this piece's side in the given direction to the given side. Makes a copy of the
         * given side to stay safe from bad people.
         *
         * @param side the side to be added
         * @param dir  the direction of the side in this piece
         */
        public Builder setSide(Side side, Direction dir) {
            sides[dir.ordinal()] = side.copy();
            return this;
        }

        /**
         * Builds a {@link Piece} from this builder's sides.
         */
        public Piece build() {
            for (Side side : sides) {
                if (side != null) {
                    return new Piece(sides);
                }
            }
            throw new IllegalStateException("At least one side must be non-null");
        }
    }
}