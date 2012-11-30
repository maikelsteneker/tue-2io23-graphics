package robotrace;

/**
 * The state variables that describe how the
 * scene should be rendered.
 */
public class GlobalState {
    
    // State variables.
    public boolean showAxes;    // Show an axis frame if true.
    public boolean showStick;   // Show robot(s) as stick figures.
    public int trackNr;         // Track to use: 0 -> O, 1 -> D, 2 -> L, 3 -> custom.
    
    public float tAnim;         // Time since start of animation in seconds.
    
    public int w;               // Width of window in pixels.
    public int h;               // Height of window in pixels.
    
    public Vector cnt;          // Center point.
    public float vDist;         // Distance eye point to center point.
    public float vWidth;        // Width of scene to be shown.
    public float phi;           // Azimuth angle in radians.
    public float theta;         // Elevation angle in radians.
    public boolean persp;       // Perspective (true) or isometric (false) projection.
    
    public int camMode;         // In race mode: 0 -> overview,
                                //               1 -> tracking helicopter,
                                //               2 -> view from the side on the leader,
                                //               3 -> view from camera on top of
                                //                    last robot,
                                //               4 -> autoswitch.
    
    public boolean lightCamera; // Light source is attached to camera (true)
                                // or world (false).

    /**
     * Default settings.
     */
    public final void reset() {
        showAxes = true;
        showStick = false;
        trackNr = 0;
        tAnim = -1;
        cnt = Vector.O;
        vDist = 10f;
        vWidth = 10f;
        phi = 0f;
        theta = 0f;
        persp = false;
        camMode = 0;
        lightCamera = false;
    }
    
    public GlobalState() {
        reset();
    }
    
    /**
     * Textual format.
     */
    @Override
    public String toString() {
        return "GlobalState{" +
                "showAxes=" + showAxes +
                ", showStick=" + showStick +
                ", trackNr=" + trackNr +
                ", tAnim=" + tAnim +
                ", w=" + w +
                ", h=" + h +
                ", cnt=" + cnt +
                ", vDist=" + vDist +
                ", vWidth=" + vWidth +
                ", phi=" + phi +
                ", theta=" + theta +
                ", persp=" + persp +
                ", camMode=" + camMode +
                ", lightCamera=" + lightCamera +
                '}';
    }
    
}
