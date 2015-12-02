package puzzlesolver.ui.console;

import java.io.PrintStream;
import java.util.Scanner;

import puzzlesolver.Generator;
import puzzlesolver.Solver;
import puzzlesolver.rotation.RotationGenerator;
import puzzlesolver.rotation.RotationSolver;

public class ConsoleController {

  /**
   * Time to wait between steps, in milliseconds.
   */
  private long timeInterval = 0;
  /**
   * Whether to require the user to hit <kbd>enter</kbd> on each step.
   */
  private boolean requireInputPerStep;

  public ConsoleController(long timeInterval, boolean requireInputPerStep) {
    this.timeInterval = timeInterval;
    this.requireInputPerStep = requireInputPerStep;
  }

  public ConsoleController() {
    requireInputPerStep = true;
  }

  public static void main(String[] args) {
    start(false);
  }

  public static void start(boolean fancy) {
    ConsoleController consoleController = new ConsoleController();
    try {
      consoleController.start(System.out, fancy);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void start(PrintStream out, boolean fancy) throws Exception {
    Generator generator = new RotationGenerator();
    Solver solver = new RotationSolver();
    TextView textView = fancy ? new AdvancedTextView(solver) : new SimpleTextView(solver);
    Scanner in = new Scanner(System.in);
    int width, height;

    out.print("Puzzle Width: ");
    width = in.nextInt();
    out.print("Puzzle Height: ");
    height = in.nextInt();
    generator.setSeed("test seed"); // TODO: Remove this line
    solver.init(generator.generate(width, height));

    do {
      print(out, textView.draw());
      if (requireInputPerStep) {
        in.nextLine();
      } else {
        Thread.sleep(timeInterval);
      }
      out.println("Solvin' away!");
    } while (!solver.nextStep());

    out.println("Done!");
    print(out, textView.draw());
    in.close();
  }

  private void print(PrintStream out, String[] render) {
    for (String line : render) {
      out.println(line);
    }
    out.println();
  }
}