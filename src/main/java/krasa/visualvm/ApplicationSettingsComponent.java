package krasa.visualvm;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import javax.swing.JComponent;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

@State(name = "VisualVMLauncher", storages = @Storage("VisualVMLauncher.xml"))
public class ApplicationSettingsComponent implements ApplicationComponent, Configurable, PersistentStateComponent<PluginSettings>
{
	private static final Logger log = Logger.getInstance(ApplicationSettingsComponent.class.getName());

	private PluginSettings settings = new PluginSettings();
	private SettingsDialog form;

	public static ApplicationSettingsComponent getInstance()
	{
		return ApplicationManager.getApplication().getComponent(ApplicationSettingsComponent.class);
	}

	public static boolean openSettingsIfNotConfigured(Project project)
	{
		ApplicationSettingsComponent instance = getInstance();
		PluginSettings state = instance.getState();
		boolean ok = true;
		if(!PluginSettings.isValid(state))
		{
			ok = ShowSettingsUtil.getInstance().editConfigurable(project, instance);
		}
		return ok;
	}

	public String getVisualVmHome()
	{
		return settings.getVisualVmExecutable();
	}

	// ApplicationComponent

	@Override
	@Nonnull
	public String getComponentName()
	{
		return "VisualVMLauncher";
	}

	@Override
	public void initComponent()
	{
	}

	@Override
	public void disposeComponent()
	{
	}

	// Configurable

	@Override
	@Nls
	public String getDisplayName()
	{
		return "VisualVM Launcher";
	}

	@Nullable
	public Icon getIcon()
	{
		return null;
	}

	@Override
	@Nullable
	@NonNls
	public String getHelpTopic()
	{
		return null;
	}

	@Override
	public JComponent createComponent()
	{
		if(form == null)
		{
			form = new SettingsDialog();
		}
		return form.getRootComponent();
	}

	@Override
	public boolean isModified()
	{
		return form.isModified(settings);
	}

	@Override
	public void apply() throws ConfigurationException
	{
		if(form != null)
		{
			form.getData(settings);
			LogHelper.debug = settings.getDebug();
		}
	}

	@Override
	public void reset()
	{
		if(form != null)
		{
			form.setDataCustom(settings);
		}
	}

	private SettingsDialog getForm()
	{
		if(form == null)
		{
			form = new SettingsDialog();
		}
		return form;
	}

	@Override
	public void disposeUIResources()
	{
		form = null;
	}

	@Override
	@Nonnull
	public PluginSettings getState()
	{

		if(settings == null)
		{
			settings = new PluginSettings();
		}
		return settings;
	}

	@Override
	public void loadState(PluginSettings state)
	{
		this.settings = state;
		LogHelper.debug = settings.getDebug();
	}
}
