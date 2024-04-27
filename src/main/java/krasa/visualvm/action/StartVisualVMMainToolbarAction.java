package krasa.visualvm.action;

import com.intellij.java.language.projectRoots.JavaSdk;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.IdeaFileChooser;
import consulo.project.Project;
import consulo.project.ProjectManager;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.AnSeparator;
import consulo.ui.ex.action.DefaultActionGroup;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.ui.ex.popup.JBPopupFactory;
import consulo.ui.ex.popup.ListPopup;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import krasa.visualvm.ApplicationSettingsService;
import krasa.visualvm.PluginSettings;
import krasa.visualvm.integration.VisualVMHelper;
import org.jetbrains.annotations.NotNull;

import java.awt.event.InputEvent;
import java.util.HashSet;
import java.util.Set;

public class StartVisualVMMainToolbarAction extends MyDumbAwareAction {

	public void actionPerformed(AnActionEvent e) {
		boolean ok = checkVisualVmExecutable();
		if (!ok) {
			return;
		}

		DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
		defaultActionGroup.add(new MyDumbAwareAction("No JDK (system default)", null));
//		defaultActionGroup.add(new FocusVisualVMAction("Focus VisualVM", null, null));
		defaultActionGroup.add(new AnSeparator());

		Set<String> homes = jdkHomes();

		homes.stream().sorted().forEach(o -> defaultActionGroup.add(new MyDumbAwareAction(o, o)));

		ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup("Select JDK for --jdkhome VisualVM parameter", defaultActionGroup, e.getDataContext(), JBPopupFactory.ActionSelectionAid.ALPHA_NUMBERING, true, (Runnable) null, -1);
		InputEvent inputEvent = e.getInputEvent();
		if (inputEvent != null) {
			popup.showInCenterOf(inputEvent.getComponent());
		} else {
			popup.showInBestPositionFor(e.getDataContext());

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
		JavaSdk javaSdk = JavaSdk.getInstance();
		for (Sdk sdk : sdkTable.getAllSdks()) {
			if (sdk.getSdkType() == javaSdk) {
				homes.add(sdk.getHomePath());
			}
		}
		return homes;
	}

	private boolean checkVisualVmExecutable() {
		PluginSettings state = ApplicationSettingsService.getInstance().getState();
		String visualVmPath = state.getVisualVmExecutable();
		if (StringUtil.isEmptyOrSpaces(visualVmPath)) {
			final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor();
			descriptor.setHideIgnored(true);

			descriptor.setTitle("Select VisualVM Executable");
			Project defaultProject = ProjectManager.getInstance().getDefaultProject();
			VirtualFile virtualFile = IdeaFileChooser.chooseFile(descriptor, defaultProject, null);
			if (virtualFile != null) {
				String path = virtualFile.getPath();
				state.setVisualVmExecutable(path);
			} else {
				return false;
			}
		}
		return true;
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
