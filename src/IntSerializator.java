public class IntSerializator implements IPrimitiveSerializator {
    private final String[] names;
    public IntSerializator(){
        names = new String[2];
        names[0] = "int";
        names[1] = "java.lang.Integer";
    }
    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    public String Serialize(Object o) {
        return String.valueOf((int)o);
    }

    @Override
    public Object Deserialize(String raw) {
        return Integer.parseInt(raw);
    }
}
