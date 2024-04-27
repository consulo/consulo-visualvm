package krasa.visualvm;

import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.ConfigurationException;
import consulo.configurable.StandardConfigurableIds;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@ExtensionImpl
public class MyConfigurable implements ApplicationConfigurable
{
	private SettingsDialog form;

	public static boolean openSettingsIfNotConfigured(Project project)
	{
		ApplicationSettingsService instance = ApplicationSettingsService.getInstance();
		PluginSettings state = instance.getState();
		boolean ok = true;
		if(!PluginSettings.isValid(state))
		{
			// TODO ok = ShowSettingsUtil.getInstance().editConfigurable(project, new MyConfigurable());
		}
		return ok;
	}

	@Nls
	public String getDisplayName()
	{
		return "VisualVM Launcher";
	}

	@Nonnull
	@Override
	public String getId()
	{
		return "visualvm";
	}

	@Nullable
	@Override
	public String getParentId()
	{
		return StandardConfigurableIds.EXECUTION_GROUP;
	}

	public JComponent createComponent()
	{
		if(form == null)
		{
			form = new SettingsDialog();
		}
		return form.getRootComponent();
	}

	public boolean isModified()
	{
		return form.isModified(ApplicationSettingsService.getInstance().getState());
	}

	public void apply() throws ConfigurationException
	{
		PluginSettings settings = ApplicationSettingsService.getInstance().getState();
		if(form != null)
		{
			form.getData(settings);
		}
	}

	public void reset()
	{
		PluginSettings settings = ApplicationSettingsService.getInstance().getState();
		if(form != null)
		{
			form.setDataCustom(settings);
		}
	}

	public void disposeUIResources()
	{
		form = null;
	}
}
