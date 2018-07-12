/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Network.DataPacket;
import Network.Hash;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer2;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author brunn
 */
public class HashCheck {
    
    public static void main(String argv[]) {
        try {
            HashCheck.check();
        } catch (IOException ex) {
            Logger.getLogger(HashCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static  void check() throws FileNotFoundException, IOException{
         
        Map<Integer,Integer> mapHashCount = new TreeMap<>();
        Hash hash = new Hash();
        /* Tamanhos de bitmaps (não primos)*/
        //int bitMapSize=64;
        int bitMapSize=512;
        //int bitMapSize=16384;
        //int bitMapSize=65536;
        /* Tamanhos de bitmaps (primos)*/
        //int bitMapSize=67;
        //int bitMapSize=521;
        //int bitMapSize=16381;
        //int bitMapSize=65537;
        
        
        int numberOfBits = Integer.highestOneBit(bitMapSize); // Number of bits em binário -> 10000000000
        int mask = numberOfBits - 1; 
        Integer largerOcurrence=0;
        String strTrace="Tracer_100k_Pacotes.csv";
        String traceFile = "D:\\Mestrado\\Projetos\\fossibitmatrix.git\\" + 
                strTrace;
        
        String delim = "[,]";

        FileInputStream tracesFIS;
        DataInputStream tracesDIS;
        BufferedReader tracesBR;
        String tracesStr;
        
        for(int i = 0; i < bitMapSize; i++){
            mapHashCount.put(i, 0);
        }

        File diretorio = new File(traceFile);
        File files[] = diretorio.listFiles();
        if (files == null) {
            files = new File[1];
            files[0] = diretorio;
        } else {
            Arrays.sort(files);
        }

        for (File file : files) {

            if (file.isFile()) {

                tracesFIS = new FileInputStream(file);
                tracesDIS = new DataInputStream(tracesFIS);
                tracesBR = new BufferedReader(new InputStreamReader(tracesDIS));

                tracesStr = tracesBR.readLine();

                long cnt = 0;
                while (tracesStr != null) {
                    if (cnt % 1000 == 0) {
                        System.out.println(cnt/1000 + "K Pacotes");
                    }

                    String[] packetTokens = tracesStr.split(delim);

                    long time = parseLong(packetTokens[0]);
                    long srcIP = parseLong(packetTokens[1]);
                    long dstIP = parseLong(packetTokens[2]);
                    int srcPort = Integer.parseInt(packetTokens[3]);
                    int dstPort = Integer.parseInt(packetTokens[4]);
                    int payload = Integer.parseInt(packetTokens[5]);

                    DataPacket pkt = new DataPacket(time, srcIP, dstIP, srcPort, dstPort, payload);

                    largerOcurrence = receivePacket(pkt,hash,mask,bitMapSize, mapHashCount,largerOcurrence);

                    tracesStr = tracesBR.readLine();
                    cnt++;
                }
                tracesBR.close();
                tracesDIS.close();
                tracesFIS.close();
            }
        }
        System.out.println("----------HISTOGRAMA----------");
        //generateHistogramChart(mapHashCount);
//        //Quantidade de posições x ocorrencia
        plotSeries(mapHashCount, bitMapSize,bitMapSize+ " bits","Posição (X)", "Ocorrência (Y)");
        
        System.out.println("----------Grafico 2 (Ocorrência x Quantidade de repeticao dessa ocorrencia----------");
        Map<Integer,Integer> mapSeriesB = genereteContentSeriesB(mapHashCount, largerOcurrence);
        plotSeries(mapSeriesB, bitMapSize,"B","Ocorrência (X)", "Repetições (Y)");
        
        System.out.println("----------Grafico 3 (Ocorrência x Cumulativo de Quantidade de repeticao dessa ocorrencia----------");
        Map<Integer,Integer> mapSeriesC = genereteContentSeriesC(mapSeriesB);
        plotSeries(mapSeriesC, bitMapSize,"C","Ocorrência (X)", "Cumulativo (Y)");
    }
    
    public static void plotSeries(Map<Integer,Integer> mapHashCount, 
            Integer bitMapSize, String chartName,String xAxisName, String yAxisName){
        String graphFileName;
        
        //Quantidade de posições x ocorrencia
        XYSeries seriesA = new XYSeries("");
        mapHashCount.entrySet().stream().forEach((map) -> {
            Integer hashIntex = map.getKey();
            Integer count = map.getValue();
            
            seriesA.add(hashIntex, new Double(count));
            System.out.println(hashIntex + ": " + count);
        });
        graphFileName = chartName + "_" + bitMapSize;
        generateChart(seriesA, graphFileName,chartName,xAxisName, yAxisName); 
    }
    
    public static Integer receivePacket(DataPacket pkt, Hash hashFunction, int mask,
        int bitMapSize, Map<Integer,Integer> mapHashCount, Integer largerOcurrence) {
  
        int hashValue = hashFunction.md5(pkt);

        int bitMapIndex = hashValue % bitMapSize;
        
        //Quantidade de posições x ocorrencia
        if(mapHashCount.containsKey(bitMapIndex)){
            
            int tmpOcurrence = mapHashCount.get(bitMapIndex)+1;
            mapHashCount.put(bitMapIndex,tmpOcurrence);
            
            if(largerOcurrence < tmpOcurrence){
                largerOcurrence = tmpOcurrence;
            }
        }else{
            System.err.println("Index: " + bitMapIndex + " fora do grafico.");
        }
        return largerOcurrence;
    }
    
    public static Long parseLong(String texto) {
        if (texto.startsWith("0x")) {
            return Long.parseLong(texto.substring(2), 16);
        } else {
            return Long.parseLong(texto);
        }
    }
    
    public static void generateChart(XYSeries series, String fileName,
            String chartName,String xAxisName, String yAxisName){
        
        final XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = createChart(dataset,chartName,xAxisName, yAxisName);
        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */ 
        File file = new File(fileName + ".jpeg"); 
        try {
            ChartUtilities.saveChartAsJPEG( file , chart , width , height );
        } catch (IOException ex) {
            Logger.getLogger(HashCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static JFreeChart createChart(final XYDataset dataset, 
            String chartName,String xAxisName, String yAxisName) {
            
        final JFreeChart chart = ChartFactory.createXYAreaChart(
            chartName,
            xAxisName,yAxisName,
            dataset,
            PlotOrientation.VERTICAL,
            false,  // legend
            true,  // tool tips
            false  // URLs
        );
        
        chart.setBackgroundPaint(Color.white);
        
        final XYPlot plot = chart.getXYPlot();
        //plot.setOutlinePaint(Color.black);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setForegroundAlpha(0.65f);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        plot.setRenderer(new XYAreaRenderer2());
        final ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickMarkPaint(Color.black);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        
        final ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setTickMarkPaint(Color.black);
        
        return chart;
        
    }
    
    public static Map<Integer,Integer> genereteContentSeriesB(Map<Integer,Integer> mapHashCount,
            Integer largerOcurrence){
        Map<Integer,Integer> mapOccurrenceCount = new TreeMap<>();
        
        for(int i=0;i< largerOcurrence+1;i++){
            mapOccurrenceCount.put(i,0);
        }
        
        mapHashCount.entrySet().stream().forEach((map) -> {
            Integer hashIntex = map.getKey();
            Integer count = map.getValue();
            
            if(mapOccurrenceCount.containsKey(count)){
                mapOccurrenceCount.put(count, mapOccurrenceCount.get(count)+1);
            }else{
                System.err.println("Index: " + count + " fora do grafico.");
            }
            
        });
        return mapOccurrenceCount;
    }
    
    public static Map<Integer,Integer> genereteContentSeriesC(Map<Integer,Integer> mapOccurrenceCount){
        Map<Integer,Integer> mapSum = new TreeMap<>();
        
        for(int i=0;i< mapOccurrenceCount.size();i++){
            mapSum.put(i,0);
        }
        
        mapOccurrenceCount.entrySet().stream().forEach((map) -> {
            Integer hashIntex = map.getKey();
            Integer count = map.getValue();
            
            if(mapSum.containsKey(hashIntex)){
                if(hashIntex==0){
                    mapSum.put(hashIntex, count);
                }else{
                    mapSum.put(hashIntex, mapSum.get(hashIntex-1)+ count);
                }
            }else{
                System.err.println("Index: " + count + " fora do grafico.");
            }
            
        });
        return mapSum;
    }
    
    
    private static void generateHistogramChart(Map<Integer,Integer> mapHashCount){
        HistogramDataset dataset = new HistogramDataset();
        double[] data = new double[mapHashCount.size()];
        int i=0;
        for (Map.Entry<Integer, Integer> entry : mapHashCount.entrySet()) {
            Integer key = entry.getKey();
            data[i] = entry.getValue();
            i++;
        }
        
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        
        dataset.addSeries("Hist",data,64); // Number of bins is 50
        String plotTitle = "";
        String xAxis = "Frequency";
        String yAxis = "Mass Error (Da)";
        PlotOrientation orientation = PlotOrientation.VERTICAL;

        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createHistogram(plotTitle, xAxis, yAxis,
                dataset, orientation, show, toolTips, urls);

        chart.setBackgroundPaint(Color.white);
        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */ 
        File file = new File("histograma" + ".jpeg"); 
        try {
            ChartUtilities.saveChartAsJPEG( file , chart , width , height );
        } catch (IOException ex) {
            Logger.getLogger(HashCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
}
