
public class Film {
    private String title;
    private String countries;
    private int    year;
    private float  billionRMB;

    public Film(String title, String countries,
                int year, float billionRMB) {
        this.title = title;
        this.countries = countries;
        this.year = year;
        this.billionRMB = billionRMB;
    }

    public String getTitle() {
        return title;
    }

    public String getCountries() {
        return countries;
    }

    public int getYear() {
        return year;
    }

    public float getBillionRMB() {
        return billionRMB;
    }
}
