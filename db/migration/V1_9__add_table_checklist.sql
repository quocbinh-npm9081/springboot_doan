CREATE TABLE public.check_lists
(
    id                 UUID PRIMARY KEY NOT NULL,
    name               VARCHAR(50),
    task_id            UUID             NOT NULL,
    created_by         VARCHAR(32)      NOT NULL,
    created_date       TIMESTAMPTZ      NOT NULL,
    last_modified_by   VARCHAR(32)      NOT NULL,
    last_modified_date TIMESTAMPTZ      NOT NULL
);

CREATE TABLE public.check_list_items
(
    id                 UUID PRIMARY KEY NOT NULL,
    check_list_id      UUID             NOT NULL,
    assignee_id        UUID,
    content            VARCHAR(500)     NOT NULL,
    is_done            BOOLEAN          NOT NULL DEFAULT FALSE,
    due_date           TIMESTAMPTZ,
    created_by         VARCHAR(32)      NOT NULL,
    created_date       TIMESTAMPTZ      NOT NULL,
    last_modified_by   VARCHAR(32)      NOT NULL,
    last_modified_date TIMESTAMPTZ      NOT NULL
);

ALTER TABLE public.check_lists
    ADD CONSTRAINT FK_tasks_to_check_list FOREIGN KEY (task_id) REFERENCES public.tasks (id);

ALTER TABLE public.check_list_items
    ADD CONSTRAINT FK_check_list_to_check_list_item FOREIGN KEY (check_list_id) REFERENCES public.check_lists (id);

ALTER TABLE public.check_list_items
    ADD CONSTRAINT FK_user_to_check_list_item FOREIGN KEY (assignee_id) REFERENCES public.users (id);

-- Delete permanent project
CREATE OR REPLACE PROCEDURE deletePermanentProject(id_project uuid)
    LANGUAGE plpgsql
AS
$$
declare
    id_task       uuid;
    id_check_list uuid;
BEGIN
    DELETE FROM public.user_project WHERE project_id = id_project;
    DELETE FROM public.invitations WHERE project_id = id_project;
    FOR id_task IN SELECT id FROM public.tasks WHERE project_id = id_project
        LOOP
            DELETE FROM public.attachments WHERE task_id = id_task;
            DELETE FROM public.comments WHERE task_id = id_task;
            FOR id_check_list IN SELECT id FROM public.check_lists WHERE task_id = id_task
                LOOP
                    DELETE FROM public.check_list_items WHERE check_list_id = id_check_list;
                END LOOP;
            DELETE FROM public.check_lists WHERE task_id = id_task;
        END LOOP;
    DELETE FROM public.tasks WHERE project_id = id_project;
    DELETE FROM public.stages WHERE project_id = id_project;
    DELETE FROM public.projects WHERE id = id_project;
END;
$$;

-- Delete permanent account
CREATE OR REPLACE PROCEDURE deletePermanentAccount(my_uuid uuid)
    LANGUAGE plpgsql
AS
$$
declare
    id_project uuid;
BEGIN
    FOR id_project IN SELECT id FROM public.projects WHERE owner_id = my_uuid
        LOOP
            CALL deletePermanentProject(id_project);
        END LOOP;
    UPDATE  public.tasks SET owner_id = NULL WHERE owner_id = my_uuid;
    UPDATE  public.tasks SET assignee_id = NULL WHERE assignee_id = my_uuid;
    UPDATE  public.comments SET user_id = NULL WHERE user_id = my_uuid;
    UPDATE  public.check_list_items SET assignee_id = NULL WHERE assignee_id = my_uuid;
    DELETE FROM public.notifications WHERE user_id = my_uuid;
    DELETE FROM public.user_project WHERE user_id = my_uuid;
    DELETE FROM public.user_privilege WHERE user_id = my_uuid;
    DELETE FROM public.keys WHERE user_id = my_uuid;
    DELETE FROM public.invitations WHERE user_id = my_uuid or inviter_id = my_uuid;
    DELETE FROM public.users WHERE id = my_uuid;
END;
$$;