import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

class GameFrame extends JFrame {
    GameFrame(){

        this.add(new GamePanelS());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

class GamePanelS extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE = 25;//size of the object ,how big the object is?
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;//total number of objects fit into the game Panel
    int xApple,yApple;
    int xSnake[]=new int[GAME_UNITS];
    int ySnake[]=new int[GAME_UNITS];
    int Delay = 70;
    Timer timer;
    boolean running = false;
    Random random;

    int bodyParts = 6;
    char direction = 'R';
    int gameScore;
    GamePanelS(){
        random = new Random();
        Border border = BorderFactory.createLineBorder(Color.orange,1);
        this.setBorder(border);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(new Color(0x4262723));
        this.setFocusable(true);
        this.addKeyListener(new MyMouseAdaptor());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(Delay,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if (running) {
//draw the matrix or grid .
           /* for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }*/
            //draw a apple
            g.setColor(Color.red);
            g.fillOval(xApple, yApple, UNIT_SIZE, UNIT_SIZE);
            //draw a snake head and body
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    //head of the snake
                    g.fillRect(xSnake[i], ySnake[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    //body of the snake
                    g.setColor(Color.ORANGE);
                    g.fillRect(xSnake[i], ySnake[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //draw score
            g.setColor(new Color(0xFF346399, true));
            g.setFont(new Font("Ink Free",Font.BOLD,30));
            FontMetrics metrics = this.getFontMetrics(g.getFont());
            g.drawString("Score:"+gameScore,(SCREEN_WIDTH-metrics.stringWidth("Score:"+gameScore))/2,g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    public void newApple(){
        //to generate the coordinates of the apple randomly
        xApple = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        yApple = random.nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for (int i=bodyParts;i>0;i--){
            //shifting all the coordinates over one spot
            xSnake[i]=xSnake[i-1];
            ySnake[i]=ySnake[i-1];
        }
        switch (direction){
            case 'U':
                ySnake[0]=ySnake[0]-UNIT_SIZE;
                break;
            case 'D':
                ySnake[0]=ySnake[0]+UNIT_SIZE;
                break;
            case  'L':
                xSnake[0]=xSnake[0]-UNIT_SIZE;
                break;
            case  'R':
                xSnake[0]=xSnake[0]+UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        if(xSnake[0]==xApple && ySnake[0]==yApple){
            newApple();
            gameScore=gameScore+10;
            bodyParts++;
        }
    }

    public void checkCollision(){
        //check if head of snake collides with any body parts
        for(int i=bodyParts-1;i>0;i--){
            if(xSnake[0]==xSnake[i] && ySnake[0] == ySnake[i]){
                running=false;//we are gone trigger the game over method .
            }
        }
        //check if head touches the border
        if(xSnake[0]<0){
            running=false;
        }
        if (xSnake[0]>SCREEN_WIDTH){
            running=false;
        }
        if(ySnake[0]<0){
            running=false;
        }
        if(ySnake[0]>SCREEN_WIDTH){
            running=false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //draw score
        g.setColor(new Color(0xFF346399));
        g.setFont(new Font("Ink Free",Font.BOLD,60));
        FontMetrics metrics1 = this.getFontMetrics(g.getFont());
        g.drawString("Score:"+gameScore,(SCREEN_WIDTH-metrics1.stringWidth("Score:"+gameScore))/2,g.getFont().getSize());


        g.setColor(Color.WHITE);
        g.setFont(new Font("MV Boli",Font.BOLD,70));
        FontMetrics metrics2 = this.getFontMetrics(g.getFont());
        g.drawString("Game Over ..!",(SCREEN_WIDTH-metrics2.stringWidth("Game Over ..!"))/2,SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
    class MyMouseAdaptor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_UP:
                    if(direction!='D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U'){
                        direction='D';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(direction!='R'){
                        direction='L';
                    }
                    break;
            }
        }
    }
}

public class Snake {
    public static void main(String[] args) {
        new GameFrame();
    }
}
