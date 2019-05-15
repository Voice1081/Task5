public interface IPrimitiveSerializator {
    String getName();
    String Serialize(Object o);
    Object Deserialize(String raw);
}
