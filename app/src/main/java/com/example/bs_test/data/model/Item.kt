package com.example.bs_test.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Hozrot Belal
 */

@Parcelize
data class Item(
    @Json(name = "id")
    val id: Int?,

    @Json(name = "node_id")
    val nodeId: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "full_name")
    val fullName: String? = null,

    @Json(name = "owner")
    val owner: Owner? = null,

    @Json(name = "private")
    val private: Boolean? = null,

    @Json(name = "html_url")
    val html_url: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "fork")
    val fork: Boolean? = null,

    @Json(name = "url")
    val url: String? = null,

    @Json(name = "forks_url")
    val forksUrl: String? = null,

    @Json(name = "keys_url")
    val keysUrl: String? = null,


    @Json(name = "teams_url")
    val teamsUrl: String? = null,

    @Json(name = "hooks_url")
    val hooksUrl: String? = null,

    @Json(name = "issue_events_url")
    val issueEventsUrl: String? = null,

    @Json(name = "events_url")
    val eventsUrl: String? = null,

    @Json(name = "assignees_url")
    val assigneesUrl: String? = null,

    @Json(name = "branches_url")
    val branchesUrl: String? = null,

    @Json(name = "tags_url")
    val tagsUrl: String? = null,

    @Json(name = "blobs_url")
    val blobsUrl: String? = null,

    @Json(name = "git_tags_url")
    val gitTagsUrl: String? = null,

    @Json(name = "git_refs_url")
    val gitRefsUrl: String? = null,

    @Json(name = "trees_url")
    val treesUrl: String? = null,

    @Json(name = "statuses_url")
    val statusesUrl: String? = null,

    @Json(name = "languages_url")
    val languagesUrl: String? = null,

    @Json(name = "stargazers_url")
    val stargazersUrl: String? = null,

    @Json(name = "contributors_url")
    val contributorsUrl: String? = null,

    @Json(name = "subscribers_url")
    val subscribersUrl: String? = null,

    @Json(name = "subscription_url")
    val subscriptionUrl: String? = null,

    @Json(name = "commits_url")
    val commitsUrl: String? = null,

    @Json(name = "git_commits_url")
    val gitCommitsUrl: String? = null,

    @Json(name = "comments_url")
    val commentsUrl: String? = null,

    @Json(name = "issue_comment_url")
    val issueCommentUrl: String? = null,

    @Json(name = "contents_url")
    val contentsUrl: String? = null,

    @Json(name = "compare_url")
    val compareUrl: String? = null,

    @Json(name = "merges_url")
    val mergesUrl: String? = null,

    @Json(name = "archive_url")
    val archiveUrl: String? = null,

    @Json(name = "downloads_url")
    val downloadsUrl: String? = null,

    @Json(name = "issues_url")
    val issuesUrl: String? = null,

    @Json(name = "pulls_url")
    val pullsUrl: String? = null,

    @Json(name = "milestones_url")
    val milestonesUrl: String? = null,

    @Json(name = "notifications_url")
    val notificationsUrl: String? = null,

    @Json(name = "labels_url")
    val labelsUrl: String? = null,

    @Json(name = "releases_url")
    val releasesUrl: String? = null,

    @Json(name = "deployments_url")
    val deploymentsUrl: String? = null,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null,

    @Json(name = "pushed_at")
    val pushedAt: String? = null,

    @Json(name = "git_url")
    val gitUrl: String? = null,

    @Json(name = "ssh_url")
    val sshUrl: String? = null,

    @Json(name = "clone_url")
    val cloneUrl: String? = null,

    @Json(name = "svn_url")
    val svnUrl: String? = null,

    @Json(name = "homepage")
    val homepage: String? = null,

    @Json(name = "size")
    val size: Int? = null,

    @Json(name = "stargazers_count")
    val stargazersCount: Int? = null,

    @Json(name = "watchers_count")
    val watchersCount: Int? = null,

    @Json(name = "language")
    val language: String? = null,

    @Json(name = "has_issues")
    val hasIssues: Boolean? = null,

    @Json(name = "has_projects")
    val hasProjects: Boolean? = null,

    @Json(name = "has_downloads")
    val hasDownloads: Boolean? = null,

    @Json(name = "has_wiki")
    val hasWiki: Boolean? = null,

    @Json(name = "has_pages")
    val hasPages: Boolean? = null,

    @Json(name = "forks_count")
    val forksCount: Int? = null,


    @Json(name = "archived")
    val archived: Boolean? = null,

    @Json(name = "open_issues_count")
    val openIssuesCount: Int? = null,

    @Json(name = "forks")
    val forks: Int? = null,

    @Json(name = "open_issues")
    val openIssues: Int? = null,

    @Json(name = "watchers")
    val watchers: Int? = null,

    @Json(name = "default_branch")
    val defaultBranch: String? = null,

    @Json(name = "network_count")
    val networkCount: Int? = null,

    @Json(name = "subscribers_count")
    val subscribersCount: Int? = null,


    ) : Parcelable {
}