package krasa.visualvm;

import com.intellij.execution.CantRunException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.JavaProgramPatcher;
import com.intellij.openapi.diagnostic.Logger;
import consulo.java.execution.configurations.OwnJavaParameters;
import krasa.visualvm.action.StartVisualVMConsoleAction;

public class VisualVMJavaProgramPatcher extends JavaProgramPatcher
{
	private static final Logger log = Logger.getInstance(VisualVMJavaProgramPatcher.class.getName());
	long lastExecution;

	@Override
	public void patchJavaParameters(Executor executor, RunProfile configuration, OwnJavaParameters javaParameters)
	{
		LogHelper.print("#patchJavaParameters start", this);

		if(Hacks.BUNDLED_SERVERS_RUN_PROFILE.equals(configuration.getClass().getName()))
		{
			LogHelper.print("patchJavaParameters com.intellij.javaee.run.configuration.CommonStrategy", this);

			if(System.currentTimeMillis() - lastExecution > 1000)
			{
				LogHelper.print("patchJavaParameters com.intellij.javaee.run.configuration.CommonStrategy patching", this);

				VisualVMContext visualVMContext = patch(javaParameters);
				new StartVisualVMConsoleAction().setVisualVMContextToRecentlyCreated(visualVMContext);
				lastExecution = System.currentTimeMillis();
			}
		}
		else
		{
			patch(javaParameters);
		}
	}

	private VisualVMContext patch(OwnJavaParameters javaParameters)
	{
		String jdkPath = null;
		try
		{
			if(javaParameters.getJdk() != null && javaParameters.getJdk().getHomeDirectory() != null)
			{
				jdkPath = javaParameters.getJdkPath();
			}
		}
		catch(CantRunException e)
		{
			// return;
		}

		final Long appId = VisualVMHelper.getNextID();

		LogHelper.print("Patching: jdkPath=" + jdkPath + "; appId=" + appId, this);
		for(String arg : VisualVMHelper.getJvmArgs(appId))
		{
			javaParameters.getVMParametersList().prepend(arg);
		}
		VisualVMContext visualVMContext = new VisualVMContext(appId, jdkPath);
		visualVMContext.save();
		return visualVMContext;
	}

}
