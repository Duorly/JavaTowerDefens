package jeu;

import jeu.unite.Commando;
import jeu.unite.ChevalierJedi;
import jeu.unite.Soldat;
import jeu.unite.Unite;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Jeu {
    private int argent;
    private final ArrayList<Unite> armee;
    private final ArrayList<Unite> ennemis;
    private int tour;
    private String niveauDeDifficulte;

    public Jeu() {
        this.argent = 200; // Argent de départ
        this.armee = new ArrayList<>();
        this.ennemis = new ArrayList<>();
        this.tour = 1;
        this.niveauDeDifficulte = "Normal";
    }

    public void demarrer() {
        System.out.println("Bienvenue dans votre Tower Defense !");
        Scanner scanner = new Scanner(System.in);
        choisirDifficulte(scanner);

        while (true) {
            System.out.println("\n==== Tour " + tour + " ====");
            afficherEtat();
            System.out.println("1. Acheter des unités\n2. Attaquer\n3. Défendre\n4. Quitter");
            int choix = lireEntreeEntier(scanner);

            switch (choix) {
                case 1 -> acheterUnites(scanner);
                case 2 -> attaquer();
                case 3 -> defendre();
                case 4 -> {
                    System.out.println("Merci d'avoir joué !");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }

            if (ennemis.isEmpty()) {
                genererEnnemis(tour);
                tour++;
            }

            if (armee.isEmpty()) {
                System.out.println("Votre armée a été détruite. Game Over !");
                break;
            }
        }
        scanner.close();
    }

    private void afficherEtat() {
        System.out.println("Argent disponible: " + argent + " Crédits Galactiques");
        System.out.println("Votre armée :");
        for (Unite unite : armee) {
            System.out.println("  - " + unite);
        }
        System.out.println("Ennemis :");
        for (Unite ennemi : ennemis) {
            System.out.println("  - " + ennemi);
        }
    }

    private void choisirDifficulte(Scanner scanner) {
        System.out.println("Choisissez le niveau de difficulté :");
        System.out.println("1. Facile\n2. Normal\n3. Difficile");
        int choix = lireEntreeEntier(scanner);

        switch (choix) {
            case 1 -> {
                niveauDeDifficulte = "Facile";
                argent = 300;
                System.out.println("Mode Facile sélectionné. Bonne chance !");
            }
            case 2 -> {
                niveauDeDifficulte = "Normal";
                argent = 200;
                System.out.println("Mode Normal sélectionné. Bonne chance !");
            }
            case 3 -> {
                niveauDeDifficulte = "Difficile";
                argent = 150;
                System.out.println("Mode Difficile sélectionné. Bonne chance !");
            }
            default -> {
                System.out.println("Choix invalide. Mode Normal sélectionné par défaut.");
                niveauDeDifficulte = "Normal";
            }
        }
    }

    private void genererEnnemis(int tour) {
        ennemis.clear();
        Random random = new Random();
        int multiplicateur = Objects.equals(niveauDeDifficulte, "Difficile") ? 2 : 1;

        int nombreEnnemis = (3 + tour) * multiplicateur;
        for (int i = 0; i < nombreEnnemis; i++) {
            int type = random.nextInt(3); // 0 = faible, 1 = intermédiaire, 2 = boss
            if (type == 0) {
                ennemis.add(new Unite("Droïde de Combat B1", 50 * multiplicateur, 10 * multiplicateur, 5, 0));
            } else if (type == 1) {
                ennemis.add(new Unite("Soldat Stormtrooper", 100 * multiplicateur, 20 * multiplicateur, 10, 0));
            } else {
                ennemis.add(new Unite("Inquisiteur Sith", 300 * multiplicateur, 50 * multiplicateur, 20, 0));
            }
        }
        System.out.println("Une vague d'ennemis est arrivée !");
    }

    public void acheterUnites(Scanner scanner) {
        System.out.println("1. Soldat Rebelle (50 Crédits Galactiques)\n2. Commando Clone (70 Crédits Galactiques)\n3. Chevalier JEDI (150 Crédits Galactiques)");
        int choix = lireEntreeEntier(scanner);

        Unite nouvelleUnite;
        if (choix == 1) {
            nouvelleUnite = new Soldat();
        } else if (choix == 2) {
            nouvelleUnite = new Commando();
        } else if (choix == 3) {
            nouvelleUnite = new ChevalierJedi();
        } else {
            System.out.println("Choix invalide.");
            return;
        }

        if (nouvelleUnite.getCout() > argent) {
            System.out.println("Pas assez d'argent !");
        } else {
            argent -= nouvelleUnite.getCout();
            armee.add(nouvelleUnite);
            System.out.println("Unité achetée : " + nouvelleUnite);
        }
    }

    public void attaquer() {
        System.out.println("Phase d'attaque !");
        ArrayList<Unite> ennemisDetruits = new ArrayList<>();
        ArrayList<Unite> unitesDetruites = new ArrayList<>();

        Random random = new Random();
        String[] messages = {
                "Vos unités attaquent avec une rage incroyable !",
                "Vos troupes s'élancent au combat en criant des slogans épiques !",
                "Les ennemis tremblent devant la puissance de votre armée !"
        };

        System.out.println(messages[random.nextInt(messages.length)]);


        for (Unite ennemi : ennemis) {
            for (Unite unite : armee) {
                ennemi.subirDegats(unite.attaquer());
                unite.subirDegats(ennemi.attaquer() / 2); // Contre-attaque ennemie

                if (ennemi.estDetruit()) {
                    ennemisDetruits.add(ennemi);
                    argent += 50; // Récompense
                    break;
                }
                if (unite.estDetruit()) {
                    unitesDetruites.add(unite);
                    break;
                }
            }
        }

        ennemis.removeAll(ennemisDetruits);
        armee.removeAll(unitesDetruites);

        for (Unite ennemi : ennemisDetruits) {
            System.out.println(ennemi.getNom() + " est détruit !");
        }
        for (Unite unite : unitesDetruites) {
            System.out.println(unite.getNom() + " est détruit !");
        }
    }


    public void defendre() {
        System.out.println("Phase de défense !");
        ArrayList<Unite> unitesDetruites = new ArrayList<>();

        Random random = new Random();

        String[] messages = {
                "Vos unités s'organisent en une défense impénétrable !",
                "Les ennemis frappent, mais votre armée résiste héroïquement !",
                "Les murs tiennent bon, mais la bataille fait rage !"
        };

        System.out.println(messages[random.nextInt(messages.length)]);


        for (Unite ennemi : ennemis) {
            for (Unite unite : armee) {
                unite.subirDegats(ennemi.attaquer() / 2); // Réduction des dégâts
                if (unite.estDetruit()) {
                    unitesDetruites.add(unite);
                    break;
                }
            }
        }

        armee.removeAll(unitesDetruites);
        for (Unite unite : unitesDetruites) {
            System.out.println(unite.getNom() + " est détruite !");
        }
    }

    private int lireEntreeEntier(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
