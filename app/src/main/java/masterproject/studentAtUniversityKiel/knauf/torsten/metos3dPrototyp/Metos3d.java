package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;

/**
 * Created by expert on 27.05.16.
 */
public class Metos3d {
    public native static String runNDOP();
    int i2;

    static {
        System.loadLibrary("metos3d");
    }
}
