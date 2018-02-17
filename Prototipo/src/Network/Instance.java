/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import algs4.DirectedEdge;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Igor Siqueira
 */
public class Instance {

    public enum InstanceType {

        BITMAP,
        COUNTER_ARRAY,
        OPT_COUNTER_ARRAY;

        public static InstanceType getType(int type) throws Exception {
            switch (type) {
                case 1:
                    return BITMAP;
                case 2:
                    return COUNTER_ARRAY;
                case 3:
                    return OPT_COUNTER_ARRAY;
                default:
                    throw new Exception("Tipo inexistente");
            }
        }
    }

    // Internal
    private final Long offset = 21600000000L;
    private final Long secOffset = 1000000L;

    // Get Set
    private String id;
    private String baseDir;
    private String entradasDir;
    private String bitmapDir;

    private int bitMapSize;
    private float bitMapThreshold;

    public Map<Integer, Switch> networkSwitch = new HashMap();
    ;

    public double[][] trafficMatrix;

    public InstanceType type;

    protected void createSwitch(Integer swId, int nbSw, Hash h, boolean isObserver) throws Exception {
        switch (type) {
            case BITMAP:
                networkSwitch.put(swId, new Switch(swId, this.bitMapSize, this.bitMapThreshold, h, this.getBitmapDir()));
                break;
            case COUNTER_ARRAY:
                networkSwitch.put(swId, new Switch(swId, nbSw, true));
                break;
            case OPT_COUNTER_ARRAY:
                networkSwitch.put(swId, new Switch(swId, nbSw, isObserver));
                break;
        }
    }

    public void doReceivePacket(Set<Integer> nodes, DataPacket pkt) throws Exception {
        for (Integer sw : nodes) {
            try {
                this.networkSwitch.get(sw).receivePacket(pkt);
            } catch (NullPointerException ex) {
                System.err.println("Error: Inexistent Switch [ " + sw + "]");
            }
        }
    }

    public void doReceivePacket(int src, int dest, Set<Integer> path) throws Exception {
        for (Integer sw : path) {
            try {
                this.networkSwitch.get(sw).receivePacket(src, dest);
            } catch (NullPointerException ex) {
                System.err.println("Error: Inexistent Switch [ " + sw + "]");
            }
        }
    }

    public void doCalculateMatrixElem(int ingress, int egress, Topology topology, int traffic) {
        int ingressSW;
        int egressSW;

        for (DirectedEdge edge : topology.getPath(ingress, egress)) {

            for (DirectedEdge edge2 : topology.getPath(edge.from(), egress)) {
                ingressSW = edge.from();
                egressSW = edge2.to();
                if (ingressSW > egressSW) {
                    ingressSW = ingressSW ^ egressSW;
                    egressSW = ingressSW ^ egressSW;
                    ingressSW = ingressSW ^ egressSW;
                }

                trafficMatrix[ingressSW][egressSW] += traffic;
            }
        }
    }

    public void saveLastBitmap(long lastEpoch) throws Exception {
        if (type.equals(InstanceType.BITMAP)) {
            for (Switch sw : networkSwitch.values()) {
                sw.saveBitmap(lastEpoch);
            }
        }
    }

    /**
     * @return the bitmapDir
     */
    public String getBitmapDir() {
        return bitmapDir;
    }

    /**
     * @param aBitmapDir the bitmapDir to set
     */
    public void setBitmapDir(String aBitmapDir) {
        bitmapDir = aBitmapDir;
        if (!bitmapDir.endsWith("\\")) {
            bitmapDir += "\\";
        }
    }

    /**
     * @return the entradasDir
     */
    public String getEntradasDir() {
        return entradasDir;
    }

    /**
     * @param aEntradasDir the entradasDir to set
     */
    public void setEntradasDir(String aEntradasDir) {
        entradasDir = aEntradasDir;
        if (!entradasDir.endsWith("\\")) {
            entradasDir += "\\";
        }
    }

    /**
     * @return the bitMapSize
     */
    public int getBitMapSize() {
        return bitMapSize;
    }

    /**
     * @param aBitMapSize the bitMapSize to set
     */
    public void setBitMapSize(int aBitMapSize) {
        bitMapSize = aBitMapSize;
    }

    /**
     * @return the bitMapThreshold
     */
    public float getBitMapThreshold() {
        return bitMapThreshold;
    }

    /**
     * @param aBitMapThreshold the bitMapThreshold to set
     */
    public void setBitMapThreshold(float aBitMapThreshold) {
        bitMapThreshold = (float) aBitMapThreshold / 100F;
    }

    public String getId() {
        if (type == InstanceType.BITMAP) {
            return bitMapSize + "_" + ((int) (bitMapThreshold * 100));
        } else if (type == InstanceType.COUNTER_ARRAY) {
            return "CONTER_ARRAY";
        } else {
            return "OPT_COUNTER_ARRAY";
        }
    }

    public void setBaseDir(String aBaseDir) {
        baseDir = aBaseDir;
    }

    public String getBaseDir() {
        return baseDir;
    }
}
