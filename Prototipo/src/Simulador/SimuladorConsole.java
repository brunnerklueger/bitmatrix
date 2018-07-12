/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulador;

import Network.Hash;
import Network.Instance;
import Network.Topology;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Igor Siqueira
 */
public class SimuladorConsole {

    ArrayList<Scenario> lstScenario = new ArrayList<>();
    Scenario actScenario;
    Topology actTopology;
    Instance actInstance;
    String simulationDir;
    static Hash hash = new Hash();

    public static void main(String argv[]) {
        try {

            String filenameConfig = null;
            boolean dtdValidate = false;
            boolean xsdValidate = false;

            boolean ignoreWhitespace = false;
            boolean ignoreComments = false;
            boolean putCDATAIntoText = false;
            boolean createEntityRefs = false;

            if (filenameConfig == null) {
                //usage();
                filenameConfig = "Config\\config.xml";
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
            Document docConfig = db.parse(new File(filenameConfig));
            //new SimuladorConsole().readAll(lstDoc);
            hash.initializeVectorHash(6);
            SimuladorConsole simulator = new SimuladorConsole();
            simulator.read(docConfig);
            new OnlineModule.OnlineDataStream().simulate(simulator.lstScenario);
            new AnalysisModule.ShortestPathAnalysis().realAnalyse(simulator.lstScenario);
            new AnalysisModule.ShortestPathAnalysis().simulateBitmapAnalyse(simulator.lstScenario);

//            List<Set> paths = new ArrayList<>();
//            for (int i = 0; i < simulator.actTopology.getNumberOfSwitches(); i++) {
//                for (int j = i + 1; j < simulator.actTopology.getNumberOfSwitches(); j++) {
//                    Set A = simulator.actTopology.getPathNodes(i, j);
////                    int k;
////                    for (k = 0; k < paths.size();) {
////                        Set B = paths.get(k);
////                        if(A.containsAll(B)) paths.remove(k);
////                        else if(B.containsAll(A)) break;
////                        else k++;
////                    }
////                    if(k == paths.size() ){
//                        paths.add(A);
////                    }
//                }
//            }
//
//            byte[][] matrix = new byte[simulator.actTopology.getNumberOfSwitches()][paths.size()];
//            String[] labels = new String[paths.size()];
//
//            for (int i = 0; i < paths.size(); i++) {
//                labels[i] = "Path" + (i + 1);
//                System.out.println(labels[i] + " " + paths.get(i));
//            }
//
//            for (int i = 0; i < simulator.actTopology.getNumberOfSwitches(); i++) {
//                for (int j = 0; j < paths.size(); j++) {
//                    Set path = (Set) paths.get(j);
//                    if (path.contains(i)) {
//                        matrix[i][j] = 1;
//                    }
//                }
//            }
//
//            for (int i = 0; i < simulator.actTopology.getNumberOfSwitches(); i++) {
//                System.out.println(Arrays.toString(matrix[i]));
//            }
//
//            ColumnObject h = DLX.buildSparseMatrix(matrix, labels);
//            DLX.solve(h, true);
//            while (true) {
//                SoundUtils.tone(450, 100);
//                Thread.sleep(1000);
//                if(false){
//                    break;
//                }
//            }
        } catch (Exception ex) {

            System.out.println("Error occurred " + ex);
            ex.printStackTrace();
        }
    }

//    public void readAll(List<Node> lstDoc) throws Exception {
//        Node doc;
//        for (int i = 0; i < lstDoc.size(); i++) {
//            doc = lstDoc.get(i);
//            if (i == 0) {
//                read(doc, i);
//
//            } else {
//                for (int j = 0; j < Scenario.quantidadeScenarios; j++) {
//                    read(doc, 1);
//                }
//            }
//
//        }
//    }
//
//    public void read(Node doc, int idConfig) throws Exception {
//        if (doc == null) {
//            return;
//        }
//        if (doc.getNodeType() != Node.ELEMENT_NODE || !doc.getNodeName().equals("Scenario")) {
//            for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
//                read(child, idConfig);
//            }
//
//        } else {
//            readScenario(doc, idConfig);
//        }
//
//    }
//
//    public void readScenario(Node doc, int idConfig) throws Exception {
//
//        int cntTopology = 0;
//        String traces = "";
//        String topologyName = "Topology";
//
//        Hash h = new Hash();
//        //Numero de pacotes travado em 6
//        h.initializeVectorHash(6);
//
//        if (idConfig == 0) {
//            for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
//
//                switch (child.getNodeName()) {
//                    case "traces":
//                        traces = child.getFirstChild().getNodeValue();
//                        break;
//                    case "Topology":
//                        Topology topology = new Topology();
//                        for (Node grandChild = child.getFirstChild(); grandChild != null; grandChild = grandChild.getNextSibling()) {
//
//                            switch (grandChild.getNodeName()) {
//                                case "id":
//                                    topology.setIdTopology(Integer.parseInt(grandChild.getFirstChild().getNodeValue()));
//                                    break;
//                                case "path":
//                                    topology.readTopology(grandChild.getFirstChild().getNodeValue());
//                                    cntTopology++;
//                                    //instance.setBaseDir("D:\\Mestrado\\Prototipo\\Simulations\\" + topologyName + "\\Instance" + instance.getId());
//                                    this.lstTopology.add(topology);
//                                    break;
//
//                                case "Instance":
//
//                                    Instance instanceTopology = new Instance();
//
//                                    readParameters(topologyName + cntTopology, instanceTopology, grandChild);
//                                    instanceTopology.doCreateSwtiches(this.lstTopology.get(cntTopology - 1), h);
//                                    this.lstTopology.get(cntTopology - 1).getLstInstance().add(instanceTopology);
//                                    break;
//                            }
//                        }
//                        break;
//                }
//
//            }
//            OnlineDataStream onlineModule = OnlineDataStreamFactory.createOnlineShortestPath();
//
//            onlineModule.simulate(this.lstTopology, traces);
//        } else {
//
//            for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
//                switch (child.getNodeName()) {
//                    case "Quantidade":
//                        Scenario.quantidadeScenarios = Integer.parseInt(child.getFirstChild().getNodeValue());
//
//                        break;
//                    case "TempControl":
//                        for (Node grandChild = child.getFirstChild(); grandChild != null; grandChild = grandChild.getNextSibling()) {
//                            if (grandChild.getNodeType() == Node.ELEMENT_NODE) {
//                                readParameterScenario(grandChild);
//                            }
//                        }
//
//                        break;
//
//                }
//            }
//
//        }
//
//    }
//
//    public void simulateAnalise() throws Exception {
//        DataAnalysis dataModule = DataAnalysisFactory.createShortestPathAnalysis();
//
//        System.out.println("Executando Real Analysis....");
//        dataModule.realAnalyse(this.lstTopology);
//
//        System.out.println("Executando Data Analysis....");
//        dataModule.simulateBitmapAnalyse(this.lstTopology);
//
//        System.out.println("Finalizado!");
//    }
//
//    private void readParameters(String topologyName, Instance instanceTopology, Node instance) throws Exception {
////        System.out.println("Executando instancia: " + instance.getNodeValue());
//        for (Node child = instance.getFirstChild(); child != null; child = child.getNextSibling()) {
//            if (child.getNodeType() == Node.ELEMENT_NODE) {
//                readParameterTopology(topologyName, instanceTopology, child);
//            }
//        }
//        instanceTopology.setBitmapDir(instanceTopology.getBaseDir() + "\\Bitmaps");
//
//    }
//
//    private void readParameterTopology(String topologyName, Instance instance, Node n) throws Exception {
//        switch (n.getNodeName()) {
//            case "id":
//                instance.setId(n.getFirstChild().getNodeValue());
//                instance.setBaseDir("D:\\Mestrado\\Prototipo\\Simulations\\Bitmaps\\" + topologyName + "\\Instance" + instance.getId());
//                break;
//            case "bmpSize":
//                instance.setBitMapSize(Integer.parseInt(n.getFirstChild().getNodeValue()));
//                break;
//            case "bmpThreshold":
//                instance.setBitMapThreshold(Integer.parseInt(n.getFirstChild().getNodeValue()));
//                break;
//            default:
//                throw new Exception("Parâmetro inexistente");
//        }
//    }
//
//    private void readParameterScenario(Node n) throws Exception {
//        switch (n.getNodeName()) {
//            case "id":
//                Scenario.idScenario = Integer.parseInt(n.getFirstChild().getNodeValue());
//                for (Topology topology : this.lstTopology) {
//                    topology.setSaidasDir("D:\\Mestrado\\Prototipo\\Simulations\\Analise\\"+ "Scenario" + Scenario.idScenario+"\\"+"Topology"+topology.getIdTopology()+"\\");
//                    createDirectoryTree(topology.getSaidasDir());
//                }
//                break;
//            case "startTime":
//                Scenario.startTime = Long.parseLong(n.getFirstChild().getNodeValue());
//                break;
//            case "endTime":
//                Scenario.endTime = Long.parseLong(n.getFirstChild().getNodeValue());
//                simulateAnalise();
//                break;
//            default:
//                throw new Exception("Parâmetro inexistente");
//        }
//    }
//    private void createDirectoryTree(String directory) {
    public static void createDirectoryTree(String directory) {
        File dir = new File(directory);
        if (dir.exists()) {
            clearDirectoryTree(dir);
        }
        dir.mkdirs();
    }

    public static void clearDirectoryTree(File dir) {
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

    private void read(Node node) throws Exception {
        if (node == null) {
            return;
        }
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            switch (node.getNodeName()) {
                case "Config":
                    for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
                        read(n);
                    }
                    break;
                case "SimulationDir":
                    simulationDir = node.getFirstChild().getNodeValue();
                    break;
                case "Scenario":
                    actScenario = new Scenario();
                    lstScenario.add(actScenario);
                    for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
                        read(n);
                    }
                    break;
                case "ScenarioId":
                    actScenario.idScenario = Integer.parseInt(node.getFirstChild().getNodeValue());
                    break;
                case "startTime":
                    actScenario.startTime = Long.parseLong(node.getFirstChild().getNodeValue());
                    break;
                case "endTime":
                    actScenario.endTime = Long.parseLong(node.getFirstChild().getNodeValue());
                    break;
                case "traces":
                    actScenario.traceFile = node.getFirstChild().getNodeValue();
                    break;
                case "Topology":
//                    System.out.print("Criando Instância: ");
//                    actTopology = new Topology();
//                    actScenario.lstTopology.add(actTopology);
//                    for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
//                        read(n);
//                    }
//                    actTopology.createSwitches(hash);
                    break;
                case "TopologyId":
//                    System.out.println(node.getFirstChild().getNodeValue());
//                    actTopology.setIdTopology(Integer.parseInt(node.getFirstChild().getNodeValue()));
//                    actTopology.setSaidasDir(simulationDir + "\\Analise\\" + "Scenario" + actScenario.idScenario + "\\" + "Topology" + actTopology.getIdTopology() + "\\");
//                    createDirectoryTree(actTopology.getSaidasDir());
                    break;
                case "path":
                    System.out.print("Criando Instância: ");
                    actTopology = new Topology();
                    actScenario.lstTopology.add(actTopology);

                    int startIndex = node.getFirstChild().getNodeValue().lastIndexOf("\\") + 1;
                    actTopology.setIdTopology(node.getFirstChild().getNodeValue().substring(startIndex));
                    String scenarioDir = simulationDir + "\\Analise\\" + "Scenario" + actScenario.idScenario;
                    actTopology.setSaidasDir(scenarioDir + "\\" + actTopology.getIdTopology() + "\\");
                    createDirectoryTree(actTopology.getSaidasDir());
                    createScenarioConfigFile(scenarioDir);//serve apenas para o GeraRelatorio.java
                    

                    actTopology.readTopology(node.getFirstChild().getNodeValue());
                    actTopology.printTopology();

                    for (Integer size : actScenario.lstSizes) {
                        for (Integer threshold : actScenario.lstThresholds) {
                            actInstance = new Instance();
                            actTopology.getLstInstance().add(actInstance);

                            actInstance.setBitMapSize(size);
                            actInstance.setBitMapThreshold(threshold);
                            actInstance.type = Instance.InstanceType.BITMAP;
                            actInstance.setBaseDir(simulationDir + "\\Bitmaps\\" + actTopology.getIdTopology() + "\\" + actInstance.getId());
                            actInstance.setBitmapDir(actInstance.getBaseDir() + "\\Bitmaps");
                            createDirectoryTree(actInstance.getBaseDir());
                        }
                    }
                    
                    actInstance = new Instance();
                    actTopology.getLstInstance().add(actInstance);
                    actInstance.type = Instance.InstanceType.COUNTER_ARRAY;
                    actInstance.setBaseDir(simulationDir + "\\Bitmaps\\" + actTopology.getIdTopology() + "\\" + actInstance.getId());
                    actInstance.setBitmapDir(actInstance.getBaseDir() + "\\Bitmaps");
                    createDirectoryTree(actInstance.getBaseDir());
                    
                    actInstance = new Instance();
                    actTopology.getLstInstance().add(actInstance);
                    actInstance.type = Instance.InstanceType.OPT_COUNTER_ARRAY;
                    actInstance.setBaseDir(simulationDir + "\\Bitmaps\\" + actTopology.getIdTopology() + "\\" + actInstance.getId());
                    actInstance.setBitmapDir(actInstance.getBaseDir() + "\\Bitmaps");
                    createDirectoryTree(actInstance.getBaseDir());
                    
                    actTopology.createSwitches(hash);

                    break;
                case "Instance":
//                    actInstance = new Instance();
//                    for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
//                        read(n);
//                    }
//                    actTopology.getLstInstance().add(actInstance);
                    break;
                case "InstanceId":
//                    actInstance.setId(node.getFirstChild().getNodeValue());
//                    actInstance.setBaseDir(simulationDir + "\\Bitmaps\\Topology" + actTopology.getIdTopology() + "\\Instance" + actInstance.getId());
//                    actInstance.setBitmapDir(actInstance.getBaseDir() + "\\Bitmaps");
//                    createDirectoryTree(actInstance.getBaseDir());
                    break;
                case "Sizes":
                    for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
                        read(n);
                    }
                    break;
                case "Thresholds":
                    for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
                        read(n);
                    }
                    break;
                case "Type":
                    actInstance.type = Instance.InstanceType.getType(Integer.parseInt(node.getFirstChild().getNodeValue()));
                    break;
                case "bmpSize":
//                    actInstance.setBitMapSize(Integer.parseInt(node.getFirstChild().getNodeValue()));
                    actScenario.lstSizes.add(Integer.parseInt(node.getFirstChild().getNodeValue()));
                    break;
                case "bmpThreshold":
//                    actInstance.setBitMapThreshold(Float.parseFloat(node.getFirstChild().getNodeValue()));
                    actScenario.lstThresholds.add(Integer.parseInt(node.getFirstChild().getNodeValue()));
                    break;
                default:
                    throw new Exception("Parâmetro " + node.getNodeName() + " desconhecido");
            }
        } else {
            read(node.getFirstChild());
        }
    }
    
    public void createScenarioConfigFile(String scenarioPath){
        
        String[] traceFileArrayName = actScenario.traceFile.split(Pattern.quote(File.separator));
        
        String traceFileName = traceFileArrayName[traceFileArrayName.length-1];
        
        
        BufferedWriter writer=null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(scenarioPath+ "\\detalhes.txt"), "utf-8"));
            writer.write(traceFileName);
            writer.newLine();
            writer.write(actScenario.startTime.toString());
            writer.newLine();
            writer.write(actScenario.endTime.toString());
        }catch(Exception ex){
            Logger.getLogger(SimuladorConsole.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
             try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
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
