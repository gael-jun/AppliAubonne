Vue util helpers
=================

But : regrouper les utilitaires d'interface réutilisables pour les pages PVGIS.

Contenu :

1. ChartsFactory (ex ChartFactory)
   - Fabrique normalisée de graphiques XChart (dimensions, style homogène, méthodes createXXX...).

2. ExportWorkerFactory (ex ExportWorkers)
   - Méthode utilitaire createExportWorker(...) construisant un SwingWorker d'export (CSV / PDF) à partir d'un ExportContext supplier et d'une stratégie.

3. FinancialCharts (ex FinancialChartsRenderer)
   - Rend les graphes financiers dans GraphsPanel et alimente une structure Cache avec les images et séries pour l'export.

4. StatusBarUtil (ex ToolbarUtil)
   - Aide pour mettre à jour de façon cohérente le label de statut (attente, export, succès, erreur). Les anciennes fonctions de gestion des boutons ont été retirées (désormais gérées par ToolbarPanel).
