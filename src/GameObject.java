import java.awt.Color;
import java.awt.Polygon;


public class GameObject
{
	public double x = 0;
	public double y = 0;
	public double x1;
	public double y1;
	public int l;
	public double angle = 0.0;
	public double speed;
	public double x2;
	public double y2;
	public int gType;
	public boolean active;
	public int life;
	public Polygon p;
	Color c;
	
	public void init()
	{
		x = (Math.random() * 640);
		y = (Math.random() * 480);
		l = 10 + (int)(Math.random() * 20);
		speed = 5 - (Math.random() * 10.0);
		changeDirection();
		angle = Math.random()*360.0;
		c = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
		gType = (int)(Math.random() * 4) + 1;
		life = l * 2;
		active = true;
		p = generatePolygon(l, 6);
	}
	
	public void update()
	{
		angle += speed;
		angle = angle % 360.0;
		x += x2;
		y -= y2;
		

		//Bounce
		//if (x > (640) | x < (0)) x2 = -x2;
		//if (y > (480) | y < (0)) y2 = -y2;

		//Wrap
		if (x > 640) x -= 640;
		if (x < 0) x += 640;
		if (y > 480) y -= 480;
		if (y < 0) y += 480;
		


		this.brake();
		
		//x1 = (int)(x + Math.sin(angle * 3.14 / 180.0) * (double)l);
		//y1 = (int)(y + Math.cos(angle * 3.14 / 180.0) * (double)l);
	}
	
	@SuppressWarnings("unused")
	private void brake()
	{
		//x2 = x2 > 3 ? 3 : x2;
		//y2 = y2 > 3 ? 3 : y2;
		//x2 = x2 < -3 ? -3 : x2;
		//y2 = y2 < -3 ? -3 : y2;
		x2 *= 0.999;
		y2 *= 0.999;
	}
	
	public void flip()
	{
		x2 = -x2;
		y2 = -y2;
	}
	
	public void changeDirection()
	{
		x2 = 2.0 - (Math.random() * 4.0);
		y2 = 2.0 - (Math.random() * 4.0);
	}
	
	public Polygon generatePolygon(double radius, int pts) {
		//Create an array to store the coordinates.
		int[] x = new int[pts];
		int[] y = new int[pts];
		double r;
		
		//Generate the points in the polygon.
		double angle = (2 * Math.PI / pts);
		for(int i = 0; i < pts; i++) {
			r = 1.0;
			x[i] = (int) (radius * Math.sin(i * angle));
			y[i] = (int) (radius * Math.cos(i * angle));
		}
		
		//Create a new polygon from the generated points and return it.
		return new Polygon(x, y, pts);
	}
}
