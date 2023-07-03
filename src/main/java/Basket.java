import java.util.ArrayList;

public class Basket extends Queries{
    ArrayList<Integer> rezepte;
    ArrayList<Integer> zutaten;

    public Basket() {
    }

    void addRezeptToBasket(Integer rezeptId){
        rezepte.add(rezeptId);
    }
    void addZutatToBasket(Integer zutatId){
        zutaten.add(zutatId);
    }
}
