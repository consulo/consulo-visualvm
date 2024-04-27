package consulo.visualvm;

import consulo.annotation.component.ExtensionImpl;
import consulo.localize.LocalizeValue;
import consulo.project.ui.notification.NotificationGroup;
import consulo.project.ui.notification.NotificationGroupContributor;
import jakarta.annotation.Nonnull;

import java.util.function.Consumer;

/**
 * @author VISTALL
 * @since 2024-04-27
 */
@ExtensionImpl
public class VisualVMNotificationGroupContributor implements NotificationGroupContributor
{
	public static final NotificationGroup GROUP = NotificationGroup.balloonGroup("VisualVM", LocalizeValue.localizeTODO("VisualVM"));

	@Override
	public void contribute(@Nonnull Consumer<NotificationGroup> consumer)
	{
		consumer.accept(GROUP);
	}
}
