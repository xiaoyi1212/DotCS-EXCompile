package ex.mcppl.compile.parser;

import ex.mcppl.compile.LexToken;
import ex.mcppl.compile.VMException;
import ex.mcppl.compile.ast.*;
import ex.mcppl.vm.VMRuntimeException;
import ex.mcppl.vm.buf.ExString;
import ex.mcppl.vm.code.ByteCode;
import ex.mcppl.vm.code.PushByteCode;

import java.util.ArrayList;

public class BasicParser {
    ArrayList<LexToken.TokenD> tds;
    int index;

    public BasicParser(ArrayList<LexToken.TokenD> tds){
        this.tds = tds;
    }

    private LexToken.TokenD getToken(){
        if(index >= tds.size()) return null;
        LexToken.TokenD t = tds.get(index);
        index += 1;
        return t;
    }

    private AstLeaf getLeaf() throws VMException{
        AstLeaf leaf;
        LexToken.TokenD td = getToken();
        try {

            if (td == null) return null;
            if (td.getToken().equals(LexToken.Token.NAME)) {
                if (td.getData().equals("include")) {
                    ArrayList<LexToken.TokenD> tds = new ArrayList<>();
                    do {
                        td = getToken();
                        tds.add(td);
                    } while (!td.getToken().equals(LexToken.Token.END));
                    leaf = new AstIncludeTree();
                    ((AstIncludeTree)leaf).setTds(tds);
                    return leaf;
                } else if (td.getData().equals("exe")) {
                    leaf = new AstInvokeTree();
                    ArrayList<LexToken.TokenD> tds = new ArrayList<>();
                    do {
                        td = getToken();
                        tds.add(td);
                    } while (!td.getToken().equals(LexToken.Token.END));
                    ((AstInvokeTree)leaf).setTds(tds);
                    return leaf;
                } else if (td.getData().equals("function")) {
                    leaf = new AstFunctionTree();
                    ArrayList<LexToken.TokenD> tds = new ArrayList<>();
                    do {
                        td = getToken();
                        if (td.getToken().equals(LexToken.Token.LP) && td.getData().equals("{")) break;
                        tds.add(td);
                    } while (true);
                    ((AstFunctionTree) leaf).setTds(tds);
                    for(AstTree at:getBlockGroup(td)){
                        leaf.children().add(at);
                    }
                    return leaf;
                }else if (td.getData().equals("value")) {
                    leaf = new AstValueTree();
                    ArrayList<LexToken.TokenD> tds = new ArrayList<>();
                    do {
                        td = getToken();
                        tds.add(td);
                    } while (!(td.getToken().equals(LexToken.Token.SEM) && td.getData().equals("=")));
                    td = getToken();
                    ((AstValueTree) leaf).setTds(tds);

                    leaf.children().add(getValueI(td));
                    return leaf;
                } else if (td.getData().equals("if")) {
                    leaf = new AstIfStatement();
                    td = getToken();
                    if (!(td.getToken().equals(LexToken.Token.LP) && td.getData().equals("("))) throw new VMException("The If statement must be followed by '('");
                    getIf((AstIfStatement) leaf);
                    return leaf;
                } else if (td.getData().equals("while")) {
                    /*
                    leaf = new AstLeaf(td);
                    leaf.type = Element.AstType.WHILE;
                    td = getToken();
                    if (!(td.getToken().equals(LexToken.Token.LP) && td.getData().equals("(")))
                        throw new VMException("The While statement must be followed by '('");
                    getIf(leaf, td);
                    return leaf;

                     */
                    return new AstIfStatement();
                }else if(td.getData().equals("set")){
                    leaf = new AstSetValue();
                    ArrayList<LexToken.TokenD> tds = new ArrayList<>();
                    do {
                        td = getToken();
                        tds.add(td);
                    } while (!(td.getToken().equals(LexToken.Token.SEM) && td.getData().equals("=")));
                    td = getToken();
                    ((AstSetValue) leaf).setTds(tds);

                    leaf.children().add(getValueI(td));
                    return leaf;
                } else throw new VMException("error in parser :" + td);
            } else throw new VMException("Unknown lexin parser:" + td);
        }catch (Exception npe){
            npe.printStackTrace();
            throw new VMException("The statement must be ended with ';'");
        }
    }

    private AstLeaf getIf(AstIfStatement leaf){
        LexToken.TokenD td;
        int lpbuf = 1;
        ArrayList<LexToken.TokenD> ccc = new ArrayList<>();
        ArrayList<LexToken.TokenD> tdd = new ArrayList<>();
        do {
            td = getToken();
            if (td.getToken().equals(LexToken.Token.LP) && td.getData().equals("(")) lpbuf += 1;
            if (td.getToken().equals(LexToken.Token.LR) && td.getData().equals(")")) lpbuf -= 1;
            tdd.add(td);
        }while (lpbuf != 0);
        leaf.setBool(tdd);

        do {
            td = getToken();
            ccc.add(td);
        }while (!td.getToken().equals(LexToken.Token.END));
        leaf.setTds(ccc);
        return leaf;
    }

    public void rust(String file_name) throws VMException, VMRuntimeException {
        AstLeaf leaf;
        while ((leaf = getLeaf())!=null)Element.getHead().children().add(leaf);
        Element.parser(file_name);
    }
    private ArrayList<AstTree> getBlockGroup(LexToken.TokenD tdt) throws VMException{
        int lpbuf = 0;if(tdt.getToken().equals(LexToken.Token.LP)&&tdt.getData().equals("{"))lpbuf += 1;
        ArrayList<AstTree> ats = new ArrayList<>();

        do{
            tdt = getToken();
            if(tdt == null)return ats;
            if(tdt.getToken().equals(LexToken.Token.LP)&&tdt.getData().equals("{"))lpbuf += 1;
            if(tdt.getToken().equals(LexToken.Token.LR)&&tdt.getData().equals("}"))lpbuf -= 1;
            if(tdt.getToken().equals(LexToken.Token.NAME)){
                if(tdt.getData().equals("exe")){
                    AstInvokeTree leaf1  = new AstInvokeTree();
                    ArrayList<LexToken.TokenD> td = new ArrayList<>();
                    do {
                        tdt = getToken();
                        td.add(tdt);
                    }while (!tdt.getToken().equals(LexToken.Token.END));
                    leaf1.setTds(td);
                    ats.add(leaf1);
                }else if(tdt.getData().equals("value")) {
                    AstValueTree leaf = new AstValueTree();
                    ArrayList<LexToken.TokenD> tds = new ArrayList<>();
                    do {
                        tdt = getToken();
                        tds.add(tdt);
                    } while (!(tdt.getToken().equals(LexToken.Token.SEM) && tdt.getData().equals("=")));
                    tdt = getToken();
                    ((AstValueTree) leaf).setTds(tds);

                    leaf.children().add(getValueI(tdt));
                    ats.add(leaf);
                }else if(tdt.getData().equals("set")){
                    AstSetValue leaf = new AstSetValue();
                    ArrayList<LexToken.TokenD> tds = new ArrayList<>();
                    do {
                        tdt = getToken();
                        tds.add(tdt);
                    } while (!(tdt.getToken().equals(LexToken.Token.SEM) && tdt.getData().equals("=")));
                    tdt = getToken();
                    ((AstSetValue) leaf).setTds(tds);

                    leaf.children().add(getValueI(tdt));
                    ats.add(leaf);
                }else if(tdt.getData().equals("if")) {
                    AstLeaf leaf1  = new AstLeaf(tdt);
                    leaf1.type = Element.AstType.IF;
                    tdt = getToken();
                    if (!(tdt.getToken().equals(LexToken.Token.LP) && tdt.getData().equals("(")))
                        throw new VMException("The If statement must be followed by '('");
                    //getIf(leaf1);
                    ats.add(leaf1);
                }else if(tdt.getData().equals("while")){
                    AstLeaf leaf1  = new AstLeaf(tdt);
                    leaf1.type = Element.AstType.WHILE;
                    tdt = getToken();
                    if (!(tdt.getToken().equals(LexToken.Token.LP) && tdt.getData().equals("(")))
                        throw new VMException("The While statement must be followed by '('");
                    //getIf(leaf1);
                    ats.add(leaf1);
                }else throw new VMException("error in parser :"+tdt);
            }
        }while (lpbuf!=0);

        return ats;
    }

    private AstLeaf getValueI(LexToken.TokenD tdt) throws VMException{
        LexToken.TokenD td = tdt;
        AstLeaf leaf;
        ArrayList<LexToken.TokenD> tdtt = new ArrayList<>();
        if(td.getToken().equals(LexToken.Token.DOUBLE)||td.getToken().equals(LexToken.Token.KEY)||td.getToken().equals(LexToken.Token.NUM)) leaf = new AstNBLTree();
        else if(td.getToken().equals(LexToken.Token.NAME)) {
            if (td.getData().equals("exe")) leaf = new AstInvokeTree();
            else if (td.getData().equals("true") || td.getData().equals("false")) leaf = new AstBoolStatement();
            else throw new VMException("Create value error: Unknown variable initializer definition.");
        }else if(td.getToken().equals(LexToken.Token.STR)) {
            LexToken.TokenD finalTd = td;
            leaf = new AstLeaf() {
                @Override
                public ByteCode eval() throws VMException {
                    return new PushByteCode(new ExString(finalTd.getData()));
                }
            };
        }else if(td.getToken().equals(LexToken.Token.LP)) {
            leaf = new AstBoolStatement();
            tdtt.add(td);
        } else throw new VMException("Create value error: Unknown variable initializer definition.");


        if(leaf instanceof AstNBLTree)tdtt.add(td);
        if(leaf instanceof AstBoolStatement)tdtt.add(td);
        do {
            td = getToken();
            if(td.getToken().equals(LexToken.Token.END))break;
            tdtt.add(td);
        } while (true);

        if(leaf instanceof AstBoolStatement) ((AstBoolStatement) leaf).setTds(tdtt);
        if(leaf instanceof AstInvokeTree)((AstInvokeTree) leaf).setTds(tdtt);

        if(leaf instanceof AstNBLTree) ((AstNBLTree) leaf).setTds(tdtt);

        return leaf;
    }
}
