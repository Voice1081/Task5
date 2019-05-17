public class StringSerializator implements IPrimitiveSerializator{
    private final String[] names;
    @Override
    public String[] getNames() {
        return names;
    }

    public StringSerializator(){
        names = new String[1];
        names[0] = "java.lang.String";
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
