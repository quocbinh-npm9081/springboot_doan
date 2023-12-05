ALTER TABLE  public.attachments DROP COLUMN comment_id;

ALTER TABLE public.comments
    ALTER COLUMN content TYPE text;

ALTER TABLE public.tasks
    ALTER COLUMN description TYPE text;