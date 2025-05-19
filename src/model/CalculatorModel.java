package model;

public class CalculatorModel {
    private String displayText = "";
    private static double memory = 0.0;
    private final double[] quadraticCoefficients = new double[3];
    private final double[] binomialFactors = new double[2];

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

    public void setQuadraticCoefficients(double a, double b, double c) {
        quadraticCoefficients[0] = a;
        quadraticCoefficients[1] = b;
        quadraticCoefficients[2] = c;
    }

    public double[] getQuadraticCoefficients() {
        return quadraticCoefficients;
    }

    public void setBinomialFactors(double a, double b) {
        binomialFactors[0] = a;
        binomialFactors[1] = b;
    }

    public double[] getBinomialFactors() {
        return binomialFactors;
    }

    public String solveQuadratic() {
        try {
            double a = quadraticCoefficients[0];
            double b = quadraticCoefficients[1];
            double c = quadraticCoefficients[2];

            if (a == 0) return "Not quadratic";

            double d = b*b - 4*a*c;
            if (d < 0) return "No real roots";
            double r1 = (-b + Math.sqrt(d)) / (2*a);
            double r2 = (-b - Math.sqrt(d)) / (2*a);
            return String.format("x=%.4f, %.4f", r1, r2);
        } catch(Exception e) {
            return "Error: Set coefficients first";
        }
    }

    public String factorQuadratic() {
        try {
            String sol = solveQuadratic();
            if (sol.startsWith("x=")) {
                String[] parts = sol.substring(2).split(",");
                double r1 = Double.parseDouble(parts[0]);
                double r2 = Double.parseDouble(parts[1].trim());
                return String.format("(x%+,.4f)(x%+,.4f)", -r1, -r2);
            }
            return sol;
        } catch(Exception e) {
            return "Error: Set coefficients first";
        }
    }

    public String expandBinomial() {
        try {
            double a = binomialFactors[0];
            double b = binomialFactors[1];
            return String.format("x^2%+,.4fx%+,.4f", a+b, a*b);
        } catch(Exception e) {
            return "Error: Set factors first";
        }
    }
}