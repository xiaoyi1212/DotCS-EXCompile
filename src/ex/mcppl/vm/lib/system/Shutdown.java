package ex.mcppl.vm.lib.system;

import ex.mcppl.vm.VMRuntimeException;
import ex.mcppl.vm.buf.ExObject;
import ex.mcppl.vm.exe.Executor;
import ex.mcppl.vm.lib.BasicLibrary;

import java.util.HashMap;

public class Shutdown implements BasicLibrary {
    @Override
    public ExObject invoke(HashMap<String, ExObject> values, Executor executor) throws VMRuntimeException {
        try {
            if (values.get("exit") == null) throw new VMRuntimeException("system.shutdown : Not found value 'exit'");
            int e = Integer.parseInt(values.get("exit").getData());
            System.exit(e);
        }catch (ClassCastException e){
            throw new VMRuntimeException("system.shutdown : Value 'exit' type isn't integer.");
        }
        return new ExObject(ExObject.Type.VOID,"");
    }

    @Override
    public String getLibFunctionName() {
        return "shutdown";
    }
}
