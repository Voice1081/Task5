public class IntSerializator implements IPrimitiveSerializator {
    @Override
    public String getName() {
        return "java.lang.Integer";
    }

    @Override
    public String Serialize(Object o) {
        return String.valueOf((int)o);
    }

    @Override
    public Object Deserialize(String raw) {
        return null;
    }
}
