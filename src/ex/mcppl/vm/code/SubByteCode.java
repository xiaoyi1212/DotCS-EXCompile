package ex.mcppl.vm.code;

import ex.mcppl.vm.VMRuntimeException;
import ex.mcppl.vm.buf.ExDouble;
import ex.mcppl.vm.buf.ExInt;
import ex.mcppl.vm.buf.ExObject;
import ex.mcppl.vm.buf.ExValueName;
import ex.mcppl.vm.exe.Executor;

public class SubByteCode implements ByteCode{
    private boolean isYEPType(ExObject obj){
        return obj.getType().equals(ExObject.Type.INT)||
                obj.getType().equals(ExObject.Type.DOUBLE);
    }
    @Override
    public void exe(Executor executor) throws VMRuntimeException {
        ExObject obj = executor.getStack().pop();
        boolean isdouble = false;
        if((obj.getType().equals(ExObject.Type.INT)
                ||obj.getType().equals(ExObject.Type.DOUBLE))
                &&(executor.getStack().peek().getType().equals(ExObject.Type.INT)||
                executor.getStack().peek().getType().equals(ExObject.Type.DOUBLE))){

            if(obj.getType().equals(ExObject.Type.DOUBLE))isdouble = true;
            if(executor.getStack().peek().getType().equals(ExObject.Type.DOUBLE))isdouble = true;

            if(isdouble){
                double r = Double.parseDouble(obj.getData()) - Double.parseDouble(executor.getStack().peek().getData());
                executor.getStack().push(new ExDouble(r));
            }else {
                int r = Integer.parseInt(obj.getData()) - Integer.parseInt(executor.getStack().peek().getData());
                executor.getStack().push(new ExInt(r));
            }

        }else if(obj.getType().equals(ExObject.Type.OBJECT)&&isYEPType(executor.getStack().peek())){
            double d = Double.parseDouble(((ExValueName)obj).getValue().getData());
            executor.push(new ExDouble(d-Double.parseDouble(executor.getStack().peek().getData())));
        }else if(executor.getStack().peek().getType().equals(ExObject.Type.OBJECT)&&isYEPType(obj)) {
            double d = Double.parseDouble(((ExValueName)executor.getStack().peek()).getValue().getData());
            executor.push(new ExDouble(d-Double.parseDouble(obj.getData())));
        }else if(obj.getType().equals(ExObject.Type.OBJECT)&&executor.getStack().peek().getType().equals(ExObject.Type.OBJECT)){
            double d = Double.parseDouble(((ExValueName)executor.getStack().peek()).getValue().getData());
            double a = Double.parseDouble(((ExValueName)obj).getValue().getData());
            executor.push(new ExDouble(d-a));
        }else throw new VMRuntimeException("Unknown type value can't cast Integer/Double");
    }
}
