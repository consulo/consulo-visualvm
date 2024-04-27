package krasa.visualvm.integration;

import com.intellij.java.execution.runners.JavaProgramPatcher;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.CantRunException;
import consulo.execution.configuration.ModuleBasedConfiguration;
import consulo.execution.configuration.RunConfigurationModule;
import consulo.execution.configuration.RunProfile;
import consulo.execution.executor.Executor;
import consulo.java.execution.configurations.OwnJavaParameters;
import consulo.logging.Logger;
import consulo.module.Module;
import krasa.visualvm.LogHelper;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public class VisualVMJavaProgramPatcher extends JavaProgramPatcher
{
	private static final Logger log = Logger.getInstance(VisualVMJavaProgramPatcher.class);

	@Override
	public void patchJavaParameters(Executor executor, RunProfile configuration, OwnJavaParameters javaParameters) {
		LogHelper.print("#patchJavaParameters start", this);

		String name = configuration.getClass().getName();
		patch(configuration, javaParameters);
	}

	private VisualVMContext patch(RunProfile configuration, OwnJavaParameters javaParameters) {
		String jdkPath = getJdkPath(javaParameters);

		final Long appId = VisualVMHelper.getNextID();
		LogHelper.print("Patching: jdkPath=" + jdkPath + "; appId=" + appId, this);
		javaParameters.getVMParametersList().prepend("-Dvisualvm.id=" + appId);


		VisualVMContext visualVMContext = new VisualVMContext(appId, jdkPath, resolveModule(configuration));
		visualVMContext.save();
		return visualVMContext;
	}

	@Nullable
	public static String getJdkPath(OwnJavaParameters javaParameters) {
		String jdkPath = null;
		try {
			if (javaParameters.getJdk() != null && javaParameters.getJdk().getHomeDirectory() != null) {
				jdkPath = javaParameters.getJdkPath();
			}
		} catch (CantRunException e) {
			// return;
		} catch (Throwable e) {
			log.error(e);
		}
		return jdkPath;
	}

	@SuppressWarnings("rawtypes")
	public static Module resolveModule(RunProfile runProfile) {
		Module runConfigurationModule = null;
		if (runProfile instanceof ModuleBasedConfiguration) {
			ModuleBasedConfiguration configuration = (ModuleBasedConfiguration) runProfile;
			RunConfigurationModule configurationModule = configuration.getConfigurationModule();
			if (configurationModule != null) {
				runConfigurationModule = configurationModule.getModule();
			}
		}
		return runConfigurationModule;
	}

}
