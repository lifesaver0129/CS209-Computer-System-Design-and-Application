//
//  TableView example
//
//  Using regular attributes
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

public class TableViewExample extends Application {

    private final String cssFile = TableViewExample.class
                                   .getClassLoader()
                                   .getResource("gui.css")
                                   .toString();
    private final String dataFile = TableViewExample.class
                                   .getClassLoader()
                                   .getResource("ChineseBoxOffice.txt")
                                   .toString().replace("file:", "");
    private static ObservableList<Film> films =
                       FXCollections.observableArrayList();
    private static TableView<Film> tv = new TableView<Film>();

    static void loadFilms(String file) {
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file),
                                       charset)) {
           String   line = null;
           String[] fields;
           while ((line = reader.readLine()) != null) {
              fields = line.split("\\|");
              films.add(new Film(fields[0],
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

    private void populate(TableView<Film> tv) {
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
        TableView<Film> tv = new TableView<Film>();
        tv.setPrefWidth(950);
        tv.setPrefHeight(600);
        // Add the TableView to the box
        box.getChildren().add(tv);
        // Create the various columns and add them to tv
        TableColumn<Film,String> c1 = new TableColumn<Film,String>("Title");
        c1.setCellValueFactory((cd)->
                new ReadOnlyStringWrapper(cd.getValue().getTitle()));
        tv.getColumns().add(c1);
        TableColumn<Film,String> c2 =
                          new TableColumn<Film,String>("Countries");
        c2.setCellValueFactory((cd)->
                new ReadOnlyStringWrapper(cd.getValue()
                                            .getCountries()));
        tv.getColumns().add(c2);
        // NOTE: must be Number, not Integer
        TableColumn<Film,Number> c3 = new TableColumn<Film,Number>("Year");
        // Note: there is no ReadOnlyNumberWrapper !!
        c3.setCellValueFactory((cd)->
                new ReadOnlyIntegerWrapper(cd.getValue()
                                             .getYear()));
        tv.getColumns().add(c3);
        // Must be Number, not Float
        TableColumn<Film,Number> c4 = new
                        TableColumn<Film,Number>("Billion RMB");
        c4.setCellValueFactory((cd)->
                new ReadOnlyFloatWrapper(cd.getValue()
                                           .getBillionRMB()));
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
