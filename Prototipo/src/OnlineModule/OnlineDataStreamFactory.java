/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OnlineModule;

/**
 *
 * @author Igor Siqueira
 */
public class OnlineDataStreamFactory {
    public static OnlineFullMeshModule createFullMeshModule(){
        return new OnlineFullMeshModule();
    }
    public static OnlineIngressEgressModule createIngressEgressModule(){
        return new OnlineIngressEgressModule();
    }
    public static OnlineShortestPath createOnlineShortestPath(){
        return new OnlineShortestPath();
    }
}
