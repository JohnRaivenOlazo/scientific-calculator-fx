package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CalculatorView {
    private final Map<String, Button> buttons = new HashMap<>();
    private final TextField display;
    private final Scene scene;
    private final Stage stage;

    public CalculatorView(Stage stage) {
        this.stage = stage;
        this.display = createDisplay();
        GridPane buttonGrid = createButtonGrid();
        this.scene = createScene(buttonGrid);
        setupStage();
    }

    private TextField createDisplay() {
        TextField display = new TextField();
        display.setEditable(false);
        display.setPrefHeight(50);
        display.setStyle("-fx-font-size: 18px;");
        display.setMaxWidth(Double.MAX_VALUE);
        return display;
    }

    private GridPane createButtonGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        String[][] btns = {
            {"7","8","9","/","sin"},
            {"4","5","6","*","cos"},
            {"1","2","3","-","tan"},
            {"0",".","^","+","log"},
            {"(",")","sqrt","ln","="},
            {"C","CE","M+","M-","MR"},
            {"MC","Solve","Factor","Expand",""}
        };

        setupGridConstraints(grid, btns[0].length);
        createButtons(grid, btns);
        return grid;
    }

    private void setupGridConstraints(GridPane grid, int cols) {
        for (int i = 0; i < cols; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setPercentWidth(100.0 / cols);
            grid.getColumnConstraints().add(cc);
        }
    }

    private void createButtons(GridPane grid, String[][] btns) {
        for (int r = 0; r < btns.length; r++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rc);

            for (int c = 0; c < btns[r].length; c++) {
                String text = btns[r][c];
                if (!text.isEmpty()) {
                    Button b = new Button(text);
                    b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    b.setTooltip(new Tooltip(getTooltipText(text)));
                    grid.add(b, c, r);
                    buttons.put(text, b);
                }
            }
        }
    }

    private String getTooltipText(String text) {
        return switch (text) {
            case "CE" -> "Clear Entry";
            case "C" -> "All Clear";
            case "MR" -> "Memory Recall";
            case "MC" -> "Memory Clear";
            case "M+" -> "Memory Add";
            case "M-" -> "Memory Subtract";
            case "Solve" -> "Solve ax^2+bx+c=0";
            case "Factor" -> "Factor ax^2+bx+c";
            case "Expand" -> "Expand (x+a)(x+b)";
            default -> text;
        };
    }

    private Scene createScene(GridPane grid) {
        VBox root = new VBox(10, display, grid);
        root.setPadding(new Insets(10));
        VBox.setVgrow(grid, Priority.ALWAYS);
        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        return scene;
    }

    private void setupStage() {
        Image icon = new Image("view/icon.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setTitle("Scientific Calculator FX");
        stage.setMinWidth(400);
        stage.setMinHeight(500);
        stage.setResizable(true);
    }

    public void setButtonHandler(String buttonText, Runnable handler) {
        Button button = buttons.get(buttonText);
        if (button != null) {
            button.setOnAction(e -> handler.run());
        }
    }

    public void setDisplayText(String text) {
        display.setText(text);
    }

    public Scene getScene() {
        return scene;
    }

    public void show() {
        stage.show();
    }
}