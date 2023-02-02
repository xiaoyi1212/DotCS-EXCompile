package ex.mcppl.vm.code;

import ex.mcppl.vm.VMRuntimeException;
import ex.mcppl.vm.buf.ExObject;
import ex.mcppl.vm.exe.Executor;
import ex.mcppl.vm.exe.Function;
import ex.mcppl.vm.lib.BasicLibrary;
import ex.mcppl.vm.lib.LibLoader;

import java.util.HashMap;

public class InvokeByteCode implements ByteCode{
    String function_name;
    String lib_name;
    HashMap<String,ExObject> value;

    public InvokeByteCode(String lib_name,String function_name, HashMap<String, ExObject> value){
        this.lib_name = lib_name;
        this.function_name = function_name;
        this.value = value;
    }

    @Override
    public void exe(Executor executor) throws VMRuntimeException {
        if(LibLoader.isLoaderLiba(lib_name)) {
            if (LibLoader.isLoaderFunction(lib_name, function_name)) {
                for (BasicLibrary bl : LibLoader.libs.get(lib_name)) {
                    if (bl.getLibFunctionName().equals(function_name)) {
                        bl.invoke(value, executor);
                    }
                }
            } else throw new VMRuntimeException("Unknown function name.");
        }else if(lib_name.equals("this")){
            for(Function function:executor.getFunctions()){
                if(function.getName().equals(function_name)){
                    function.invoke(executor,value);
                    return;
                }
            }
            throw new VMRuntimeException("Not found function in this script: '"+function_name+"'.");
        }else{
            throw new VMRuntimeException("Unknown lib name:'"+lib_name+"' You can use 'include \""+lib_name+"\";' import library.");
        }
    }
}
