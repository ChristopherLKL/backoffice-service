package eu.christopherlee.admin.manager;

import java.util.TimerTask;

public class TpLinkTask extends TimerTask {
	private TpLinkManager manager;

	public void setManager(TpLinkManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		manager.scheduledTask();
	}
}