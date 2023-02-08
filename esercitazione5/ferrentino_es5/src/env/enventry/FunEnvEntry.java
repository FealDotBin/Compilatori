package env.enventry;

import java.util.ArrayList;

public class FunEnvEntry extends EnvEntry {

    private String returnType;
    private ArrayList<ParamType> paramTypeList;

    public FunEnvEntry(String kind, String id, String returnType, ArrayList<ParamType> paramTypeList) {
        super(kind, id);
        this.returnType = returnType;
        this.paramTypeList = paramTypeList;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public ArrayList<ParamType> getParamTypeList() {
        return paramTypeList;
    }

    public void setParamTypeList(ArrayList<ParamType> paramTypeList) {
        this.paramTypeList = paramTypeList;
    }
}
