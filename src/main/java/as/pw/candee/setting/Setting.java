 package as.pw.candee.setting;
 import java.util.function.Predicate;
 public class Setting<T>
 {
     public final String name;
     public T value;
     public T maxValue;
     public T minValue;
     public Predicate<T> visible;
     public Setting(String name, T defaultValue, T maxValue, T minValue) {
         this.name = name;
         this.value = defaultValue;
         this.maxValue = maxValue;
         this.minValue = minValue;
     }
     public Setting(String name, T defaultValue) {
         this.name = name;
         this.value = defaultValue;
     }
     public Setting(String name, T defaultValue, Predicate<T> visible) {
         this.name = name;
         this.value = defaultValue;
         this.visible = visible;
     }
     public Setting(String name, T defaultValue, T maxValue, T minValue, Predicate<T> visible) {
         this.name = name;
         this.value = defaultValue;
         this.maxValue = maxValue;
         this.minValue = minValue;
         this.visible = visible;
     }
     public Setting(String name, Predicate<T> visible) {
         this.name = name;
         this.visible = visible;
     }
     public T getValue() {
         return this.value;
     }
     public boolean visible() {
         return (this.visible == null || this.visible.test(getValue()));
     }
     public void setValue(Object value) {
         this.value = (T)value;
     }
     public int currentEnum() {
         return EnumConverter.currentEnum((Enum)this.value);
     }
     public void increaseEnum() {
         this.value = (T)EnumConverter.increaseEnum((Enum)this.value);
     }
     public void setEnum(int index) {
         this.value = (T)EnumConverter.setEnum((Enum)this.value, index);
     }
 }