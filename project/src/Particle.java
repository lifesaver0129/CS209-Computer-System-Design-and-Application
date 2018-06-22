import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Particle is used to draw shapes and some apply to visual effects, and was blend color overlapping objects and animation objects.
 *
 * @author Yuxing HU
 * @author Xiaorong HE
 * @see javafx.scene.canvas.GraphicsContext
 * @see javafx.scene.paint.Color
 * @see javafx.scene.paint.Paint
 */
public class Particle {
    /**
     * Gravity element which used to control drop speed.
     */
    private static final double GRAVITY = 0.04;
    /**
     * Double instance alpha is used to calculate the next partial of spark.
     */
    double alpha;
    /**
     * Easing is a parameter to calculate the the next partial of spark just like alpha.
     */
    final double easing;
    /**
     * Fade is a double instance to set when a spark is about to fade away.
     */
    double fade;
    /**
     * PosX is the initial coordinate x-axis value of the spark.
     */
    double posX;
    /**
     * PosY is the initial coordinate y-axis value of the spark.
     */
    double posY;
    /**
     * VelX is the updated coordinate x-axis value of the spark.
     */
    double velX;
    /**
     * VelY is the updated coordinate y-axis value of the spark.
     */
    double velY;
    /**
     * TargetX is an parameter used for calculate the right x position in the triangle graph.
     */
    final double targetX;
    /**
     * TargetY is an parameter used for calculate the right y position in the triangle graph.
     */
    final double targetY;
    /**
     * Used to drop the color on the map board.
     */
    final Paint color;
    /**
     * Size is used to control the size of spark on the map.
     */
    final int size;
    /**
     * usePhysics instance is used to control whether to consider the gravity.
     */
    final boolean usePhysics;
    /**
     * hasTail instance is used to control whether the spark will has the tail effect.
     */
    final boolean hasTail;
    /**
     * lastPosX is used to determine last point on the map to disappear on the x-axis.
     */
    double lastPosX;
    /**
     * lastPosY is used to determine last point on the map to disappear on the y-axis.
     */
    double lastPosY;

    /**
     * Particle class is used to initialize the origin firework spark particle.
     *
     * @param posX       Initial position of value x
     * @param posY       Initial position of value y
     * @param velX       An update parameter of x
     * @param velY       An update parameter of y
     * @param targetX    New position of particle x
     * @param targetY    New position of particle y
     * @param color      The particle's color
     * @param size       The size of particle
     * @param usePhysics Boolean instance to determine whether to implement gravity of article
     * @param hasTail    Boolean instance to determine whether to implement tail of particle.
     */
    public Particle(double posX, double posY, double velX, double velY, double targetX, double targetY, Paint color,
                    int size, boolean usePhysics, boolean hasTail) {
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.color = color;
        this.size = size;
        this.usePhysics = usePhysics;
        this.hasTail = hasTail;
        this.alpha = 1;
        this.easing = 0.0005 + Math.random() * 0.0045;
        this.fade = 0.03 + Math.random() * 0.18;
    }

    /**
     * The update method of the next spark's position.
     *
     * @return void method, nothing to return
     */
    public boolean update() {
        lastPosX = posX;
        lastPosY = posY;
        if (this.usePhysics) {
            velY += GRAVITY;
            posY += velY;
            this.alpha -= this.fade;
        } else {
            double distance = (targetY - posY);
            posY += distance * (0.01 + easing);
            alpha = Math.min(distance * distance * 0.00005, 1);
        }
        posX += velX;
        return alpha < 0.85;
    }

    /**
     * Drawing function of the whole map, adding particle to it.
     *
     * @param context A graphic component for method to load
     */
    public void draw(GraphicsContext context) {
        final double x = Math.round(posX);
        final double y = Math.round(posY);
        final double xVel = (x - lastPosX) * -5;
        final double yVel = (y - lastPosY) * -5;
        context.setGlobalAlpha(Math.random() * this.alpha);
        context.setFill(color);
        context.fillOval(x - size, y - size, size + size, size + size);
        if (hasTail) {
            context.setFill(Color.rgb(255, 255, 255, 0.3));
            context.fillPolygon(new double[]{posX + 1.5, posX + xVel, posX - 1.5},
                    new double[]{posY, posY + yVel, posY}, 3);
        }
    }
}