package krasa.visualvm;

import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.ConfigurationException;
import consulo.configurable.StandardConfigurableIds;
import consulo.disposer.Disposable;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.Component;
import consulo.ui.ex.awtUnsafe.TargetAWT;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public class MyConfigurable implements ApplicationConfigurable {
    private SettingsDialog form;

    public static boolean openSettingsIfNotConfigured(Project project) {
        ApplicationSettingsService instance = ApplicationSettingsService.getInstance();
        PluginSettings state = instance.getState();
        boolean ok = true;
        if (!PluginSettings.isValid(state)) {
            // TODO ok = ShowSettingsUtil.getInstance().editConfigurable(project, new MyConfigurable());
        }
        return ok;
    }

    @Override
    @Nls
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("VisualVM Launcher");
    }

    @Nonnull
    @Override
    public String getId() {
        return "visualvm";
    }

    @Nullable
    @Override
    public String getParentId() {
        return StandardConfigurableIds.EXECUTION_GROUP;
    }

    @Override
    public Component createUIComponent(Disposable parentDisposable) {
        if (form == null) {
            form = new SettingsDialog();
        }
        return TargetAWT.wrap(form.getRootComponent());
    }

    @Override
    public boolean isModified() {
        return form.isModified(ApplicationSettingsService.getInstance().getState());
    }

    @Override
    public void apply() throws ConfigurationException {
        PluginSettings settings = ApplicationSettingsService.getInstance().getState();
        if (form != null) {
            form.getData(settings);
        }
    }

    @Override
    public void reset() {
        PluginSettings settings = ApplicationSettingsService.getInstance().getState();
        if (form != null) {
            form.setDataCustom(settings);
        }
    }

    @Override
    public void disposeUIResources() {
        form = null;
    }
}
