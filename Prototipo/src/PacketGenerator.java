
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Igor Siqueira
 */
public class PacketGenerator {

    public static void main(String argv[]) {
        try {
            File file = new File("D:\\Mestrado\\Prototipo\\Trace\\Los Angeles\\equinix-sanjose.dirA.20120920-130000.UTC.anon.pcap.csv");
            File newFile = new File("D:\\Mestrado\\Prototipo\\Trace\\Manual\\entrada03");

            FileInputStream tracesFIS = new FileInputStream(file);
            DataInputStream tracesDIS = new DataInputStream(tracesFIS);
            BufferedReader tracesBR = new BufferedReader(new InputStreamReader(tracesDIS));
            
            FileWriter tracesFW = new FileWriter(newFile);

            String tracesStr = tracesBR.readLine();

            Random random = new Random();

            while(tracesStr != null) {

                String[] packetTokens = tracesStr.split(",");

                long time = Long.parseLong(packetTokens[0]);
                long srcIP = Long.parseLong(packetTokens[1]);
                long dstIP = Long.parseLong(packetTokens[2]);
                int srcPort = Integer.parseInt(packetTokens[3]);
                int dstPort = Integer.parseInt(packetTokens[4]);
                int payload = random.nextInt(256);
                
                tracesFW.write(time + ","+ srcIP + ","+ dstIP + ","+ srcPort + ","+ dstPort + ","+ payload + "\n");

                tracesStr = tracesBR.readLine();

            }
            tracesBR.close();
            tracesDIS.close();
            tracesFIS.close();
            tracesFW.close();
        } catch (Exception e) {
        }
    }
}
