package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.*;

import java.util.*;

import static model.DrawItem.CROSS;
import static model.DrawItem.EMPTY;
import static model.DrawItem.ZERO;
import static model.GameResult.LOSE;
import static model.GameResult.WIN;

public class Controller {
    Stage stage;
    ObservableList<Node> list;
    private DrawItem[] field;
    private AiGameStrategy strategy = new RandomAiGameStrategy();
    @FXML
    GridPane gridPane;

    @FXML
    public void close() {
        stage.close();
    }

    public void setRoot(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        gridPane.addEventHandler(ActionEvent.ACTION, e -> setPosition(e));
        this.list = gridPane.getChildrenUnmodifiable();

        field = new DrawItem[9];
        Arrays.fill(field, EMPTY);
        clearField();
    }

    private void clearField() {
        for (Node node : list) {
            if (node instanceof Button) {
                ((Button) node).setText("");
            }
        }
    }

    private void setPosition(ActionEvent event) {
        Object source = event.getTarget();
        try {
            if (source instanceof Button) {
                Button b = (Button) source;
                int crossPos = Integer.parseInt(b.getId().substring(1)) - 1;
                if (validate(crossPos)) {
                    setCross(b, crossPos);

                    int zeroPos = strategy.aiMakeTurn(arrTransform(field));

                    if (zeroPos == -1) {
                        new Alert(Alert.AlertType.INFORMATION, "No moves more", ButtonType.OK).showAndWait();
                        return;
                    }
                    setZero(zeroPos);
                }
            }
        } catch (FinishException e) {
            return;
        }
    }

    private int[] arrTransform(DrawItem[] arr) {
        int[] res = new int[9];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == CROSS) {
                res[i] = 1;
            } else if (arr[i] == ZERO) {
                res[i] = -1;
            }
        }
        return res;
    }
    private void showStep(int index) {
        for (Node node : list) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                int id = Integer.parseInt(btn.getId().substring(1)) - 1;
                if (id == index) {
                    btn.setText("O");
                    return;
                }
            }
        }
    }
    private void setZero(int zeroPos) throws FinishException {
        field[zeroPos] = ZERO;
        this.showStep(zeroPos);
        if (Checker.checkResult(field, ZERO) == WIN) {
            new Alert(Alert.AlertType.INFORMATION, "You lose", ButtonType.OK).showAndWait();
            throw new FinishException();
        }
    }

    private void setCross(Button b, int crossPos) throws FinishException {
        field[crossPos] = CROSS;
        b.setText("X");
        if (Checker.checkResult(field, CROSS) == LOSE) {
            new Alert(Alert.AlertType.INFORMATION, "You won", ButtonType.OK).showAndWait();
            throw new FinishException();
        }
    }

    private boolean validate(int pos) {
        if (field[pos] != EMPTY) {
            return false;
        } else {
            return true;
        }
    }
}