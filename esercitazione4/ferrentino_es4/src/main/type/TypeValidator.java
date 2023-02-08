package main.type;

public class TypeValidator {

    String toValidate;

    public TypeValidator(String toValidate) {
        this.toValidate = toValidate;
    }

    public boolean validateBasicType(){
        for(String el : Type.basicTypes){
            if(toValidate.equals(el))
                return true;
        }
        return false;
    }

    public boolean validateVar(){
        if(toValidate.equals(Type.VAR))
            return true;
        return false;
    }

    public boolean validateVoid(){
        if(toValidate.equals(Type.VOID))
            return true;
        return false;
    }
}
