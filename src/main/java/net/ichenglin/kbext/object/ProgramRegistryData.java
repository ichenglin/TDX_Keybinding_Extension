package net.ichenglin.kbext.object;

public class ProgramRegistryData implements Comparable<ProgramRegistryData> {
    private String data_key;
    private Object data_value;

    public ProgramRegistryData(String data_key, Object data_value) {
        this.data_key = data_key;
        this.data_value = data_value;
    }

    public String get_key() {
        return this.data_key;
    }

    public Object get_value() {
        return this.data_value;
    }

    @Override
    public int compareTo(ProgramRegistryData object) {
        return this.get_key().compareTo(object.get_key());
    }
}
