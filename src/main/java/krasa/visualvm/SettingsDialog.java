package krasa.visualvm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import javax.annotation.Nonnull;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

public class SettingsDialog
{
	private JTextField visualVmExecutable;
	private JComponent rootComponent;
	private JButton browseButton;
	private JLabel validationMessageLabel;
	private JCheckBox debugCheckBox;
	private JFormattedTextField duration;
	private JLabel durationLabel;
	private JFormattedTextField delayForStgartingVisualVM;

	public SettingsDialog()
	{
		super();
		duration.setFormatterFactory(getDefaultFormatterFactory());
		delayForStgartingVisualVM.setFormatterFactory(getDefaultFormatterFactory());


		browseButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				browseForFile(visualVmExecutable);
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

	private DefaultFormatterFactory getDefaultFormatterFactory()
	{
		NumberFormatter defaultFormat = new NumberFormatter();
		NumberFormat integerInstance = NumberFormat.getIntegerInstance();
		integerInstance.setGroupingUsed(false);
		defaultFormat.setFormat(integerInstance);
		return new DefaultFormatterFactory(defaultFormat);
	}

	private void browseForFile(@Nonnull final JTextField target)
	{
		final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor();
		descriptor.setHideIgnored(true);

		descriptor.setTitle("Select VisualVM home");
		String text = target.getText();
		final VirtualFile toSelect = text == null || text.isEmpty() ? null : LocalFileSystem.getInstance().findFileByPath(text);

		// 10.5 does not have #chooseFile
		Project defaultProject = ProjectManager.getInstance().getDefaultProject();
		VirtualFile[] virtualFile = FileChooser.chooseFiles(descriptor, defaultProject, toSelect);
		if(virtualFile != null && virtualFile.length > 0)
		{
			target.setText(virtualFile[0].getPath());
		}
	}

	private void setValidationMessage(String visualVmExecutable1)
	{
		if(StringUtil.isEmptyOrSpaces(visualVmExecutable1) )
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

	public void setData(PluginSettings data)
	{
		visualVmExecutable.setText(data.getVisualVmExecutable());
		duration.setText(data.getDurationToSetContextToButton());
		delayForStgartingVisualVM.setText(data.getDelayForVisualVMStart());
		debugCheckBox.setSelected(data.getDebug());
	}

	public void getData(PluginSettings data)
	{
		data.setVisualVmExecutable(visualVmExecutable.getText());
		data.setDurationToSetContextToButton(duration.getText());
		data.setDelayForVisualVMStart(delayForStgartingVisualVM.getText());
		data.setDebug(debugCheckBox.isSelected());
	}

	public boolean isModified(PluginSettings data)
	{
		if(visualVmExecutable.getText() != null ? !visualVmExecutable.getText().equals(data.getVisualVmExecutable()) : data.getVisualVmExecutable() != null)

		{
			return true;
		}
		if(duration.getText() != null ? !duration.getText().equals(data.getDurationToSetContextToButton()) : data.getDurationToSetContextToButton() != null)
		{
			return true;
		}
		if(delayForStgartingVisualVM.getText() != null ? !delayForStgartingVisualVM.getText().equals(data.getDelayForVisualVMStart()) : data.getDelayForVisualVMStart() != null)
		{
			return true;
		}
		if(debugCheckBox.isSelected() != data.getDebug())
		{
			return true;
		}
		return false;
	}

	public void setDataCustom(PluginSettings settings)
	{
		setData(settings);
		setValidationMessage(settings.getVisualVmExecutable());
	}
}
