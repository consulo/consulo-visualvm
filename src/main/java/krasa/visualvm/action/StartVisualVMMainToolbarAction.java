package krasa.visualvm.action;

import com.intellij.java.language.projectRoots.JavaSdkType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserFactory;
import consulo.project.Project;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.AnSeparator;
import consulo.ui.ex.action.DefaultActionGroup;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.ui.ex.popup.JBPopupFactory;
import consulo.ui.ex.popup.ListPopup;
import consulo.util.lang.StringUtil;
import consulo.dataContext.DataContext;
import consulo.util.io.FileUtil;
import krasa.visualvm.ApplicationSettingsService;
import krasa.visualvm.PluginSettings;
import krasa.visualvm.integration.VisualVMHelper;
import org.jetbrains.annotations.NotNull;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.util.HashSet;
import java.util.Set;

public class StartVisualVMMainToolbarAction extends MyDumbAwareAction {

	public void actionPerformed(AnActionEvent e) {
		DataContext dataContext = e.getDataContext();
		InputEvent inputEvent = e.getInputEvent();
		Component component = inputEvent == null ? null : inputEvent.getComponent();

		PluginSettings state = ApplicationSettingsService.getInstance().getState();
		if (!StringUtil.isEmptyOrSpaces(state.getVisualVmExecutable())) {
			showJdkPopup(dataContext, component);
			return;
		}

		final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor();
		descriptor.setHideIgnored(true);
		descriptor.setTitle("Select VisualVM Executable");
		FileChooserFactory.getInstance().newBuilder(descriptor).showSingle().whenComplete((path, error) -> {
			if (path == null) {
				return;
			}
			state.setVisualVmExecutable(FileUtil.toSystemIndependentName(path.toString()));
			showJdkPopup(dataContext, component);
		});
	}

	private void showJdkPopup(DataContext dataContext, Component component) {
		DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
		defaultActionGroup.add(new MyDumbAwareAction("No JDK (system default)", null));
//		defaultActionGroup.add(new FocusVisualVMAction("Focus VisualVM", null, null));
		defaultActionGroup.add(new AnSeparator());

		Set<String> homes = jdkHomes();

		homes.stream().sorted().forEach(o -> defaultActionGroup.add(new MyDumbAwareAction(o, o)));

		ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup("Select JDK for --jdkhome VisualVM parameter", defaultActionGroup, dataContext, JBPopupFactory.ActionSelectionAid.ALPHA_NUMBERING, true, (Runnable) null, -1);
		if (component != null) {
			popup.showInCenterOf(component);
		} else {
			popup.showInBestPositionFor(dataContext);

		}
	}

	@NotNull
	private Set<String> jdkHomes() {
		Set<String> homes = new HashSet<>();

		PluginSettings state = ApplicationSettingsService.getInstance().getState();
		String configuredJdkHome = state.getJdkHome();
		if (!StringUtil.isEmptyOrSpaces(configuredJdkHome)) {
			homes.add(configuredJdkHome);
		}

		SdkTable sdkTable = SdkTable.getInstance();
		for (Sdk sdk : sdkTable.getAllSdks()) {
			if (sdk.getSdkType() instanceof JavaSdkType) {
				homes.add(sdk.getHomePath());
			}
		}
		return homes;
	}


	private static class MyDumbAwareAction extends DumbAwareAction
	{
		private final String homePath;

		public MyDumbAwareAction(String name, String homePath) {
			super(name);
			this.homePath = homePath;
		}

		@Override
		public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
			VisualVMHelper.startVisualVM(anActionEvent.getData(Project.KEY), homePath);
		}
	}
}
