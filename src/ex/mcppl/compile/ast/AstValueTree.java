package ex.mcppl.compile.ast;

import ex.mcppl.compile.LexToken;
import ex.mcppl.compile.VMException;
import ex.mcppl.compile.parser.Element;
import ex.mcppl.vm.code.ByteCode;
import ex.mcppl.vm.code.GroupByteCode;
import ex.mcppl.vm.code.MovByteCode;

import java.util.ArrayList;

public class AstValueTree extends AstLeaf{

    ArrayList<LexToken.TokenD> tds;

    private int index = 0;

    private LexToken.TokenD getTokens(){
        if(index > tds.size())return null;
        LexToken.TokenD td = tds.get(index);
        index += 1;
        return td;
    }

    public AstValueTree(){
    }

    public void setTds(ArrayList<LexToken.TokenD> tds) {
        this.tds = tds;
    }

    @Override
    public Element.AstType getType() {
        return Element.AstType.VALUE;
    }

    @Override
    public ByteCode eval() throws VMException {
        String name,text;ArrayList<ByteCode> bcs = new ArrayList<>();
        LexToken.TokenD td = getTokens();
        if(!td.getToken().equals(LexToken.Token.KEY))throw new VMException("Variable names must not be defined with other types.");
        name = td.getData();td = getTokens();
        if(!(td.getToken().equals(LexToken.Token.SEM)&&td.getData().equals(":")))throw new VMException("Unknown lex in value creator.");
        text = td.getData();
        if(Element.value_names.contains(name))throw new VMException("Cannot define variables with the same variable name.");



        for(AstTree tree:this.children()){

            ByteCode bc = tree.eval();

            if(bc instanceof GroupByteCode){
                bcs.addAll(((GroupByteCode) bc).getBcs());
            }else bcs.add(bc);
        }
        Element.value_names.add(name);
        return new MovByteCode(name,text,bcs);// new MovByteCode();
    }
}
