package com.rialms.assessment

/**
 * Created with IntelliJ IDEA.
 * User: relango
 * Date: 11/28/12
 * Time: 11:33 PM
 * To change this template use File | Settings | File Templates.
 */
class ValidationResult {

    public enum Type {ERROR, WARNING, INFO}

    private EnumMap<Type,List<String>> result;

    public ValidationResult(){
        result = new EnumMap<Type, List<String>>(Type.class);
        Type.values().each {t-> result[t] = []}
    }

    public ValidationResult(org.qtitools.qti.validation.ValidationResult qtiResult){
        this();
        result[Type.ERROR] = qtiResult.errors.collect() {it.toString()};
        result[Type.WARNING] = qtiResult.warnings.collect() {it.toString()};
        result[Type.INFO] = qtiResult.infos.collect() {it.toString()};
    }

    public List<String> getErrors(){
        return result[Type.ERROR];
    }

    public List<String> getWarnings(){
        return result[Type.WARNING];
    }

    public List<String> getInfos(){
        return result[Type.INFO];
    }

    public void add(Type type, String result){
        this.result[type].add(result);
    }

    public boolean isSuccess(){
        return result.isEmpty();
    }

}
