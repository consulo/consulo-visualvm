package krasa.visualvm.runner;

import com.intellij.java.execution.runners.JavaPatchableProgramRunner;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import krasa.visualvm.integration.VisualVMContext;
import krasa.visualvm.integration.VisualVMHelper;

public class RunnerUtils {
	private static final Logger log = Logger.getInstance(RunnerUtils.class);

	static void runVisualVM(final JavaPatchableProgramRunner runner, ExecutionEnvironment env, RunProfileState state) throws ExecutionException {
		try {
			VisualVMHelper.startVisualVM(VisualVMContext.load(), env.getProject(), runner);
		} catch (Throwable e) {
			log.error(e);
		}
	}
}
