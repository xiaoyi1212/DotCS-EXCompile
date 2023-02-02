package ex.mcppl.vm;

import ex.mcppl.Main;

public class VMRuntimeException extends Exception{
    public VMRuntimeException(){
        ThreadGroup parentThread;
        int totalThread = 0;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent()) {
            totalThread = parentThread.activeCount();
        }
        int byteToMb = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        long vmFree = rt.freeMemory() / byteToMb;
        long vmTotal = rt.totalMemory() / byteToMb;
        long vmUse = vmTotal - vmFree;
    }
    public VMRuntimeException(String filename,String message){
        ThreadGroup parentThread;
        int totalThread = 0;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent()) {
            totalThread = parentThread.activeCount();
        }
        int byteToMb = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        long vmFree = rt.freeMemory() / byteToMb;
        long vmTotal = rt.totalMemory() / byteToMb;
        long vmUse = vmTotal - vmFree;
        System.err.println("Exception in EXVM: "+message+"\n" +
                "UsageMemory:"+vmUse+"MB\n" +
                "VMThreads: "+totalThread+"\n" +
                "VMVersion: "+ Main.vm_version+"\n" +
                "ExecuteFile: "+filename);
    }
    public VMRuntimeException(String message) {
        ThreadGroup parentThread;
        int totalThread = 0;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent()) {
            totalThread = parentThread.activeCount();
        }
        int byteToMb = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        long vmFree = rt.freeMemory() / byteToMb;
        long vmTotal = rt.totalMemory() / byteToMb;
        long vmUse = vmTotal - vmFree;
        System.err.println("Exception in EXVM: "+message+"\n" +
                "UsageMemory:"+vmUse+"MB\n" +
                "VMThreads: "+totalThread+"\n" +
                "VMVersion: "+ Main.vm_version+"\n" +
                "ExecuteFile: <VMLoader>");
    }
}
