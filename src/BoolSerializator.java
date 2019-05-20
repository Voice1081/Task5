public class BoolSerializator implements IPrimitiveSerializator {
    private final String[] names;

    public BoolSerializator() {
        names = new String[2];
        names[0] = "java.lang.Boolean";
        names[1] = "bool";
    }

    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    public String Serialize(Object o) {
        return o.toString();
    }

    @Override
    public Object Deserialize(String raw) {
        return raw.contains("true");
    }
}
