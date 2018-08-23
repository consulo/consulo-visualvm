package krasa.visualvm;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

@Singleton
@State(name = "VisualVMLauncher", storages = @Storage("visual-vm.xml"))
public class PluginSettings implements PersistentStateComponent<PluginSettings>
{
	public static PluginSettings getInstance()
	{
		return ServiceManager.getService(PluginSettings.class);
	}

	private String visualVmExecutable;
	private boolean debug;
	private String durationToSetContextToButton = "5000";
	private String delayForVisualVMStart = "5000";

	public String getVisualVmExecutable()
	{
		return visualVmExecutable;
	}

	public void setVisualVmExecutable(final String visualVmExecutable)
	{
		this.visualVmExecutable = visualVmExecutable;
	}

	public boolean getDebug()
	{
		return debug;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}


	public static boolean isValid(PluginSettings state)
	{
		return state != null && VisualVMHelper.isValidPath(state.getVisualVmExecutable());
	}

	public String getDurationToSetContextToButton()
	{
		return durationToSetContextToButton;
	}

	public void setDurationToSetContextToButton(final String durationToSetContextToButton)
	{
		this.durationToSetContextToButton = durationToSetContextToButton;
	}

	public String getDelayForVisualVMStart()
	{
		return delayForVisualVMStart;
	}

	public void setDelayForVisualVMStart(String delayForVisualVMStart)
	{
		this.delayForVisualVMStart = delayForVisualVMStart;
	}

	public long getDurationToSetContextToButtonAsLong()
	{
		return Long.parseLong(durationToSetContextToButton);
	}

	public long getDelayForVisualVMStartAsLong()
	{
		return Long.parseLong(delayForVisualVMStart);
	}

	@Nullable
	@Override
	public PluginSettings getState()
	{
		return this;
	}

	@Override
	public void loadState(PluginSettings state)
	{
		XmlSerializerUtil.copyBean(state, this);
	}
}
