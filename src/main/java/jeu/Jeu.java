package jeu;

import jeu.unite.Archer;
import jeu.unite.Cavalier;
import jeu.unite.Soldat;
import jeu.unite.Unite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Jeu {
    private int argent;
    private ArrayList<Unite> armee;
    private ArrayList<Unite> ennemis;
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
            System.out.println("\nTour " + tour);
            afficherEtat();
            System.out.println("1. Acheter des unités\n2. Attaquer\n3. Défendre\n4. Quitter");
            int choix = scanner.nextInt();

            switch (choix) {
                case 1 -> acheterUnites(scanner);
                case 2 -> attaquer();
                case 3 -> defendre();
                case 4 -> {
                    System.out.println("Merci d'avoir joué !");
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
        System.out.println("Argent disponible: " + argent);
        System.out.println("Votre armée: " + armee);
        System.out.println("Ennemis: " + ennemis);
    }

    private void choisirDifficulte(Scanner scanner) {
        System.out.println("Choisissez le niveau de difficulté :");
        System.out.println("1. Facile\n2. Normal\n3. Difficile");
        int choix = scanner.nextInt();

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
        int multiplicateur = switch (niveauDeDifficulte) {
            case "Facile" -> 1;
            case "Difficile" -> 2;
            default -> 1;
        };

        int nombreEnnemis = (3 + tour) * multiplicateur;
        for (int i = 0; i < nombreEnnemis; i++) {
            int type = random.nextInt(3); // 0 = faible, 1 = intermédiaire, 2 = boss
            if (type == 0) {
                ennemis.add(new Unite("Ennemi Faible", 50 * multiplicateur, 10 * multiplicateur, 5, 0));
            } else if (type == 1) {
                ennemis.add(new Unite("Ennemi Intermédiaire", 100 * multiplicateur, 20 * multiplicateur, 10, 0));
            } else {
                ennemis.add(new Unite("Boss", 300 * multiplicateur, 50 * multiplicateur, 20, 0));
            }
        }
        System.out.println("Une vague d'ennemis est arrivée !");
    }

    public void acheterUnites(Scanner scanner) {
        System.out.println("1. Soldat (50)\n2. Archer (70)\n3. Cavalier (150)");
        int choix = scanner.nextInt();

        Unite nouvelleUnite = null;
        if (choix == 1) {
            nouvelleUnite = new Soldat();
        } else if (choix == 2) {
            nouvelleUnite = new Archer();
        } else if (choix == 3) {
            nouvelleUnite = new Cavalier();
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
        Random random = new Random();
        String[] messages = {
                "Vos unités attaquent avec une rage incroyable !",
                "Vos troupes s'élancent au combat en criant des slogans épiques !",
                "Les ennemis tremblent devant la puissance de votre armée !"
        };

        System.out.println(messages[random.nextInt(messages.length)]);

        Iterator<Unite> iterEnnemis = ennemis.iterator();
        while (iterEnnemis.hasNext()) {
            Unite ennemi = iterEnnemis.next();
            for (Unite unite : armee) {
                ennemi.subirDegats(unite.attaquer());
                if (ennemi.estDetruit()) {
                    System.out.println(ennemi.getNom() + " est détruit !");
                    iterEnnemis.remove();
                    argent += 50; // Récompense
                    break;
                }
            }
        }
    }

    public void defendre() {
        System.out.println("Phase de défense !");
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
                    System.out.println(unite.getNom() + " est détruit !");
                    armee.remove(unite);
                    break;
                }
            }
        }
    }
}