package eu.christopherlee.admin.manager;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TpLinkTask extends TimerTask {
	private static final Log log = LogFactory.getLog(TpLinkTask.class);
	private TpLinkManager manager;

	public void setManager(TpLinkManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		try{
			manager.scheduledTask();
	    }catch (RuntimeException e){
	        log.error("Uncaught Runtime Exception",e);
	        return;
	    }catch (Throwable e){
	        log.error("Unrecoverable error",e);
	        throw e;
	    }
	}
}