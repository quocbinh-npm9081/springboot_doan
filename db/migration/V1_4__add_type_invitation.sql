alter table invitations
    add column action VARCHAR(50) default 'INVITE_TALENT_BY_MAIL' not null;

alter table invitations
    ALTER COLUMN user_id drop not null


--alter table invitations
--    ADD CONSTRAINT FK_invitations_to_users FOREIGN KEY (user_id) REFERENCES public.user (id);