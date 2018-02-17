/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulador;

import Network.Topology;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class Scenario {
    public Long startTime;
    public Long endTime;
    public int idScenario;
    public String traceFile;
    public List<Topology> lstTopology = new ArrayList<>();  
    public List<Integer> lstSizes = new ArrayList<>();
    public List<Integer> lstThresholds = new ArrayList<>();
}
