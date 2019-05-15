import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.lang.reflect.Field;

public class Serializator<T> {
    private final HashMap<String, IPrimitiveSerializator> primitives;

    public Serializator() {
        primitives = new HashMap<>();
    }

    public void register(IPrimitiveSerializator serializator){
        primitives.put(serializator.getName(), serializator);
    }

    private String SerializeToString(Object o){
        if(o == null) return "null";
        String serialized;
        StringBuilder serializedSB = new StringBuilder();
        Class oClass = o.getClass();
        String oClassName = oClass.getName();
        if(primitives.containsKey(oClassName)) serialized =  primitives.get(oClassName).Serialize(o);
        else{
            serializedSB.append('{');
            serializedSB.append('\n');
            serializedSB.append(oClassName);
            serializedSB.append('\n');
            Field[] oFields = oClass.getDeclaredFields();
            for(Field field:oFields){
                field.setAccessible(true);;
                String fieldName = field.getName();
                Object fieldValue = null;
                try {
                    fieldValue = field.get(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                String fieldClass = field.getType().getName();
                serializedSB.append(fieldClass);
                serializedSB.append(' ');
                serializedSB.append(fieldName);
                serializedSB.append('\n');
                serializedSB.append(new String(Serialize(fieldValue)));
                serializedSB.append('\n');
            }
            serializedSB.append('}');
            serialized = serializedSB.toString();
        }
        return serialized;
    }

    public byte[] Serialize(Object o){
        return SerializeToString(o).getBytes(StandardCharsets.UTF_8);
    }

    public T Deserialize(byte[] raw){
        return (T)DeserializeToObject(raw);
    }

    private Object DeserializeToObject(byte[] raw){
        return DeserializeToObject(new String(raw));
    }
    private Object DeserializeToObject(String raw){
        String[] lines = raw.split("\\R");
        Class oClass = null;
        try {
            oClass = Class.forName(lines[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Object o = oClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for(int i = 1; i < lines.length; i++){

        }
        return null;
    }
}
