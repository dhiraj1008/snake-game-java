//main method file;
public class Snake {
    public static void main(String[] args)
    {
        new GameFrame();
    }
}
//gameframe file
import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame()
    {   this.add(new GamePanel());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Snake game");
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);//to appear our frame at the center
    }
}
//gamepanel file
import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.event.KeyAdapter;
        import java.awt.event.KeyEvent;
        import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static  final int screen_width=600;
    static final int screen_height=600;
    static final int  unit_size=25;
    static final int Game_unit=(screen_width*screen_height)/unit_size;
    static  final int delay=70;
    final int x[]=new int[Game_unit];
    final int y[]=new int[Game_unit];
    int bodyParts=6;
    int appleEaten;
    int appleX;
    int appleY;
    char direction='R';
    boolean running=false;
    Timer timer;
    Random r;
    GamePanel()
    {
        r=new Random();
        this.setPreferredSize(new Dimension(screen_width,screen_height));
        this.setBackground(new Color(0x1234));
        this.setFocusable(true);
        this.addKeyListener(new MYKeyAdaptor());
        startGame();
    }
    public void startGame()
    {   newApple();
        running=true;
        timer=new Timer(delay,this);
        timer.start();
    }
    public void paintComponent(Graphics g)
    {   super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    { if(running){
      /*for(int i=0;i<screen_height/unit_size;i++)
  {
      g.drawLine(i*unit_size,0,i*unit_size,screen_height);
      g.drawLine(0,i*unit_size,screen_width,i*unit_size);
  }*/
        g.setColor(Color.GREEN);
        g.fillOval(appleX,appleY,unit_size,unit_size);

        for(int i=0;i<bodyParts;i++) {
            if (i == 0) {
                g.setColor(Color.RED);
                g.fillRect(x[i], y[i], unit_size, unit_size);
            } else {
                //g.setColor(new Color(45, 180, 0));
                g.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
                g.fillRect(x[i], y[i], unit_size, unit_size);
            }
        }

    }
    else{
        gameOver(g);
    }
        //Game score text;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink free",Font.ITALIC,40));
        FontMetrics metrics=getFontMetrics(g.getFont());//center our font of the metrics;
        g.drawString("Score: "+appleEaten,450,585);

    }
    public void newApple()
    {   appleX=r.nextInt((int)(screen_width/unit_size))*unit_size;
        appleY=r.nextInt((int)(screen_height/unit_size))*unit_size;
    }
    public void checkApples()
    {
        if(x[0]==appleX && y[0]==appleY){
            bodyParts++;//to increase body parts;
            appleEaten++;//to increase score;
            newApple();//to generate new apple for user;
        }

    }
    public void move()
    {
        for(int i=bodyParts;i>0;i--)
        {
            x[i]=x[i-1];
            y[i]=y[i-1];
        }//enhanced switch;
        switch (direction) {
            case 'U' -> y[0] = y[0] - unit_size;
            case 'D' -> y[0] = y[0] + unit_size;
            case 'R' -> x[0] = x[0] + unit_size;
            case 'L' -> x[0] = x[0] - unit_size;
        }
    }
    public void checkCollisions()
    {//head collide with the body;
        for(int i=bodyParts;i>0;i--)
        {
            if(x[0]==x[i] && y[0]==y[i])
            {
                running=false;
            }
        }
        //check if head collide touches left border;
        if(x[0]<0){
            running=false;
        }
        //check if head collide touches right border;
        if(x[0]>screen_width){
            running=false;
        }
        //check if head collide touches top border;
        if(y[0]<0){
            running=false;
        }
        //check if head collide touches bottom border;
        if(y[0]>screen_height){
            running=false;
        }
        if(!running)
            timer.stop();
    }
    public void gameOver(Graphics g)
    {
        //Game over text;
        g.setColor(Color.red);
        g.setFont(new Font("Ink free",Font.BOLD,75));
        FontMetrics metrics=getFontMetrics(g.getFont());//center our font of the metrics;
        g.drawString("Game Over",(screen_width-metrics.stringWidth("Game Over"))/2,screen_height/2);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApples();
            checkCollisions();

        }
        repaint();
    }
    public class MYKeyAdaptor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // super.keyPressed(e);
            switch (e.getKeyCode())
            {case KeyEvent.VK_LEFT:
                if(direction!='R')//helps user to rotate the snake 90 degree;
                {
                    direction='L';
                }
                break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L')//helps user to rotate the snake 90 degree;
                    {
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!='D')//helps user to rotate the snake 90 degree;
                    {
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U')//helps user to rotate the snake 90 degree;
                    {
                        direction='D';
                    }
                    break;
            }
        }
    }
}

