package krasa.visualvm;

import javax.annotation.Nullable;
import javax.swing.JComponent;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import consulo.annotations.RequiredDispatchThread;

public class VisualVMConfigurable implements Configurable
{
	private SettingsDialog form;

	public static boolean openSettingsIfNotConfigured(Project project)
	{
		VisualVMConfigurable instance = new VisualVMConfigurable();
		PluginSettings state = PluginSettings.getInstance();
		boolean ok = true;
		if(!PluginSettings.isValid(state))
		{
			ok = ShowSettingsUtil.getInstance().editConfigurable(project, instance);
		}
		return ok;
	}

	private final PluginSettings mySettings;

	public VisualVMConfigurable()
	{
		mySettings = PluginSettings.getInstance();
	}

	@Override
	@Nls
	public String getDisplayName()
	{
		return "VisualVM Launcher";
	}

	@Override
	@Nullable
	@NonNls
	public String getHelpTopic()
	{
		return null;
	}

	@RequiredDispatchThread
	@Override
	public JComponent createComponent()
	{
		if(form == null)
		{
			form = new SettingsDialog();
		}
		return form.getRootComponent();
	}

	@RequiredDispatchThread
	@Override
	public boolean isModified()
	{
		return form.isModified(mySettings);
	}

	@RequiredDispatchThread
	@Override
	public void apply() throws ConfigurationException
	{
		if(form != null)
		{
			form.getData(mySettings);
			LogHelper.debug = mySettings.getDebug();
		}
	}

	@RequiredDispatchThread
	@Override
	public void reset()
	{
		if(form != null)
		{
			form.setDataCustom(mySettings);
		}
	}

	@RequiredDispatchThread
	@Override
	public void disposeUIResources()
	{
		form = null;
	}
}
