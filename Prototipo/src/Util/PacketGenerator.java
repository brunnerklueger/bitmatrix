package Util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Igor Siqueira
 */
public class PacketGenerator {

    public static void main(String argv[]) {
        new PacketGenerator().GenerateGraphGnuplot();
    }

    public static void GeneratePacket() {
        try {
            File file = new File("D:\\Mestrado\\Prototipo\\Trace\\Los Angeles\\equinix-sanjose.dirA.20120920-130000.UTC.anon.pcap.csv");
            File newFile = new File("D:\\Mestrado\\SketchMatrix\\trunk\\Tracer_100_Pacotes.csv");

            FileInputStream tracesFIS = new FileInputStream(file);
            DataInputStream tracesDIS = new DataInputStream(tracesFIS);
            BufferedReader tracesBR = new BufferedReader(new InputStreamReader(tracesDIS));

            FileWriter tracesFW = new FileWriter(newFile);

            String tracesStr = tracesBR.readLine();

            Random random = new Random();

            long numberOfPackets = 0;

            while (tracesStr != null && numberOfPackets < 100) {

                String[] packetTokens = tracesStr.split(",");

                long time = Long.parseLong(packetTokens[0]);
                long srcIP = Long.parseLong(packetTokens[1]);
                long dstIP = Long.parseLong(packetTokens[2]);
                int srcPort = Integer.parseInt(packetTokens[3]);
                int dstPort = Integer.parseInt(packetTokens[4]);
                int payload = random.nextInt(256);

                tracesFW.write(time + "," + srcIP + "," + dstIP + "," + srcPort + "," + dstPort + "," + payload + "\n");

                tracesStr = tracesBR.readLine();
                numberOfPackets++;
                if ((numberOfPackets % 100000) == 0) {
                    System.out.println(numberOfPackets / 1000 + "K Pacotes");
                }
            }
            tracesBR.close();
            tracesDIS.close();
            tracesFIS.close();
            tracesFW.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void GenerateTopology() {
        try {
            File file = new File("D:\\Mestrado\\Prototipo\\topologydata\\AS1239\\CONVERTED.POP.1239");
            File newFile = new File("D:\\Mestrado\\SketchMatrix\\trunk\\AS1239_TOPOLOGY");

            FileInputStream topologyFIS = new FileInputStream(file);
            DataInputStream topologyDIS = new DataInputStream(topologyFIS);
            BufferedReader topologyBR = new BufferedReader(new InputStreamReader(topologyDIS));

            String topologyStr = topologyBR.readLine();

            int numberOfEdges = 0;
            Set<Integer> nodes = new HashSet<>();

            while (topologyStr != null) {

                String[] edge = topologyStr.split(" ");

                nodes.add(Integer.parseInt(edge[0]));
                nodes.add(Integer.parseInt(edge[1]));
                numberOfEdges++;

                topologyStr = topologyBR.readLine();

            }
            topologyBR.close();
            topologyDIS.close();
            topologyFIS.close();

            topologyFIS = new FileInputStream(file);
            topologyDIS = new DataInputStream(topologyFIS);
            topologyBR = new BufferedReader(new InputStreamReader(topologyDIS));
            FileWriter topologyFW = new FileWriter(newFile);

            topologyStr = topologyBR.readLine();

            topologyFW.write(nodes.size() + " " + numberOfEdges + "\n");

            while (topologyStr != null) {

                String[] edge = topologyStr.split(" ");

                nodes.add(Integer.parseInt(edge[0]));
                nodes.add(Integer.parseInt(edge[1]));
                nodes.add(Integer.parseInt(edge[1]));

                topologyFW.write(Integer.parseInt(edge[0]) + " " + Integer.parseInt(edge[1]) + " 1\n");

                topologyStr = topologyBR.readLine();

            }

            topologyBR.close();
            topologyDIS.close();
            topologyFIS.close();
            topologyFW.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void GenerateGraph() {
        try {
            for (int j = 6; j <= 6; j++) {
                File real = new File("D:\\Mestrado\\SketchMatrix\\trunk\\Simulations\\Analise\\Scenario1\\Topology" + j + "\\Real.csv");
                for (int k = 1; k <= 4; k++) {
                    File simu = new File("D:\\Mestrado\\SketchMatrix\\trunk\\Simulations\\Analise\\Scenario1\\Topology" + j + "\\SimulacaoInstancia" + k + ".csv");

                    FileInputStream simuFIS = new FileInputStream(simu);
                    DataInputStream simuDIS = new DataInputStream(simuFIS);
                    BufferedReader simuBR = new BufferedReader(new InputStreamReader(simuDIS));

                    FileInputStream realFIS = new FileInputStream(real);
                    DataInputStream realDIS = new DataInputStream(realFIS);
                    BufferedReader realBR = new BufferedReader(new InputStreamReader(realDIS));

                    String lineSimu = simuBR.readLine();
                    String lineReal = realBR.readLine();

                    XYSeries matrix = new XYSeries("Matriz", false, true);
                    while (lineSimu != null && lineReal != null) {

                        lineSimu = lineSimu.replaceAll(",", ".");
                        String[] simuMatriz = lineSimu.split(";");
                        String[] realMatriz = lineReal.split(";");

                        for (int i = 0; i < simuMatriz.length; i++) {
                            try {
                                Integer valorReal = Integer.parseInt(realMatriz[i]);
                                Float valorSimu = Float.parseFloat(simuMatriz[i]);
                                matrix.add(valorReal.doubleValue() / 1000.0, valorSimu.doubleValue() / 1000.0);
                            } catch (NumberFormatException ex) {

                            }
                        }
                        lineSimu = simuBR.readLine();
                        lineReal = realBR.readLine();
                    }

                    simuFIS.close();
                    simuDIS.close();
                    simuBR.close();

                    realFIS.close();
                    realDIS.close();
                    realBR.close();

                    double maxPlot = Double.max(matrix.getMaxX(), matrix.getMaxY()) * 1.1;
                    XYSeries middle = new XYSeries("Referência");;
                    middle.add(0, 0);
                    middle.add(maxPlot, maxPlot);
                    XYSeries max = new XYSeries("Superior 20%");
                    max.add(0, 0);
                    max.add(maxPlot, maxPlot * 1.2);
                    XYSeries min = new XYSeries("Inferior 20%");
                    min.add(0, 0);
                    min.add(maxPlot, maxPlot * 0.8);

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(middle);
                    dataset.addSeries(matrix);
                    dataset.addSeries(max);
                    dataset.addSeries(min);
                    JFreeChart chart;
                    if (k == 4) {
                        chart = ChartFactory.createXYLineChart(
                                "Matriz de Tráfego",
                                "Real",
                                "CMO-MT",
                                dataset
                        );
                    } else {
                        chart = ChartFactory.createXYLineChart(
                                "Matriz de Tráfego",
                                "CMO-MT",
                                "Zhao",
                                dataset
                        );
                    }
                    chart.setBackgroundPaint(Color.WHITE);
                    chart.getPlot().setBackgroundPaint(Color.WHITE);
                    chart.getTitle().setFont(new Font("Times New Roman", Font.BOLD, 13));

                    chart.getLegend().setItemFont(new Font("Times New Roman", Font.TRUETYPE_FONT, 10));

                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Times New Roman", Font.BOLD, 10));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(new Font("Times New Roman", Font.TRUETYPE_FONT, 1));
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Times New Roman", Font.BOLD, 10));
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(new Font("Times New Roman", Font.TRUETYPE_FONT, 1));

                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();

                    renderer.setSeriesLinesVisible(1, false);
                    renderer.setSeriesShapesVisible(1, true);

                    renderer.setSeriesStroke(0, new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{0.1f}, 0.0f));
                    renderer.setSeriesShape(1, new Ellipse2D.Float(-1.5f, -1.5f, 3f, 3f));
                    renderer.setSeriesStroke(2, new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{3.0f}, 0.0f));
                    renderer.setSeriesStroke(3, new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{3.0f}, 0.0f));

                    renderer.setSeriesPaint(0, Color.BLACK);
                    renderer.setSeriesPaint(1, Color.BLACK);
                    renderer.setSeriesPaint(2, Color.BLACK);
                    renderer.setSeriesPaint(3, Color.BLACK);

                    int width = (int) (192 * 1.5f); /* Width of the image */

                    int height = (int) (144 * 1.5f); /* Height of the image */

                    File XYChart = new File("D:\\Mestrado\\SketchMatrix\\trunk\\Simulations\\Analise\\Scenario1\\Topology" + j + "\\SimulacaoInstancia" + k + ".jpeg");
                    ChartUtilities.saveChartAsJPEG(XYChart, chart, width, height);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GenerateGraphGnuplot() {
        try {
            for (int j = 6; j <= 6; j++) {
                File real = new File("D:\\Mestrado\\SketchMatrix\\trunk\\Simulations\\Analise\\Scenario1\\Topology" + j + "\\Real.csv");
                for (int k = 1; k <= 4; k++) {
                    File simu = new File("D:\\Mestrado\\SketchMatrix\\trunk\\Simulations\\Analise\\Scenario1\\Topology" + j + "\\SimulacaoInstancia" + k + ".csv");
                    File dat = new File("D:\\Mestrado\\SketchMatrix\\trunk\\Simulations\\Analise\\Scenario1\\Topology" + j + "\\SimulacaoInstancia" + k + ".txt");

                    FileInputStream simuFIS = new FileInputStream(simu);
                    DataInputStream simuDIS = new DataInputStream(simuFIS);
                    BufferedReader simuBR = new BufferedReader(new InputStreamReader(simuDIS));

                    FileInputStream realFIS = new FileInputStream(real);
                    DataInputStream realDIS = new DataInputStream(realFIS);
                    BufferedReader realBR = new BufferedReader(new InputStreamReader(realDIS));

                    PrintWriter datPW = new PrintWriter(dat);

                    String lineSimu = simuBR.readLine();
                    String lineReal = realBR.readLine();

                    double maxX = 0.0;
                    double maxY = 0.0;

                    HashMap<Double, Double> map = new HashMap<>();

                    while (lineSimu != null && lineReal != null) {

                        lineSimu = lineSimu.replaceAll(",", ".");
                        String[] simuMatriz = lineSimu.split(";");
                        String[] realMatriz = lineReal.split(";");

                        for (int i = 0; i < simuMatriz.length; i++) {
                            try {
                                Integer valorReal = Integer.parseInt(realMatriz[i]);
                                Double valorSimu = Double.parseDouble(simuMatriz[i]);
                                if (map.containsKey(valorReal) && map.containsValue(valorSimu)) {
                                    continue;
                                }
                                map.put(valorReal.doubleValue(), valorSimu);
                                datPW.write(valorReal.doubleValue() + "\t");
                                datPW.write(valorReal.doubleValue() + "\t");
                                datPW.write(valorSimu.doubleValue() + "\t");
                                datPW.write(valorReal.doubleValue() * 1.2 + "\t");
                                datPW.write(valorReal.doubleValue() * 0.8 + "\n");
                                if (valorReal > maxX) {
                                    maxX = valorReal;
                                }
                                if (valorSimu > maxY) {
                                    maxY = valorSimu;
                                }
                            } catch (NumberFormatException ex) {

                            }
                        }
                        lineSimu = simuBR.readLine();
                        lineReal = realBR.readLine();
                    }

                    simuFIS.close();
                    simuDIS.close();
                    simuBR.close();

                    realFIS.close();
                    realDIS.close();
                    realBR.close();

                    datPW.close();

                    Double max = Math.max(maxX, maxY);
                    max *= 1.05;

                    Process p = Runtime.getRuntime().exec("cmd");
                    new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
                    new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
                    PrintWriter stdin = new PrintWriter(p.getOutputStream());
                    stdin.println("gnuplot");
                    stdin.println("cd 'D:\\Mestrado\\SketchMatrix\\trunk\\Simulations\\Analise\\Scenario1\\Topology" + j + "'");
                    stdin.println("set terminal postscript eps enhanced \"Times\" 20");
                    stdin.println("set output \"SimulacaoInstancia" + k + ".eps\"");
                    stdin.println("unset title");
                    stdin.println("unset style line");
                    stdin.println("set style line 1 pt 7 lc 7 lw 1");
                    stdin.println("set style line 2 lt 1 lc 7 lw 1");
                    stdin.println("set style line 3 lt 4 lc 7 lw 1");
                    stdin.println("set style line 4 lt 4 lc 7 lw 1");
                    stdin.println("set style line 5 lt 5 lc 5 lw 3");
                    stdin.println("set style line 6 lt 6 lc 6 lw 3");
                    stdin.println("set style line 7 pt 7 lc 7 lw 3");
                    if (k == 4) {
                        stdin.println("set ylabel \"CMO-MT\"");
                        stdin.println("set xlabel \"Real\"");
                    } else {
                        stdin.println("set ylabel \"Zhao\"");
                        stdin.println("set xlabel \"CMO-MT\"");
                    }
                    stdin.println("set key top left");
                    stdin.println("set xrange [0:" + max.intValue() + "]");
                    stdin.println("set yrange [0:" + max.intValue() + "]");
                    stdin.println("set grid ytics lc rgb \"#bbbbbb\" lw 1 lt 0");
                    stdin.println("set grid xtics lc rgb \"#bbbbbb\" lw 1 lt 0");
                    stdin.println("plot "
                                + "x title \"Referencia\"      ls 2,"
                                + "\"SimulacaoInstancia" + k + ".txt\" using 1:3 title \"Matriz\"          ls 7,"
                                + "1.2*x title \"Superior 20%\"    ls 4,"
                                + "0.8*x title \"Inferior 20%\"    ls 4"
                    );
                    
                    stdin.println("exit");
                    stdin.println("exit");
                    // write any other commands you want here
                    stdin.close();
                    int returnCode = p.waitFor();
                    System.out.println("Return code = " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
