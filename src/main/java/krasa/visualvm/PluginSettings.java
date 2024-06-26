package krasa.visualvm;

import krasa.visualvm.integration.VisualVMHelper;

public class PluginSettings {

	private String visualVmExecutable;
	private String jdkHome;
	private boolean useTabIndex;
	private String tabIndex = "2";
	private boolean sourceConfig = false;
	private boolean useModuleJdk = true;
	private String laf = "";

	public String getVisualVmExecutable() {
		return visualVmExecutable;
	}

	public void setVisualVmExecutable(final String visualVmExecutable) {
		this.visualVmExecutable = visualVmExecutable;
	}

	public static boolean isValid(PluginSettings state) {
		return state != null && VisualVMHelper.isValidPath(state.getVisualVmExecutable());
	}

	public String getJdkHome() {
		return jdkHome;
	}

	public void setJdkHome(final String jdkHome) {
		this.jdkHome = jdkHome;
	}

	public boolean isUseTabIndex() {
		return useTabIndex;
	}

	public void setUseTabIndex(final boolean useTabIndex) {
		this.useTabIndex = useTabIndex;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(final String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public boolean isSourceConfig() {
		return sourceConfig;
	}

	public void setSourceConfig(final boolean sourceConfig) {
		this.sourceConfig = sourceConfig;
	}

	public boolean isUseModuleJdk() {
		return useModuleJdk;
	}

	public void setUseModuleJdk(final boolean useModuleJdk) {
		this.useModuleJdk = useModuleJdk;
	}


	public String getLaf() {
		return laf;
	}

	public void setLaf(final String laf) {
		this.laf = laf;
	}
}
