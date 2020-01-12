package commandEnterpreter;

import commandEnterpreter.interfaces.Executable;

public interface CommandInterpreter {

    Executable interpretCommand(String data);
}
