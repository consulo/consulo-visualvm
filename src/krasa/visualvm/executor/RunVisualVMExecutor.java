package krasa.visualvm.executor;

import javax.swing.Icon;

import org.consulo.java.module.extension.JavaModuleExtension;
import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.module.extension.ModuleExtensionHelper;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.project.Project;
import krasa.visualvm.VisualVMIcons;

public class RunVisualVMExecutor extends DefaultRunExecutor
{

	public static final String RUN_WITH_VISUAL_VM = "Run with VisualVM";
	public static final String RUN_WITH_VISUAL_VM1 = "RunWithVisualVM";

	@Override
	@NotNull
	public String getToolWindowId()
	{
		return getId();
	}

	@Override
	public Icon getToolWindowIcon()
	{
		return getIcon();
	}

	@Override
	@NotNull
	public Icon getIcon()
	{
		return VisualVMIcons.RUN;
	}

	@Override
	public Icon getDisabledIcon()
	{
		return null;
	}

	@Override
	public String getDescription()
	{
		return RUN_WITH_VISUAL_VM;
	}

	@Override
	@NotNull
	public String getActionName()
	{
		return RUN_WITH_VISUAL_VM1;
	}

	@Override
	@NotNull
	public String getId()
	{
		return RUN_WITH_VISUAL_VM;
	}

	@Override
	@NotNull
	public String getStartActionText()
	{
		return RUN_WITH_VISUAL_VM;
	}

	@Override
	public boolean isApplicable(@NotNull Project project)
	{
		return ModuleExtensionHelper.getInstance(project).hasModuleExtension(JavaModuleExtension.class);
	}

	@Override
	public String getContextActionId()
	{
		// HACK: ExecutorRegistryImpl expects this to be non-null, but we don't want any context actions for every file
		return getId() + " context-action-does-not-exist";
	}

	@Override
	public String getHelpId()
	{
		return null;
	}
}
