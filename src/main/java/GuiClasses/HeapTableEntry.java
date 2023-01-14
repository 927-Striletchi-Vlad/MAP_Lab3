package GuiClasses;

import javafx.beans.property.SimpleStringProperty;

public class HeapTableEntry {
    private final SimpleStringProperty address;
    private final SimpleStringProperty value;

    public HeapTableEntry(String address, String value){
        this.address = new SimpleStringProperty(address);
        this.value = new SimpleStringProperty(value);
    }

    public String getAddress(){
        return address.get();
    }

    public String getValue(){
        return value.get();
    }

    public void setAddress(String address){
        this.address.set(address);
    }

    public void setValue(String value){
        this.value.set(value);
    }
}
