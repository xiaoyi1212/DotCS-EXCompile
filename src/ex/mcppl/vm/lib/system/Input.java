package ex.mcppl.vm.lib.system;

import ex.mcppl.vm.VMRuntimeException;
import ex.mcppl.vm.buf.ExObject;
import ex.mcppl.vm.buf.ExString;
import ex.mcppl.vm.exe.Executor;
import ex.mcppl.vm.lib.BasicLibrary;

import java.util.HashMap;
import java.util.Scanner;

public class Input implements BasicLibrary {
    @Override
    public ExObject invoke(HashMap<String, ExObject> values, Executor executor) throws VMRuntimeException {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        executor.getStack().push(new ExString(s));
        return new ExString(s);
    }

    @Override
    public String getLibFunctionName() {
        return "input";
    }
}
