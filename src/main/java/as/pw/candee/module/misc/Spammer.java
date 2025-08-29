 package as.pw.candee.module.misc;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.MathUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.network.play.client.CPacketChatMessage;
 public class Spammer extends Module {
     public final Setting<Float> delay;
     public final Setting<type> spam;
     public final Setting<Boolean> suffix;
     public Timer timer;
     public final String chinese = "这是一首爱情之歌吗？ 或者是一首关于梦想的歌？无论怎样，他们都很难追上。 而我们则继续徘徊。 梦想总是在后面。 只有当我们追上他们时，我们才能从正面看到他们。 只有到那时，我们才意识到这张脸是我们自己的。 \"纯洁就像玫瑰，美丽而刺眼。 净化你的梦想 ............. 你的追捕行动将持续多久？";
     public final String korean = "풀 쿤 기 주 쿄 두르 고 친 멈칫 부상 흑인 폰 델 르 응용 세 우고 트 료케 가스 다이루 진의 정 만데 오예 크루 고 가스 눈쿠 모스 문 송 승기 조아세。국도 추증 바다에서 가시 광선이 웃음. 비스듬히 뒤쪽의 열병은 퇴고를 거듭한다.\n";
     public final String[] amongus;
     public int index;
     public Spammer() {
         super("Spammer", Categories.MISC, false, false);
         this.delay = register(new Setting("Delay", 50.0F, 100.0F, 1.0F));
         this.spam = register(new Setting("Type", type.chinese));
         this.suffix = register(new Setting("Suffix", Boolean.TRUE));
         this.timer = new Timer();
         this.amongus = new String[] { "⠄⠄⠄⠄⠄⠄⠄⣀⣀⣐⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢠⠄⣠⣶⣿⣿⣿⠿⠿⣛⣂⣀⣀⡒⠶⣶⣤⣤⣬⣀⡀⠄⢀⠄⠄⠄⠄⠄⠄⠄⠄⠄⢀⣾⣿⣿⣿⡟⢡⢾⣿⣿⣿⣿⣿⣿⣶⣌⠻⣿⣿⣿⣿⣷⣦⣄⡀⠄⠄⠄⠄⠄⠄⠄⣈⣉⡛⣿⣿⣿⡌⢇⢻⣿⣿⣿⣿⣿⠿⠛⣡⣿⣿⣿⣿⣿⣿⣿⣿⣦⣄⠄⠄⠄⠄⠺⠟⣉⣴⡿⠛⣩⣾⣎⠳⠿⠛⣋⣩⣴⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⠄⠄⠄⠄⠄⠘⢋⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡆⠄⠄⠄⢀⢀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⣀⠄⠄⠄⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠘⠛⠄⠄⠄⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠋⣀⣀⣠⣤⠄⠄⣀⣀⡙⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢛⣩⠤⠾⠄⠛⠋⠉⢉⠄⠺⠿⠛⠛⠃⠄⠉⠙⠛⠛⠻⠿⠿⠿⠟⠛⠛⠛⠉⠁⠄⠄⣀⣀⣠⣤⣠⣴⣶⣼⣿" };
         this.index = 0;
     }
     public void onEnable() {
         this.index = 0;
     }
     public void onUpdate() {
         if (this.timer == null) {
             this.timer = new Timer();
         }
         if (this.timer.passedDms(this.delay.getValue())) {
             this.timer.reset();
             String msg = "";
             if (this.spam.getValue() == type.chinese) {
                 msg = msg + "这是一首爱情之歌吗？ 或者是一首关于梦想的歌？无论怎样，他们都很难追上。 而我们则继续徘徊。 梦想总是在后面。 只有当我们追上他们时，我们才能从正面看到他们。 只有到那时，我们才意识到这张脸是我们自己的。 \"纯洁就像玫瑰，美丽而刺眼。 净化你的梦想 ............. 你的追捕行动将持续多久？";
             }
             if (this.spam.getValue() == type.korean) {
                 msg = msg + "풀 쿤 기 주 쿄 두르 고 친 멈칫 부상 흑인 폰 델 르 응용 세 우고 트 료케 가스 다이루 진의 정 만데 오예 크루 고 가스 눈쿠 모스 문 송 승기 조아세。국도 추증 바다에서 가시 광선이 웃음. 비스듬히 뒤쪽의 열병은 퇴고를 거듭한다.\n";
             }
             if (this.spam.getValue() == type.amongus) {
                 msg = msg + this.amongus[this.index];
                 this.index++;
                 if (this.amongus.length <= this.index) {
                     this.index = 0;
                 }
             }
             if (this.suffix.getValue()) {
                 msg = msg + MathUtil.getRandom(1000, 10000);
             }
             mc.player.connection.sendPacket(new CPacketChatMessage(msg));
         }
     }
     public enum type
     {
         chinese,
         korean,
         amongus
             }
 }