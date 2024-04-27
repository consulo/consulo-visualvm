package krasa.visualvm;

import com.intellij.java.language.projectRoots.JavaSdk;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.IdeaFileChooser;
import consulo.project.Project;
import consulo.project.ProjectManager;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Collection;

public class SettingsDialog
{
	private JTextField visualVmExecutable;
	private JComponent rootComponent;
	private JButton browseButton;
	private JLabel validationMessageLabel;
	private JTextField jdkHome;
	private JButton browseJdkHome;
	private JCheckBox openOnTabForCheckBox;
	private JTextField tabIndex;
	private JCheckBox sourceConfig;
	private JCheckBox useModuleJdk;
	private JTextField laf;

	public SettingsDialog()
	{
		browseButton.addActionListener(e -> browseForFile(visualVmExecutable));
		browseJdkHome.addActionListener(e -> {
			JavaSdk javaSdk = JavaSdk.getInstance();

			String text = jdkHome.getText();
			Collection<String> paths = javaSdk.suggestHomePaths();
			VirtualFile toSelect = null;
			if(StringUtil.isEmptyOrSpaces(text))
			{
				for(String path : paths)
				{
					Path sdkPath = Path.of(path);
					if(Files.exists(sdkPath))
					{
						toSelect = LocalFileSystem.getInstance().findFileByNioFile(sdkPath);
					}
				}
			}
			else
			{
				toSelect = LocalFileSystem.getInstance().findFileByPath(text);
			}

			Project defaultProject = ProjectManager.getInstance().getDefaultProject();
			VirtualFile file = IdeaFileChooser.chooseFile(javaSdk.getHomeChooserDescriptor(), defaultProject, toSelect);
			if(file != null)
			{
				jdkHome.setText(file.getPath());
			}
		});
		visualVmExecutable.getDocument().addDocumentListener(new DocumentListener()
		{

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				updateLabel(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				updateLabel(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				updateLabel(e);
			}

			private void updateLabel(DocumentEvent e)
			{
				java.awt.EventQueue.invokeLater(new Runnable()
				{

					@Override
					public void run()
					{
						setValidationMessage(visualVmExecutable.getText());
					}
				});
			}
		});
	}

	private void browseForFile(@NotNull final JTextField target)
	{
		final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor();
		descriptor.setHideIgnored(true);

		descriptor.setTitle("Select VisualVM Executable");
		String text = target.getText();
		final VirtualFile toSelect = text == null || text.isEmpty() ? null
				: LocalFileSystem.getInstance().findFileByPath(text);

		// 10.5 does not have #chooseFile
		Project defaultProject = ProjectManager.getInstance().getDefaultProject();
		VirtualFile[] virtualFile = IdeaFileChooser.chooseFiles(descriptor, defaultProject, toSelect);
		if(virtualFile != null && virtualFile.length > 0)
		{
			target.setText(virtualFile[0].getPath());
		}
	}

	private void setValidationMessage(String visualVmExecutable1)
	{
		if(StringUtil.isEmptyOrSpaces(visualVmExecutable1))
		{
			validationMessageLabel.setText("Path is required");
		}
		else if(!new File(visualVmExecutable1).exists())
		{
			validationMessageLabel.setText("File does not exists");
		}
		else
		{
			validationMessageLabel.setText("");
		}
	}

	public JComponent getRootComponent()
	{
		return rootComponent;
	}

	public void setDataCustom(PluginSettings settings)
	{
		setData(settings);
		setValidationMessage(settings.getVisualVmExecutable());
	}

	public void setData(PluginSettings data)
	{
		visualVmExecutable.setText(data.getVisualVmExecutable());
		jdkHome.setText(data.getJdkHome());
		openOnTabForCheckBox.setSelected(data.isUseTabIndex());
		tabIndex.setText(data.getTabIndex());
		sourceConfig.setSelected(data.isSourceConfig());
		useModuleJdk.setSelected(data.isUseModuleJdk());
		laf.setText(data.getLaf());
	}

	public void getData(PluginSettings data)
	{
		data.setVisualVmExecutable(visualVmExecutable.getText());
		data.setJdkHome(jdkHome.getText());
		data.setUseTabIndex(openOnTabForCheckBox.isSelected());
		data.setTabIndex(tabIndex.getText());
		data.setSourceConfig(sourceConfig.isSelected());
		data.setUseModuleJdk(useModuleJdk.isSelected());
		data.setLaf(laf.getText());
	}

	public boolean isModified(PluginSettings data)
	{
		if(visualVmExecutable.getText() != null ? !visualVmExecutable.getText().equals(data.getVisualVmExecutable()) : data.getVisualVmExecutable() != null)
		{
			return true;
		}
		if(jdkHome.getText() != null ? !jdkHome.getText().equals(data.getJdkHome()) : data.getJdkHome() != null)
		{
			return true;
		}
		if(openOnTabForCheckBox.isSelected() != data.isUseTabIndex())
		{
			return true;
		}
		if(tabIndex.getText() != null ? !tabIndex.getText().equals(data.getTabIndex()) : data.getTabIndex() != null)
		{
			return true;
		}
		if(sourceConfig.isSelected() != data.isSourceConfig())
		{
			return true;
		}
		if(useModuleJdk.isSelected() != data.isUseModuleJdk())
		{
			return true;
		}
		if(laf.getText() != null ? !laf.getText().equals(data.getLaf()) : data.getLaf() != null)
		{
			return true;
		}
		return false;
	}
}
