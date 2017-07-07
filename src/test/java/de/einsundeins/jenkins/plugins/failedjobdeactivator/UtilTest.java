/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Jochen A. Fuerbacher, 1&1 Telecommunication SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.einsundeins.jenkins.plugins.failedjobdeactivator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.FreeStyleProject;

public class UtilTest {

	@Rule
	public JenkinsRule j = new JenkinsRule();

	@Test
	public void testIsInstanceOfAbstractProject() throws IOException {
		FreeStyleProject job = j.createFreeStyleProject();
		String jobName = job.getName();

		assertTrue(Util.isInstanceOfAbstractProject(job));
		assertTrue(Util.isInstanceOfAbstractProject(jobName));

		assertFalse(Util.isInstanceOfAbstractProject("abcdef"));
	}

	@Test
	public void testIsBuildFailureAnalyserAvailable() {
		assertTrue(Util.isBuildFailureAnalyserAvailable());
	}

	@Test
	public void testIsJobConfigHistoryAvailable() {
		assertTrue(Util.isJobConfigHistoryAvailable());
	}

}