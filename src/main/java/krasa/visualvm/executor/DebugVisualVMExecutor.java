package krasa.visualvm.executor;

import javax.swing.Icon;

import org.jetbrains.annotations.NonNls;
import javax.annotation.Nonnull;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.project.Project;
import consulo.java.module.extension.JavaModuleExtension;
import consulo.module.extension.ModuleExtensionHelper;
import krasa.visualvm.VisualVMIcons;

public class DebugVisualVMExecutor extends DefaultRunExecutor
{
	@NonNls
	public static final String EXECUTOR_ID = "Debug with VisualVM";
	public static final String DEBUG_WITH_VISUAL_VM = "DebugWithVisualVM";

	@Override
	@Nonnull
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
	@Nonnull
	public Icon getIcon()
	{
		return VisualVMIcons.DEBUG;
	}

	@Override
	public Icon getDisabledIcon()
	{
		return null;
	}

	@Override
	public String getDescription()
	{
		return EXECUTOR_ID;
	}

	@Override
	@Nonnull
	public String getActionName()
	{
		return DEBUG_WITH_VISUAL_VM;
	}

	@Override
	@Nonnull
	public String getId()
	{
		return EXECUTOR_ID;
	}

	@Override
	@Nonnull
	public String getStartActionText()
	{
		return EXECUTOR_ID;
	}

	@Override
	public boolean isApplicable(@Nonnull Project project)
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
