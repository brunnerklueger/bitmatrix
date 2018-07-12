package Network;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rodolfovillaca on 15/09/14.
 */
public class Switch {

    public enum Sketch {

        BITMAP,
        COUNTER_ARRAY;
    }

    protected final int id;
    protected final Hash h;
    protected final int bitMapSize;
    protected final float bitMapThreshold;
    protected BitMap bitMap;
    protected final int mask;
    private int bitMapCounter = 0;
    protected String bmpDir;
    public int[][] arrayCounter;
    public Sketch swType;
    public boolean isObserver;

    public Switch(int id, int bitMapSize, float bitMapThreshold, Hash h, String bitMapDir) throws IOException {
        this.id = id;
        this.bitMapSize = bitMapSize;
        this.bitMapThreshold = bitMapThreshold;
        this.h = h;
        bitMap = new BitMap(bitMapSize);
        int numberOfBits = Integer.highestOneBit(bitMapSize); // Number of bits em binário -> 10000000000
        mask = numberOfBits - 1;                              // mask em binário -> 01111111111
        this.createSwitcheDir(bitMapDir);
        swType = Sketch.BITMAP;
        isObserver = true;
    }

    public Switch(int id, int arraySize, boolean isObserver) throws IOException {
        this.id = id;
        this.swType = Sketch.COUNTER_ARRAY;
        this.arrayCounter = new int[arraySize][arraySize];
        this.isObserver = isObserver;
        this.h = null;
        this.bitMapSize = 0;
        this.bitMapThreshold = 0;
        this.mask = 0;
    }

    public int receivePacket(DataPacket pkt) throws IOException {

        if (bitMap.getNumberOfPackets() == 0) {
            //if (id==12) System.out.println("Zerou!");
            bitMap.setStartEpoch(pkt.getTime());
        }

        bitMap.incrementNumberOfPackets();

        int hashValue = h.vectorHashing(pkt);
        hashValue = h.md5(pkt);
        //System.out.println(hashValue);

        int bitMapIndex = hashValue & mask;
        bitMapIndex = hashValue % bitMapSize;

        bitMap.setPosition(bitMapIndex);

        if (((float) bitMap.getNumberOfPackets() / (float) bitMap.getBitMapSize()) >= bitMapThreshold) {
            this.saveBitmap(pkt.getTime());
        }

        return bitMapIndex;
    }

    public void receivePacket(int src, int dest) {
        try {
            if (isObserver) {
                arrayCounter[src][dest] += 1;
            }
        } catch (Exception ex) {
            System.out.println("ALOU");
        }
    }

    public void saveBitmap(long endEpoch) throws IOException {
        boolean createFile = false;
        FileOutputStream fout;
        ObjectOutputStream oos;
        bitMap.setEndEpoch(endEpoch);
        File bmpFile = new File(bmpDir + "BitMap" + bitMapCounter + ".bmp");
        if (!bmpFile.exists()) {
            bmpFile.createNewFile();
        }
        fout = new FileOutputStream(bmpFile, true);
        oos = new ObjectOutputStream(fout);
        oos.writeObject(bitMap);
        oos.close();
        fout.close();

        File summaryFile = new File(bmpDir + "summary" + ".csv");
        if (!summaryFile.exists()) {
            summaryFile.createNewFile();
            createFile = true;
        }
        FileWriter fw = new FileWriter(summaryFile, true);
        PrintWriter pw = new PrintWriter(fw);
        if (createFile) {
            pw.write("Bmp;Packets;Colisions;Error\n");
        }
        pw.append("bmp" + bitMapCounter + ";" + bitMap.getNumberOfPackets() + ";" + bitMap.getNumberOfColisions() + ";" + String.format("%4.3f", 100.0 * bitMap.getNumberOfColisions() / bitMap.getNumberOfPackets()) + "\n");
        pw.close();
        fw.close();

        bitMapCounter++;

        bitMap = new BitMap(bitMapSize);
    }

    public void createSwitcheDir(String bitMapDir) {
        bmpDir = bitMapDir + "sw" + id + "\\";
        File bmpFile = new File(bmpDir);
        if (!bmpFile.exists()) {

            bmpFile.mkdirs();
        }
    }

}
