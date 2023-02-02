package ex.mcppl.compile.ast;

import ex.mcppl.compile.LexToken;
import ex.mcppl.compile.VMException;
import ex.mcppl.compile.parser.Element;
import ex.mcppl.vm.buf.ExDouble;
import ex.mcppl.vm.buf.ExInt;
import ex.mcppl.vm.buf.ExObject;
import ex.mcppl.vm.buf.ExString;
import ex.mcppl.vm.buf.ExValueName;
import ex.mcppl.vm.code.ByteCode;
import ex.mcppl.vm.code.InvokeByteCode;
import ex.mcppl.vm.lib.LibLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class AstInvokeTree extends AstLeaf{
    private ArrayList<LexToken.TokenD> tds;
    private int index = 0;

    private LexToken.TokenD getTokens(){
        if(index > tds.size())return null;
        LexToken.TokenD td = tds.get(index);
        index += 1;
        return td;
    }

    @Override
    public Element.AstType getType() {
        return Element.AstType.INVOKE;
    }

    public void setTds(ArrayList<LexToken.TokenD> tds) {
        this.tds = tds;
    }

    @Override
    public ByteCode eval() throws VMException{

        String lib = null;
        String function = null;
        HashMap<String,ExObject> values = new HashMap<>();
        LexToken.TokenD td = getTokens();
        if(!(td.getToken().equals(LexToken.Token.SEM)&&td.getData().equals(".")))throw new VMException("Unknown parser in invoke function");td = getTokens();
        if((td.getToken().equals(LexToken.Token.KEY)&&LibLoader.isLibs(td.getData()))||(td.getToken().equals(LexToken.Token.NAME)&&td.getData().equals("this"))) lib = td.getData();
        if(lib==null)throw new VMException("Unknown library name:"+td.getData());
        td = getTokens();

        if(!(td.getToken().equals(LexToken.Token.SEM)&&td.getData().equals(".")))throw new VMException("Unknown parser in invoke function");td = getTokens();

        if(td.getToken().equals(LexToken.Token.KEY)&&LibLoader.isFunction(lib,td.getData())) function = td.getData();
        else if(td.getToken().equals(LexToken.Token.KEY)&&Element.function_names.contains(td.getData())) function = td.getData();
        if(function==null)throw new VMException("Unknown function name.");
        td = getTokens();
        if(!(td.getToken().equals(LexToken.Token.LP)&&td.getData().equals("(")))throw new VMException("Unknown parser in invoke function");
        td = getTokens();

        try {
            do {
                String l = null;
                ExObject f = null;
                do {
                    if (td.getToken().equals(LexToken.Token.LR) && td.getData().equals(")")) break;
                    if (td.getToken().equals(LexToken.Token.KEY)) l = td.getData();
                    td = getTokens();
                    if (!(td.getToken().equals(LexToken.Token.SEM) && td.getData().equals(":"))) throw new VMException("Unknown function value parser");
                    td = getTokens();

                    if (td.getToken().equals(LexToken.Token.STR)) f = new ExString(td.getData());
                    else if (td.getToken().equals(LexToken.Token.NUM)) f = new ExInt(Integer.parseInt(td.getData()));
                    else if (td.getToken().equals(LexToken.Token.DOUBLE))
                        f = new ExDouble(Double.parseDouble(td.getData()));
                    else if (td.getToken().equals(LexToken.Token.KEY)) f = new ExValueName(td.getData());
                    else throw new VMException("Unknown value type.");
                    td = getTokens();
                } while (!((td.getToken().equals(LexToken.Token.SEM) && td.getData().equals(",")) || (td.getToken().equals(LexToken.Token.LR) && td.getData().equals(")"))));
                values.put(l, f);
                td = getTokens();
            } while (!(td.getToken().equals(LexToken.Token.LR) && td.getData().equals(")")));
        }catch (IndexOutOfBoundsException e){
        }catch (Exception e){
            e.printStackTrace();
            throw new VMException("Unknown parser error.");
        }

        return new InvokeByteCode(lib,function,values);
    }
}
