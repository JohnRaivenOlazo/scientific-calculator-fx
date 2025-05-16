package controller;

import javafx.scene.input.KeyCode;
import model.CalculatorModel;
import view.CalculatorView;

import java.util.Arrays;

public class CalculatorController {
    private final CalculatorModel model;
    private final CalculatorView view;

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;
        initializeButtonHandlers();

        initializeKeyHandlers();
    }

    private void initializeButtonHandlers() {
        String[][] buttons = {
            {"7","8","9","/","sin"},
            {"4","5","6","*","cos"},
            {"1","2","3","-","tan"},
            {"0",".","^","+","log"},
            {"(",")","sqrt","ln","="},
            {"C","CE","M+","M-","MR"},
            {"MC","Solve","Factor","Expand"}
        };

        System.out.println(Arrays.deepToString(buttons[1]));

        for (String[] row : buttons) {
            for (String buttonText : row) {
                if (!buttonText.isEmpty()) {
                    view.setButtonHandler(buttonText, () -> handleButton(buttonText));
                }
            }
        }
    }

    private void initializeKeyHandlers() {
        view.getScene().setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER) {
                handleButton("=");
            }
        });
    }

    private void handleButton(String text) {
        try {
            switch (text) {
                case "C" -> model.clearDisplay();
                case "CE" -> model.clearLastEntry();
                case "=" -> model.setDisplayText(model.calculate());
                case "M+" -> CalculatorModel.memoryAdd(Double.parseDouble(model.calculate()));
                case "M-" -> CalculatorModel.memorySubtract(Double.parseDouble(model.calculate()));
                case "MR" -> model.setDisplayText(String.valueOf(CalculatorModel.memoryRecall()));
                case "MC" -> CalculatorModel.memoryClear();
                case "Solve" -> model.setDisplayText(model.solveQuadratic());
                case "Factor" -> model.setDisplayText(model.factorQuadratic());
                case "Expand" -> model.setDisplayText(model.expandBinomial());
                default -> model.appendToDisplay(text);
            }
            view.setDisplayText(model.getDisplayText());
        } catch (Exception ex) {
            model.setDisplayText("Error");
            view.setDisplayText(model.getDisplayText());
        }
    }
}