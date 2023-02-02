package ex.mcppl.vm.exe;

import ex.mcppl.vm.VMRuntimeException;
import ex.mcppl.vm.buf.ExObject;
import ex.mcppl.vm.code.ByteCode;

import java.util.ArrayList;
import java.util.Stack;

public class Executor {
    private Executor(){
    }
    public enum Status{
        RUNNING,LOADING,STOP,ERROR
    }
    Stack<ExObject> stack;
    FileByteCode fbc;
    Status status;
    ArrayList<Function> functions;
    static Executor e;
    boolean debug;
    public static Executor load(FileByteCode fbc){
        e = new Executor();
        e.stack = new Stack<>();
        e.fbc = fbc;
        e.status = Status.LOADING;
        e.functions = new ArrayList<>();
        e.debug = false;
        return e;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void addFunction(Function function){
        functions.add(function);
    }

    public ArrayList<Function> getFunctions(){
        return functions;
    }

    public Stack<ExObject> getStack() {
        return stack;
    }
    public void push(ExObject obj){
        stack.push(obj);
    }

    public Status getStatus() {
        return status;
    }

    public void start(){
        this.status = Status.RUNNING;
        long a = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (ByteCode bc : fbc.getBcs()) {
                        bc.exe(e);
                    }
                    status = Status.STOP;
                }catch (VMRuntimeException e){
                    status = Status.ERROR;
                }
                if(debug){
                    System.out.println("Done! Times("+(System.currentTimeMillis()- a)+")MS");
                    System.out.println("LoadedFunctions:"+functions.size());
                    System.out.println("StackSize:"+stack.size());
                    System.out.println("ByteCodes:"+fbc.getBcs().size());
                }
            }
        }).start();
    }
}
