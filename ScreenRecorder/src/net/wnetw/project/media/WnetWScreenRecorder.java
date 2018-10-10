package net.wnetw.project.media;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.Element;




public class WnetWScreenRecorder extends Thread{

  private Dimension screenSize;
  private Rectangle rectangle;
  private Robot robot;
  private long i = 0;
  private double width,height;

  public WnetWScreenRecorder() {
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    rectangle = new Rectangle(screenSize);//可以指定捕获屏幕区域
    width  = screenSize.getWidth();
    height = screenSize.getHeight();
    try{
      robot = new Robot();
    }catch(Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
  }

  public static void main(String[] args) {
    new WnetWScreenRecorder().start();
  }

  public void run(){
    FileOutputStream fos = null;
    while (true){
      try{
        BufferedImage image = robot.createScreenCapture(rectangle);//捕获制定屏幕矩形区域
        //fos = new FileOutputStream("C:\\records\\" + i + ".jpg");
        //JPEGCodec.createJPEGEncoder(fos).encode(image);//图像编码成JPEG
        String url = "C:/records/";  
        String name  =  "sd456asd.gif";  
        name = i+".jpg";  
        Tosmallerpic(image,url,name,(int)width,(int)height,(float)1.0); 
        //fos.close();
        i = i + 1;
        Thread.sleep(40);//每秒25帧
      }catch(Exception e){
        e.printStackTrace();
        System.out.println(e.getMessage()+"#");
        try{
          //if (fos != null)fos.close();
        }catch(Exception e1){}
      }
    }
  }
  
  
  private static void  Tosmallerpic(Image src1,String destFilePath,String name,int w,int h,float per){   
      Image src;   
      try {   
          src  =  src1; //构造Image对象   

         String img_midname  =  destFilePath + File.separator + "c_"+name;   
         int old_w = src.getWidth(null); //得到源图宽   
         int old_h = src.getHeight(null);   
         int new_w = 0;   
         int new_h = 0; //得到源图长   

         double w2 = (old_w*1.00)/(w*1.00);   
         double h2 = (old_h*1.00)/(h*1.00);   

         //图片跟据长宽留白，成一个正方形图。   
         BufferedImage oldpic;   
         if(old_w>old_h)   
         {   
             oldpic = new BufferedImage(old_w,old_w,BufferedImage.TYPE_INT_RGB);   
         }else{if(old_w<old_h){   
             oldpic = new BufferedImage(old_h,old_h,BufferedImage.TYPE_INT_RGB);   
         }else{   
              oldpic = new BufferedImage(old_w,old_h,BufferedImage.TYPE_INT_RGB);   
         }   
         }   
          Graphics2D g  =  oldpic.createGraphics();   
          g.setColor(Color.white);   
          if(old_w>old_h)   
          {   
              g.fillRect(0, 0, old_w, old_w);   
              g.drawImage(src, 0, (old_w - old_h) / 2, old_w, old_h, Color.white, null);   
          }else{   
              if(old_w<old_h){   
              g.fillRect(0,0,old_h,old_h);   
              g.drawImage(src, (old_h - old_w) / 2, 0, old_w, old_h, Color.white, null);   
              }else{   
                  //g.fillRect(0,0,old_h,old_h);   
                  g.drawImage(src.getScaledInstance(old_w, old_h,  Image.SCALE_SMOOTH), 0,0,null);   
              }   
          }                
          g.dispose();   
          src  =  oldpic;   
          //图片调整为方形结束   
         if(old_w>w)   
         new_w = (int)Math.round(old_w/w2);   
         else   
             new_w = old_w;   
         if(old_h>h)   
         new_h = (int)Math.round(old_h/h2);//计算新图长宽   
         else   
             new_h = old_h;   
         BufferedImage image_to_save  =  new BufferedImage(new_w,new_h,BufferedImage.TYPE_INT_RGB);          
         image_to_save.getGraphics().drawImage(src.getScaledInstance(new_w, new_h,  Image.SCALE_SMOOTH), 0,0,null);   
         FileOutputStream fos = new FileOutputStream(img_midname); //输出到文件流   
            
         //旧的使用 jpeg classes进行处理的方法  
//         JPEGImageEncoder encoder  =  JPEGCodec.createJPEGEncoder(fos);   
//         JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(image_to_save);   
          /* 压缩质量 */   
//         jep.setQuality(per, true);   
//         encoder.encode(image_to_save, jep);   
            
         //新的方法  
         saveAsJPEG(100, image_to_save, per, fos);  
            
         fos.close();   
         } catch (IOException ex) {   
        	 System.out.println("11111"+ex.getMessage());
//          Logger.getLogger(Img_Middle.class.getName()).log(Level.SEVERE, null, ex);   
      }   
}   
 
/** 
   * 以JPEG编码保存图片 
   * @param dpi  分辨率 
   * @param image_to_save  要处理的图像图片 
   * @param JPEGcompression  压缩比 
   * @param fos 文件输出流 
   * @throws IOException 
   */  
  public static void saveAsJPEG(Integer dpi ,BufferedImage image_to_save, float JPEGcompression, FileOutputStream fos) throws IOException {  
          
      //useful documentation at http://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html  
      //useful example program at http://johnbokma.com/java/obtaining-image-metadata.html to output JPEG data  
      
      //old jpeg class  
      //com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder  =  com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);  
      //com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam  =  jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);  
      
      // Image writer  
//    JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpeg").next();  
      ImageWriter imageWriter  =   ImageIO.getImageWritersBySuffix("jpg").next();  
      ImageOutputStream ios  =  ImageIO.createImageOutputStream(fos);  
      imageWriter.setOutput(ios);  
      //and metadata  
      IIOMetadata imageMetaData  =  imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image_to_save), null);  
         
         
      if(dpi !=  null && !dpi.equals("")){  
             
           //old metadata  
          //jpegEncodeParam.setDensityUnit(com.sun.image.codec.jpeg.JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);  
          //jpegEncodeParam.setXDensity(dpi);  
          //jpegEncodeParam.setYDensity(dpi);  
      
          //new metadata  
          Element tree  =  (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");  
          Element jfif  =  (Element)tree.getElementsByTagName("app0JFIF").item(0);  
          jfif.setAttribute("Xdensity", Integer.toString(dpi) );  
          jfif.setAttribute("Ydensity", Integer.toString(dpi));  
             
      }  
      
      
      if(JPEGcompression >= 0 && JPEGcompression <= 1f){  
      
          //old compression  
          //jpegEncodeParam.setQuality(JPEGcompression,false);  
      
          // new Compression  
          JPEGImageWriteParam jpegParams  =  (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();  
          jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);  
          jpegParams.setCompressionQuality(JPEGcompression);  
      
      }  
      
      //old write and clean  
      //jpegEncoder.encode(image_to_save, jpegEncodeParam);  
      
      //new Write and clean up  
      imageWriter.write(imageMetaData, new IIOImage(image_to_save, null, null), null);  
      ios.close();  
      imageWriter.dispose();  
      
  }  
  
}