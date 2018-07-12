package Report;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author brunn
 */
public class Report implements Serializable{

    private String maxTop;
    private LinkedList<SubReport> lstAnalysisData;
    
    public Report(){
    }

    public Report(String maxTop, LinkedList<SubReport> lstAnalysisData) {
        this.maxTop = maxTop;
        this.lstAnalysisData = lstAnalysisData;
    }

    public String getMaxTop() {
        return maxTop;
    }

    public LinkedList<SubReport> getLstAnalysisData() {
        return lstAnalysisData;
    }
    
    
    
}
