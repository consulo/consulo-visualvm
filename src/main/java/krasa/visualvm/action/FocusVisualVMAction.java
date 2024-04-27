package krasa.visualvm.action;

import consulo.project.Project;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.image.Image;
import jakarta.annotation.Nullable;
import krasa.visualvm.integration.VisualVMHelper;

public class FocusVisualVMAction extends MyDumbAwareAction {
	public FocusVisualVMAction() {
	}

	public FocusVisualVMAction(@Nullable String text, @Nullable String description, @Nullable Image icon) {
		super(text, description, icon);
	}

	public void actionPerformed(AnActionEvent e) {
		VisualVMHelper.executeVisualVM(e.getData(Project.KEY),  "--window-to-front");
	}

}
