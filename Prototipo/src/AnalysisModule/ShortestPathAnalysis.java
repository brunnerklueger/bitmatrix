/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalysisModule;

import Network.Topology;
import Simulador.Instance;
import algs4.DirectedEdge;
import java.io.PrintWriter;

/**
 *
 * @author Igor Siqueira
 */
public class ShortestPathAnalysis extends DataAnalysis {

    @Override
    protected void doPrintElement(PrintWriter oos, int i, int j, Object element) {
        if (j <= i) {
            oos.print(";");
        } else {
            if (element instanceof Long) {
                oos.print(";" + element);
            } else {
                oos.print(";" + String.format("%4.3f", element));
            }
        }
    }

    @Override
    protected void doCalculateMatrixElem(int ingress, int egress,Topology topology) {
        int ingressSW;
        int egressSW;
        Long[][] trafficMatrix = topology.getTrafficMatrix();
        egress = topology.getNumberOfSwitches() - egress - 1;

        for (DirectedEdge edge : topology.getPath(ingress, egress)) {
            
            for (DirectedEdge edge2 : topology.getPath(edge.from(), egress)) {
                ingressSW = edge.from();
                egressSW = edge2.to();
                if (ingressSW > egressSW) {
                    ingressSW = ingressSW ^ egressSW;
                    egressSW = ingressSW ^ egressSW;
                    ingressSW = ingressSW ^ egressSW;
                }

                 trafficMatrix[ingressSW][egressSW] += 1L;
            }
        }
        topology.setTrafficMatrix(trafficMatrix);
    }

    @Override
    protected String getSrcDir(Instance instance, int sourceId) {
        return instance.getBitmapDir() + "sw" + sourceId + "\\";
    }

    @Override
    protected String getDestDir(Instance instance, int destId) {
        return instance.getBitmapDir() + "sw" + destId + "\\";
    }

}
