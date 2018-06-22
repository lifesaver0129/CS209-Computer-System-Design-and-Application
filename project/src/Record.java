import javafx.beans.property.SimpleStringProperty;

/**
 * Record class is used to store every line of data and display in the table of final display program.<br>
 * There are eight lines in every record, the detail can be found in the following statement. <br>
 * Eight value of the record is format in simple string property, which is required by javaFx.
 *
 * @author Yuxing HU
 * @author Xiaorong HE
 *
 * @see javafx.beans.property.SimpleStringProperty
 */
public class Record  {

    /**
     * Each simple string property represent a value in line.
     */
    private SimpleStringProperty f1, f2, f3, f4, f5, f6, f7, f8;

    /**
     * The get method for f1, return the value of id.
     *
     * @return SimpleStringProperty id
     */
    public String getF1() {
        return f1.get();
    }

    /**
     * The get method for f2, return the value of date, including time and date of the quake.
     *
     * @return SimpleStringProperty UTC_date
     */
    public String getF2() {
        return f2.get();
    }

    /**
     * The get method for f3, return the value of the latitude of the quake.
     *
     * @return SimpleStringProperty Latitude
     */
    public String getF3() {
        return f3.get();
    }

    /**
     * The get method for f4, return the value of the Longitude of the quake.
     *
     * @return SimpleStringProperty Longitude
     */
    public String getF4() {
        return f4.get();
    }

    /**
     * The get method for f5, return the value of the Depth of the quake.
     *
     * @return SimpleStringProperty Depth
     */
    public String getF5() {
        return f5.get();
    }

    /**
     * The get method for f6, return the value of the Magnitude of the quake.
     *
     * @return SimpleStringProperty Magnitude
     */
    public String getF6() {
        return f6.get();
    }

    /**
     * The get method for f7, return the value of the Region of the quake.
     *
     * @return SimpleStringProperty Region
     */
    public String getF7() {
        return f7.get();
    }

    /**
     * The get method for f8, return the value of the Area ID of the quake.
     * This area id is create by the trigger that built in the sql.
     *
     * @return SimpleStringProperty Area ID
     */
    public String getF8() {
        return f8.get();
    }

    /**
     * The construction method of record. Record is used to added in the dadaist and display in the program.
     *
     * @param f1 Quake id
     * @param f2 The date and time that quake awake
     * @param f3 Latitude of the quake
     * @param f4 Longitude of the quake
     * @param f5 Depth of the quake
     * @param f6 Magnitude of the quake
     * @param f7 Region of the quake
     * @param f8 Area ID based where the quake is occurred
     */
    Record(String f1, String f2, String f3, String f4, String f5, String f6, String f7, String f8) {
        this.f1 = new SimpleStringProperty(f1);
        this.f2 = new SimpleStringProperty(f2);
        this.f3 = new SimpleStringProperty(f3);
        this.f4 = new SimpleStringProperty(f4);
        this.f5 = new SimpleStringProperty(f5);
        this.f6 = new SimpleStringProperty(f6);
        this.f7 = new SimpleStringProperty(f7);
        this.f8 = new SimpleStringProperty(f8);
    }
}