//
//  TableView example
//
//  Using SimpleXXXProperty in the class displayed.
//
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.collections.*;
import javafx.beans.property.*;

import java.util.ListIterator;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;

public class TableViewExample2 extends Application {

    private final String cssFile = TableViewExample2.class
                                   .getClassLoader()
                                   .getResource("gui.css")
                                   .toString();
    private final String dataFile = TableViewExample2.class
                                   .getClassLoader()
                                   .getResource("ChineseBoxOffice.txt")
                                   .toString().replace("file:", "");
    private static ObservableList<Film2> films =
                       FXCollections.observableArrayList();
    private static TableView<Film2> tv = new TableView<Film2>();

    static void loadFilms(String file) {
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file),
                                       charset)) {
           String   line = null;
           String[] fields;
           while ((line = reader.readLine()) != null) {
              fields = line.split("\\|");
              films.add(new Film2(fields[0],
                                 fields[1],
                                 Integer.parseInt(fields[2]),
                                 Float.parseFloat(fields[3])));
           }
        } catch (IOException x) {
           System.err.format("IOException: %s%n", x);
        }
    }

    @Override
    public void init() {
        loadFilms(dataFile);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void populate(TableView<Film2> tv) {
        tv.setItems(films);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("TableView");
        Group root = new Group();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(cssFile);
        Pane pane = new Pane();
        VBox box = new VBox();
        root.getChildren().add(pane);
        pane.getChildren().add(box);
        // Create the TableView
        TableView<Film2> tv = new TableView<Film2>();
        tv.setPrefWidth(950);
        tv.setPrefHeight(600);
        // Add the TableView to the box
        box.getChildren().add(tv);
        // Create the various columns and add them to tv
        TableColumn<Film2,String> c1 = new TableColumn<Film2,String>("Title");
        c1.setCellValueFactory(new
                          PropertyValueFactory<Film2,String>("title"));
        tv.getColumns().add(c1);
        TableColumn<Film2,String> c2 =
                          new TableColumn<Film2,String>("Countries");
        c2.setCellValueFactory(new
                          PropertyValueFactory<Film2,String>("countries"));
        tv.getColumns().add(c2);
        TableColumn<Film2,Integer> c3 = new TableColumn<Film2,Integer>("Year");
        c3.setCellValueFactory(new
                          PropertyValueFactory<Film2,Integer>("year"));
        tv.getColumns().add(c3);
        TableColumn<Film2,Float> c4 = new
                        TableColumn<Film2,Float>("Billion RMB");
        c4.setCellValueFactory(new
                          PropertyValueFactory<Film2,Float>("billionRMB"));
        tv.getColumns().add(c4);

        // We could populate here but in practice it would
        // probably be the result of a query, based on what
        // users would enter in some fields ...
        populate(tv);
        // Everything is ready, display
        stage.setScene(scene);
        stage.show();
    }

}
