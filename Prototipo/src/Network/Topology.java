/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Simulador.Instance;
import algs4.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import stdlib.In;

/**
 *
 * @author Igor Siqueira
 */
public class Topology {

    private DijkstraAllPairsSP paths;
    private EdgeWeightedDigraph topology;
    private EdgeWeightedDigraphChild topology2;
    private Set<Integer> nodes;
    private String traces;
    private ArrayList<Instance> lstInstance = new ArrayList<>();
    private int numberOfBitsForSwId;
    private long bitmask;
    private int numberOfSwitches;
    private String saidasDir;
    private int idTopology;
    
    private Long[][] trafficMatrix;
    //private int numberOfPacketFields;

    public void readTopology(String filename) throws IOException, InterruptedException {

        topology = new EdgeWeightedDigraph(new In(filename));
        topology2 = new EdgeWeightedDigraphChild(new In(filename));
        System.out.println(topology);
        PrintWriter writer = new PrintWriter("grafoColorido.dot", "UTF-8");
        writer.println("digraph 1 {");
        writer.println("graph[fontname=\"CourierNew\";rankdir=\"LR\";pad=\"0.25\"]\n"
                + "node[fontname=\"CourierNew\" target=\"_parent\"]\n"
                + "edge[fontname=\"CourierNew\"]\n"
                + "concentrate=true");

        writer.println(topology2);
        writer.println("}");
        writer.close();
        paths = new DijkstraAllPairsSP(topology2);
        System.out.println(paths);
        nodes = new HashSet<>();
        for (DirectedEdge edge : topology.edges()) {
            nodes.add(edge.from());
            nodes.add(edge.to());
        }
        this.setNumberOfSwitches(nodes.size());

    }

    public Set<Integer> getPathNodes(int src, int dest) {
        HashSet<Integer> path = new HashSet<>();
        for (DirectedEdge edge : paths.path(src, dest)) {
            path.add(edge.from());
            path.add(edge.to());
        }
        return path;
    }

    public Iterable<DirectedEdge> getPath(int src, int dest) {
        return paths.path(src, dest);
    }

    public Set<Integer> getNodes() {
        return nodes;
    }

    public PrintWriter paintPath(int ingress, int egress) throws FileNotFoundException, UnsupportedEncodingException {
        for (DirectedEdge edge : this.getPath(ingress,egress)) {
            DirectedEdgeChild edgeChild = (DirectedEdgeChild) edge;
            edgeChild.setColor(true);
        }
        PrintWriter writer = new PrintWriter("grafoColorido.dot", "UTF-8");
        writer.println("digraph 1 {");
        writer.println("graph[fontname=\"CourierNew\";rankdir=\"LR\";pad=\"0.25\"]\n"
                + "node[fontname=\"CourierNew\" target=\"_parent\"]\n"
                + "edge[fontname=\"CourierNew\"]\n");
        writer.println(this.topology2);

        writer.println(ingress + "[style=filled, fillcolor=red]");
        writer.println(egress + "[style=filled, fillcolor=green]");
        writer.println("}");
        writer.close();
        return writer;

    }

    public void receivePacket(DataPacket pkt) throws Exception {
        //substituir pelo get correto depois.. se são t
        int ingress = (int) ((bitmask & pkt.getSrcIP()) >> (32 - numberOfBitsForSwId));

        //Egress Switch
        int egress = (int) ((bitmask & pkt.getDstIP()) >> (32 - numberOfBitsForSwId));

        egress = this.numberOfSwitches - egress - 1;

        // Dropa o pacote (dele pra ele mesmo não faz sentido por enquanto)
        if (ingress == egress) {
            return;
        }
        int t = 0;

        for (Instance instance : this.lstInstance) {
            instance.doReceivePacket(this.getPathNodes(ingress, egress),pkt);
        }

        this.paintPath(ingress, egress);

        t = t;
    }
    
    public void saveLastBitmap(long lastEpoch) throws Exception{
        for (Instance instance : this.lstInstance) {
            instance.saveLastBitmap(lastEpoch);
        }
    }
    
    /**
     * @param aNumberOfSwitches the numberOfSwitches to set
     */
    public void setNumberOfSwitches(int aNumberOfSwitches) {
        this.numberOfSwitches = aNumberOfSwitches;
        this.numberOfBitsForSwId = 31 - Integer.numberOfLeadingZeros(this.numberOfSwitches);
        long base = 0x80000000L;
        this.bitmask = 0x0L;
        for (int i = 0; i < this.numberOfBitsForSwId; i++) {
            this.bitmask += base;
            base = base >> 1;
        }
    }

    public int getNumberOfSwitches() {
        return numberOfSwitches;
    }

    public ArrayList<Instance> getLstInstance() {
        return lstInstance;
    }

    public void setLstInstance(ArrayList<Instance> lstInstance) {
        this.lstInstance = lstInstance;
    }

    public String getTraces() {
        return traces;
    }

    public void setTraces(String traces) {
        this.traces = traces;
    }

    public int getNumberOfBitsForSwId() {
        return numberOfBitsForSwId;
    }

    public void setNumberOfBitsForSwId(int numberOfBitsForSwId) {
        this.numberOfBitsForSwId = numberOfBitsForSwId;
    }

    public long getBitmask() {
        return bitmask;
    }

    public void setBitmask(long bitmask) {
        this.bitmask = bitmask;
    }

    /**
     * @return the saidasDir
     */
    public String getSaidasDir() {
        return saidasDir;
    }

    /**
     * @param aSaidasDir the saidasDir to set
     */
    public void setSaidasDir(String aSaidasDir) {
        saidasDir = aSaidasDir;
        if(!saidasDir.endsWith("\\")){
            saidasDir += "\\";
        }
    }

    public Long[][] getTrafficMatrix() {
        return trafficMatrix;
    }

    public void setTrafficMatrix(Long[][] trafficMatrix) {
        this.trafficMatrix = trafficMatrix;
    }

    public int getIdTopology() {
        return idTopology;
    }

    public void setIdTopology(int idTopology) {
        this.idTopology = idTopology;
    }

}
