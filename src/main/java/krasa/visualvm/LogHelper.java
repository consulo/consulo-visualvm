package krasa.visualvm;

import consulo.logging.Logger;

public class LogHelper {
	private static final Logger log = Logger.getInstance(LogHelper.class);

	public static void print(String x, Object thisInstance) {
		if (log.isDebugEnabled()) {
			String simpleName = "LogHelper : ";
			if (thisInstance != null) {
				simpleName = thisInstance.getClass().getSimpleName() + ": ";
			}
			log.debug(simpleName + x);
		}
	}

}
