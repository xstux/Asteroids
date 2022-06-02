import java.awt.Graphics2D;
import java.awt.Color;

public class Bullet
{
	public double x;
	public double y;
	public double vx;
	public double vy;
	public double angle;
	public int lifeTimer;
	public int damage;
	public double rotation;
	
	public Bullet(double x, double y, double angle)
	{
		this.x = x + Math.sin(angle / 180.0 * 3.14) * 10;;
		this.y = y - Math.cos(angle / 180.0 * 3.14) * 10;;
		this.angle = angle;
		//this.vx = Math.sin(angle / 180.0 * 3.14) * 1;
		//this.vy = Math.cos(angle / 180.0 * 3.14) * 1;
		this.vx = Math.sin(angle / 180.0 * 3.14) * 12;
		this.vy = Math.cos(angle / 180.0 * 3.14) * 12;

		damage = 12;
		lifeTimer = 64;
		rotation = Math.random() * 360.0;
		
	}
	
	public void update()
	{
		/*if (vx * vx + vy * vy <= (144))
		{
			this.vx += Math.sin(angle / 180.0 * 3.14) * 0.6;
			this.vy += Math.cos(angle / 180.0 * 3.14) * 0.6;
		}
		*/
		this.x += this.vx;
		this.y -= this.vy;
		
		//Bounce
		if (x > 640 || x < 0)
			vx = -vx;
		if (y > 480 || y < 0)
			vy = -vy;
		
		
		//Wraparound
		/*if (x < 0) x += 640;
		if (x > 640) x -=640;
		if (y > 480) y -= 480;
		if (y < 0) y += 480;
		*/
		rotation += 4.0;
		
		if (lifeTimer > 0)
				lifeTimer--;
	}
	
	public void draw(Graphics2D g)
	{
		g.rotate(rotation / 180.0 * 3.14);
		g.setColor(Color.WHITE);
		//g.drawOval(-2, -2, 4, 4);
		//g.drawLine(0, -4, 0, 4);
		//g.setColor(Color.GREEN);
		//g.drawLine(-1, -4, -1, 4);
		//g.drawLine(1, -4, 1, 4);
		//g.fillRect(-2, -8, 4, 16);
		//g.setColor(Color.WHITE);
		//g.fillRect(-1, -6, 2, 12);
		
		//g.drawLine(-2, 3, 0, -3);
		//g.drawLine(2, 3, 0, -3);
		//g.drawLine(-2, 3, 2, 3);
		
		g.drawLine(0, -3, 0, 3);
		g.drawLine(-3, 0, 3, 0);
		g.drawLine(-3, -3, 3, 3);
		g.drawLine(-3, 3, 3, -3);
		
	}
}
