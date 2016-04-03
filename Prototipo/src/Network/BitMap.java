package Network;

import java.util.BitSet;
import java.io.Serializable;

/**
 * Created by rodolfovillaca on 23/09/14.
 */
public class BitMap implements Serializable {

    private int bitMapSize;
    private long startEpoch, endEpoch;
    private int numberOfPackets;
    private int numberOfColisions;

    private BitSet bitSet;

    public BitMap(int bitMapSize) {
        this.bitMapSize = bitMapSize;
        bitSet = new BitSet(bitMapSize);
        numberOfPackets = 0;
        bitSet.clear();
    }

    public BitMap(int bitMapSize, long startEpoch) {
        this.bitMapSize = bitMapSize;
        this.startEpoch = startEpoch;
        bitSet = new BitSet(bitMapSize);
        numberOfPackets = 0;
        numberOfColisions = 0;
        bitSet.clear();
    }

    public void setEndEpoch(long endEpoch) {
        this.endEpoch = endEpoch;
    }

    public long getEndEpoch() {
        return endEpoch;
    }

    public void setStartEpoch(long startEpoch) {
        this.startEpoch = startEpoch;
    }

    public long getStartEpoch() {
        return startEpoch;
    }

    public int getBitMapSize() {
        return bitMapSize;
    }

    public void setPosition(int pos) {
        if(bitSet.get(pos)){
            numberOfColisions++;
        }
        bitSet.set(pos);
    }

    public boolean getPosition(int pos) {
        return bitSet.get(pos);
    }

    public int occupancy() {
        return bitSet.cardinality();
    }

    public void reset() {
        numberOfColisions = 0;
        numberOfPackets = 0;
        bitSet.clear();
    }

    public int getSize() {
        return bitSet.size();
    }

    public BitSet getBitSet() {
        return bitSet;
    }
    
    public int getNumberOfColisions(){
        return numberOfColisions;
    }
    
    public void incrementNumberOfPackets(){
        numberOfPackets++;
    }
    
    public int getNumberOfPackets(){
        return numberOfPackets;
    }

}
