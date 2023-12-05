-- start roles
INSERT INTO public.roles (id, name)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'ADMINISTRATOR');

INSERT INTO public.roles (id, name)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', 'PROJECT_MANAGER');

INSERT INTO public.roles (id, name)
VALUES ('cd731519-1315-44bf-853d-54d514115251', 'AGENCY');

INSERT INTO public.roles (id, name)
VALUES ('9dd04dc4-ad2c-407b-8176-00c286ceff01', 'TALENT');
-- end roles

-- start users
INSERT INTO public.users (id, first_name, last_name, username, password, status, role_id,
                          created_by, created_date, last_modified_by, last_modified_date)
VALUES ('2eda6ac1-daa5-4eea-b559-c652a38ec83a', 'Luan', 'Tran', 'dh51903224@student.edu.vn',
        '$2a$10$0JL.gFovNIRKOd/VFdyfNOOBw2JW1qf7JBN9WRX7aWeQl8iD3DYPm', 'ACTIVE',
        '81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'system', '2023-01-01 00:00:00+00:00', 'system',
        '2023-01-01 00:00:00+00:00');
-- end users

-- start privileges
-- USER
INSERT INTO public.privileges (id, name)
VALUES ('a782ab68-9edc-47af-9949-df64acaf2d59', 'CREATE_USER');

INSERT INTO public.privileges (id, name)
VALUES ('a232ab68-9edc-47af-9949-df64acaf2d59', 'UPDATE_USER');

INSERT INTO public.privileges (id, name)
VALUES ('a232ab68-9edc-47af-9949-df64acaf2d23', 'VIEW_USER');

INSERT INTO public.privileges (id, name)
VALUES ('a232ab68-9edc-47af-3949-df14acaf2d59', 'DELETE_USER');

-- PROJECT
INSERT INTO public.privileges (id, name)
VALUES ('2078444f-8609-4be3-a57f-ac48824e20a4', 'CREATE_PROJECT');

INSERT INTO public.privileges (id, name)
VALUES ('524de462-03f1-4493-a4c2-7bfe4cec102f', 'VIEW_PROJECT');

INSERT INTO public.privileges (id, name)
VALUES ('78cfdb1c-8d2d-45be-1e62-cb2ab9ec6f80', 'VIEW_MY_PROJECT');

INSERT INTO public.privileges (id, name)
VALUES ('78cfdb1c-8d2d-45be-3e62-cb2ab9ec6f80', 'UPDATE_PROJECT');

INSERT INTO public.privileges (id, name)
VALUES ('78cfdb1c-8d2d-45be-9e62-cb2ab9ec6f80', 'DELETE_PROJECT');

-- PROFILE
INSERT INTO public.privileges (id, name)
VALUES ('78cfdb1c-8d2d-45be-9e62-cb1ab9ec6f80', 'UPDATE_PROFILE');

INSERT INTO public.privileges (id, name)
VALUES ('71cfdb1c-3d2d-45be-9e22-cb1ab9ec6f80', 'VIEW_PROFILE');

-- INVITATION
INSERT INTO public.privileges (id, name)
VALUES ('2078333f-8609-4be3-a57f-ac48824e20a4', 'CREATE_INVITATION');

INSERT INTO public.privileges (id, name)
VALUES ('524de222-03f1-4493-a4c2-7bfe4cec102f', 'VIEW_INVITATION');

INSERT INTO public.privileges (id, name)
VALUES ('78cfdb1c-8d2d-11be-1e62-cb2ab9ec6f80', 'VIEW_MY_INVITATION');

INSERT INTO public.privileges (id, name)
VALUES ('78cfdb1c-8d2d-45be-1e62-cb3ab9ec6f80', 'UPDATE_INVITATION');

INSERT INTO public.privileges (id, name)
VALUES ('78cfdb1c-8d2d-22be-1e62-cb3ab9ec6f80', 'DELETE_INVITATION');

-- start role_privilege
--- start ADMINISTRATOR
INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'a782ab68-9edc-47af-9949-df64acaf2d59');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'a232ab68-9edc-47af-9949-df64acaf2d59');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'a232ab68-9edc-47af-9949-df64acaf2d23');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'a232ab68-9edc-47af-3949-df14acaf2d59');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '2078444f-8609-4be3-a57f-ac48824e20a4');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '524de462-03f1-4493-a4c2-7bfe4cec102f');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '78cfdb1c-8d2d-45be-1e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '78cfdb1c-8d2d-45be-3e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '78cfdb1c-8d2d-45be-9e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '78cfdb1c-8d2d-45be-9e62-cb1ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '71cfdb1c-3d2d-45be-9e22-cb1ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '2078333f-8609-4be3-a57f-ac48824e20a4');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '524de222-03f1-4493-a4c2-7bfe4cec102f');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '78cfdb1c-8d2d-45be-1e62-cb3ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', '78cfdb1c-8d2d-22be-1e62-cb3ab9ec6f80');

--- end ADMINISTRATOR

--- start PROJECT_MANAGER
INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '2078444f-8609-4be3-a57f-ac48824e20a4');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '78cfdb1c-8d2d-45be-1e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '78cfdb1c-8d2d-45be-3e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '78cfdb1c-8d2d-45be-9e62-cb1ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '71cfdb1c-3d2d-45be-9e22-cb1ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '2078333f-8609-4be3-a57f-ac48824e20a4');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '78cfdb1c-8d2d-45be-1e62-cb3ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '78cfdb1c-8d2d-11be-1e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '78cfdb1c-8d2d-11be-1e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', '78cfdb1c-8d2d-45be-9e62-cb2ab9ec6f80');
--- end PROJECT_MANAGER


--- start AGENCY

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', '78cfdb1c-8d2d-45be-1e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', '78cfdb1c-8d2d-45be-9e62-cb1ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', '71cfdb1c-3d2d-45be-9e22-cb1ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', '2078333f-8609-4be3-a57f-ac48824e20a4');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', '78cfdb1c-8d2d-11be-1e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', '78cfdb1c-8d2d-45be-1e62-cb3ab9ec6f80');

--- end AGENCY

--- start TALENT
INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('9dd04dc4-ad2c-407b-8176-00c286ceff01', '78cfdb1c-8d2d-45be-1e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('9dd04dc4-ad2c-407b-8176-00c286ceff01', '78cfdb1c-8d2d-45be-9e62-cb1ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('9dd04dc4-ad2c-407b-8176-00c286ceff01', '71cfdb1c-3d2d-45be-9e22-cb1ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('9dd04dc4-ad2c-407b-8176-00c286ceff01', '78cfdb1c-8d2d-11be-1e62-cb2ab9ec6f80');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('9dd04dc4-ad2c-407b-8176-00c286ceff01', '78cfdb1c-8d2d-45be-1e62-cb3ab9ec6f80');
--- end TALENT

-- end role_privilege

-- start user_privilege
INSERT INTO user_privilege (user_id, privilege_id)
SELECT u.id, p.id
FROM public.users u
         JOIN public.roles r ON u.role_id = r.id
         JOIN public.role_privilege pr ON r.id = pr.role_id
         JOIN public.privileges p ON pr.privilege_id = p.id;
-- end user_privilege

-- ADD PRIVILEGES
    INSERT INTO public.privileges (id, name)
VALUES ('a782ab68-9edc-47af-9949-df64acaf2d12', 'CREATE_TASK');

INSERT INTO public.privileges (id, name)
VALUES ('a782ab68-9edc-47af-9949-df64acaf2d13', 'VIEW_TASK');

INSERT INTO public.privileges (id, name)
VALUES ('a782ab68-9edc-47af-9949-df64acaf2d14', 'UPDATE_TASK');

INSERT INTO public.privileges (id, name)
VALUES ('a782ab68-9edc-47af-9949-df64acaf2d15', 'DELETE_TASK');

-- ADD PRIVILEGES TO ROLE

-- ADMINISTRATOR
INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'a782ab68-9edc-47af-9949-df64acaf2d12');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'a782ab68-9edc-47af-9949-df64acaf2d13');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'a782ab68-9edc-47af-9949-df64acaf2d14');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('81aeddbf-1e5f-4e1f-a99a-e2ac010690f0', 'a782ab68-9edc-47af-9949-df64acaf2d15');

-- PROJECT MANAGER
INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', 'a782ab68-9edc-47af-9949-df64acaf2d12');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', 'a782ab68-9edc-47af-9949-df64acaf2d13');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', 'a782ab68-9edc-47af-9949-df64acaf2d14');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('45a3d83c-d1be-43ae-aeac-ab4da0d6af44', 'a782ab68-9edc-47af-9949-df64acaf2d15');

-- AGENCY
INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', 'a782ab68-9edc-47af-9949-df64acaf2d12');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', 'a782ab68-9edc-47af-9949-df64acaf2d13');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', 'a782ab68-9edc-47af-9949-df64acaf2d14');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('cd731519-1315-44bf-853d-54d514115251', 'a782ab68-9edc-47af-9949-df64acaf2d15');

-- TALENT
INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('9dd04dc4-ad2c-407b-8176-00c286ceff01', 'a782ab68-9edc-47af-9949-df64acaf2d12');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('9dd04dc4-ad2c-407b-8176-00c286ceff01', 'a782ab68-9edc-47af-9949-df64acaf2d13');

INSERT INTO public.role_privilege (role_id, privilege_id)
VALUES ('9dd04dc4-ad2c-407b-8176-00c286ceff01', 'a782ab68-9edc-47af-9949-df64acaf2d14');

--  CREATE PRIVILEGES

-- ADD PRIVILEGE TO ADMINISTRATOR

INSERT INTO public.user_privilege (user_id, privilege_id)
VALUES ('2eda6ac1-daa5-4eea-b559-c652a38ec83a', 'a782ab68-9edc-47af-9949-df64acaf2d12');

INSERT INTO public.user_privilege (user_id, privilege_id)
VALUES ('2eda6ac1-daa5-4eea-b559-c652a38ec83a', 'a782ab68-9edc-47af-9949-df64acaf2d13');

INSERT INTO public.user_privilege (user_id, privilege_id)
VALUES ('2eda6ac1-daa5-4eea-b559-c652a38ec83a', 'a782ab68-9edc-47af-9949-df64acaf2d14');

INSERT INTO public.user_privilege (user_id, privilege_id)
VALUES ('2eda6ac1-daa5-4eea-b559-c652a38ec83a', 'a782ab68-9edc-47af-9949-df64acaf2d15');