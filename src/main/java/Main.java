import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        HomePageFrame homePageFrame = new HomePageFrame();
        homePageFrame.openHomePage();
        Queries queries = new Queries();

        Basket basket = new Basket();
        basket.addRezeptToBasket(1,3);
        basket.addRezeptToBasket(2,4);
        basket.addRezeptToBasket(3,5);
        //basket.addZutatToBasket(3,5);
        //BasketFrame frame = new BasketFrame(1001, basket);
    }
}
