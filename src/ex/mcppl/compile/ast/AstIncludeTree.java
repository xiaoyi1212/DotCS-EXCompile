package ex.mcppl.compile.ast;

import ex.mcppl.compile.LexToken;
import ex.mcppl.compile.VMException;
import ex.mcppl.compile.parser.Element;
import ex.mcppl.vm.code.ByteCode;
import ex.mcppl.vm.code.ImpByteCode;
import ex.mcppl.vm.lib.LibLoader;

import java.util.ArrayList;

public class AstIncludeTree extends AstLeaf{
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
        return Element.AstType.INCLUDE;
    }

    public void setTds(ArrayList<LexToken.TokenD> tds) {
        this.tds = tds;
    }

    @Override
    public ByteCode eval() throws VMException {
        LexToken.TokenD td = getTokens();
        if(!td.getToken().equals(LexToken.Token.STR))throw new VMException("Unknown lib name type.");
        if(!LibLoader.isLibs(td.getData()))throw new VMException("Unknown library name.");
        String lib = td.getData();

        return new ImpByteCode(lib);
    }
}
