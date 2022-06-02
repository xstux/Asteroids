import java.awt.Color;
import java.awt.Graphics2D;


public class Explosion
{
	public double x;
	public double y;
	public double vx;
	public double vy;
	public int r;
	public int lifeTimer;
	public boolean active;

	
	public Explosion()
	{
		lifeTimer = 12;
		r = 2;
		active = true;
		
	}
	
	public void update()
	{
		x += vx;
		y -= vy;
		lifeTimer--;
		r += 2;
		if (lifeTimer == 0)
				active = false;
	}
	
	public void draw(Graphics2D g)
	{
		Color c = new Color(255,255,255, 255 / 12 * lifeTimer);
		g.setColor(c);
		g.drawOval(-r / 2, -r / 2, r, r);
	}
}
