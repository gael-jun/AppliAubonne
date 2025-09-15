package modele.cablesetprotections;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * La classe abstraite {@code FamilleDElements} représente une collection d'éléments
 * identifiés par une référence unique. Elle fournit des méthodes pour ajouter, supprimer,
 * obtenir et afficher des éléments de cette collection.
 * <p>
 * Les sous-classes de {@code FamilleDElements} pourront étendre cette classe pour gérer
 * des types spécifiques d'éléments tout en utilisant les fonctionnalités de base fournies.
 * </p>
 */
public abstract class FamilleDElements {
    
    private Map<String, Element> famille;

    /**
     * Initialise une nouvelle instance de {@code FamilleDElements} avec une collection vide.
     */
    public FamilleDElements() {
        famille = new LinkedHashMap<>();
    }
    
    /**
     * Ajoute un élément à la collection.
     * <p>
     * Si un élément avec la même référence existe déjà, il sera remplacé.
     * </p>
     *
     * @param element L'élément à ajouter à la collection.
     * @throws NullPointerException si {@code element} est {@code null}.
     */
    public void ajouterElement(Element element) {
        if (element == null) {
            throw new NullPointerException("L'élément ne peut pas être nul.");
        }
        famille.put(element.getReference(), element);
    }

    /**
     * Supprime un élément de la collection en fonction de sa référence.
     * 
     * @param reference La référence de l'élément à supprimer.
     * @throws NullPointerException si {@code reference} est {@code null}.
     */
    public void supprimerElement(String reference) {
        if (reference == null) {
            throw new NullPointerException("La référence ne peut pas être nulle.");
        }
        famille.remove(reference);
    }

    /**
     * Obtient un élément de la collection en fonction de sa référence.
     *
     * @param reference La référence de l'élément à obtenir.
     * @return L'élément correspondant à la référence, ou {@code null} si aucun élément
     *         avec la référence spécifiée n'existe.
     * @throws NullPointerException si {@code reference} est {@code null}.
     */
    public Element obtenirElement(String reference) {
        if (reference == null) {
            throw new NullPointerException("La référence ne peut pas être nulle.");
        }
        return famille.get(reference);
    }

    /**
     * Obtient la liste des références de tous les éléments dans la collection.
     *
     * @return Une liste des références des éléments.
     */
    public List<String> getReferences() {
        return new ArrayList<>(famille.keySet());
    }
    
    /**
     * Obtient la liste de tous les éléments dans la collection.
     *
     * @return Une liste contenant tous les éléments de la collection.
     */
    public List<Element> getElements() {
        return new ArrayList<>(famille.values());
    }
    
    /**
     * Affiche tous les éléments de la collection en utilisant leur méthode {@code toString}.
     */
    public void afficherTousLesElements() {
        for (Element e : famille.values()) {
            System.out.println(e);
        }
    }
}
