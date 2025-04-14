package ui;

import model.ScientificOperation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class CalculatorUI {
    private final JTextField display;
    private final ScientificOperation operation;

    public CalculatorUI() {
        operation = new ScientificOperation();
        JFrame frame = new JFrame("Scientific Calculator");
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon.png")));
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(360, 600));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        // Main container
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        container.setBackground(new Color(18, 18, 18));

        // Display
        display = new JTextField();
        display.setFont(new Font("Segoe UI", Font.BOLD, 30));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBackground(new Color(30, 30, 30));
        display.setForeground(Color.WHITE);
        display.setCaretColor(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        display.setEditable(false);
        container.add(display, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(7, 4, 10, 10));
        buttonPanel.setBackground(new Color(18, 18, 18));
        String[] buttons = {
                "C", "(", ")", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "=", "^",
                "sqrt", "sin", "cos", "tan",
                "log", "ln", "", ""
        };

        for (String text : buttons) {
            if (text.isEmpty()) {
                buttonPanel.add(new JLabel()); // placeholder
            } else {
                buttonPanel.add(createButton(text));
            }
        }

        container.add(buttonPanel, BorderLayout.CENTER);
        frame.setContentPane(container);
        frame.pack();
        frame.setVisible(true);
    }

    private JButton createButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        // Colors by type
        if (label.equals("C")) {
            btn.setBackground(new Color(192, 57, 43)); // Red-orange
        } else if ("=/*-+^".contains(label)) {
            btn.setBackground(new Color(44, 62, 80)); // Operators
        } else if ("0123456789.".contains(label)) {
            btn.setBackground(new Color(40, 40, 40)); // Digits
        } else {
            btn.setBackground(new Color(30, 60, 60)); // Functions
        }

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(btn.getBackground().brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (label.equals("C")) {
                    btn.setBackground(new Color(192, 57, 43));
                } else if ("=/*-+^".contains(label)) {
                    btn.setBackground(new Color(44, 62, 80));
                } else if ("0123456789.".contains(label)) {
                    btn.setBackground(new Color(40, 40, 40));
                } else {
                    btn.setBackground(new Color(30, 60, 60));
                }
            }
        });

        // Click logic
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

        return btn;
    }
}
