package krasa.visualvm.action;

import consulo.ui.ex.action.DumbAwareAction;
import consulo.ui.image.Image;
import org.jetbrains.annotations.Nullable;

public abstract class MyDumbAwareAction extends DumbAwareAction
{
	public MyDumbAwareAction() {
	}

	public MyDumbAwareAction(@Nullable Image icon) {
		super(icon);
	}

	public MyDumbAwareAction(@Nullable String text) {
		super(text);
	}

	public MyDumbAwareAction(@Nullable  String text, @Nullable String description, @Nullable Image icon) {
		super(text, description, icon);
	}
}
