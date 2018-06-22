import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {

    private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    private static Random rand_generator;
    private double x;
    private double y;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainUI.fxml"));
        primaryStage.setTitle("My Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rand_generator = new Random();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void moveWindow(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        double height = screenBounds.getHeight();
        double width = screenBounds.getWidth();
        double x_move = width / 10 + rand_generator.nextDouble() * width / 2;
        double y_move = height / 10 + rand_generator.nextDouble() * height / 2;
        this.x = (double) ((long) (this.x + x_move) % (long) (width - stage.getWidth()));
        this.y = (double) ((long) (this.y + y_move) % (long) (height - stage.getHeight()));
        stage.setX(this.x);
        stage.setY(this.y);
    }
}



