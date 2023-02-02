package ex.mcppl.compile.ast;

import ex.mcppl.compile.LexToken;
import ex.mcppl.compile.VMException;
import ex.mcppl.compile.parser.Element;
import ex.mcppl.vm.code.ByteCode;
import ex.mcppl.vm.code.GroupByteCode;
import ex.mcppl.vm.code.SetByteCode;

import java.util.ArrayList;

public class AstSetValue extends AstLeaf{
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
        return Element.AstType.SET;
    }

    public void setTds(ArrayList<LexToken.TokenD> tds) {
        this.tds = tds;
    }

    @Override
    public ByteCode eval() throws VMException {
        LexToken.TokenD td = getTokens();
        ArrayList<ByteCode> bcs = new ArrayList<>();
        if(!td.getToken().equals(LexToken.Token.KEY))throw new VMException("Unknown character in 'set' statement:"+td.getData());
        if(!Element.value_names.contains(td.getData())) throw new VMException("Not found value:"+td.getData());
        for(AstTree tree:this.children()){
            ByteCode bc = tree.eval();
            if(bc instanceof GroupByteCode){
                bcs.addAll(((GroupByteCode) bc).getBcs());
            }else bcs.add(bc);
        }

        return new SetByteCode(td.getData(),bcs);
    }
}
