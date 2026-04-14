package krasa.visualvm;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.application.Application;
import consulo.logging.Logger;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@ServiceAPI(ComponentScope.APPLICATION)
@ServiceImpl
@Singleton
@State(name = "VisualVMLauncher", storages = {@Storage("VisualVMLauncher.xml")})
public class ApplicationSettingsService implements PersistentStateComponent<PluginSettings>
{
	private static final Logger log = Logger.getInstance(ApplicationSettingsService.class);

	private PluginSettings settings = new PluginSettings();

	public static ApplicationSettingsService getInstance()
	{
		return Application.get().getInstance(ApplicationSettingsService.class);
	}

	@NotNull
	@Override
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
	}
}
