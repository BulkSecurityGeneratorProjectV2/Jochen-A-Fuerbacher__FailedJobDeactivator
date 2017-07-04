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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Plugin;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.model.Job;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;

public class FailedJobDeactivator extends Plugin {

	private Logger logger = Logger
			.getLogger(FailedJobDeactivator.class.getName());

	private JobScanner scanner;

	public FailedJobDeactivatorGlobalConfiguration getGlobalConfiguration() {
		return FailedJobDeactivatorGlobalConfiguration.get();
	}

	public void doStartDetection(StaplerRequest req, StaplerResponse rsp)
			throws IOException {

		rsp.sendRedirect("showDetectedJobs");

		try {
			scanner = new JobScanner(
					req.getSubmittedForm().getInt("lastSuccessfulBuild"));
			scanner.startDetection();
		} catch (ServletException e) {
			logger.log(Level.WARNING, "Failed to get submitted form! " + e);
		}
	}

	public List<Job<?, ?>> getDetectedJobs() {
		return scanner.getDetectedJobs();
	}

	public boolean isInstanceOfAbstractProject(String jobName) {
		for (Item item : Jenkins.getInstance().getAllItems()) {
			if (item.getName().equals(jobName)
					&& item instanceof AbstractProject) {
				return true;
			}
		}
		return false;
	}

	public void doHandleJobs(StaplerRequest req, StaplerResponse rsp)
			throws IOException, ServletException {

		rsp.sendRedirect("");
		new JobHandling()
				.performJobHandling(convertJsonToMap(req.getSubmittedForm()));
	}

	private Map<String, String> convertJsonToMap(JSONObject json) {
		Map<String, String> map = new HashMap<>();

		try {
			Iterator<?> jobs = json.keys();
			while (jobs.hasNext()) {
				String jobName = (String) jobs.next();
				String action = (String) json.get(jobName);
				map.put(jobName, action);
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Failed to convert job action json to map. ", e);
		}

		return map;
	}

}
