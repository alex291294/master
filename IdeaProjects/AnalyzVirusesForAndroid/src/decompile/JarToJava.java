package decompile;

import com.android.utils.FileUtils;
import jd.core.Decompiler;
import jd.core.DecompilerException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by alex on 22.05.16.
 */
public class JarToJava extends Decompile {

    @Override
    public void executeDecompile(String pathVirus) {
        File files[] = new File(".").listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        try {
            String folder = files[0].getName().split(".jar")[0];
            File file = new File(folder);
            if (file.exists()) {
                FileUtils.deleteFolder(file);
            }
            new Decompiler().decompile(files[0].getAbsolutePath(),
                    folder);
            files[0].delete();
        } catch (DecompilerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
