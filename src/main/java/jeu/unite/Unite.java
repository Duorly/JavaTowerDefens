package jeu.unite;

public class Unite {
    private String nom;
    private int pointsDeVie;
    private int attaque;
    private int defense;
    private int cout;

    public Unite(String nom, int pointsDeVie, int attaque, int defense, int cout) {
        this.nom = nom;
        this.pointsDeVie = pointsDeVie;
        this.attaque = attaque;
        this.defense = defense;
        this.cout = cout;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public void setPointsDeVie(int pointsDeVie) {
        this.pointsDeVie = pointsDeVie;
    }

    public int getAttaque() {
        return attaque;
    }

    public void setAttaque(int attaque) {
        this.attaque = attaque;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public int attaquer() {
        return this.attaque;
    }

    public void subirDegats(int degats) {
        int dommages = Math.max(0, degats - this.defense);
        this.pointsDeVie -= dommages;
    }

    public boolean estDetruit() {
        return this.pointsDeVie <= 0;
    }

    public String toString() {
        return nom + " (PV: " + pointsDeVie + ", Attaque: " + attaque + ", Défense: " + defense + ", Coût: " + cout + ")";
    }
}
