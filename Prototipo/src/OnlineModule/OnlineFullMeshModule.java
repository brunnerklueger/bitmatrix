package OnlineModule;

/**
 * Created by rodolfovillaca on 15/09/14.
 */
import Network.DataPacket;
import Network.Hash;
import Network.Switch;
import Simulador.Instance;

public class OnlineFullMeshModule extends OnlineDataStream {

    private Switch[] networkSwitch;

    //@Override
//    protected void doCreateSwtiches(Instance instance,Hash h) throws Exception {
//        networkSwitch = new Switch[instance.getNumberOfSwitches()];
//        for (int id = 0; id < instance.getNumberOfSwitches(); id++) {
//            networkSwitch[id] = new Switch(id, instance.getBitMapSize(), instance.getBitMapThreshold(), h);
//        }
//    }

//    //@Override
//    protected void doReceivePacket(Instance instance, int ingress, int egress, DataPacket pkt) throws Exception {
//        egress = instance.getNumberOfSwitches() - egress - 1;
//
//        // Dropa o pacote (dele pra ele mesmo não faz sentido por enquanto)
//        if (ingress == egress) {
//            return;
//        }
//
//        //System.out.println("Sending to ingress switch #" + swId + " : Packet " + pkt.print());
//        if (networkSwitch[ingress].receivePacket(pkt) > 0) {
//            System.err.println("Error at Ingress Switch #" + ingress + " : Packet " + pkt.print());
//            System.exit(1);
//        }
//
//        if (networkSwitch[egress].receivePacket(pkt) > 0) {
//            System.err.println("Error at Egress Switch #" + egress + " : Packet " + pkt.print());
//            System.exit(2);
//        }
//    }

    //@Override
//    protected void doSaveLastBitmap(long lastEpoch) throws Exception {
//        for (int j = 0; j < Instance.getNumberOfSwitches(); j++) {
//            networkSwitch[j].saveBitmap(lastEpoch);
//        }
//    }
}
