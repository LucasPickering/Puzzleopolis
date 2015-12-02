package puzzlesolver.constants;

// UIConstants
public final class UIConstants {

  public static final String RENDER_TEXT_SIMPLE = "Text (Simple)", RENDER_TEXT_ADVANCED = "Text (Fancy)",
      RENDER_VISUAL_SIMPLE = "Visual (Simple)", RENDER_VISUAL_FANCY = "Visual (Fancy)";

  public static final String SOLVER_SIMPLE = "Simple", SOLVER_ROTATION = "Rotation";
  public static final String GENERATOR_SIMPLE = "Simple", GENERATOR_ROTATION = "Rotation";

  public static final String WIDTH_FIELD = "Width", HEIGHT_FIELD = "Height";

  public static final String BUTTON_SOLVE = "Solve", BUTTON_STOP = "Stop",
      BUTTON_SHOW = "Show", BUTTON_GENERATE = "Generate", BUTTON_REGENERATE = "Regenerate";

  @SuppressWarnings("SuspiciousNameCombination")
  public static final double VISUAL_PIECE_WIDTH = Constants.SIDE_LENGTH,
      VISUAL_PIECE_HEIGHT = VISUAL_PIECE_WIDTH,
      WINDOW_MIN_WIDTH = 400d, WINDOW_MIN_HEIGHT = 400d,
      VISUAL_PIECE_PADDING = Constants.SIDE_LENGTH;

  public static final int DEFAULT_PUZZLE_WIDTH = 16, DEFAULT_PUZZLE_HEIGHT = 16;
}