package retryAnalyser;

import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.framework.utils.GlobalVariables;
import com.framework.utils.LoggerHelper;

public class RetryFailedTests implements IRetryAnalyzer{
	private Logger log;
	private ThreadLocal<Integer>  retryCount = ThreadLocal.withInitial(() -> 0);
	private ThreadLocal<Integer>  maxRetryCount = ThreadLocal.withInitial(() -> 0);

	@Override
	public boolean retry(ITestResult result) {
		log = LoggerHelper.getLogger(result.getClass());
		maxRetryCount.set(Integer.parseInt(GlobalVariables.configProp.getProperty("retryCount")));
		if (retryCount.get()< maxRetryCount.get()) {
			log.debug("Retrying Method:" + result.getName() + "again and the count is" + (retryCount.get() + 1));
			retryCount.set(retryCount.get()+1);
			GlobalVariables.currentRetryCount.set(retryCount.get() );
			GlobalVariables.maxRetryCount.set(maxRetryCount.get());
			return true;
		} else {
			retryCount.set(0);
			GlobalVariables.currentRetryCount.set(retryCount.get());
			return false;
		}		
	}
	public int getCurrentRetryCount()
	{
		return retryCount.get() ;
	}

}
