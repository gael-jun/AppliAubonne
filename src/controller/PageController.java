package controller;

import export.*;
import modele.*;
import org.knowm.xchart.CategoryChart;
import service.FinancialCalculator;
import service.PVGISService;
import util.ChartFactory;
import modele.MonthlyResult;
import modele.HistogramBucket;
import java.io.File;
import java.util.*;

/**
 * Mediator/Presenter for the Off-Grid page. Testable without Swing rendering.
 */
public final class PageController {
    private final PVGISModel model;
    private final PVGISService service;
    private final FinancialCalculator financialCalculator;

    public PageController(PVGISModel model) {
        this(model, new PVGISService(), new FinancialCalculator());
    }

    public PageController(PVGISModel model, PVGISService service, FinancialCalculator financialCalculator) {
        this.model = model;
        this.service = service;
        this.financialCalculator = financialCalculator;
    }

    public void estimateAsync(PVGISRequest req) {
        model.setRequest(req);
        model.setStatus(PVGISModel.Status.LOADING);
        new Thread(() -> {
            try {
                PVGISResult res = service.fetch(req);
                model.setResult(res);
                model.setStatus(PVGISModel.Status.READY);
            } catch (java.io.IOException | InterruptedException ex) {
                model.setStatus(PVGISModel.Status.ERROR);
                Thread.currentThread().interrupt();
            }
        }, "PageController-Estimate").start();
    }

    public List<CategoryChart> buildPVCharts(PVGISResult result) {
        String[] moisFrancais = {"Jan","Fév","Mars","Avril","Mai","Juin","Juil","Août","Sep","Oct","Nov","Déc"};
        List<String> mois = new ArrayList<>();
        List<Double> jours = Arrays.asList(31.,28.,31.,30.,31.,30.,31.,31.,30.,31.,30.,31.);
        List<Double> prod = new ArrayList<>();
        for (MonthlyResult m : result.monthly) {
            int idx = m.month - 1;
            mois.add(idx >= 0 && idx < 12 ? moisFrancais[idx] : ("Mois " + m.month));
            prod.add(m.E_d * jours.get(idx));
        }
        List<CategoryChart> charts = new ArrayList<>();
        charts.add(ChartFactory.createProductionChart(mois, prod));
        List<Double> lost = new ArrayList<>();
        for (MonthlyResult m : result.monthly) { lost.add(m.E_lost_d * jours.get(m.month-1)); }
        charts.add(ChartFactory.createLostEnergyChart(mois, lost));
        List<String> csLabels = new ArrayList<>();
        List<Double> fcs = new ArrayList<>();
        for (HistogramBucket h : result.histogram) { csLabels.add(h.CS_min+"-"+h.CS_max); fcs.add(h.f_CS); }
        charts.add(ChartFactory.createHistogramChart(csLabels, fcs));
        List<Double> ff = new ArrayList<>(); List<Double> fe = new ArrayList<>();
        for (MonthlyResult m : result.monthly) { ff.add(m.f_f); fe.add(m.f_e); }
        charts.add(ChartFactory.createBatteryStatusChart(mois, ff, fe));
        return charts;
    }

    public FinancialResult computeFinancial(PVGISResult res, FinancialParams params) {
        FinancialResult fr = financialCalculator.compute(res, params);
        model.setFinancialParams(params);
        model.setFinancialResult(fr);
        return fr;
    }

    public void exportCsv(File file, ExportContext ctx) throws java.io.IOException {
        new ExportFacade().export(new CsvExportStrategy(), file, ctx);
    }

    public void exportPdf(File file, ExportContext ctx) throws java.io.IOException {
        new ExportFacade().export(new PdfExportStrategy(), file, ctx);
    }
}
