import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /*HomePageFrame homePageFrame = new HomePageFrame();
        homePageFrame.openHomePage();*/
        Queries queries = new Queries();

        Basket basket = new Basket();
        basket.rezepte.add(1);
        basket.rezepte.add(2);
        BasketFrame frame = new BasketFrame(1001, basket);
    }
}
