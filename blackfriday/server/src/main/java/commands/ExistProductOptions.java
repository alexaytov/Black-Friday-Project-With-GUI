package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import store.Store;

public class ExistProductOptions implements Executable {

    @Inject
    private Store store;

    /**
     * Sets store chosen product to null
     */
    @Override
    public void execute() {
        this.store.setChosenProduct(null);
    }
}
