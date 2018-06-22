import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Film2 {
    private SimpleStringProperty  title;
    private SimpleStringProperty  countries;
    private SimpleIntegerProperty year;
    private SimpleFloatProperty   billionRMB;

    public Film2(String title, String countries,
                int year, float billionRMB) {
        this.title = new SimpleStringProperty(title);
        this.countries = new SimpleStringProperty(countries);
        this.year = new SimpleIntegerProperty(year);
        this.billionRMB = new SimpleFloatProperty(billionRMB);
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleStringProperty countriesProperty() {
        return countries;
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }

    public SimpleFloatProperty billionRMBProperty() {
        return billionRMB;
    }
}
