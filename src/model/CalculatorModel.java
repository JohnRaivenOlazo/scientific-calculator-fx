package model;

import java.util.regex.*;

public class CalculatorModel {
    private String displayText = "";
    private static double memory = 0.0;

    public String getDisplayText() { return displayText; }
    public void setDisplayText(String text) { displayText = text; }
    public void appendToDisplay(String text) { displayText += text; }
    public void clearDisplay() { displayText = ""; }
    public void clearLastEntry() {
        if (!displayText.isEmpty()) {
            displayText = displayText.substring(0, displayText.length() - 1);
        }
    }

    public static void memoryAdd(double v) { memory += v; }
    public static void memorySubtract(double v) { memory -= v; }
    public static double memoryRecall() { return memory; }
    public static void memoryClear() { memory = 0.0; }

    public String calculate() throws Exception {
        try {
            return String.valueOf(eval(displayText));
        } catch (Exception e) {
            throw new Exception("Invalid Expression");
        }
    }

    private double eval(String expr) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() { ch = (++pos < expr.length()) ? expr.charAt(pos) : -1; }
            boolean eat(int c) {
                while (ch==' ') nextChar();
                if (ch==c) { nextChar(); return true; }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expr.length()) throw new RuntimeException("Unexpected: "+(char)ch);
                return x;
            }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int start = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch>='0'&&ch<='9')||ch=='.') {
                    while ((ch>='0'&&ch<='9')||ch=='.') nextChar();
                    x = Double.parseDouble(expr.substring(start, this.pos));
                } else if (ch>='a'&&ch<='z') {
                    while (ch>='a'&&ch<='z') nextChar();
                    String func = expr.substring(start, this.pos);
                    x = parseFactor();
                    x = switch(func) {
                        case "sqrt" -> Math.sqrt(x);
                        case "sin" -> Math.sin(Math.toRadians(x));
                        case "cos" -> Math.cos(Math.toRadians(x));
                        case "tan" -> Math.tan(Math.toRadians(x));
                        case "log" -> Math.log10(x);
                        case "ln" -> Math.log(x);
                        default -> throw new RuntimeException("Unknown func: "+func);
                    };
                } else throw new RuntimeException("Unexpected: "+(char)ch);
                if (eat('^')) x = Math.pow(x, parseFactor());
                return x;
            }
        }.parse();
    }

    public String solveQuadratic() {
        try {
            String left = displayText.split("=")[0].replaceAll("\\s","");
            Pattern p = Pattern.compile("([+-]?\\d*\\.?\\d*)x\\^2([+-]?\\d*\\.?\\d*)x([+-]?\\d*\\.?\\d*)");
            Matcher m = p.matcher(left);
            if (!m.matches()) return "Invalid quadratic";
            double a = m.group(1).isEmpty()||m.group(1).equals("+")?1:
                    m.group(1).equals("-")?-1:Double.parseDouble(m.group(1));
            double b = Double.parseDouble(m.group(2));
            double c = Double.parseDouble(m.group(3));
            double d = b*b - 4*a*c;
            if (d<0) return "No real roots";
            double r1 = (-b + Math.sqrt(d)) / (2*a);
            double r2 = (-b - Math.sqrt(d)) / (2*a);
            return String.format("x=%.4f, %.4f", r1, r2);
        } catch(Exception e) {
            return "Error";
        }
    }

    public String factorQuadratic() {
        String sol = solveQuadratic();
        if (sol.startsWith("x=")) {
            String[] parts = sol.substring(2).split(",");
            double r1 = Double.parseDouble(parts[0]);
            double r2 = Double.parseDouble(parts[1].trim());
            return String.format("(x%+,.4f)(x%+,.4f)", -r1, -r2);
        }
        return sol;
    }

    public String expandBinomial() {
        try {
            Pattern p = Pattern.compile("\\(x([+-]?\\d*\\.?\\d*)\\)\\(x([+-]?\\d*\\.?\\d*)\\)");
            Matcher m = p.matcher(displayText.replaceAll("\\s",""));
            if (!m.matches()) return "Invalid form";
            double a = m.group(1).isEmpty()||m.group(1).equals("+")?1:
                    m.group(1).equals("-")?-1:Double.parseDouble(m.group(1));
            double b = Double.parseDouble(m.group(2));
            return String.format("x^2%+,.4fx%+,.4f", a+b, a*b);
        } catch(Exception e) {
            return "Error";
        }
    }
}