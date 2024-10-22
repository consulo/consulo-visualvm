package krasa.visualvm.executor;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.executor.DefaultRunExecutor;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import consulo.visualvm.icon.VisualVMIconGroup;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl(id = "visualVmExecutor", order = "before debugVisualVmExecutor, after run")
public class RunVisualVMExecutor extends DefaultRunExecutor {
	public static final String EXECUTOR_ID = "RunWithVisualVM";

	@Override
	@NotNull
	public String getToolWindowId() {
		return getId();
	}

	@Override
	public Image getToolWindowIcon() {
		return VisualVMIconGroup.run16();
	}

	@Override
	@NotNull
	public Image getIcon() {
		return VisualVMIconGroup.run16();
	}

	@Nonnull
	@Override
	public LocalizeValue getDescription() {
		return getActionName();
	}

	@Override
	@NotNull
	public LocalizeValue getActionName() {
		return LocalizeValue.localizeTODO("Run with VisualVM");
	}

	@Override
	@NotNull
	public String getId() {
		return EXECUTOR_ID;
	}

	@Override
	@NotNull
	public LocalizeValue getStartActionText() {
		return getActionName();
	}

	@Nonnull
	@Override
	public String getContextActionId() {
		// HACK: ExecutorRegistryImpl expects this to be non-null, but we don't want any context actions for every file
		return getId() + " context-action-does-not-exist";
	}
}
