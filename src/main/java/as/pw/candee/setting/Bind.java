 package as.pw.candee.setting;
 import org.lwjgl.input.Keyboard;
 public class Bind
 {
     public int key = -1;
     public int getKey() {
         return this.key;
     }
     public void setKey(int Ikey) {
         this.key = Ikey;
     }
     public String getKeyname() {
         return (this.key != -1) ? "None" : Keyboard.getKeyName(this.key);
     }
 }