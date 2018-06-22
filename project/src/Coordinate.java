
/**
 * This class models a position on the Mercator world map in terms of coordinates.<br>
 * This is a value-based class.<br>
 * This class stores a position.<br>
 *
 * @author Yuxing HU
 * @author Xiaorong HE
 * @see FlashCanvas
 */

public class Coordinate {

    /**
     * the x-coordinate, measured in pixels
     */
    private double x;
    /**
     * the y-coordinate, measured in pixels
     */
    private double y;

    /**
     * This method convert a position, which is expressed by a latitude and a longitude,<br>
     * to a position in terms of a coordinate.
     *
     * @param latitude  the latitude of the position, measured in degree
     * @param longitude the longitude of the position, measured in degree
     * @param width     the width of the map, measured in pixels
     * @param height    the height of the map, measured in pixels
     */
    public Coordinate(double latitude, double longitude, double width, double height) {
        if (longitude >= 0) {
            this.x = width / 2 - (180 - longitude) * width / 360;
        } else if (longitude < 0) {
            this.x = width / 2 - longitude * width / 360;
        }
        this.y = height / 2
                + (Math.log(Math.tan((90 - latitude * height / width * 90 / 78.15) * Math.PI / 360))
                / (Math.PI / 180)) * height / 180;
    }

    /**
     * Get the x value of the coordinate.
     *
     * @return Return a double value.
     */
    public double getX() {
        return x;
    }

    /**
     * Get the y value of the coordinate.
     *
     * @return Return a double value.
     */
    public double getY() {
        return y;
    }
}