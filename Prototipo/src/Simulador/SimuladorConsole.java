/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulador;

import AnalysisModule.DataAnalysis;
import AnalysisModule.DataAnalysisFactory;
import Network.Hash;
import Network.Scenario;
import Network.Topology;
import OnlineModule.OnlineDataStream;
import OnlineModule.OnlineDataStreamFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Igor Siqueira
 */
public class SimuladorConsole {

    ArrayList<Topology> lstTopology = new ArrayList<>();

    public static void main(String argv[]) {
        try {

            String filenameTopology = null;
            String filenameScenario = null;
            boolean dtdValidate = false;
            boolean xsdValidate = false;

            boolean ignoreWhitespace = false;
            boolean ignoreComments = false;
            boolean putCDATAIntoText = false;
            boolean createEntityRefs = false;

            if (filenameTopology == null) {
                //usage();
                filenameTopology = "Config\\configTopology.xml";
            }
            if (filenameScenario == null) {
                filenameScenario = "Config\\configScenario.xml";
            }

            // Step 1: create a DocumentBuilderFactory and configure it
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // Set namespaceAware to true to get a DOM Level 2 tree with nodes
            // containing namesapce information.  This is necessary because the
            // default value from JAXP 1.0 was defined to be false.
            dbf.setNamespaceAware(true);

            // Set the validation mode to either: no validation, DTD
            // validation, or XSD validation
            dbf.setValidating(dtdValidate || xsdValidate);

            // Optional: set various configuration options
            dbf.setIgnoringComments(ignoreComments);
            dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
            dbf.setCoalescing(putCDATAIntoText);
            // The opposite of creating entity ref nodes is expanding them inline
            dbf.setExpandEntityReferences(!createEntityRefs);

            // Step 2: create a DocumentBuilder that satisfies the constraints
            // specified by the DocumentBuilderFactory
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Step 3: parse the input file
            Document docScenario = db.parse(new File(filenameScenario));
            Document docTopology = db.parse(new File(filenameTopology));
            List<Node> lstDoc = new ArrayList<>();
            lstDoc.add(docTopology);
            lstDoc.add(docScenario);
            Scenario.quantidadeScenarios = 1;
            new SimuladorConsole().readAll(lstDoc);

        } catch (Exception ex) {

            System.out.println("Error occurred " + ex);
        }
    }

    public void readAll(List<Node> lstDoc) throws Exception {
        Node doc;
        for (int i = 0; i < lstDoc.size(); i++) {
            doc = lstDoc.get(i);
            if (i == 0) {
                read(doc, i);

            } else {
                for (int j = 0; j < Scenario.quantidadeScenarios; j++) {
                    read(doc, 1);
                }
            }

        }
    }

    public void read(Node doc, int idConfig) throws Exception {
        if (doc == null) {
            return;
        }
        if (doc.getNodeType() != Node.ELEMENT_NODE || !doc.getNodeName().equals("Scenario")) {
            for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
                read(child, idConfig);
            }

        } else {
            readScenario(doc, idConfig);
        }

    }

    public void readScenario(Node doc, int idConfig) throws Exception {

        int cntTopology = 0;
        String traces = "";
        String topologyName = "Topology";

        Hash h = new Hash();
        //Numero de pacotes travado em 6
        h.initializeVectorHash(6);

        if (idConfig == 0) {
            for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {

                switch (child.getNodeName()) {
                    case "traces":
                        traces = child.getFirstChild().getNodeValue();
                        break;
                    case "Topology":
                        Topology topology = new Topology();
                        for (Node grandChild = child.getFirstChild(); grandChild != null; grandChild = grandChild.getNextSibling()) {

                            switch (grandChild.getNodeName()) {
                                case "id":
                                    topology.setIdTopology(Integer.parseInt(grandChild.getFirstChild().getNodeValue()));
                                    break;
                                case "path":
                                    topology.readTopology(grandChild.getFirstChild().getNodeValue());
                                    cntTopology++;
                                    //instance.setBaseDir("D:\\Mestrado\\Prototipo\\Simulations\\" + topologyName + "\\Instance" + instance.getId());
                                    this.lstTopology.add(topology);
                                    break;

                                case "Instance":

                                    Instance instanceTopology = new Instance();

                                    readParameters(topologyName + cntTopology, instanceTopology, grandChild);
                                    instanceTopology.doCreateSwtiches(this.lstTopology.get(cntTopology - 1), h);
                                    this.lstTopology.get(cntTopology - 1).getLstInstance().add(instanceTopology);
                                    break;
                            }
                        }
                        break;
                }

            }
            OnlineDataStream onlineModule = OnlineDataStreamFactory.createOnlineShortestPath();

            onlineModule.simulate(this.lstTopology, traces);
        } else {

            for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
                switch (child.getNodeName()) {
                    case "Quantidade":
                        Scenario.quantidadeScenarios = Integer.parseInt(child.getFirstChild().getNodeValue());

                        break;
                    case "TempControl":
                        for (Node grandChild = child.getFirstChild(); grandChild != null; grandChild = grandChild.getNextSibling()) {
                            if (grandChild.getNodeType() == Node.ELEMENT_NODE) {
                                readParameterScenario(grandChild);
                            }
                        }

                        break;

                }
            }

        }

    }

    public void simulateAnalise() throws Exception {
        DataAnalysis dataModule = DataAnalysisFactory.createShortestPathAnalysis();

        System.out.println("Executando Real Analysis....");
        dataModule.realAnalyse(this.lstTopology);

        System.out.println("Executando Data Analysis....");
        dataModule.simulateBitmapAnalyse(this.lstTopology);

        System.out.println("Finalizado!");
    }

    private void readParameters(String topologyName, Instance instanceTopology, Node instance) throws Exception {
//        System.out.println("Executando instancia: " + instance.getNodeValue());
        for (Node child = instance.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                readParameterTopology(topologyName, instanceTopology, child);
            }
        }
        instanceTopology.setBitmapDir(instanceTopology.getBaseDir() + "\\Bitmaps");

    }

    private void readParameterTopology(String topologyName, Instance instance, Node n) throws Exception {
        switch (n.getNodeName()) {
            case "id":
                instance.setId(n.getFirstChild().getNodeValue());
                instance.setBaseDir("D:\\Mestrado\\Prototipo\\Simulations\\Bitmaps\\" + topologyName + "\\Instance" + instance.getId());
                break;
            case "bmpSize":
                instance.setBitMapSize(Integer.parseInt(n.getFirstChild().getNodeValue()));
                break;
            case "bmpThreshold":
                instance.setBitMapThreshold(Integer.parseInt(n.getFirstChild().getNodeValue()));
                break;
            default:
                throw new Exception("Parâmetro inexistente");
        }
    }

    private void readParameterScenario(Node n) throws Exception {
        switch (n.getNodeName()) {
            case "id":
                Scenario.idScenario = Integer.parseInt(n.getFirstChild().getNodeValue());
                for (Topology topology : this.lstTopology) {
                    topology.setSaidasDir("D:\\Mestrado\\Prototipo\\Simulations\\Analise\\"+ "Scenario" + Scenario.idScenario+"\\"+"Topology"+topology.getIdTopology()+"\\");
                    createDirectoryTree(topology.getSaidasDir());
                }
                break;
            case "startTime":
                Scenario.startTime = Long.parseLong(n.getFirstChild().getNodeValue());
                break;
            case "endTime":
                Scenario.endTime = Long.parseLong(n.getFirstChild().getNodeValue());
                simulateAnalise();
                break;
            default:
                throw new Exception("Parâmetro inexistente");
        }
    }

//    private void createDirectoryTree(String directory) {
    private void createDirectoryTree(String directory) {
        File dir = new File(directory);
        if (dir.exists()) {
            clearDirectoryTree(dir);
        }
        dir.mkdirs();
    }

    private void clearDirectoryTree(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                clearDirectoryTree(file);
            } else {
                file.delete();
            }
        }
        dir.delete();
    }

//    public void simulateInstance(Instance instanceTopology, Node instance) throws Exception {
//        OnlineDataStream onlineModule = OnlineDataStreamFactory.createOnlineShortestPath();
//        DataAnalysis dataModule = DataAnalysisFactory.createShortestPathAnalysis();
//
////        System.out.println("Lendo Parâmetros....");
////        readParameters(instanceTopology, instance);
//        System.out.println("Criando diretórios de bitmaps....");
//        createDirectoryTree(instanceTopology.getBitmapDir());
//        //createDirectoryTree(Instance.getBitmapDir());
//
////        System.out.println("Executando Online Module....");
////        onlineModule.simulate(instanceTopology);
//        System.out.println("Ciando dirtórios de Resultados....");
//        createDirectoryTree(instanceTopology.getSaidasDir());
//        //createDirectoryTree(Instance.getSaidasDir());
//
//        System.out.println("Executando Real Analysis....");
//        dataModule.realAnalyse();
//
//        System.out.println("Executando Data Analysis....");
//        dataModule.simulateBitmapAnalyse();
//
//        System.out.println("Finalizado!");
//    }
//        private void readParameters(Instance instanceTopology, Node instance) throws Exception {
//        NamedNodeMap atts = instance.getAttributes();
//        for (int i = 0; i < atts.getLength(); i++) {
//            Node att = atts.item(i);
//        //instanceTopology.setId(instance.getNodeValue());
//
//            Instance.setId(att.getNodeValue());
//            Instance.setBaseDir("D:\\Mestrado\\Prototipo\\Simulations\\Instance" + att.getNodeValue());
//            Instance.setBitmapDir(Instance.getBaseDir() + "\\Bitmaps");
//            Instance.setSaidasDir(Instance.getBaseDir() + "\\Saidas");
//        System.out.println("Executando instancia: " + instance.getNodeValue());
//        }
//        for (Node child = instance.getFirstChild(); child != null; child = child.getNextSibling()) {
//            if (child.getNodeType() == Node.ELEMENT_NODE) {
//                readParameter(instanceTopology, child);
//            }
//        }
//
//        instanceTopology.setBitmapDir(instanceTopology.getBaseDir() + "\\Bitmaps");
//        instanceTopology.setSaidasDir(instanceTopology.getBaseDir() + "\\Saidas");
//    }
//    private void readParameter(Instance instance, Node n) throws Exception {
//        switch (n.getNodeName()) {
//            case "id":
//                instance.setId(n.getFirstChild().getNodeValue());
//                instance.setBaseDir("D:\\Mestrado\\Prototipo\\Simulations\\Instance" + instance.getId());
//                break;
//            // Lido na topologia
////            case "switches":
////                Instance.setNumberOfSwitches(Integer.parseInt(n.getFirstChild().getNodeValue()));
////                break;
//
//            //Fields fixados em 6
////            case "fields":
////                instance.setNumberOfPacketFields(Integer.parseInt(n.getFirstChild().getNodeValue()));
////                break;
//            case "bmpSize":
//                instance.setBitMapSize(Integer.parseInt(n.getFirstChild().getNodeValue()));
//                break;
//            case "bmpThreshold":
//                instance.setBitMapThreshold(Integer.parseInt(n.getFirstChild().getNodeValue()));
//                break;
//            case "startTime":
//                instance.setStartTime(Long.parseLong(n.getFirstChild().getNodeValue()));
//                break;
//            case "endTime":
//                instance.setEndTime(Long.parseLong(n.getFirstChild().getNodeValue()));
//                break;
//            default:
//                throw new Exception("Parâmetro inexistente");
//        }
//    }
}
