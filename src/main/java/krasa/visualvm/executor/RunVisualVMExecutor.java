package krasa.visualvm.executor;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.executor.DefaultRunExecutor;
import consulo.ui.image.Image;
import consulo.visualvm.icon.VisualVMIconGroup;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl(id = "visualVmExecutor", order = "before debugVisualVmExecutor, after run")
public class RunVisualVMExecutor extends DefaultRunExecutor {

	public static final String RUN_WITH_VISUAL_VM = "Run with VisualVM";
	public static final String RUN_WITH_VISUAL_VM1 = "RunWithVisualVM";

	@NotNull
	public String getToolWindowId() {
		return getId();
	}

	public Image getToolWindowIcon() {
		return VisualVMIconGroup.run16();
	}

	@NotNull
	public Image getIcon() {
		return VisualVMIconGroup.run16();
	}

	public Image getDisabledIcon() {
		return null;
	}

	public String getDescription() {
		return RUN_WITH_VISUAL_VM;
	}

	@NotNull
	public String getActionName() {
		return RUN_WITH_VISUAL_VM1;
	}

	@NotNull
	public String getId() {
		return RUN_WITH_VISUAL_VM;
	}

	@NotNull
	public String getStartActionText() {
		return RUN_WITH_VISUAL_VM;
	}

	public String getContextActionId() {
		// HACK: ExecutorRegistryImpl expects this to be non-null, but we don't want any context actions for every file
		return getId() + " context-action-does-not-exist";
	}

	public String getHelpId() {
		return null;
	}
}
