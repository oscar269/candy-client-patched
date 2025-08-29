 package as.pw.candee.utils;
 import java.awt.image.BufferedImage;
 import net.minecraft.client.renderer.texture.DynamicTexture;
 import net.minecraft.util.ResourceLocation;
 public class CandyDynamicTexture
     extends DynamicTexture {
     private final int Height;
     private final int Width;
     private final BufferedImage m_BufferedImage;
     private ResourceLocation m_TexturedLocation;
     private ImageFrame m_Frame;
     public CandyDynamicTexture(BufferedImage bufferedImage, int p_Height, int p_Width) {
         super(bufferedImage);
         this.m_Frame = null;
         this.m_BufferedImage = bufferedImage;
         this.Height = p_Height;
         this.Width = p_Width;
     }
     public int GetHeight() {
         return this.Height;
     }
     public int GetWidth() {
         return this.Width;
     }
     public final DynamicTexture GetDynamicTexture() {
         return this;
     }
     public final BufferedImage GetBufferedImage() {
         return this.m_BufferedImage;
     }
     public void SetResourceLocation(ResourceLocation dynamicTextureLocation) {
         this.m_TexturedLocation = dynamicTextureLocation;
     }
     public final ResourceLocation GetResourceLocation() {
         return this.m_TexturedLocation;
     }
     public void SetImageFrame(ImageFrame p_Frame) {
         this.m_Frame = p_Frame;
     }
     public final ImageFrame GetFrame() {
         return this.m_Frame;
     }
 }