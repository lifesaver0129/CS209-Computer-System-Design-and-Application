import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sqlite.SQLiteException;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

/**
 * The controller is about to grab all the framework of the program.
 * Please note that the current version of program can only be run on Java 9, and need to import outside jar file.
 *
 * @author Yuxing HU
 * @author Xiaorong HE
 */
public class Controller implements Initializable {
    /**
     * columnF1 shows the ID of each earthquake.
     */
    @FXML
    private TableColumn columnF1;
    /**
     * columnF2 shows the time of each earthquake.
     */
    @FXML
    private TableColumn columnF2;
    /**
     * columnF3 shows the latitude of each earthquake location.
     */
    @FXML
    private TableColumn columnF3;
    /**
     * columnF4 shows the longitude of each earthquake location.
     */
    @FXML
    private TableColumn columnF4;
    /**
     * columnF5 shows the depth of each earthquake.
     */
    @FXML
    private TableColumn columnF5;
    /**
     * columnF6 shows the magnitude of each earthquake.
     */
    @FXML
    private TableColumn columnF6;
    /**
     * columnF7 shows the region of each earthquake.
     */
    @FXML
    private TableColumn columnF7;
    /**
     * columnF8 shows the areaID of each earthquake.
     */
    @FXML
    private TableColumn columnF8;
    /**
     * tableView contains all the earthquake records.
     */
    @FXML
    private TableView<Record> tableView;
    /**
     * scrollBar is used to select a critical value of minimum magnitude.
     *
     * @see JFXSlider
     */
    @FXML
    private JFXSlider scrollBar;
    /**
     * magnitude is a Label which shows the value of the scrollBar.
     *
     * @see Label
     */
    @FXML
    private Label magnitude;
    /**
     * dateStart is a DatePicker used to select the start date of earthquakes.
     *
     * @see DatePicker
     */
    @FXML
    private DatePicker dateStart;
    /**
     * dateEnd is a DatePicker used to select the end date of earthquakes.
     *
     * @see DatePicker
     */
    @FXML
    private DatePicker dateEnd;
    /**
     * comboBox is a menu used to select earthquakes by regions.
     *
     * @see Canvas JFXComboBox
     */
    @FXML
    private Canvas canvas;
    /**
     * comboBox is a menu used to select earthquakes by regions.
     *
     * @see Canvas
     * @see JFXComboBox
     */
    @FXML
    private JFXComboBox<String> comboBox;
    /**
     * pieChart is a chart which shows each earthquake class and its proportion.
     *
     * @see PieChart
     */
    @FXML
    private PieChart pieChart;
    /**
     * barChart is a chart which shows each earthquake class and its quantity.
     *
     * @see BarChart
     */
    @FXML
    private BarChart<String, Integer> barChart;
    /**
     * regionSet is a set contains all the earthquake regions.
     *
     * @see Set
     */
    private Set<String> regionSet = new HashSet<>();
    /**
     * regionList is an ObservableList contains all the earthquake regions.
     *
     * @see ObservableList
     */
    private ObservableList<String> regionList = FXCollections.observableArrayList();
    /**
     * dataList is an ObservableList contains all the earthquake records.
     *
     * @see ObservableList
     */
    private ObservableList<Record> dataList = FXCollections.observableArrayList();
    /**
     * Either start or is a LocalDate, whose value is transmitted from a DatePicker.
     *
     * @see DatePicker
     * @see LocalDate
     */
    private LocalDate start, end;
    /**
     * pane is a StackPane used to contains elements of the GUI.
     *
     * @see StackPane
     */
    public StackPane pane;
    /**
     * Either width or height is a double, whose value will be determined by the map.
     */
    private double width, height;
    /**
     * A connection created by sql and is used for connecting the database
     */
    private static Connection con = null;
    /**
     * M is a float whose value will be determined by the scrollBar.
     */
    private float M;
    /**
     * allRegion is the worldwide region for earthquake selection.
     */
    private final String allRegion = "-- All the Region --";
    /**
     * Counters for different earthquake classes.
     */
    private int M1, M2, M3, M4, M5 = 0;
    /**
     * coordinates is an ArrayList contains every coordinate for every earthquake location.
     */
    ArrayList<Coordinate> coordinates = new ArrayList<>();
    /**
     * A string value which store the value of current region
     */
    private String R;


    /**
     * Initialize is the method to initialize the GUI when you run the program.
     *
     * @param url input of loading site of current data
     * @param rb  default parameter of the FX implement
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showElements();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**
         * dayCellFactory1 is used to forbidden the selection of the end date which is before the start date.
         */
        final Callback<DatePicker, DateCell> dayCellFactory1 = new Callback<>() {
            /**
             * A call function to get the date instance and return the date.
             * @param datePicker The date that grab from the calender in the program
             * @return DateCell a date which has been processed
             */
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(dateStart.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        dateEnd.setDayCellFactory(dayCellFactory1);
        dateEnd.setValue(LocalDate.now());

        /**
         * dayCellFactory2 is used to forbidden the selection of the start date which is after the end date.
         */
        final Callback<DatePicker, DateCell> dayCellFactory2 = new Callback<>() {
            /**
             * A call function to get the date instance and return the date.
             * @param datePicker The date that grab from the calender in the program
             * @return DateCell a date which has been processed
             */
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isAfter(dateEnd.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        dateStart.setDayCellFactory(dayCellFactory2);
        dateStart.setValue(dateEnd.getValue().minusWeeks(1));
        /**
         * Load in the Mercator map to pinpoint earthquakes on.
         */
        Image image = new Image("Mercator.jpg");
        width = image.getWidth();
        height = image.getHeight();
        /**
         * Load in the map which has all the pinpoint earthquakes on.
         */
        ImageView iv = new ImageView(image);
        pane.getChildren().add(iv);
        canvas = new FlashCanvas(width, height, coordinates);
        pane.getChildren().add(canvas);
    }

    /**
     * showElements is the most important method to initialize the GUI, which first set pieChart, barChart, scrollBar, comboBox.
     *
     * @throws IOException    [Signals that an I/O exception of some sort has occurred.]
     * @throws SQLException   [An exception that provides information on a database access error or other errors. ]
     * @throws ParseException [Signals that an error has been reached unexpectedly while parsing.]
     * @see javafx.scene.chart.PieChart
     * @see javafx.scene.chart.BarChart
     * @see javafx.scene.control
     */
    public void showElements() throws IOException, SQLException, ParseException {
        pieChart.setTitle("Magnitude Classes");
        XYChart.Series<String, Integer> series0 = new XYChart.Series<>();
        series0.getData().add(new XYChart.Data<>("Minor (<1.0)", M1));
        series0.getData().add(new XYChart.Data<>("Light (1.0-2.9)", M2));
        series0.getData().add(new XYChart.Data<>("Moderate (3.0-4.4)", M3));
        series0.getData().add(new XYChart.Data<>("Strong (4.5-5.9)", M4));
        series0.getData().add(new XYChart.Data<>("Major (>=6.0)", M5));
        barChart.getData().add(series0);
        barChart.setTitle("Magnitude Classes");
        scrollBar.setValue(4);
        dragDetected();
        openDB("earthquakes-1.sqlite");
        grabData();
        if (con != null) {
            try {
                Statement stmt;
                ResultSet rs;
                stmt = con.createStatement();
                rs = stmt.executeQuery("select * from quakes");
                while (rs.next()) {
                    regionSet.add(rs.getString(7));
                }
                regionList.add(allRegion);
                for (String str : regionSet) {
                    regionList.add(str);
                }
                Collections.sort(regionList);
                comboBox.setItems(regionList);
                comboBox.getSelectionModel().select(0);
                rs.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    /**
     * grabData method is used to grab the data from website and then add them into the database.
     * This method will avoid the data which is duplicate.
     *
     * @throws IOException    [Signals that an I/O exception of some sort has occurred.]
     * @throws SQLException   [An exception that provides information on a database access error or other errors. ]
     * @throws ParseException [Signals that an error has been reached unexpectedly while parsing.]
     */
    private static void grabData() throws IOException, SQLException, ParseException {
        Statement st = con.createStatement();
        String commend1 = "select max(UTC_date) from quakes;";
        ResultSet rs = st.executeQuery(commend1);
        String latest = rs.getString(1);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date local = sdf.parse(latest);
        String website = "https://www.emsc-csem.org/Earthquake/?view=";
        System.out.println("Start grabing data from web");
        loop:
        for (int i = 1; i <= 50; i++) {
            String websiteCurrent = website + i;
            Document doc = Jsoup
                    .connect(websiteCurrent)
                    .get();
            Elements table = doc.select("tbody#tbody");
            for (Element t : table) {
                Elements rows = t.select("tr");
                for (Element r : rows) {
                    try {
                        String id = r.attr("id");
                        String date = r.select("a").text();
                        if (date.length() > 21)
                            date = date.substring(date.length() - 21, date.length());
                        Date web = sdf.parse(date);
                        if (web.before(local))
                            break loop;
                        String lat;
                        if (r.select(".tabev2").get(0).text().equals("N"))
                            lat = r.select(".tabev1").get(0).text();
                        else
                            lat = "-" + r.select(".tabev1").get(0).text();
                        String lon;
                        if (r.select(".tabev2").get(1).text().equals("E"))
                            lon = r.select(".tabev1").get(1).text();
                        else
                            lon = "-" + r.select(".tabev1").get(1).text();
                        String depth = r.select(".tabev3").get(0).text();
                        String mag = r.select(".tabev2").get(2).text();
                        String region = r.select(".tb_region").get(0).text();
                        String commend2 = "insert into quakes values(" + id + ", '" + date + "', " + lat + ", " + lon
                                + ", " + depth + ", " + mag + ", '" + region + "', 0)";
                        System.out.println(commend2);
                        st.executeUpdate(commend2);
                    } catch (IndexOutOfBoundsException | NumberFormatException | SQLiteException | ParseException e) {
                        continue;
                    }
                }
            }
        }
        System.out.println("Grabing data finished");
        con.commit();
    }

    /**
     * updateData update the GUI when click the botton Search. The new date are determined by condition selections through the contrl elements.
     */
    public void updateData() {
        filterSelect();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Minor (<1.0)", M1),
                new PieChart.Data("Light (1.0-2.9)", M2),
                new PieChart.Data("Moderate (3.0-4.4)", M3),
                new PieChart.Data("Strong (4.5-5.9)", M4),
                new PieChart.Data("Major (>=6.0)", M5));
        pieChart.setData(pieChartData);
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().clear();
        series.getData().add(new XYChart.Data<>("Minor (<1.0)", M1));
        series.getData().add(new XYChart.Data<>("Light (1.0-2.9)", M2));
        series.getData().add(new XYChart.Data<>("Moderate (3.0-4.4)", M3));
        series.getData().add(new XYChart.Data<>("Strong (4.5-5.9)", M4));
        series.getData().add(new XYChart.Data<>("Major (>=6.0)", M5));
        barChart.getData().clear();
        barChart.getData().add(series);
        columnF1.setCellValueFactory(new PropertyValueFactory<>("f1"));
        columnF2.setCellValueFactory(new PropertyValueFactory<>("f2"));
        columnF3.setCellValueFactory(new PropertyValueFactory<>("f3"));
        columnF4.setCellValueFactory(new PropertyValueFactory<>("f4"));
        columnF5.setCellValueFactory(new PropertyValueFactory<>("f5"));
        columnF6.setCellValueFactory(new PropertyValueFactory<>("f6"));
        columnF7.setCellValueFactory(new PropertyValueFactory<>("f7"));
        columnF8.setCellValueFactory(new PropertyValueFactory<>("f8"));
        tableView.setItems(dataList);
    }

    /**
     * Open all the quake info from the database
     *
     * @param dbPath The abstract path of the database
     */
    private static void openDB(String dbPath) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            System.err.println("Cannot find the driver.");
            System.exit(1);
        }
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            con.setAutoCommit(false);
            System.err.println("Successfully connected to the database.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * dragDetected is used to update the Label for the magnitude.
     *
     * @see Record
     */
    public void dragDetected() {
        String m = String.format("%.1f", scrollBar.getValue());
        magnitude.setText(m);
        M = Float.parseFloat(String.format("%.1f", scrollBar.getValue()));
    }

    /**
     * Select all the data that changed by the user in the origin
     */
    private void filterSelect() {
        dataList.clear();
        coordinates.clear();
        if (dateStart.getValue() != null) {
            if (dateEnd.getValue() != null) {
                start = dateStart.getValue();
                end = dateEnd.getValue();
            } else {
                start = dateStart.getValue();
                end = LocalDate.now();
            }
        } else {
            if (dateEnd.getValue() != null) {
                start = LocalDate.MIN.plusDays(1);
                end = dateEnd.getValue();
            } else {
                start = LocalDate.MIN.plusDays(1);
                end = LocalDate.now();
            }
        }
        String Start = start.minusDays(1).toString() + " 23:59:59";
        String End = end.plusDays(1).toString();
        R = comboBox.getValue();
        if (con != null) {
            try {
                Statement stmt1;
                ResultSet rs1;
                stmt1 = con.createStatement();
                if (R == allRegion) {
                    rs1 = stmt1.executeQuery("select * from quakes where magnitude>='" + M + "' and UTC_Date between '"
                            + Start + "' and '" + End + "'");
                } else {
                    rs1 = stmt1.executeQuery("select * from quakes where magnitude>='" + M + "' and UTC_Date between '"
                            + Start + "' and '" + End + "' and Region=='" + R + "' ");
                }
                while (rs1.next()) {
                    Record record = new Record(rs1.getString(1), rs1.getString(2),
                            rs1.getString(3), rs1.getString(4),
                            rs1.getString(5), rs1.getString(6),
                            rs1.getString(7), rs1.getString(8));

                    if (Float.parseFloat(rs1.getString(6)) < 1) {
                        M1++;
                    } else if (Float.parseFloat(rs1.getString(6)) < 3) {
                        M2++;
                    } else if (Float.parseFloat(rs1.getString(6)) < 4.5) {
                        M3++;
                    } else if (Float.parseFloat(rs1.getString(6)) < 6) {
                        M4++;
                    } else {
                        M5++;
                    }
                    Coordinate coordinate = new Coordinate(Double.parseDouble(rs1.getString(3)),
                            Double.parseDouble(rs1.getString(4)), width, height);
                    dataList.add(record);
                    coordinates.add(coordinate);
                }
                rs1.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }
}