
ALTER TABLE  public.comments ALTER COLUMN user_id DROP NOT NULL;
ALTER TABLE  public.tasks ALTER COLUMN owner_id DROP NOT NULL;

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
    DELETE FROM public.notifications WHERE user_id = my_uuid;
    DELETE FROM public.user_project WHERE user_id = my_uuid;
    DELETE FROM public.user_privilege WHERE user_id = my_uuid;
    DELETE FROM public.keys WHERE user_id = my_uuid;
    DELETE FROM public.invitations WHERE user_id = my_uuid or inviter_id = my_uuid;
    DELETE FROM public.users WHERE id = my_uuid;
END;
$$;


