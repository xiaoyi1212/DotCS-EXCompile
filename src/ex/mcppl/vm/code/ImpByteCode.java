package ex.mcppl.vm.code;

import ex.mcppl.vm.VMRuntimeException;
import ex.mcppl.vm.exe.Executor;
import ex.mcppl.vm.lib.LibLoader;

public class ImpByteCode implements ByteCode{
    String libname;
    public ImpByteCode(String libname){
        this.libname = libname;
    }
    @Override
    public void exe(Executor executor) throws VMRuntimeException {
        if(!LibLoader.isLibs(libname)) throw new VMRuntimeException("Not found library:"+libname);
        LibLoader.loader.put(libname,LibLoader.libs.get(libname));
    }
}
