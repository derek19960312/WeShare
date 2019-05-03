package weshare.groupfour.derek.util;

public class RequestDataBuilder {

    private StringBuilder sb;

    public RequestDataBuilder() {
        sb = new StringBuilder();

    }

    public RequestDataBuilder build(){

        return new RequestDataBuilder();
    }

    public RequestDataBuilder setAction(String action){
        sb.append("action="+action+"&");
        return this;
    }

    public RequestDataBuilder setData(String key,String val){
        sb.append(key+"="+val+"&");
        return this;
    }



    public String create(){
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
