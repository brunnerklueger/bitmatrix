package OnlineModule;

/**
 * Created by rodolfovillaca on 15/09/14.
 */
import Network.DataPacket;
import Network.EgressSwitch;
import Network.Hash;
import Network.IngressSwitch;
import Network.Switch;
import Simulador.Instance;

public class OnlineIngressEgressModule extends OnlineDataStream {

    Switch[] ingressNetworkSwitch;
    Switch[] egressNetworkSwitch;

//    @Override
    protected void doCreateSwtiches(Hash h) throws Exception {
//        ingressNetworkSwitch = new Switch[Instance.getNumberOfSwitches()];
//        for (int id = 0; id < Instance.getNumberOfSwitches(); id++) {
//            ingressNetworkSwitch[id] = new IngressSwitch(id, Instance.getBitMapSize(), Instance.getBitMapThreshold(), h);
//        }
//        egressNetworkSwitch = new Switch[Instance.getNumberOfSwitches()];
//        for (int id = 0; id < Instance.getNumberOfSwitches(); id++) {
//            egressNetworkSwitch[id] = new EgressSwitch(id, Instance.getBitMapSize(), Instance.getBitMapThreshold(), h);
//        }
    }

//    @Override
//    protected void doReceivePacket(int ingress, int egress, DataPacket pkt) throws Exception {
//        
//        if (ingressNetworkSwitch[ingress].receivePacket(pkt) > 0) {
//            System.err.println("Error at Ingress Switch #" + ingress + " : Packet " + pkt.print());
//            System.exit(1);
//        }
//        
//        if (egressNetworkSwitch[egress].receivePacket(pkt) > 0) {
//            System.err.println("Error at Egress Switch #" + egress + " : Packet " + pkt.print());
//            System.exit(2);
//        }
//    }
//
//    @Override
//    protected void doSaveLastBitmap(long lastEpoch) throws Exception {
//        for (int j = 0; j < Instance.getNumberOfSwitches(); j++) {
//            ingressNetworkSwitch[j].saveBitmap(lastEpoch);
//            egressNetworkSwitch[j].saveBitmap(lastEpoch);
//        }
//    }

}
