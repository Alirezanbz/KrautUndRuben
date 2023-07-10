import java.util.ArrayList;

public class Basket extends Queries{
    ArrayList<ArrayList<Integer>> rezepte;
    ArrayList<ArrayList<Integer>> zutaten;

    public Basket() {
        this.rezepte = new ArrayList<>();
        this.zutaten = new ArrayList<>();
    }

    void addRezeptToBasket(Integer rezeptId, Integer menge){
        Boolean duplicate = false;
        ArrayList<Integer> newRezept = new ArrayList<>();
        for(ArrayList<Integer> rezept : this.rezepte){
            if (rezeptId == rezept.get(0)) {
                duplicate = true;
                rezept.set(1, rezept.get(1) + menge);
            }
        }
        if (!duplicate) {
            newRezept.add(rezeptId);
            newRezept.add(menge);
            this.rezepte.add(newRezept);
        }
    }
    void addZutatToBasket(Integer zutatId, Integer menge){
        Boolean duplicate = false;
        ArrayList<Integer> newZutat = new ArrayList<>();
        for(ArrayList<Integer> zutat : this.zutaten){
            if (zutatId == zutat.get(0)) {
                duplicate = true;
                zutat.set(1, zutat.get(1) + menge);
            }
        }
        if (!duplicate) {
            newZutat.add(zutatId);
            newZutat.add(menge);
            this.zutaten.add(newZutat);
        }
    }
}
