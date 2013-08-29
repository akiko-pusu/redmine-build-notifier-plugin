package org.jenkinsci.plugins.redmine_build_notifier;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Hudson;
import hudson.model.JobProperty;
import java.net.URL;

import hudson.tasks.Shell;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.JenkinsRule;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.*;

public class RedmineBuildNotifierTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testDisplayName() throws Exception {
        RedmineBuildNotifier.DescriptorImpl descriptor = new RedmineBuildNotifier.DescriptorImpl();
        String d = descriptor.getDisplayName();
        String m = Messages.redmine_build_notifier();
        assertEquals(d, Messages.redmine_build_notifier());

        FreeStyleProject project = j.createFreeStyleProject();
        project.getBuildersList().add(new Shell("echo RedmineBuildNotifierTest"));
        FreeStyleBuild build = project.scheduleBuild2(0).get();
        System.out.println(build.getDisplayName() + " completed");
        // TODO: change this to use HtmlUnit
        String s = FileUtils.readFileToString(build.getLogFile());
        assertThat(s, containsString("+ echo RedmineBuildNotifierTest"));
    }
}