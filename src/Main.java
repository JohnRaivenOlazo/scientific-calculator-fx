import javafx.application.Application;
import javafx.stage.Stage;
import controller.CalculatorController;
import model.CalculatorModel;
import view.CalculatorView;

public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        CalculatorModel model = new CalculatorModel();
        CalculatorView view = new CalculatorView(stage);
        new CalculatorController(model, view);

        view.show();
    }
}
