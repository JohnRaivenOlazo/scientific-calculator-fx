package controller;

import javafx.scene.input.KeyCode;
import model.CalculatorModel;
import view.CalculatorView;

public class CalculatorController {
    private final CalculatorModel model;
    private final CalculatorView view;
    private boolean inputMode = false;
    private int coefficientIndex = 0;
    private String currentOperation = "";

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
                {"MC","Solve","Factor","Expand","Next"}
        };

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
            if (inputMode) {
                handleCoefficientInput(text);
                return;
            }

            switch (text) {
                case "C" -> model.clearDisplay();
                case "CE" -> model.clearLastEntry();
                case "=" -> model.setDisplayText(model.calculate());
                case "M+" -> CalculatorModel.memoryAdd(Double.parseDouble(model.calculate()));
                case "M-" -> CalculatorModel.memorySubtract(Double.parseDouble(model.calculate()));
                case "MR" -> model.setDisplayText(String.valueOf(CalculatorModel.memoryRecall()));
                case "MC" -> CalculatorModel.memoryClear();
                case "Solve" -> startOperation("Solve");
                case "Factor" -> startOperation("Factor");
                case "Expand" -> startOperation("Expand");
                default -> model.appendToDisplay(text);
            }
            view.setDisplayText(model.getDisplayText());
        } catch (Exception ex) {
            model.setDisplayText("Error");
            view.setDisplayText(model.getDisplayText());
        }
    }

    private void startOperation(String operation) {
        inputMode = true;
        coefficientIndex = 0;
        currentOperation = operation;
        model.clearDisplay();
        if (operation.equals("Expand")) {
            view.setDisplayText("Enter first factor:");
        } else {
            view.setDisplayText("Enter x² coefficient:");
        }
    }

    private void handleCoefficientInput(String text) {
        try {
            if (text.equals("Next")) {
                double value = Double.parseDouble(model.getDisplayText());

                if (currentOperation.equals("Expand")) {
                    if (coefficientIndex == 0) {
                        model.setBinomialFactors(value, 0);
                        coefficientIndex++;
                        model.clearDisplay();
                        view.setDisplayText("Enter second factor:");
                    } else {
                        model.setBinomialFactors(model.getBinomialFactors()[0], value);
                        inputMode = false;
                        model.setDisplayText(model.expandBinomial());
                    }
                } else {
                    if (coefficientIndex == 0) {
                        model.setQuadraticCoefficients(value, 0, 0);
                        coefficientIndex++;
                        model.clearDisplay();
                        view.setDisplayText("Enter x coefficient:");
                    } else if (coefficientIndex == 1) {
                        model.setQuadraticCoefficients(model.getQuadraticCoefficients()[0], value, 0);
                        coefficientIndex++;
                        model.clearDisplay();
                        view.setDisplayText("Enter constant:");
                    } else {
                        model.setQuadraticCoefficients(
                                model.getQuadraticCoefficients()[0],
                                model.getQuadraticCoefficients()[1],
                                value
                        );
                        inputMode = false;
                        if (currentOperation.equals("Solve")) {
                            model.setDisplayText(model.solveQuadratic());
                        } else {
                            model.setDisplayText(model.factorQuadratic());
                        }
                    }
                }
            } else if (text.equals("C")) {
                model.clearDisplay();
            } else if (text.equals("CE")) {
                model.clearLastEntry();
            } else {
                model.appendToDisplay(text);
            }
            view.setDisplayText(model.getDisplayText());
        } catch (Exception e) {
            model.setDisplayText("Error");
            view.setDisplayText(model.getDisplayText());
        }
    }
}