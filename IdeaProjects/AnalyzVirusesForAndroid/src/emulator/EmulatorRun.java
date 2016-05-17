package emulator;

import java.io.IOException;

/**
 * Created by alex on 20.01.16.
 */
public class EmulatorRun implements IEmulator {
    private Emulator emulator;

    public EmulatorRun(Emulator emulator) {
        this.emulator = emulator;
    }

    @Override
    public void execute() throws IOException {
        emulator.runEmulator();
    }
}
