CREATE TABLE public.labels
(
    id                 UUID PRIMARY KEY NOT NULL,
    name               VARCHAR(50),
    color              VARCHAR(10),
    task_id            UUID             NOT NULL,
    is_marked          BOOLEAN          NOT NULL DEFAULT FALSE,
    created_by         VARCHAR(32)      NOT NULL,
    created_date       TIMESTAMPTZ      NOT NULL,
    last_modified_by   VARCHAR(32)      NOT NULL,
    last_modified_date TIMESTAMPTZ      NOT NULL
);

ALTER TABLE public.labels
    ADD CONSTRAINT FK_tasks_to_label FOREIGN KEY (task_id) REFERENCES public.tasks (id);