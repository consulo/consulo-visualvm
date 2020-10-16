/*
 * Copyright (c) 2007, 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package krasa.visualvm.integration;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import krasa.visualvm.ApplicationSettingsService;
import krasa.visualvm.LogHelper;
import krasa.visualvm.PluginSettings;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public final class VisualVMHelper {
	private static final Logger log = Logger.getInstance(VisualVMHelper.class.getName());

	public static void startVisualVM(VisualVMContext vmContext, Project project, Object thisInstance) {
		VisualVMHelper.openInVisualVM(vmContext.getAppId(), vmContext.getJdkPath(), vmContext.getModule(), project, thisInstance);
	}

	public static long getNextID() {
		return System.nanoTime();
	}

	public static void startVisualVM(Project project, String jdkHome_doNotOverride) {
		PluginSettings state = ApplicationSettingsService.getInstance().getState();

		String visualVmPath = state.getVisualVmExecutable();
		if (!isValidPath(visualVmPath)) {
			final Notification notification = new Notification("VisualVMLauncher", "",
				"Path to VisualVM is not valid, path='" + visualVmPath + "'",
				NotificationType.ERROR);
			ApplicationManager.getApplication().invokeLater(() -> Notifications.Bus.notify(notification));
		} else {
			try {
				if (StringUtils.isBlank(jdkHome_doNotOverride)) {
					startVisualVMProcess(project, visualVmPath);
				} else {
					startVisualVMProcess(project, visualVmPath, "--jdkhome", jdkHome_doNotOverride);
				}
			} catch (IOException e) {
				throw new RuntimeException("visualVmPath=" + visualVmPath + "; jdkHome=" + jdkHome_doNotOverride, e);
			}
		}

	}

	public static void openInVisualVM(long id, String jdkHome, Module module, Project project, Object thisInstance) {
		PluginSettings pluginSettings = ApplicationSettingsService.getInstance().getState();

		String visualVmPath = pluginSettings.getVisualVmExecutable();
		String customJdkHome = pluginSettings.getJdkHome();
		boolean useModuleJdk = pluginSettings.isUseModuleJdk();
		boolean sourceConfig = pluginSettings.isSourceConfig();

		if (useModuleJdk) {
			if (StringUtils.isBlank(jdkHome)) {
				jdkHome = customJdkHome;
			}
		} else {
			jdkHome = customJdkHome;
		}

		String idString = String.valueOf(id);
		if (pluginSettings.isUseTabIndex()) {
			idString += "@" + pluginSettings.getTabIndex();
		}

		if (!isValidPath(visualVmPath)) {
			final Notification notification = new Notification("VisualVMLauncher", "",
				"Path to VisualVM is not valid, path='" + visualVmPath + "'",
				NotificationType.ERROR);
			ApplicationManager.getApplication().invokeLater(() -> Notifications.Bus.notify(notification));
		} else {
			run(jdkHome, project, visualVmPath, idString, sourceConfig, module, thisInstance);
		}
	}

	private static void run(String jdkHome, Project project, String visualVmPath, String idString, boolean sourceConfig, Module module, Object thisInstance) {
		LogHelper.print("starting VisualVM with id=" + idString, thisInstance);
		List<String> cmds = new ArrayList<>();
		try {
			cmds.add(visualVmPath);
			if (!StringUtils.isBlank(jdkHome)) {
				cmds.add("--jdkhome");
				cmds.add(jdkHome);
			}
			cmds.add("--openid");
			cmds.add(idString);
			if (sourceConfig) {
				try {
					addSourceConfig(project, cmds, module);
				} catch (Throwable e) {
					log.error(e);
				}
			}
//			if (sourceRoots) {
//				try {
//					addSourcePluginParameters(project, cmds, module);
//				} catch (Throwable e) {
//					log.error(e);
//				}
//			}

			startVisualVMProcess(project, cmds.toArray(new String[0]));
		} catch (IOException e) {
			if (sourceConfig) {
				boolean contains = e.getMessage().contains("The filename or extension is too long");
				if (contains) {
					log.error("Please disable 'Integrate with VisualVM-GoToSource plugin' option at 'File | Settings | Other Settings | VisualVM Launcher'.\nThe command was too long: " + cmds.toString().length(), e);
					run(jdkHome, project, visualVmPath, idString, false, module, thisInstance);
					return;
				}
			}
			throw new RuntimeException(cmds.toString(), e);
		}
	}

	private static void startVisualVMProcess(Project project, String... cmds) throws IOException {
		boolean disableProcessDialog = ApplicationSettingsService.getInstance().getState().isDisableProcessDialog();
		log.info("Starting VisualVM with parameters:" + Arrays.toString(cmds));
		ProcessBuilder processBuilder = new ProcessBuilder(cmds);
		if (disableProcessDialog) {
			File file = new File(PathManager.getLogPath(), "visualVMLauncher.log");
			file.createNewFile();
			if (file.exists()) {
				processBuilder.redirectErrorStream(true);
				processBuilder.redirectOutput(file);
			}
		}
		Process process = processBuilder.start();
		if (!disableProcessDialog) {
			process.toHandle().onExit().whenCompleteAsync((processHandle, throwable) -> accept(project, process, processHandle, throwable));
		}
	}

	private static void addSourceConfig(Project project, List<String> cmds, Module runConfigurationModule) throws IOException {
		Properties props = new Properties();
		props.setProperty("source-roots", SourceRoots.resolve(project, runConfigurationModule));

		File ideExecutable = getIdeExecutable();
		if (ideExecutable != null) {
			props.setProperty("source-viewer", ideExecutable.getAbsolutePath());
		}

		File tempFile = FileUtil.createTempFile("visualVmConfig", ".properties");
		try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8")) {
			props.store(osw, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		cmds.add("--source-config");
		cmds.add(tempFile.getAbsolutePath());
	}


	protected static File getIdeExecutable() {
		if (SystemInfo.isWindows) {
			String bits = SystemInfo.is64Bit ? "64" : "";
			return new File(PathManager.getBinPath(), ApplicationNamesInfo.getInstance().getScriptName() + bits + ".exe");
		} else if (SystemInfo.isMac) {
			File appDir = new File(PathManager.getHomePath(), "MacOS");
			return new File(appDir, ApplicationNamesInfo.getInstance().getScriptName());
		} else if (SystemInfo.isUnix) {
			return new File(PathManager.getBinPath(), ApplicationNamesInfo.getInstance().getScriptName() + ".sh");
		} else {
			log.error("invalid OS: " + SystemInfo.getOsNameAndVersion());
			return null;
		}
	}

	public static boolean isValidPath(String visualVmPath) {
		return !StringUtils.isBlank(visualVmPath) && new File(visualVmPath).exists();
	}

	private static void accept(Project project, Process process, ProcessHandle processHandle, Throwable throwable) {
		try {
			if (!processHandle.isAlive()) {
				if (process.exitValue() != 0) {
					String err = new String(process.getErrorStream().readAllBytes(), "UTF-8");
					if (StringUtils.isNotBlank(err)) {
						String message = "VisualVM exited with code: " + process.exitValue() + ".\nError: " + err;
						SwingUtilities.invokeLater(() ->
							Messages.showErrorDialog(project, message, "VisualVM Launcher"));
						log.warn(message);
					}

				}
			}
		} catch (Throwable e) {
			log.warn(e);
		}
	}
}
