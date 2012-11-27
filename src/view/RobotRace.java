package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import javax.media.opengl.GL;
import static javax.media.opengl.GL2.*;
import static java.lang.Math.*;
import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.awt.GLJPanel;
import model.*;

/**
 * Handles all of the RobotRace graphics functionality, which should be extended
 * per the assignment.
 *
 * OpenGL functionality: - Basic commands are called via the gl object; -
 * Utility commands are called via the glu and glut objects;
 *
 * GlobalState: The gs object contains the GlobalState as described in the
 * assignment: - The camera viewpoint angles, phi and theta, are changed
 * interactively by holding the left mouse button and dragging; - The camera
 * view width, vWidth, is changed interactively by holding the right mouse
 * button and dragging upwards or downwards; - The center point can be moved up
 * and down by pressing the 'q' and 'z' keys, forwards and backwards with the
 * 'w' and 's' keys, and left and right with the 'a' and 'd' keys; - Other
 * settings are changed via the menus at the top of the screen.
 *
 * Textures: Place your "track.jpg", "brick.jpg", "head.jpg", and "torso.jpg"
 * files in the same folder as this file. These will then be loaded as the
 * texture objects track, bricks, head, and torso respectively. Be aware, these
 * objects are already defined and cannot be used for other purposes. The
 * texture objects can be used as follows:
 *
 * gl.glColor3f(1f, 1f, 1f); track.bind(gl); gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0); gl.glVertex3d(0, 0, 0); gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0); gl.glTexCoord2d(1, 1); gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1); gl.glVertex3d(0, 1, 0); gl.glEnd();
 *
 * Note that it is hard or impossible to texture objects drawn with GLUT. Either
 * define the primitives of the object yourself (as seen above) or add
 * additional textured primitives to the GLUT object.
 */
public class RobotRace extends Base {

    double fovy = -1;
    Game game;
    ClickListener clickListener;
    Vector clickPoint;

    /**
     * Called upon the start of the application. Primarily used to configure
     * OpenGL.
     */
    @Override
    public void initialize() {
        GLJPanel glPanel = (GLJPanel) frame.glPanel;
        clickListener = new ClickListener();
        glPanel.addMouseListener(clickListener);

        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Enable anti-aliasing.
        gl.glEnable(GL_LINE_SMOOTH);
        //gl.glEnable(GL_POLYGON_SMOOTH);
        gl.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        //gl.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);

        // Enable textures. 
        gl.glEnable(GL_TEXTURE_2D);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        // Create game object
        Random generator = new Random();
        TileType[][] types = new TileType[5][5];
        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < types[0].length; j++) {
                int type = generator.nextInt(3);
                switch (type) {
                    case 0:
                        types[j][i] = TileType.DeepWater;
                        break;
                    case 1:
                        types[j][i] = TileType.Land;
                        break;
                    case 2:
                        types[j][i] = TileType.ShallowWater;
                        break;
                }
            }
        }
        GameMap map = new GameMap(types);
        Player p1 = new Player("1");
        Player p2 = new Player("2");
        Set players = new HashSet<Player>();
        players.add(p1);
        players.add(p2);
        try {
            game = new Game(players, map);
        } catch (Exception ex) {
            Logger.getLogger(RobotRace.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);

        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();
        if (gs.persp) {
            if (fovy == -1) {
                fovy = atan2(gs.w, 0.1) / (gs.w / gs.h);
            }
            glu.gluPerspective(toDegrees(fovy), gs.w / gs.h, 0.1, 1000);
        } else {
            float height = gs.vWidth / (gs.w / gs.h);
            gl.glOrtho(-0.5 * gs.vWidth, 0.5 * gs.vWidth, -0.5 * height, 0.5 * height, 0.1, 1000);
        }


        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();


        Vector dir = new Vector(cos(gs.phi) * cos(gs.theta),
                sin(gs.phi) * cos(gs.theta),
                sin(gs.theta));

        Vector eye = gs.cnt.add(dir.scale(gs.vDist));

        glu.gluLookAt(eye.x(), eye.y(), eye.z(), // eye point
                gs.cnt.x(), gs.cnt.y(), gs.cnt.z(), // center point
                0.0, 0.0, 1.0);   // up axis

        // Enable lighting (2.1)
        gl.glEnable(GL_LIGHTING); //enable lighting (lighting influences color)
        gl.glEnable(GL_LIGHT0); //enable light source 0
        //gl.glLoadIdentity();
        float[] location = {(float) eye.x(), (float) eye.y(), (float) eye.z()};
        gl.glLightfv(GL_LIGHT0, GL_POSITION, location, 0); //set location of ls0
        gl.glEnable(GL_COLOR_MATERIAL); //enable materials (material influences color)
    }

    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        //save current position
        //gl.glPushMatrix();

        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);

        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);

        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);

        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        /*
         * // Unit box around origin. glut.glutWireCube(1f);
         *
         * // Move in x-direction. gl.glTranslatef(2f, 0f, 0f);
         *
         * // Rotate 30 degrees, around z-axis. gl.glRotatef(30f, 0f, 0f, 1f);
         *
         * // Scale in z-direction. gl.glScalef(1f, 1f, 2f);
         *
         * // Translated, rotated, scaled box. glut.glutWireCube(1f);
         *
         * //revert back to original position gl.glPopMatrix();
         *
         * //draw grid //drawGrid();
         *
         *
         */
        // Axis Frame
        //drawAxisFrame();

        //draw robots
        /*
         * gl.glPushMatrix(); gl.glTranslatef(-NUMROBOTS / 2, 0, 0); for (Robot
         * r : robots) { gl.glTranslatef(1.0f, 0, 0); r.draw(); }
         * gl.glPopMatrix();
         */
        gl.glColor3f(1, 1, 1);
        GameMap map = game.getMap();
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.5f, 0);
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                System.out.println((clickPoint == null) ? "" : "(" + clickPoint.x + "," + clickPoint.y + ")");
                
                gl.glLoadName(i*10+j);
                if (clickPoint != null && i <= clickPoint.y && clickPoint.y < i + 1 && j <= clickPoint.x && clickPoint.x < j + 1) {
                    gl.glColor3f(0.75f, 0.75f, 0.75f);
                    glut.glutSolidCube(1);
                } else {
                    Tile tile = map.getTile(i, j);
                    TileType type = tile.getType();

                    switch (type) {
                        case DeepWater:
                            gl.glColor3f(0, 0, 1);
                            break;
                        case ShallowWater:
                            gl.glColor3f(0, 1, 0);
                            break;
                        case Land:
                            gl.glColor3f(1, 0, 0);
                            break;
                    }
                    gl.glScalef(1, 1, 0.1f);
                    
                glut.glutSolidCube(1);
                gl.glScalef(1, 1, 10);
                }

                //glut.glutWireCube(1);
                gl.glTranslatef(1, 0, 0);
            }
            gl.glTranslatef(-map.getHeight(), 1, 0);
        }
        gl.glPopMatrix();

        if (clickListener.x != -1) {
            handleMouseClick(clickListener.x, clickListener.y);
            clickListener.x = -1;
            clickListener.y = -1;
        }
        /*
        if (clickPoint != null) {
            gl.glPushMatrix();
            gl.glTranslated(clickPoint.x(), clickPoint.y(), clickPoint.z());
            glut.glutSolidTeapot(0.5f);
            gl.glPopMatrix();
        }*/
    }

    public void drawArrow() {
        gl.glPushMatrix();

        gl.glTranslatef(0f, 0, 0.5f);
        gl.glScalef(0.01f, 0.01f, 1f);
        glut.glutSolidCube(0.9f);

        gl.glPopMatrix();
        gl.glPushMatrix();

        gl.glTranslatef(0f, 0f, 1f);
        glut.glutSolidCone(0.05, 0.1, 15, 2);

        gl.glPopMatrix();
    }

    public void drawAxisFrame() {
        if (gs.showAxes) {
            gl.glColor3f(1.0f, 1.0f, 0);
            glut.glutSolidSphere(0.10f, 20, 20);

            gl.glPushMatrix();
            gl.glRotatef(90, 0, 1, 0);
            gl.glColor3f(1.0f, 0, 0);
            drawArrow();
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glRotatef(-90, 1, 0, 0);
            gl.glColor3f(0, 1.0f, 0);
            drawArrow();
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glColor3f(0, 0, 1.0f);
            drawArrow();
            gl.glPopMatrix();
        }
    }

    private void handleMouseClick(int x, int y) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        glut.glutSolidTeapot(5);
        gl.glPopMatrix();

        double[] model = new double[16];
        gl.glGetDoublev(GL_MODELVIEW_MATRIX, model, 0);
        double[] proj = new double[16];
        gl.glGetDoublev(GL_PROJECTION_MATRIX, proj, 0);
        int[] view = new int[4];
        gl.glGetIntegerv(GL_VIEWPORT, view, 0);
        double[] objPos = new double[3];
        FloatBuffer z = FloatBuffer.allocate(1);
        gl.glReadPixels(x, y, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, z);
        glu.gluUnProject(x, view[3] - y - 1, z.get(), model, 0, proj, 0, view, 0, objPos, 0);
        /*
         * gl.glBegin(GL_LINES); gl.glVertex3d(objPos[0], objPos[1], objPos[2]);
         * gl.glVertex3d(objPos[0], objPos[1], objPos[2] + 100);
            gl.glEnd();
         */
        /*
        gl.glPushMatrix();
        gl.glTranslated(objPos[0], objPos[1], objPos[2]);
        glut.glutSolidTeapot(0.5f);
        gl.glPopMatrix();
        */
        Vector click = new Vector(objPos[0], objPos[1], objPos[2]);
        clickPoint = click;
        //System.out.println(objPos[0] + "," + objPos[1] + "," + objPos[2]);
    }

    private final class ClickListener implements MouseListener {

        int x = -1, y = -1;

        @Override
        public void mouseClicked(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            //System.out.println(x + "," + y);
            //handleMouseClick(e.getPoint());
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * Main program execution body, delegates to an instance of the RobotRace
     * implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
    }
}