package krasa.visualvm.action;

import consulo.project.Project;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.Presentation;
import consulo.visualvm.icon.VisualVMIconGroup;
import krasa.visualvm.LogHelper;
import krasa.visualvm.MyConfigurable;
import krasa.visualvm.integration.VisualVMContext;
import krasa.visualvm.integration.VisualVMHelper;

import java.util.LinkedList;
import java.util.List;

public class StartVisualVMConsoleAction extends MyDumbAwareAction {
	private VisualVMContext visualVMContext;
	private boolean postConstructContextSet;
	private long created;

	public static volatile List<StartVisualVMConsoleAction> currentlyExecuted = new LinkedList<StartVisualVMConsoleAction>();

	public StartVisualVMConsoleAction() {
	}

	public StartVisualVMConsoleAction(VisualVMContext visualVMContext) {
		super("Start VisualVM", null, VisualVMIconGroup.console16());
		this.visualVMContext = visualVMContext;
		created = System.currentTimeMillis();
		currentlyExecuted.add(this);
		LogHelper.print("created with " + visualVMContext, this);
	}

	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		final Presentation presentation = e.getPresentation();
		if (!VisualVMContext.isValid(visualVMContext)) {
//			presentation.setVisible(false);
			presentation.setEnabled(false);
		} else {
			presentation.setDescription("Open VisualVM with id=" + visualVMContext.getAppId());
		}
	}

	@Override
	public void actionPerformed(final AnActionEvent e) {
		Project project = e.getData(Project.KEY);
		if (!MyConfigurable.openSettingsIfNotConfigured(project)) {
			return;
		}
		VisualVMHelper.startVisualVM(visualVMContext, project, this);
	}

	public void setVisualVMContext(VisualVMContext visualVMContext) {
		if (postConstructContextSet) {
			LogHelper.print("setVisualVMContext false with " + visualVMContext, this);
		} else {
			postConstructContextSet = true;
			LogHelper.print("setVisualVMContext " + visualVMContext, this);
			this.visualVMContext = visualVMContext;
		}
	}

	public long getCreated() {
		return created;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("StartVisualVMConsoleAction");
		sb.append("{visualVMContext=").append(visualVMContext);
		sb.append(", created=").append(created);
		sb.append('}');
		return sb.toString();
	}
}
