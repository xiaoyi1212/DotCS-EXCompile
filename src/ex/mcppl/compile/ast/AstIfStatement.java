package ex.mcppl.compile.ast;

import ex.mcppl.compile.LexToken;
import ex.mcppl.compile.VMException;
import ex.mcppl.compile.nbl.BoolExpression;
import ex.mcppl.compile.parser.Element;
import ex.mcppl.vm.code.*;

import java.util.ArrayList;

public class AstIfStatement extends AstLeaf{
    private ArrayList<LexToken.TokenD> tds;
    private ArrayList<LexToken.TokenD> bool;
    private int index = 0;
    private int bool_index = 0;

    private LexToken.TokenD getTokens(){
        if(index > tds.size())return null;
        LexToken.TokenD td = tds.get(index);
        index += 1;
        return td;
    }
    private LexToken.TokenD getBoolTokens(){
        if(bool_index > bool.size())return null;
        LexToken.TokenD td = bool.get(bool_index);
        bool_index += 1;
        return td;
    }

    @Override
    public Element.AstType getType() {
        return Element.AstType.IF;
    }

    public void setTds(ArrayList<LexToken.TokenD> tds) {
        this.tds = tds;
    }

    public void setBool(ArrayList<LexToken.TokenD> bool) {
        this.bool = bool;
    }

    @Override
    public ByteCode eval() throws VMException {
        LexToken.TokenD td = getBoolTokens();
        bool.remove(bool.size()-1);boolean isbool = false;

        for(LexToken.TokenD t:bool)
            if (t.getToken().equals(LexToken.Token.SEM) || t.getToken().equals(LexToken.Token.LP)) {
                isbool = true;
                break;
            }

        if(!isbool){
            if(td.getData().equals("true")) {
                AstInvokeTree invoke = new AstInvokeTree();
                if(!(tds.get(0).getToken().equals(LexToken.Token.NAME)&&tds.get(0).getData().equals("exe")))throw new VMException("Unknown lex in if statement.");
                tds.remove(0);
                invoke.setTds(tds);
                return invoke.eval();
            }else if(td.getData().equals("false")){
                return new NolByteCode();
            }else throw new VMException("Cannot use keywords in Boolean expressions in IF statements.");
        }else if(isbool){
            ArrayList<ByteCode> bc = new ArrayList<>();
            AstInvokeTree in = new AstInvokeTree();

            tds.remove(0);

            in.setTds(tds);
            bc.add(in.eval());
            return new JneByteCode(new GroupByteCode(BoolExpression.calculate(BoolExpression.parseBoolExpr(bool))),new GroupByteCode(bc));
        }else throw new VMException("Cannot in if statement boolean use key.");
    }
}
