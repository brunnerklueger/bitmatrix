package Network;

/**
 * Created by rodolfovillaca on 15/09/14.
 */

public class DataPacket {

    private long time;
    private long srcIP;
    private long dstIP;
    private long srcPort;
    private long dstPort;
    private long protocol;
    private long size;
    private long payload;

    public int numberOfItems = 7;

    public DataPacket(long time, long srcIP, long dstIP, int srcPort, int dstPort, int protocol, int size) {
        this.time = time;
        this.srcIP = srcIP;
        this.dstIP = dstIP;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.protocol = protocol;
        this.size = size;
    }
    
    public DataPacket(long time, long srcIP, long dstIP, int srcPort, int dstPort, int payload) {
        this.time = time;
        this.srcIP = srcIP;
        this.dstIP = dstIP;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.payload = payload;
    }

    public String print() {
        String comma = ", ";
        return time + comma + srcIP + comma + dstIP + comma + srcPort + comma + dstPort + comma + protocol + comma + size;
    }

    //Time
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    // Source IP
    public long getSrcIP() {
        return srcIP;
    }

    public void setSrcIP(long srcIP) {
        this.srcIP = srcIP;
    }

    //Destination IP
    public long getDstIP() {
        return dstIP;
    }

    public void setDstIP(long dstIP) {
        this.dstIP = dstIP;
    }

    //Source Port
    public long getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(long srcPort) {
        this.srcPort = srcPort;
    }

    //Destination Port
    public long getDstPort() {
        return dstPort;
    }

    public void setDstPort(long dstPort) {
        this.dstPort = dstPort;
    }

    //Protocol
    public long getProtocol() {
        return protocol;
    }

    public void setProtocol(long protocol) {
        this.protocol = protocol;
    }

    //Size
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Long getByNumber(int i) {
        switch (i) {
            case 0:
                return time;
            case 1:
                return srcIP;
            case 2:
                return dstIP;
            case 3:
                return srcPort;
            case 4:
                return dstPort;
            case 5:
                return payload;
            default:
                return -1L;
        }
    }

}