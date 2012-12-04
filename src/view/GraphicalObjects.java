package view;

import javax.media.opengl.GL2;
import static javax.media.opengl.GL2.*;

/**
 * Class containing some methods to draw glut-like objects.
 *
 * @author maikel
 */
public class GraphicalObjects {
    GL2 gl;
    
    public GraphicalObjects(GL2 gl) {
        this.gl = gl;
    }
    
    public void drawSolidCube() {
        gl.glBegin(GL_QUADS);
        cubeVertices();
        gl.glEnd();
    }
    
    public void drawWireCube() {
        gl.glBegin(GL_LINE_STRIP);
        gl.glEnd();
    }

    private void cubeVertices() {
        // Bottom plane.
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 1, 0);
        gl.glVertex3f(1, 1, 0);
        gl.glVertex3f(1, 0, 0);
        
        // Back plane.
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 0, 1);
        gl.glVertex3f(1, 0, 1);
        gl.glVertex3f(1, 0, 0);
        
        // Upper plane.
        gl.glVertex3f(0, 0, 1);
        // TODO: maybe finish this some day.
        
    }
}
