package net.wnetw.project.media;

import java.awt.*;
import javax.swing.*;
import java.io.*;



public class WnetWScreenRecordPlayer extends JFrame{
  BorderLayout borderLayout1 = new BorderLayout();
  Dimension screenSize;

  public WnetWScreenRecordPlayer() {
super();
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize(screenSize);
    Screen p = new Screen();
    Container c = this.getContentPane();
    c.setLayout(borderLayout1);
    c.add(p,"Center");
    new Thread(p).start();
    this.show();
  }

  public static void main(String[] args){
    new WnetWScreenRecordPlayer();
  }

}

class Screen extends JPanel implements Runnable{
  private BorderLayout borderLayout1 = new BorderLayout();
  private Image cimage;

  public void run(){
int i = 0;
    while(true){
      try{
        cimage = loadImage(i + ".jpg");
        i = i + 1;
        repaint();
        Thread.sleep(40);//与录像时每秒帧数一致

      }catch(Exception e){
        e.printStackTrace();
        System.out.println(e);
      }
    }
  }

  public Image loadImage(String name) {
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image image = null;
    image = tk.getImage("C:/records/" +"c_"+ name);
    MediaTracker mt = new MediaTracker(this);
    mt.addImage(image, 0);
    try {
      mt.waitForID(0);
    }catch (Exception e) {
      e.printStackTrace();
      System.out.println(e);
    }
    return image;
  }

  public Screen() {
    super();
    this.setLayout(null);
  }

  public void paint(Graphics g){
    super.paint(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(cimage, 0, 0, null);
  }
}
