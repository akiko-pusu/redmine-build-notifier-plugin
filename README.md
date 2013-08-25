Redmine Build Notifier plugin
===========================

Jenkins plugin to add link to target Redmine issue for each build, and post build summary to
target issue.

What's this?
-------------

Redmine Build Notifier is a [Jenkins](http://jenkins-ci.org/) plugin.
This plugin provides a post-build step to add link to target issue.
And to post build summary to target issue via Redmine API.
(As you know, Redmine plugin is already released, but Redmine plugin need some scm commit
informations to relate isues.)

* You can associate a build with target Remine issue, passed as environment variable named
* "REDMINE_ISSUE_ID", though target Redmine project has no SCM.
* And you can set REDMINE_ISSUE_ID via build parameter.
* If a build related to issue, likn to target issue is shown at left side.
* Optionally, you can post build summary to target issue via Redmine API.

TODO
----

* So many...

