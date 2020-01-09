package product.interfaces;

import exceptions.NotEnoughQuantityException;
import user.User;

public interface Buyable {

    void buy(User user, int quantity) throws NotEnoughQuantityException;

    double getPrice();

    void setPrice(double price);

}
