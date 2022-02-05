import java.io.IOException;

public class Main {

    static public void main(String[] args) throws IOException, InterruptedException {
        ADuCBurner burner = new ADuCBurner("COM4", 9600);
        burner.sendWakeUp();
        burner.setFastBaudRate();
        burner.clearProgramMemory();
        burner.sendProgram("C:\\Users\\henri\\OneDrive\\Documentos\\MPA\\Ultra\\Software_Atualizado\\ADuC847\\Dinamica\\Dinamica_Low_Empac_CP\\build\\Debug\\Dinamica_Low_Empac_CP.hex");
        burner.setBootMode();
        burner.setSecureMode();
        burner.runProgram();
    }
}
