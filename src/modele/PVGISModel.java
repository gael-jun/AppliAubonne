package modele;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class PVGISModel {
    public enum Status { IDLE, LOADING, READY, ERROR }

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private PVGISRequest request;
    private PVGISResult result;
    private FinancialParams financialParams;
    private FinancialResult financialResult;
    private Status status = Status.IDLE;

    public void addPropertyChangeListener(PropertyChangeListener l) { pcs.addPropertyChangeListener(l); }
    public void removePropertyChangeListener(PropertyChangeListener l) { pcs.removePropertyChangeListener(l); }

    public PVGISRequest getRequest() { return request; }
    public void setRequest(PVGISRequest newReq) {
        PVGISRequest old = this.request;
        this.request = newReq;
        pcs.firePropertyChange("request", old, newReq);
    }

    public PVGISResult getResult() { return result; }
    public void setResult(PVGISResult newRes) {
        PVGISResult old = this.result;
        this.result = newRes;
        pcs.firePropertyChange("result", old, newRes);
    }

    public FinancialParams getFinancialParams() { return financialParams; }
    public void setFinancialParams(FinancialParams newParams) {
        FinancialParams old = this.financialParams;
        this.financialParams = newParams;
        pcs.firePropertyChange("financialParams", old, newParams);
    }

    public FinancialResult getFinancialResult() { return financialResult; }
    public void setFinancialResult(FinancialResult newRes) {
        FinancialResult old = this.financialResult;
        this.financialResult = newRes;
        pcs.firePropertyChange("financialResult", old, newRes);
    }

    public Status getStatus() { return status; }
    public void setStatus(Status newStatus) {
        Status old = this.status;
        this.status = newStatus;
        pcs.firePropertyChange("status", old, newStatus);
    }
}
