//
// EarthQuakes project
//
//  GUI design: on top a GridPane to select the quakes.
//              Below a paned tab with:
//                 One tab for a table view of data
//                 One tab for a map
//                 Several tabs for various charts
import java.util.ArrayList;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.time.LocalDate;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

public class EarthQuakes1 extends Application {
    // Screen size
    private Rectangle2D screenBounds 
                  = Screen.getPrimary().getVisualBounds();
    // Keep track of the current location of the window
    private double    x;
    private double    y;
    // Keep track of the location of the current executable
    // file because the image and the css file are expected
    // to be found in the same directory.
    private String directory = EarthQuakes.class
                              .getProtectionDomain()
                              .getCodeSource()
                              .getLocation()
                              .toString();
    private SimpleDateFormat       fmt = null;
    private DataLoader1            dataSource = null;
    // Widget for entering criteria.
    // Here to be easily accessible by all methods.
    private DatePicker fromDate = null; 
    private String     minDate = null;
    private DatePicker toDate = null; 
    private String     maxDate = null;
    private Slider     magnitude = null;
    private ComboBox<String>   region = new ComboBox<String>(); 
    // Observable list to back the region ComboBox
    private ObservableList<String> regions = null;
    // Observable list that corresponds to the current selection
    private ObservableList<Quake>  quakes = null;
    // The status Label simply displays how many quakes are
    // in the current selection
    private final Label            status = new Label();
    // === Tabs ===
    private static final String[]  tabLabels = {"Data",
                                                "Mercator Map",
                                                "Eckert IV Map",
                                                "Chart by Magnitude",
                                                "Chart by Date"};
    // Data tab
    private final TableView<Quake> tableView = new TableView<Quake>();
    // --- Maps ---
    private final StackPane stpMercator = new StackPane();
    private       WorldMap  wmm;
    private       int       mW;
    private       int       mH;
    private final StackPane stpEckert = new StackPane();
    private       WorldMap  wme;
    private       int       eW;
    private       int       eH;
    // --- Charts ---
    // Chart 1 tab. Bar chart showing how many quakes in the selection
    // per range of magnitude
    private final CategoryAxis xAxisChart1 = new CategoryAxis();
    private final NumberAxis   yAxisChart1 = new NumberAxis();
    private final BarChart<String,Number> chart1 =
                          new BarChart<String,Number>(xAxisChart1,yAxisChart1);
    private final XYChart.Series<String,Number> seriesChart1 =
                                new XYChart.Series<String,Number>();
    private String[] magnitudeRanges = {"Under 3.0",
                                        "3.0 to 4.0",
                                        "4.0 to 5.0",
                                        "5.0 to 6.0",
                                        "6.0 and over"};
    private final  ObservableList<XYChart.Data<String,Number>> chart1Data =
                                           FXCollections.observableArrayList();
    // Chart 2 tab. Bar chart showing how many quakes in the selection
    // per day
    private final CategoryAxis xAxisChart2 = new CategoryAxis();
    private final NumberAxis   yAxisChart2 = new NumberAxis();
    private final BarChart<String,Number> chart2 =
                          new BarChart<String,Number>(xAxisChart2,yAxisChart2);
    private final XYChart.Series<String,Number> seriesChart2 =
                                new XYChart.Series<String,Number>();
    private final  ObservableList<XYChart.Data<String,Number>> chart2Data =
                                           FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    public void init() {
        dataSource = new DataLoader1();
        minDate = dataSource.getMinDate();
        maxDate = dataSource.getMaxDate();
        try {
          quakes = FXCollections.observableArrayList(dataSource
                                            .getQuakes(minDate,
                                                       maxDate,
                                                       0f,
                                                       Quake.WORLDWIDE));
          // System.out.println("Quake count " + quakes.size());
          regions = FXCollections.observableArrayList(
                  new ArrayList<String>(dataSource.getRegions()));
          tableView.setItems(quakes);
        } catch (Exception e) {
          System.err.println("load() error: " + e.getMessage());
          e.printStackTrace();
          System.exit(1);
        }
    }

    private void refreshMaps() {
       float magnitude;
       int   diameter;
       int[] xy;
       ObservableList<Node> paneChildren;

       // Remove existing canvases
       paneChildren = stpMercator.getChildren();
       if (paneChildren.size() > 1) {
         paneChildren.remove(1, paneChildren.size());
       }
       paneChildren = stpEckert.getChildren();
       if (paneChildren.size() > 1) {
         paneChildren.remove(1, paneChildren.size());
       }
       //
       Canvas cvMercator = new Canvas(mW, mH);
       GraphicsContext gcm = cvMercator.getGraphicsContext2D();
       Canvas cvEckert = new Canvas(eW, eH);
       GraphicsContext gce = cvEckert.getGraphicsContext2D();
       gcm.setStroke(Color.RED);
       gcm.setLineWidth(2);
       gce.setStroke(Color.RED);
       gce.setLineWidth(2);
       for (Quake q: quakes) {
         diameter = (int)(5 * q.getMagnitude() - 4);
         // System.out.println("Diameter: " + diameter + " (magnitude "
         //                    + q.getMagnitude() + ")");
         // Mercator
         xy = wmm.imgxy(q.getLatitude(), q.getLongitude());
         // (x, y, w, h)
         gcm.strokeOval(xy[0] - diameter / 2, xy[1] - diameter / 2,
                        diameter, diameter);
         // Eckert 
         xy = wme.imgxy(q.getLatitude(), q.getLongitude());
         gce.strokeOval(xy[0] - diameter / 2, xy[1] - diameter / 2,
                        diameter, diameter);
       }
       stpMercator.getChildren().add(cvMercator);
       stpEckert.getChildren().add(cvEckert);
    }

    private void refreshCharts() {
    //private static ObservableList<XYChart.Data<String,Number>> chart2Data =
       chart1Data.clear();
       chart2Data.clear();
       TreeMap<String,Integer> counters1 = new TreeMap<String,Integer>();
       TreeMap<String,Integer> counters2 = new TreeMap<String,Integer>();
       int     range;
       String  date;
       Integer counter;
       float   magnitude;
       // Would be better done with a database query ...
       // Initialize counters for the magnitude chart
       for (int i = 0; i < magnitudeRanges.length; i++) {
         counters1.put(magnitudeRanges[i], 0);
       }
       for (Quake q: quakes) {
          magnitude = q.getMagnitude();
          if (magnitude < 3.0) {
             range = 0;
          } else if (magnitude < 4.0) {
             range = 1;
          } else if (magnitude < 5.0) {
             range = 2;
          } else if (magnitude < 6.0) {
             range = 3;
          } else {
             range = 4;
          }
          date = q.getDate().substring(0, 10);
          counter = counters1.get(magnitudeRanges[range]);
          counter++;
          counters1.put(magnitudeRanges[range], counter);
          counter = counters2.get(date);
          if (counter == null) {
            counter = 1;
          } else {
            counter++;
          }
          counters2.put(date, counter);
       }
       for (int i = 0; i < magnitudeRanges.length; i++) {
         chart1Data.add(new XYChart.Data<String,Number>(magnitudeRanges[i],
                                     counters1.get(magnitudeRanges[i])));
         // System.out.println(magnitudeRanges[i] + " "
         //                    + counters1.get(magnitudeRanges[i]));
       }
       date = counters2.firstKey();
       while (date != null) {
         chart2Data.add(new XYChart.Data<String,Number>(date,
                                     counters2.get(date)));
         // System.out.println(date + " "
         //                    + counters2.get(date));
         date = counters2.higherKey(date);
       }
       seriesChart1.setData(chart1Data);
       seriesChart2.setData(chart2Data);
    }

    private void buildEntry(GridPane entry) {
        fromDate = new DatePicker();
        fromDate.setValue(LocalDate.parse(minDate));
        Label labelFrom = new Label("From:");
        toDate = new DatePicker();
        toDate.setValue(LocalDate.parse(maxDate));
        Label labelTo = new Label("To:");
        // Slider should go from 1 to 9, with 0.5 increments
        // Default is 1.
        magnitude = new Slider(1, 9, 1);
        magnitude.setValue(1); // Select all
        magnitude.setShowTickLabels(true);
        magnitude.setShowTickMarks(true);
        magnitude.setMajorTickUnit(1);
        magnitude.setMinorTickCount(1);
        magnitude.setBlockIncrement(0.5f);
        Label labelMag = new Label("Magnitude ≥");
        region.setItems(regions);
        region.setValue(Quake.WORLDWIDE);
        Label labelReg = new Label("Region");
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction((e)->{
          quakes = FXCollections.observableArrayList(dataSource
                                      .getQuakes(fromDate.getValue()
                                                         .toString(),
                                                 toDate.getValue()
                                                         .toString(),
                                                 (float)magnitude.getValue(),
                                                 region.getValue()));
          status.setText(Integer.toString(quakes.size()) + " quakes selected");
          tableView.setItems(quakes);
          refreshMaps();
          refreshCharts();});
        entry.setPadding(new Insets(10));
        // Horizontal and vertical gap between cells
        entry.setHgap(10);
        entry.setVgap(5);
        //GridPane.setConstraints(label, 2, 0); // column=2 row=0

        GridPane.setConstraints(labelFrom, 0, 1);  // column 0, row 1
        GridPane.setConstraints(fromDate, 1, 1);
        GridPane.setConstraints(labelTo, 3, 1);
        GridPane.setConstraints(toDate, 4, 1);
        GridPane.setConstraints(labelMag, 0, 2);  // column 0, row 2
        GridPane.setConstraints(magnitude, 1, 2);
        GridPane.setMargin(magnitude, new Insets(20, 0, 0, 0));
        GridPane.setConstraints(labelReg, 0, 3);  // column 0, row 3
        GridPane.setConstraints(region, 1, 3);
        GridPane.setConstraints(searchBtn, 1, 4, 3, 1); // column 1, row 4,
                                                     // spans 3 columns
        GridPane.setMargin(searchBtn, new Insets(20));
        // Add a "status" (number of quakes in the selection)
        GridPane.setConstraints(status, 6, 4); // column 6, row 4
        // don't forget to add children to gridpane
        entry.getChildren().addAll(labelFrom, fromDate,
                                   labelTo, toDate,
                                   labelMag, magnitude,
                                   labelReg, region,
                                   searchBtn,
                                   status); 
    }

    public void start(Stage stage) {
        stage.setTitle("Earthquakes");
        // stage.setResizable(false);
        Group root = new Group();
        Scene scene = new Scene(root);
        // Associate a style-sheet with the application.
        // If the file isn't found, a warning is issued
        // but there is no exception thrown and the application
        // runs with the default style-sheet
        scene.getStylesheets().add(directory + "earthquakes.css");
        // Create a vertical box, add spacing to it.
        VBox vBox = new VBox();
        vBox.setSpacing(8);
        // Ask for centering
        // vBox.setAlignment(Pos.CENTER);
        root.getChildren().add(vBox);
        // Create a GridPane and add it to the VBox
        GridPane entry = new GridPane();
        vBox.getChildren().add(entry);
        buildEntry(entry);
        // Set the TableView stuff (columns, and so forth)
        TableColumn<Quake,Integer> idCol =
                  new TableColumn<Quake,Integer>("Id");
        idCol.setCellValueFactory(cellData->
                    cellData.getValue().idProperty().asObject());
        tableView.getColumns().add(idCol);
        TableColumn<Quake,String> dateCol =
                  new TableColumn<Quake,String>("UTC Date");
        dateCol.setCellValueFactory(cellData->
                    cellData.getValue().dateProperty());
        tableView.getColumns().add(dateCol);
        TableColumn<Quake,Float> latitudeCol =
                  new TableColumn<Quake,Float>("Latitude");
        latitudeCol.setCellValueFactory(cellData->
                    cellData.getValue().latitudeProperty().asObject());
        tableView.getColumns().add(latitudeCol);
        TableColumn<Quake,Float> longitudeCol =
                  new TableColumn<Quake,Float>("Longitude");
        longitudeCol.setCellValueFactory(cellData->
                    cellData.getValue().longitudeProperty().asObject());
        tableView.getColumns().add(longitudeCol);
        TableColumn<Quake,Integer> depthCol =
                  new TableColumn<Quake,Integer>("Depth (km)");
        depthCol.setCellValueFactory(cellData->
                    cellData.getValue().depthProperty().asObject());
        tableView.getColumns().add(depthCol);
        TableColumn<Quake,Float> magnitudeCol =
                  new TableColumn<Quake,Float>("Magnitude");
        magnitudeCol.setCellValueFactory(cellData->
                    cellData.getValue().magnitudeProperty().asObject());
        tableView.getColumns().add(magnitudeCol);
        TableColumn<Quake,String> regionCol =
                  new TableColumn<Quake,String>("Region");
        regionCol.setCellValueFactory(cellData->
                    cellData.getValue().regionProperty());
        tableView.getColumns().add(regionCol);
        tableView.setPrefWidth(900);
        tableView.setPrefHeight(500);
        // Prepare chart 1
        chart1.setTitle("Quakes by Magnitude");
        xAxisChart1.setLabel("Magnitude");
        xAxisChart1.setAutoRanging(true);
        yAxisChart1.setLabel("Number of Quakes");
        yAxisChart1.setAutoRanging(true);
        chart1.setLegendVisible(false);
        chart1.getData().add(seriesChart1);
        // Prepare chart 2
        chart2.setTitle("Quakes by Date");
        xAxisChart2.setLabel("Date");
        xAxisChart2.setAutoRanging(true);
        yAxisChart2.setLabel("Number of Quakes");
        yAxisChart2.setAutoRanging(true);
        chart2.setLegendVisible(false);
        chart2.getData().add(seriesChart2);
        // Create a TabPane
        TabPane pane = new TabPane();
        vBox.getChildren().add(pane);
        Tab      tab;
        VBox     tabBox;
        URL      url;
        Image     image;
        ImageView iv;
        // Create tabs
        for (int i = 0; i < tabLabels.length; i++) {
          tab = new Tab();
          tabBox = new VBox();
          tabBox.setPadding(new Insets(20));
          switch(i) {
            case 0:
                 ScrollPane scrPane = new ScrollPane();
                 tabBox.getChildren().add(scrPane);
                 scrPane.setContent(tableView);
                 break;
            case 1: // Mercator Map
                 tabBox.getChildren().add(stpMercator);
                 url = this.getClass()
                           .getClassLoader()
                           .getResource("Mercator.jpg");
                 if (url != null) {
                   image = new Image(url.toString());
                   mW = (int)image.getWidth();
                   mH = (int)image.getHeight();
                   wmm = new WorldMap(image, WorldMap.Projection.MERCATOR, 180);
                   iv = new ImageView(image);
                   stpMercator.getChildren().add(iv);
                 }
                 break;
            case 2: // Eckert IV Map
                 tabBox.getChildren().add(stpEckert);
                 url = this.getClass()
                           .getClassLoader()
                           .getResource("Eckert_IV.png");
                 if (url != null) {
                   image = new Image(url.toString());
                   eW = (int)image.getWidth();
                   eH = (int)image.getHeight();
                   wme = new WorldMap(image, WorldMap.Projection.ECKERT_IV,
                                      180);
                   iv = new ImageView(image);
                   stpEckert.getChildren().add(iv);
                 }
                 break;
            case 3:
                 tabBox.getChildren().add(chart1);
                 break;
            case 4:
                 tabBox.getChildren().add(chart2);
                 break;
            default:
                 tabBox.getChildren().add(new Label("Content of tab "
                                           + Integer.toString(1+i)));
                 break;
          }
          tab.setText(tabLabels[i]);
          tab.setClosable(false);
          tab.setContent(tabBox);
          pane.getTabs().add(tab);
        }
        status.setText(Integer.toString(quakes.size()) + " quakes selected");
        // Set up maps and charts
        refreshMaps();
        refreshCharts();
        // Everything is ready, display
        stage.setScene(scene);
        stage.show();
        // Center. You need to show the stage first before you can
        // get its dimensions (width and height), which is why it's
        // here. It's not displayed on the screen yet, it will be
        // when we exit the function.
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);  
        // Record the initial position of the window
        this.x = stage.getX();
        this.y = stage.getY();
    }
}
