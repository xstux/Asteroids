import javax.swing.JFrame;

public class Game
{
	public static void main (String [] args)
	{
		JFrame window = new JFrame("Game Test");
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setContentPane(new GamePanel());
		
		//window.setSize(640, 480);
		window.pack();
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
