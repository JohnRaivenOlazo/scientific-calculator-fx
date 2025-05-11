import javafx.application.Application;
import javafx.stage.Stage;
import controller.CalculatorController;
import model.CalculatorModel;
import view.CalculatorView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        CalculatorModel model = new CalculatorModel();
        CalculatorView view = new CalculatorView(primaryStage);
        new CalculatorController(model, view);
        view.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
