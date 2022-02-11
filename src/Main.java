import java.io.IOException;
import java.util.Scanner;

public class Main {

    static public void main(String[] args) throws IOException, InterruptedException {
        ADuCBurner burner = new ADuCBurner("COM4", 9600);
        Scanner sc = new Scanner(System.in);
        boolean isRunning = true;

        if(args.length == 1){
            System.out.println("Iniciando gravacao automatica...");
            burner.setPath(args[0]);
            System.out.print("Conectando com o dispositivo: ");
            if(burner.sendWakeUp()){
                System.out.print("Sucesso\n");
            }else{
                System.out.print("Falha\n");
            }

            System.out.print("Alterando velocidade da serial: ");
            if(burner.setFastBaudRate()){
                System.out.print("Sucesso\n");
            }else{
                System.out.print("Falha\n");
            }

            System.out.print("Limpando memoria de programas: ");
            if(burner.clearProgramMemory()){
                System.out.print("Sucesso\n");
            }else{
                System.out.print("Falha\n");
            }

            System.out.print("Preparando para enviar dados... ");
            Thread.sleep(2000);
            if(burner.sendProgram()){
                System.out.print("Configurando modo de Boot: ");
                if(burner.setBootMode()){
                    System.out.print("Sucesso\n");
                }else{
                    System.out.print("Falha\n");
                }
            }

            System.out.print("Configurando modo de segurança do codigo: ");
            if(burner.setSecureMode()){
                System.out.print("Sucesso\n");
            }else{
                System.out.print("Falha\n");
            }

            System.out.println("Iniciando programa... ");
            if(burner.runProgram()){
                System.out.print("Sucesso\n");
            }else{
                System.out.print("Falha\n");
            }
        }

        System.out.print("Deseja ver o menu [S/N]?");
        String ans = sc.next();

        if(ans.equals("n") || ans.equals("N")){
            isRunning = false;
            burner.stopBurner();
        }else if(ans.equals("s") || ans.equals("S")){
            System.out.println("--------------------------------------------LSD--------------------------------------------");
        }else{
            System.out.print("\nComando nao reconhecido, redirecionando para o menu...\n\n");
            System.out.println("--------------------------------------------LSD--------------------------------------------");
        }

        while(isRunning){
            System.out.println("Menu:");
            System.out.print("|\n");
            System.out.println("|---> 1 Definir porta.");
            System.out.println("|---> 2 Enviar Wake Up para o processador.");
            System.out.println("|---> 3 Escolher diretorio do arquivo.");
            System.out.println("|---> 4 Ativar modo de seguranca.");
            System.out.println("|---> 5 Rodar programa ao finalizar gravacao.");
            System.out.println("|---> 6 Gravar programa.");
            System.out.println("|---> 7 Fechar LSD.");
            System.out.print("\nInforme o que você deseja fazer (Ex.: 1): ");

            String Scmd = sc.next();
            int cmd;
            try{
                cmd = Integer.parseInt(Scmd);
            }catch(NumberFormatException ex){
                cmd = -1;
            }
            String port;
            boolean onSecure = false;
            boolean runProgram = false;

            switch(cmd){
                case 1:
                    System.out.print("Informe a porta: ");
                    port = sc.next();
                    burner = new ADuCBurner(port, 9600);
                    break;

                case 2:
                    System.out.print("Conectando com o dispositivo: ");
                    if(burner.sendWakeUp()){
                        System.out.print("Sucesso\n");
                    }else{
                        System.out.print("Falha\n");
                    }
                    break;

                case 3:
                    System.out.print("Informe o diretorio: ");
                    String path = sc.next();
                    burner.setPath(path);
                    break;

                case 4:
                    onSecure = true;
                    break;

                case 5:
                    runProgram = true;
                    break;

                case 6:
                    System.out.print("Conectando com o dispositivo: ");
                    if(burner.sendWakeUp()){
                        System.out.print("Sucesso\n");
                    }else{
                        System.out.print("Falha\n");
                    }

                    System.out.print("Alterando velocidade da serial: ");
                    if(burner.setFastBaudRate()){
                        System.out.print("Sucesso\n");
                    }else{
                        System.out.print("Falha\n");
                    }

                    System.out.print("Limpando memoria de programas: ");
                    if(burner.clearProgramMemory()){
                        System.out.print("Sucesso\n");
                    }else{
                        System.out.print("Falha\n");
                    }

                    System.out.print("Preparando para enviar dados... ");
                    Thread.sleep(2000);
                    if(burner.sendProgram()){
                        System.out.print("Configurando modo de Boot: ");
                        if(burner.setBootMode()){
                            System.out.print("Sucesso\n");
                        }else{
                            System.out.print("Falha\n");
                        }
                    }

                    if(onSecure){
                        System.out.print("Configurando modo de segurança do codigo: ");
                        if(burner.setSecureMode()){
                            System.out.print("Sucesso\n");
                        }else{
                            System.out.print("Falha\n");
                        }
                    }

                    if(runProgram){
                        System.out.println("Iniciando programa... ");
                        if(burner.runProgram()){
                            System.out.println("Sucesso! Firmware instalado!");
                        }else{
                            System.out.println("Falha na inicializacao!");
                        }
                    }

                    break;

                case 7:
                    burner.stopBurner();
                    isRunning = false;
                    break;

                default:
                    System.out.println("\nComando nao reconhecido, tente novamente!");
                    break;
            }
            System.out.println("");
        }
    }
}
