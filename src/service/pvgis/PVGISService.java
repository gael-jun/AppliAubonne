package service.pvgis;

import modele.pvgis.PVOffGridRequest;
import modele.pvgis.PVGISResult;
import modele.pvgis.PVGridAndTrackerRequest;

import java.io.IOException;

/**
 * Service façade pour l'API PVGIS.
 * Regroupe la construction des URLs et délègue:
 *  - à {@link PVGISClient} l'exécution HTTP (implémentation injectée pour faciliter les tests)
 *  - à {@link PVGISParser} le parsing JSON -> {@link modele.pvgis.PVGISResult}
 *
 * Deux endpoints principaux sont gérés:
 *  - SHScalc (systèmes isolés / off-grid)
 *  - PVcalc (systèmes grid & tracker)
 */
public class PVGISService {
    /** Client HTTP (interface) permettant d'injecter un mock lors des tests. */
    private final PVGISClient client;

    /**
     * Constructeur par défaut utilisant l'implémentation HTTP réelle.
     */
    public PVGISService() { this(new HttpClientPVGIS()); }

    /**
     * Constructeur avec injection du client (testabilité, substitution).
     * @param client implémentation de {@link PVGISClient}
     */
    public PVGISService(PVGISClient client) { this.client = client; }

    /**
     * Appelle l'endpoint SHScalc (off-grid) et retourne le résultat typé.
     * @param req paramètres (DTO) off-grid
     * @return résultat parsé
     * @throws IOException erreur réseau
     * @throws InterruptedException interruption appel HTTP
     */
    public PVGISResult fetch(PVOffGridRequest req) throws IOException, InterruptedException {
        // Construction incrémentale de l'URL (ajout uniquement des paramètres non vides)
        StringBuilder url = new StringBuilder("https://re.jrc.ec.europa.eu/api/v5_3/SHScalc?");
        url.append("lat=").append(req.lat)
           .append("&lon=").append(req.lon)
           .append("&peakpower=").append(req.peakPower)
           .append("&batterysize=").append(req.batterySize)
           .append("&cutoff=").append(req.cutoff)
           .append("&consumptionday=").append(req.consumptionDay);
        // Paramètres optionnels
        if (notEmpty(req.angle)) url.append("&angle=").append(req.angle);
        if (notEmpty(req.aspect)) url.append("&aspect=").append(req.aspect);
        if (req.radDatabase != null) url.append("&raddatabase=").append(req.radDatabase);
        url.append("&usehorizon=").append(req.useHorizon ? "1" : "0");
        if (notEmpty(req.userHorizon)) url.append("&userhorizon=").append(req.userHorizon);
        if (notEmpty(req.hourConsumption)) url.append("&hourconsumption=").append(req.hourConsumption);
        if (notEmpty(req.outputFormat)) url.append("&outputformat=").append(req.outputFormat);
        url.append("&browser=").append(req.browser ? "1" : "0");

        String responseBody = client.get(url.toString());      // Exécution HTTP
        return PVGISParser.parse(responseBody);                // Parsing JSON -> modèle
    }

    /**
     * Appelle l'endpoint PVcalc (grid & tracker) et retourne le résultat typé.
     * @param req paramètres (DTO) grid ou tracker
     * @return résultat parsé
     * @throws IOException erreur réseau
     * @throws InterruptedException interruption appel HTTP
     */
    public PVGISResult fetchPVcalc(PVGridAndTrackerRequest req) throws IOException, InterruptedException {
        StringBuilder url = new StringBuilder("https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?");
        // Paramètres obligatoires / de base
        url.append("lat=").append(req.lat)
           .append("&lon=").append(req.lon)
           .append("&peakpower=").append(req.peakPower)
           .append("&loss=").append(req.loss)
           .append("&fixed=").append(req.fixed ? "1" : "0")
           .append("&optimalinclination=").append(req.optimalInclination ? "1" : "0")
           .append("&optimalangles=").append(req.optimalAngles ? "1" : "0")
           .append("&inclined_axis=").append(req.inclinedAxis ? "1" : "0")
           .append("&inclined_optimum=").append(req.inclinedOptimum ? "1" : "0")
           .append("&vertical_axis=").append(req.verticalAxis ? "1" : "0")
           .append("&vertical_optimum=").append(req.verticalOptimum ? "1" : "0")
           .append("&twoaxis=").append(req.twoAxis ? "1" : "0")
           .append("&usehorizon=").append(req.useHorizon ? "1" : "0")
           .append("&browser=").append(req.browser ? "1" : "0");

        // Paramètres optionnels (ajoutés seulement s'ils sont fournis)
        if (req.radDatabase != null) url.append("&raddatabase=").append(req.radDatabase);
        if (req.pvTechChoice != null) url.append("&pvtechchoice=").append(req.pvTechChoice);
        if (req.mountingPlace != null) url.append("&mountingplace=").append(req.mountingPlace);
        if (notEmpty(req.angle)) url.append("&angle=").append(req.angle);
        if (notEmpty(req.aspect)) url.append("&aspect=").append(req.aspect);
        if (notEmpty(req.inclinedAxisAngle)) url.append("&inclinedaxisangle=").append(req.inclinedAxisAngle);
        if (notEmpty(req.verticalAxisAngle)) url.append("&verticalaxisangle=").append(req.verticalAxisAngle);
        if (notEmpty(req.pvPrice)) url.append("&pvprice=").append(req.pvPrice);
        if (notEmpty(req.systemCost)) url.append("&systemcost=").append(req.systemCost);
        if (notEmpty(req.interest)) url.append("&interest=").append(req.interest);
        if (notEmpty(req.lifetime)) url.append("&lifetime=").append(req.lifetime);
        if (notEmpty(req.userHorizon)) url.append("&userhorizon=").append(req.userHorizon);
        if (notEmpty(req.outputFormat)) url.append("&outputformat=").append(req.outputFormat);
        if (req.includeGlobal) url.append("&global=1"); // Option pour inclure irradiation globale

        String responseBody = client.get(url.toString());
        return PVGISParser.parse(responseBody);
    }

    // --- Helpers internes -------------------------------------------------

    /** Retourne vrai si la chaîne n'est ni nulle ni vide après trim. */
    private static boolean notEmpty(String s) { return s != null && !s.isEmpty(); }
}
