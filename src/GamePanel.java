import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.Iterator;

//@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable
{
	public static int WIDTH = 640;
	public static int HEIGHT = 480;
	public static int NUM_OBJ = 16;

	
	public GameObject[] gObj = new GameObject[NUM_OBJ];

	
	public Vector<GameObject> gObjV = new Vector<>();
	public Vector<GameObject> gObjQ = new Vector<>();
	public Vector<Explosion> explosions = new Vector<>();
	public Vector<Particle> particles = new Vector<>();
	
	public Player player = new Player(); 
	
	
	private Thread thread;
	private boolean running;
	public boolean aFlag;
	public boolean cFlag = false;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private int FPS = 30;
	private double averageFPS;
	
	public Block block = new Block();
	
	long startTime;
	long URDTimeMillis;
	long waitTime;
	long totalTime = 0;
	
	int frameCount = 0;
	int maxFrameCount = FPS;
	
	long targetTime = 1000 / FPS;
	Font myFont;
	DecimalFormat df;
	
	// Constructor
	public GamePanel()
	{
		super();
		 myFont = new Font("Monospace", Font.PLAIN, 10);
		 df = new DecimalFormat("0.0");
		
		
		for (int x = 0; x < NUM_OBJ; x++)
		{
			//gObj[x] = new GameObject();
			//gObj[x].init();
			
			//GameObject gV = new GameObject();
			//gV.init();
			//gObjV.add(gV);
			generateGameObject();
		}
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		aFlag = true;
		
		addMouseListener(new MouseAdapter() {
			
			public void mousePressed (MouseEvent e)
			{
				//testPress(e.getX(), e.getY(), e.getButton());
				if (e.getButton() == 1)
					player.fire = true;
			}
			
			public void mouseReleased(MouseEvent e)
			{
				if (e.getButton() == 1)
					player.fire = false;
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			
			public void mouseMoved (MouseEvent e)
			{
				//testMove(e.getX(), e.getY());
				player.getMousePosition(e.getX(), e.getY());
				
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed (KeyEvent k)
			{
				//testKeyPress(k.getKeyCode());
				switch(k.getKeyCode())
				{
					case KeyEvent.VK_LEFT:
						player.rotateLeft = true;
						break;
					
					case KeyEvent.VK_RIGHT:
						player.rotateRight = true;
						break;
					
					case KeyEvent.VK_UP:
						player.thrust = true;
						break;
					
					case KeyEvent.VK_ESCAPE:
						running = false;
						break;
					
					case KeyEvent.VK_A:
						aFlag = !aFlag;
						break;
					
					case KeyEvent.VK_O:
						cFlag = true;
						break;
					
					case KeyEvent.VK_Z:
						player.fire = true;
						break;
						
						
				}
			};
			
			@Override
			public void keyReleased(KeyEvent k)
			{
				switch(k.getKeyCode())
				{
					case KeyEvent.VK_LEFT:
						player.rotateLeft = false;
						break;
				
					case KeyEvent.VK_RIGHT:
						player.rotateRight = false;
						break;		
						
					case KeyEvent.VK_UP:
						player.thrust = false;
						break;
					
					case KeyEvent.VK_Z:
						player.fire = false;
						break;

				}
			}
		});
		
		
	}
	
	void testKeyPress(int k)
	{
		//System.out.println("key: " + k);
		if (k == 27)
		{
			running = false;
		}
		
		if (k == 65)
		{
			aFlag = !aFlag;
		}
		
		if (k == 37)
			player.rotateLeft = true;
		else
			player.rotateLeft = false;
	}
	
	/*private void testPress(int x, int y, int b)
	{
		System.out.println("x:" + x + " y:" + y + " b:" + b);
		
		switch(b)
		{
		case MouseEvent.BUTTON1:
			player.getMousePosition(x, y);
			player.fire = true;
			break;
		}

	}*/

	/*private void testMove(int x, int y)
	{
		//System.out.println("x:" + x + " y:" + y);
		player.getMousePosition(x, y);
	}*/

	
	// Functions
	public void addNotify()
	{
		super.addNotify();
		if (thread == null)
		{
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void run()
	{
		running = true;
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		// Game loop
		while (running)
		{
			startTime = System.nanoTime();
			
			gameUpdate();
			gameRender();
			gameDraw();
			
			URDTimeMillis = (System.nanoTime() - startTime) / 1_000_000;
			
			waitTime = targetTime - URDTimeMillis;
			
			try
			{
				Thread.sleep(waitTime);
			}
			catch (Exception e)
			{
				
			}
			
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if (frameCount == maxFrameCount)
			{
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1_000_000);
				frameCount = 0;
				totalTime = 0;
			}
			
		}
		
		System.exit(0);
	}
	
	private void gameUpdate()
	{
		//System.out.println("Frame: " + frameCount);
		if (cFlag)
		{
			generateGameObject();
			cFlag = false;	
		}
		
		//Add objects from queue
		Iterator<GameObject> q = gObjQ.iterator();
		while (q.hasNext())
		{
			gObjV.add(q.next());
		}
		gObjQ.clear();
		
		//for (int x = 0; x < NUM_OBJ; x++)
		//{
		//	gObj[x].update();
		//}
		
		Iterator<GameObject> i = gObjV.iterator();
		
		while (i.hasNext())
		{
			GameObject gameObject = i.next();
			gameObject.update();
			
			Iterator<Bullet> ib = player.bullets.iterator();
			
			while (ib.hasNext())
			{
				Bullet bullet = ib.next();
				
				
				//for (int z = 0; z < 1; z++)
				/*if (bullet.lifeTimer % 2 == 0)
				{
					Particle p1 = new Particle(bullet.x, bullet.y, bullet.angle - 180.0);
					p1.vx += p1.vx + bullet.vx;
					p1.vy += p1.vy + bullet.vy;
					particles.add(p1);
				}*/
				
				
				if (gameObject.active && (distance(bullet.x, bullet.y, gameObject.x, gameObject.y) <= (gameObject.l * 1.25)))
				{
					gameObject.life -= bullet.damage;
					bullet.lifeTimer = 0;
					//bullet.lifeTimer -= bullet.damage;
					//bullet.angle += (Math.random() * 45.0) - 22.5;
					//bullet.vx = Math.sin(bullet.angle / 180 * 3.14) * 6.0;
					//bullet.vy = Math.cos(bullet.angle / 180 * 3.14) * 6.0;
					//bullet.vx -= bullet.vx * 0.9;
					//bullet.vy -= bullet.vy * 0.9;
					gameObject.speed += (Math.random() - 0.5);
					
					Explosion e = new Explosion();
					e.x = bullet.x;
					e.y = bullet.y;
					e.vx = gameObject.x2 / 2;
					e.vy = gameObject.y2 / 2;
					explosions.add(e);
					
					for (int z = 0; z < 3; z++)
					{
						Particle p1 = new Particle(bullet.x, bullet.y, Math.random() * 360);
						particles.add(p1);
					}

					gameObject.x2 += bullet.vx * 0.3;
					gameObject.y2 += bullet.vy * 0.3;
					
					player.score += 1;
					
					if (gameObject.life <= 0)
					{
						gameObject.active = false;
										
						player.score += 10;
						
						for (int x = 0; x < (int)(Math.random() * 4) + 1; x++)
						{
							GameObject gObjNew = new GameObject();
							gObjNew.init();
							gObjNew.x = gameObject.x;
							gObjNew.y = gameObject.y;
							//gObjNew.x2 = ((bullet.vx + gameObject.x2) * Math.random() * 2.0);
							//gObjNew.y2 = ((bullet.vy + gameObject.y2) * Math.random() * 2.0);
							
							gObjNew.x2 += ((bullet.vx * 0.2) + (gameObject.x2) * Math.random() * 0.2);
							gObjNew.y2 += ((bullet.vy * 0.2) + (gameObject.y2) * Math.random() * 0.2);
							gObjNew.l = (int)(gameObject.l * (0.2 + Math.random() * 0.5));
							gObjNew.life = gObjNew.l * 2;
							gObjNew.p = gObjNew.generatePolygon(gObjNew.l, 4 + (int)(Math.random() * 4));
							if (gObjNew.l > 4)
								gObjQ.add(gObjNew);
							
							for (int z = 0; z < (int)(Math.random() * 12) + 12; z++)
							{
								Particle p1 = new Particle(gameObject.x, gameObject.y, Math.random() * 360);
								particles.add(p1);
							}
							
						}
					}
					
				}
				
				
			}
			
			/*Iterator<GameObject> i2 = gObjV.iterator();
			
			while (i2.hasNext())
			{
				GameObject gameObject2 = i2.next();
				
				if (gameObject != gameObject2)
				{
					if (distance(gameObject.x, gameObject.y, gameObject2.x, gameObject2.y) < 4)
					{
						double x1 = gameObject.x2;
						double y1 = gameObject.y2;
						gameObject.x2 = Math.sqrt(gameObject2.x2 * gameObject.x2);
						gameObject.y2 = Math.sqrt(gameObject2.y2 * gameObject.y2);
						//gameObject2.x2 += (x1 * 0.1);
						//gameObject2.y2 += (y1 * 0.1);
						
						
						
					}
				}
				
			}*/
			
			
		}
		
		
		if (player.thrust)
		{
			/*for (int z = 0; z < 2; z++)
			{
				Particle particle = new Particle(player.x + (int)(Math.sin((player.angle - 180) / 180.0 * 3.14) * 6), player.y - (int)(Math.cos((player.angle - 180) / 180.0 * 3.14) * 6), player.angle - 180.0);
				particle.vx += player.vx;
				particle.vy += player.vy;
				particles.add(particle);
			}*/
		}
		
		player.update();

		//Iterator<Bullet> ib = player.bullets.iterator();
		
		/*while (ib.hasNext())
		{
			Bullet bullet = ib.next();
			
			
			for (int z = 0; z < 1; z++)
			if (bullet.lifeTimer % 2 == 0)
			{
				Particle p1 = new Particle(bullet.x, bullet.y, bullet.angle - 180.0);
				//p1.vx += bullet.vx;
				//p1.vy += bullet.vy;
				particles.add(p1);
			}
		}*/
		
		
		Iterator<Explosion> ie = explosions.iterator();
		
		while (ie.hasNext())
		{
			ie.next().update();
		}
		
		
		Iterator<Particle> pi = particles.iterator();
		while (pi.hasNext())
		{
			pi.next().Update();
		}
		
		cleanUp();
	}
	
	private void gameRender()
	{
		//Graphics2D g2d = (Graphics2D) g;
		if (aFlag)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		AffineTransform identity = g.getTransform();
		
		
		//Make 1 polygon
		//Polygon p = generatePolygon(12.0, 6);
		
		/*for (int x = 0; x < NUM_OBJ; x++)
		{
			GameObject g1 = gObj[x];
			g.setColor(g1.c);
			g.setTransform(identity);
			g.translate(g1.x, g1.y);
			g.rotate(g1.angle);
			
			if (g1.gType == 1)
				g.drawLine(-g1.l / 2, 0, g1.l / 2, 0);
			else if (g1.gType == 2)
				g.drawRect(-6, -6, 12, 12);
			else if (g1.gType == 3)
				g.drawOval(-6, -6, 12, 12);
			else if (g1.gType == 4)
				g.drawPolygon(p);
			
			//g.fillOval((int)g1.x, (int)g1.y, 24, 24);
			//g.drawRect((int)gObj[x].x, (int)gObj[x].y, Math.abs(320 -(int)gObj[x].x)/3, Math.abs(240 - (int)gObj[x].y)/2);
			//g.drawString((int)gObj[x].x + ":" + (int)gObj[x].y, (int)gObj[x].x1, (int)gObj[x].y1);
			
			//Collisions
			for (int y = 0; y < gObj.length; y++)
			{
				if (gObj[x] == gObj[y])
						continue;
				
				//if (((int)(gObj[x].x) == ((int)gObj[y].x)) && (((int)gObj[x].y) == ((int)gObj[y].y)))
				if ((Math.abs((gObj[x].x - gObj[y].x)) < 2) && (Math.abs((gObj[x].y - gObj[y].y)) < 2))
				{
					gObj[x].x2 = (gObj[x].x2 + gObj[y].x2) * 0.8;
					gObj[x].y2 = (gObj[x].y2 + gObj[y].y2) * 0.8;
				}
			}
			
		}*/
		
		Iterator<Particle> pi = particles.iterator();
		while (pi.hasNext())
		{
			Particle particle = pi.next();
			g.setTransform(identity);
			g.translate(particle.x, particle.y);
			particle.Draw(g);
		}
		
		Iterator<GameObject> i = gObjV.iterator();
		
		while (i.hasNext())
		{
			GameObject gameObject = i.next();
			//g.setColor(gameObject.c);
			g.setColor(Color.WHITE);
			g.setTransform(identity);
			g.translate(gameObject.x, gameObject.y);
			g.rotate(gameObject.angle * 3.14 / 180.0);
			
			//if (gameObject.gType == 1)
			//	g.drawLine(-gameObject.l / 2, 0, gameObject.l / 2, 0);
			//else if (gameObject.gType == 2)
			//	g.drawRect(-6, -6, 12, 12);
			//else if (gameObject.gType == 3)
				//g.drawOval(-6, -6, 12, 12);
			
			//else if (gameObject.gType == 4)
			//	g.drawPolygon(p);	
			
			//g.drawOval(-gameObject.l / 2, -gameObject.l / 2, gameObject.l, gameObject.l);
			//g.drawRect(-gameObject.l / 2, -gameObject.l / 2, gameObject.l, gameObject.l);
			//Polygon polygon = generatePolygon(gameObject.l, 5);
			//g.drawPolygon(polygon);
			g.drawPolygon(gameObject.p);
			//g.fillPolygon(gameObject.p);
		}
		
		Iterator<Bullet> ib = player.bullets.iterator();
		
		while (ib.hasNext())
		{
			Bullet bullet = ib.next();
			g.setTransform(identity);
			g.translate(bullet.x, bullet.y);
			g.rotate(bullet.angle * 3.14 / 180.0);
			//System.out.println(bullet.angle);
			bullet.draw(g);
		};
		
		Iterator<Explosion> ie = explosions.iterator();
		
		while (ie.hasNext())
		{
			Explosion e = ie.next();
			g.setTransform(identity);
			g.translate(e.x, e.y);
			e.draw(g);
		}
		

		
		
		g.setTransform(identity);
		g.translate(player.x, player.y);
		g.rotate(player.angle  * 3.14 / 180.0);
		
		player.draw(g);
		
		g.setTransform(identity);
				
		g.setColor(Color.BLACK);
		g.setFont(myFont);
		g.fillRect(0, 0, 102, 22);
		g.setColor(Color.RED);
		//g.drawString("Frame: " + frameCount, 2, 10);
		g.drawString("Score: " + player.score, 2, 10);
		
		g.drawString("Average FPS: " + df.format(averageFPS), 2, 20);
		g.drawRect(0, 0, 102, 22);
		
		//g.drawRect((int)block.r.getX(), (int)block.r.getY(), (int)block.r.getWidth(), (int)block.r.getHeight());
	}
	
	private void gameDraw()
	{
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	
	public Polygon generatePolygon(double radius, int pts) {
		//Create an array to store the coordinates.
		int[] x = new int[pts];
		int[] y = new int[pts];
		
		//Generate the points in the polygon.
		double angle = (2 * Math.PI / pts);
		for(int i = 0; i < pts; i++) {
			x[i] = (int) (radius * Math.sin(i * angle));
			y[i] = (int) (radius * Math.cos(i * angle));
		}
		
		//Create a new polygon from the generated points and return it.
		return new Polygon(x, y, pts);
	}
	
	public void generateGameObject()
	{	
		GameObject g = new GameObject();
		g.init();
		gObjV.add(g);
	}
	
	public double distance(double x1, double y1, double x2, double y2)
	{
		
		double dx = (x2 - x1) * (x2 - x1);
		double dy = (y2 - y1) * (y2 - y1);
		return Math.sqrt(dx + dy);
	}
	
	public void cleanUp()
	{
		Iterator<GameObject> i = gObjV.iterator();
		
		while (i.hasNext())
		{
			GameObject gameObject = i.next();
			if (!gameObject.active)
				i.remove();
		}
		
		Iterator<Explosion> ie = explosions.iterator();
		while (ie.hasNext())
		{
			Explosion e = ie.next();
			if (!e.active)
				ie.remove();
		}
		
		Iterator<Particle> pi = particles.iterator();
		while (pi.hasNext())
		{
			Particle particle = pi.next();
			if (particle.life <= 0)
				pi.remove();
		}

	}
}
