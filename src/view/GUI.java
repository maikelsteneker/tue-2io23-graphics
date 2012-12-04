package view;

import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import javax.media.opengl.GL;
import static javax.media.opengl.GL2.*;
import static java.lang.Math.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLException;
import javax.media.opengl.awt.GLJPanel;
import model.*;

public class GUI extends Base {

    Game game;
    ClickListener clickListener;
    int clicki = -1, clickj = -1;
    Player player;
    Vector test = null;

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
        TileType[][] types = new TileType[50][50];
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
        LandCreature l = new LandCreature();
        SeaCreature s = new SeaCreature();
        GameMap map = new GameMap(types);
        map.getTile(0, 0).addInhabitant(l);
        map.getTile(1, 1).addInhabitant(new Food());
        map.getTile(2, 2).addInhabitant(s);

        Player p1 = new Player("1");
        Set p1c = new HashSet<Creature>();
        p1c.add(l);
        p1.setCreatures(p1c);

        Player p2 = new Player("2");
        Set p2c = new HashSet<Creature>();
        p2c.add(s);
        p2.setCreatures(p2c);
        player = p1;
        Set players = new HashSet<Player>();
        players.add(p1);
        players.add(p2);
        try {
            game = new Game(players, map);
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
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
        float height = gs.vWidth / (gs.w / gs.h);
        gl.glOrtho(-0.5 * gs.vWidth, 0.5 * gs.vWidth, -0.5 * height, 0.5 * height, 0.1, 1000);
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();


        Vector dir = new Vector(cos(gs.phi) * cos(gs.theta),
                sin(gs.phi) * cos(gs.theta),
                sin(gs.theta));

        Vector eye = gs.cnt.add(dir.scale(gs.vDist));

        glu.gluLookAt(-40f, -40f, 30f, // eye point
                gs.cnt.x(), gs.cnt.y(), gs.cnt.z(), // center point
                0.0, 0.0, 1.0);   // up axis
    }

    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        if (clickListener.x != -1) {
            int x = clickListener.x;
            int y = clickListener.y;

            clickListener.x = -1;
            clickListener.y = -1;
            handleMouseClick(x, y);
            player.getCurrentCreature().select(game.getMap().getTile(clicki, clickj));
            gs.cnt = test.add(new Vector(clickj, clicki, 0));
            
        }
        gl.glMatrixMode(GL_MODELVIEW);

        // Enable lighting
        gl.glPushMatrix();
        gl.glEnable(GL_LIGHTING); //enable lighting (lighting influences color)
        gl.glEnable(GL_LIGHT0); //enable light source 0
        gl.glLoadIdentity();
        float[] location = {game.getMap().getWidth() / 2, game.getMap().getHeight() / 2, 10, 1};
        gl.glLightfv(GL_LIGHT0, GL_POSITION, location, 0); //set location of ls0
        gl.glEnable(GL_COLOR_MATERIAL); //enable materials (material influences color)
        gl.glTranslatef(location[0], location[1], location[2]);
        gl.glPopMatrix();

        // Draw stuff.
        draw();
    }

    private void draw() {
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);

        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);

        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);

        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);

        // Draw layer under map.
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        gl.glColor3f(1, 0, 1);
        gl.glBegin(GL_QUADS);
        float v = 1000;
        gl.glVertex3f(-v, -v, -1);
        gl.glVertex3f(-v, v, -1);
        gl.glVertex3f(v, v, -1);
        gl.glVertex3f(v, -v, -1);
        gl.glEnd();

        // Draw map.
        drawMap(game.getMap());
    }

    private void drawMap(GameMap map) throws GLException {
        gl.glPushMatrix();
        gl.glTranslatef(-map.getHeight() / 2, -map.getWidth() / 2, 0.0f);
        if(test == null){
            test = new Vector(-map.getHeight()/2,-map.getWidth()/2, 0);
            gs.cnt = test;
        }
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                // Load unique name for this tile.
                gl.glLoadName(i * map.getHeight() + j + 1);

                // Load correct tile texture.
                /*if (clicki == i && clickj == j) {
                 gl.glPushMatrix();
                 //gl.glTranslatef(0, 0, 0.5f);
                 gl.glColor3f(0.75f, 0.75f, 0.75f);
                 //gl.glTranslatef(0.5f, 0.5f, 0);
                 empty.bind(gl);
                 gl.glScalef(1, 1, 0.1f);
                 //glut.glutSolidCube(1);
                 gl.glBegin(GL_QUADS);
                 gl.glVertex3f(0, 0, 0);
                 gl.glVertex3f(0, 1, 0);
                 gl.glVertex3f(1, 1, 0);
                 gl.glVertex3f(1, 0, 0);
                 gl.glEnd();
                 gl.glPopMatrix();
                 } else {*/
                TileType type = map.getTile(i, j).getType();
                gl.glColor3f(1, 1, 1);
                switch (type) {
                    case DeepWater:
                        //gl.glColor3f(0, 0, 1);
                        deepWater.bind(gl);
                        break;
                    case ShallowWater:
                        //gl.glColor3f(0, 1, 0);
                        shallowWater.bind(gl);
                        break;
                    case Land:
                        //gl.glColor3f(1, 0, 0);
                        land.bind(gl);
                        break;
                }

                // Draw tile.
                gl.glBegin(GL_QUADS);
                gl.glTexCoord2d(0, 0);
                gl.glVertex3d(0, 0, 0);
                gl.glTexCoord2d(1, 0);
                gl.glVertex3d(1, 0, 0);
                gl.glTexCoord2d(1, 1);
                gl.glVertex3d(1, 1, 0);
                gl.glTexCoord2d(0, 1);
                gl.glVertex3d(0, 1, 0);
                gl.glEnd();
                //}

                // Draw inhabitants.
                gl.glPushMatrix();
                gl.glPushAttrib(GL_CURRENT_BIT);
                Set<Inhabitant> inhabitants = map.getTile(i, j).getInhabitants();
                empty.bind(gl);
                for (Inhabitant inhabitant : inhabitants) {
                    gl.glPushMatrix();
                    // TODO: replace by more meaningful, non-glut objects.
                    gl.glTranslatef(0.5f, 0.5f, 0.5f);

                    if (inhabitant instanceof LandCreature) {
                        gl.glColor3f(0, 1, 0);
                        gl.glRotatef(90, 1, 0, 0);
                        glut.glutSolidTeapot(0.5);
                    } else if (inhabitant instanceof Food) {
                        gl.glColor3f(1, 1, 1);
                        gl.glRotatef(90, 1, 0, 0);
                        glut.glutSolidTeapot(0.5);
                    }
                    gl.glPopMatrix();
                }
                gl.glPopAttrib();
                gl.glPopMatrix();

                // Move to the next column.
                gl.glTranslatef(1, 0, 0);
            }
            //Move to the next row and back to the first column.
            gl.glTranslatef(-map.getHeight(), 1, 0);
        }
        gl.glPopMatrix();
    }

    private void handleMouseClick(int x, int y) {
        y = gs.h - y;
        int buffsize = 64;
        IntBuffer buff = IntBuffer.allocate(buffsize);
        gl.glSelectBuffer(buffsize, buff);
        IntBuffer view = IntBuffer.allocate(4);
        gl.glGetIntegerv(GL_VIEWPORT, view);
        gl.glRenderMode(GL_SELECT);
        gl.glInitNames();
        gl.glPushName(0);
        gl.glMatrixMode(GL_PROJECTION);

        gl.glPushMatrix();
        gl.glLoadIdentity();
        glu.gluPickMatrix(x, y, 1.0, 1.0, view);
        float height = gs.vWidth / (gs.w / gs.h);
        gl.glOrtho(-0.5 * gs.vWidth, 0.5 * gs.vWidth, -0.5 * height, 0.5 * height, 0.1, 1000);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glPushMatrix();
        draw();
        gl.glPopMatrix();
        gl.glMatrixMode(GL_PROJECTION);
        gl.glPopMatrix();

        int hits = gl.glRenderMode(GL_RENDER);
        int clickcode = 0;

        //get last element (N.B. usually i=3 is the actual correct value)
        for (int i = buffsize - 1; i >= 0; i--) {
            if (buff.get(i) != 0) {
                clickcode = buff.get(i);
                break;
            }
        }
        int h = game.getMap().getHeight();
        clicki = hits > 0 ? (clickcode - 1) / h : -1;
        clickj = hits > 0 ? (clickcode - 1) % h : -1;

        gl.glMatrixMode(GL_MODELVIEW);
    }

    private final class ClickListener implements MouseListener {

        int x = -1, y = -1;

        @Override
        public void mouseClicked(MouseEvent e) {
            x = e.getX();
            y = e.getY();
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

    public static void main(String args[]) {
        new GUI();
    }
}
