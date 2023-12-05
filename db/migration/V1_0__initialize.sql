CREATE TABLE public.users
(
    id                 UUID PRIMARY KEY NOT NULL,
    first_name         VARCHAR(50),
    last_name          VARCHAR(50),
    username           VARCHAR(50)      NOT NULL,
    password           VARCHAR(100),
    phone_number       VARCHAR(20),
    gender             VARCHAR(10),
    status             VARCHAR(15)      NOT NULL DEFAULT 'INACTIVE',
    role_id            UUID             NOT NULL,
    created_by         VARCHAR(32)      NOT NULL,
    created_date       TIMESTAMPTZ      NOT NULL,
    last_modified_by   VARCHAR(32)      NOT NULL,
    last_modified_date TIMESTAMPTZ      NOT NULL,
    deleted_at         TIMESTAMPTZ
);

CREATE TABLE public.roles
(
    id   UUID PRIMARY KEY   NOT NULL,
    name VARCHAR(60) UNIQUE NOT NULL
);

CREATE TABLE public.privileges
(
    id   UUID PRIMARY KEY   NOT NULL,
    name VARCHAR(60) UNIQUE NOT NULL
);

CREATE TABLE public.role_privilege
(
    role_id      UUID NOT NULL,
    privilege_id UUID NOT NULL
);

CREATE TABLE public.user_privilege
(
    user_id      UUID NOT NULL,
    privilege_id UUID NOT NULL
);

CREATE TABLE public.keys
(
    id           UUID PRIMARY KEY NOT NULL,
    user_id      UUID             NOT NULL,
    key          VARCHAR(32)      NOT NULL,
    expired_time TIMESTAMPTZ,
    used         BOOLEAN,
    action       VARCHAR(50)      NOT NULL
);

CREATE TABLE public.projects
(
    id                 UUID PRIMARY KEY NOT NULL,
    name               VARCHAR(100)     NOT NULL,
    owner_id           UUID             NOT NULL,
    description        VARCHAR(200),
    created_by         VARCHAR(32)      NOT NULL,
    created_date       TIMESTAMPTZ      NOT NULL,
    last_modified_by   VARCHAR(32)      NOT NULL,
    last_modified_date TIMESTAMPTZ      NOT NULL,
    status             VARCHAR(15)      NOT NULL DEFAULT 'CREATED',
    deleted_at         TIMESTAMPTZ
);

CREATE TABLE public.user_project
(
    id         UUID PRIMARY KEY NOT NULL,
    user_id    UUID             NOT NULL,
    project_id UUID             NOT NULL,
    status     VARCHAR(15)      NOT NULL DEFAULT 'WAITING'
);

CREATE TABLE public.invitations
(
    id                 UUID PRIMARY KEY NOT NULL,
    user_id            UUID             NOT NULL,
    inviter_id         UUID             NOT NULL,
    project_id         UUID             NOT NULL,
    key                VARCHAR(32)      NOT NULL,
    created_by         VARCHAR(32)      NOT NULL,
    created_date       TIMESTAMPTZ      NOT NULL,
    last_modified_by   VARCHAR(32)      NOT NULL,
    last_modified_date TIMESTAMPTZ      NOT NULL,
    expired_time       TIMESTAMPTZ,
    used               BOOLEAN
);

CREATE TABLE public.stages
(
    id                 UUID PRIMARY KEY NOT NULL,
    name               VARCHAR(100)     NOT NULL,
    order_number       INTEGER          NOT NULL,
    project_id         UUID             NOT NULL,
    created_date       TIMESTAMP        NOT NULL,
    created_by         VARCHAR          NOT NULL,
    last_modified_date TIMESTAMP        NOT NULL,
    last_modified_by   VARCHAR          NOT NULL
);

CREATE TABLE public.tasks
(
    id                 UUID PRIMARY KEY NOT NULL,
    title              VARCHAR(100)     NOT NULL,
    description        VARCHAR(10000),
    project_id         UUID             NOT NULL,
    stage_id           UUID             NOT NULL,
    owner_id           UUID             NOT NULL,
    assignee_id        UUID,
    next_id            UUID,
    previous_id        UUID,
    created_date       TIMESTAMP        NOT NULL,
    created_by         VARCHAR          NOT NULL,
    last_modified_date TIMESTAMP        NOT NULL,
    last_modified_by   VARCHAR          NOT NULL
);

CREATE TABLE public.attachments
(
    id                 UUID PRIMARY KEY NOT NULL,
    name               VARCHAR(100)     NOT NULL,
    original_name      VARCHAR(100)     NOT NULL,
    task_id            UUID             NOT NULL,
    comment_id         UUID,
    created_date       TIMESTAMP        NOT NULL,
    created_by         VARCHAR          NOT NULL,
    last_modified_date TIMESTAMP        NOT NULL,
    last_modified_by   VARCHAR          NOT NULL
);

CREATE TABLE public.comments
(
    id                 UUID PRIMARY KEY NOT NULL,
    content            VARCHAR(500),
    task_id            UUID             NOT NULL,
    user_id            UUID             NOT NULL,
    parent_comment_id  UUID,
    count_reply        INT              NOT NULL,
    created_date       TIMESTAMP        NOT NULL,
    created_by         VARCHAR          NOT NULL,
    last_modified_date TIMESTAMP        NOT NULL,
    last_modified_by   VARCHAR          NOT NULL
);

ALTER TABLE public.users
    ADD CONSTRAINT FK_users_to_roles FOREIGN KEY (role_id) REFERENCES public.roles (id);

ALTER TABLE public.role_privilege
    ADD CONSTRAINT FK_role_privilege_to_roles FOREIGN KEY (role_id) REFERENCES public.roles (id);

ALTER TABLE public.role_privilege
    ADD CONSTRAINT FK_role_privilege_to_privileges FOREIGN KEY (privilege_id) REFERENCES public.privileges (id);

ALTER TABLE public.user_privilege
    ADD CONSTRAINT FK_user_privilege_to_user FOREIGN KEY (user_id) REFERENCES public.users (id);

ALTER TABLE public.user_privilege
    ADD CONSTRAINT FK_user_privilege_to_privileges FOREIGN KEY (privilege_id) REFERENCES public.privileges (id);

ALTER TABLE public.keys
    ADD CONSTRAINT FK_keys_to_users FOREIGN KEY (user_id) REFERENCES public.users (id);

ALTER TABLE public.user_project
    ADD CONSTRAINT FK_user_to_project FOREIGN KEY (user_id) REFERENCES public.users (id);

ALTER TABLE public.user_project
    ADD CONSTRAINT FK_user_project_to_project FOREIGN KEY (project_id) REFERENCES public.projects (id);

ALTER TABLE public.invitations
    ADD CONSTRAINT FK_invitation_user FOREIGN KEY (user_id) REFERENCES public.users (id);

ALTER TABLE public.invitations
    ADD CONSTRAINT FK_invitation_inviter FOREIGN KEY (inviter_id) REFERENCES public.users (id);

ALTER TABLE public.invitations
    ADD CONSTRAINT FK_invitation_project FOREIGN KEY (project_id) REFERENCES public.projects (id);

ALTER TABLE public.stages
    ADD CONSTRAINT fk_project_to_stage FOREIGN KEY (project_id) REFERENCES public.projects (id);

ALTER TABLE public.tasks
    ADD CONSTRAINT fk_project_to_task FOREIGN KEY (project_id) REFERENCES public.projects (id);

ALTER TABLE public.tasks
    ADD CONSTRAINT fk_stage_to_task FOREIGN KEY (stage_id) REFERENCES public.stages (id);

ALTER TABLE public.tasks
    ADD CONSTRAINT fk_user_to_owner FOREIGN KEY (owner_id) REFERENCES public.users (id);

ALTER TABLE public.tasks
    ADD CONSTRAINT fk_user_to_assignee FOREIGN KEY (assignee_id) REFERENCES public.users (id);

ALTER TABLE public.attachments
    ADD CONSTRAINT fk_task_to_attachments FOREIGN KEY (task_id) REFERENCES public.tasks (id);

ALTER TABLE public.attachments
    ADD CONSTRAINT fk_comment_to_attachments FOREIGN KEY (comment_id) REFERENCES public.comments (id);

ALTER TABLE public.comments
    ADD CONSTRAINT fk_task_to_comments FOREIGN KEY (task_id) REFERENCES public.tasks (id);

ALTER TABLE public.comments
    ADD CONSTRAINT fk_user_to_comments FOREIGN KEY (user_id) REFERENCES public.users (id);

ALTER TABLE public.comments
    ADD CONSTRAINT fk_parent_comment_to_comments FOREIGN KEY (parent_comment_id) REFERENCES public.comments (id);

ALTER TABLE public.projects
    ADD CONSTRAINT fk_project_to_owner FOREIGN KEY (owner_id) REFERENCES public.users (id);
