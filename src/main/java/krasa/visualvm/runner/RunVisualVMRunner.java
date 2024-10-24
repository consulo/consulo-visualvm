/*
 * This file is part of VisualVM for IDEA
 *
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *
 *     * Neither the name of the copyright holder nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package krasa.visualvm.runner;

import com.intellij.java.execution.impl.DefaultJavaProgramRunner;
import com.intellij.java.execution.impl.jar.JarApplicationConfiguration;
import com.intellij.java.execution.impl.remote.RemoteConfiguration;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.configuration.ModuleRunProfile;
import consulo.execution.configuration.RunProfile;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.ui.RunContentDescriptor;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import krasa.visualvm.MyConfigurable;
import krasa.visualvm.executor.RunVisualVMExecutor;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public class RunVisualVMRunner extends DefaultJavaProgramRunner
{
	@Override
	@NotNull
	public String getRunnerId() {
		return RunVisualVMExecutor.EXECUTOR_ID;
	}

	public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
		return executorId.equals(RunVisualVMExecutor.EXECUTOR_ID) && (profile instanceof ModuleRunProfile || profile instanceof JarApplicationConfiguration) && !(profile instanceof RemoteConfiguration);
	}

	@Override
	public void execute(@NotNull final ExecutionEnvironment env) throws ExecutionException {
		boolean b = MyConfigurable.openSettingsIfNotConfigured(env.getProject());
		if (!b) {
			return;
		}
		super.execute(env);
	}

	@Override
	protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment env)
			throws ExecutionException {
		RunContentDescriptor runContentDescriptor = super.doExecute(state, env);
		RunnerUtils.runVisualVM(this, env, state);
		return runContentDescriptor;
	}
}
