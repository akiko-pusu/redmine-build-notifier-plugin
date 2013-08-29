Redmine Build Notifier plugin
===========================

このプラグインは、ビルドの結果を、Redmineの指定のチケットと関連付けするためのプラグインです。
また、ビルドのサマリをRedmineの指定のチケットにコメントとしてPOSTします。

どんな特徴があるの？
-------------

Redmine Build Notifier は、 [Jenkins](http://jenkins-ci.org/) のプラグインです。

ご存知の通り、すでに [Redmine plugin](https://wiki.jenkins-ci.org/display/JENKINS/Redmine+Plugin) というプラグインがリリースされています。
双方の違いは、前者がリポジトリの変更を受けて、コミットログに記載されたチケット情報をもとにRedmineとJenkinsの関連付けを行うのに対し、
本プラグンは、ビルドパラメータとしてRedmineのチケットIDを渡すことで、対象のチケットとJenkinsのビルドの結果との関連づけをします。

大きな違いは、『リポジトリを持たないRedmineからも、Jenkinsのジョブの記録との関連付けができる』点になります。

基本的な機能
-------------

このプラグインでは、ビルド実行後に、指定のチケットへのリンクを作成します。
また、設定オプションによって、Redmine REST API経由でジョブの結果のサマリを指定のチケットにPOSTします。

* ビルドとチケットは、"REDMINE_ISSUE_ID" という名前の環境変数を指定することで、関連付けをします。
* また、"REDMINE_ISSUE_ID" をビルドパラメータとして設定すれば、ビルドごとに異なるチケットとの関連付けが設定できます。
* チケットIDの指定が無い場合は、なにも起こりません。
* チケットとの関連付けが明示されると、ビルドの左サイドバーに、チケットへのリンクが生成されます。
* 設定画面で、ビルド結果をチケットにPOSTするにチェックを入れると、チケットにもビルドのサマリがコメントとしてPOSTされます。

TODO
----

* たくさん...
* Enabled to choice the situation of build to post comment to target issue. (E.g. In case success only..)
* Code should be more simplified, and refactored not to use Redmine Java API in view of the perpose of this plugin...
* Write much better documentation in English..

