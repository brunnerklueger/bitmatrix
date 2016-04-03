/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalysisModule;

import Network.BitMap;
import Network.Scenario;
import Network.Topology;
import Simulador.Instance;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Igor Siqueira
 */
public abstract class DataAnalysis {

    public void realAnalyse(ArrayList<Topology> lstTopology) throws Exception {
//        List<Long[][]> matrix = realAnalysis(lstTopology);
        realAnalysis(lstTopology);
        for (Topology topology : lstTopology) {
            File bmpFile = new File(topology.getSaidasDir() + "Real.csv");
            if (!bmpFile.exists()) {
                bmpFile.createNewFile();
            }

            FileWriter fout = new FileWriter(bmpFile);
            PrintWriter oos = new PrintWriter(fout);
            for (int i = 0; i < topology.getNumberOfSwitches(); i++) {
                oos.print(";Router " + (i + 1));
            }
            for (int i = 0; i < topology.getNumberOfSwitches(); i++) {
                oos.println();
                oos.print("Router " + (i + 1));
                for (int j = 0; j < topology.getNumberOfSwitches(); j++) {
                    doPrintElement(oos, i, j, topology.getTrafficMatrix()[i][j]);
                }
            }
            oos.close();
            fout.close();

        }

//        File bmpFile = new File(Instance.getSaidasDir() + "Real.csv");
//        if (!bmpFile.exists()) {
//            bmpFile.createNewFile();
//        }
//        FileWriter fout = new FileWriter(bmpFile);
//        PrintWriter oos = new PrintWriter(fout);
//        Long[][] matrix = realAnalysis();
//        for (int i = 0; i < Instance.getNumberOfSwitches(); i++) {
//            oos.print(";Router " + (i + 1));
//        }
//        for (int i = 0; i < Instance.getNumberOfSwitches(); i++) {
//            oos.println();
//            oos.print("Router " + (i + 1));
//            for (int j = 0; j < Instance.getNumberOfSwitches(); j++) {
//                doPrintElement(oos, i, j, matrix[i][j]);
//            }
//        }
//        oos.close();
//        fout.close();
    }

    public void simulateBitmapAnalyse(List<Topology> lstTopology) throws Exception {
        Double valor;
        
        for (Topology topology : lstTopology) {
            for (Instance instance : topology.getLstInstance()) {
                HashMap<Double, List<HashMap<Integer, Integer>>> orderMap = new HashMap<>();
                File bmpFile = new File(topology.getSaidasDir() + "SimulacaoInstancia" + instance.getId() + ".csv");
                if (!bmpFile.exists()) {
                    bmpFile.createNewFile();
                }
                FileWriter fout = new FileWriter(bmpFile);
                PrintWriter oos = new PrintWriter(fout);
                for (int i = 0; i < topology.getNumberOfSwitches(); i++) {
                    oos.print(";Router " + (i + 1));
                }
                for (int i = 0; i < topology.getNumberOfSwitches(); i++) {
                    oos.println();
                    oos.print("Router " + (i + 1));
                    for (int j = 0; j < topology.getNumberOfSwitches(); j++) {
                        int sourceId = i;
                        int destinationId = j;
                        if (sourceId > topology.getNumberOfSwitches() || destinationId > topology.getNumberOfSwitches()) {
                            System.out.println("Erro no número de switches");
                            throw new Exception("Erro no número de switches");
                        } else {

                            valor = bitmapAnalyse(instance, i, j);

                            doPrintElement(oos, i, j, valor);

                            if (i < j) {
                                if (orderMap.containsKey(valor)) {

                                    HashMap<Integer, Integer> mapNodes = new HashMap<>();
                                    mapNodes.put(i + 1, j + 1);
                                    orderMap.get(valor).add(mapNodes);
                                } else {
                                    LinkedList listaHashMap = new LinkedList();
                                    HashMap<Integer, Integer> mapNodes = new HashMap<>();
                                    mapNodes.put(i + 1, j + 1);
                                    listaHashMap.add(mapNodes);
                                    orderMap.put(valor, listaHashMap);
                                }
                            }
                        }
                    }
                }
                oos.close();
                fout.close();

                Map<Double, List<HashMap<Integer, Integer>>> map = new TreeMap<>(orderMap);
                System.out.println("Instancia"+ instance.getId()+ " After Sorting:");
                Set set2 = map.entrySet();
                Iterator iterator2 = set2.iterator();
                while (iterator2.hasNext()) {
                    Map.Entry me2 = (Map.Entry) iterator2.next();
                    System.out.print(me2.getKey() + ": ");
                    System.out.println(me2.getValue());
                }

            }

        }
//        File bmpFile = new File(Instance.getSaidasDir() + "Simulacao.csv");
//        if (!bmpFile.exists()) {
//            bmpFile.createNewFile();
//        }
//        FileWriter fout = new FileWriter(bmpFile);
//        PrintWriter oos = new PrintWriter(fout);
//        for (int i = 0; i < Instance.getNumberOfSwitches(); i++) {
//            oos.print(";Router " + (i + 1));
//        }
//        for (int i = 0; i < Instance.getNumberOfSwitches(); i++) {
//            oos.println();
//            oos.print("Router " + (i + 1));
//            for (int j = 0; j < Instance.getNumberOfSwitches(); j++) {
//                doPrintElement(oos, i, j, bitmapAnalyse(i, j));
//            }
//        }
//        oos.close();
//        fout.close();
    }

    protected void realAnalysis(ArrayList<Topology> lstTopology) throws Exception {
        String delim = "[,]";
        DecimalFormat df = new DecimalFormat("00");

        FileInputStream tracesFIS;
        DataInputStream tracesDIS;
        BufferedReader tracesBR;
        String tracesStr;

        for (Topology topology : lstTopology) {
            Long[][] trafficMatrix = new Long[topology.getNumberOfSwitches()][topology.getNumberOfSwitches()];
            for (int i = 0; i < topology.getNumberOfSwitches(); i++) {
                for (int j = 0; j < topology.getNumberOfSwitches(); j++) {
                    trafficMatrix[i][j] = 0L;
                }
            }
            topology.setTrafficMatrix(trafficMatrix);
        }
        String tracerEntrada;
        tracerEntrada = "C:\\Users\\ADMIN\\Desktop\\entrada";
        File diretorio = new File(tracerEntrada);
        File files[] = diretorio.listFiles();
        if (files == null) {
            files = new File[1];
            files[0] = diretorio;
        } else {
            Arrays.sort(files);
        }

        for (File file : files) {

            if (file.isFile()) {

                //System.out.println("Trace #" + df.format(i));
                tracesFIS = new FileInputStream(file);
                tracesDIS = new DataInputStream(tracesFIS);
                tracesBR = new BufferedReader(new InputStreamReader(tracesDIS));

                tracesStr = tracesBR.readLine();

                while (tracesStr != null) {

                    String[] packetTokens = tracesStr.split(delim);

                    long time = parseLong(packetTokens[0]);
                    long srcIP = parseLong(packetTokens[1]);
                    long dstIP = parseLong(packetTokens[2]);

                    if (time - 21600000000L > Scenario.startTime && time - 21600000000L < Scenario.endTime) {

                        for (Topology topology : lstTopology) {

                            //Ingress Switch
                            int ingressSW = (int) ((topology.getBitmask() & srcIP) >> (32 - topology.getNumberOfBitsForSwId()));

                            //Egress Switch
                            int egressSW = (int) ((topology.getBitmask() & dstIP) >> (32 - topology.getNumberOfBitsForSwId()));

                            doCalculateMatrixElem(ingressSW, egressSW, topology);
                        }
                    }
                    tracesStr = tracesBR.readLine();
                }
                tracesBR.close();
                tracesDIS.close();
                tracesFIS.close();
            }
        }
//        return lstMatrizRealanalysis;

    }

    protected Long parseLong(String texto) {
        if (texto.startsWith("0x")) {
            return Long.parseLong(texto.substring(2), 16);
        } else {
            return Long.parseLong(texto);
        }
    }

    protected Double bitmapAnalyse(Instance instance, int sourceId, int destinationId) throws Exception {
        BitMap bitMap;
        String bmpDir;
        int numberOfFiles;
        Double trafficVolume = 0D;
        FileInputStream fin = null;
        ObjectInputStream ois = null;

        TreeMap<Long, BitMap> sourceBitMapTree = new TreeMap<>();
        TreeMap<Long, BitMap> destinationBitMapTree = new TreeMap<>();

        bmpDir = getSrcDir(instance, sourceId);
        numberOfFiles = new File(bmpDir).listFiles().length;
        int firstBitmap = -2;

        for (int numberOfBmp = 0; numberOfBmp < numberOfFiles; numberOfBmp++) {

            fin = new FileInputStream(bmpDir + "BitMap" + numberOfBmp + ".bmp");
            ois = new ObjectInputStream(fin);
            bitMap = (BitMap) ois.readObject();

            if ((bitMap.getStartEpoch() - 21600000000L > Scenario.startTime)
                    && (bitMap.getStartEpoch() - 21600000000L < Scenario.endTime)) {
                if (firstBitmap == -2) {
                    firstBitmap = numberOfBmp - 1;
                }
                sourceBitMapTree.put(bitMap.getStartEpoch() - 21600000000L, bitMap);

            }
            ois.close();
            fin.close();

        }

        //Add the first bitmap in the Measurement Interval
        if (firstBitmap >= 0) {
            fin = new FileInputStream(bmpDir + "BitMap" + firstBitmap + ".bmp");
            ois = new ObjectInputStream(fin);
            bitMap = (BitMap) ois.readObject();

            sourceBitMapTree.put(bitMap.getStartEpoch() - 21600000000L, bitMap);
            ois.close();
            fin.close();

        }

        bmpDir = getDestDir(instance, destinationId);
        numberOfFiles = new File(bmpDir).listFiles().length;
        firstBitmap = -2;

        for (int numberOfBmp = 0; numberOfBmp < numberOfFiles; numberOfBmp++) {

            fin = new FileInputStream(bmpDir + "BitMap" + numberOfBmp + ".bmp");
            ois = new ObjectInputStream(fin);
            bitMap = (BitMap) ois.readObject();
            if ((bitMap.getStartEpoch() - 21600000000L > Scenario.startTime)
                    && (bitMap.getStartEpoch() - 21600000000L < Scenario.endTime)) {
                if (firstBitmap == -2) {
                    firstBitmap = numberOfBmp - 1;
                }
                destinationBitMapTree.put(bitMap.getStartEpoch() - 21600000000L, bitMap);
            }
            ois.close();
            fin.close();
        }

        //Add the first bitmap in the Measurement Interval
        if (firstBitmap >= 0) {
            fin = new FileInputStream(bmpDir + "BitMap" + firstBitmap + ".bmp");
            ois = new ObjectInputStream(fin);
            bitMap = (BitMap) ois.readObject();

            destinationBitMapTree.put(bitMap.getStartEpoch() - 21600000000L, bitMap);

            ois.close();
            fin.close();

        }

        //Estimation
        int k1 = sourceBitMapTree.size();
        int k2 = destinationBitMapTree.size();

        Collection sourceEntrySet = sourceBitMapTree.entrySet();
        Iterator sourceEntries = sourceEntrySet.iterator();

        for (int q = 0; q < k1; q++) {
            Map.Entry entrySrc = (Map.Entry) sourceEntries.next();
            BitMap bmpSrc = (BitMap) entrySrc.getValue();

            Collection destinationEntrySet = destinationBitMapTree.entrySet();
            Iterator destinationEntries = destinationEntrySet.iterator();

            for (int r = 0; r < k2; r++) {
                Map.Entry entryDst = (Map.Entry) destinationEntries.next();
                BitMap bmpDst = (BitMap) entryDst.getValue();

                boolean overlap
                        = bmpSrc.getStartEpoch() <= bmpDst.getEndEpoch()
                        && bmpSrc.getEndEpoch() >= bmpDst.getStartEpoch();

                if (overlap) {
                    double sourceDTr = instance.getBitMapSize() * Math.log(((double) instance.getBitMapSize()) / (instance.getBitMapSize() - bmpSrc.occupancy()));

                    double destinationDTr = instance.getBitMapSize() * Math.log(((double) instance.getBitMapSize()) / (instance.getBitMapSize() - bmpDst.occupancy()));

                    BitSet orSrcDst = (BitSet) bmpSrc.getBitSet().clone();

                    orSrcDst.or(bmpDst.getBitSet());
                    double orDTr = instance.getBitMapSize() * Math.log(((double) instance.getBitMapSize()) / (instance.getBitMapSize() - orSrcDst.cardinality()));

                    double estimation = 0D;
                    if (Double.isFinite(orDTr)) {
                        estimation = sourceDTr + destinationDTr - orDTr;
                    }

                    trafficVolume += estimation;
                }
            }
        }
        return trafficVolume;
//        Double a = null;
//        return a;
    }

    protected abstract void doPrintElement(PrintWriter oos, int i, int j, Object element);

    protected abstract void doCalculateMatrixElem(int ingressSW, int egressSW, Topology topology);

    protected abstract String getSrcDir(Instance instance, int srcId);

    protected abstract String getDestDir(Instance instance, int destId);
}
