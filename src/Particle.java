import java.awt.Color;
import java.awt.Graphics2D;


public class Particle {
	
	public double x, y;
	public double vx, vy;
	public double angle;
	public int life;
	public double rotation;
	public float red, green, blue;
	

	public Particle(double x, double y, double angle)
	{
		this.x = x;
		this.y = y;
		this.angle = angle + ((Math.random() * 3.0) - 1.5);
		life = 40;
		rotation = Math.random() * 360.0;
		
		double s = (Math.random() * 3.0) + 3.0;
		
		vx = Math.sin(this.angle / 180.0 * 3.14) * s;
		vy = Math.cos(this.angle / 180.0 * 3.14) * s;

		red = (float)Math.random() * 1.0f;
		green = (float)Math.random() * 1.0f;
		blue = (float)Math.random() * 1.0f;
		
	}
	
	public void Update()
	{
		vx -= vx * 0.02;
		vy -= vy * 0.02;
		
		x += vx;
		y -= vy;
		
		life -= 1;
		rotation += 6.0;
		
		//Wraparound
		/*if (x < 0) x += 640;
		if (x > 640) x -=640;
		if (y > 480) y -= 480;
		if (y < 0) y += 480;
		*/
		if (x < 0 || x > 640)
			vx = -vx;
		if (y < 0 || y > 480)
			vy = -vy;
	}
	
	public void Draw(Graphics2D g)
	{
		int s = life / 10;
		Color c = new Color(red, green, blue, (float)(life / 40.0 * 1.0));
		
		g.setColor(c);
		//g.rotate(rotation * 3.14 / 180.0);
		g.fillOval(-s, -s, s * 2, s * 2);
		//g.fillOval(-2, -2, 4, 4);
		//g.fillRect(-s, -s, s * 2, s * 2);
		//g.drawLine(0, -2, 0, 2);
		//g.drawLine(-2, 0, 2, 0);
		//g.drawLine(-2, -2, 2, 2);
	}
	
	
	
}
