/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalysisModule;

/**
 *
 * @author Igor Siqueira
 */
public class DataAnalysisFactory {
    
    public static FullMeshDataAnalysis createFullMeshDataAnalysis(){
        return new FullMeshDataAnalysis();
    }
    
    public static IngressEgressDataAnalysis createIngressEgressDataAnalysis(){
        return new IngressEgressDataAnalysis();
    }
    
    public static ShortestPathAnalysis createShortestPathAnalysis(){
        return new ShortestPathAnalysis();
    }
    
}
