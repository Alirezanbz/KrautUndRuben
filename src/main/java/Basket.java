import java.util.ArrayList;

public class Basket extends Queries{
    ArrayList<ArrayList<Integer>> rezepte;
    ArrayList<ArrayList<Integer>> zutaten;

    public Basket() {
        this.rezepte = new ArrayList<>();
        this.zutaten = new ArrayList<>();
    }

    void addRezeptToBasket(Integer rezeptId, Integer menge){
        ArrayList<Integer> rezept = new ArrayList<>();
        rezept.add(rezeptId);
        rezept.add(menge);
        this.rezepte.add(rezept);
    }
    void addZutatToBasket(Integer zutatId, Integer menge){
        ArrayList<Integer> zutat = new ArrayList<>();
        zutat.add(zutatId);
        zutat.add(menge);
        this.zutaten.add(zutat);
    }
}
