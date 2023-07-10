import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {



        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);

        //System.out.println(queries.selectQuery("nachname", "kunden", "where kunden.kdnr = 1001"));
        Queries queries = new Queries();

        Basket basket = new Basket();
        basket.addRezeptToBasket(1,3);
        basket.addRezeptToBasket(2,4);
        basket.addRezeptToBasket(3,5);
        //basket.addZutatToBasket(3,5);
        //BasketFrame frame = new BasketFrame(1001, basket);
    }
}
