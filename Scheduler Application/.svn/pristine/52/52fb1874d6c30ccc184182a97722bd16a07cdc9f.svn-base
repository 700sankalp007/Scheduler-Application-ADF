package sc.common.view.bean;

import java.util.ArrayList;

public class CycleCountSeqDetailMasterBean {
    
    CycleCountSeqDetailBean cycleCountBean = null;
    ArrayList<CycleCountSeqDetailBean> cycleCountList = null;
    
    public void setCycleCountBean(CycleCountSeqDetailBean cycleCountBean) {
        if(cycleCountList==null){
            cycleCountList = new ArrayList<CycleCountSeqDetailBean>();
        }
        cycleCountList.add(cycleCountBean);
        this.cycleCountBean = cycleCountBean;
    }

    public CycleCountSeqDetailBean getCycleCountBean() {
        return cycleCountBean;
    }

    public void setCycleCountList(ArrayList<CycleCountSeqDetailBean> cycleCountList) {
        this.cycleCountList = cycleCountList;
    }

    public ArrayList<CycleCountSeqDetailBean> getCycleCountList() {
        return cycleCountList;
    }
    
}
