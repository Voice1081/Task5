public interface IPrimitiveSerializator {
    String[] getNames();
    String Serialize(Object o);
    Object Deserialize(String raw);
}
