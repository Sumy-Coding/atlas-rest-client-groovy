package com.anma.jira

import com.anma.jira.srv.IssueService

static void main(String[] args) {

    IssueService issueService = new IssueService()

    String issueKey = "FDCSUP-2845"

    def issue = issueService.getIssue(issueKey)

    println(issue)

}
