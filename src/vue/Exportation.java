package vue;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import modele.cablesetprotections.CalculCablesEtProtections;
import vue.cablesetprotections.Tables;
import vue.cablesetprotections.ZonesDeTexte;

import javax.swing.*;
import javax.swing.table.TableModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * La classe Exportation est responsable de l'exportation des données
 * vers un fichier Excel (.xlsx). Les données sont exportées de plusieurs pages de l'application
 * vers différentes feuilles de calcul dans le fichier Excel.
 */
public class Exportation {

	private static int TAILLE_1E_COL = 40;
	private static CellStyle borderStyle;
	private static CellStyle headerStyle;
	private static Font headerFont;
	
    /**
     * Constructeur par défaut pour la classe Exportation.
     * Initialise une nouvelle instance de Exportation.
     */
    public Exportation() {
        // Aucun code particulier dans le constructeur
    }
	
	/**
     * Lance le processus d'exportation, en demandant à l'utilisateur de choisir l'emplacement
     * de sauvegarde du fichier Excel. Si l'utilisateur approuve la sélection, les données
     * des tables et zones de texte sont exportées vers le fichier Excel.
     */
    public static void exporter() {
	    
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionnez l'emplacement du fichier à exporter");
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (Workbook workbook = new XSSFWorkbook()) {
            	
        	    borderStyle = workbook.createCellStyle();
        	    borderStyle.setBorderTop(BorderStyle.THIN);
        	    borderStyle.setBorderBottom(BorderStyle.THIN);
        	    borderStyle.setBorderLeft(BorderStyle.THIN);
        	    borderStyle.setBorderRight(BorderStyle.THIN);
        	
        	    headerStyle = workbook.createCellStyle();
        	    headerFont = workbook.createFont();
        	    headerFont.setBold(true);
        	    headerStyle.setFont(headerFont);
        	    headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        	    headerStyle.setBorderTop(BorderStyle.THIN);
        	    headerStyle.setBorderBottom(BorderStyle.THIN);
        	    headerStyle.setBorderLeft(BorderStyle.THIN);
        	    headerStyle.setBorderRight(BorderStyle.THIN);
        	    
                Sheet sheetSurface = workbook.createSheet("Surface");
                sheetSurface.setColumnWidth(0, TAILLE_1E_COL * 256);
                exportTableToSheet(PageSurface.getTableSurface(), sheetSurface);
                exportTableToSheet(PageSurface.getTablePortrait(), sheetSurface);
                exportTableToSheet(PageSurface.getTablePaysage(), sheetSurface);
                exportTableToSheet(PageSurface.getTableSurfaceFin(), sheetSurface);
                
                Sheet sheetPuissance = workbook.createSheet("Puissance");
                sheetPuissance.setColumnWidth(0, TAILLE_1E_COL * 256);
                exportTableToSheet(PagePuissance.getTablePuissance1(), sheetPuissance);
                exportTableToSheet(PagePuissance.getTablePuissance2(), sheetPuissance);
                exportTableToSheet(PagePuissance.getTablePuissance3(), sheetPuissance);
                
                Sheet sheetOnduleurs = workbook.createSheet("Onduleurs");
                sheetOnduleurs.setColumnWidth(0, TAILLE_1E_COL * 256);
                exportTableToSheet(PageOnduleur.getTableOnduleur(), sheetOnduleurs);
                exportTablesInLine(PageOnduleur.getTablesAdditionnelles(), sheetOnduleurs);
                exportTablesInLine(PageOnduleur.getTablesBilan(), sheetOnduleurs);
                exportTableToSheet(PageOnduleur.getTableComparatif(), sheetOnduleurs);
                
                Sheet sheetCablesEtProtections = workbook.createSheet("Câbles_Protections");
                sheetCablesEtProtections.setColumnWidth(0, TAILLE_1E_COL * 256);
                exportValueToSheet("DISPOSITIFS DE PROTECTION ÉLECTRIQUE", sheetCablesEtProtections);
                exportValueToSheet("1. Choix des dispositifs de protection partie DC", sheetCablesEtProtections);
                exportValueToSheet("Choix de fusible : Protection des modules PV contre les surintensité", sheetCablesEtProtections);
                String[] valuesIrm = new String[] {"Irm (A)", ZonesDeTexte.getIrm()};
                exportValuesToSheet(valuesIrm, sheetCablesEtProtections);
                String[] valuesRappelUocTmin = new String[] {"Rappel de Uoc Tmin (V)", ZonesDeTexte.getRappelUocTmin()};
                exportValuesToSheet(valuesRappelUocTmin, sheetCablesEtProtections);
                String[] valuesRappelIscSTC = new String[] {"Rappel de Isc STC (A)", ZonesDeTexte.getRappelIscSTC()};
                exportValuesToSheet(valuesRappelIscSTC, sheetCablesEtProtections);
                String[] valuesRappelUmppSTC = new String[] {"Rappel de Umpp STC (V)", ZonesDeTexte.getRappelUmppSTC()};
                exportValuesToSheet(valuesRappelUmppSTC, sheetCablesEtProtections);
                String[] valuesRappelImppSTC = new String[] {"Rappel de Impp STC (A)", ZonesDeTexte.getRappelImppSTC()};
                exportValuesToSheet(valuesRappelImppSTC, sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableChoixDeFusible(), sheetCablesEtProtections);
                String[] valuesChoixCalibre = new String[] {"Choix final du calibre du fusible (A)", CalculCablesEtProtections.getFusibleCourant()};
                exportValuesToSheet(valuesChoixCalibre, sheetCablesEtProtections);
                exportValueToSheet("Choix des parafoudres DC", sheetCablesEtProtections);
                String[] valuesNkDC = new String[] {"Nk", ZonesDeTexte.getNkDC()};
                exportValuesToSheet(valuesNkDC, sheetCablesEtProtections);
                String[] valuesNgDC = new String[] {"Ng", ZonesDeTexte.getNgDC()};
                exportValuesToSheet(valuesNgDC, sheetCablesEtProtections);
                String[] valuesConstantePourLcrit = new String[] {"Constante pour Lcrit", ZonesDeTexte.getConstantePourLcrit()};
                exportValuesToSheet(valuesConstantePourLcrit, sheetCablesEtProtections);
                String[] valuesLcrit = new String[] {"Lcrit (m)", ZonesDeTexte.getLcrit()};
                exportValuesToSheet(valuesLcrit, sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableDesDifferentsCables(), sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableL(), sheetCablesEtProtections);
                String[] valuesLtotale = new String[] {"Ltotale (m)", ZonesDeTexte.getLtotale()};
                exportValuesToSheet(valuesLtotale, sheetCablesEtProtections);
                String[] valuesProtectionParafoudreObligatoireDC = new String[] {"Protection parafoudre obligatoire côté DC ?", ZonesDeTexte.getProtectionParafoudreObligatoireDC()};
                exportValuesToSheet(valuesProtectionParafoudreObligatoireDC, sheetCablesEtProtections);
                exportValueToSheet("Choix et mise en oeuvre des parafoudres côté DC", sheetCablesEtProtections);
                String[] valuesUw = new String[] {"Uw (V)", ZonesDeTexte.getUw()};
                exportValuesToSheet(valuesUw, sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableParafoudreDC(), sheetCablesEtProtections);
                String[] valuesSecondParafoudreDC = new String[] {"Besoin d'un second parafoudre ?", ZonesDeTexte.getSecondParafoudreDC()};
                exportValuesToSheet(valuesSecondParafoudreDC, sheetCablesEtProtections);
                exportValueToSheet("Choix de l'interrupteur sectionneur DC", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableInterrupteursSectionneurs(), sheetCablesEtProtections);
                exportValueToSheet("2. Choix des dispositifs de protection partie AC", sheetCablesEtProtections);
                exportValueToSheet("Choix des disjoncteurs AC associés à chaque onduleur", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableDisjoncteursACAssociesAuxOnduleurs(), sheetCablesEtProtections);
                exportValueToSheet("Choix du disjoncteur général (AGCP)", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableDisjoncteurACGeneral(), sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportValueToSheet("Choix du parafoudre AC", sheetCablesEtProtections);
                String[] valuesNkAC = new String[] {"Nk", ZonesDeTexte.getNkAC()};
                exportValuesToSheet(valuesNkAC, sheetCablesEtProtections);
                String[] valuesNgAC = new String[] {"Ng", ZonesDeTexte.getNgAC()};
                exportValuesToSheet(valuesNgAC, sheetCablesEtProtections);
                String[] valuesProtectionParafoudreObligatoireAC = new String[] {"Protection parafoudre obligatoire ?", ZonesDeTexte.getProtectionParafoudreObligatoireAC()};
                exportValuesToSheet(valuesProtectionParafoudreObligatoireAC, sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableParafoudreAC(), sheetCablesEtProtections);
                exportValueToSheet("Disjoncteur associé au parafoudre", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableDisjoncteurAssocieAuParafoudreAC(), sheetCablesEtProtections);
                exportValueToSheet("SECTION DES CABLES ET CHUTE DES TENSIONS", sheetCablesEtProtections);
                exportValueToSheet("1. Choix des câbles de chaîne et vérification de la chute de tension", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableNcNpIz(), sheetCablesEtProtections);
                String[] valuesLettreSelectionCablesChaine = new String[] {"Lettre de sélection : ", ZonesDeTexte.getLettreSelectionCablesChaine()};
                exportValuesToSheet(valuesLettreSelectionCablesChaine, sheetCablesEtProtections);
                String[] valuesK1CablesChaine = new String[] {"K1", ZonesDeTexte.getK1CablesChaine()};
                exportValuesToSheet(valuesK1CablesChaine, sheetCablesEtProtections);
                String[] valuesK2CablesChaine = new String[] {"K2", ZonesDeTexte.getK2CablesChaine()};
                exportValuesToSheet(valuesK2CablesChaine, sheetCablesEtProtections);
                String[] valuesK3CablesChaine = new String[] {"K3", ZonesDeTexte.getK3CablesChaine()};
                exportValuesToSheet(valuesK3CablesChaine, sheetCablesEtProtections);
                String[] valuesKCablesChaine = new String[] {"K", ZonesDeTexte.getKCablesChaine()};
                exportValuesToSheet(valuesKCablesChaine, sheetCablesEtProtections);
                exportValueToSheet("Choix des câbles de chaîne (2.5, 4, 6mm² ?) : ", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableChoixCablesDeChaine(), sheetCablesEtProtections);
                String[] valuesChoixSectionCablesDeChaine = new String[] {"Choix final de la section pour les câbles de chaîne", String.valueOf(CalculCablesEtProtections.getChoixSectionCablesDeChaine())};
                exportValuesToSheet(valuesChoixSectionCablesDeChaine, sheetCablesEtProtections);
                exportValueToSheet("Calcul de la chute de tension (chaîne) : ", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableChuteDeTension1(), sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableChuteDeTension2(), sheetCablesEtProtections);
                exportValueToSheet("2. Choix du câble principal et vérification de la chute de tension", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableCablePrincipal1(), sheetCablesEtProtections);
                String[] valuesLettreSelectionCablePrincipal = new String[] {"Lettre de sélection : ", ZonesDeTexte.getLettreSelectionCablePrincipal()};
                exportValuesToSheet(valuesLettreSelectionCablePrincipal, sheetCablesEtProtections);
                String[] valuesK1CablePrincipal = new String[] {"K1", ZonesDeTexte.getK1CablePrincipal()};
                exportValuesToSheet(valuesK1CablePrincipal, sheetCablesEtProtections);
                String[] valuesK2CablePrincipal = new String[] {"K2", ZonesDeTexte.getK2CablePrincipal()};
                exportValuesToSheet(valuesK2CablePrincipal, sheetCablesEtProtections);
                String[] valuesK3CablePrincipal = new String[] {"K3", ZonesDeTexte.getK3CablePrincipal()};
                exportValuesToSheet(valuesK3CablePrincipal, sheetCablesEtProtections);
                String[] valuesKCablePrincipal = new String[] {"K", ZonesDeTexte.getKCablePrincipal()};
                exportValuesToSheet(valuesKCablePrincipal, sheetCablesEtProtections);
                exportValueToSheet("Choix du câble principal (2.5, 4, 6mm² ?) : ", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableCablePrincipal2(), sheetCablesEtProtections);
                String[] valuesChoixSectionCablePrincipal = new String[] {"Choix final de la section pour le câble principal", String.valueOf(CalculCablesEtProtections.getChoixSectionCablePrincipal())};
                exportValuesToSheet(valuesChoixSectionCablePrincipal, sheetCablesEtProtections);
                exportValueToSheet("Calcul de la chute de tension maximum à respecter : ", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableChuteDeTensionMaximumARespecter(), sheetCablesEtProtections);
                exportValueToSheet("Chute de tension totale DC maximum à respecter : ", sheetCablesEtProtections);
                exportValueToSheet("Règle : 3% maxi selon la norme, mais il est préférable de choisir 1%.", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableChuteDeTensionTotaleDCMaximumARespecter(), sheetCablesEtProtections);
                exportValueToSheet("3. Section des câbles et chute de tension côté AC", sheetCablesEtProtections);
                exportValueToSheet("Calcul de la chute de tension entre un onduleur et le disjoncteur différentiel en sortie d'onduleur", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableChuteDeTensionEntreOnduleurEtDisjoncteurDiff(), sheetCablesEtProtections);
                exportValueToSheet("Calcul de la chute de tension maximum à respecter : ", sheetCablesEtProtections);
                exportValueToSheet("", sheetCablesEtProtections);
                exportTableToSheet(Tables.getTableChuteDeTensionEntreDisjoncteurDiffEtAGCP(), sheetCablesEtProtections);
                exportValueToSheet("Chute de tension totale AC : ", sheetCablesEtProtections);
                exportValueToSheet("On retient le cumul le plus important de chute de tension entre onduleur et l’AGCP.", sheetCablesEtProtections);
                String[] valuesChuteTensionAC = new String[] {"ΔU/U totale (%)", ZonesDeTexte.getChuteTensionAC()};
                exportValuesToSheet(valuesChuteTensionAC, sheetCablesEtProtections);
                
                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                    JOptionPane.showMessageDialog(null, "Fichier exporté avec succès à " + filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de l'exportation du fichier Excel.");
            }
        }
    }
    
    /**
     * Exporte les données de la table spécifiée vers la feuille Excel.
     *
     * @param table La table contenant les données à exporter.
     * @param sheet La feuille Excel où les données doivent être exportées.
     */
    private static void exportTableToSheet(JTable table, Sheet sheet) {
        if (table == null || sheet == null) {
            return;
        }
        
        TableModel model = table.getModel();
        int rowNum = sheet.getLastRowNum() + 1;

        Row headerRow = sheet.createRow(rowNum++);
        for (int j = 0; j < model.getColumnCount(); j++) {
            Cell cell = headerRow.createCell(j);
            cell.setCellValue(model.getColumnName(j));
            cell.setCellStyle(headerStyle);
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            Row row = sheet.createRow(rowNum++);
            for (int j = 0; j < model.getColumnCount(); j++) {
                Cell cell = row.createCell(j);
                Object value = model.getValueAt(i, j);
                if (value instanceof Number) {
                    if (value instanceof Integer) {
                        cell.setCellValue(((Integer) value).doubleValue());
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    }
                } else {
                    cell.setCellValue(value != null ? value.toString() : "");
                }
                cell.setCellStyle(borderStyle);
            }
        }
        sheet.createRow(rowNum);

        for (int i = 0; i < model.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

	/**
	 * Exporte une liste de tables dans la même ligne de la feuille Excel.
	 * Chaque table est exportée dans des colonnes consécutives.
	 *
	 * @param tables La liste des tables à exporter.
	 * @param sheet La feuille Excel où les données doivent être exportées.
	 * @param workbook Le workbook pour créer les stylsssses.
	 */
	private static void exportTablesInLine(List<JTable> tables, Sheet sheet) {
	    if (tables == null || tables.isEmpty()) {
	        return;
	    }
	
	    int rowNum = sheet.getLastRowNum() + 1; 
	    int currentCol = 0;
	
	    for (JTable table : tables) {
	        if (table != null) {
	            TableModel model = table.getModel();
	
	            Row headerRow = sheet.getRow(rowNum);
	            if (headerRow == null) {
	                headerRow = sheet.createRow(rowNum);
	            }
	            for (int j = 0; j < model.getColumnCount(); j++) {
	                Cell cell = headerRow.createCell(currentCol + j);
	                cell.setCellValue(model.getColumnName(j));
	                cell.setCellStyle(headerStyle);
	            }
	
	            for (int i = 0; i < model.getRowCount(); i++) {
	                Row row = sheet.getRow(rowNum + 1 + i);
	                if (row == null) {
	                    row = sheet.createRow(rowNum + 1 + i);
	                }
	                for (int j = 0; j < model.getColumnCount(); j++) {
	                    Cell cell = row.createCell(currentCol + j);
	                    Object value = model.getValueAt(i, j);
	                    if (value instanceof Number) {
	                        if (value instanceof Integer) {
	                            cell.setCellValue(((Integer) value).doubleValue());
	                        } else if (value instanceof Double) {
	                            cell.setCellValue((Double) value);
	                        }
	                    } else {
	                        cell.setCellValue(value != null ? value.toString() : "");
	                    }
	                    cell.setCellStyle(borderStyle);
	                }
	            }
	
	            currentCol += model.getColumnCount();
	        }
	    }
	
	    sheet.createRow(rowNum + 1);
	}

    /**
     * Exporte une valeur sous forme de texte dans la feuille Excel spécifiée.
     * Le texte est placé dans une nouvelle ligne.
     *
     * @param value La valeur texte à exporter.
     * @param sheet La feuille Excel où la valeur doit être exportée.
     */
    private static void exportValueToSheet(String value, Sheet sheet) {
        if (value == null || sheet == null) {
            return;
        }
        
        if (value.equals("-1.0")) {
            return;
        }
        
        int rowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(0);

        try {
            cell.setCellValue(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            cell.setCellValue(value);
        }
    }
    
    /**
     * Exporte un tableau de valeurs sous forme de texte dans la feuille Excel spécifiée.
     * Chaque valeur est placée dans une cellule distincte de la même ligne.
     *
     * @param values Le tableau de valeurs à exporter.
     * @param sheet La feuille Excel où les valeurs doivent être exportées.
     */
    private static void exportValuesToSheet(String[] values, Sheet sheet) {
        if (values == null || sheet == null) {
            return;
        }

        int rowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowNum);

        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            
            if (value.equals("-1.0")) {
                continue;
            }
            
            Cell cell = row.createCell(i);

            try {
                cell.setCellValue(Double.parseDouble(value));
            } catch (NumberFormatException e) {
                cell.setCellValue(value);
            }
        }
    }
}
