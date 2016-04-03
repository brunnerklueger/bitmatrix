/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulador;

import Network.DataPacket;
import Network.Hash;
import Network.Switch;
import Network.Topology;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Igor Siqueira
 */
public class Instance {
    
    // Internal
    private final Long offset = 21600000000L;
    private final Long secOffset = 1000000L;

    // Get Set
    private String id;
    private String baseDir;
    private String entradasDir;
    private String bitmapDir;
    
    private int bitMapSize;
    private float bitMapThreshold ;
    
    
    private Map<Integer, Switch> networkSwitch;

    
    protected void doCreateSwtiches(Topology topology, Hash h) throws Exception {
        networkSwitch = new HashMap();
        for (Integer id : topology.getNodes()) {
            networkSwitch.put(id, new Switch(id, this.bitMapSize, this.bitMapThreshold, h,this.getBitmapDir()));
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

    public void saveLastBitmap(long lastEpoch) throws Exception {
        for (Switch sw : networkSwitch.values()) {
            sw.saveBitmap(lastEpoch);
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
        if(!bitmapDir.endsWith("\\")){
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
        if(!entradasDir.endsWith("\\")){
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
    public void setBitMapThreshold(int aBitMapThreshold) {
        bitMapThreshold = (float)aBitMapThreshold/100F;
    }

    public void setId(String aId) {
        id = aId;
    }
    
    public String getId(){
        return id;
    }

    public void setBaseDir(String aBaseDir) {
        baseDir = aBaseDir;
    }
    
    public String getBaseDir(){
        return baseDir;
    }
}
