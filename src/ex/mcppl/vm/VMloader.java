package ex.mcppl.vm;

import ex.mcppl.vm.exe.Executor;
import ex.mcppl.vm.exe.FileByteCode;

import java.util.ArrayList;

public class VMloader {
    public static ArrayList<FileByteCode> fbc = new ArrayList<>();
    public static void launch() throws VMRuntimeException{
        for(FileByteCode fbcc:fbc){
            Executor e = Executor.load(fbcc);
            e.start();
        }
    }
}
