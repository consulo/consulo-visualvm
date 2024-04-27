package krasa.visualvm.action;

import com.intellij.java.execution.runners.ProcessProxyFactory;
import consulo.process.ProcessHandler;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.Presentation;
import consulo.ui.image.Image;

public abstract class LauncherBasedAction extends MyDumbAwareAction {
	protected final ProcessHandler myProcessHandler;

	LauncherBasedAction(String text, String description, Image icon, ProcessHandler processHandler) {
		super(text, description, icon);
		myProcessHandler = processHandler;
	}

	@Override
	public void update(final AnActionEvent event) {
		final Presentation presentation = event.getPresentation();
		if (!isVisible()) {
			presentation.setVisible(false);
			presentation.setEnabled(false);
			return;
		}
		presentation.setVisible(true);
		presentation.setEnabled(!myProcessHandler.isProcessTerminated());
	}

	protected boolean isVisible() {
		return ProcessProxyFactory.getInstance().getAttachedProxy(myProcessHandler) != null;
	}
}
