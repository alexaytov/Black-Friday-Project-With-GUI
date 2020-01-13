package product.interfaces;

import exceptions.NotEnoughQuantityException;

public interface Buyable {

    void buy(int quantity) throws NotEnoughQuantityException;

    double getPrice();

    void setPrice(double price);

}
