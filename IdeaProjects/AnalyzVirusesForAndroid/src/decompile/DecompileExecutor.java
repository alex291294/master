package decompile;

/**
 * Created by alex on 02.06.16.
 */
public class DecompileExecutor implements IDecompile {

    private Decompile decompile;

    public DecompileExecutor(String pathToVirus) {
        this.decompile = new Decompile(pathToVirus);
    }

    @Override
    public void apkToJar() {
        decompile.apkToJar();
    }

    @Override
    public void jarToJava() {
        decompile.jarToJava();
    }
}
