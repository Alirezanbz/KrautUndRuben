import java.util.ArrayList;

public class Basket extends Queries{
    ArrayList<Integer> rezepte;
    ArrayList<Integer> zutaten;

    public Basket() {
        this.rezepte = new ArrayList<>();
        this.zutaten = new ArrayList<>();
    }

    void addRezeptToBasket(Integer rezeptId){
        this.rezepte.add(rezeptId);
    }
    void addZutatToBasket(Integer zutatId){
        this.zutaten.add(zutatId);
    }
}
