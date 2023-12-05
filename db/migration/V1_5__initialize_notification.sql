CREATE TABLE public.notifications
(
    id                 UUID PRIMARY KEY NOT NULL,
    notification_type  VARCHAR(100)     NOT NULL,
    read_at            TIMESTAMPTZ              ,
    created_date       TIMESTAMPTZ              ,
    created_by         VARCHAR(100)             ,
    last_modified_by   VARCHAR(32)              ,
    last_modified_date TIMESTAMPTZ              ,
    metadata          JSONB
);




