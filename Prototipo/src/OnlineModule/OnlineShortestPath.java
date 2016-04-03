/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OnlineModule;

import Network.DataPacket;
import Network.Hash;
import Network.Switch;
import Network.Topology;
import Simulador.Instance;
import algs4.DirectedEdge;
import algs4.DirectedEdgeChild;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Igor Siqueira
 */
public class OnlineShortestPath extends OnlineDataStream {

    private Map<Integer, Switch> networkSwitch;

//    @Override
//    protected void doCreateSwtiches(Hash h) throws Exception {
//        networkSwitch = new HashMap();
//        for (Integer id : Topology.getNodes()) {
//            networkSwitch.put(id, new Switch(id, Instance.getBitMapSize(), Instance.getBitMapThreshold(), h));
//        }
//    }
//
//    @Override
//    protected void doReceivePacket(int ingress, int egress, DataPacket pkt) throws Exception {
//
//        egress = Instance.getNumberOfSwitches() - egress - 1;
//
//        // Dropa o pacote (dele pra ele mesmo não faz sentido por enquanto)
//        if (ingress == egress) {
//            return;
//        }
//        int t = 0;
//
//        for (Integer sw : Topology.getPathNodes(ingress, egress)) {
//            try {
//                t = networkSwitch.get(sw).receivePacket(pkt);
//            } catch (NullPointerException ex) {
//                System.err.println("Error: Inexistent Switch [ " + sw + "]");
//            }
//        }
//
//        
//        Topology.paintPath(ingress , egress);
//        
//        t = t;
//    }
//
//    @Override
//    protected void doSaveLastBitmap(long lastEpoch) throws Exception {
//        for (Switch sw : networkSwitch.values()) {
//            sw.saveBitmap(lastEpoch);
//        }
//    }
}
