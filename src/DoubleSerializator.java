public class DoubleSerializator implements IPrimitiveSerializator {
    private final String[] names;

    public DoubleSerializator() {
        names = new String[2];
        names[0] = "java.lang.Double";
        names[1] = "double";
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
        return Double.parseDouble(raw);
    }
}
