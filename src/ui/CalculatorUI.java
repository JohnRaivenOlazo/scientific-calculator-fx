package ui;

import model.ScientificOperation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorUI {
    private final JTextField display;
    private final ScientificOperation operation;

    public CalculatorUI() {
        operation = new ScientificOperation();
        JFrame frame = new JFrame("Scientific Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(false);
        frame.setLayout(new BorderLayout());

        // App Background
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(20, 20, 20));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Display
        display = new JTextField();
        display.setFont(new Font("Consolas", Font.PLAIN, 32));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.GREEN);
        display.setCaretColor(Color.GREEN);
        display.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        display.setEditable(false);

        container.add(display, BorderLayout.NORTH);

        // Buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(6, 4, 12, 12));
        buttonsPanel.setBackground(new Color(20, 20, 20));
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "(", ")", "^", "sqrt",
                "sin", "cos", "tan", "log"
        };

        for (String text : buttons) {
            buttonsPanel.add(createStyledButton(text));
        }

        // Bottom Buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        bottomPanel.setBackground(new Color(20, 20, 20));
        bottomPanel.add(createStyledButton("C"));
        bottomPanel.add(createStyledButton("ln"));

        container.add(buttonsPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(container);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Consolas", Font.BOLD, 22));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(35, 35, 35));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            switch (label) {
                case "=" -> {
                    try {
                        double result = operation.execute(display.getText());
                        display.setText(String.valueOf(result));
                    } catch (Exception ex) {
                        display.setText("Error");
                    }
                }
                case "C" -> display.setText("");
                default -> display.setText(display.getText() + label);
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(60, 60, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(35, 35, 35));
            }
        });

        return btn;
    }
}
