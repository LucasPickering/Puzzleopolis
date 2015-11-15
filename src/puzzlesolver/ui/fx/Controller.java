package puzzlesolver.ui.fx;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import puzzlesolver.Constants;
import puzzlesolver.Generator;
import puzzlesolver.Piece;
import puzzlesolver.Solver;
import puzzlesolver.simple.SimpleGenerator;
import puzzlesolver.simple.SimpleSolver;

public class Controller {

  public Button generateButton;
  public Button solveButton;
  public TextField heightField;
  public TextField widthField;
  public final ObservableList<String> renderTypes =
      FXCollections.observableArrayList(Constants.UI.TEXT_SIMPLE,
                                        Constants.UI.TEXT_FANCY,
                                        Constants.UI.VISUAL,
                                        Constants.UI.VISUAL_FANCY);
  public final ChoiceBox<String> renderTypeChoiceBox = new ChoiceBox<>(renderTypes);

  public void setupChoiceBox() {
    System.out.println(renderTypeChoiceBox.getItems());
    System.out.println(renderTypeChoiceBox.getSelectionModel().selectedItemProperty().getValue());
    renderTypeChoiceBox.getSelectionModel()
        .selectedItemProperty()
        .addListener(this::changeRenderMode);
    System.out.println(renderTypeChoiceBox.getItems());
    System.out.println(renderTypeChoiceBox.getSelectionModel().selectedItemProperty().getValue());
    renderTypeChoiceBox.show();
    System.out.println(renderTypeChoiceBox.getItems());
    System.out.println(renderTypeChoiceBox.getSelectionModel().selectedItemProperty().getValue());
  }

  private Piece[] puzzle;

  public void generate() {
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
    solveButton.setDisable(true);
    solveButton.setText("Solving...");
    Solver solver = new SimpleSolver();
    solver.init(puzzle);

    // TODO
  }

  public void changeRenderMode(ObservableValue ov, String oldValue, String newValue) {
    if (!newValue.equals(oldValue)) {
      switch (newValue) {
        case Constants.UI.TEXT_SIMPLE:
          // TODO
          break;
        case Constants.UI.TEXT_FANCY:
          // TODO
          break;
        case Constants.UI.VISUAL:
          // TODO
          break;
        case Constants.UI.VISUAL_FANCY:
          // TODO
          break;
      }
    }
  }
}
