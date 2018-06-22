import java.text.DecimalFormat;

public class Film3 {
    private String title;
    private String countries;
    private int    year;
    private float  billionRMB;
    private final DecimalFormat df = new DecimalFormat("#########0.000");
    private final int           padding = 15;

    public Film3(String title, String countries,
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

    public String getYear() {
        return Integer.toString(year);
    }

    public String getBillionRMB() {
        String s = df.format(billionRMB);
        int    len = s.length();
        if (len < padding) {
          return "               ".substring(len) + s;
        } else {
          return s;
        } 
    }
}
