Redmine Build Notifier plugin
===========================

Jenkins plugin to add the link to target issue of Redmine, and post build summary to target issue.

What's this?
-------------

Redmine Build Notifier is a [Jenkins](http://jenkins-ci.org/) plugin.

As you know, [Redmine plugin](https://wiki.jenkins-ci.org/display/JENKINS/Redmine+Plugin) for Jenkins already exists, but Redmine plugin needs
changeset info to integrate Jenkins and Redmine.

On one hand, this plugin aims to enable Jenkins build result to associate with target Redmine issue, in case no changeset, no SCM.

Basic Feature
-------------

This plugin provides a post-build step to add link to target issue.
And to post build summary to target issue via Redmine API. (Optionally)

* You can associate a build with target Remine issue, passed as environment variable named
* "REDMINE_ISSUE_ID", though target Redmine project has no SCM.
* And you can set REDMINE_ISSUE_ID via build parameter.
* If a build related to issue, likn to target issue is shown at left side.
* Optionally, you can post build summary to target issue via Redmine API.

TODO
----

* So many...
* Enabled to choice the situation of build to post comment to target issue. (E.g. In case success only..)
* Code should be more simplified, and refactored not to use Redmine Java API in view of the perpose of this plugin...
* Write much better documentation in English..

