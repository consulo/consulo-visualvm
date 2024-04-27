package krasa.visualvm.executor;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.executor.DefaultRunExecutor;
import consulo.ui.image.Image;
import consulo.visualvm.icon.VisualVMIconGroup;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl(id = "debugVisualVmExecutor", order = "after debug")
public class DebugVisualVMExecutor extends DefaultRunExecutor {
	@NonNls
	public static final String EXECUTOR_ID = "Debug with VisualVM";
	public static final String DEBUG_WITH_VISUAL_VM = "DebugWithVisualVM";

	@NotNull
	public String getToolWindowId() {
		return getId();
	}

	public Image getToolWindowIcon() {
		return VisualVMIconGroup.debug16();
	}

	@NotNull
	public Image getIcon() {
		return VisualVMIconGroup.debug16();
	}

	public Image getDisabledIcon() {
		return null;
	}

	public String getDescription() {
		return EXECUTOR_ID;
	}

	@NotNull
	public String getActionName() {
		return DEBUG_WITH_VISUAL_VM;
	}

	@NotNull
	public String getId() {
		return EXECUTOR_ID;
	}

	@NotNull
	public String getStartActionText() {
		return EXECUTOR_ID;
	}

	public String getContextActionId() {
		// HACK: ExecutorRegistryImpl expects this to be non-null, but we don't want any context actions for every file
		return getId() + " context-action-does-not-exist";
	}

	public String getHelpId() {
		return null;
	}

}
