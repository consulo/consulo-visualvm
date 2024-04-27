package krasa.visualvm.runner;

import com.intellij.java.debugger.impl.GenericDebuggerRunner;
import com.intellij.java.execution.configurations.RemoteConnection;
import com.intellij.java.execution.impl.jar.JarApplicationConfiguration;
import com.intellij.java.execution.impl.remote.RemoteConfiguration;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.configuration.ModuleRunProfile;
import consulo.execution.configuration.RunProfile;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.ui.RunContentDescriptor;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import krasa.visualvm.LogHelper;
import krasa.visualvm.MyConfigurable;
import krasa.visualvm.executor.DebugVisualVMExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public class DebugVisualVMRunner extends GenericDebuggerRunner {
	private static final Logger log = Logger.getInstance(DebugVisualVMRunner.class);

	@NotNull
	public String getRunnerId() {
		return DebugVisualVMExecutor.EXECUTOR_ID;
	}

	@Override
	public void execute(@NotNull final ExecutionEnvironment environment) throws ExecutionException {
		LogHelper.print("#execute", this);

		boolean b = MyConfigurable.openSettingsIfNotConfigured(environment.getProject());
		if (!b) {
			return;
		}
		super.execute(environment);
	}

	public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
		return executorId.equals(DebugVisualVMExecutor.EXECUTOR_ID) && (profile instanceof ModuleRunProfile || profile instanceof JarApplicationConfiguration)
				&& !(profile instanceof RemoteConfiguration);
	}

	@Nullable
	@Override
	protected RunContentDescriptor attachVirtualMachine(RunProfileState state, @NotNull ExecutionEnvironment env,
														RemoteConnection connection, boolean pollConnection) throws ExecutionException {
		RunContentDescriptor runContentDescriptor = super.attachVirtualMachine(state, env, connection, pollConnection);
		LogHelper.print("#attachVirtualMachine", this);
		RunnerUtils.runVisualVM(this, env, state);
		return runContentDescriptor;
	}

}
