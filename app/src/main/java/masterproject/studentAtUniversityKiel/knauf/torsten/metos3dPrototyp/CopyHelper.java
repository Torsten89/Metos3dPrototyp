package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyHelper {

    public static void copyFile(File src, File target) throws IOException {
        if(!src.isFile())
            throw new IOException("Src doesn't exists: "+ target.getAbsolutePath());

        InputStream is = new FileInputStream(src);
        try {
            copyFile(is, target);
        } finally {
            is.close();
        }
    }

    public static void copyDir(File src, File target) throws IOException{
        if (src.isDirectory()) {
            if (!target.exists() && !target.mkdirs())
                throw new IOException("Cannot create dir " + target.getAbsolutePath());

            String[] children = src.list();
            for (int i=0; i<children.length; i++) {
                copyDir(new File(src, children[i]), new File(target, children[i]));
            }
        } else {
            File directory = target.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs())
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());

            copyFile(src, target);
        }
    }

    public static void copyFileFromAsset(AssetManager assetManager, String assetName, String target) throws IOException{
        InputStream is= null;
        try {
            is = assetManager.open(assetName);
            File newFile = new File(target);
            copyFile(is, newFile);
        } finally {
            is.close();
        }
    }

    private static void copyFile(InputStream in, File targetFile) throws IOException {
        OutputStream out = null;
        try {
            new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } finally {
            out.close();
        }
    }
}
