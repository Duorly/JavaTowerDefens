package jeu;

import jeu.unite.*;

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
        this.argent = 200;
        this.armee = new ArrayList<>();
        this.ennemis = new ArrayList<>();
        this.tour = 1;
        this.niveauDeDifficulte = "Normal";
    }

    public void demarrer() {
        System.out.println("Bienvenue dans votre Starwars Tower Defense !");
        Scanner scanner = new Scanner(System.in);
        choisirDifficulte(scanner);

        while (true) {
            System.out.println("\n==== Tour " + tour + " ====");
            afficherEtat();
            System.out.println("1. Acheter des unités\n2. Attaquer\n3. Défendre\n4. Quitter");
            int choix = lireEntreeEntier(scanner);

            switch (choix) {
                case 1 -> acheterUnites(scanner);
                case 2 -> attaquer(scanner);
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
        System.out.println("\nEnnemis :");
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
            int type = random.nextInt(6);
            if (type == 0) {
                ennemis.add(new Unite("Droïd de Combat B1", 50 * multiplicateur, 10 * multiplicateur, 5, 0));
            } else if (type == 1) {
                ennemis.add(new Unite("Soldat Stormtrooper", 100 * multiplicateur, 20 * multiplicateur, 10, 0));
            } else if (type == 2) {
                ennemis.add(new Unite("Inquisiteur Sith", 250 * multiplicateur, 50 * multiplicateur, 20, 0));
            } else if (type == 3) {
                ennemis.add(new Unite("Garde Impérial", 150 * multiplicateur, 30 * multiplicateur, 15, 0));
            } else if (type == 4) {
                ennemis.add(new Unite("Chasseur TIE", 80 * multiplicateur, 25 * multiplicateur, 8, 0));
            } else {
                ennemis.add(new Unite("Darth Maul", 350 * multiplicateur, 70 * multiplicateur, 25, 0));
            }
        }
        System.out.println("Une vague d'ennemis est arrivée !");
    }

    public void acheterUnites(Scanner scanner) {
        System.out.println("1. Soldat Rebelle (50 Crédits Galactiques)\n2. Commando Clone (70 Crédits Galactiques)\n3. Chevalier JEDI (150 Crédits Galactiques)\n4. Droïde Avancé (80 Crédits Galactiques)\n5. Jedi Noir (200 Crédits Galactiques)\n6. Mercenaire (60 Crédits Galactiques)");
        int choix = lireEntreeEntier(scanner);

        Unite nouvelleUnite;
        switch (choix) {
            case 1 -> nouvelleUnite = new Soldat();
            case 2 -> nouvelleUnite = new Commando();
            case 3 -> nouvelleUnite = new ChevalierJedi();
            case 4 -> nouvelleUnite = new Droide();
            case 5 -> nouvelleUnite = new JediNoir();
            case 6 -> nouvelleUnite = new Mercenaire();
            default -> {
                System.out.println("Choix invalide.");
                return;
            }
        }

        if (nouvelleUnite.getCout() > argent) {
            System.out.println("Pas assez d'argent !");
        } else {
            argent -= nouvelleUnite.getCout();
            armee.add(nouvelleUnite);
            System.out.println("Unité achetée : " + nouvelleUnite);
        }
    }

    public void attaquer(Scanner scanner) {
        System.out.println("Phase d'attaque ! Choisissez un ennemi à attaquer :");
        for (int i = 0; i < ennemis.size(); i++) {
            System.out.println((i + 1) + ". " + ennemis.get(i));
        }

        int choix = lireEntreeEntier(scanner);
        if (choix < 1 || choix > ennemis.size()) {
            System.out.println("Choix invalide. Attaque annulée.");
            return;
        }

        Unite cible = ennemis.get(choix - 1);
        ArrayList<Unite> unitesDetruites = new ArrayList<>();

        for (Unite unite : armee) {
            cible.subirDegats(unite.attaquer());

            Random random = new Random();
            if (random.nextBoolean()) { // Contre-attaque aléatoire
                unite.subirDegats(cible.attaquer());
            }

            if (cible.estDetruit()) {
                ennemis.remove(cible);
                argent += 50; // Récompense
                System.out.println(cible.getNom() + " est détruit !");
                break;
            }

            if (unite.estDetruit()) {
                unitesDetruites.add(unite);
            }
        }

        armee.removeAll(unitesDetruites);
        for (Unite unite : unitesDetruites) {
            System.out.println(unite.getNom() + " est détruit !");
        }
    }

    public void defendre() {
        System.out.println("Phase de défense !");
        ArrayList<Unite> unitesDetruites = new ArrayList<>();

        defenseCri();

        for (Unite ennemi : ennemis) {
            for (Unite unite : armee) {
                unite.subirDegats(ennemi.attaquer() / 2);
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

    private void attaqueCri() {
        Random random = new Random();
        String[] messages = {
                "Que la Force soit avec nous, attaquez sans relâche !",
                "Pour la Rébellion, écrasez l'ennemi !",
                "L'Empire tremblera devant notre puissance !",
                "Déployez vos sabres et vos blasters, la victoire nous attend !",
                "Pour la République, chargez !",
                "En avant, soldats de la galaxie, pour la liberté !",
                "Les Sith ne passeront pas, attaquez avec honneur !",
                "Unissez vos forces, ensemble nous sommes invincibles !",
                "L'heure de la revanche est arrivée, que l'Empire tombe !",
                "Frappez vite et fort, que nos ennemis se dispersent dans l'ombre !"
        };

        System.out.println(messages[random.nextInt(messages.length)]);
    }

    private void defenseCri() {
        Random random = new Random();

        String[] messages = {
                "Tenez vos positions, nous ne céderons pas un pouce à l'Empire !",
                "Les boucliers sont activés, résistez avec courage !",
                "Que la Force renforce nos défenses, rien ne passera !"
        };

        System.out.println(messages[random.nextInt(messages.length)]);
    }
}