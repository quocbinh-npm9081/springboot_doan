alter table public.notifications add column user_id UUID NOT NULL;
alter table public.notifications add column view_at TIMESTAMPTZ;