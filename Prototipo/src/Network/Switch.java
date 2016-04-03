package Network;

import Simulador.Instance;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.File;

/**
 * Created by rodolfovillaca on 15/09/14.
 */
public class Switch {

    protected final int id;
    protected final Hash h;
    protected final int bitMapSize;
    protected final float bitMapThreshold;
    protected BitMap bitMap;
    protected final int mask;
    private int bitMapCounter = 0;
    protected String bmpDir;

    public Switch(int id, int bitMapSize, float bitMapThreshold, Hash h, String bitMapDir) throws IOException {
        this.id = id;
        this.bitMapSize = bitMapSize;
        this.bitMapThreshold = bitMapThreshold;
        this.h = h;
        bitMap = new BitMap(bitMapSize);
        int numberOfBits = Integer.highestOneBit(bitMapSize); // Number of bits em binário -> 10000000000
        mask = numberOfBits - 1;                              // mask em binário -> 01111111111
        this.createSwitcheDir(bitMapDir);
    }

    public int receivePacket(DataPacket pkt) throws IOException {

        if (bitMap.getNumberOfPackets() == 0) {
            //if (id==12) System.out.println("Zerou!");
            bitMap.setStartEpoch(pkt.getTime());
        }

        bitMap.incrementNumberOfPackets();

        int hashValue = h.vectorHashing(pkt);
        //System.out.println(hashValue);

        int bitMapIndex = hashValue & mask;

        bitMap.setPosition(bitMapIndex);

        if (((float) bitMap.getNumberOfPackets() / (float) bitMap.getBitMapSize()) >= bitMapThreshold) {
            this.saveBitmap(pkt.getTime());
        }

        return bitMapIndex;
    }

    public void saveBitmap(long endEpoch) throws IOException {
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
