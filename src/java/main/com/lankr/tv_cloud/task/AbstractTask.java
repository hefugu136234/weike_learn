/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月14日
 * 	@modifyDate 2016年6月14日
 *  
 */
package com.lankr.tv_cloud.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kalean.Xiang
 *
 */
public class AbstractTask {

	private final static ExecutorService pool = Executors.newFixedThreadPool(5);

	protected void execute(Task task) {
		if (task != null) {
			pool.execute(task);
		}
	}
	
	static abstract class Task implements Runnable {

		public abstract void process();

		public boolean onBefore() {
			return true;
		}

		public void onFinished() {
		}

		@Override
		public void run() {
			if (!onBefore()) {
				return;
			}
			process();
			onFinished();
		}

	}
}
