package krasa.visualvm;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nonnull;
import com.intellij.execution.actions.ConsoleActionsPostProcessor;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.diagnostic.Logger;
import krasa.visualvm.action.StartVisualVMConsoleAction;

public class VisualVMConsoleActionsPostProcessor extends ConsoleActionsPostProcessor
{
	private static final Logger log = Logger.getInstance(VisualVMConsoleActionsPostProcessor.class.getName());

	@Nonnull
	@Override
	public AnAction[] postProcess(@Nonnull ConsoleView console, @Nonnull AnAction[] actions)
	{
		VisualVMContext context = VisualVMContext.load();
		ArrayList<AnAction> anActions = new ArrayList<AnAction>();
		anActions.add(new StartVisualVMConsoleAction(context));
		anActions.addAll(Arrays.asList(actions));
		return anActions.toArray(new AnAction[anActions.size()]);
	}
}
