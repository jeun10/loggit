CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    github_id BIGINT NOT NULL UNIQUE,
    github_login VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    display_name VARCHAR(100),
    avatar_url VARCHAR(500),
    timezone VARCHAR(50) NOT NULL DEFAULT 'Asia/Seoul',
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE github_credentials (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    encrypted_access_token TEXT NOT NULL,
    key_version INT NOT NULL DEFAULT 1,
    scopes VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE user_commit_emails (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    UNIQUE (user_id, email)
);

CREATE TABLE projects (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    color VARCHAR(7),
    visibility VARCHAR(20) NOT NULL DEFAULT 'PRIVATE' CHECK (visibility IN ('PRIVATE', 'PUBLIC')),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_projects_user_id ON projects (user_id);

CREATE TABLE git_repositories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    github_repo_id BIGINT NOT NULL,
    owner VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    default_branch VARCHAR(255) NOT NULL,
    is_private BOOLEAN NOT NULL,
    connection_status VARCHAR(20) NOT NULL DEFAULT 'CONNECTED'
        CHECK (connection_status IN ('CONNECTED', 'DISCONNECTED', 'NOT_FOUND')),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    UNIQUE (project_id, github_repo_id)
);

CREATE TABLE tracked_branches (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL REFERENCES git_repositories(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    last_synced_at TIMESTAMPTZ,
    last_sync_status VARCHAR(20) CHECK (last_sync_status IN ('SUCCESS', 'FAILED')),
    created_at TIMESTAMPTZ NOT NULL,
    UNIQUE (repository_id, name)
);

CREATE TABLE commits (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL REFERENCES git_repositories(id) ON DELETE CASCADE,
    sha CHAR(40) NOT NULL,
    message TEXT NOT NULL,
    author_github_id BIGINT,
    author_email VARCHAR(255),
    author_name VARCHAR(255),
    authored_at TIMESTAMPTZ NOT NULL,
    committed_at TIMESTAMPTZ NOT NULL,
    additions INT,
    deletions INT,
    html_url VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,
    UNIQUE (repository_id, sha)
);

CREATE INDEX idx_commits_repository_authored_at ON commits (repository_id, authored_at);
CREATE INDEX idx_commits_author_github_id ON commits (author_github_id);

CREATE TABLE branch_commits (
    branch_id BIGINT NOT NULL REFERENCES tracked_branches(id) ON DELETE CASCADE,
    commit_id BIGINT NOT NULL REFERENCES commits(id) ON DELETE CASCADE,
    PRIMARY KEY (branch_id, commit_id)
);

CREATE INDEX idx_branch_commits_commit_id ON branch_commits (commit_id);

CREATE TABLE task_statuses (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    category VARCHAR(20) NOT NULL CHECK (category IN ('NOT_STARTED', 'IN_PROGRESS', 'DONE', 'CANCELED')),
    display_order INT NOT NULL,
    color VARCHAR(7),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    UNIQUE (user_id, name)
);

CREATE TABLE tasks (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    status_id BIGINT NOT NULL REFERENCES task_statuses(id) ON DELETE RESTRICT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    due_date DATE,
    completed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_tasks_project_status ON tasks (project_id, status_id);
CREATE INDEX idx_tasks_due_date ON tasks (due_date);

CREATE TABLE task_commits (
    task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    commit_id BIGINT NOT NULL REFERENCES commits(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (task_id, commit_id)
);

CREATE INDEX idx_task_commits_commit_id ON task_commits (commit_id);

CREATE TABLE notes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    project_id BIGINT REFERENCES projects(id) ON DELETE SET NULL,
    note_date DATE NOT NULL,
    content TEXT NOT NULL,
    visibility VARCHAR(20) NOT NULL DEFAULT 'PRIVATE' CHECK (visibility IN ('PRIVATE', 'PUBLIC')),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_notes_user_note_date ON notes (user_id, note_date);

CREATE TABLE summaries (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    period_type VARCHAR(20) NOT NULL CHECK (period_type IN ('DAILY', 'WEEKLY')),
    period_start DATE NOT NULL,
    scope VARCHAR(20) NOT NULL CHECK (scope IN ('MINE', 'ALL')),
    rule_content JSONB,
    llm_content TEXT,
    llm_model VARCHAR(100),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'DONE', 'STALE', 'FAILED')),
    generated_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    UNIQUE (project_id, period_type, period_start, scope)
);

CREATE INDEX idx_summaries_status ON summaries (status);
