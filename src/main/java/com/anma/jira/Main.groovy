package com.anma.jira

import com.anma.jira.serv.IssueService

static void main(String[] args) {
    IssueService issueService = new IssueService()

    def issue = issueService.getSubTasks("AAA-2")

    println(issue)
}
