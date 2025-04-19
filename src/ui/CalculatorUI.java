package ui;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import model.ScientificOperation;

import java.util.Objects;

public class CalculatorUI extends Application {
    private final TextField display = new TextField();
    private final ScientificOperation op = new ScientificOperation();

    @Override
    public void start(Stage primaryStage) {
        display.setEditable(false);
        display.setPrefHeight(50);
        display.setStyle("-fx-font-size: 18px;");
        display.setMaxWidth(Double.MAX_VALUE);

        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/icon.png"))));

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
                {"MC","Solve","Factor","Expand","Theme"}
        };

        int cols = btns[0].length;
        for (int i = 0; i < cols; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setPercentWidth(100.0 / cols);
            grid.getColumnConstraints().add(cc);
        }

        for (int r = 0; r < btns.length; r++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rc);

            for (int c = 0; c < btns[r].length; c++) {
                String text = btns[r][c];
                Button b = new Button(text);
                b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                b.setTooltip(new Tooltip(switch (text) {
                    case "CE" -> "Clear Entry";
                    case "C" -> "All Clear";
                    case "MR" -> "Memory Recall";
                    case "MC" -> "Memory Clear";
                    case "M+" -> "Memory Add";
                    case "M-" -> "Memory Subtract";
                    case "Solve" -> "Solve ax^2+bx+c=0";
                    case "Factor" -> "Factor ax^2+bx+c";
                    case "Expand" -> "Expand (x+a)(x+b)";
                    case "Theme" -> "Toggle Light/Dark Mode";
                    default -> text;
                }));
                grid.add(b, c, r);
                b.setOnAction(e -> onButton(text));
            }
        }

        VBox root = new VBox(10, display, grid);
        root.setPadding(new Insets(10));
        VBox.setVgrow(grid, Priority.ALWAYS);

        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("calculator.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Scientific Calculator FX");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(500);
        primaryStage.setResizable(true);
        primaryStage.show();

        // Enter‑key handling
        scene.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER) onButton("=");
        });
    }

    private void onButton(String txt) {
        try {
            switch (txt) {
                case "C" -> display.clear();
                case "CE" -> {
                    String t = display.getText();
                    if (!t.isEmpty()) display.setText(t.substring(0, t.length() - 1));
                }
                case "=" -> {
                    double res = op.execute(display.getText());
                    display.setText(String.valueOf(res));
                }
                case "M+" -> ScientificOperation.memoryAdd(op.execute(display.getText()));
                case "M-" -> ScientificOperation.memorySubtract(op.execute(display.getText()));
                case "MR" -> display.setText(String.valueOf(ScientificOperation.memoryRecall()));
                case "MC" -> ScientificOperation.memoryClear();
                case "Solve", "Factor", "Expand" -> {
                    String expr = display.getText();
                    String out = switch (txt) {
                        case "Solve" -> ScientificOperation.solveQuadratic(expr);
                        case "Factor" -> ScientificOperation.factorQuadratic(expr);
                        default -> ScientificOperation.expandBinomial(expr);
                    };
                    display.setText(out);
                }
                case "Theme" -> {
                    Scene s = display.getScene();
                    String light = Objects.requireNonNull(getClass().getResource("calculator.css")).toExternalForm();
                    String dark = Objects.requireNonNull(getClass().getResource("calculator-dark.css")).toExternalForm();
                    if (s.getStylesheets().contains(light)) {
                        s.getStylesheets().clear();
                        s.getStylesheets().add(dark);
                    } else {
                        s.getStylesheets().clear();
                        s.getStylesheets().add(light);
                    }
                }
                default -> display.appendText(txt);
            }
        } catch (Exception ex) {
            display.setText("Error");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}