package engine;

import gui.MainFrame;
import service.InactivityService;

public class MainEngine {

	public static void main(String[] args) {
		AppBuilder build = new AppBuilder();
		MainFrame mainFrame = new MainFrame(build);
		InactivityService.getInstance();
		InactivityService.setMainFrame(mainFrame);
		
		mainFrame.setVisible(true);

	}

}
