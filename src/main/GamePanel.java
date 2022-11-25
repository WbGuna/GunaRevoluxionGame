package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManeger;

public class GamePanel extends JPanel implements Runnable{

	private static final long serialVersionUID = 6644375181764124582L;
	
	final int originalTilesSize = 16; // 16x16 tile
	final int scale = 3;
	
	public final int tileSize = originalTilesSize * scale; // 48x48 tile
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels - Largura
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels - Altura
	
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	
	int FPS = 60;
	
	TileManeger tileM = new TileManeger(this);
	KeyHandler keyH = new KeyHandler();
	Sound music = new Sound();
	Sound se = new Sound();
	Thread gameThread;
	public CollisionCheker cChecker = new CollisionCheker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public Player player = new Player(this, keyH);
	public SuperObject obj[] = new SuperObject[10];
	
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		aSetter.setObject();
		playMusic(0);
	}
	
	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS; // por secundos
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCont = 0;

		while(gameThread != null) {

			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			if(delta >= 1) {
				update();			
				repaint();
				delta --;
				drawCont++;
				
			}
			
			if(timer >= 1000000000) {
				System.out.println("FPS: " + drawCont);
				drawCont = 0;
				timer = 0;
			}			
		}		
	}
	
	public void update() {
		player.update();
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);		
		Graphics2D g2 = (Graphics2D)g;
		
		long drawStart = 0; // Debug - correção
		if(keyH.checkDrawTime == true) {
			
			drawStart = System.nanoTime();
		}
		
		tileM.draw(g2); //Tile
		
		for (int i = 0; i < obj.length; i++) { // Objeto
			if(obj[i] != null) {
				obj[i].draw(g2, this);
			}
		}
		player.draw(g2); //Player
		
		ui.draw(g2); // UI
		
		//Debug - Correção
		if(keyH.checkDrawTime == true) {
			
			long drawEnd = System.nanoTime();
			long passed = drawEnd -  drawStart;
			g2.setColor(Color.white);
			g2.drawString("Sorteio:" + passed, 10, 400);
			System.out.println("Sorteio: "+ passed);
		}
		
		g2.dispose();
		
	}
	
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	public void playSE(int i){
		se.setFile(i);
		se.play();
	}

}
