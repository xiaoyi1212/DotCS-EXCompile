package ex.mcppl;

import ex.mcppl.compile.LexToken;
import ex.mcppl.compile.VMException;
import ex.mcppl.compile.parser.BasicParser;
import ex.mcppl.vm.VMloader;
import ex.mcppl.vm.lib.LibLoader;

import java.util.ArrayList;

public class Main {
    public static final String vm_version = "EXVirtualMachine-v0.2.5:"+System.getProperty("java.vm.name")+"-"+System.getProperty("java.vm.specification.version");

    public static boolean isKey(String data){
        return data.equals("exe")||
                data.equals("if")||
                data.equals("while")||
                data.equals("function")||
                data.equals("value")||
                data.equals("include")||
                data.equals("false")||
                data.equals("true")||
                data.equals("set")||
                data.equals("this");
    }
    public static void main(String[] args) throws Exception{
        try {

            if(args.length == 0)throw new VMException("No have input filename.");
            String file_name = args[0];


            /*
            String file_name = "debug.exf";

             */
            ArrayList<LexToken.TokenD> tds = new LexToken(file_name).launch();

            ArrayList<LexToken.TokenD> buf = new ArrayList<>();
            for (LexToken.TokenD td : tds) {
                if (td.getToken().equals(LexToken.Token.TEXT)) continue;
                buf.add(td);
            }
            LibLoader.init();
            new BasicParser(buf).rust(file_name);
            System.gc();

            VMloader.launch();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
