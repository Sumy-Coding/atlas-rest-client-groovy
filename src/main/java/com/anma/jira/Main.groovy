package com.anma.jira

import com.anma.jira.serv.IssueService

static void main(String[] args) {
    IssueService issueService = new IssueService()

    def issue = issueService.getIssue("AAA-1")

    println(issue)
}
