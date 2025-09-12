package modele.pvgis;

import modele.finance.FinancialParams;
import modele.finance.FinancialResult;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Modèle observable (MVC) pour l'estimation PVGIS.
 * Notifie la vue via PropertyChangeSupport lors des mises à jour de requête, résultat, finances ou statut.
 */
public final class PVGISModel {
    public enum Status { IDLE, LOADING, READY, ERROR }

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private PVOffGridRequest request;
    private PVGISResult result;
    private FinancialParams financialParams;
    private FinancialResult financialResult;
    private Status status = Status.IDLE;

    public void addPropertyChangeListener(PropertyChangeListener l) { pcs.addPropertyChangeListener(l); }
    public void removePropertyChangeListener(PropertyChangeListener l) { pcs.removePropertyChangeListener(l); }

    public PVOffGridRequest getRequest() { return request; }
    public void setRequest(PVOffGridRequest newReq) { var old = this.request; this.request = newReq; pcs.firePropertyChange("request", old, newReq); }

    public PVGISResult getResult() { return result; }
    public void setResult(PVGISResult newRes) { var old = this.result; this.result = newRes; pcs.firePropertyChange("result", old, newRes); }

    public FinancialParams getFinancialParams() { return financialParams; }
    public void setFinancialParams(FinancialParams newParams) { var old = this.financialParams; this.financialParams = newParams; pcs.firePropertyChange("financialParams", old, newParams); }

    public FinancialResult getFinancialResult() { return financialResult; }
    public void setFinancialResult(FinancialResult newRes) { var old = this.financialResult; this.financialResult = newRes; pcs.firePropertyChange("financialResult", old, newRes); }

    public Status getStatus() { return status; }
    public void setStatus(Status newStatus) { var old = this.status; this.status = newStatus; pcs.firePropertyChange("status", old, newStatus); }
}
