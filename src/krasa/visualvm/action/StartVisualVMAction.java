package krasa.visualvm.action;

import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.impl.DebuggerContextImpl;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;

public class StartVisualVMAction extends AnAction implements AnAction.TransparentUpdate
{

	@Override
	public void actionPerformed(AnActionEvent e)
	{
		final Project project = e.getProject();
		if(project == null)
		{
			return;
		}
		DebuggerContextImpl context = (DebuggerManagerEx.getInstanceEx(project)).getContext();

		final DebuggerSession session = context.getDebuggerSession();
		if(session != null && session.isAttached())
		{
			final DebugProcessImpl process = context.getDebugProcess();
		}
	}


	@Override
	public void update(AnActionEvent e)
	{
		Presentation presentation = e.getPresentation();
		Project project = e.getProject();
		if(project == null)
		{
			presentation.setEnabled(false);
			return;
		}
		DebuggerSession debuggerSession = (DebuggerManagerEx.getInstanceEx(project)).getContext().getDebuggerSession();
		presentation.setEnabled(debuggerSession != null && debuggerSession.isAttached());
	}
}
