import java.util.Date;
import javafx.beans.property.*;

public class Quake {
    private IntegerProperty id;
    private StringProperty  date;
    private FloatProperty   latitude;
    private FloatProperty   longitude;
    private IntegerProperty depth;
    private FloatProperty   magnitude;
    private StringProperty  region;

    // Constant to be used elsewhere 
    public static final String WORLDWIDE = "--- World Wide ---";

    public Quake(int id, String date,
                 float latitude, float longitude,
                 int depth, float magnitude,
                 String region) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleStringProperty(date);
        this.latitude = new SimpleFloatProperty(latitude);
        this.longitude = new SimpleFloatProperty(longitude);
        this.depth = new SimpleIntegerProperty(depth);
        this.magnitude = new SimpleFloatProperty(magnitude);
        this.region = new SimpleStringProperty(region);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.getValue();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public String getDate() {
        return date.getValue();
    }

    public FloatProperty latitudeProperty() {
        return latitude;
    }

    public float getLatitude() {
        return latitude.getValue();
    }

    public FloatProperty longitudeProperty() {
        return longitude;
    }

    public float getLongitude() {
        return longitude.getValue();
    }

    public IntegerProperty depthProperty() {
        return depth;
    }

    public int getDepth() {
        return depth.getValue();
    }

    public FloatProperty magnitudeProperty() {
        return magnitude;
    }

    public float getMagnitude() {
        return magnitude.getValue();
    }

    public StringProperty regionProperty() {
        return region;
    }

    public String getRegion() {
        return region.getValue();
    }

}
