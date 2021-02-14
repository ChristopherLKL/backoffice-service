package eu.christopherlee.admin.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TpLinkPurgeTask extends QuartzJobBean {
	private static final Log log = LogFactory.getLog(TpLinkPurgeTask.class);
	private TpLinkManager manager;

	public void setManager(TpLinkManager manager) {
		this.manager = manager;
	}

	@Override
	protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		try {
			manager.scheduledPurgeTask();
		} catch (Exception e) {
			log.error(e);
		}
	}
}