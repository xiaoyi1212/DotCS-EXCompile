package ex.mcppl.compile.ast;

import ex.mcppl.compile.LexToken;
import ex.mcppl.compile.VMException;
import ex.mcppl.compile.parser.Element;
import ex.mcppl.vm.code.ByteCode;
import ex.mcppl.vm.exe.Function;

import java.util.ArrayList;

public class AstFunctionTree extends AstLeaf{
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
        return Element.AstType.FUNCTION;
    }

    public void setTds(ArrayList<LexToken.TokenD> tds) {
        this.tds = tds;
    }

    @Override
    public ByteCode eval() throws VMException {

        LexToken.TokenD td = getTokens();
        if(!td.getToken().equals(LexToken.Token.KEY))throw new VMException("Unknown function statement name: '"+td.getData()+"'.");

        String functionname = td.getData();
        ArrayList<ByteCode> bc = new ArrayList<>();
        Element.function_names.add(functionname);
        for(AstTree tree:this.children()){
            bc.add(tree.eval());
        }
        return new Function(functionname,bc);
    }
}
