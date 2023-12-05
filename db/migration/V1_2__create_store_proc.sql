-- Delete permanent project
CREATE OR REPLACE PROCEDURE deletePermanentProject(id_project uuid)
    LANGUAGE plpgsql
AS
$$
declare
    id_task uuid;
BEGIN
    DELETE FROM public.user_project WHERE project_id = id_project;
    DELETE FROM public.invitations WHERE project_id = id_project;
    FOR id_task IN SELECT id FROM public.tasks WHERE project_id = id_project
        LOOP
            DELETE FROM public.attachments WHERE task_id = id_task;
            DELETE FROM public.comments WHERE task_id = id_task;
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
    DELETE FROM public.user_privilege WHERE user_id = my_uuid;
    DELETE FROM public.keys WHERE user_id = my_uuid;
    DELETE FROM public.invitations WHERE user_id = my_uuid;
    DELETE FROM public.users WHERE id = my_uuid;
END;
$$;


