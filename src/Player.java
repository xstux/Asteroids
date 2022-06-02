import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;
import java.util.Iterator;


public class Player
{
	public double x;
	public double y;
	public double angle;
	public double speed;
	public double vx;
	public double vy;
	
	public boolean rotateLeft;
	public boolean rotateRight;
	public boolean thrust;
	public boolean fire;
	public int reloadTimer;
	
	public int mouseX, mouseY;
	public double fireAngle;
	
	public int score = 0;
	
	public Vector<Bullet> bullets;
	
	
	
	//Constructor
	public Player()
	{
		x = 320.0;
		y = 240.0;
		angle = 0;
		vx = 0;
		vy = 0;
		bullets = new Vector<>();
		fire = false;
		reloadTimer = 0;
	}
	
	public void update()
	{
		
		if (fire && reloadTimer == 0)
		{
			for (int x = -4; x <= 4; x += 4)
			{
				Bullet b = new Bullet(this.x, this.y, this.angle + x);
				b.vx += this.vx;
				b.vy += this.vy;
				bullets.add(b);
			}
			
			//fireAngle = Math.atan2(mouseY - (int)this.y, mouseX - (int)this.x) / 3.14 * 180.0;
			
			//if (fireAngle <= 0) fireAngle += 360.0;
			//Bullet b = new Bullet(this.x, this.y, this.angle + (Math.random() * 6.0) - 3.0);
			//b.vx += this.vx;
			//b.vy += this.vy;
			//bullets.add(b);
			
			reloadTimer = 8;
			
			//fire = false;
		}
		
		if (rotateLeft)
		{
			angle -=5;
		}
		if (rotateRight)
		{
			angle +=5;
		}
		if (thrust)
		{
			speed = 0.15;
		}
		else
		{
			speed = 0;
		}
		//if (speed >= 4.0)
		//	speed = 4.0;
		
		angle %= 360;
		
		//System.out.println(angle);
	
		//if ((vx * vx + vy * vy) < 10)
		{
			vx += Math.sin(angle * 3.14 / 180.0) * speed;
			vy += Math.cos(angle * 3.14 / 180.0) * speed;
		}

		
		x += vx;
		y -= vy;
		
		
		//vx *= 0.98;
		//vy *= 0.98;
		
		vx -= vx * 0.01;
		vy -= vy * 0.01;
		
		//Bounce off edge
		if (x < 0 || x > 640)
			vx = -vx;
		if (y < 0 || y > 480)
			vy = -vy;
		
		//Wraparound
		//if (x < 0) x += 640;
		//if (x > 640) x -=640;
		//if (y > 480) y -= 480;
		//if (y < 0) y += 480;
		
		Iterator<Bullet> i = bullets.iterator();
		
		while (i.hasNext())
		{
			Bullet bullet = i.next();
			
			bullet.update();
			
			if (bullet.lifeTimer <= 0)
			{
				i.remove();
			}
		}
		
		if (reloadTimer > 0)
			reloadTimer--;
	}
	
	public void draw(Graphics2D g)
	{
		
		/*if (thrust)
		{
			g.setColor(Color.RED);
			g.drawLine(-4, 6, 0, 12);
			g.drawLine(4, 6, 0, 12);	
		}*/
		
		g.setColor(Color.YELLOW);
		//g.drawRect(-8, -8, 16, 16);
		//g.drawLine(0,8,0,-20);
		//g.drawLine(-10, -8, 10, 0);
		//g.drawLine(-10, 8, 10, 0);
		//g.drawLine(-6, -6, -6, 6);
		g.drawLine(-8,10,0,-10);
		g.drawLine(8,10,0,-10);
		g.drawLine(-8, 10, -4, 6);
		g.drawLine(8, 10, 4, 6);
		g.drawLine(-4, 6, 4, 6);
		//g.drawLine(-2, 6, 0, -10);
		//g.drawLine(2, 6, 0, -10);
		
		

		
	}
	
	public void getMousePosition(int x, int y)
	{
		mouseX = x;
		mouseY = y;
		
		fireAngle = Math.atan2(mouseY - (int)this.y, mouseX - (int)this.x) / 3.14 * 180.0;
		if (fireAngle <= 0) fireAngle += 360.0;
		
		//System.out.println(fireAngle);
		
	}
}
