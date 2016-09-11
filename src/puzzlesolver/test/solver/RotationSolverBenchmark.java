package puzzlesolver.test.solver;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

import puzzlesolver.Piece;
import puzzlesolver.PieceNotFoundException;
import puzzlesolver.generator.Generator;
import puzzlesolver.generator.PolypointGenerator;
import puzzlesolver.solver.RotationSolver;
import puzzlesolver.solver.Solver;
import puzzlesolver.test.Benchmarks;

@State(Scope.Benchmark)
@Threads(1)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class RotationSolverBenchmark {

  @Param({"50", "100", "200"})
  private int size;
  @Param({"0.2d", "0.1d", "0.05d", "0.01d"})
  private double complexity;
  private Piece[] puzzle;

  @Setup
  public void setUp() {
    final Generator generator = new PolypointGenerator(complexity, complexity);
    generator.setSeed(Benchmarks.GENERATOR_SEED);
    puzzle = generator.generate(size, size);
  }

  @Benchmark
  public void measureFull() {
    Solver solver = new RotationSolver();
    solver.init(puzzle);

    try {
      //noinspection StatementWithEmptyBody
      while (solver.nextStep());
    } catch (PieceNotFoundException e) {
      e.printStackTrace();
    }
  }
}