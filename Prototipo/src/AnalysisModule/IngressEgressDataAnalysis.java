package AnalysisModule;

import Network.Topology;
import Network.Instance;
import java.io.PrintWriter;

/**
 * Created by rodolfovillaca on 24/09/14.
 */
public class IngressEgressDataAnalysis extends DataAnalysis {

    @Override
    protected void doPrintElement(PrintWriter oos, int i, int j, Object element) {
        if (element instanceof Long) {
            oos.print(";" + element);
        } else {
            oos.print(";" + String.format("%4.3f", element));
        }
    }

    protected void doCalculateMatrixElem(int ingressSW, int egressSW, Long[][] trafficMatrix) {
        trafficMatrix[ingressSW][egressSW] += 1L;
    }

//    @Override
//    protected String getSrcDir(int sourceId) {
////        return Instance.getBitmapDir() + "ingress" + sourceId + "\\";
//        String str = null;
//        return str;
//    }
//
//    @Override
//    protected String getDestDir(int destId) {
////        return Instance.getBitmapDir() + "egress" + destId + "\\";
//        String str = null;
//        return str;
//    }

    @Override
    protected void doCalculateMatrixElem(int ingressSW, int egressSW, Topology topology) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getSrcDir(Instance instance, int srcId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getDestDir(Instance instance, int destId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
