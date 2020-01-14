package commands;

import command.enterpreter.interfaces.Executable;
import store.services.ProductService;

public class ExistProductOptions implements Executable {

    private ProductService productService;

    public ExistProductOptions(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Sets product service chosen product to null
     */
    @Override
    public void execute() {
        productService.setChosenProduct(null);
    }
}
