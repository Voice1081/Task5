import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.lang.reflect.Field;

public class Serializator<T> {
    private final HashMap<String, IPrimitiveSerializator> primitives;

    public Serializator() {
        primitives = new HashMap<>();
    }

    public void register(IPrimitiveSerializator serializator){
        String[] names = serializator.getNames();
        for(String name : names) primitives.put(name, serializator);
    }

    private String SerializeToString(Object o, int level){
        if(o == null) return "null";
        String serialized;
        StringBuilder serializedSB = new StringBuilder();
        if(level == 0) {
            serializedSB.append("deserializable");
            serializedSB.append('\n');
        }

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
                serializedSB.append(SerializeToString(fieldValue, level+1));
                serializedSB.append('\n');
            }
            serializedSB.append('}');
            serialized = serializedSB.toString();
        }
        return serialized;
    }

    public byte[] Serialize(Object o){
        return SerializeToString(o,0).getBytes(StandardCharsets.UTF_8);
    }

    public T Deserialize(byte[] raw) throws DeserializeException {
        return (T)DeserializeToObject(raw);
    }

    private Object DeserializeToObject(byte[] raw) throws DeserializeException {
        return DeserializeToObject(new String(raw), 0);
    }
    private Object DeserializeToObject(String raw, int level) throws DeserializeException {
        String[] lines = raw.split("\\R");
        if(level == 0 && !lines[0].equals("deserializable")) throw new DeserializeException("Not deserializable");
        int classLine = 1;
        if (lines[1].equals("{")) classLine = 2;
        Class oClass = null;
        Object o = null;
        try {
            oClass = Class.forName(lines[classLine]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            o = oClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for(int i = classLine + 1; i < lines.length; i++){
            String[] splited = lines[i].split(" ");
            if(splited.length < 2) continue;
            Field field = null;
            try {
                field = oClass.getDeclaredField(splited[1]);
                field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if(primitives.containsKey(splited[0])){
                Object value = primitives.get(splited[0]).Deserialize(lines[i+1]);
                try {
                    field.set(o, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                i += 1;
            }
            else if(lines[i+1].equals("null")){
                try {
                    field.set(o, null);
                    i += 1;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            else if(lines[i+1].equals("{")){
                Tuple<String, Integer> descrAndLength = getRefDescription(lines, i+1);
                Object value = DeserializeToObject(descrAndLength.x, level + 1);
                try {
                    field.set(o, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                i += descrAndLength.y;
            }
        }
        return o;
    }

    private Tuple<String, Integer> getRefDescription(String[] lines, int pos){
        StringBuilder description = new StringBuilder();
        description.append(lines[pos]);
        description.append('\n');
        pos += 1;
        int counter = 1;
        while(counter != 0 && pos < lines.length){
            description.append(lines[pos]);
            description.append('\n');
            if(lines[pos].equals("{")) counter += 1;
            else if(lines[pos].equals("}")) counter -= 1;
            pos += 1;
        }
        return new Tuple<>(description.toString(), pos);
    }
}
