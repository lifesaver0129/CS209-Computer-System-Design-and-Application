import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used to draw shapes, apply to visual effects, and blend color overlapping objects and animation objects.
 *
 * @author Yuxing HU
 * @author Xiaorong HE
 * @see javafx.scene.canvas.Canvas
 * @see javafx.scene.canvas.GraphicsContext
 * @see javafx.animation.AnimationTimer
 * @see javafx.scene.effect.BlendMode
 * @see javafx.scene.paint
 * @see java.util.ArrayList
 * @see java.util.Iterator
 * @see java.util.List
 * @see Particle
 */
public class FlashCanvas extends Canvas {
    /**
     * A Timer instance to decide the time
     */
    private AnimationTimer timer;
    /**
     * A link style list that store all the quake particles
     */
    private final List<Particle> particles = new ArrayList<Particle>();
    /**
     * A array style list that store all the color of the particles
     */
    private final Paint[] colors;
    /**
     * countDown is the value of the graphs of canvas
     */
    private int countDown = 10;
    /**
     * A Graphic context value of current canvas
     */
    private GraphicsContext gc;

    /**
     * This method create a color palette of 180 colors, create animation timer that will be called every frame and create a canvas.
     *
     * @param width       the width of the map, measured in pixels
     * @param height      the height of the map, measured in pixels
     * @param coordinates the coordinates of all the earthquake locations
     * @see Coordinate
     */
    public FlashCanvas(double width, double height, ArrayList<Coordinate> coordinates) {
        /**
         * Generate from the canvas class
         */
        super(width, height);
        this.setBlendMode(BlendMode.ADD);
        gc = getGraphicsContext2D();
        colors = new Paint[181];
        colors[0] = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE),
                new Stop(0.2, Color.hsb(59, 0.38, 1)), new Stop(0.6, Color.hsb(59, 0.38, 1, 0.1)),
                new Stop(1, Color.hsb(59, 0.38, 1, 0)));
        for (int h = 0; h < 360; h += 2) {
            colors[1 + (h / 2)] = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.WHITE), new Stop(0.2, Color.hsb(h, 1, 1)),
                    new Stop(0.6, Color.hsb(h, 1, 1, 0.1)), new Stop(1, Color.hsb(h, 1, 1, 0)));
        }
        timer = new AnimationTimer() {
            /**
             * This method clear area with transparent black,draw marks,and countdown to launching the next time.
             * @param now the time of the first draw
             *            @see Coordinate
             */
            @Override
            public void handle(long now) {
                gc.setFill(Color.rgb(0, 0, 0, 0.2));
                gc.fillRect(0, 0, 1024, 708);
                drawFireworks(gc);
                Iterator<Coordinate> iter = coordinates.iterator();
                if (countDown == 0) {
                    countDown = 50 + (int) (Math.random() * 15);
                    while (iter.hasNext()) {
                        Coordinate temp = iter.next();
                        fireParticle(temp.getX(), temp.getY());
                    }
                }
                countDown--;
            }
        };
        timer.start();
    }


    /**
     * This method look through whether the marks should be added,<br>
     * whether the marks should be removed,<br>
     * and wait to draw or remove the marks on the GraphicsContext.
     *
     * @param gc where the marks are draw
     * @see Particle
     */
    private void drawFireworks(GraphicsContext gc) {
        Iterator<Particle> iter = particles.iterator();
        List<Particle> newParticles = new ArrayList<Particle>();
        while (iter.hasNext()) {
            Particle mark = iter.next();
            if (mark.update()) {
                iter.remove();

                if (mark.size == 8) {
                    explodeCircle(mark, newParticles);
                }
            }

            mark.draw(gc);
        }
        particles.addAll(newParticles);
    }

    /**
     * This method add every particle to a List,<br>
     * according to every earthquake location.
     *
     * @param x the x-coordinate of the particle, measured in pixels
     * @param y the y-coordinate of the particle, measured in pixels
     * @see Particle
     */
    private void fireParticle(double x, double y) {
        particles.add(new Particle(x, y, 0, 0, 0, y, colors[0], 8, false, false));
    }

    /**
     * This method depict the marks' movements, and how they look.
     *
     * @param mark         A particle instance on the map
     * @param newParticles The array type list of the new particles
     * @see Particle
     */
    private void explodeCircle(Particle mark, List<Particle> newParticles) {
        final int count = 5 + (int) (10 * Math.random());
        final double angle = (Math.PI * 2) / count;
        final int color = (int) (Math.random() * colors.length);
        for (int i = count; i > 0; i--) {
            double randomVelocity = 2 + Math.random() * 2;
            double particleAngle = i * angle;
            newParticles.add(new Particle(mark.posX, mark.posY, Math.cos(particleAngle) * randomVelocity,
                    Math.sin(particleAngle) * randomVelocity, 0, 0, colors[color], 5, true, true));
        }
    }
}
