import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main program of running all the other function.
 * Please note that the current version of program can only be run on Java 9, and need to import outside jar file.
 *
 * @author Yuxing HU
 * @author Xiaorong HE
 */
public class Main extends Application {
    /**
     * Start method is used to call all the additional method of the project quake
     *
     * @param primaryStage The first stage of JavaFx frame
     * @throws Exception Including all the exception that may cause by the program
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Appearance.fxml"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Main program of all.
     *
     * @param args all the outer args input from outer space
     */
    public static void main(String[] args) {
        launch(args);
    }
}
