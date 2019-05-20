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
        if(o == null) return "null";
        String raw = (String)o;
        return raw.replaceAll("\n", "\\\\n");
    }

    @Override
    public Object Deserialize(String raw) {
        if(raw.equals("null")) return null;
        StringBuilder sb = new StringBuilder();
        String[] splited = raw.split("\\\\n");
        for(int i = 0; i < splited.length; i++){
            sb.append(splited[i]);
            if(i != splited.length - 1) sb.append('\n');
        }
        return sb.toString();
    }
}
