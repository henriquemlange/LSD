import java.io.IOException;

public class Main {
    static public void main(String[] args) throws IOException, InterruptedException {
        if(args.length < 2){
            System.out.println("Uso: LSD.jar <caminho\\do\\programa.hex> <portaSerial>");
        }else{
            String path = args[0];
            String serialPort = args[1];

            ADuCBurner burner = new ADuCBurner(serialPort, 9600);

            System.out.print("Procurando dispositivo: ");
            if(burner.sendWakeUp()){
                System.out.println("Sucesso");
            }else{
                System.out.println("Falhou");
            }

            System.out.print("Aumentando o baudrate: ");
            if(burner.setFastBaudRate()){
                System.out.println("Sucesso");
            }else{
                System.out.println("Falhou");
            }

            System.out.print("Limpando memoria de programa: ");
            if(burner.clearProgramMemory()){
                System.out.println("Sucesso");
            }else{
                System.out.println("Falhou");
            }

            System.out.print("Atualizando programa: ");
            burner.setFilepath(path);
            if(!burner.sendProgram()){
                System.out.println("Falhou");
            }

            System.out.print("Configurando modo de boot: ");
            if(burner.setBootMode()){
                System.out.println("Sucesso");
            }else{
                System.out.println("Falhou");
            }

            System.out.print("Setando modo de seguranca: ");
            if(burner.setSecureMode()){
                System.out.println("Sucesso");
            }else{
                System.out.println("Falhou");
            }
            System.out.print("Iniciando programa: ");
            if(burner.runProgram()){
                System.out.println("Sucesso");
            }else{
                System.out.println("Falhou");
            }

            burner.stopBurner();
            System.exit(1);
        }
    }
}