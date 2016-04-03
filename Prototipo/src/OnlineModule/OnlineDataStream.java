/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OnlineModule;

import Network.DataPacket;
import Network.Topology;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Igor Siqueira
 */
public class OnlineDataStream {

    public static int[] convertIP(long ip) {
        int[] ipAddr = new int[4];
        for (int byteIndex = 0; byteIndex < 4; byteIndex++) {
            ipAddr[byteIndex] = (int) (ip % 256);
            ip = ip >> 8;
        }
        return ipAddr;
    }

    public void simulate(ArrayList<Topology> lstTopology, String traces) throws Exception {
        String delim = "[,]";
        DecimalFormat df = new DecimalFormat("00");

        FileInputStream tracesFIS;
        DataInputStream tracesDIS;
        BufferedReader tracesBR;
        String tracesStr;

        long lastEpoch = 0;

        //hash movido
        //doCreateSwtiches(instance,h);
        File diretorio = new File(traces);
        File files[] = diretorio.listFiles();
        if (files == null) {
            files = new File[1];
            files[0] = diretorio;
        } else {
            Arrays.sort(files);
        }

        for (File file : files) {

            if (file.isFile()) {

                tracesFIS = new FileInputStream(file);
                tracesDIS = new DataInputStream(tracesFIS);
                tracesBR = new BufferedReader(new InputStreamReader(tracesDIS));

                tracesStr = tracesBR.readLine();

                int cnt = 0;
                while (tracesStr != null) {
                    if (cnt % 40000 == 0) {
                        System.out.println(cnt);
                    }

                    String[] packetTokens = tracesStr.split(delim);

                    long time = parseLong(packetTokens[0]);
                    long srcIP = parseLong(packetTokens[1]);
                    long dstIP = parseLong(packetTokens[2]);
                    int srcPort = Integer.parseInt(packetTokens[3]);
                    int dstPort = Integer.parseInt(packetTokens[4]);
                    int payload = Integer.parseInt(packetTokens[5]);

                    //DataPacket pkt = new DataPacket(time, srcIP, dstIP, srcPort, dstPort, protocol, size);
                    DataPacket pkt = new DataPacket(time, srcIP, dstIP, srcPort, dstPort, payload);

                    for (Topology topology : lstTopology) {
                        topology.receivePacket(pkt);
                    }

                    lastEpoch = pkt.getTime();

                    tracesStr = tracesBR.readLine();
                    cnt++;
                }
                tracesBR.close();
                tracesDIS.close();
                tracesFIS.close();
            }
        }

        for (Topology topology : lstTopology) {
            topology.saveLastBitmap(lastEpoch);
        }

    }

    protected Long parseLong(String texto) {
        if (texto.startsWith("0x")) {
            return Long.parseLong(texto.substring(2), 16);
        } else {
            return Long.parseLong(texto);
        }
    }

//    protected abstract void doCreateSwtiches(Instance instance, Hash h) throws Exception;
//
//    protected abstract void doReceivePacket(Instance instance, int ingress, int egress, DataPacket pkt) throws Exception;
//
//    protected abstract void doSaveLastBitmap(long lastEpoch) throws Exception;
}
