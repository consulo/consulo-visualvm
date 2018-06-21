package krasa.visualvm.executor;

import javax.annotation.Nonnull;

import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.project.Project;
import consulo.java.module.extension.JavaModuleExtension;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.ui.image.Image;
import krasa.visualvm.VisualVMIcons;

public class RunVisualVMExecutor extends DefaultRunExecutor
{
	public static final String RUN_WITH_VISUAL_VM = "Run with VisualVM";
	public static final String RUN_WITH_VISUAL_VM1 = "RunWithVisualVM";

	@Override
	@Nonnull
	public String getToolWindowId()
	{
		return getId();
	}

	@Override
	public Image getToolWindowIcon()
	{
		return getIcon();
	}

	@Override
	@Nonnull
	public Image getIcon()
	{
		return VisualVMIcons.RUN;
	}

	@Override
	public String getDescription()
	{
		return RUN_WITH_VISUAL_VM;
	}

	@Override
	@Nonnull
	public String getActionName()
	{
		return RUN_WITH_VISUAL_VM1;
	}

	@Override
	@Nonnull
	public String getId()
	{
		return RUN_WITH_VISUAL_VM;
	}

	@Override
	@Nonnull
	public String getStartActionText()
	{
		return RUN_WITH_VISUAL_VM;
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
