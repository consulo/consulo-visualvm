package krasa.visualvm.integration;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.ui.console.ConsoleActionsPostProcessor;
import consulo.execution.ui.console.ConsoleView;
import consulo.logging.Logger;
import consulo.ui.ex.action.AnAction;
import krasa.visualvm.action.StartVisualVMConsoleAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

@ExtensionImpl
public class VisualVMConsoleActionsPostProcessor extends ConsoleActionsPostProcessor {
	private static final Logger log = Logger.getInstance(VisualVMConsoleActionsPostProcessor.class);

	@NotNull
	@Override
	public AnAction[] postProcess(@NotNull ConsoleView console, @NotNull AnAction[] actions) {
		VisualVMContext context = VisualVMContext.load();
		ArrayList<AnAction> anActions = new ArrayList<AnAction>();
		anActions.add(new StartVisualVMConsoleAction(context));
		anActions.addAll(Arrays.asList(actions));
		return anActions.toArray(new AnAction[anActions.size()]);
	}
}
