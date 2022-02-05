import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ADuCBurner {
    private volatile byte accepted;
    private final SerialPort comPort;

    public ADuCBurner(String portName, int baudRate) {
        this.accepted = 0;
        this.comPort = SerialPort.getCommPort(portName);
        comPort.setComPortParameters(baudRate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        comPort.openPort();

        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[comPort.bytesAvailable()];
                comPort.readBytes(newData, newData.length);
                System.out.println("Loader:" + Arrays.toString(newData));
                accepted = newData[0];
            }
        });
    }

    public boolean sendWakeUp(){
        byte[] WakeUp = {0x21, 0x5A, 0x00, (byte) 0xA6};
        comPort.writeBytes(WakeUp, WakeUp.length);
        System.out.println(Arrays.toString(WakeUp));
        while (accepted == 0) Thread.onSpinWait();

        if(accepted == 7){
            accepted = 0;
            System.out.println("Device not found");
            return false;
        }else if(accepted == 6){
            accepted = 0;
        }
        return true;
    }

    public boolean setFastBaudRate(){
        byte[] changeBaudRate = {7, 14, 3, 66, -127, 45, 13};
        comPort.writeBytes(changeBaudRate, changeBaudRate.length);
        System.out.println(Arrays.toString(changeBaudRate));
        comPort.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        while (accepted == 0) Thread.onSpinWait();

        if(accepted == 7){
            accepted = 0;
            System.out.println("Failed while changing baudrate");
            return false;
        }else if(accepted == 6){
            accepted = 0;
        }
        return true;
    }

    public boolean clearProgramMemory(){
        byte[] Clearcmd = {0x07, 0x0E, 0x01, 0x43, (byte) 0xBC};
        comPort.writeBytes(Clearcmd, Clearcmd.length);
        System.out.println(Arrays.toString(Clearcmd));
        while (accepted == 0) Thread.onSpinWait();

        if(accepted == 7){
            accepted = 0;
            System.out.println("Failed while errasing program memory");
            return false;
        }else if(accepted == 6){
            accepted = 0;
        }
        return true;
    }

    public boolean setSecureMode(){
        byte[] SecureMode = {7, 14, 2, 83, 5, -90};
        comPort.writeBytes(SecureMode, SecureMode.length);
        System.out.println(Arrays.toString(SecureMode));
        while (accepted == 0) Thread.onSpinWait();

        if(accepted == 7){
            accepted = 0;
            System.out.println("Failed while setting secure mode");
            return false;
        }else if(accepted == 6){
            accepted = 0;
        }
        return true;
    }

    public boolean setBootMode(){
        byte[] BootMode = {7, 14, 2, 70, -1, -71};
        comPort.writeBytes(BootMode, BootMode.length);
        System.out.println(Arrays.toString(BootMode));
        while (accepted == 0) Thread.onSpinWait();

        if(accepted == 7){
            accepted = 0;
            System.out.println("Failed while changing boot mode");
            return false;
        }else if(accepted == 6){
            accepted = 0;
        }
        return true;
    }

    public boolean runProgram(){
        byte[] Run = {7, 14, 4, 85, 0, 0, 0, -89};
        comPort.writeBytes(Run, Run.length);
        System.out.println(Arrays.toString(Run));
        while (accepted == 0) Thread.onSpinWait();

        if(accepted == 7){
            accepted = 0;
            System.out.println("Failed while runing program");
            return false;
        }else if(accepted == 6){
            accepted = 0;
        }
        return true;
    }

    public boolean sendProgram(String filepath) throws IOException {
        FileReader SensorsFile = new FileReader(filepath);
        BufferedReader buff = new BufferedReader(SensorsFile);
        String line;
        int size;
        int addr;
        int type;

        while((line = buff.readLine()) != null){
            size = Integer.parseInt(line.substring(1, 3), 16);
            addr = Integer.parseInt(line.substring(3, 7), 16);
            type = Integer.parseInt(line.substring(7, 9), 16);
            if(type == 1) {
                break;
            }
            byte[] toSend = new byte[size + 8];
            toSend[0] = 0x07;
            toSend[1] = 0x0E;
            toSend[2] = (byte) (size + 4);
            toSend[3] = 0x57;
            toSend[4] = (byte) ((addr >> 16) & 0xFF);
            toSend[5] = (byte) ((addr >> 8) & 0xFF);
            toSend[6] = (byte) (addr  & 0xFF);
            for(int i = 0; i < size; i++){
                toSend[i + 7] = (byte) Integer.parseInt(line.substring(i*2 + 9, i*2 + 11), 16);
            }
            int checksum = 0;
            for(int i = 2; i < toSend.length - 1; i++){
                checksum += toSend[i];
            }
            checksum = (byte) (checksum & 0xFF);
            checksum = 0x100 - checksum;
            toSend[toSend.length - 1] = (byte) checksum;
            comPort.writeBytes(toSend, toSend.length);
            System.out.println(Arrays.toString(toSend));
            while (accepted == 0) Thread.onSpinWait();

            if(accepted == 7){
                accepted = 0;
                System.out.println("Failed while recording program bytes");
                return false;
            }else if(accepted == 6){
                accepted = 0;
            }
        }
        return true;
    }
}
