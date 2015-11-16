package puzzlesolver.ui.fx;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import puzzlesolver.Constants;
import puzzlesolver.Generator;
import puzzlesolver.Piece;
import puzzlesolver.Solver;
import puzzlesolver.simple.SimpleGenerator;
import puzzlesolver.simple.SimpleSolver;

public class MainController extends Application {

  public Canvas puzzleCanvas;
  public Button generateButton;
  public Button solveButton;
  public Button showButton = new Button(Constants.UI.BUTTON_SOLVE);
  public TextField heightField;
  public TextField widthField;
  public ObservableList<String> renderTypes =
      FXCollections.observableArrayList(Constants.UI.TEXT_SIMPLE,
                                        Constants.UI.TEXT_FANCY,
                                        Constants.UI.VISUAL,
                                        Constants.UI.VISUAL_FANCY);
  public ChoiceBox<String> renderTypeChoiceBox;
  public Slider rateSlider = new Slider();
  Timer timer = null;
  private Solver solver = new SimpleSolver();
  private PuzzleRenderer puzzleRenderer = new PuzzleRenderer(solver, puzzleCanvas);
  private Piece[] puzzle;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
    primaryStage.setTitle("Puzzle-O-Matic!");
    primaryStage.setResizable(false);
    Scene scene = new Scene(root, root.prefWidth(400), root.prefHeight(300));
    primaryStage.setScene(scene);
    primaryStage.show();

    // Set up renderTypeChoiceBox
    renderTypeChoiceBox = new ChoiceBox<>(renderTypes);
    renderTypeChoiceBox.getSelectionModel()
        .selectedItemProperty()
        .addListener(this::changeRenderMode);
    renderTypeChoiceBox.show();

    showButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Parent root;
        try {
          root = FXMLLoader.load(getClass().getResource("puzzle.fxml"));
          Stage stage = new Stage();
          stage.setTitle("Puzzle!");
          stage.setScene(new Scene(root, puzzleRenderer.getRequiredWidth(),
                                   puzzleRenderer.getRequiredHeight()));
          stage.show();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    rateSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.equals(oldValue) && timer != null) {
        solve();
      }
    });
  }

  public void generate(ActionEvent actionEvent) {
    generateButton.setDisable(true);
    generateButton.setText("Generating...");

    Generator generator = new SimpleGenerator();

    try {
      puzzle = generator.generate(Integer.parseInt(widthField.getText()),
                                  Integer.parseInt(heightField.getText()));
    } catch (NumberFormatException e) {
      // TODO some kind of pop-up box or notifier for bad data
    } finally {
      generateButton.setDisable(false);
      generateButton.setText("Regenerate");
      solveButton.setDisable(false);
    }
  }

  public void solve() {
    if (timer != null) {
      timer.cancel();
    }
    switch (solveButton.getText()) {
      case Constants.UI.BUTTON_SOLVE:
        solveButton.setText(Constants.UI.BUTTON_CANCEL);
        solver.init(puzzle);

        timer = new Timer("solver", true);
        timer.scheduleAtFixedRate(new TimerTask() {
          @Override
          public void run() {
            puzzleRenderer.draw();
            solver.nextStep();
          }
        }, 0, (long) rateSlider.getValue());
        break;
      case Constants.UI.BUTTON_CANCEL:
        solveButton.setText(Constants.UI.BUTTON_SOLVE);
    }
  }

  public void changeRenderMode(ObservableValue ov, String oldValue, String newValue) {
    if (!newValue.equals(oldValue)) {
      puzzleRenderer.setRenderMethod(newValue);
    }
  }
}