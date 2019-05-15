public class StringSerializator implements IPrimitiveSerializator{
    @Override
    public String getName() {
        return "java.lang.String";
    }

    @Override
    public String Serialize(Object o) {
        return (String)o;
    }

    @Override
    public Object Deserialize(String raw) {
        if(raw.equals("null")) return null;
        return raw;
    }
}
