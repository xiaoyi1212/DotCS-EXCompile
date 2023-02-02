package ex.mcppl.vm.lib.vm;

import ex.mcppl.vm.VMRuntimeException;
import ex.mcppl.vm.buf.ExObject;
import ex.mcppl.vm.exe.Executor;
import ex.mcppl.vm.lib.BasicLibrary;

import java.util.HashMap;

public class GetValueText implements BasicLibrary {
    @Override
    public ExObject invoke(HashMap<String, ExObject> values, Executor executor) throws VMRuntimeException {
        String name = values.get("var").getData();
        //if(AllValueBuffer.values.get(name) == null) throw new VMRuntimeException();
        return null;
    }

    @Override
    public String getLibFunctionName() {
        return "text";
    }
}
