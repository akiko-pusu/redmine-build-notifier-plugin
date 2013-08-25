package org.jenkinsci.plugins.redmine_build_notifier;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/* Redmine Java API */
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;

/**
 * Notifier that add link to target issue and posts build summary to target issue on Redmine.
 *
 * @author akiko_pusu
 */
public class RedmineBuildNotifier extends Notifier {

    private String redmineUrl;
    private String redmineApiKey;
    private String shouldPost;

    // TODO: Enable to customize of post message. Now default Redmine wiki formatting.
    private static final String REPORT_FORMAT
            = "h3. %s\n\nJOB_NAME: %s\nBUILD_RESULT: %s\nCAUSE: %s\nBUID_ID: #%d%s\n";

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public RedmineBuildNotifier(String redmineUrl, String redmineApiKey, String shouldPost) {
        if (redmineUrl == null) throw new IllegalArgumentException("Redmine URL cannot be null");
        this.redmineUrl = redmineUrl;
        this.redmineApiKey = redmineApiKey;
        this.shouldPost = shouldPost;
    }

    public String getRedmineUrl() {
        return redmineUrl;
    }

    public String getRedmineApiKey() {
        return redmineApiKey;
    }

    public String getShouldPost() {
        return shouldPost;
    }

    public void setRedmineUrl(String redmineUrl) {
        this.redmineUrl = redmineUrl;
    }

    public void setRedmineApiKey(String redmineApiKey) {
        this.redmineApiKey = redmineApiKey;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        EnvVars envVars = build.getEnvironment(TaskListener.NULL);

        // TODO: Enable to get redmineIssueID not from envVars but some othe way if possible.
        String redmineIssueID = envVars.get("REDMINE_ISSUE_ID");

        if (redmineIssueID == null || redmineIssueID.isEmpty()) {
            listener.getLogger()
                    .println(
                            "[RedmineBuildNotifier] Redmine IssueID not specified. Skip post build task.");
            return true;
        }

        // Do Post
        RedmineManager mgr = new RedmineManager(redmineUrl, redmineApiKey);
        StringBuffer postResult = new StringBuffer();
        try {
            postResult.append(tryGetIssues(mgr,Integer.valueOf(redmineIssueID),generatePostMessage(build)));

            /* Add sidebar link to redmine if success */
            RedmineBuildNotifierAction rpa = new RedmineBuildNotifierAction();
            rpa.setUrl(MessageFormat.format("{0}issues/{1}", redmineUrl, redmineIssueID));
            rpa.setIssueId(redmineIssueID);
            build.addAction(rpa);

        } catch (RedmineException re) {
            postResult.append(re.getMessage());
        } catch (Exception e) {
            postResult.append(e.getStackTrace());
        }

        listener.getLogger().println("[RedmineBuildNotifier] Target Redmine Issue ID: " + redmineIssueID);
        listener.getLogger().println("[RedmineBuildNotifier] Target Redmine Issue Subject: " + postResult.toString());

        return true;
    }

	@Extension
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Publisher> {

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}


		@Override
		public String getDisplayName() {
			return "Post result for Redmine";
		}
	}

    /* For Redmine Code */
    private String tryGetIssues(RedmineManager mgr, Integer issueId, String note) throws Exception {
        Issue issue = mgr.getIssueById(issueId);
        if (this.shouldPost == "true") {
            issue.setNotes(note);
            mgr.update(issue);
        }
        return issue.getSubject();
    }

    private static String generatePostMessage(AbstractBuild<?, ?> build) throws IOException,InterruptedException {
        String projectName = build.getProject().getName();
        String result = build.getResult().toString();

        EnvVars envVars = build.getEnvironment(TaskListener.NULL);
        String buildUrl = envVars.get("BUILD_URL");
        if (buildUrl == null || buildUrl.isEmpty() || buildUrl != "null") {
            buildUrl = " (" + buildUrl + ")";
        } else {
            buildUrl = "";
        }
        String absoluteBuildURL = buildUrl + "console";

        List<Cause> causes = new LinkedList<Cause>();
        CauseAction causeAction = build.getAction(CauseAction.class);
        if (causeAction != null) {
            causes = causeAction.getCauses();
        }

        String causeStr = formatCauses(causes);
        String header;
        header = Messages.header();

        return String.format(REPORT_FORMAT, header, projectName,result,causeStr,
                build.number, buildUrl);
    }

    private static String formatCauses(List<Cause> causes) {
        if (causes.isEmpty()) {
            return "N/A";
        }

        List<String> causeNames = new LinkedList<String>();
        for (Cause cause : causes) {
            causeNames.add(cause.getShortDescription());
        }

        return StringUtils.join(causeNames, ", ");
    }
}
