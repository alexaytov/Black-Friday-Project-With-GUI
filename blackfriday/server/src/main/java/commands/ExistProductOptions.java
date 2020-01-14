package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import store.services.ProductService;

public class ExistProductOptions implements Executable {

    @Inject
    private ProductService productService;

    /**
     * Sets product service chosen product to null
     */
    @Override
    public void execute() {
        productService.setChosenProduct(null);
    }
}
