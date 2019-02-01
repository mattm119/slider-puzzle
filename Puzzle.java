import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.awt.event.*;
//import java.net.*;

/**
 * Matthew Miles
 * 
 * Last edited 24 Jan 2019
 *
 * Puzzle.java
 * Computer program of variable size slider puzzle
 * 
 * Demo of this working can be found at https://youtu.be/AjrbEXrm8j8
 */

public class Puzzle
{
  //variables
  private JFrame f;
  private JPanel p;
  private static BufferedImage image = null, icon = null;
  private JButton[] array;
  private int[] order;
  private int time = 0, size = 3; //
  private Random rng;
  //private URL url;
  
  public Puzzle()
  {
    f = new JFrame();
    p = new JPanel(new GridLayout(size,size));

    try {icon = ImageIO.read(new File("icon.png"));} 
    catch (IOException e) {}
    
    //try {url = new URL(" ENTER URL HERE ");}
    //catch (MalformedURLException e) {}
    
    //read image
    try {image = ImageIO.read(new File("fsu.png"));} 
    catch (IOException e) {}
    
    //get height and width of image pieces
    int width = image.getWidth() / size;
    int height = image.getHeight() / size;
    int count = 0;
    array = new JButton[size*size];
    order = new int[size*size];
    rng = new Random();
    
    //split the image into subimages to put on buttons
    for(int i = 0; i < size; i++)
    {
      for(int j = 0; j < size; j++)
      {
        JButton b = new JButton(new ImageIcon(image.getSubimage(width*j,
                                                                height*i,
                                                                width, height)));
        array[count] = b;
        order[count] = count;
        final int c = count;
        b.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            move(c);
          }
        });
        count++;
      }
    }
    
    //amount of randomness
    for(int i = 0; i < 10000; i++)
    {
      int a = rng.nextInt(4)+1;
      randomize(a);
    }
    
    update();
    
    //show the gui, add a title, icon ...
    f.add(p);
    f.setTitle("Slider Puzzle");
    f.setIconImage(icon);
    f.setSize(image.getWidth()+50,image.getHeight()+50);
    f.setLocationRelativeTo(null);
    f.setResizable(false);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
    
    ActionListener taskPerformer = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
          time++;
      }
  };
  new javax.swing.Timer(100, taskPerformer).start();
  }
  
  //randomize
  public void randomize(int seed)
  {
    int n = -1;
    for(int i = 0; i < size*size; i++)
    {
      if(order[i] == size*size-1)
        n = i;
    }
    
    if(seed == 1)
    {
      if(n+1<size*size && n%size != size-1)
      {
        int t = order[n];
        order[n] = order[n+1];
        order[n+1] = t;
      }
    }
    else if(seed == 2 && n%size != 0)
    {
      if(n-1>-1)
      {
        int t = order[n];
        order[n] = order[n-1];
        order[n-1] = t;
      }
    }
    else if(seed == 3)
    {
      if(n+size<size*size)
      {
        int t = order[n];
        order[n] = order[n+size];
        order[n+size] = t;
      }
    }
    else if(seed == 4)
    {
      if(n-size>-1)
      {
        int t = order[n];
        order[n] = order[n-size];
        order[n-size] = t;
      }
    }
  }
  
  //move
  public void move(int b)
  {
    int p = -1;
    for(int i = 0; i < size*size; i++)
    {
      if(b == order[i])
      {
        p = i;
      }
    }
    
    if(p != -1)
    {
      if(p+1<size*size) //empty space to the right
      {
        if(order[p+1] == size*size-1 && p%size != size-1)
        {
          int t = order[p];
          order[p] = order[p+1];
          order[p+1] = t;
        }
      }
      
      if(p-1>-1)//empty space to the left
      {
        if(order[p-1] == size*size-1 && p%size != 0)
        {
          int t = order[p];
          order[p] = order[p-1];
          order[p-1] = t;
        }
      }
      
      if(p+size<size*size)//empty space below
      {
        if(order[p+size] == size*size-1)
        {
          int t = order[p];
          order[p] = order[p+size];
          order[p+size] = t;
        }
      }
      
      if(p-size>-1)//empty space above
      {
        if(order[p-size] == size*size-1)
        {
          int t = order[p];
          order[p] = order[p-size];
          order[p-size] = t;
        }
      }
    }
    update();
    
    //if you win display the time
    if(win())
    {
      int minutes = 0;
      int seconds = 0;
      int extra = time;
      while(extra > 600)
      {
        extra -= 600;
        minutes++;
      }
      while(extra > 10)
      {
        extra -= 10;
        seconds++;
      }
      JOptionPane.showMessageDialog(f, "YOU WIN\nTime: "+minutes
                                      +" minutes "+seconds+"."+extra+" seconds");
    }
  }
  
  //checks for win
  public boolean win()
  {
    for(int i = 0; i < size*size; i++)
    {
      if(order[i] != i)
        return false;
    }
    return true;
  }
  
  //update
  public void update()
  {
    p.removeAll();
    
    for(int i = 0; i < size*size; i++)
    {
      if(order[i] == size*size-1)
        p.add(new JLabel(""));
      else
        p.add(array[order[i]]);
    }
    
    f.revalidate();
    f.repaint();
  }
  
  public static void main(String[] args)
  {
    new Puzzle();
  }
}